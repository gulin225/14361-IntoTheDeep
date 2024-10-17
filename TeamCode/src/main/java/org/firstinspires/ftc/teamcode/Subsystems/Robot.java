package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Robot {
    public Claw claw;
    public Limelight limelight;
    public LinearRail linearRail;
    public DriveTrain driveTrain;
    public VerticalSlides verticalSlides;

    public Robot(HardwareMap hardwareMap, Telemetry telemetry){
        linearRail = new LinearRail(hardwareMap);
        claw = new Claw(hardwareMap);
     //   limelight = new Limelight(hardwareMap, telemetry);
        driveTrain = new DriveTrain(hardwareMap);
        verticalSlides = new VerticalSlides(hardwareMap);
    }

    public void init(){
        verticalSlides.setSlides(VerticalSlides.slideStates.intake);
        claw.moveClaw(Claw.clawStates.wristIntake);
        claw.moveClaw(Claw.clawStates.outtake);
        linearRail.moveRail(LinearRail.linearRailStates.outtake);

    }

    public void intake(){
        linearRail.moveRail(LinearRail.linearRailStates.intake);
        claw.moveClaw(Claw.clawStates.intake);
    }

    public void outtake(){
        linearRail.moveRail(LinearRail.linearRailStates.outtake);
        claw.moveClaw(Claw.clawStates.outtake);
    }
}
