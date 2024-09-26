package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

public class Limelight {
    Limelight3A limelight;
    public Limelight(HardwareMap hardwareMap, Telemetry telemetry){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        telemetry.setMsTransmissionInterval(11);

        limelight.pipelineSwitch(0);

        limelight.start();

    }

    public ArrayList<Double> getLatestPosition(){
        ArrayList<Double> positions = new ArrayList<>();
        LLResult result = limelight.getLatestResult();
        positions.add(result.getTx());
        positions.add(result.getTy());
        return positions;
    }

}
