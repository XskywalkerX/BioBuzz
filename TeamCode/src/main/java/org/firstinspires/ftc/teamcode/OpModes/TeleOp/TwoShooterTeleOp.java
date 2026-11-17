package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Enums.Alliance;
import org.firstinspires.ftc.teamcode.Enums.IntakeStates;
import org.firstinspires.ftc.teamcode.Enums.ShooterStates;
import org.firstinspires.ftc.teamcode.Enums.StopperStates;
import org.firstinspires.ftc.teamcode.Systems.Robot;
import org.firstinspires.ftc.teamcode.Systems.Subsystems.HoodSystem;
import org.firstinspires.ftc.teamcode.Systems.Subsystems.IntakeSystem;
import org.firstinspires.ftc.teamcode.Systems.Subsystems.ShooterSystem;
import org.firstinspires.ftc.teamcode.Systems.Subsystems.StopperSystem;
import org.firstinspires.ftc.teamcode.Util.CSVData;
import org.firstinspires.ftc.teamcode.Util.Globals;

@Configurable
@TeleOp
public class TwoShooterTeleOp extends LinearOpMode {

    public static double kP = -0.05; // 0.05 pollen

    double lastIntakeCurrent = 0, lastIntakeSpeed = 0;

    Robot robot;

    ShooterSystem pollenShooter, nectarShooter;
    IntakeSystem intakeSystem;

    HoodSystem hoodPollen, hoodNectar;
    StopperSystem portaPollen, portaNectar;
    ElapsedTime timer = new ElapsedTime();

    CSVData data;

    @Override
    public void runOpMode() throws InterruptedException {

        data = new CSVData("/sdcard/FIRST/regression.csv", telemetry);

        robot = new Robot(hardwareMap);
        intakeSystem = new IntakeSystem();
        Globals.NECTAR_SHOOTER.setShooter(robot.getNectarShooter());
        Globals.POLLEN_SHOOTER.setShooter(robot.getPollenShooter());
        pollenShooter = new ShooterSystem(Globals.POLLEN_SHOOTER);
        nectarShooter = new ShooterSystem(Globals.NECTAR_SHOOTER);

        hoodPollen = new HoodSystem(Globals.POLLEN_SHOOTER, robot.getPollenHood());
        hoodNectar = new HoodSystem(Globals.NECTAR_SHOOTER, robot.getNectarHood());

        telemetry = PanelsTelemetry.INSTANCE.getFtcTelemetry();

        robot.getFollower().setPose(Globals.POSE_ROBOT);

        waitForStart();

        while (opModeIsActive()) {

            timer.reset();

            robot.getFollower().update();

            robot.getPollenHood().setPosition(Globals.POLLEN_HOOD_MIN);
            robot.getNectarHood().setPosition(Globals.NECTAR_HOOD_MIN);

            portaPollen = new StopperSystem(Globals.POLLEN_SHOOTER, robot.getPortaPollen());
            portaNectar = new StopperSystem(Globals.NECTAR_SHOOTER, robot.getPortaNectar());


            if (gamepad1.cross) {
                robot.getFollower().manual(
                        -gamepad1.left_stick_y,
                        gamepad1.left_stick_x,
                        (0 - robot.aim(Alliance.BLUE)) * kP);
            } else {
                robot.getFollower().manual(
                        -gamepad1.left_stick_y,
                        gamepad1.left_stick_x,
                        (gamepad1.right_stick_x - gamepad1.right_stick_y));
            }

            double current = robot.getIntake().getCurrent(CurrentUnit.AMPS);
            double speed = robot.getIntake().getVelocity();

            double dSpeed = (speed - lastIntakeSpeed) / timer.milliseconds();
            double dCurrent = (current - lastIntakeCurrent) / timer.milliseconds();

            double distance = Globals.NECTAR_SHOOTER.calculateHiveDistance(
                    robot.getFollower().pose()
            );

            telemetry.addData("TARGET SHOOTER VEL", Globals.TARGET_VEL);
            telemetry.addData("HIVE DISTANCE", distance);
            telemetry.addData("dVEL", dSpeed);
            telemetry.addData("dCurrent", dCurrent);
            telemetry.addData("nectar Shooter Vel", robot.getNectarShooter().getVelocity());
            telemetry.addData("pollen Shooter Vel", robot.getPollenShooter().getVelocity());
            robot.debug(telemetry);

            if(gamepad2.circleWasPressed()){
                data.addData("Distance", distance);
                data.addData("Nectar Velocity", robot.getNectarShooter().getVelocity());
                data.addData("Pollen Velocity", robot.getPollenShooter().getVelocity());
                data.addData("Nectar Hood Position", robot.getNectarHood().getPosition());
                data.addData("Pollen Hood Position", robot.getPollenHood().getPosition());
                data.nextRow();
            }

            if (gamepad1.left_bumper) {
                pollenShooter.setShooterState(ShooterStates.SHOOTING);
                portaPollen.setState(StopperStates.OPEN);
            } else {
                pollenShooter.setShooterState(ShooterStates.IDLE);
                portaPollen.setState(StopperStates.CLOSED);
            }

            if (gamepad1.right_bumper) {
                nectarShooter.setShooterState(ShooterStates.SHOOTING);
                portaNectar.setState(StopperStates.OPEN);
            } else {
                nectarShooter.setShooterState(ShooterStates.IDLE);
                portaNectar.setState(StopperStates.CLOSED);
            }

            intakeSystem.setIntakeState(IntakeStates.DRIVER);

            intakeSystem.update(robot, gamepad1);
            nectarShooter.update(robot);
            pollenShooter.update(robot);
            hoodPollen.update(robot, pollenShooter);
            hoodNectar.update(robot, nectarShooter);
            portaPollen.update(pollenShooter, false);
            portaNectar.update(nectarShooter, true);

            lastIntakeCurrent = current;
            lastIntakeSpeed = speed;
        }
    }
}