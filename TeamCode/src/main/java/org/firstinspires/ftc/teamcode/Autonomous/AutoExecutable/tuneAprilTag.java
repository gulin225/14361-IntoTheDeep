package org.firstinspires.ftc.teamcode.Autonomous.AutoExecutable;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Autonomous.RRdrives.PinpointDrive;
import org.firstinspires.ftc.teamcode.Subsystems.Limelight;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


@Config
@Autonomous(name = "tune april tag", group = "Autonomous")
public class tuneAprilTag extends LinearOpMode {
    PinpointDrive drive;
    Pose2d start = new Pose2d(0,0,0);

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new PinpointDrive(hardwareMap, start);

        waitForStart();

        while (opModeIsActive() && !isStopRequested()){
            drive.updatePoseEstimate();
            telemetry.addData("x", drive.pose.position.x);
            telemetry.addData("y", drive.pose.position.y);
            telemetry.addData("H", Math.toDegrees(drive.pose.heading.toDouble()));
            telemetry.update();
        }

    }

}
