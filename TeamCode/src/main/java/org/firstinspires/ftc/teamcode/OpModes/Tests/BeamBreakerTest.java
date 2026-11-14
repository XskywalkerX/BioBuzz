package org.firstinspires.ftc.teamcode.OpModes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp(name = "Beam Breaker Test", group = "test")
public class BeamBreakerTest extends OpMode {
    // This must match the sensor name in the Robot Controller configuration.
    private DigitalChannel beamBreaker;
    private boolean previousBeamBroken;
    private int ballsDetected;

    @Override
    public void init() {
        beamBreaker = hardwareMap.get(DigitalChannel.class, "BeamBreaker");
        beamBreaker.setMode(DigitalChannel.Mode.INPUT);

        telemetry.addLine("Beam breaker ready");
        telemetry.addLine("Move a ball through the sensor to test it.");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Typical break-beam sensors are LOW while blocked. If yours reads
        // BLOCKED while clear, remove the ! below to invert the interpretation.
        boolean beamBroken = !beamBreaker.getState();

        // Count once when the ball first blocks the beam, not on every loop.
        if (beamBroken && !previousBeamBroken) {
            ballsDetected++;
        }
        previousBeamBroken = beamBroken;
        telemetry.addData("Beam", beamBroken ? "BLOCKED" : "CLEAR");
        telemetry.addData("Ball passes", ballsDetected);
        telemetry.addData("Status", beamBroken ? "Ball detected" : "Waiting for ball");
        telemetry.update();
    }
}
