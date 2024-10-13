package org.firstinspires.ftc.teamcode.Subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.roadrunner.ftc.LazyImu;
//import com.arcrobotics.ftclib.command.Subsystem;
//import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class DriveTrain
{
    private DcMotorEx leftFront,leftBack,rightBack,rightFront;
    private double offset = 1;
    public final LazyImu lazyImu;
    IMU imu;
    BNO055IMU.Parameters parameters;
    private double x, y, rx, rotX, rotY, denominator, frontLeftPower, backLeftPower, frontRightPower, backRightPower;
    public double slow_mode, botHeading ;
    public RevHubOrientationOnRobot.LogoFacingDirection logoFacingDirection =
            RevHubOrientationOnRobot.LogoFacingDirection.UP;
    public RevHubOrientationOnRobot.UsbFacingDirection usbFacingDirection =
            RevHubOrientationOnRobot.UsbFacingDirection.LEFT;

    public DriveTrain(HardwareMap hardwareMap)
    {
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");

        lazyImu = new LazyImu(hardwareMap, "imu", new RevHubOrientationOnRobot(
                logoFacingDirection, usbFacingDirection));
        imu = lazyImu.get();
    }

    public void resetIMU() {
        imu.resetYaw();
    }

    public void fieldCentric(/*GamepadEx driver*/){
        //y = driver.getLeftY();
        //x = driver.getLeftX();
        //rx = -driver.getRightX();
        botHeading = imu.getRobotYawPitchRollAngles().getYaw();

        rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
        rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);
        denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);

        frontLeftPower = 1 * (rotY + rotX + rx) / denominator;
        backLeftPower = 1 * (rotY - rotX + rx) / denominator;
        frontRightPower = 1 * (rotY - rotX - rx) / denominator;
        backRightPower = 1 * (rotY + rotX - rx) / denominator;

        leftFront.setPower(frontLeftPower*slow_mode);
        leftBack.setPower(backLeftPower*slow_mode);
        rightFront.setPower(frontRightPower*slow_mode);
        rightBack.setPower(backRightPower*slow_mode);
    }





}