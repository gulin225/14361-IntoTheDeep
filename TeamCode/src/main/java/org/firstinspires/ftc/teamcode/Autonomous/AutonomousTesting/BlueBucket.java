
package org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting;

import static org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting.AutoStates.idle;
import static org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting.AutoStates.runningPath;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Autonomous.PinpointDrive;
import org.firstinspires.ftc.teamcode.Autonomous.SubsystemActions.BotActions;

//-72 y = red side
//x is pos on red side
@Config
@Autonomous(name = "BlueBucket", group = "Autonomous")
public class BlueBucket extends LinearOpMode {

    AutoStates autoStates = runningPath;
    BotActions botActions;
    PinpointDrive drive;
    final Pose2d startPose = new Pose2d(0,0, Math.toRadians(0));
    TelemetryPacket tel = new TelemetryPacket();
    Action SlidePIDLoop;
    SequentialAction path;
    boolean running;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new PinpointDrive(hardwareMap, startPose);
        botActions = new BotActions(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        if (autoStates == runningPath) {
            path = createPath();
            running = path.run(tel);
        }

        while (!isStopRequested() && opModeIsActive()) {
            SlidePIDLoop = botActions.slideActions.PID();

            switch (autoStates){
                case runningPath:
                    running = path.run(tel);
                    if (!running) {
                        autoStates = idle;
                    }
                    break;
                case idle:
                    break;
            }

            SlidePIDLoop.run(tel);

            telemetry.addData("State", autoStates);
            telemetry.addLine(drive.pose.toString());
            telemetry.update();
        }
    }

    public SequentialAction createPath(){
        SequentialAction path = new SequentialAction(
                new ParallelAction(drive.actionBuilder(drive.pose)
                    .setTangent(Math.toRadians(170))
                    .splineToConstantHeading(new Vector2d(17,15), Math.toRadians(150))
                    .waitSeconds(.4).build(),
                    botActions.init(),
                    new SequentialAction(new SleepAction(2.2), botActions.clawActions.openClawAction())
                ),

                new ParallelAction(drive.actionBuilder(new Pose2d(30,5,Math.toRadians(0)))
                    .setTangent(Math.toRadians(300))
                    .splineToLinearHeading(new Pose2d(42,-11.5, Math.toRadians(-90)), Math.toRadians(200)).build(),
                    new SequentialAction(
                        new SleepAction(.5),
                        botActions.intake(),
                        new SleepAction(2),
                        botActions.clawActions.spinOffAction()
                    ),
                    new ParallelAction(drive.actionBuilder(new Pose2d(42,-24, Math.toRadians(-90)))
                        .setTangent(Math.toRadians(0))
                        .splineToLinearHeading(new Pose2d(10,-34,Math.toRadians(-225)),Math.toRadians(0)).build()
                )
//                new ParallelAction(drive.actionBuilder(new Pose2d(10,-34,Math.toRadians(-225)))
//                        .setTangent(Math.toRadians(180))
//                        .splineToLinearHeading(new Pose2d(25,-37,Math.toRadians(-180)),Math.toRadians(180)).build()
//                ),
//                new ParallelAction(drive.actionBuilder(new Pose2d(25,-37,Math.toRadians(-180)))
//                        .setTangent(Math.toRadians(0))
//                        .splineToLinearHeading(new Pose2d(10,-34,Math.toRadians(-225)),Math.toRadians(0)).build()
//                ),
//                new ParallelAction(drive.actionBuilder(new Pose2d(10,-34,Math.toRadians(-225)))
//                        .setTangent(Math.toRadians(0))
//                        .splineToLinearHeading(new Pose2d(30,-40,Math.toRadians(-100)),Math.toRadians(0)).build()
//                ),
//                new ParallelAction(drive.actionBuilder(new Pose2d(25,-39,Math.toRadians(-100)))
//                        .setTangent(Math.toRadians(0))
//                        .splineToLinearHeading(new Pose2d(10,-34,Math.toRadians(-225)),Math.toRadians(0)).build()
//                )

        ));

        return  path;
    }
}
























