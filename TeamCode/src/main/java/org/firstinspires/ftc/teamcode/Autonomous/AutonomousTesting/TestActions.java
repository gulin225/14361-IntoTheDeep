package org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting;

import static org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting.AutoStates.idle;
import static org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting.AutoStates.preload;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting.SubsystemActions.ClawActions;
import org.firstinspires.ftc.teamcode.Autonomous.MecanumDrive;

public class TestActions extends LinearOpMode {
    AutoStates autoStates = preload;
    ClawActions claw;
    Pose2d start = new Pose2d(18,-63.5, Math.toRadians(90));
    MecanumDrive drive;
    @Override
    public void runOpMode() throws InterruptedException {
     drive = new MecanumDrive(hardwareMap, start);
     claw = new ClawActions(hardwareMap);
     drive.pose = start;
        waitForStart();

        if (isStopRequested()) return;

        while (!isStopRequested() && opModeIsActive()) {
            telemetry.addLine("Drive Pose Estimate" + drive.pose);
            telemetry.addLine("Drive Pose X" + drive.pose.position.x);
            telemetry.addLine("Drive Pose Estimate" + drive.pose.position.y);
            drive.updatePoseEstimate();
            switch (autoStates){
                case preload:
                    Actions.runBlocking(
                            new ParallelAction(
                                    claw.closeClawAction(),
                                    drive.actionBuilder(drive.pose)
                                            .lineToY(40)
                                            .build()));

                    autoStates = idle;
                    break;
               // Actions.runBlocking(new ParallelAction(drive.actionBuilder(drive.pose).lineToX(100).build(), slide.RaiseSlideUp()));

                case idle:

                    break;
            }
        }

    }
}
