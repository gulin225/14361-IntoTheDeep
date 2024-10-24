
package org.firstinspires.ftc.teamcode.Autonomous.AutoExecutable.paths;

import static org.firstinspires.ftc.teamcode.Autonomous.AutoExecutable.AutoStates.*;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Autonomous.AutoExecutable.AutoStates;
import org.firstinspires.ftc.teamcode.Autonomous.AutoExecutable.Functions;
import org.firstinspires.ftc.teamcode.Autonomous.RRdrives.PinpointDrive;
import org.firstinspires.ftc.teamcode.Autonomous.SubsystemActions.BotActions;
import org.firstinspires.ftc.teamcode.Subsystems.Limelight;

@Config
@Autonomous(name = "Blue Specimen", group = "Autonomous")
public class BlueSpecimen extends LinearOpMode {
    public static class Poses{
        Pose2d start = new Pose2d(8.5,8, Math.toRadians(0));
        Pose2d preload = new Pose2d(36,18,Math.toRadians(0));
        Pose2d cycleSampleBucket = new Pose2d(25,-34,Math.toRadians(30));
        Pose2d intakeSpecimen = new Pose2d(13, -22, Math.toRadians(0));
        Pose2d placeSpecimen1 = new Pose2d(36,14,Math.toRadians(0));
    }

    AutoStates autoStates = followingPath;
    BotActions botActions;
    PinpointDrive drive;
    TelemetryPacket tel = new TelemetryPacket();
    Action verticalSlidePID;
    SequentialAction path;
    boolean running;
    Functions func;
    Poses poses = new Poses();

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new PinpointDrive(hardwareMap, poses.start, Limelight.corners.blueSpecimen, telemetry);
        botActions = new BotActions(hardwareMap);
        func = new Functions(drive, telemetry);

        //botActions.init();
        while (!isStarted()) drive.updatePoseEstimate();

        poses.start = drive.pose;
        path = createPath();

        while (!isStopRequested() && opModeIsActive()) {
            //verticalSlidePID = botActions.slideActions.PID();

            switch (autoStates){
                case followingPath:
                    running = path.run(tel);
                    if (!running) autoStates = AutoStates.idle;
                    break;
                case idle:
                    break;
            }

            //verticalSlidePID.run(tel);
            func.updateTelemetry(autoStates);
        }
    }

    public SequentialAction createPath(){
        return new SequentialAction(
            preloadSpecimen(), sample1()
        );
    }

    public SequentialAction preloadSpecimen(){
        return new SequentialAction(
            new ParallelAction(
                func.SingleSpline(poses.start, poses.preload, 0, 0),
                new SequentialAction(
                    new SleepAction(.5)
                )
            )
        );
    }

    public SequentialAction sample1(){
        return new SequentialAction(
            new ParallelAction(
                func.SingleSpline(poses.preload, poses.cycleSampleBucket, 260, 0),
                new SequentialAction(
                    new SleepAction(1)
                )
            ),
            new ParallelAction(
                    func.Turn(poses.cycleSampleBucket, 45, 30),
                    new SequentialAction(
                            new SleepAction(1)
                    )
            )
        );
    }

    public SequentialAction sample2(){
        return new SequentialAction(
            new ParallelAction(
                func.Turn(poses.cycleSampleBucket, 0, 45),
                new SequentialAction(
                    new SleepAction(1)
                )
            ),
            new SequentialAction(
                func.Turn(poses.cycleSampleBucket, 45, 0),
                new SleepAction(1)
            )
        );
    }

    public SequentialAction sample3(){
        return new SequentialAction(
                new ParallelAction(
                    func.Turn(poses.cycleSampleBucket, -20, 45),
                    new SequentialAction(
                        new SleepAction(1)
                    )
                ),
                new SequentialAction(
                    func.Turn(poses.cycleSampleBucket, 45, -20),
                    new SleepAction(1)
                )
        );
    }
    public SequentialAction specimen1(){
        return new SequentialAction(
                new ParallelAction(
                    func.SingleSpline(
                        new Pose2d(poses.cycleSampleBucket.position.x, poses.cycleSampleBucket.position.y, Math.toRadians(45)),
                        poses.intakeSpecimen, 180, 180),
                    new SequentialAction(
                        new SleepAction(1)
                    )
                ),
                new ParallelAction(
                    func.SingleSpline(poses.intakeSpecimen, poses.placeSpecimen1, 0, 0),
                    new SequentialAction(
                        new SleepAction(1)
                    )
                )
        );
    }
}