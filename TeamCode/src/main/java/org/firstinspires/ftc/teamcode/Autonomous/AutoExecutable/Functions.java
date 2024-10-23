package org.firstinspires.ftc.teamcode.Autonomous.AutoExecutable;

import com.acmerobotics.roadrunner.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Autonomous.RRdrives.PinpointDrive;

import java.lang.Math;

public class Functions {
  PinpointDrive drive;
  Telemetry telemetry;
  public Functions(PinpointDrive drivee, Telemetry telemetryy){
    drive = drivee;
    telemetry = telemetryy;
  }

  public void updateTelemetry(AutoStates state){
    //shorter telemetry
    telemetry.addData("State", state);
    telemetry.addLine(drive.pose.toString());
    telemetry.update();
  }

  public Action createSingleSpline(Pose2d start, Pose2d end, double startTangent, double endTangent) {
    //shorter spline
    return drive.actionBuilder(start)
            .setTangent(Math.toRadians(startTangent))
            .splineToLinearHeading(end, Math.toRadians(endTangent))
            .build();
  }

}
