package org.firstinspires.ftc.teamcode.OpModes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "servotest")
public class Servos extends LinearOpMode {


    Servo servo1, servo2, servo3, servo4;
    @Override
    public void runOpMode() throws InterruptedException {

        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class,"servo2");
        servo3 = hardwareMap.get(Servo.class,"servo3");
        servo4 = hardwareMap.get(Servo.class,"servo4");

        waitForStart();

        while (opModeIsActive()) {
            if(gamepad1.circle) {
                servo1.setPosition(1);
                telemetry.addLine("porta 3 dos expansion");
                telemetry.update();
            }else {
                servo1.setPosition(0);
            }
            if(gamepad1.cross){
                servo2.setPosition(1);
                telemetry.addLine("porta 2 do expansion");
                telemetry.update();
            }else  {
                servo2.setPosition(0);
            }
            if(gamepad1.triangle){
                servo3.setPosition(1);
                telemetry.addLine("porta 0 do expansion");
                telemetry.update();
            }else  {
                servo3.setPosition(0);
            }
            if(gamepad1.square){
                servo4.setPosition(1);
                telemetry.addLine("porta 5 do expansion");
                telemetry.update();
            }else  {
                servo4.setPosition(0);
            }

        }
    }
}
