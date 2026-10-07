package org.firstinspires.ftc.teamcode.Systems.Subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Enums.IntakeStates;
import org.firstinspires.ftc.teamcode.Systems.Robot;
import org.firstinspires.ftc.teamcode.Util.Globals;

public class IntakeSystem {

    public IntakeStates PS, CS;

    StorageSystem storage;
    ElapsedTime time = new ElapsedTime();

    public IntakeSystem() {
        CS = IntakeStates.INIT;
        PS = IntakeStates.INIT;
        storage = new StorageSystem();
        time.reset();
    }

    public void update(Robot robot, Gamepad gamepad) {

        switch (CS) {
            case INIT:
            case IDLE:
                if (storage.full()) {
                    robot.getIntake().setPower(0);
                } else if(storage.overflow()) {
                    setIntakeState(IntakeStates.EJECTING);
                }
                break;
            case CATCHING:
                if (!storage.full()) {
                    robot.getIntake().setPower(Globals.INTAKE_DIRECTION);
                } else if (storage.full()) {
                    setIntakeState(IntakeStates.IDLE);
                } else {
                    setIntakeState(IntakeStates.EJECTING);
                }
                break;
            case EJECTING:
                if (storage.overflow()) {
                    robot.getIntake().setPower(-Globals.INTAKE_DIRECTION);
                } else {
                    setIntakeState(IntakeStates.IDLE);
                }
                break;
            case DRIVER:
                if (!storage.full()) {
                    robot.getIntake().setPower(gamepad.right_trigger - gamepad.left_trigger);
                } else {
                    setIntakeState(IntakeStates.IDLE);
                }
                break;
        }
        PS = CS;
    }

    public void setIntakeState(IntakeStates s) {
        CS = s;
    }

    public IntakeStates getState() {
        return CS;
    }
    public IntakeStates getPrevState() {
        return PS;
    }
}