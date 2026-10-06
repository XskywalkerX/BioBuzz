package org.firstinspires.ftc.teamcode.Util;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class POLLEN_SHOOTER_COEFFICIENTS {
    public static double kp = 0.00010;
    public static double ki = 0.00005;
    public static double kd = 0.0001;
    public static double kf = 0.000433;
    public static double targetVelocity = 1550.0;
    public static double HOOD_MAX = 1.0;
    public static double HOOD_MIN = 0.0;
}