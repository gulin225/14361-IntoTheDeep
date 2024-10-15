package org.firstinspires.ftc.teamcode.Subsystems;

import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ArmPID {
    private PIDController controller;

    public static double p = 0, i = 0, d = 0;

    public static double f = 0;

    public static int target =0;

    public final double ticks_in_degrees = 700/180; //num of ticks per rotation we need to find this out

    private DcMotorEx arm_motor;

    public ArmPID(HardwareMap hardwareMap){
        controller = new PIDController(p,i,d);
        arm_motor = hardwareMap.get(DcMotorEx.class, "arm");
    }

    public void loop(){
        controller.setPID(p,i,d);
        int armPos = arm_motor.getCurrentPosition();

        double pid = controller.calculate(armPos, target);

        double ff = Math.cos(Math.toRadians(target/ticks_in_degrees)) * f;

        double power = pid + ff;

        arm_motor.setPower(power);

    }
}
