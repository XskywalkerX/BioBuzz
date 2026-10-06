package org.firstinspires.ftc.teamcode.OpModes.Auto;

import static com.pedropathing.api.Paths.*;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name = "AutoREDSIDE", group = "Autonomous")
public class AutoXRed extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees().mirrorX(70.75);

    private final Pose startShooting = poseFactory.of(85, 127, 90);
    private final Pose collectCantoStart = poseFactory.of(85, 127, 270);
    private final Pose collectCanto = poseFactory.of(134, 133, 50);
    private final Pose collectCantoControl1 = poseFactory.of(115.2577, 110.3915, 0);
    private final Pose driveBack = poseFactory.of(113, 27, 78.7941);
    private final Pose alignShooting = poseFactory.of(85, 27, 90);
    private final Pose searchingForTipLoots = poseFactory.of(85, 121, 90);
    private final Pose confirmation = poseFactory.of(64, 130, 156.8014);
    private final Pose goingBackAndShooting = poseFactory.of(85, 27, 101.5237);
    private final Pose parking = poseFactory.of(133, 26, 90);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                follow(follower, collectCanto()),
                follow(follower, driveBack()),
                follow(follower, alignShooting()),
                follow(follower, searchingForTipLoots()),
                follow(follower, confirmation()),
                follow(follower, goingBackAndShooting()),
                follow(follower, parking())
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startShooting);
        follower.update();

        waitForStart();
        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

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

    public Path searchingForTipLoots() {
        return line(alignShooting, searchingForTipLoots).constant(searchingForTipLoots);
    }

    public Path confirmation() {
        return line(searchingForTipLoots, confirmation).tangent();
    }

    public Path goingBackAndShooting() {
        return line(confirmation, goingBackAndShooting).reverseTangent();
    }

    public Path parking() {
        return line(goingBackAndShooting, parking).constant(parking);
    }
}