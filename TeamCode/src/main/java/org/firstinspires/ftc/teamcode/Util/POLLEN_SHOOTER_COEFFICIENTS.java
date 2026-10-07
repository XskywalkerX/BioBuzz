package org.firstinspires.ftc.teamcode.Util;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class POLLEN_SHOOTER_COEFFICIENTS {

    public static double K = 0.005; //hood constant
    public static double kp = 0.00010;
    public static double ki = 0.00005;
    public static double kd = 0.0001;
    public static double kf = 0.000433;
    public static double targetVelocity = 1550.0;
    public static double HOOD_MAX = 1.0;
    public static double HOOD_MIN = 0.0;

    public static double A = 0;
    public static double B = 0;
    public static double C = 0;
    public static double D = 0;
    public static double E = 0;
    public static double F = 0;
    public static double G = 0;
}