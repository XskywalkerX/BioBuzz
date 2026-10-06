package org.firstinspires.ftc.teamcode.Util;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class COEFFICIENTS {

    @Configurable
    public static class PollenShooter {
        public static double kp = 0.00010;
        public static double ki = 0.00005;
        public static double kd = 0.0001;
        public static double kf = 0.000433;
        public static double targetVelocity = 1550.0;
    }

    @Configurable
    public static class NectarShooter {
        public static double kp = 0.0005;
        public static double ki = 0.00005;
        public static double kd = 0.0001;
        public static double kf = 0.000692;
        public static double targetVelocity = 1300.0;
    }
}
