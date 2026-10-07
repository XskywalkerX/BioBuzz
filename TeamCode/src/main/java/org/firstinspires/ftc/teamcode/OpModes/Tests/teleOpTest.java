package org.firstinspires.ftc.teamcode.OpModes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Enums.Alliance;
import org.firstinspires.ftc.teamcode.Enums.IntakeStates;
import org.firstinspires.ftc.teamcode.Enums.ShooterStates;
import org.firstinspires.ftc.teamcode.Systems.GamepadBoladao2;
import org.firstinspires.ftc.teamcode.Systems.Robot;
import org.firstinspires.ftc.teamcode.Systems.Subsystems.HoodSystem;
import org.firstinspires.ftc.teamcode.Systems.Subsystems.IntakeSystem;
import org.firstinspires.ftc.teamcode.Systems.Subsystems.ShooterSystem;
import org.firstinspires.ftc.teamcode.Util.Globals;

@TeleOp(name = "o bicho vai pegar 2", group = "test")
public class teleOpTest extends LinearOpMode {

    ShooterSystem pollenShooter, nectarShooter;
    HoodSystem pollenHood, nectarHood;
    IntakeSystem intake;

    Robot robot;
    GamepadBoladao2 gamepad;

    ElapsedTime time = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {

        robot = new Robot(hardwareMap);
        gamepad = new GamepadBoladao2(gamepad1);

        Globals.POLLEN_SHOOTER.setShooter(robot.getPollenShooter());
        Globals.NECTAR_SHOOTER.setShooter(robot.getNectarShooter());

        pollenShooter = new ShooterSystem(Globals.POLLEN_SHOOTER);
        nectarShooter = new ShooterSystem(Globals.NECTAR_SHOOTER);
        pollenHood = new HoodSystem(Globals.POLLEN_SHOOTER, robot.getPollenHood());
        nectarHood = new HoodSystem(Globals.NECTAR_SHOOTER, robot.getNectarHood());

        intake = new IntakeSystem();

        while (opModeInInit()) {
            gamepad.readGamepad(gamepad1);
            if(gamepad.ONEwasCrossPressed()) {
                Globals.alliance = Globals.alliance == Alliance.RED ? Alliance.BLUE : Alliance.RED;
            }

            telemetry.addLine("============== INIT ==============\n");
            telemetry.addData("Alliance: ", Globals.alliance);
            telemetry.addData("Loop time: ", "%.2fms", time.milliseconds());
            telemetry.update();

            time.reset();
        }

        waitForStart();

        intake.setIntakeState(IntakeStates.DRIVER);

        while (opModeIsActive()) {
            gamepad.readGamepad(gamepad1);

            intake.update(robot, gamepad1);
            pollenShooter.update(robot);
            nectarShooter.update(robot);

            if (gamepad.getGamepadOne().triangle) {
                pollenShooter.setShooterState(ShooterStates.SHOOTING);
                nectarShooter.setShooterState(ShooterStates.IDLE);
            } else if (gamepad.getGamepadTwo().triangle) {
                nectarShooter.setShooterState(ShooterStates.SHOOTING);
                pollenShooter.setShooterState(ShooterStates.IDLE);
            } else if (gamepad.getGamepadOne().circle) {
                nectarShooter.setShooterState(ShooterStates.SHOOTING);
                pollenShooter.setShooterState(ShooterStates.SHOOTING);
            } else {
                nectarShooter.setShooterState(ShooterStates.IDLE);
                pollenShooter.setShooterState(ShooterStates.IDLE);
            }

            telemetry.addLine("============== INFO ==============\n");
            telemetry.addData("Alliance: ", Globals.alliance);
            telemetry.addData("Loop time: ", "%.2fms", time.milliseconds());
            telemetry.addLine("\n============== DATA ==============\n");
            robot.debug(telemetry);
            time.reset();
        }
    }
}