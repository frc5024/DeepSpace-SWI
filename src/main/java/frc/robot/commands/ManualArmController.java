package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.lib5k.utils.RobotLogger;
import frc.robot.Robot;

public class ManualArmController extends Command {
    RobotLogger logger = RobotLogger.getInstance();
    
    double m_armSpeed = 0.0;

    @Override
    protected void initialize() {
        logger.log("[ManualArmController] Unlocking Climber");
        Robot.m_climber.unlock();
        
    }
    
    @Override
    protected void execute() {
        // Read arm speed from input
        m_armSpeed = Robot.m_oi.getArmMovementOverrideSpeed();

        // Pass arm speed to Climber
        Robot.m_climber.setArmMovementRate(m_armSpeed);

        // Set a constant crawl speed for Climber
        Robot.m_climber.setCrawlRate(1.0);
        
    }

    @Override
    protected void end() {
        logger.log("[ManualArmController] Finished. Locking Climber and resetting");
        Robot.m_climber.lock();
        Robot.m_climber.reset();
    }

    @Override
    protected void interrupted() {
        logger.log("[ManualArmController] Canceled. Locking Climber and resetting");
        Robot.m_climber.lock();
        Robot.m_climber.reset();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }


}