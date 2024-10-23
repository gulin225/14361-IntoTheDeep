package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.TeleOp.TeleOperation.states;

public class Robot {
    public Claw claw;
    public Limelight limelight;
    public LinearRail linearRail;
    public DriveTrain driveTrain;
    public VerticalSlides verticalSlides;

    public Robot(HardwareMap hardwareMap, Telemetry telemetry){
        linearRail = new LinearRail(hardwareMap);
        claw = new Claw(hardwareMap);
        driveTrain = new DriveTrain(hardwareMap);
        verticalSlides = new VerticalSlides(hardwareMap);
    }

    public void init(){
        verticalSlides.setSlides(VerticalSlides.slideStates.intake);
        claw.moveClaw(Claw.clawStates.wristIntake);
        claw.moveClaw(Claw.clawStates.outtake);
        linearRail.moveRail(LinearRail.linearRailStates.outtake);

    }

    public void intake(){
        linearRail.moveRail(LinearRail.linearRailStates.outtake );
        claw.moveClaw(Claw.clawStates.intakeFlat);
    }

    public void outtake(){
        linearRail.moveRail(LinearRail.linearRailStates.outtake);
        claw.moveClaw(Claw.clawStates.outtake);
    }

    public void setClaw(Gamepad gamepad){
        if (gamepad.left_trigger > .2) claw.moveClaw(Claw.clawStates.spinReverse);
        else if (gamepad.right_trigger > .2) claw.moveClaw(Claw.clawStates.spinOn);
        else  claw.moveClaw(Claw.clawStates.spinOff);

        if (gamepad.left_bumper) claw.moveClaw(Claw.clawStates.open);
        if (gamepad.right_bumper) claw.moveClaw(Claw.clawStates.close);
    }
    public void setArm_Rail(Gamepad gamepad, states state){
        switch (state){
            case outtakingBucket:
                if (gamepad.square) claw.moveClaw(Claw.clawStates.intakeFlat);
                if (gamepad.circle) claw.moveClaw(Claw.clawStates.outtake);
                break;
            case drivingToSubmersible:
                if (gamepad.square) {
                    claw.moveClaw(Claw.clawStates.intakeSubmersible);
                    linearRail.moveRail(LinearRail.linearRailStates.intake);
                    claw.moveClaw(Claw.clawStates.open);
                }
                if (gamepad.circle) {
                    claw.moveClaw(Claw.clawStates.intakeFlat);
                    claw.moveClaw(Claw.clawStates.close);
                    linearRail.moveRail(LinearRail.linearRailStates.outtake);
                }
        }
    }

    public states setVerticalSlides(Gamepad gamepad){
        if (gamepad.dpad_down) {
            verticalSlides.setSlides(VerticalSlides.slideStates.intake);

            claw.moveClaw(Claw.clawStates.intakeFlat);
            linearRail.moveRail(LinearRail.linearRailStates.outtake);
            claw.moveClaw(Claw.clawStates.close);
            claw.moveClaw(Claw.clawStates.spinOff);

            return states.drivingToSubmersible;
        }
        if (gamepad.dpad_left) {
            verticalSlides.setSlides(VerticalSlides.slideStates.specimen);

            claw.moveClaw(Claw.clawStates.outtake);
            linearRail.moveRail(LinearRail.linearRailStates.outtake);
            claw.moveClaw(Claw.clawStates.close);
            claw.moveClaw(Claw.clawStates.spinOff);

            return states.intakeSpecimen;
        }
        if (gamepad.dpad_up) {
            verticalSlides.setSlides(VerticalSlides.slideStates.highRung);

            claw.moveClaw(Claw.clawStates.intakeFlat);
            linearRail.moveRail(LinearRail.linearRailStates.outtake);
            claw.moveClaw(Claw.clawStates.close);
            claw.moveClaw(Claw.clawStates.spinOff);

            return states.outtakingSpecimen;
        }
        if (gamepad.dpad_right) {
            verticalSlides.setSlides(VerticalSlides.slideStates.highBasket);

            claw.moveClaw(Claw.clawStates.outtake);
            linearRail.moveRail(LinearRail.linearRailStates.outtake);
            claw.moveClaw(Claw.clawStates.close);
            claw.moveClaw(Claw.clawStates.spinOff);

            return states.outtakingBucket;
        }

        verticalSlides.addTarget(-gamepad.right_stick_y);
        verticalSlides.addTarget(-gamepad.left_stick_y * 2);
        return null;
    }
}
