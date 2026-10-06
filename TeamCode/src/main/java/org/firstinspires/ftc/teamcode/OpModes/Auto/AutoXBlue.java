package org.firstinspires.ftc.teamcode.OpModes.Auto;

import static com.pedropathing.api.Paths.*;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.Enums.IntakeStates;
import org.firstinspires.ftc.teamcode.Enums.ShooterStates;
import org.firstinspires.ftc.teamcode.Systems.Robot;
import org.firstinspires.ftc.teamcode.Systems.Subsystems.IntakeSystem;
import org.firstinspires.ftc.teamcode.Systems.Subsystems.ShooterSystem;

@Autonomous(name = "AUTOBLUESIDE", group = "Autonomous")
public class AutoXBlue extends LinearOpMode {

    private Robot robot;
    private IntakeSystem intakeSystem;
    private ShooterSystem shooterSystem;
    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose startShooting = poseFactory.of(85, 127, 90);
    private final Pose collectCantoStart = poseFactory.of(85, 127, 270);
    private final Pose collectCanto = poseFactory.of(134, 133, 50);
    private final Pose collectCantoControl1 = poseFactory.of(115.2577, 110.3915, 0);
    private final Pose driveBack = poseFactory.of(113, 27, 78.7941);
    private final Pose alignShooting = poseFactory.of(85, 27, 90);
    private final Pose point4 = poseFactory.of(136, 27, 90);

    // Autonomous routine integrating Intake and Shooter subsystems
    public Command autoRoutine() {
        return sequential(
                // Start intake when following collectCanto path
                instant(() -> intakeSystem.setIntakeState(IntakeStates.CATCHING)),
                follow(follower, collectCanto()),

                // Drive back and stop intake
                parallel(
                        follow(follower, driveBack()),
                        instant(() -> intakeSystem.setIntakeState(IntakeStates.IDLE))
                ),

                // Align for shooting and spin up shooter
                instant(() -> shooterSystem.setShooterState(ShooterStates.SHOOTING)),
                follow(follower, alignShooting()),
                waitMs(1000), // Brief delay for shooting

                // Final movement and turn off shooter
                follow(follower, path4()),
                instant(() -> shooterSystem.setShooterState(ShooterStates.IDLE))
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        robot = new Robot(hardwareMap);
        intakeSystem = new IntakeSystem();
        shooterSystem = new ShooterSystem();
        follower = robot.getFollower();
        follower.setPose(startShooting);
        follower.update();

        waitForStart();
        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            intakeSystem.update(robot, gamepad1);
            shooterSystem.update(robot);
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());
            telemetry.addData("Intake State", IntakeSystem.CS);
            telemetry.addData("Shooter State", ShooterSystem.CS);

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            telemetry.update();
        }
    }

    public Path collectCanto() {
        return curve(collectCantoStart, collectCantoControl1, collectCanto).linear(collectCantoStart, collectCanto);
    }

    public Path driveBack() {
        return line(collectCanto, driveBack).reverseTangent();
    }

    public Path alignShooting() {
        return line(driveBack, alignShooting).constant(alignShooting);
    }

    public Path path4() {
        return line(alignShooting, point4).constant(point4);
    }
}
