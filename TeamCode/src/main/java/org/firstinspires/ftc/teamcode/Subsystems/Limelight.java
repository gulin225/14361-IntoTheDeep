package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.ArrayList;
import java.util.List;

public class Limelight {
    public Limelight3A limelight;
    final double cameraPlacementX = 0;
    final double cameraPlacementY = 7.4;
    final double cameraAngle = Math.atan(cameraPlacementY/cameraPlacementX);
    final double botCenterHypotenuse = Math.sqrt(Math.pow(cameraPlacementX,2) + Math.pow(cameraPlacementY,2));
    public Vector2d targetAprilTag;
    public enum corners{
        blueBucket, blueSpecimen, redBucket, redSpecimen
    }
    corners corner;

    public Limelight(HardwareMap hardwareMap, corners c){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
        corner = c;

        switch (corner){
            case blueBucket:
                targetAprilTag = new Vector2d(20,40);
                break;
            case blueSpecimen:
                targetAprilTag = new Vector2d(20,-40);
                break;
        }
    }

    public Pose2d getLatestPosition(double heading, Pose2d pinpointPose){
        Pose2d weightedPose = null;

        limelight.updateRobotOrientation(heading);
        LLResult result = limelight.getLatestResult();

        if (!result.getFiducialResults().isEmpty()) {
            Pose3D rawPose = result.getBotpose_MT2();

            double cameraY = -((rawPose.getPosition().x - 1.8002) / 0.04203)-85;
            double cameraX = (((rawPose.getPosition().y * 39.37) + 47.3044) / 1.65203)-58;

            double relativeBotX = Math.cos(Math.toRadians(heading) + cameraAngle) * botCenterHypotenuse;
            double relativeBotY = Math.sin(Math.toRadians(heading) + cameraAngle) * botCenterHypotenuse;

            double absoluteBotX = cameraX + relativeBotX;
            double absoluteBotY = 0;
            switch (corner){
                case blueBucket:
                    absoluteBotY = cameraY + relativeBotY;
                    break;
                case blueSpecimen:
                    absoluteBotY = cameraY - relativeBotY;
                    break;
            }

            Pose2d distanceFromTag = new Pose2d(absoluteBotX, absoluteBotY, Math.toRadians(heading));

            Vector2d botPos = new Vector2d(0,0);
            switch (corner){
                case blueBucket:
                    botPos = new Vector2d(
                            targetAprilTag.x + distanceFromTag.position.x,
                            targetAprilTag.y - distanceFromTag.position.y);
                    break;
            }


            weightedPose = new Pose2d(
                    (botPos.x + pinpointPose.position.x)/2,
                    (botPos.y + pinpointPose.position.y)/2,
                    heading
            );
        };
        return weightedPose;
    }

}
