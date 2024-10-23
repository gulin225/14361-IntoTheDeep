package org.firstinspires.ftc.teamcode.Autonomous.RRdrives;



import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.FlightRecorder;
import com.acmerobotics.roadrunner.ftc.GoBildaPinpointDriver;
import com.acmerobotics.roadrunner.ftc.GoBildaPinpointDriverRR;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Autonomous.AutoExecutable.BlueBucket;
import org.firstinspires.ftc.teamcode.Autonomous.RRdrives.MecanumDrive;
import org.firstinspires.ftc.teamcode.Autonomous.messages.PoseMessage;
import org.firstinspires.ftc.teamcode.Subsystems.Limelight;

/**
 * Experimental extension of MecanumDrive that uses the Gobilda Pinpoint sensor for localization.
 * <p>
 * Released under the BSD 3-Clause Clear License by j5155 from 12087 Capital City Dynamics
 * Portions of this code made and released under the MIT License by Gobilda (Base 10 Assets, LLC)
 * Unless otherwise noted, comments are from Gobilda
 */
public class PinpointDrive extends MecanumDrive {
    public static class Params {
        public double xOffset = -7.58;
        public double yOffset = -.012;
        public double encoderResolution = GoBildaPinpointDriverRR.goBILDA_4_BAR_POD;
        public GoBildaPinpointDriver.EncoderDirection xDirection = GoBildaPinpointDriver.EncoderDirection.REVERSED;
        public GoBildaPinpointDriver.EncoderDirection yDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD;
        final double cameraPlacementX = 0;
        final double cameraPlacementY = 7.4;
        final double cameraAngle = Math.atan(cameraPlacementY/cameraPlacementX);
        final double botCenterHypotenuse = Math.sqrt(Math.pow(cameraPlacementX,2) + Math.pow(cameraPlacementY,2));
        public Vector2d targetAprilTag;
    }
    public enum corners{
        blueBucket, blueSpecimen, redBucket, redSpecimen
    }

    public static Params PARAMS = new Params();
    public GoBildaPinpointDriverRR pinpoint;
    private Pose2d lastPinpointPose = pose;
    Limelight limelight;

    public PinpointDrive(HardwareMap hardwareMap, Pose2d pose) {
        super(hardwareMap, pose);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        FlightRecorder.write("PINPOINT_PARAMS",PARAMS);
        pinpoint = hardwareMap.get(GoBildaPinpointDriverRR.class,"pinpoint");
        pinpoint.setOffsets(DistanceUnit.MM.fromInches(PARAMS.xOffset), DistanceUnit.MM.fromInches(PARAMS.yOffset));
        pinpoint.setEncoderResolution(PARAMS.encoderResolution);
        pinpoint.setEncoderDirections(PARAMS.xDirection, PARAMS.yDirection);
        pinpoint.resetPosAndIMU();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        pinpoint.setPosition(pose);
        limelight = new Limelight(hardwareMap);
    }
    public PinpointDrive(HardwareMap hardwareMap, Pose2d pose, corners corner) {
        super(hardwareMap, pose);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        FlightRecorder.write("PINPOINT_PARAMS",PARAMS);
        pinpoint = hardwareMap.get(GoBildaPinpointDriverRR.class,"pinpoint");
        pinpoint.setOffsets(DistanceUnit.MM.fromInches(PARAMS.xOffset), DistanceUnit.MM.fromInches(PARAMS.yOffset));
        pinpoint.setEncoderResolution(PARAMS.encoderResolution);
        pinpoint.setEncoderDirections(PARAMS.xDirection, PARAMS.yDirection);
        pinpoint.resetPosAndIMU();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        pinpoint.setPosition(pose);
        limelight = new Limelight(hardwareMap);

        switch (corner){
            case blueBucket:
                PARAMS.targetAprilTag = new Vector2d(20,40);
                break;
        }
    }

    @Override
    public PoseVelocity2d updatePoseEstimate() {
        if (lastPinpointPose != pose) {
            pinpoint.setPosition(pose);
        }
        pinpoint.update();
        pose = pinpoint.getPositionRR();

        double heading = pose.heading.toDouble();
        Pose2d aprilTagPose = UpdateAprilTag(Math.toDegrees(heading));
        if (aprilTagPose != null){
            Pose2d botTagPose = new Pose2d(
                PARAMS.targetAprilTag.x + aprilTagPose.position.x,
                PARAMS.targetAprilTag.y - aprilTagPose.position.y,
                heading);
            Pose2d weightedPose = new Pose2d(
                (botTagPose.position.x + pose.position.x)/2,
                (botTagPose.position.y + pose.position.y)/2,
                heading
            );
            pose = weightedPose;
        }

        lastPinpointPose = pose;

        poseHistory.add(pose);
        while (poseHistory.size() > 100) {
            poseHistory.removeFirst();
        }

        return pinpoint.getVelocityRR();
    }

    public Pose2d UpdateAprilTag(double heading) {
        Pose3D botpose = limelight.getLatestPosition(heading);

        Pose2d newPose = null;
        if (botpose != null) {
            double cameraY = -((botpose.getPosition().x - 1.8002) / 0.04203)-85;
            double cameraX = (((botpose.getPosition().y * 39.37) + 47.3044) / 1.65203)-58;

            //if camera has y displacement from origin
            double relativeBotX = Math.cos(Math.toRadians(heading) + PARAMS.cameraAngle) * PARAMS.botCenterHypotenuse;
            double relativeBotY = Math.sin(Math.toRadians(heading) + PARAMS.cameraAngle) * PARAMS.botCenterHypotenuse;

            double absoluteBotX = cameraX - relativeBotX;
            double absoluteBotY = cameraY + relativeBotY;
            newPose = new Pose2d(absoluteBotX, absoluteBotY, Math.toRadians(heading));
        }
        return newPose;
    }


    // for debug logging
    public static final class FTCPoseMessage {
        public long timestamp;
        public double x;
        public double y;
        public double heading;

        public FTCPoseMessage(Pose2D pose) {
            this.timestamp = System.nanoTime();
            this.x = pose.getX(DistanceUnit.INCH);
            this.y = pose.getY(DistanceUnit.INCH);
            this.heading = pose.getHeading(AngleUnit.RADIANS);
        }
    }



}
