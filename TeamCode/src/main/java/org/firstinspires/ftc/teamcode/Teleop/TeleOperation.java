package org.firstinspires.ftc.teamcode.TeleOp;

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
           robot.driveTrain.fieldCentric(gamepad2, telemetry);
           robot.verticalSlides.PIDLoop();

           if (gamepad1.square) robot.intake();
           if (gamepad1.circle) robot.outtake();

           if((gamepad1.right_trigger > .2)) robot.claw.moveClaw(Claw.clawStates.spinOn);
           else  robot.claw.moveClaw(Claw.clawStates.spinOff);
           if(gamepad2.b) {
                robot.claw.moveClaw(Claw.clawStates.outtake);
                robot.claw.moveClaw(Claw.clawStates.spinOff);
                robot.claw.moveClaw(Claw.clawStates.wristOuttake);
            }
           if (gamepad1.dpad_down) {

               robot.verticalSlides.setSlides(VerticalSlides.slideStates.intake);
           }
           if (gamepad1.dpad_left) {
               robot.outtake();
               robot.verticalSlides.setSlides(VerticalSlides.slideStates.lowBasket);
           }
           if (gamepad1.dpad_up) {
               robot.outtake();
               robot.verticalSlides.setSlides(VerticalSlides.slideStates.highRung);
           }
           if (gamepad1.dpad_right) {
               robot.outtake();
               robot.verticalSlides.setSlides(VerticalSlides.slideStates.highBasket);
           }

           if (gamepad1.left_bumper) robot.claw.moveClaw(Claw.clawStates.open);
           if (gamepad1.right_bumper) robot.claw.moveClaw(Claw.clawStates.close);


       }

    }
}
