package org.firstinspires.ftc.teamcode.OpModes.Tests;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;


@TeleOp(name = "odometryTest")
public class odometryTest extends LinearOpMode {

    Follower follower;

    private TelemetryManager panelsTelemetry;


    @Override
    public void runOpMode() throws InterruptedException {
        follower = Constants.create(hardwareMap);
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();


        waitForStart();

        while (opModeIsActive()){
            follower.update();


            double forward = -gamepad1.left_stick_y;
            double lateral = -gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            follower.manual(forward, lateral, turn);
            follower.update();



            telemetry.addData("Robot x", follower.pose().x());
            telemetry.addData("robot y", follower.pose().y());
            telemetry.addData("robot heading", follower.pose().heading());
            telemetry.update();



        }

    }
}
