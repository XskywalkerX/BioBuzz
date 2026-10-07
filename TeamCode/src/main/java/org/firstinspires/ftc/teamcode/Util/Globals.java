package org.firstinspires.ftc.teamcode.Util;

import org.firstinspires.ftc.teamcode.Enums.Alliance;

public class Globals {
    public static int INTAKE_DIRECTION = 1;
    public static int SHOOTER_POLLEN_DIRECTION = -1;
    public static int HOOD_POLLEN_DIRECTION = 1;
    public static int HOOD_NECTAR_DIRECTION = 1;
    public static int SHOOTER_NECTAR_DIRECTION = 1;

    public static double BLUE_HIVE_X = 0;
    public static double BLUE_HIVE_Y = 0;

    public static double RED_HIVE_X = 0;
    public static double RED_HIVE_Y = 0;

    public static ShooterConfig POLLEN_SHOOTER = new ShooterConfig(
            null, 0, 0, 0,0, 1.0, 0.0, 0.005, 0, 0, 0 ,0
    );
    public static ShooterConfig NECTAR_SHOOTER = new ShooterConfig(
            null, 0, 0, 0, 0.0, 1.0, 0.0, 0.005, 0, 0, 0, 0
    );

    public static Alliance alliance = Alliance.BLUE;

    public void setAlliance(Alliance a) {
        alliance = a;
    }
}
