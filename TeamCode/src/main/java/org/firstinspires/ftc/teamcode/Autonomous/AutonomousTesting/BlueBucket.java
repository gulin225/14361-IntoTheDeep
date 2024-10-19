
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
import org.firstinspires.ftc.teamcode.Autonomous.SubsystemActions.ClawActions;
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

        botActions.clawActions.closeClawAction().run(tel);
        botActions.clawActions.wristOn().run(tel);
        botActions.clawActions.ArmHighRung().run(tel);

        waitForStart();

        if (autoStates == state1) {
            path = createPath();
            running = path.run(tel);
        }

        while (!isStopRequested() && opModeIsActive()) {
            SlidePIDLoop = botActions.slideActions.PID();

            switch (autoStates){
                case state1:
                    running = path.run(tel);
                    if (!running) autoStates = AutoStates.idle;
                    break;
                case idle:
                    break;
            }

            SlidePIDLoop.run(tel);
            updateTelemetry();
        }
    }

    public SequentialAction createPath(){
        return new SequentialAction(
            botActions.init(),
            drive.actionBuilder(drive.pose)
                .setTangent(Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(27.5,-10), Math.toRadians(0)).build(),
            new SleepAction(.5),
            botActions.placeSpecimen(),
            new SleepAction(.7),
            botActions.clawActions.openClawAction(),
            new ParallelAction(
                new SequentialAction(
                    new SleepAction(.5),
                    botActions.intake()
                ),
                drive.actionBuilder(new Pose2d(27.5,-10, Math.toRadians(0)))
                    .setTangent(Math.toRadians(180))
                    .splineToConstantHeading(new Vector2d(18,33), Math.toRadians(0)).build()
            ),
            botActions.clampSample(),
            new ParallelAction(
                drive.actionBuilder(new Pose2d(15,33, Math.toRadians(0)))
                    .setTangent(Math.toRadians(0))
                    .splineToLinearHeading(new Pose2d(13,34,Math.toRadians(-45)), Math.toRadians(0))
                    .build(),
                botActions.outtakeHighBasket()
            ),
            new SleepAction(.3),
            drive.actionBuilder(new Pose2d(13,34,Math.toRadians(-45)))
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(4,43,Math.toRadians(-45)), Math.toRadians(180))
                .build(),
            botActions.clawActions.openClawAction(),
            new SleepAction(.3),
            drive.actionBuilder(new Pose2d(4,43,Math.toRadians(-45)))
                    .setTangent(Math.toRadians(0))
                    .splineToLinearHeading(new Pose2d(10,46,Math.toRadians(0)), Math.toRadians(0))
                    .build(),
            botActions.slideActions.intakeAction(),
            new SleepAction(.7),
            botActions.intake(),
            new SleepAction(.5),
            drive.actionBuilder(new Pose2d(10,46,Math.toRadians(0)))
                .setTangent(Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(15,46,Math.toRadians(0)), Math.toRadians(0))
                .build(),
            botActions.clampSample(),
            new ParallelAction(
                drive.actionBuilder(new Pose2d(15,46,Math.toRadians(0)))
                    .setTangent(Math.toRadians(0))
                    .splineToLinearHeading(new Pose2d(12,32,Math.toRadians(-45)), Math.toRadians(0))
                    .build(),
                botActions.outtakeHighBasket()
            ),
            new SleepAction(.3),
            drive.actionBuilder(new Pose2d(12,32,Math.toRadians(-45)))
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(4,43,Math.toRadians(-45)), Math.toRadians(180))
                .build(),
            botActions.clawActions.openClawAction(),
            new SleepAction(.3),
            drive.actionBuilder(new Pose2d(4,43,Math.toRadians(-45)))
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(10,40,Math.toRadians(0)), Math.toRadians(180))
                .build(),
            botActions.slideActions.intakeAction(),
            new SleepAction(.7),
            botActions.intake(),
            drive.actionBuilder(new Pose2d(10,40,Math.toRadians(0)))
                    .setTangent(Math.toRadians(270))
                    .splineToSplineHeading(new Pose2d(36,28,Math.toRadians(90)), Math.toRadians(90))
                    .splineToConstantHeading(new Vector2d(36,34), Math.toRadians(90))
                    .build(),
            new SequentialAction(
                    botActions.clawActions.closeClawAction(),
                    new SleepAction(.3),
                    botActions.clawActions.spinOffAction()
            ),
            new ParallelAction(
                drive.actionBuilder(new Pose2d(36,34,Math.toRadians(90)))
                    .setTangent(Math.toRadians(270))
                    .splineToLinearHeading(new Pose2d(19,35,Math.toRadians(135)), Math.toRadians(90))
                    .build(),
                botActions.slideActions.highBasketAction(),
                botActions.clawActions.thirdSampleArmOuttakeAction()
            ),
            new SleepAction(.3),
            drive.actionBuilder(new Pose2d(19,35,Math.toRadians(135)))
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(17,37,Math.toRadians(135)), Math.toRadians(180))
                .build(),
            botActions.clawActions.openClawThirdSampleAction(),
            new SleepAction(.3),
            drive.actionBuilder(new Pose2d(17,37,Math.toRadians(135)))
                    .setTangent(Math.toRadians(180))
                    .splineToLinearHeading(new Pose2d(10,37,Math.toRadians(0)), Math.toRadians(180))
                    .build(),
            botActions.slideActions.intakeAction(),
            botActions.clawActions.closeClawAction(),
            new SleepAction(.7),
            botActions.clawActions.ArmHighRung()
        );
    }

    public SequentialAction oneSamplePath(){
        return new SequentialAction(
                botActions.init(),
                drive.actionBuilder(drive.pose)
                        .setTangent(Math.toRadians(0))
                        .splineToConstantHeading(new Vector2d(27.5,-10), Math.toRadians(0)).build(),
                new SleepAction(.5),
                botActions.placeSpecimen(),
                new SleepAction(.7),
                botActions.clawActions.openClawAction(),
                new ParallelAction(
                        new SequentialAction(
                                new SleepAction(.5),
                                botActions.intake()
                        ),
                        drive.actionBuilder(new Pose2d(27.5,-10, Math.toRadians(0)))
                                .setTangent(Math.toRadians(180))
                                .splineToConstantHeading(new Vector2d(15,33), Math.toRadians(0)).build()
                ),
                botActions.clampSample(),
                new ParallelAction(
                        drive.actionBuilder(new Pose2d(15,33, Math.toRadians(0)))
                                .setTangent(Math.toRadians(0))
                                .splineToLinearHeading(new Pose2d(10,37,Math.toRadians(-45)), Math.toRadians(0))
                                .build(),
                        botActions.outtakeHighBasket()
                ),
                new SleepAction(.3),
                drive.actionBuilder(new Pose2d(10,37,Math.toRadians(-45)))
                        .setTangent(Math.toRadians(180))
                        .splineToLinearHeading(new Pose2d(4,43,Math.toRadians(-45)), Math.toRadians(180))
                        .build(),
                botActions.clawActions.openClawAction(),
                new SleepAction(.3),
                drive.actionBuilder(new Pose2d(4,43,Math.toRadians(-45)))
                        .setTangent(Math.toRadians(180))
                        .splineToLinearHeading(new Pose2d(10,40,Math.toRadians(0)), Math.toRadians(180))
                        .build(),
                botActions.slideActions.intakeAction(),
                new SleepAction(1),
                botActions.clawActions.ArmHighRung()
        );
    }

    public void updateTelemetry(){
        telemetry.addData("State", autoStates);
        telemetry.addLine(drive.pose.toString());
        telemetry.update();
    }
}
























