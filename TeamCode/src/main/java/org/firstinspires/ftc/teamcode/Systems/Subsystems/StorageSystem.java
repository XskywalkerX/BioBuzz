package org.firstinspires.ftc.teamcode.Systems.Subsystems;

import org.firstinspires.ftc.teamcode.Enums.IntakeStates;
import org.firstinspires.ftc.teamcode.Systems.Robot;

public class StorageSystem {

    double count = 0;

    public StorageSystem() {
        resetCount();
    }

    public void update(
            Robot robot,
            IntakeSystem intake) {

        if(robot.getBB().getState()) {
            if(intake.getState() == IntakeStates.DRIVER) {
                if(robot.getIntake().getPower() > 0) {
                    count++;
                } else if(robot.getIntake().getPower() < 0) {
                    count--;
                }
            } else if(intake.getState() == IntakeStates.EJECTING) {
                count--;
            } else {
                count++;
            }
        }

        if(robot.getNBB().getState() || robot.getPBB().getState()) {
            count--;
        }
    }

    public void resetCount() {
        count = 0;
    }

    public boolean overflow() {
        return count > 4;
    }
    public boolean full() {
        return count == 4;
    }
}
