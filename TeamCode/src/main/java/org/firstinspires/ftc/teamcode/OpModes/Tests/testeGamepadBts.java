package org.firstinspires.ftc.teamcode.OpModes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Systems.GamepadBoladao2;

@TeleOp(name = "testeGamepadBts", group = "Tests")
public class testeGamepadBts extends LinearOpMode {

    GamepadBoladao2 gamepad;

    @Override
    public void runOpMode() throws InterruptedException {
        gamepad = new GamepadBoladao2(gamepad1);
        waitForStart();
        while (opModeIsActive()) {
            gamepad.readGamepad(gamepad1);
            telemetry.addData("ONE", gamepad.getGamepadOne().square);
            telemetry.addData("TWO", gamepad.getGamepadTwo().square);
            telemetry.update();
        }
    }
}
