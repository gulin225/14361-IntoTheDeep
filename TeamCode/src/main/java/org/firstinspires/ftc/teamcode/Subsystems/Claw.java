package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    ColorSensor sensor;
    Servo claw;
    CRServo spinner;
    public Claw(HardwareMap hardwareMap){
        claw = hardwareMap.servo.get("claw");
        spinner = hardwareMap.crservo.get("spinner");
       // sensor = hardwareMap.get(ColorSensor.class, "colorSensor");
    }

    public void openClaw(){
        claw.setPosition(.21);
    }
    public void closeClaw(){
        claw.setPosition(.4);
    }
    public void spinOn(){
        spinner.setPower(1);
    }
    public void spinOff(){
        spinner.setPower(0);
    }

 //   public double getColorVal(){
 //       return sensor.red() + sensor.blue() + sensor.green();
  //  }

}
