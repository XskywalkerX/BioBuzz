package org.firstinspires.ftc.teamcode.Systems.Subsystems;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Enums.Alliance;
import org.firstinspires.ftc.teamcode.Enums.ShooterStates;
import org.firstinspires.ftc.teamcode.Systems.Robot;
import static org.firstinspires.ftc.teamcode.Util.COEFFICIENTS.*;
import static org.firstinspires.ftc.teamcode.Util.Globals.*;

public class ShooterSystem {

    PIDFController pollenController = new PIDFController(
            PollenShooter.kp,
            PollenShooter.ki,
            PollenShooter.kd,
            PollenShooter.kf
    );

    PIDFController nectarController = new PIDFController(
            NectarShooter.kp,
            NectarShooter.ki,
            NectarShooter.kd,
            NectarShooter.kf
    );

    public static ShooterStates PS, CS = ShooterStates.INIT;
    ElapsedTime time = new ElapsedTime();

    public ShooterSystem() {
        CS = ShooterStates.INIT;
        PS = ShooterStates.INIT;
        time.reset();
    }

    public void update(Robot robot) {

        pollenController.setP(PollenShooter.kp);
        pollenController.setI(PollenShooter.ki);
        pollenController.setD(PollenShooter.kd);
        pollenController.setF(PollenShooter.kf);

        nectarController.setP(NectarShooter.kp);
        nectarController.setI(NectarShooter.ki);
        nectarController.setD(NectarShooter.kd);
        nectarController.setF(NectarShooter.kf);

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
