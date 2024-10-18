package org.firstinspires.ftc.teamcode.Subsystems;

import android.sax.StartElementListener;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting.AprilTagDrive;

public class VerticalSlides {
    private PIDController controllerUp, controllerDown;


    boolean leftBackFrontRight = true;
    public static double f = 0.1;

    public enum slideStates{
        intake, lowBasket, highRung, highBasket, pullDown
    }
   public static int target =0;
    //p = 0.01 d = .001
    public static double pUp = 0.021, iUp = 0, dUp = 0.000225;
    int num;
    public static double pDown = 0.015, iDown = 0, dDown = 0.00012;
    public final double ticks_in_degrees = 145.1/360; //num of ticks per rotation we need to find this out
    DcMotorEx backLeftSlide, backRightSlidePID, frontLeftSlide, frontRightSlide;


    public VerticalSlides(HardwareMap hardwareMap){
        controllerUp = new PIDController(pUp,iUp,dUp);
        controllerDown = new PIDController(pDown, iDown, dDown);


        backLeftSlide = hardwareMap.get(DcMotorEx.class,"leftBackS");
        backRightSlidePID = hardwareMap.get(DcMotorEx.class,"rightBackS");
        frontLeftSlide = hardwareMap.get(DcMotorEx.class,"leftFrontS");
        frontRightSlide = hardwareMap.get(DcMotorEx.class,"rightFrontS");


        backRightSlidePID.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightSlidePID.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);



        frontLeftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightSlidePID.setDirection(DcMotorSimple.Direction.REVERSE);

    }


    public void PIDLoop(){

        int armPos = backRightSlidePID.getCurrentPosition();

        if(target > armPos) {
            controllerUp.setPID(pUp,iUp,dUp);
            double pid = controllerUp.calculate(armPos, target);

            double ff = Math.cos(Math.toRadians(target / ticks_in_degrees)) * f;

            double power = pid + ff;
            if(power > 1) power = 1;
            backRightSlidePID.setPower(power);
            backLeftSlide.setPower(power);
            frontLeftSlide.setPower(power);
            frontRightSlide.setPower(power);

        }
        else{
            controllerDown.setPID(pDown, iDown,dDown);
            double pid = controllerDown.calculate(armPos, target);

            double ff = Math.cos(Math.toRadians(target / ticks_in_degrees)) * f;

            double power = pid + ff;

            num = 1;
            if(num == 1){
                backLeftSlide.setPower(power);
                frontRightSlide.setPower(power);
                frontLeftSlide.setPower(power);
                num = 2;
            } else if (num ==2) {
                frontRightSlide.setPower(power);
                frontLeftSlide.setPower(power);
                backRightSlidePID.setPower(power);
                num = 3;
            } else if (num == 3) {
                backLeftSlide.setPower(power);
                frontRightSlide.setPower(power);
                backRightSlidePID.setPower(power);
                num =4;
            } else if (num==4) {
                backRightSlidePID.setPower(power);
                backLeftSlide.setPower(power);
                frontLeftSlide.setPower(power);
                num = 1;
            }


        }
    }

    public void setSlidesUp(){
        target = 500;
    }

    public void setSlidesDown(){
        target = 0;
    }

    public void setSlides(slideStates state){
        switch (state){
            case intake:
                target=0;
                break;
            case pullDown:
                target = 490;
                break;
            case lowBasket:
                target=125;
                break;
            case highRung:
                target=690;
                break;
            case highBasket:
                target=1000;
                break;
        }
    }

    public void test(double one, double two, double three, double four){
        backLeftSlide.setPower(one);
        backRightSlidePID.setPower(two);
        frontLeftSlide.setPower(three);
        frontRightSlide.setPower(four);

    }
    public void setPowerZero(){
        backLeftSlide.setPower(0);
        backRightSlidePID.setPower(0);
        frontLeftSlide.setPower(0);
        frontRightSlide.setPower(0);
    }

    public int getTarget(){
        return target;
    }
    public int getCurrent(){
        return backRightSlidePID.getCurrentPosition();
    }

    public void testBackLeft(){
        backLeftSlide.setPower(.5);
    }


}
