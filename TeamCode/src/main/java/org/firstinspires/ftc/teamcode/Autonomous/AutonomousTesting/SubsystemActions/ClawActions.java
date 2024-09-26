package org.firstinspires.ftc.teamcode.Autonomous.AutonomousTesting.SubsystemActions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Subsystems.Claw;

public class ClawActions {
    private Claw claw;
    public ClawActions(HardwareMap hardwareMap){
        claw = new Claw(hardwareMap);
    }
    public class closeClaw implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
               claw.closeClaw();
                initialized = true;
            }

            return true;
        }
    }
    public class openClaw implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                claw.openClaw();
                initialized = true;
            }

            return true;
        }
    }

    public Action closeClawAction() {
        return new closeClaw();
    }

    public Action openClawAction(){
        return new openClaw();
    }
}

