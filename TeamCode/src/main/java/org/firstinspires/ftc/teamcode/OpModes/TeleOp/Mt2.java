package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Mt2 extends OpMode {

    DcMotorEx motor;


    @Override
    public void init() {

        motor = hardwareMap.get(DcMotorEx.class, "motor");

    }

    @Override
    public void loop() {
    motor.setPower(1);
    }
}
