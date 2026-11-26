package org.firstinspires.ftc.teamcode.Util;

public class FlyWheelController {

    private double kV, kA, kStatic, kP;
    private double targetVel;
    private double vel;
    private double targetAccel;

    private double lastTargetVel = 0;

    private double lastUpdateTimestamp = 0;

    public FlyWheelController(double kV, double kA, double kStatic, double kP) {
        this.kV = kV;
        this.kA = kA;
        this.kStatic = kStatic;
        this.kP = kP;
    }

    public void setTargetVel(double targetVel) {
        this.targetVel = targetVel;
    }

    public void setTargetVel(double targetVel, double targetAccel) {
        this.targetVel = targetVel;
        this.targetAccel = targetAccel;
    }

    public void updateVel(double vel) {
        this.vel = vel;
    }

    public void setCoefficients(double kV, double kA, double kStatic, double kP) {
        this.kV = kV;
        this.kA = kA;
        this.kStatic = kStatic;
        this.kP = kP;
    }

    public void setV(double kV) {
        this.kV = kV;
    }

    public void setA(double kA) {
        this.kA = kA;
    }

    public void setS(double kStatic) {
        this.kStatic = kStatic;
    }

    public void setP(double kP) {
        this.kP = kP;
    }

    public double calculate() {

        long now = System.nanoTime();

        double dt = (lastUpdateTimestamp == 0) ? 0.0 : (now - lastUpdateTimestamp) / 1e9;
        lastUpdateTimestamp = now;

        double error = targetVel - vel;
        double accel = (targetAccel != 0.0 || dt <= 0)
                ? targetAccel
                : (targetVel - lastTargetVel) / dt;

        lastTargetVel = vel;

        double v = targetVel * kV;
        double s = Math.signum(targetVel) * kStatic;
        double p = error * kP;

        return v + s;
    }

    public double calculate(double vel) {
        updateVel(vel);
        return calculate();
    }
}
