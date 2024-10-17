package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
@Config
@TeleOp(name = "TestingPID")
public class TestingPID extends OpMode {
private PIDController controllerUp, controllerDown;


    boolean leftBackFrontRight = true;
    public static double f = 0.1;

    public static int target =0;
    //p = 0.01 d = .001
    public static double pUp = 0.021, iUp = 0, dUp = 0.000225;
    public static double pDown = 0.015, iDown = 0, dDown = 0.00012;
    public final double ticks_in_degrees = 145.1/360; //num of ticks per rotation we need to find this out
    DcMotorEx backLeftSlide, backRightSlidePID, frontLeftSlide, frontRightSlide;

    //max = 1060
    @Override
    public void init() {
        controllerUp = new PIDController(pUp,iUp,dUp);
        controllerDown = new PIDController(pDown, iDown, dDown);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        backLeftSlide = hardwareMap.get(DcMotorEx.class,"leftBackS");
        backRightSlidePID = hardwareMap.get(DcMotorEx.class,"rightBackS");
        frontLeftSlide = hardwareMap.get(DcMotorEx.class,"leftFrontS");
        frontRightSlide = hardwareMap.get(DcMotorEx.class,"rightFrontS");


       backRightSlidePID.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightSlidePID.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //backRightSlidePID.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightSlidePID.setDirection(DcMotorSimple.Direction.REVERSE);
    //   backLeftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
    //    frontRightSlide.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {

        int armPos = backRightSlidePID.getCurrentPosition();

        if(target > armPos) {
            controllerUp.setPID(pUp,iUp,dUp);
            double pid = controllerUp.calculate(armPos, target);

            double ff = Math.cos(Math.toRadians(target / ticks_in_degrees)) * f;

            double power = pid + ff;
            if(power > 1) power = 1;
            telemetry.addData("arm pos", armPos);
            telemetry.addData("target", target);
            telemetry.addData("power",power);

            backRightSlidePID.setPower(power);
            backLeftSlide.setPower(power);
           frontLeftSlide.setPower(power);
            frontRightSlide.setPower(power);
            telemetry.update();
        }
        else{
            controllerDown.setPID(pDown, iDown,dDown);
            double pid = controllerDown.calculate(armPos, target);

            double ff = Math.cos(Math.toRadians(target / ticks_in_degrees)) * f;

            double power = pid + ff;

            telemetry.addData("arm pos", armPos);
            telemetry.addData("target", target);

          //
//            if(leftBackFrontRight){
//                backLeftSlide.setPower(power);
//                frontRightSlide.setPower(power);
//                leftBackFrontRight = false;
//            }
//            else{
//                backRightSlidePID.setPower(power);
//                frontLeftSlide.setPower(power);
//                leftBackFrontRight = true;
//            }
            int num = 1;
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


            //

            telemetry.update();
        }
    }
}
