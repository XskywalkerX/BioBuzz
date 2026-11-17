package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class TeleOpRavena extends LinearOpMode {
    Follower follower;

    DcMotorEx LF,LB,RF,RB;

    @Override
    public void runOpMode() throws InterruptedException {
        LF = hardwareMap.get(DcMotorEx.class, "LF");
        LF.setDirection(DcMotorSimple.Direction.REVERSE);

        LB =hardwareMap.get(DcMotorEx.class,"LB");
        LB.setDirection(DcMotorSimple.Direction.REVERSE);

        RF = hardwareMap.get(DcMotorEx.class,"RF");
        RF.setDirection(DcMotorSimple.Direction.FORWARD);

        RB = hardwareMap.get(DcMotorEx.class,"RB");
        RB.setDirection(DcMotorSimple.Direction.FORWARD);
        waitForStart();
        while (opModeIsActive()){

            LF.setPower(gamepad1.right_stick_y);
            RB.setPower(gamepad1.right_stick_y);
            LB.setPower(gamepad1.right_stick_y);
            RF.setPower(gamepad1.right_stick_y);
        }


    }
}
