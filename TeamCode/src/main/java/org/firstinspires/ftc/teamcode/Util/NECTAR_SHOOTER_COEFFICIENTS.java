package org.firstinspires.ftc.teamcode.Util;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class NECTAR_SHOOTER_COEFFICIENTS {
    public static double kp = 0.0005;
    public static double ki = 0.00005;
    public static double kd = 0.0001;
    public static double kf = 0.000692;
    public static double targetVelocity = 1300.0;
    public static double HOOD_MAX = 1.0;
    public static double HOOD_MIN = 0.0;
}