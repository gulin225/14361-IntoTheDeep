package org.firstinspires.ftc.teamcode.Autonomous.SubsystemActions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Subsystems.LinearRail;

public class LinearRailActions {
    private LinearRail linearRail;

    public LinearRailActions(HardwareMap hardwareMap){
        linearRail = new LinearRail(hardwareMap);
    }

    public class intake implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                linearRail.moveRail(LinearRail.linearRailStates.intake);
                initialized = true;
            }

            return true;
        }
    }

    public class outtake implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                linearRail.moveRail(LinearRail.linearRailStates.outtake);
                initialized = true;
            }

            return true;
        }
    }

    public Action outtakeAction(){
        return new outtake();
    }
    public Action intakeAction(){
        return new intake();
    }
}
