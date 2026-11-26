package org.firstinspires.ftc.teamcode.Systems.Subsystems;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Enums.ShooterStates;
import org.firstinspires.ftc.teamcode.Systems.Robot;
import org.firstinspires.ftc.teamcode.Util.FlyWheelController;
import org.firstinspires.ftc.teamcode.Util.ShooterConfig;

import static org.firstinspires.ftc.teamcode.Util.Globals.*;

public class ShooterSystem {
    public ShooterStates PS, CS;
    ElapsedTime time = new ElapsedTime();
    private ShooterConfig config;
    FlyWheelController controller;

    public ShooterSystem(ShooterConfig config) {
        CS = ShooterStates.INIT;
        PS = ShooterStates.INIT;
        this.config = config;

        controller = new FlyWheelController(
                config.kV,
                config.kA,
                config.kStatic,
                config.kP
        );

        time.reset();
    }

    public void update(Robot robot) {
        if (config.shooter == null) return;

        controller.setV(config.kV);
        controller.setA(config.kA);
        controller.setS(config.kStatic);
        controller.setP(config.kP);

        double distance = config.calculateHiveDistance(
                robot.getFollower().pose()
        );

        controller.setTargetVel(config.calculateTargetVelocity(distance));

        double output = controller.calculate(
                config.shooter.getVelocity()
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

    public void setShooterConfig(ShooterConfig config){
        this.config = config;
    }

    public ShooterStates getState() {
        return CS;
    }
    public ShooterStates getPrevState() {
        return PS;
    }
}