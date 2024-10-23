package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class LinearRail {
    Servo linearRail, linkage;
    public enum linearRailStates{
        intake, outtake, test,middle
    }
    public LinearRail(HardwareMap hardwareMap){
        linearRail = hardwareMap.servo.get("linearRail");
        linkage = hardwareMap.servo.get("linkage");
    }

    public void moveRail(linearRailStates state){
        switch (state){
            case intake:
                linearRail.setPosition(.27);
                linkage.setPosition(.23);
                break;
            case outtake:
                linearRail.setPosition(.69); //.69
                linkage.setPosition(.63); //.63
                break;
            case middle:
                linearRail.setPosition(.45);
                break;
        }
    }
}
