package org.firstinspires.ftc.teamcode.Util;

import static org.firstinspires.ftc.teamcode.Util.Globals.BLUE_HIVE_X;
import static org.firstinspires.ftc.teamcode.Util.Globals.BLUE_HIVE_Y;
import static org.firstinspires.ftc.teamcode.Util.Globals.POSE_HIVE;
import static org.firstinspires.ftc.teamcode.Util.Globals.RED_HIVE_X;
import static org.firstinspires.ftc.teamcode.Util.Globals.RED_HIVE_Y;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.Enums.Alliance;

public class ShooterConfig {
    Follower follower;
    Pose robotPose;
    public double kV,kA,kStatic,kP,a,b,c,d;
    public DcMotorEx shooter;

    public ShooterConfig(
            DcMotorEx shooter,

            double kV,
            double kA,
            double kStatic,
            double kP,
            double a,
            double b,
            double c,
            double d
    ) {
        this.kV = kV;
        this.kA = kA;
        this.kStatic = kStatic;
        this.kP = kP;
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

    public void setV(double kV){
        this.kV = kV;
    }
    public void setA(double kA){
        this.kA = kA;
    }
    public void setS(double kStatic){
        this.kStatic = kStatic;
    }
    public void setP(double kP){
        this.kP = kP;
    }
}
