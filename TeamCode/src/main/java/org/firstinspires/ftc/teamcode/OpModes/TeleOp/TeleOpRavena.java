package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Tank Drive")
public class TeleOpRavena extends OpMode {
    private DcMotorEx leftFront;
    private DcMotorEx rightFront;
    private DcMotorEx leftRear;
    private DcMotorEx rightRear;

    @Override
    public void init() {
        leftFront = hardwareMap.get(DcMotorEx.class, "Motor1");
        rightFront = hardwareMap.get(DcMotorEx.class, "Motor2");
        leftRear = hardwareMap.get(DcMotorEx.class, "Motor3");
        rightRear = hardwareMap.get(DcMotorEx.class, "Motor4");

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("Drive with left stick: forward/back to move, left/right to steer");
        telemetry.update();
    }

    @Override
    public void loop() {

        // Arcade drive: forward/back drives both sides; horizontal input steers.
        // FTC gamepad stick Y is negative when pushed forward.
        double drive = gamepad1.left_stick_y;
        double turn = -gamepad1.right_stick_x;
        double leftPower = drive + turn;
        double rightPower = drive - turn;

        // Keep the left/right power ratio while limiting both values to [-1, 1].
        double maxMagnitude = Math.max(1.0, Math.max(Math.abs(leftPower), Math.abs(rightPower)));
        leftPower /= maxMagnitude;
        rightPower /= maxMagnitude;

        leftFront.setPower(leftPower);
        leftRear.setPower(leftPower);
        rightFront.setPower(rightPower);
        rightRear.setPower(-rightPower);

        telemetry.addData("Drive", "%.2f", drive);
        telemetry.addData("Turn", "%.2f", turn);
        telemetry.addData("Left power", "%.2f", leftPower);
        telemetry.addData("Right power", "%.2f", rightPower);
        telemetry.update();
    }
}
