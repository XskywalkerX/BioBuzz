package org.firstinspires.ftc.teamcode.Util;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.Enums.Alliance;


@Configurable
public class Globals {

    public static int TARGET_VEL = 1550;
    public static int INTAKE_DIRECTION = 1;
    public static double BLUE_HIVE_X = 0;
    public static double BLUE_HIVE_Y = 0;
    public static double RED_HIVE_X = 0;
    public static double RED_HIVE_Y = 0;
    public static double POLLEN_HOOD_MAX = 0.6;
    public static double POLLEN_HOOD_MIN = 0;
    public static double NECTAR_HOOD_MAX = 0.6;
    public static double NECTAR_HOOD_MIN = 0.3;

    public static double POLLEN_DOOR_MAX = 0.6;

    public static double POLLEN_DOOR_MIN = -0.6;

    public static double NECTAR_DOOR_MAX = 0;

    public static double NECTAR_DOOR_MIN = -1;


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