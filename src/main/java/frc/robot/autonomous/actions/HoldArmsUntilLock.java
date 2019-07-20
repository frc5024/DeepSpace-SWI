package frc.robot.autonomous.actions;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;
import frc.robot.Robot;

/**
 * Hold the arms at a speed until the climber is "locked".
 * 
 * This effectively holds the arms forever during a climb.
 */
public class HoldArmsUntilLock extends Command {
    RobotLogger logger = RobotLogger.getInstance();
    
    double speed = 0.0;

    public HoldArmsUntilLock(double speed) {
        this.speed = speed;
    }

    @Override
    protected void execute() {
        // Send speed to arms
        Robot.m_climber.setArmMovementRate(speed);

    }

    @Override
    protected boolean isFinished() {
        // If the climber gets locked, stop holding the arms
        return Robot.m_climber.isLocked();
    }
}