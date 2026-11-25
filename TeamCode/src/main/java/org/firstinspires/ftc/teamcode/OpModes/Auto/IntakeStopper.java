package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

/** Runs the intake until four pollen balls have passed the beam sensor. */
@Autonomous(name = "IntakeStopper", group = "test")
public class IntakeStopper extends LinearOpMode {
    private static final String BEAM_NAME = "BeamSensor";
    private static final String INTAKE_NAME = "intake";
    private static final int POLLEN_LIMIT = 4;
    private static final double INTAKE_POWER = 1.0;

    @Override
    public void runOpMode() {
        DigitalChannel beamBreaker = hardwareMap.get(DigitalChannel.class, BEAM_NAME);
        beamBreaker.setMode(DigitalChannel.Mode.INPUT);
        DcMotor intake = hardwareMap.get(DcMotor.class, INTAKE_NAME);

        int pollensCollected = 0;
        boolean previousBeamBroken = !beamBreaker.getState();

        telemetry.addLine("Ready: intake will run until four pollens pass the beam.");
        telemetry.addData("Beam", previousBeamBroken ? "BLOCKED" : "CLEAR");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) {
            return;
        }
        intake.setPower(INTAKE_POWER);
        while (opModeIsActive() && pollensCollected < POLLEN_LIMIT) {
            // Typical break-beam sensors read LOW while blocked.
            boolean beamBroken = !beamBreaker.getState();
            // Count one pollen when the beam changes from clear to blocked.
            if (beamBroken && !previousBeamBroken) {
                pollensCollected++;
            }
            previousBeamBroken = beamBroken;

            telemetry.addData("Beam", beamBroken ? "BLOCKED" : "CLEAR");
            telemetry.addData("Pollens", "%d / %d", pollensCollected, POLLEN_LIMIT);
            telemetry.addData("Intake", "RUNNING");
            telemetry.update();
        }

        intake.setPower(0.0);
        telemetry.addData("Pollens", "%d / %d", pollensCollected, POLLEN_LIMIT);
        telemetry.addLine(pollensCollected >= POLLEN_LIMIT
                ? "Four pollens collected. Intake stopped."
                : "OpMode stopped. Intake stopped.");
        telemetry.update();
    }
}
