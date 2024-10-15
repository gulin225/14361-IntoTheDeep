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
import org.firstinspires.ftc.teamcode.Subsystems.LinearRail;
import org.firstinspires.ftc.teamcode.Subsystems.Robot;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

import java.util.concurrent.TimeUnit;


@Config
@TeleOp(name = "test")
public class TeleOperation extends LinearOpMode {
    Robot robot;
    @Override
    public void runOpMode() throws InterruptedException {
       robot = new Robot(hardwareMap, telemetry);

       waitForStart();
       robot.init();

       while (!isStopRequested() && opModeIsActive()){
           if (gamepad2.left_bumper) robot.intake();
           if (gamepad2.right_bumper) robot.outtake();
           if (gamepad2.dpad_down) robot.linearRail.moveRail(LinearRail.linearRailStates.middle);

           if (gamepad1.dpad_down)//low
           if (gamepad1.dpad_left)//middle
           if (gamepad1.dpad_up)//third
           if (gamepad1.dpad_right)//fourth

           if (gamepad1.left_bumper) robot.claw.moveClaw(Claw.clawStates.open);
           if (gamepad1.right_bumper) robot.claw.moveClaw(Claw.clawStates.close);

           telemetry.update();
       }

    }
}
