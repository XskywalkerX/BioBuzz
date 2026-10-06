package org.firstinspires.ftc.teamcode.OpModes.Auto;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Enums.IntakeStates;
import org.firstinspires.ftc.teamcode.Enums.ShooterStates;
import org.firstinspires.ftc.teamcode.Systems.Robot;
import org.firstinspires.ftc.teamcode.Systems.Subsystems.IntakeSystem;
import org.firstinspires.ftc.teamcode.Systems.Subsystems.ShooterSystem;

public class Paths extends LinearOpMode {

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
    private final Pose tipLoots = poseFactory.of(85, 121, 90);
    private final Pose confirm = poseFactory.of(64,130,157);
    private final Pose getBack = poseFactory.of(85,27,102);
    private final Pose park = poseFactory.of(133,26,90);

    public Command autoRoutine() {
        return sequential(
                instant(()-> intakeSystem.setIntakeState(IntakeStates.CATCHING)),
                follow(follower, collectCanto()),
                follow(follower, driveBack()),
                follow(follower, alignShooting()),
                instant(()-> shooterSystem.setShooterState(ShooterStates.SHOOTING)),
                waitMs(1000),
                instant(()-> shooterSystem.setShooterState(ShooterStates.IDLE)),
                follow(follower, tipLoot()),
                follow(follower, confirm()),
                follow(follower, backAndShoot()),
                follow(follower, park())
        );
    }


    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();
        while(opModeIsActive()){
            follower.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading",follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }
            follower.update();
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
    public Path tipLoot(){
        return line(alignShooting, tipLoots).constant(tipLoots);
    }
    public Path confirm(){
        return line(tipLoots, confirm).tangent();
    }
    public Path backAndShoot(){
        return line(confirm, getBack).tangent();
    }
    public Path park(){
        return line(getBack, park).constant(park);
    }
}
