package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    ColorSensor sensor;
    Servo claw, slide, rightArm;
    CRServo spinner;
    public Claw(HardwareMap hardwareMap){
        //claw = hardwareMap.servo.get("claw");
        slide = hardwareMap.servo.get("slide");
        rightArm = hardwareMap.servo.get("rightArm");

        //spinner = hardwareMap.crservo.get("spinner");
       // sensor = hardwareMap.get(ColorSensor.class, "colorSensor");
    }
    public void test(double pos, double pos2) {
        double offset = .035;
        slide.setPosition(pos);
        rightArm.setPosition(pos+offset);
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
    public void slideServo() {
        slide.setDirection(Servo.Direction.REVERSE);
        //outtaking: .16
        //intake .7
    }
    public void rail(){
        //outtake .65
        //intake: .3
    }

 //   public double getColorVal(){
 //       return sensor.red() + sensor.blue() + sensor.green();
  //  }

}
