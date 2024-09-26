package org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.Autonomous.MecanumDrive;

import java.util.ArrayList;

public class AutoActions {
    public ParallelAction preloadAction, specimenAction;

    Pose2d currentPose;
    public  ArrayList<Action> sample1 =  new ArrayList<Action>();

    //Pose2d start;

    public AutoActions(MecanumDrive drive, Pose2d start){
        SequentialAction preload = new SequentialAction(drive.actionBuilder(start)
            //    .splineToConstantHeading(new Vector2d(18,-35), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(18,-33), Math.toRadians(0))
                .waitSeconds(2)
                .build());

        preloadAction = new ParallelAction(preload);
    }
    public ParallelAction returnSampleAction(MecanumDrive drive){
        SequentialAction sample1and2Action = new SequentialAction(drive.actionBuilder(getDrivePoseEstimate(drive))
                .splineToLinearHeading(new Pose2d(50,-33,Math.toRadians(85.5)), Math.toRadians(90))
                .waitSeconds(1)
                .splineToLinearHeading(new Pose2d(55,-57, Math.toRadians(90)), Math.toRadians(340))
                .waitSeconds(1)
                .build(),
                updatePoseEstimateAction(drive),
                new ParallelAction((drive.actionBuilder(drive.pose)
                        .lineToY(-33)
                        .build()))
                );
        drive.updatePoseEstimate();
        SequentialAction sample3Action = new SequentialAction(drive.actionBuilder(new Pose2d(55,-33, Math.toRadians(90)))
                .splineToLinearHeading(new Pose2d(62,-57,Math.toRadians(85.5)), Math.toRadians(-90))
                .waitSeconds(.1)

                .build(),
                updatePoseEstimateAction(drive),
                new ParallelAction((drive.actionBuilder(new Pose2d(62,-57, Math.toRadians(90)))
                        .lineToY(-33)
                        .build())),
                new SequentialAction((drive.actionBuilder(new Pose2d(62, -33, Math.toRadians(90))))
                        .splineToLinearHeading(new Pose2d(62,-57,Math.toRadians(90)), Math.toRadians(-90))
                        .build())
                );

        SequentialAction specimenSeq = new SequentialAction(sample1and2Action, sample3Action);
        //put sensor stuff below
        specimenAction = new ParallelAction(specimenSeq);
        return specimenAction;
    }

    public ParallelAction returnSampleActionTest(MecanumDrive drive){
        SequentialAction goTowardsCenter = new SequentialAction(drive.actionBuilder(drive.pose)
                .lineToYConstantHeading(-33)
                .build());
        SequentialAction goTowardsWall1 = new SequentialAction(drive.actionBuilder(drive.pose)
                .lineToYConstantHeading(55)
                .build());

        ParallelAction goToHuman = new ParallelAction(goTowardsCenter, goTowardsWall1);
return goToHuman;
    }

    public Pose2d getDrivePoseEstimate(MecanumDrive drive){
        return drive.pose;

    }

    public SequentialAction updatePoseEstimateAction(MecanumDrive drive){
        drive.updatePoseEstimate();
        SequentialAction updatePoseEstimate = new SequentialAction(drive.actionBuilder(drive.pose)
                .build());
       return updatePoseEstimate;
    }

}
