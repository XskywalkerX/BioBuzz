package org.firstinspires.ftc.teamcode.Util;

import static org.firstinspires.ftc.teamcode.Util.Globals.MAX_VEL;
import static org.firstinspires.ftc.teamcode.Util.Globals.MIN_VEL;
import com.pedropathing.math.Pose;
import org.firstinspires.ftc.teamcode.Systems.Robot;

public class RegressionUtil {

    Pose robotPose;
    double targetY = Globals.BLUE_HIVE_Y;
    double targetX = Globals.BLUE_HIVE_X;

    Robot robot;

    public static double POLLEN_TARGET_VEL(Pose robotPose, double targetY, double targetX){
            double distance = getDistance(robotPose, targetY, targetX);
            double target_vel = (Globals.SLOPE * distance)+ Globals.INTERCEPT;
        return Math.max(MIN_VEL, Math.min(MAX_VEL, target_vel));
    }

    public static double getDistance(Pose robotPose, double targetY, double targetX){
        return Math.hypot(targetX - robotPose.x(), targetY - robotPose.y());
    }
}
