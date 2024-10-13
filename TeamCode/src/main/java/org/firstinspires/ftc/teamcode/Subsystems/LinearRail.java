package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting.AprilTagDrive;

public class LinearRail {
    Servo linearRail, linkage;
    public enum linearRailStates{
        intake, outtake, test
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
                linearRail.setPosition(.6);
                linkage.setPosition(.75);
                break;
            case test:
                linkage.setPosition(AprilTagDrive.PARAMS.kA);
        }
    }
}
