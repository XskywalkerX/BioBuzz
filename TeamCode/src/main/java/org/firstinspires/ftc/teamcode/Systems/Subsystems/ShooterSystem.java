package org.firstinspires.ftc.teamcode.Systems.Subsystems;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Enums.Alliance;
import org.firstinspires.ftc.teamcode.Enums.ShooterStates;
import org.firstinspires.ftc.teamcode.Systems.Robot;
import org.firstinspires.ftc.teamcode.Util.NECTAR_SHOOTER_COEFFICIENTS;
import org.firstinspires.ftc.teamcode.Util.POLLEN_SHOOTER_COEFFICIENTS;

import static org.firstinspires.ftc.teamcode.Util.Globals.*;

public class ShooterSystem {

    PIDFController pollenController = new PIDFController(
            POLLEN_SHOOTER_COEFFICIENTS.kp,
            POLLEN_SHOOTER_COEFFICIENTS.ki,
            POLLEN_SHOOTER_COEFFICIENTS.kd,
            POLLEN_SHOOTER_COEFFICIENTS.kf
    );

    PIDFController nectarController = new PIDFController(
            NECTAR_SHOOTER_COEFFICIENTS.kp,
            NECTAR_SHOOTER_COEFFICIENTS.ki,
            NECTAR_SHOOTER_COEFFICIENTS.kd,
            NECTAR_SHOOTER_COEFFICIENTS.kf
    );

    public static ShooterStates PS, CS = ShooterStates.INIT;
    ElapsedTime time = new ElapsedTime();

    public ShooterSystem() {
        CS = ShooterStates.INIT;
        PS = ShooterStates.INIT;
        time.reset();
    }

    public void update(Robot robot) {

        pollenController.setP(POLLEN_SHOOTER_COEFFICIENTS.kp);
        pollenController.setI(POLLEN_SHOOTER_COEFFICIENTS.ki);
        pollenController.setD(POLLEN_SHOOTER_COEFFICIENTS.kd);
        pollenController.setF(POLLEN_SHOOTER_COEFFICIENTS.kf);

        nectarController.setP(NECTAR_SHOOTER_COEFFICIENTS.kp);
        nectarController.setI(NECTAR_SHOOTER_COEFFICIENTS.ki);
        nectarController.setD(NECTAR_SHOOTER_COEFFICIENTS.kd);
        nectarController.setF(NECTAR_SHOOTER_COEFFICIENTS.kf);

        double distance = calculateHiveDistance(robot.getFollower().pose(), Alliance.RED);

        switch (CS) {

            case INIT:
            case IDLE:
                pollenController.setSetPoint(0);
                robot.getPollenShooter().setPower(0);
                robot.getNectarShooter().setPower(0);
                break;
            case SHOOTING:
                pollenController.setSetPoint(
                        calculateTargetVelocity(distance));
                nectarController.setSetPoint(
                        calculateTargetVelocity(distance));
                robot.getPollenShooter().setPower(
                        pollenController.calculate(robot.getPollenShooter().getVelocity()));
                robot.getNectarShooter().setPower(
                        nectarController.calculate(robot.getNectarShooter().getVelocity()));
                break;

            case SHOOTING_POLLEN:
                pollenController.setSetPoint(
                        calculateTargetVelocity(distance));
                nectarController.setSetPoint(0);
                robot.getPollenShooter().setPower(
                        pollenController.calculate(robot.getPollenShooter().getVelocity()));
                robot.getNectarShooter().setPower(0);
                break;
            case SHOOTING_NECTAR:
                pollenController.setSetPoint(0);
                nectarController.setSetPoint(calculateTargetVelocity(distance));
                robot.getPollenShooter().setPower(0);
                robot.getNectarShooter().setPower(nectarController.calculate(robot.getNectarShooter().getVelocity()));

                break;
        }

        PS = CS;
    }

    double calculateHiveDistance(Pose botPose, Alliance alliance) {
        return alliance == Alliance.RED ?
                Math.sqrt(Math.pow(botPose.x() - RED_HIVE_X, 2) + Math.pow(botPose.y() - RED_HIVE_Y, 2)) :
                Math.sqrt(Math.pow(botPose.x() - BLUE_HIVE_X, 2) + Math.pow(botPose.y() - BLUE_HIVE_Y, 2));
    }
    double calculateTargetVelocity(double hive_distance) {
        return 1500;
    }
    public void setShooterState(ShooterStates state) {
        CS = state;
    }
}
