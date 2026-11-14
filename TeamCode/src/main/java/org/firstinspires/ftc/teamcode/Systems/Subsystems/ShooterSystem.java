package org.firstinspires.ftc.teamcode.Systems.Subsystems;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Enums.ShooterStates;
import org.firstinspires.ftc.teamcode.Systems.Robot;
import org.firstinspires.ftc.teamcode.Util.ShooterConfig;

import static org.firstinspires.ftc.teamcode.Util.Globals.*;

public class ShooterSystem {

    public ShooterStates PS, CS;
    ElapsedTime time = new ElapsedTime();
    private final ShooterConfig config;
    PIDFController controller;

    public ShooterSystem(ShooterConfig config) {
        CS = ShooterStates.INIT;
        PS = ShooterStates.INIT;
        this.config = config;
        controller = new PIDFController(
                config.kp,
                config.ki,
                config.kd,
                config.kf
        );

        time.reset();
    }

    public void update(Robot robot) {
        if (config.shooter == null) return;

        if (config.kp == 0 && config.ki == 0 && config.kd == 0 && config.kf == 0) {
            config.kp = 0.001;
            config.kf = 0.0005;
        }

        controller.setP(config.kp);
        controller.setI(config.ki);
        controller.setD(config.kd);
        controller.setF(config.kf);

        double distance = config.calculateHiveDistance(
                robot.getFollower().pose(),
                alliance
        );

        double output = controller.calculate(
                config.shooter.getVelocity(),
                config.calculateTargetVelocity(distance)
        );

        switch (CS) {

            case INIT:
            case IDLE:
                config.shooter.setPower(0);
                break;
            case SHOOTING:
                config.shooter.setPower(-output);
                break;
        }

        PS = CS;
    }
    public void setShooterState(ShooterStates state) {
        CS = state;
    }

    public ShooterStates getState() {
        return CS;
    }
    public ShooterStates getPrevState() {
        return PS;
    }
}