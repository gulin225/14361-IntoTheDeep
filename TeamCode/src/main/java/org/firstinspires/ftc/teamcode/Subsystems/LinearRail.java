package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class LinearRail {
    Servo linearRail, linkage;
    public enum linearRailStates{
        intake, outtake
    }
    public enum linkageStates{
        intake, outtake
    }
    public LinearRail(HardwareMap hardwareMap){
        linearRail = hardwareMap.servo.get("linearRail");
        linkage = hardwareMap.servo.get("linkage");
        linkage.setDirection(Servo.Direction.REVERSE);
    }

    public void moveRail(linearRailStates state){
        switch (state){
            case intake:
                linearRail.setPosition(.3);
                break;
            case outtake:
                linearRail.setPosition(.65);
                break;
        }
    }

    public void moveLinkage(linkageStates state){
        switch (state){
            case intake:
                linearRail.setPosition(.7);
                break;
            case outtake:
                linearRail.setPosition(.16);
                break;
        }
    }
}
