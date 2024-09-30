package org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting;

import static org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting.AutoStates.idle;
import static org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting.AutoStates.preload;
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
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Autonomous.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.Limelight;

import java.util.ArrayList;

//-72 y = red side
//x is pos on red side
@Config
@Autonomous(name = "RedSpecimenPoukie", group = "Autonomous")
public class RedSpecimenPoukie extends LinearOpMode {

    AutoStates autoStates = preload;
    IMU imu;
    MecanumDrive drive;
    Pose2d currentPose = new Pose2d(18,-63.5, Math.toRadians(90));
    TelemetryPacket tel = new TelemetryPacket();
    SequentialAction sampleAction;
    ParallelAction preloadAction;
    Limelight limelight;
    final Vector2d targetAprilTag = new Vector2d(71.5,-47.5);
    final double cameraPlacementX = 7.5;
    final double cameraPlacementY = 0;
    final double cameraAngle = Math.atan(cameraPlacementY/cameraPlacementX);
    final double botCenterHypotenuse = Math.sqrt(Math.pow(cameraPlacementX,2) + Math.pow(cameraPlacementY,2));
    @Override
    public void runOpMode() throws InterruptedException {
        drive = new MecanumDrive(hardwareMap, currentPose);
        limelight = new Limelight(hardwareMap, telemetry);
        imu = drive.lazyImu.get();

        while(!isStopRequested() && !opModeIsActive()) {
        }

        waitForStart();

        if (isStopRequested()) return;

        autoStates = idle;
        if (autoStates == idle) {}
        else {
            preloadAction = createPreloadAction();
            Actions.runBlocking(preloadAction);
        }

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
        double heading = imu.getRobotYawPitchRollAngles().getYaw();
        limelight.limelight.updateRobotOrientation(heading);
        Pose3D botpose = limelight.getLatestPosition();
        telemetry.addData("Heading", heading);

        if (botpose != null){
            double cameraX = (Math.abs(botpose.getPosition().x*39.37)-70.562)/1.62;
            double cameraY = -10 + (((botpose.getPosition().y*39.37)-1)+75.0458)/1.444;
            telemetry.addData("Camera X", cameraX);
            telemetry.addData("Camera Y",  cameraY);

            //if camera is centered
            double relativeBotX = Math.cos(Math.toRadians(heading))*cameraPlacementX;
            double relativeBotY = Math.sin(Math.toRadians(heading))*cameraPlacementX;

            //if camera has y displacement from origin
            relativeBotX = Math.cos(Math.toRadians(heading) + cameraAngle) * botCenterHypotenuse;
            relativeBotY = Math.sin(Math.toRadians(heading) + cameraAngle) * botCenterHypotenuse;
            telemetry.addData("Relative X", relativeBotX);
            telemetry.addData("Relative Y",  relativeBotY);

            double absoluteBotX = relativeBotX + cameraX;
            double absoluteBotY = relativeBotY + cameraY;
        }

        telemetry.update();
        return null;
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

























