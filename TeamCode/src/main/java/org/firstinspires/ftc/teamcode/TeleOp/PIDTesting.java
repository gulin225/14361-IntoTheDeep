package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.SlidePID;
@TeleOp(name = "PID")

public class PIDTesting extends OpMode {
    SlidePID slide;

    @Override
    public void init() {
        slide = new SlidePID(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        slide.loop();
    }
}
