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
    DcMotorEx backLeftSlide, backRightSlide, frontLeftSlide, frontRightSlide;
    @Override
    public void runOpMode() throws InterruptedException {
        //claw = new Claw(hardwareMap);

        telemetry.addLine("Hello");
        telemetry.update();

        //backRightMotor = hardwareMap.get(DcMotorEx.class,"rightBack");
        //frontRightMotor = hardwareMap.get(DcMotorEx.class,"rightFront");
        //backLeftMotor = hardwareMap.get(DcMotorEx.class,"leftBack");
        backLeftSlide = hardwareMap.get(DcMotorEx.class,"test");
        backRightSlide = hardwareMap.get(DcMotorEx.class,"test2");
        frontLeftSlide = hardwareMap.get(DcMotorEx.class,"test3");
        frontRightSlide = hardwareMap.get(DcMotorEx.class,"test4");
        frontRightSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();

       while (!isStopRequested() && opModeIsActive()){

           frontLeftSlide.setPower(1);
           frontRightSlide.setPower(1);
           backLeftSlide.setPower(1);
           backRightSlide.setPower(1);
           telemetry.update();

       }

    }
}
