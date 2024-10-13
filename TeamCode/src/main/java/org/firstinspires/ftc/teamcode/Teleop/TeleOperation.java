package org.firstinspires.ftc.teamcode.Teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting.AprilTagDrive;
import org.firstinspires.ftc.teamcode.Autonomous.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Subsystems.LinearRail;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

import java.util.concurrent.TimeUnit;


@Config
@TeleOp(name = "test")
public class TeleOperation extends LinearOpMode {
    LinearRail linearRail;
    Claw claw;
    Servo linkage;
    @Override
    public void runOpMode() throws InterruptedException {
       linearRail = new LinearRail(hardwareMap);
       claw = new Claw(hardwareMap);

       waitForStart();
       claw.moveClaw(Claw.clawStates.wristIntake);

       while (!isStopRequested() && opModeIsActive()){
           if (gamepad2.left_bumper){
               linearRail.moveRail(LinearRail.linearRailStates.intake);
           }
           if (gamepad2.right_bumper){
               linearRail.moveRail(LinearRail.linearRailStates.outtake);
           }
           if (gamepad1.dpad_down){
               //low
           }
           if (gamepad1.dpad_left){
                //2nd
           }
           if (gamepad1.dpad_up){
                //third
           }
           if (gamepad1.dpad_right){
                //fourth
           }
           if (gamepad1.square){
               claw.moveClaw(Claw.clawStates.intake);
           }
           if (gamepad1.circle){
               claw.moveClaw(Claw.clawStates.outtake);
           }
           telemetry.addLine(gamepad1.right_trigger + "");
           if (gamepad1.left_bumper){
               claw.moveClaw(Claw.clawStates.open);
           }
           if (gamepad1.right_bumper){
               claw.moveClaw(Claw.clawStates.close);
           }
           claw.moveClaw(Claw.clawStates.wristIntake);
           telemetry.update();
       }

    }
}
