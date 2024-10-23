
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
@Autonomous(name = "Blue Bucket", group = "Autonomous")
public class BlueBucket extends LinearOpMode {
    public static class Poses{
        Pose2d start = new Pose2d(8.5,-7.8, Math.toRadians(0));
        Pose2d preload = new Pose2d(36,-18,Math.toRadians(0));
        Pose2d cycleSampleBucket = new Pose2d(25,34,Math.toRadians(-20));
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
        drive = new PinpointDrive(hardwareMap, poses.start, Limelight.corners.blueBucket);
        botActions = new BotActions(hardwareMap);
        func = new Functions(drive, telemetry);

        //botActions.init();

        waitForStart();

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
                preloadSpecimen(), sample1(), sample2(), sample3()
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
                        new SleepAction(.5)
                    )
            ),
            new ParallelAction(
                func.Turn(poses.cycleSampleBucket, -45, 0),
                new SequentialAction(
                    new SleepAction(.5)
                )
            )
        );
    }

    public SequentialAction sample2(){
        return new SequentialAction(
            new ParallelAction(
                func.Turn(poses.cycleSampleBucket, 0, -45),
                new SequentialAction(
                        new SleepAction(.5)
                )
            ),
            new ParallelAction(
                func.Turn(poses.cycleSampleBucket, -45, 0),
                new SequentialAction(
                        new SleepAction(.5)
                )
            )
        );
    }

    public SequentialAction sample3(){
        return new SequentialAction(
            new ParallelAction(
                func.Turn(poses.cycleSampleBucket, -45, 20),
                new SequentialAction(
                        new SleepAction(.5)
                )
            ),
            new ParallelAction(
                func.Turn(poses.cycleSampleBucket, -45, 20),
                new SequentialAction(
                        new SleepAction(.5)
                )
            )
        );
    }

    public Pose2d P2D(double x, double y, double angle) {
        //Shorter Pose2d
        return new Pose2d(x,y,Math.toRadians(angle));
    }
}























