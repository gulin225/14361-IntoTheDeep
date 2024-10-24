package org.firstinspires.ftc.teamcode.Autonomous.SubsystemActions;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class BotActions {
    public ClawActions clawActions;
    public LinearRailActions linearRailActions;
    public SlideActions slideActions;

    public BotActions(HardwareMap hardwareMap){
        clawActions = new ClawActions(hardwareMap);
        linearRailActions = new LinearRailActions(hardwareMap);
        slideActions = new SlideActions(hardwareMap);
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

}
