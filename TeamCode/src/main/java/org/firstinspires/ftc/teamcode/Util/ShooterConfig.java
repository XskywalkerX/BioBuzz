package org.firstinspires.ftc.teamcode.Util;

import static org.firstinspires.ftc.teamcode.Util.Globals.BLUE_HIVE_X;
import static org.firstinspires.ftc.teamcode.Util.Globals.BLUE_HIVE_Y;
import static org.firstinspires.ftc.teamcode.Util.Globals.POSE_HIVE;
import static org.firstinspires.ftc.teamcode.Util.Globals.RED_HIVE_X;
import static org.firstinspires.ftc.teamcode.Util.Globals.RED_HIVE_Y;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.Enums.Alliance;

public class ShooterConfig {
    public double kp,ki,kd,kf,hMax,hMin,hK,a,b,c,d;
    public DcMotorEx shooter;
    public ShooterConfig(
            DcMotorEx shooter,
            double kp,
            double ki,
            double kd,
            double kf,
            double hMax,
            double hMin,
            double hK,
            double a,
            double b,
            double c,
            double d
    ) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.kf = kf;
        this.hK = hK;
        this.hMax = hMax;
        this.hMin = hMin;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.shooter = shooter;
    }

    public double calculateTargetVelocity(double hive_distance) {
        return Globals.TARGET_VEL; // 1500
    }

    public double calculateHiveDistance(Pose botPose) {
        return Math.sqrt(Math.pow(botPose.x() - POSE_HIVE.x(), 2) + Math.pow(botPose.y() - POSE_HIVE.y(), 2));
    }

    public void setShooter(DcMotorEx shooter) {
        this.shooter = shooter;
    }
}
