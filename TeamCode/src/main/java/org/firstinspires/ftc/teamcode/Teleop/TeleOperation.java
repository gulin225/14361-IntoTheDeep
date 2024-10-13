package org.firstinspires.ftc.teamcode.Teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting.AprilTagDrive;
import org.firstinspires.ftc.teamcode.Autonomous.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

import java.util.concurrent.TimeUnit;


@Config
@TeleOp(name = "test")
public class TeleOperation extends LinearOpMode {
    Servo wrist;
    VerticalSlides verticalSlides;
    final double one = 1;
    final double two=1;
    final double three=1;
    final double four=1;
    ElapsedTime timer = new ElapsedTime();
    DcMotorEx leftFront,leftBack,rightBack,rightFront;
    DcMotorEx backLeftSlide, backRightSlide, frontLeftSlide, frontRightSlide;
    @Override
    public void runOpMode() throws InterruptedException {
       waitForStart();
        backLeftSlide = hardwareMap.get(DcMotorEx.class,"leftBackS");
        backRightSlide = hardwareMap.get(DcMotorEx.class,"rightBackS");
        frontLeftSlide = hardwareMap.get(DcMotorEx.class,"leftFrontS");
        frontRightSlide = hardwareMap.get(DcMotorEx.class,"rightFrontS");
        frontLeftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightSlide.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");

        wrist = hardwareMap.servo.get("wrist");
        timer.reset();
       while (!isStopRequested() && opModeIsActive()){
           double test = AprilTagDrive.PARAMS.kA;
           wrist.setPosition(test);
           telemetry.update();
       }

    }
}
