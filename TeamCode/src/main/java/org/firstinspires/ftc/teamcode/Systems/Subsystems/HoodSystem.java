package org.firstinspires.ftc.teamcode.Systems.Subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Enums.HoodStates;
import org.firstinspires.ftc.teamcode.Enums.IntakeStates;
import org.firstinspires.ftc.teamcode.Systems.Robot;
import org.firstinspires.ftc.teamcode.Util.COEFFICIENTS;
import org.firstinspires.ftc.teamcode.Util.Globals;

public class HoodSystem {

    public static HoodStates PS, CS = HoodStates.INIT;
    ElapsedTime time = new ElapsedTime();

    public HoodSystem() {
        CS = HoodStates.INIT;
        PS = HoodStates.INIT;
        time.reset();
    }
    public void update(Robot robot, Gamepad gamepad) {
        switch (CS) {
            case INIT:
            case IDLE:
                robot.getPollenHood().setPosition(COEFFICIENTS.PollenShooter.HOOD_MIN);
                robot.getNectarHood().setPosition(COEFFICIENTS.NectarShooter.HOOD_MIN);
                break;
            case SHOOTING:

                break;
        }
        PS = CS;
    }
}
