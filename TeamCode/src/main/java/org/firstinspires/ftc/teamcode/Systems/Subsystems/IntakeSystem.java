package org.firstinspires.ftc.teamcode.Systems.Subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Enums.IntakeStates;
import org.firstinspires.ftc.teamcode.Systems.Robot;
import org.firstinspires.ftc.teamcode.Util.Globals;

public class IntakeSystem {
    public IntakeStates PS, CS;
    ElapsedTime time = new ElapsedTime();
    public IntakeSystem() {
        CS = IntakeStates.INIT;
        PS = IntakeStates.INIT;
        time.reset();
    }

    public void update(Robot robot, Gamepad gamepad) {
        switch (CS) {
            case INIT:
            case IDLE:
                robot.getIntake().setPower(0);
                break;
            case CATCHING:
                robot.getIntake().setPower(Globals.INTAKE_DIRECTION);
                break;
            case EJECTING:
                robot.getIntake().setPower(-Globals.INTAKE_DIRECTION);
                break;
            case DRIVER:
                robot.getIntake().setPower(gamepad.right_trigger - gamepad.left_trigger);
                break;
        }
        PS = CS;
    }

    public void setIntakeState(IntakeStates s) {
        CS = s;
    }
}