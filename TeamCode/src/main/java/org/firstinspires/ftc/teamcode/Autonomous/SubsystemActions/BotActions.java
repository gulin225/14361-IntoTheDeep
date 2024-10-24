package org.firstinspires.ftc.teamcode.Autonomous.SubsystemActions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Autonomous.RRdrives.PinpointDrive;
import org.firstinspires.ftc.teamcode.Subsystems.Claw;

public class BotActions {
    public ClawActions clawActions;
    public LinearRailActions linearRailActions;
    public SlideActions slideActions;
    public PinpointDrive drive;

    public BotActions(HardwareMap hardwareMap, PinpointDrive pdrive){
        clawActions = new ClawActions(hardwareMap);
        linearRailActions = new LinearRailActions(hardwareMap);
        slideActions = new SlideActions(hardwareMap);
        drive = pdrive;
    }

    public ParallelAction outtakeHighRung(){
        return new ParallelAction(
           linearRailActions.outtakeAction(),
           slideActions.highRungAction()
       );
    }

    public ParallelAction init(){
        return new ParallelAction(
            clawActions.closeClawAction(),
            clawActions.wristOn(),
            clawActions.ArmHighRung(),
            slideActions.intakeAction()
        );
    }
    public ParallelAction start(){
        return new ParallelAction(
            slideActions.highRungAction(),
            linearRailActions.outtakeAction(),
            new SequentialAction(

            )
        );
    }

    public ParallelAction outtakeHighBasket(){
        return new ParallelAction(
            linearRailActions.outtakeAction(),
            slideActions.highBasketAction(),
            clawActions.armOuttakeAction()
        );
    }

    public ParallelAction placeSpecimen(){
        return new ParallelAction(
            clawActions.armSubmerisbleAction(),
            slideActions.pullDownRungAction()
        );
    }
    public ParallelAction intake(){
        return new ParallelAction(
            clawActions.armIntakeAction(),
            slideActions.intakeAction(),
            clawActions.openClawAction(),
            linearRailActions.intakeAction(),
            clawActions.spinOnAction()
        );
    }
    public SequentialAction clampSample(){
        return new SequentialAction(
            clawActions.closeClawAction(),
            new SleepAction(.3),
            clawActions.spinOffAction()
        );
    }

    public class zoomIn implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                drive.limelight.zoomIn();
                initialized = true;
            }
            return false;
        }
    }

    public Action zoomInAction(){
        return new zoomIn();
    }

}
