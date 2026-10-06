package org.firstinspires.ftc.teamcode.Systems;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedro.Constants;

public class Robot {
    DcMotorEx intake, pollenShooter, nectarShooter;
    Servo pollenHood, nectarHood;

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
}
