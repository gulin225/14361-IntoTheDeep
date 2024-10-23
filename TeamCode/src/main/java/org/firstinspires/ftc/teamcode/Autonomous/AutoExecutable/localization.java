package org.firstinspires.ftc.teamcode.Autonomous.AutoExecutable;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Autonomous.RRdrives.PinpointDrive;
import org.firstinspires.ftc.teamcode.Subsystems.Limelight;


@Config
@Autonomous(name = "localization", group = "Autonomous")
public class localization extends LinearOpMode {
    PinpointDrive drive;
    Pose2d start = new Pose2d(0,0,0);

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new PinpointDrive(hardwareMap, start, Limelight.corners.blueSpecimen);

        waitForStart();

        while (opModeIsActive() && !isStopRequested()){
            drive.setDrivePowers(new PoseVelocity2d(
                    new Vector2d(
                            -gamepad1.left_stick_y,
                            -gamepad1.left_stick_x
                    ),
                    -gamepad1.right_stick_x
            ));

            if (gamepad1.left_bumper){
                drive.limelight.zoomIn();
            }
            if (gamepad1.right_bumper){
                drive.limelight.zoomOut();
            }
            drive.updatePoseEstimate();
            telemetry.addData("x", drive.pose.position.x);
            telemetry.addData("y", drive.pose.position.y);
            telemetry.addData("H", Math.toDegrees(drive.pose.heading.toDouble()));
            telemetry.update();
        }

    }

}
