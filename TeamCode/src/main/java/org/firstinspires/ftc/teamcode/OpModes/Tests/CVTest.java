package org.firstinspires.ftc.teamcode.OpModes.Tests;

import org.firstinspires.ftc.teamcode.Systems.Eyes.CVPathGen;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.Path;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import java.util.ArrayList;

@TeleOp(name = "CVTesT")
public class CVTest extends LinearOpMode {
    DcMotorEx intake;
    private CVPathGen pather;
    private Follower follower;
    ArrayList<Pose> allBalls = new ArrayList<>();

    @Override
    public void runOpMode() throws InterruptedException {
        follower = Constants.create(hardwareMap);
        follower.setPose(new Pose(72,72, Math.toRadians(90)));
        follower.update();
        pather = new CVPathGen(hardwareMap, follower, telemetry);
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        waitForStart();
        while (opModeIsActive()){
            follower.manual(-gamepad1.left_stick_y, -gamepad1.left_stick_x,
                    gamepad1.right_stick_y -gamepad1.right_stick_x );
            follower.update();
            allBalls = pather.getBalls(false);
            if(gamepad1.crossWasPressed()){
                Path path = pather.pathGen();
                if(path != null){
                    follower.follow(path);
                    intake.setPower(1);
                }
            }
            if(follower.isBusy() && follower.atParametricEnd()){
                follower.stop();
                intake.setPower(0);
            }
            for (Pose ball: allBalls) {
                telemetry.addLine("(" + ball.x() + ", "+ ball.y() + ")");
            }
            telemetry.addData("Robot X", follower.pose().x());
            telemetry.addData("Robot Y", follower.pose().y());
            telemetry.addData("Remaining", follower.remainingDistance());
            telemetry.addData("Dist to endpoint", follower.distanceToEndpoint());
            telemetry.addData("Completion", follower.completion());
            telemetry.update();
        }
    }
}