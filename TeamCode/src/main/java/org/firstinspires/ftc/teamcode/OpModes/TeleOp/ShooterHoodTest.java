package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Enums.ShooterStates;
import org.firstinspires.ftc.teamcode.Systems.Robot;
import org.firstinspires.ftc.teamcode.Systems.Subsystems.ShooterSystem;
import org.firstinspires.ftc.teamcode.Util.Globals;

@TeleOp(name = "SHT")
public class ShooterHoodTest extends LinearOpMode {
    Robot robot;
    DcMotorEx intake;
    ShooterSystem nectarShooter;
    ShooterSystem pollenShooter;

    @Override
    public void runOpMode() throws InterruptedException {

        intake = hardwareMap.get(DcMotorEx.class, "intake");

        Globals.NECTAR_SHOOTER.setShooter(robot.getNectarShooter());
        Globals.POLLEN_SHOOTER.setShooter(robot.getPollenShooter());

        nectarShooter = new ShooterSystem(Globals.NECTAR_SHOOTER);
        pollenShooter = new ShooterSystem(Globals.POLLEN_SHOOTER);

        robot.getPortaPollen().setPosition(0.0);
        robot.getPortaNectar().setPosition(0.0);

        robot.getNectarShooter().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.getPollenShooter().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        waitForStart();

        while (opModeIsActive()) {

            if(gamepad1.triangleWasPressed()){
                intake.setPower(1);
            }else if(gamepad1.triangleWasReleased()){
                intake.setPower(0);
            }

            if (gamepad1.left_bumper) {
                pollenShooter.setShooterState(ShooterStates.SHOOTING);
            } else {
                pollenShooter.setShooterState(ShooterStates.IDLE);
            }

            if(gamepad1.right_bumper){
                nectarShooter.setShooterState(ShooterStates.SHOOTING);
            }else{
                nectarShooter.setShooterState(ShooterStates.IDLE);
            }

            nectarShooter.update(robot);
            pollenShooter.update(robot);
        }
        }

    }
