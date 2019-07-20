package frc.robot.autonomous.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;

/**
 * This will only run if the Chooser failed to find anything else to do
 */
public class FailedAutonomous extends CommandGroup {
    RobotLogger logger = RobotLogger.getInstance();
    
    public FailedAutonomous() {

    }
    
    @Override
    protected void initialize() {
        // Warn in the log
        logger.log("!!! Chooser failed to find an autonomous or incorrect options where sent !!!", Level.kWarning);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}