package org.firstinspires.ftc.teamcode.Systems.Subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Enums.HoodStates;
import org.firstinspires.ftc.teamcode.Systems.Robot;
import org.firstinspires.ftc.teamcode.Util.NECTAR_SHOOTER_COEFFICIENTS;
import org.firstinspires.ftc.teamcode.Util.POLLEN_SHOOTER_COEFFICIENTS;

public class HoodSystem {

    public static HoodStates PS, CS = HoodStates.INIT;
    ElapsedTime time = new ElapsedTime();

    public HoodSystem() {
        CS = HoodStates.INIT;
        PS = HoodStates.INIT;
        time.reset();
    }

    public void update(Robot robot, double distance) {
        switch (CS) {
            case INIT:
            case IDLE:
                robot.getPollenHood().setPosition(POLLEN_SHOOTER_COEFFICIENTS.HOOD_MIN);
                robot.getNectarHood().setPosition(NECTAR_SHOOTER_COEFFICIENTS.HOOD_MIN);
                break;
            case SHOOTING:
                //quando pollen/nectar tocar a flywheel e diminuir o rpm
                robot.getNectarHood().setPosition(getHoodAngleN(distance));
                robot.getPollenHood().setPosition(getHoodAngleP(distance));
                //beam break sensor
                //calcular a diferença entre a velocidade alvo e a atual do shooter
                double difference = POLLEN_SHOOTER_COEFFICIENTS.targetVelocity - robot.getPollenShooter().getVelocity();
                //diferença ++ posição do servo --

                //diferença -- posição do servo ++
                //constante K = o quanto muda
                //posição do servo = posição atual+(diferença * K);
                double pollenHoodPos = velCompensation(
                        getHoodAngleP(distance),
                        POLLEN_SHOOTER_COEFFICIENTS.targetVelocity,
                        robot.getPollenShooter().getVelocity(),
                        POLLEN_SHOOTER_COEFFICIENTS.K
                        );

                double nectarHoodPos = velCompensation(
                        getHoodAngleN(distance),
                        NECTAR_SHOOTER_COEFFICIENTS.targetVelocity,
                        robot.getNectarShooter().getVelocity(),
                        NECTAR_SHOOTER_COEFFICIENTS.K
                );

                robot.getPollenHood().setPosition(pollenHoodPos);
                robot.getNectarHood().setPosition(nectarHoodPos);

                break;
        }
        PS = CS;
    }
    public double velCompensation(double hoodAngle, double targetVel, double currentVel, double hoodK){
        double diference = targetVel - currentVel;
        return hoodAngle - (diference * hoodK);
    }

    public double getHoodAngleN(double goalDistance) {
        double angle = NECTAR_SHOOTER_COEFFICIENTS.D * Math.pow(goalDistance, 3) + NECTAR_SHOOTER_COEFFICIENTS.E * Math.pow(goalDistance, 2) + NECTAR_SHOOTER_COEFFICIENTS.F * goalDistance + NECTAR_SHOOTER_COEFFICIENTS.G; // cubic regression formula
        angle = Math.min(Math.max(angle, 0.11), 0.904); // clamp to valid range
        return angle; // optional offset
    }

    public double getHoodAngleP(double goalDistance) {
        double angle = POLLEN_SHOOTER_COEFFICIENTS.D * Math.pow(goalDistance, 3) + POLLEN_SHOOTER_COEFFICIENTS.E * Math.pow(goalDistance, 2) + POLLEN_SHOOTER_COEFFICIENTS.F * goalDistance + POLLEN_SHOOTER_COEFFICIENTS.G; // cubic regression formula
        angle = Math.min(Math.max(angle, 0.11), 0.904); // clamp to valid range
        return angle; // optional offset
    }
}
