package org.firstinspires.ftc.teamcode.Systems.Subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Enums.HoodStates;
import org.firstinspires.ftc.teamcode.Systems.Robot;
import org.firstinspires.ftc.teamcode.Util.NECTAR_SHOOTER_COEFFICIENTS;
import org.firstinspires.ftc.teamcode.Util.POLLEN_SHOOTER_COEFFICIENTS;

public class HoodSystem {

    public static HoodStates PS, CS = HoodStates.INIT;
    ElapsedTime time = new ElapsedTime();

    public HoodSystem() {
        CS = HoodStates.INIT;
        PS = HoodStates.INIT;
        time.reset();
    }
    public void update(Robot robot) {
        switch (CS) {
            case INIT:
            case IDLE:
                robot.getPollenHood().setPosition(POLLEN_SHOOTER_COEFFICIENTS.HOOD_MIN);
                robot.getNectarHood().setPosition(NECTAR_SHOOTER_COEFFICIENTS.HOOD_MIN);
                break;
            case SHOOTING:

                break;
        }
        PS = CS;
    }
}
