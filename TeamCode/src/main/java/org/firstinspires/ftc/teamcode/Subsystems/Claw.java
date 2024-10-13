package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    ColorSensor sensor;
    Servo claw;
    CRServo activeIntake;
    public enum clawStates{
        spinOn, spinOff, open, close
    }
    public Claw(HardwareMap hardwareMap){
        claw = hardwareMap.servo.get("claw");
        activeIntake = hardwareMap.crservo.get("activeIntake");
    }

    public void moveClaw(clawStates state){
        switch (state){
            case spinOn:
                activeIntake.setPower(1);
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
        }
    }
}
