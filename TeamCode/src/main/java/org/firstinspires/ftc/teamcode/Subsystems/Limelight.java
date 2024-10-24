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
    public enum cameraModes{
        close, far
    }
    public corners corner;
    public cameraModes zoom = cameraModes.close;
    Telemetry telemetry;

    public Limelight(HardwareMap hardwareMap, corners c, Telemetry tel){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
        corner = c;
        telemetry = tel;

        switch (corner){
            case blueBucket:
                targetAprilTag = new Vector2d(23,47.5);
                break;
            case redBucket:
                targetAprilTag = new Vector2d(23,47.5);
                break;
            case blueSpecimen:
                targetAprilTag = new Vector2d(23,-47.5);
                break;
        }
    }

    public Pose2d getLatestPosition(double heading, Pose2d pinpointPose){
        telemetry.addData("Zoom", corner);
        telemetry.addLine("April Tag Detected");
        Pose2d weightedPose = null;

        limelight.updateRobotOrientation(heading);
        LLResult result = limelight.getLatestResult();

        if (!result.getFiducialResults().isEmpty()) {
            Pose3D rawPose = result.getBotpose_MT2();

            double cameraX = 0, cameraY = 0;

            switch (zoom){
                case close:
                    switch (corner){
                        case blueSpecimen:
                            cameraY = -((rawPose.getPosition().x - 1.8002) / 0.04203)-85;
                            cameraX = (((rawPose.getPosition().y * 39.37) + 47.3044) / 1.65203)-58;
                            break;
                        case blueBucket:
                            cameraY = -((rawPose.getPosition().x - 1.8002) / 0.04203)+.5;
                            cameraX = -(((rawPose.getPosition().y * 39.37) + 47.3044) / 1.65203)+57.5;
                            break;
                        case redBucket:
                            cameraY = -((rawPose.getPosition().x - 1.8002) / 0.04203)+-85.5;
                            cameraX = -(((rawPose.getPosition().y * 39.37) + 47.3044) / 1.65203);
                            break;
                    }
                    break;
                case far:
                    switch(corner){
                        case blueSpecimen:
                            cameraY = (rawPose.getPosition().y*39.37)*-1.99415 + 87.7902;
                            cameraX = (rawPose.getPosition().x*39.37)*-1.96417-135.804;
                            break;
                        case blueBucket:
                            cameraY = (rawPose.getPosition().y*39.37)*-1.99415 + 84.8;
                            cameraX = -(rawPose.getPosition().x*39.37)*-1.96417-135.804;
                            break;
                        case redBucket:
                            cameraY = (rawPose.getPosition().y*39.37)*-2.15176-122.776;
                            cameraX = -(rawPose.getPosition().x*39.37)*2.09-151.24;
                            break;
                    }
                    break;
            }

            double relativeBotX = Math.cos(Math.toRadians(heading) + cameraAngle) * botCenterHypotenuse;
            double relativeBotY = Math.sin(Math.toRadians(heading) + cameraAngle) * botCenterHypotenuse;

            double absoluteBotX = cameraX + relativeBotX;
            double absoluteBotY = cameraY + relativeBotY;

            Vector2d botPos = new Vector2d(0,0);
            switch (corner){
                case blueSpecimen:
                    botPos = new Vector2d(targetAprilTag.x + absoluteBotX, targetAprilTag.y + absoluteBotY);
                    break;
                case blueBucket:
                    botPos = new Vector2d(targetAprilTag.x + absoluteBotX, targetAprilTag.y - absoluteBotY);
                    break;
                case redBucket:
                    botPos = new Vector2d(targetAprilTag.x + absoluteBotX, targetAprilTag.y - absoluteBotY);
                    break;
            }

            weightedPose = new Pose2d(botPos, Math.toRadians(heading));
            telemetry.addLine(weightedPose.position.toString());
            telemetry.addData("rel x", relativeBotX);
            telemetry.addData("rel y", relativeBotY);
            telemetry.addData("Cam x", cameraX);
            telemetry.addData("Cam y", cameraY);
            telemetry.addData("zoom", zoom);
            telemetry.addData("corner", corner);
        };
        return weightedPose;
    }

    public void zoomIn(){
        limelight.pipelineSwitch(1);
        zoom = cameraModes.far;
    }
    public void zoomOut(){
        limelight.pipelineSwitch(0);
        zoom = cameraModes.close;
    }
}
