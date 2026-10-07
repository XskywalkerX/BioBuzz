package org.firstinspires.ftc.teamcode.Systems.Subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Enums.HoodStates;
import org.firstinspires.ftc.teamcode.Enums.ShooterStates;
import org.firstinspires.ftc.teamcode.Systems.Robot;
import org.firstinspires.ftc.teamcode.Util.Globals;
import org.firstinspires.ftc.teamcode.Util.ShooterConfig;

public class HoodSystem {
    public HoodStates PS, CS;
    ElapsedTime time = new ElapsedTime();

    ShooterConfig config;

    Servo hood;

    public HoodSystem(ShooterConfig config, Servo hood) {
        CS = HoodStates.INIT;
        PS = HoodStates.INIT;
        this.config = config;
        this.hood = hood;
        time.reset();
    }

    public void update(Robot robot, ShooterSystem shooterSystem) {
        switch (CS) {
            case INIT:
            case IDLE:
                if (shooterSystem.getState() != ShooterStates.SHOOTING) {
                    hood.setPosition(config.hMin);
                } else {
                    setState(HoodStates.SHOOTING);
                }
                break;
            case SHOOTING:
                if (shooterSystem.getState() == ShooterStates.SHOOTING) {
                    double distance = config.calculateHiveDistance(
                            robot.getFollower().pose(),
                            Globals.alliance
                    );

                    double hoodAngle = calculateHoodAngle(
                            distance,
                            config.a,
                            config.b,
                            config.c,
                            config.d
                    );


                    hood.setPosition(velCompensation(
                            hoodAngle,
                            config.calculateTargetVelocity(distance),
                            config.shooter.getVelocity(),
                            config.hK
                    ));
                } else {
                    setState(HoodStates.IDLE);
                }

                break;
        }
        PS = CS;
    }

    public double velCompensation(double hoodAngle, double targetVel, double currentVel, double hoodK) {
        double error = targetVel - currentVel;
        return hoodAngle - (error * hoodK);
    }

    public double calculateHoodAngle(double goalDistance, double a, double b, double c, double d) {
        double angle = a * Math.pow(goalDistance, 3) + b * Math.pow(goalDistance, 2) + c * goalDistance + d;
        angle = Math.min(Math.max(angle, 0.11), 0.904);
        return angle;
    }

    public void setState(HoodStates state) {
        CS = state;
    }

    public HoodStates getState() {
        return CS;
    }

    public HoodStates getPrevState() {
        return PS;
    }
}
