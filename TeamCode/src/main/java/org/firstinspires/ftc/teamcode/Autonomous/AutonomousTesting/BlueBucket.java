
package org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting;

import static org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting.AutoStates.*;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Autonomous.PinpointDrive;
import org.firstinspires.ftc.teamcode.Autonomous.SubsystemActions.BotActions;
import org.firstinspires.ftc.teamcode.Subsystems.LinearRail;
import org.firstinspires.ftc.teamcode.Subsystems.Robot;

@Config
@Autonomous(name = "BlueBucket", group = "Autonomous")
public class BlueBucket extends LinearOpMode {

    AutoStates autoStates = state1;
    BotActions botActions;
    PinpointDrive drive;
    final Pose2d startPose = new Pose2d(0,0, Math.toRadians(0));
    TelemetryPacket tel = new TelemetryPacket();
    Action SlidePIDLoop;
    SequentialAction path, path2, path3;
    boolean running;
    LinearRail linearRail;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new PinpointDrive(hardwareMap, startPose);
        botActions = new BotActions(hardwareMap);
        linearRail = new LinearRail(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        if (autoStates == state1) {
            path = createPath();
            running = path.run(tel);
        }
        botActions.init().run(tel);

        while (!isStopRequested() && opModeIsActive()) {
            SlidePIDLoop = botActions.slideActions.PID();

            switch (autoStates){
                case state1:
                    running = path.run(tel);
                    if (!running) {
                        autoStates = AutoStates.state2;
                        botActions.intake().run(tel);
                        path2 = createPath2();
                    }
                    break;
                case state2:
                    running = path2.run(tel);
                    if (!running) {
                        autoStates = state3;
                        botActions.linearRailActions.intakeAction().run(tel);
                        path3 = createPath3();
                    }
                    break;
                case state3:
                    //running = path3.run(tel);
                    if (!running) autoStates = idle;
                    break;
                case idle:
                    break;
            }

            SlidePIDLoop.run(tel);
            updateTelemetry();
        }
    }

    public SequentialAction createPath(){
        SequentialAction path1 = new SequentialAction(
                drive.actionBuilder(drive.pose)
                    .setTangent(Math.toRadians(0))
                    .splineToConstantHeading(new Vector2d(30.2,-15), Math.toRadians(0)).build(),
                new SleepAction(1.2)
                /*new ParallelAction(drive.actionBuilder(new Pose2d(42,-24, Math.toRadians(-90)))
                    .setTangent(Math.toRadians(0))
                    .splineToLinearHeading(new Pose2d(10,-34,Math.toRadians(-225)),Math.toRadians(0)).build(),
                    new SequentialAction(
                        botActions.outtakeHighBasket(),
                        new SleepAction(2),
                        botActions.clawActions.openClawAction()
                    )
                ),
                new ParallelAction(drive.actionBuilder(new Pose2d(10,-34,Math.toRadians(-225)))
                    .setTangent(Math.toRadians(180))
                    .splineToLinearHeading(new Pose2d(25,-37,Math.toRadians(-180)),Math.toRadians(180)).build(),
                    new SequentialAction(
                            botActions.intake(),
                            new SleepAction(2),
                            botActions.clawActions.spinOffAction()
                    )
                ),
                new ParallelAction(drive.actionBuilder(new Pose2d(25,-37,Math.toRadians(-180)))
                    .setTangent(Math.toRadians(0))
                    .splineToLinearHeading(new Pose2d(10,-34,Math.toRadians(-225)),Math.toRadians(0)).build(),
                    new SequentialAction(
                            botActions.outtakeHighBasket(),
                            new SleepAction(2),
                            botActions.clawActions.openClawAction()
                    )
                ),
                new ParallelAction(drive.actionBuilder(new Pose2d(10,-34,Math.toRadians(-225)))
                    .setTangent(Math.toRadians(0))
                    .splineToLinearHeading(new Pose2d(30,-40,Math.toRadians(-100)),Math.toRadians(0)).build(),
                    new SequentialAction(
                            botActions.intake(),
                            new SleepAction(2),
                            botActions.clawActions.spinOffAction()
                    )
                ),
                new ParallelAction(drive.actionBuilder(new Pose2d(25,-39,Math.toRadians(-100)))
                    .setTangent(Math.toRadians(0))
                    .splineToLinearHeading(new Pose2d(10,-34,Math.toRadians(-225)),Math.toRadians(0)).build(),
                    new SequentialAction(
                            botActions.outtakeHighBasket(),
                            new SleepAction(2),
                            botActions.clawActions.openClawAction()
                    )
                )*/
        );



        return  path1;
    }

    public SequentialAction createPath2(){
        return new SequentialAction(
                drive.actionBuilder(new Pose2d(30.2,-15, Math.toRadians(0)))
                    .splineToConstantHeading(new Vector2d(25,-15), Math.toRadians(0))
                    .splineToConstantHeading(new Vector2d(15,33), Math.toRadians(200)).build(),
                new InstantAction(() -> linearRail.moveRail(LinearRail.linearRailStates.intake))
            )
        ;}
    public SequentialAction createPath3(){
        return new SequentialAction(
                drive.actionBuilder(new Pose2d(15,33, Math.toRadians(0)))
                        .splineToLinearHeading(new Pose2d(10,37,Math.toRadians(-45)), Math.toRadians(0))
                        .build(),
                new SequentialAction(
                    botActions.outtakeHighBasket()
                )
            );}

    public void updateTelemetry(){
        telemetry.addData("State", autoStates);
        telemetry.addLine(drive.pose.toString());
        telemetry.update();
    }
}
























