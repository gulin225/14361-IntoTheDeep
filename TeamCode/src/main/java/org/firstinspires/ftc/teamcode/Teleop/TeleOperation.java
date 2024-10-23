package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Autonomous.RRdrives.PinpointDrive;
import org.firstinspires.ftc.teamcode.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Subsystems.LinearRail;
import org.firstinspires.ftc.teamcode.Subsystems.Robot;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;


@Config
@TeleOp(name = "tele op")
public class TeleOperation extends LinearOpMode {
    Robot robot;
    private GamepadEx driver, controller;
    PinpointDrive drive;
    public enum states{
        outtakingSpecimen, outtakingBucket, drivingToSubmersible, intaking, intakeSpecimen
    }
    states currentState = states.intaking;
    states switchState = null;

    @Override
    public void runOpMode() throws InterruptedException {
       robot = new Robot(hardwareMap, telemetry);
       driver = new GamepadEx(gamepad2);
       controller = new GamepadEx(gamepad1);

       waitForStart();

       robot.init();

       while (!isStopRequested() && opModeIsActive()){
           driver.readButtons();
           controller.readButtons();
           robot.driveTrain.fieldCentric(driver);
           robot.verticalSlides.PIDLoop();
           if (gamepad2.square) robot.verticalSlides.resetEncoder();

           switch (currentState){
               case outtakingBucket:
                    robot.setClaw(gamepad1);
                    robot.setArm_Rail(gamepad1, currentState);

                    switchState = robot.setVerticalSlides(gamepad1);
                    if (switchState != null) currentState = switchState;
                    break;
               case outtakingSpecimen:
                   robot.setClaw(gamepad1);

                   switchState = robot.setVerticalSlides(gamepad1);
                   if (switchState != null) currentState = switchState;
                   break;
               case intakeSpecimen:
                   robot.setClaw(gamepad1);

                   switchState = robot.setVerticalSlides(gamepad1);
                   if (switchState != null) currentState = switchState;
                   break;
               case drivingToSubmersible:
                   robot.setClaw(gamepad1);
                   robot.setArm_Rail(gamepad1, currentState);

                   switchState = robot.setVerticalSlides(gamepad1);
                   if (switchState != null) currentState = switchState;
                   break;
           }

            telemetry.update();
       }

    }

}
