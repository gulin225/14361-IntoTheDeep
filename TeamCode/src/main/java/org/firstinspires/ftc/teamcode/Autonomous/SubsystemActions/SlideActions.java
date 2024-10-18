package org.firstinspires.ftc.teamcode.Autonomous.SubsystemActions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

public class SlideActions {
    private VerticalSlides verticalSlides;

    public SlideActions(HardwareMap hardwareMap){
        verticalSlides = new VerticalSlides(hardwareMap);
    }

    public class intake implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                verticalSlides.setSlides(VerticalSlides.slideStates.intake);
                initialized = true;
            }

            return true;
        }
    }
    public class PIDLoop implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                verticalSlides.PIDLoop();
                initialized = true;
            }

            return true;
        }
    }
    public class highRung implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                verticalSlides.setSlides(VerticalSlides.slideStates.highRung);
                initialized = true;
            }

            return true;
        }
    }
    public class highBasket implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                verticalSlides.setSlides(VerticalSlides.slideStates.highBasket);
                initialized = true;
            }

            return true;
        }
    }
    public class pullDownRung implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                verticalSlides.setSlides(VerticalSlides.slideStates.pullDown);
                initialized = true;
            }

            return true;
        }
    }

//    public class slidePIDHighBasket implements Action{
//        private boolean initialized = false;
//
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            if (!initialized) {
//                verticalSlides.setSlides(VerticalSlides.slideStates.highRung);
//                verticalSlides.PIDLoop();
//                initialized = true;
//            }
//
//            if(Math.abs(verticalSlides.getCurrent() - verticalSlides.getTarget()) < 50){
//                verticalSlides.setPowerZero();
//                return true;
//            }
//            else{
//                verticalSlides.PIDLoop();
//                return false;
//            }
//
//        }
//    }
    public Action highBasketAction(){
        return new highBasket();
    }
    public Action highRungAction(){
        return new highRung();
    }
    public Action intakeAction(){
        return new intake();
    }
    public Action pullDownRungAction(){
        return new pullDownRung();
    }
    public Action PID() { return new PIDLoop(); }
}
