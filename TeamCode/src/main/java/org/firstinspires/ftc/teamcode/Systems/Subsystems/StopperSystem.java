package org.firstinspires.ftc.teamcode.Systems.Subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Enums.ShooterStates;
import org.firstinspires.ftc.teamcode.Enums.StopperStates;
import org.firstinspires.ftc.teamcode.Util.Globals;
import org.firstinspires.ftc.teamcode.Util.ShooterConfig;

public class StopperSystem {
    public StopperStates PS, CS;
    ElapsedTime time = new ElapsedTime();

    ShooterConfig config;

    Servo door;

    public StopperSystem(ShooterConfig config, Servo door) {
        CS = StopperStates.INIT;
        PS = StopperStates.INIT;
        this.config = config;
        time.reset();
        this.door = door;
    }

    public void update(ShooterSystem shooterSystem, boolean nectar) {

        double closed = nectar ? Globals.PORTANECTARFECHADO : Globals.PORTAPOLLENFECHADO;
        double open = nectar ? Globals.PORTANECTARABERTO : Globals.PORTAPOLLENABERTO;

        switch (CS) {
            case INIT:
            case CLOSED:
                if (shooterSystem.getState() != ShooterStates.IDLE) {
                    door.setPosition(closed);
                } else {
                    setState(StopperStates.OPEN);
                }
                break;
            case OPEN:
                if (shooterSystem.getState() == ShooterStates.SHOOTING) {
                    door.setPosition(open);
                } else {
                    setState(StopperStates.CLOSED);
                }
                break;
        }
        PS = CS;
    }
    public void setState(StopperStates state) {
        CS = state;
    }

    public StopperStates getState() {
        return CS;
    }

    public StopperStates getPrevState() {
        return PS;
    }
}