package org.firstinspires.ftc.teamcode.Commands;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LiftPID {
    double Kp = 0.02;
    double Ki = 0;
    double Kd = 1;
    double output, currentLeftPosition, currentRightPosition,error;
    DecimalFormat df = new DecimalFormat("#.##");


    public LiftPID(){

    }
}


