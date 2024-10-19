package org.firstinspires.ftc.teamcode.Autonomous.SubsystemActions;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Subsystems.LinearRail;

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
       ParallelAction outtakingHighRung = new ParallelAction(
               linearRailActions.outtakeAction(),
               slideActions.highRungAction()
       );
       return outtakingHighRung;
    }

    public ParallelAction init(){
        ParallelAction init = new ParallelAction(
                clawActions.closeClawAction(),
                slideActions.highRungAction(),
                linearRailActions.outtakeAction(),
                clawActions.wristOn(),
                clawActions.ArmHighRung()
        );
        return init;
    }

    public ParallelAction outtakeHighBasket(){
        ParallelAction outtakingHighBasket = new ParallelAction(
                linearRailActions.outtakeAction(),
                slideActions.highBasketAction(),
                clawActions.armOuttakeAction()
        );
        return outtakingHighBasket;
    }

    public ParallelAction placeSpecimen(){
        return new ParallelAction(
                clawActions.armSubmerisbleAction(),
                slideActions.pullDownRungAction()
        );
    }
    public ParallelAction intake(){
        ParallelAction intakeAfterSpecimen = new ParallelAction(
                clawActions.armIntakeAction(),
                slideActions.intakeAction(),
                clawActions.openClawAction(),
                linearRailActions.intakeAction(),
                clawActions.spinOnAction()
        );
        return intakeAfterSpecimen;
    }
    public SequentialAction clampSample(){
        SequentialAction clampSample = new SequentialAction(
                clawActions.closeClawAction(),
                new SleepAction(.3),
                clawActions.spinOffAction()
        );
        return clampSample;
    }

}
