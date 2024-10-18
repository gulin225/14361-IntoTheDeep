package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    ColorSensor sensor;
    public Servo claw, leftArm, rightArm, wrist;
    public CRServo activeIntake;
    public enum clawStates{
        spinOn, spinOff, open, close, intakeSubmersible, outtake, wristIntake, intakeFlat, intakeAuto
    }
    final double armOffset = .02;
    public Claw(HardwareMap hardwareMap){
        claw = hardwareMap.servo.get("claw");
        activeIntake = hardwareMap.crservo.get("activeIntake");
        wrist = hardwareMap.servo.get("wrist");
        leftArm = hardwareMap.servo.get("leftArm");
        rightArm = hardwareMap.servo.get("rightArm");
    }

    public void moveClaw(clawStates state){
        switch (state){
            case spinOn:
                activeIntake.setPower(-1);
                break;
            case spinOff:
                activeIntake.setPower(0);
                break;
            case open:
                claw.setPosition(.21);
                break;
            case close:
                claw.setPosition(.4);
                break;
            case intakeSubmersible:
                leftArm.setPosition(.3);
                rightArm.setPosition(.3+armOffset);
                break;
            case intakeAuto:
                leftArm.setPosition(.32);
                rightArm.setPosition(.32+armOffset);
                break;
            case intakeFlat:
                leftArm.setPosition(.4);
                rightArm.setPosition(.4+armOffset);
                break;
            case outtake:
                leftArm.setPosition(.76);
                rightArm.setPosition(.76+armOffset);
                break;
            case wristIntake:
                wrist.setPosition(.15);
                break;
        }
    }
}
