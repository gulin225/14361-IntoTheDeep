package org.firstinspires.ftc.teamcode.Autonomous.RRdrives;



import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
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
        /*
        Set the odometry pod positions relative to the point that the odometry computer tracks around.
        The X pod offset refers to how far sideways from the tracking point the
        X (forward) odometry pod is. Left of the center is a positive number,
        right of the center is a negative number. The Y pod offset refers to how far forwards from
        the tracking point the Y (strafe) odometry pod is: forward of the center is a positive number,
        backwards is a negative number.
         */
        //These are tuned for 3110-0002-0001 Product Insight #1
        // RR localizer note: These units are inches, presets are converted from mm (which is why they are inexact)
        public double xOffset = -7.58;
        public double yOffset = -.012;

        /*
        Set the kind of pods used by your robot. If you're using goBILDA odometry pods, select either
        the goBILDA_SWINGARM_POD or the goBILDA_4_BAR_POD.
        If you're using another kind of odometry pod, input the number of ticks per millimeter for that pod.

        RR LOCALIZER NOTE: this is ticks per MILLIMETER, NOT inches per tick.
        This value should be more than one; the value for the Gobilda 4 Bar Pod is approximately 20.
        To get this value from inPerTick, first convert the value to millimeters (multiply by 25.4)
        and then take its inverse (one over the value)
         */
        public double encoderResolution = GoBildaPinpointDriverRR.goBILDA_4_BAR_POD;

        /*
        Set the direction that each of the two odometry pods count. The X (forward) pod should
        increase when you move the robot forward. And the Y (strafe) pod should increase when
        you move the robot to the left.
         */
        public GoBildaPinpointDriver.EncoderDirection xDirection = GoBildaPinpointDriver.EncoderDirection.REVERSED;
        public GoBildaPinpointDriver.EncoderDirection yDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD;
    }

    public static Params PARAMS = new Params();
    public GoBildaPinpointDriverRR pinpoint;
    private Pose2d lastPinpointPose = pose;
    Telemetry telemetry;
    final double cameraPlacementX = 7.5;
    final double cameraPlacementY = 0;
    final double cameraAngle = Math.atan(cameraPlacementY/cameraPlacementX);
    final double botCenterHypotenuse = Math.sqrt(Math.pow(cameraPlacementX,2) + Math.pow(cameraPlacementY,2));
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
    }
    public PinpointDrive(HardwareMap hardwareMap, Pose2d pose, Telemetry tel) {
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
        telemetry = tel;
        limelight = new Limelight(hardwareMap, telemetry);
        pinpoint.setPosition(pose);
    }

    @Override
    public PoseVelocity2d updatePoseEstimate() {
        if (lastPinpointPose != pose) {
            pinpoint.setPosition(pose);
        }
        pinpoint.update();

        /*Pose2d aprilTagPose = updatePoseWithAprilTag(pinpoint.getHeading());
        if (aprilTagPose != null){
            double weightedX = (pose.position.x + aprilTagPose.position.x)/2;
            double weightedY = (pose.position.y + aprilTagPose.position.y)/2;
            Pose2d weightedPose = new Pose2d(weightedX, weightedY,pose.heading.toDouble());
            telemetry.addData("apriltag", aprilTagPose.toString());
        }else telemetry.addLine("tag not in sight");*/

        pose = pinpoint.getPositionRR();
        lastPinpointPose = pose;

        poseHistory.add(pose);
        while (poseHistory.size() > 100) {
            poseHistory.removeFirst();
        }

        FlightRecorder.write("ESTIMATED_POSE", new PoseMessage(pose));
        FlightRecorder.write("PINPOINT_RAW_POSE",new FTCPoseMessage(pinpoint.getPosition()));
        FlightRecorder.write("PINPOINT_STATUS",pinpoint.getDeviceStatus());


        return pinpoint.getVelocityRR();
    }

    public Pose2d updatePoseWithAprilTag(double heading){
        limelight.limelight.updateRobotOrientation(heading);
        Pose3D botpose = limelight.getLatestPosition(telemetry);

        Pose2d newPose = null;
        if (botpose != null){
            double cameraX = (botpose.getPosition().x-1.8002)/0.04203;
            double cameraY = ((botpose.getPosition().y*39.37)+ 47.3044)/1.65203;

            //if camera is centered
            double relativeBotX = Math.cos(Math.toRadians(heading))*cameraPlacementX;
            double relativeBotY = Math.sin(Math.toRadians(heading))*cameraPlacementX;

            //if camera has y displacement from origin
            relativeBotX = Math.cos(Math.toRadians(heading) + cameraAngle) * botCenterHypotenuse;
            relativeBotY = Math.sin(Math.toRadians(heading) + cameraAngle) * botCenterHypotenuse;

            double absoluteBotX = cameraX - relativeBotX;
            double absoluteBotY = cameraY - relativeBotY;

            //double botPosX = targetAprilTag.x + absoluteBotX;
            //double botPosY = targetAprilTag.y + absoluteBotY;

            newPose = new Pose2d(cameraX, cameraY, heading);
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
