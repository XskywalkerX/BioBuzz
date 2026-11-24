package org.firstinspires.ftc.teamcode.Systems;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Enums.Alliance;
import org.firstinspires.ftc.teamcode.Util.Globals;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.HashMap;
import java.util.Map;

public class Robot {

    DcMotorEx intake, pollenShooter, nectarShooter;
    Servo portaPollen;
    Servo portaNectar;

    public HashMap<DcMotorEx, String> motor = new HashMap<>();
    public HashMap<Servo, String> servo = new HashMap<>();

    //    Limelight3A ll;
    HardwareMap hwMap;

    Follower follower;
    public Robot(HardwareMap hwMap) {
        this.hwMap = hwMap;
        intake = hwMap.get(DcMotorEx.class, "intake");
        pollenShooter = hwMap.get(DcMotorEx.class, "shooterPollen");
        nectarShooter = hwMap.get(DcMotorEx.class, "shooterNectar");
        portaPollen = hwMap.get(Servo.class, "portaPollen");
        portaNectar = hwMap.get(Servo.class, "portaNectar");

        pollenShooter.setDirection(DcMotorSimple.Direction.REVERSE);
//        ll = hwMap.get(Limelight3A.class, "Limelight");
        follower = Constants.create(hwMap);

        pollenShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        nectarShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        motor.put(pollenShooter, "Pollen Shooter");
        motor.put(nectarShooter, "Nectar Shooter");
        motor.put(intake, "Intake");

        servo.put(portaPollen, "Pollen Stopper");
        servo.put(portaNectar, "Nectar Stopper");
    }

//    public void startLL() {
//        ll.deleteSnapshots();
//        ll.pipelineSwitch(7);
//        ll.start();
//    }

    public DcMotorEx getIntake(){return intake;}
    public DcMotorEx getNectarShooter(){return nectarShooter;}
    public Servo getPortaPollen(){return portaPollen;}
    public Servo getPortaNectar(){return portaNectar;}
    public DcMotorEx getPollenShooter(){return pollenShooter;}
//    public Limelight3A getLimelight(){return ll;}
    public Follower getFollower(){return follower;}


    public void debug(Telemetry telemetry) {
        for (Map.Entry<DcMotorEx, String> motor : motor.entrySet()) {
            telemetry.addLine("\n-- " + motor.getValue() + " --");
            telemetry.addData(motor.getValue() + " Vel: ", "%.2f ticks/s",
                    motor.getKey().getVelocity());
            telemetry.addData(motor.getValue() + " Pos: ", "%.2f ticks",
                    motor.getKey().getCurrentPosition());
            telemetry.addData(motor.getValue() + " Current: ", "%.2f AMPS",
                    motor.getKey().getCurrent(CurrentUnit.AMPS));
        }
        for(Map.Entry<Servo, String> servo : servo.entrySet()) {
            telemetry.addData(servo.getValue() + "Pos: ", servo.getKey());
        }
        telemetry.update();
    }

    public double aim(Alliance alliance) {
        double tX = alliance == Alliance.BLUE ? Globals.BLUE_HIVE_X : Globals.RED_HIVE_X;
        double tY = alliance == Alliance.BLUE ? Globals.BLUE_HIVE_Y : Globals.RED_HIVE_Y;
        return Math.atan2(tY - follower.pose().y(), tX - follower.pose().x());
    }
}
