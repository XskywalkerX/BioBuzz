package org.firstinspires.ftc.teamcode.OpModes.Tests;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Enums.LimelightStates;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@TeleOp(name = "HiveVisionPath", group = "Hive Vision")
public class HiveVisionPath extends LinearOpMode {
    private static final String LIMELIGHT_NAME = "limelight";
    public static int PIPELINE_INDEX = 7; // Change to your neural networked pipeline
    LimelightStates state = LimelightStates.SEARCHING;
    private static final int CLASS_YELLOW = 0;
    private static final int CLASS_RED = 1;
    private static final int CLASS_BLUE = 2;
    private Set<Integer> allowedClasses =
            new HashSet<>(Arrays.asList(CLASS_RED, CLASS_BLUE));
    public static double MIN_CONF = 0.30;
    private static final long MAX_STALENESS_US = 120_000;
    public static double LOCK_GATE_DEG = 12.0;
    public static long LOCK_LOST_MS = 300;
    // If your robot drives backward, change this to a negative value (e.g., -0.40)
    public static double CHASE_FWD_POWER = -0.25;
    public static double TURN_KP = -0.025;
    public static double MIN_TURN = 0.25;
    public static double MAX_TURN = 0.55;
    public static double SEARCH_TURN = 0.20;
    private static final double TA_DISTANCE_K = 55.0 * Math.sqrt(0.280);
    private final ElapsedTime lastSeenTimer = new ElapsedTime();
    private final ElapsedTime pickupTimer = new ElapsedTime();
    private final ElapsedTime lockAge = new ElapsedTime();
    public static double PICKUP_TA = 0.0;
    public static double COAST_MAX_DIST = 26.0;
    public static double COAST_POWER = 0.25;
    public static long COAST_MS = 450;
    public static long TARGET_LOSS_GRACE_MS = 250; // Prevents flickering when target is lost briefly
    public static long PICKUP_DWELL_MS = 600;
    public static double INTAKE_POWER = 1.0;
    private double lastSeenDist = Double.MAX_VALUE;
    private Double lockTx = null;
    private Double lockTy = null;
    private boolean rightBumperPrev = false;
    private boolean autoChaseActive = false;

    DcMotorEx  lf, rf, lb, rb;
    DcMotor intake;
    Follower follower;
    Limelight3A limelight;


    @Override
    public void runOpMode() throws InterruptedException {
        intake = hardwareMap.get(DcMotor.class, "intake");
        follower = Constants.create(hardwareMap);

        limelight = hardwareMap.get(Limelight3A.class, LIMELIGHT_NAME);
        limelight.pipelineSwitch(PIPELINE_INDEX);
        limelight.setPollRateHz(100);
        limelight.start();

        selectAllowedColors();

        telemetry.addLine("RightBumper to toggle Auto-Chase ON/OFF.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()){
            boolean rightBumperCurrent = gamepad1.right_bumper;
            if (rightBumperCurrent && !rightBumperPrev) {
                autoChaseActive = !autoChaseActive;
            }
            rightBumperPrev = rightBumperCurrent;

            if (autoChaseActive) {
                autoChase();
                telemetry.addData("Mode", "AUTO CHASE ACTIVE");
            } else {
                state = LimelightStates.SEARCHING;
                clearLock();
                ManualDrive();
                telemetry.addData("Mode", "MANUAL DRIVE");
            }
            telemetry.addData("Current State:", state);
            telemetry.update();

            }
             limelight.stop();
             drive(0, 0, 0);
             setIntake(0);
        }

    private void selectAllowedColors() {
        while (!isStarted() && !isStopRequested()) {
            if (gamepad1.dpad_up) allowedClasses = setOf(CLASS_RED);
            if (gamepad1.dpad_down) allowedClasses = setOf(CLASS_BLUE);
            if (gamepad1.dpad_left) allowedClasses = setOf(CLASS_RED, CLASS_BLUE);
//            if (gamepad1.dpad_right) allowedClasses = setOf(CLASS_RED, CLASS_BLUE, CLASS_YELLOW);

            telemetry.addLine("D-pad: UP red | DOWN blue | LEFT red+blue | RIGHT all");
            telemetry.addData("targets", describeAllowed());
            telemetry.update();
        }
    }
    private static Set<Integer> setOf(Integer... ids) {
        return new HashSet<>(Arrays.asList(ids));
    }
    private void beginPickup() {
        state = LimelightStates.PICKUP;
        pickupTimer.reset();
        drive(-1, 1, 0);
        setIntake(INTAKE_POWER);
    }
    private void autoChase() {
        if (state == LimelightStates.PICKUP) {
            drive(-1, 0, 0);
            setIntake(INTAKE_POWER);

            if (pickupTimer.milliseconds() >= PICKUP_DWELL_MS) {
                state = LimelightStates.SEARCHING;
                lastSeenDist = Double.MAX_VALUE;
                clearLock();
            }
            return;
        }

        LLResultTypes.DetectorResult target = findTarget();

        if (target != null) {
            double tx = target.getTargetXDegrees();
            double ty = target.getTargetYDegrees();
            double ta = target.getTargetArea();
            double estimatedDist = distanceFromTa(ta);

            lastSeenTimer.reset();
            lastSeenDist = estimatedDist;

            double turn = Range.clip(tx * TURN_KP, -MAX_TURN, MAX_TURN);
            if (Math.abs(turn) < MIN_TURN) {
                turn = Math.copySign(MIN_TURN, tx);
            }

            double forwad = CHASE_FWD_POWER;

            state = LimelightStates.CHASING;
            setIntake(INTAKE_POWER);
            drive(forwad, 0, turn);

            telemetry.addData("ball", "tx=%+.1f dist≈%.1f", tx, estimatedDist);
            return;
        }

        // Grace period check: if we just lost the ball briefly, keep going straight/intaking instead of flickering
        if (lastSeenTimer.milliseconds() < TARGET_LOSS_GRACE_MS && state == LimelightStates.CHASING) {
            setIntake(INTAKE_POWER);
            drive(CHASE_FWD_POWER, -1, 0);
            telemetry.addData("drive", "grace period maintaining chase");
            return;
        }

        boolean wasClose = (state == LimelightStates.CHASING || state == LimelightStates.COASTING)
                && lastSeenDist <= COAST_MAX_DIST;

        if (wasClose) {
            if (lastSeenTimer.milliseconds() < COAST_MS) {
                state = LimelightStates.COASTING;
                setIntake(INTAKE_POWER);
                drive(COAST_POWER, 0, 0);
            } else {
                beginPickup();
            }
            return;
        }

        state = LimelightStates.SEARCHING;
        setIntake(0);
        drive(-1, 1, SEARCH_TURN);
    }
    private String describeAllowed() {
        if (allowedClasses.size() == 1 && allowedClasses.contains(CLASS_RED)) return "RED only";
        if (allowedClasses.size() == 1 && allowedClasses.contains(CLASS_BLUE)) return "BLUE only";
        if (allowedClasses.size() == 2) return "Colours = RED + BLUE";
        return "Colours = RED + BLUE + YELLOW";
    }

    private void dropStaleLock() {
        if (lockTx != null && lockAge.milliseconds() > LOCK_LOST_MS) {
            clearLock();
        }
    }
    private LLResultTypes.DetectorResult findTarget() {
        LLResult result = limelight.getLatestResult();

        if (result == null || result.getStaleness() > MAX_STALENESS_US) {
            dropStaleLock();
            return null;
        }

        List<LLResultTypes.DetectorResult> detections = result.getDetectorResults();
        if (detections == null || detections.isEmpty()) {
            dropStaleLock();
            return null;
        }

        LLResultTypes.DetectorResult closest = null;
        LLResultTypes.DetectorResult lockMatch = null;
        double bestLockJump = Double.MAX_VALUE;

        for (LLResultTypes.DetectorResult detection : detections) {
            if (!allowedClasses.contains(detection.getClassId())) continue;
            if (detection.getConfidence() < MIN_CONF) continue;

            if (closest == null || detection.getTargetArea() > closest.getTargetArea()) {
                closest = detection;
            }

            if (lockTx != null) {
                double jump = Math.hypot(detection.getTargetXDegrees() - lockTx, detection.getTargetYDegrees() - lockTy);
                if (jump < bestLockJump) {
                    bestLockJump = jump;
                    lockMatch = detection;
                }
            }
        }

        if (closest == null) {
            dropStaleLock();
            return null;
        }

        LLResultTypes.DetectorResult chosen = (lockMatch != null && bestLockJump <= LOCK_GATE_DEG) ? lockMatch : closest;

        lockTx = chosen.getTargetXDegrees();
        lockTy = chosen.getTargetYDegrees();
        lockAge.reset();

        return chosen;
    }
    private void clearLock() {
        lockTx = null;
        lockTy = null;
    }
    private double distanceFromTa(double taPercent) {
        if (taPercent <= 0.0) return Double.MAX_VALUE;
        return TA_DISTANCE_K / Math.sqrt(taPercent);
    }

    private void ManualDrive(){
        double forwad = -gamepad1.left_stick_y;
        double lateral = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;

        follower.manual(forwad, lateral, turn);
        follower.update();
        setIntake(gamepad1.left_bumper ? INTAKE_POWER : 0);
    }

    private void drive(double forwad, double strafe, double turn){
        double pLf = forwad + strafe + turn;
        double pRf = forwad - strafe - turn;
        double pLb = forwad - strafe + turn;
        double pRb = forwad + strafe - turn;
        double largestPower = Math.max(1.0, Math.max(Math.max(Math.abs(pLf), Math.abs(pRf)), Math.max(Math.abs(pLb), Math.abs(pRb))));
        lf.setPower(pLf / largestPower);
        rf.setPower(pRf / largestPower);
        lb.setPower(pLb / largestPower);
        rb.setPower(pRb / largestPower);
    }

    private void setIntake(double power) {
        if (intake != null) {
            intake.setPower(power);
        }
    }

}
