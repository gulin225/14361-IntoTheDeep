package org.firstinspires.ftc.teamcode.Autonomous.RRdrives;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.ftc.FlightRecorder;
import com.acmerobotics.roadrunner.ftc.GoBildaPinpointDriver;
import com.acmerobotics.roadrunner.ftc.GoBildaPinpointDriverRR;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Subsystems.Limelight;

public class PinpointDrive extends MecanumDrive {
    public static class Params {
        public double xOffset = -7.58;
        public double yOffset = -.012;
        public double encoderResolution = GoBildaPinpointDriverRR.goBILDA_4_BAR_POD;
        public GoBildaPinpointDriver.EncoderDirection xDirection = GoBildaPinpointDriver.EncoderDirection.REVERSED;
        public GoBildaPinpointDriver.EncoderDirection yDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD;
    }

    public static Params PARAMS = new Params();
    public GoBildaPinpointDriverRR pinpoint;
    private Pose2d lastPinpointPose = pose;
    public Limelight limelight;
    Telemetry telemetry;

    public PinpointDrive(HardwareMap hardwareMap, Pose2d pose, Limelight.corners corner, Telemetry tel) {
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
        limelight = new Limelight(hardwareMap, corner, tel);
        telemetry = tel;
    }

    @Override
    public PoseVelocity2d updatePoseEstimate() {
        if (lastPinpointPose != pose) {
            pinpoint.setPosition(pose);
        }
        pinpoint.update();
        pose = pinpoint.getPositionRR();

        if (pose.position.y > 30 && lastPinpointPose.position.y <= 30) limelight.zoomIn();

        double heading = pose.heading.toDouble();
        Pose2d aprilTagPose = limelight.getLatestPosition(Math.toDegrees(heading), pose);
        if (aprilTagPose != null) pose = aprilTagPose;


        lastPinpointPose = pose;

        poseHistory.add(pose);
        while (poseHistory.size() > 100) {
            poseHistory.removeFirst();
        }

        return pinpoint.getVelocityRR();
    }
}
