package org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting;

import static org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting.AutoStates.idle;
import static org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting.AutoStates.samples;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Autonomous.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.Limelight;

//-72 y = red side
//x is pos on red side
@Config
@Autonomous(name = "RedSpecimenPoukie", group = "Autonomous")
public class RedSpecimenPoukie extends LinearOpMode {

    AutoStates autoStates = idle;
    AutoActions actions;
    MecanumDrive drive;
    Pose2d currentPose = new Pose2d(18,-63.5, Math.toRadians(90));
    TelemetryPacket tel = new TelemetryPacket();
    SequentialAction sampleAction;
    ParallelAction preloadAction;
    Limelight limelight;
    @Override
    public void runOpMode() throws InterruptedException {
        drive = new MecanumDrive(hardwareMap, currentPose);
        actions = new AutoActions(drive,currentPose);
        limelight = new Limelight(hardwareMap, telemetry);

        while(!isStopRequested() && !opModeIsActive()) {
        }

        waitForStart();

        if (isStopRequested()) return;

        preloadAction = createPreloadAction();
        Actions.runBlocking(preloadAction);

        while (!isStopRequested() && opModeIsActive()) {
            switch (autoStates){
                case preload:
                    updatePoseWithAprilTag();
                    if (!preloadAction.run(tel)) {
                        sampleAction = createSampleAction();
                        Actions.runBlocking(sampleAction);
                        autoStates = samples;
                    }
                    break;
                case samples:
                    updatePoseWithAprilTag();
                    if (!sampleAction.run(tel)) {

                        autoStates = idle;
                    }
                    break;
                case idle:
                    updatePoseWithAprilTag();
            }
        }
    }

    public Pose2d updatePoseWithAprilTag(){
        telemetry.addData("Limelight pos", limelight.getLatestPosition().get(0) + ' ' + limelight.getLatestPosition().get(1));
        telemetry.update();
        return null;
       // return Pose2d;
    }
    public ParallelAction createPreloadAction(){
            ParallelAction preloadAction = new ParallelAction(drive.actionBuilder(currentPose)
                    .splineToConstantHeading(new Vector2d(18,-33), Math.toRadians(0))
                    .waitSeconds(2).build()
            );

            return  preloadAction;
    }

    public SequentialAction createSampleAction(){
        //Each action goes to the sample, then deposits it at the human player
        ParallelAction sample1Action = new ParallelAction(drive.actionBuilder(currentPose)
                .splineToLinearHeading(new Pose2d(50,-33,Math.toRadians(85.5)), Math.toRadians(90))
                .waitSeconds(1)
                .splineToLinearHeading(new Pose2d(55,-57, Math.toRadians(90)), Math.toRadians(340))
                .waitSeconds(1)
                .build()
        //There should be sleep actions with subsytem movements inside each parallel action
        );

        ParallelAction sample2Action = new ParallelAction(drive.actionBuilder(currentPose)
                .lineToY(-33)
                .splineToLinearHeading(new Pose2d(62,-57,Math.toRadians(85.5)), Math.toRadians(-90))
                .waitSeconds(1).build()
        );

        ParallelAction sample3Action = new ParallelAction(drive.actionBuilder(currentPose)
                .build()
        );

        SequentialAction specimenSequence = new SequentialAction(sample1Action, sample2Action, sample3Action);
        return specimenSequence;
    }
}

























