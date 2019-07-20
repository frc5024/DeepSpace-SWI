package frc.robot.autonomous.actions;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class SetClimberLock extends Command {

    boolean m_isLocked;
    
    public SetClimberLock(boolean is_locked) {
        m_isLocked = is_locked;

    }
    
    @Override
    protected void initialize() {

        // Set the lock
        if (m_isLocked) {
            Robot.m_climber.lock();
        } else {
            Robot.m_climber.unlock();
        }
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
    
}