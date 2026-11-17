package org.firstinspires.ftc.teamcode.Util;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.Enums.Alliance;


@Configurable
public class Globals {

    public static int TARGET_VEL = 1400;
    public static int INTAKE_DIRECTION = 1;
    public static double BLUE_HIVE_X = 84;
    public static double START_X = 81.8;
    public static double START_Y = 9.5;
    public static double START_H = 90;

    //-28
    public static double BLUE_HIVE_Y = 81;
    public static Pose POSE_HIVE = new Pose(BLUE_HIVE_X, BLUE_HIVE_Y);
    public static Pose POSE_ROBOT = new Pose(START_X, START_Y, Math.toRadians(START_H));

    public static double RED_HIVE_X = 57;

    //+28
    public static double RED_HIVE_Y = 58;
    public static double POLLEN_HOOD_MAX = 0;
    public static double POLLEN_HOOD_MIN = 0.4;
    public static double NECTAR_HOOD_MAX = 0.6;
    public static double NECTAR_HOOD_MIN = 0.3;
    public static double PORTAPOLLENABERTO = -0.4;
    public static double PORTAPOLLENFECHADO = 0.2;
    public static double PORTANECTARABERTO = 0;
    public static double PORTANECTARFECHADO = 0;
    public static double POLLEN_DOOR_MAX = 0.6;
    public static double POLLEN_DOOR_MIN = -0.6;
    public static double NECTAR_DOOR_MAX = 0;
    public static double NECTAR_DOOR_MIN = -1;


    public static ShooterConfig POLLEN_SHOOTER = new ShooterConfig(
            null, -0.0030, 0.001, 0.00001, 0, POLLEN_HOOD_MAX, POLLEN_HOOD_MIN, 0.005, 0, 0, 0, 0
    );
    public static ShooterConfig NECTAR_SHOOTER = new ShooterConfig(
            null, -0.25, 0.001, 0, 0.05, NECTAR_HOOD_MAX, NECTAR_HOOD_MIN, 0.005, 0, 0, 0, 0
    );
    public static Alliance alliance = Alliance.BLUE;
    public void setAlliance(Alliance a) {
        alliance = a;
    }
}