package org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting;

import static org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting.AutoStates.preload;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Autonomous.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.Limelight;

//-72 y = red side
//x is pos on red side
@Config
@Autonomous(name = "goforward", group = "Autonomous")
public class forward extends LinearOpMode {

    AutoStates autoStates = preload;
    IMU imu;
    MecanumDrive drive;
    final Pose2d startPose = new Pose2d(18,-63.5, Math.toRadians(90));
    TelemetryPacket tel = new TelemetryPacket();
    SequentialAction sampleAction;
    ParallelAction preloadAction;
    Limelight limelight;
    boolean running;
    final Vector2d targetAprilTag = new Vector2d(71.5,-47.5);
    final double cameraPlacementX = 7.5;
    final double cameraPlacementY = 0;
    final double cameraAngle = Math.atan(cameraPlacementY/cameraPlacementX);
    final double botCenterHypotenuse = Math.sqrt(Math.pow(cameraPlacementX,2) + Math.pow(cameraPlacementY,2));
    @Override
    public void runOpMode() throws InterruptedException {
        drive = new MecanumDrive(hardwareMap, startPose);
        imu = drive.lazyImu.get();
        imu.resetYaw();
        while(!isStopRequested() && !opModeIsActive()) {
        }

        waitForStart();

        if (isStopRequested()) return;

        while (!isStopRequested() && opModeIsActive()) {
            Actions.runBlocking(new SequentialAction(drive.actionBuilder(startPose).lineToY(0).build()));

        }
    }


    public ParallelAction createPreloadAction(){
        ParallelAction preloadAction = new ParallelAction(drive.actionBuilder(drive.pose)
                .splineToConstantHeading(new Vector2d(18,-45), Math.toRadians(0))
                .waitSeconds(2).build()
        );

        return  preloadAction;
    }

    public SequentialAction createSampleAction(){
        //Each action goes to the sample, then deposits it at the human player
        ParallelAction sample1Action = new ParallelAction(drive.actionBuilder(drive.pose)
                .splineToLinearHeading(new Pose2d(50,-33,Math.toRadians(85.5)), Math.toRadians(90))
                .waitSeconds(1)
                .splineToLinearHeading(new Pose2d(55,-57, Math.toRadians(90)), Math.toRadians(340))
                .waitSeconds(1)
                .build()
                //There should be sleep actions with subsytem movements inside each parallel action
        );

        ParallelAction sample2Action = new ParallelAction(drive.actionBuilder(drive.pose)
                .lineToY(-33)
                .splineToLinearHeading(new Pose2d(62,-57,Math.toRadians(85.5)), Math.toRadians(-90))
                .waitSeconds(1).build()
        );

        ParallelAction sample3Action = new ParallelAction(drive.actionBuilder(drive.pose)
                .build()
        );

        SequentialAction specimenSequence = new SequentialAction(sample1Action, sample2Action, sample3Action);
        return specimenSequence;
    }

    public class telemetryAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            return true;
        }
    }

    public Pose2d updatePoseWithAprilTag(){
        double heading = imu.getRobotYawPitchRollAngles().getYaw();
        limelight.limelight.updateRobotOrientation(heading);
        Pose3D botpose = limelight.getLatestPosition(telemetry);
        telemetry.addData("Heading", heading);
        Pose2d newPose = null;
        if (botpose != null){
            double cameraX = (botpose.getPosition().x-1.8002)/0.04203;
            double cameraY = ((botpose.getPosition().y*39.37)+ 47.3044)/1.65203;

            //if camera is centered
            double relativeBotX = Math.cos(Math.toRadians(heading))*cameraPlacementX;
            double relativeBotY = Math.sin(Math.toRadians(heading))*cameraPlacementX;

            //if camera has y displacement from origin
            relativeBotX = Math.cos(Math.toRadians(heading) + cameraAngle) * botCenterHypotenuse;
            relativeBotY = Math.sin(Math.toRadians(heading) + cameraAngle) * botCenterHypotenuse;

            double absoluteBotX = cameraX - relativeBotX;
            double absoluteBotY = cameraY - relativeBotY;

            double botPosX = targetAprilTag.x + absoluteBotX;
            double botPosY = targetAprilTag.y + absoluteBotY;

            telemetry.addData("Cam X", cameraX);
            telemetry.addData("Cam Y", cameraY);
            newPose = new Pose2d(botPosX, botPosY, heading);
        }
        return newPose;
    }
}

























