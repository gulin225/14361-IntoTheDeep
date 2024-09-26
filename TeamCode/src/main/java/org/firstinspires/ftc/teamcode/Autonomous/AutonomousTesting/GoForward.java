package org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Autonomous.MecanumDrive;

@Config
@Autonomous(name = "GO_FORWARD", group = "Autonomous")
public class GoForward extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Action trajectoryAction1;
        Pose2d start = new Pose2d(0,0, Math.toRadians(0));
        MecanumDrive drive = new MecanumDrive(hardwareMap,start);
        while(!isStopRequested() && !opModeIsActive()) {
            telemetry.addLine("Hello");
            telemetry.update();



        }
        trajectoryAction1 = drive.actionBuilder(start)
                .splineTo(new Vector2d(60,20), Math.toRadians(90))
              //  .lineToX(10)
                .build();
        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                  trajectoryAction1
                )
        );


    }
}
