package org.firstinspires.ftc.teamcode.Teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Subsystems.Claw;


@Config
@TeleOp(name = "test")
public class TeleOperation extends LinearOpMode {
    Claw claw;
    DcMotorEx frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;
    @Override
    public void runOpMode() throws InterruptedException {
        //claw = new Claw(hardwareMap);

        telemetry.addLine("Hello");
        telemetry.update();

        backRightMotor = hardwareMap.get(DcMotorEx.class,"rightBack");
        frontRightMotor = hardwareMap.get(DcMotorEx.class,"rightFront");
        backLeftMotor = hardwareMap.get(DcMotorEx.class,"leftBack");
        frontLeftMotor = hardwareMap.get(DcMotorEx.class,"leftFront");
        backLeftMotor.setDirection(DcMotorEx.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorEx.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorEx.Direction.REVERSE);
        waitForStart();

       while (!isStopRequested() && opModeIsActive()){

           frontLeftMotor.setPower(1);
           backLeftMotor.setPower(1);
           frontRightMotor.setPower(1);
           backRightMotor.setPower(1);
           telemetry.update();

       }

    }
}
