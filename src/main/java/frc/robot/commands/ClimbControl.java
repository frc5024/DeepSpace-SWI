package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ClimbControl extends Command {

    @Override
    protected void execute() {
        // Check for 2fa button
        if (Robot.m_oi.getClimb2FA()) {
            // Check if we are currently climbing
            if (Robot.m_climbGroup.isRunning()) {
                // If climbing, cancle
                Robot.m_climbGroup.cancel();
            } else {
                // Start climbing
                Robot.m_climbGroup.start();
            }
        }

        
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
    
}