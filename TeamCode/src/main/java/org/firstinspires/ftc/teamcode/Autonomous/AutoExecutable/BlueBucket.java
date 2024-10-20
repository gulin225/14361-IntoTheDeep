
package org.firstinspires.ftc.teamcode.Autonomous.AutoExecutable;

import static org.firstinspires.ftc.teamcode.Autonomous.AutoExecutable.AutoStates.*;

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

import org.firstinspires.ftc.teamcode.Autonomous.RRdrives.PinpointDrive;
import org.firstinspires.ftc.teamcode.Autonomous.SubsystemActions.BotActions;
import org.firstinspires.ftc.teamcode.Subsystems.LinearRail;

@Config
@Autonomous(name = "Bucket", group = "Autonomous")
public class BlueBucket extends LinearOpMode {

    AutoStates autoStates = followingPath;
    BotActions botActions;
    PinpointDrive drive;
    final Pose2d startPose = new Pose2d(0,0, Math.toRadians(0));
    TelemetryPacket tel = new TelemetryPacket();
    Action verticalSlidePID;
    SequentialAction path;
    boolean running;
    LinearRail linearRail;
    Functions func;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new PinpointDrive(hardwareMap, startPose);
        botActions = new BotActions(hardwareMap);
        linearRail = new LinearRail(hardwareMap);
        func = new Functions(drive, telemetry);

        botActions.init();

        waitForStart();

        path = createPath();

        while (!isStopRequested() && opModeIsActive()) {
            verticalSlidePID = botActions.slideActions.PID();

            switch (autoStates){
                case followingPath:
                    running = path.run(tel);
                    if (!running) autoStates = AutoStates.idle;
                    break;
                case idle:
                    break;
            }

            verticalSlidePID.run(tel);
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
            botActions.start(),
            func.createSingleSpline(drive.pose, P2D(27.5, -10, 0), 0, 0),
            new SleepAction(.5),
            botActions.placeSpecimen(),
            new SleepAction(.7),
            botActions.clawActions.openClawAction()
        );
    }

    public SequentialAction sample1(){
        return new SequentialAction(
            new ParallelAction(
                new SequentialAction(
                    new SleepAction(.5),
                    botActions.intake()
                ),
                func.createSingleSpline(P2D(27.5,-10,0), P2D(18,33,0), 180, 0)
            ),
            botActions.clampSample(),
            new ParallelAction(
                    func.createSingleSpline(P2D(18,33,0), P2D(13,34,-45), 0, 0),
                    botActions.outtakeHighBasket()
            ),
            new SleepAction(.3),
            func.createSingleSpline(P2D(13,34,-45), P2D(4,43,-45), 180, 180),
            botActions.clawActions.openClawAction(),
            new SleepAction(.3),
            func.createSingleSpline(P2D(4, 43, -45), P2D(10, 46, 0), 0, 0),
            botActions.slideActions.intakeAction(),
            new SleepAction(.7),
            botActions.intake(),
            new SleepAction(.5)
        );
    }

    public SequentialAction sample2(){
        return new SequentialAction(
                func.createSingleSpline(P2D(10,46,0), P2D(15,46,0),0,0),
            botActions.clampSample(),
            new ParallelAction(
                    func.createSingleSpline(P2D(15, 46, 0), P2D(12,32,-45), 0, 0),
                    botActions.outtakeHighBasket()
            ),
            new SleepAction(.3),
            func.createSingleSpline(P2D(12, 32, -45), P2D(4, 43, -45), 180, 180),
            botActions.clawActions.openClawAction(),
            new SleepAction(.3),
            func.createSingleSpline(P2D(4, 43, -45), P2D(10, 40, 0), 180, 180),
            botActions.slideActions.intakeAction(),
            new SleepAction(.7),
            botActions.intake()
        );
    }

    public SequentialAction sample3(){
        return new SequentialAction(
            drive.actionBuilder(new Pose2d(10, 40, Math.toRadians(0)))
                .setTangent(Math.toRadians(270))
                .splineToSplineHeading(new Pose2d(36, 28, Math.toRadians(90)), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(36, 34), Math.toRadians(90)).build(),
            botActions.clawActions.closeClawAction(),
            new SleepAction(.3),
            botActions.clawActions.spinOffAction(),
            new ParallelAction(
                func.createSingleSpline(P2D(36,34,90),P2D(19, 35, 135), 270, 90),
                botActions.slideActions.highBasketAction(),
                botActions.clawActions.thirdSampleArmOuttakeAction()
            ),
            new SleepAction(.3),
            func.createSingleSpline(P2D(19,35,135), P2D(17,37,135), 180, 180),
            botActions.clawActions.openClawThirdSampleAction(),
            new SleepAction(.3),
            func.createSingleSpline(P2D(17,37,135), P2D(10,37,0), 180, 180),
            botActions.slideActions.intakeAction(),
            botActions.clawActions.closeClawAction(),
            new SleepAction(.7),
            botActions.clawActions.ArmHighRung()
        );
    }

    public Pose2d P2D(double x, double y, double angle) {
        //Shorter Pose2d
        return new Pose2d(x,y,Math.toRadians(angle));
    }
}
























