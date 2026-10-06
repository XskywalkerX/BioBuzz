package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
        c.xPodOffset.set(0.38462575026384493);
        c.yPodOffset.set(3.016136950395239);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.9799073707704872);
                Controller secondaryTranslationalForward = Controller.proportional(0.3620495898925177);
                Controller primaryTranslationalLateral = Controller.proportional(0.33090119607325763);
                Controller secondaryTranslationalLateral = Controller.proportional(0.12225915010626708);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.018499579436157758));
                c.brake.set(Controller.proportionalFeedforward(0.015724642520734095));

                c.headingFeedback.set(Controller.proportional(5.750102887526286));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.06023306311166142, 0.004480967936014432));

                c.linearBrakeCoefficients.set(Matrix.diag(0.07216116958641768, 0.020316791171781482));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0014101020256918554, 0.002308605543721399));

                c.maxAchievableForwardVelocity.set(58.57419043813377);
                c.maxAchievableStrafeVelocity.set(46.72459687113937);
                c.naturalForwardDeceleration.set(43.24261386134592);
                c.naturalStrafeDeceleration.set(73.9498733197342);
            }
    );

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("frontLeft");
        c.frontRightName.set("frontRight");
        c.backLeftName.set("backLeft");
        c.backRightName.set("backRight");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }
}