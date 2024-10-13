package org.firstinspires.ftc.teamcode.Subsystems;

import android.sax.StartElementListener;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class VerticalSlides {
    DcMotorEx backLeftSlide, backRightSlide, frontLeftSlide, frontRightSlide;

    public VerticalSlides(HardwareMap hardwareMap){
        backLeftSlide = hardwareMap.get(DcMotorEx.class,"leftBackS");
        backRightSlide = hardwareMap.get(DcMotorEx.class,"rightBackS");
        frontLeftSlide = hardwareMap.get(DcMotorEx.class,"leftFrontS");
        frontRightSlide = hardwareMap.get(DcMotorEx.class,"rightFrontS");
        frontRightSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void test(double one, double two, double three, double four){
        backLeftSlide.setPower(one);
        backRightSlide.setPower(two);
        frontLeftSlide.setPower(three);
        frontRightSlide.setPower(four);

    }
}
