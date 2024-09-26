package org.firstinspires.ftc.teamcode.Teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Claw;


@Config
@TeleOp(name = "test")
public class TeleOperation extends LinearOpMode {
    Claw claw;
    @Override
    public void runOpMode() throws InterruptedException {
        claw = new Claw(hardwareMap);

        telemetry.addLine("Hello");
        telemetry.update();

        waitForStart();

       while (!isStopRequested() && opModeIsActive()){
           telemetry.addLine("COlor Sensor Val" + claw.getColorVal());

           if (claw.getColorVal() > 800){
               claw.spinOff();
           }
           else if(gamepad1.square){
           //    claw.closeClaw();
               claw.spinOn();
           }

           if(gamepad1.circle){
               claw.spinOff();
           }
           if (gamepad1.left_bumper){
               claw.openClaw();
           }
           if (gamepad1.right_bumper){
               claw.closeClaw();
           }


           telemetry.update();

       }

    }
}
