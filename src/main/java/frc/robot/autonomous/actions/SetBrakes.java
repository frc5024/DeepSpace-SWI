package frc.robot.autonomous.actions;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/**
 * Sets the DriveTrain's brakes in autonomous
 */
public class SetBrakes extends Command {

    boolean requestedState = false;

    public SetBrakes(boolean on) {
        requestedState = on;
    }

    @Override
    protected void initialize() {
        Robot.m_driveTrain.setBrakes(requestedState);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
    
}