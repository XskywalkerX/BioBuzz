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
            if(intake.getState() == IntakeStates.CATCHING) {
                count++;
                if(full()) {
                    intake.setIntakeState(IntakeStates.IDLE);
                } else if(overflow()) {
                    intake.setIntakeState(IntakeStates.EJECTING);
                }
            } else if(intake.getState() == IntakeStates.EJECTING) {
                count--;
                if(!full()) {
                    intake.setIntakeState(IntakeStates.IDLE);
                }
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
