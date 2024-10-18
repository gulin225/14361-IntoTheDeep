package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Subsystems.LinearRail;
import org.firstinspires.ftc.teamcode.Subsystems.Robot;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;


@Config
@TeleOp(name = "tele op")
public class TeleOperation extends LinearOpMode {
    Robot robot;
    private GamepadEx driver, controller;
    double total = 0;

    @Override
    public void runOpMode() throws InterruptedException {
       robot = new Robot(hardwareMap, telemetry);
        driver = new GamepadEx(gamepad2);
        controller = new GamepadEx(gamepad2);
       waitForStart();
       robot.init();

       while (!isStopRequested() && opModeIsActive()){
           driver.readButtons();
           controller.readButtons();

           robot.driveTrain.fieldCentric(driver);
           robot.verticalSlides.PIDLoop();

           if (gamepad1.square) {
               robot.claw.moveClaw(Claw.clawStates.intakeSubmersible);
               robot.linearRail.moveRail(LinearRail.linearRailStates.intake);
           }
           if (gamepad1.triangle) {
               robot.linearRail.moveRail(LinearRail.linearRailStates.middle);
           }
           if (gamepad1.circle) robot.outtake();

           if((gamepad1.right_trigger > .2)) robot.claw.moveClaw(Claw.clawStates.spinOn);
           else  robot.claw.moveClaw(Claw.clawStates.spinOff);
           if(gamepad2.b) {
                robot.claw.moveClaw(Claw.clawStates.outtake);
                robot.claw.moveClaw(Claw.clawStates.spinOff);
            }
           if (gamepad1.dpad_down) {
               robot.intake();
               robot.verticalSlides.setSlides(VerticalSlides.slideStates.intake);
           }
           if (gamepad1.dpad_left) {
               robot.intake();
               robot.verticalSlides.setSlides(VerticalSlides.slideStates.lowBasket);
           }
           if (gamepad1.dpad_up) {
               robot.intake();
               robot.verticalSlides.setSlides(VerticalSlides.slideStates.highRung);
           }
           if (gamepad1.dpad_right) {
               robot.outtake();
               robot.verticalSlides.setSlides(VerticalSlides.slideStates.highBasket);
           }

           if (gamepad1.left_bumper) robot.claw.moveClaw(Claw.clawStates.open);
           if (gamepad1.right_bumper) robot.claw.moveClaw(Claw.clawStates.close);

            telemetry.update();
       }

    }
}
