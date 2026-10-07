package org.firstinspires.ftc.teamcode.Systems;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Enums.Alliance;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.HashMap;
import java.util.Map;

public class Robot {
    DcMotorEx intake, pollenShooter, nectarShooter;
    Servo pollenHood, nectarHood;

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
        pollenHood = hwMap.get(Servo.class, "hoodPollen");
        nectarHood = hwMap.get(Servo.class, "hoodNectar");

        pollenShooter.setDirection(DcMotorSimple.Direction.REVERSE);
        pollenHood.setDirection(Servo.Direction.REVERSE);
//        ll = hwMap.get(Limelight3A.class, "Limelight");
        follower = Constants.create(hwMap);

        motor.put(pollenShooter, "Pollen Shooter");
        motor.put(nectarShooter, "Nectar Shooter");
        motor.put(intake, "Intake");

        servo.put(pollenHood, "Pollen Hood");
        servo.put(nectarHood, "Nectar Hood");
    }


//    public void startLL() {
//        ll.deleteSnapshots();
//        ll.pipelineSwitch(7);
//        ll.start();
//    }
    public DcMotorEx getIntake(){return intake;}
    public DcMotorEx getNectarShooter(){return nectarShooter;}
    public Servo getPollenHood(){return pollenHood;}
    public Servo getNectarHood(){return nectarHood;}
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
}
