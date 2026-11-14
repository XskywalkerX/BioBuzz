package org.firstinspires.ftc.teamcode.Util;

import org.firstinspires.ftc.teamcode.Enums.Alliance;

public class Globals {
    public static int INTAKE_DIRECTION = 1;
    public static double BLUE_HIVE_X = 0;
    public static double BLUE_HIVE_Y = 0;
    public static double RED_HIVE_X = 0;
    public static double RED_HIVE_Y = 0;
    public static double POLLEN_HOOD_START = 0.0;
    public static double NECTAR_HOOD_MIN_POSITION = 0.30;
    public static double NECTAR_HOOD_START = NECTAR_HOOD_MIN_POSITION;
    public static double MAX_HOOD_POSITION = 0.60;

    public static ShooterConfig POLLEN_SHOOTER = new ShooterConfig(
            null, -0.030, 0.001, 0.00001, 0, 0.75, 0.0, 0.005, 0, 0, 0, 0
    );
    public static ShooterConfig NECTAR_SHOOTER = new ShooterConfig(
            null, -0.25, 0.001, 0, 0, 1.0, 0.0, 0.005, 0, 0, 0, 0
    );
    public static Alliance alliance = Alliance.BLUE;
    public void setAlliance(Alliance a) {
        alliance = a;
    }
}