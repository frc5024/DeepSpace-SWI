package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;
import frc.robot.Robot;

/**
 * This command starts other climb commands depending on 2fa inputs.
 */
public class ClimbControl extends Command {
    RobotLogger logger = RobotLogger.getInstance();

    @Override
    protected void execute() {
        // Check for 2fa button
        if (Robot.m_oi.getClimb2FA()) {
            // Check if we are currently climbing
            if (Robot.m_climbGroup.isRunning()) {
                // If climbing, cancel
                Robot.m_climbGroup.cancel();
                logger.log("[ClimbControl] Autonomous climb canceled (or stopped)");
            } else {
                // Start climbing
                Robot.m_climbGroup.start();
                logger.log("[ClimbControl] Autonomous climb started!");
            }
        }

        // Check for manual arm override
        if (Robot.m_oi.getManualArmToggle()) {
            // Check if we are not already running ManualClimbController
            if (!Robot.m_manualArmController.isRunning()) {
                // Check if lock already acquired (this means auto-climb is running)
                if (!Robot.m_climber.isLocked()) {
                    logger.log(
                            "[ClimbControl] Manual arm override requested while climber unlocked. This means that something else was using the climber, or a scheduler fell out of sync. Bad things \"shouldn't\" happen...",
                            Level.kWarning);
                    logger.log(
                            "[ClimbControl] Canceling autonomous climb and stopping the climber to be safe before acquiring lock for ManualClimbController",
                            Level.kWarning);
                    // Cancel auto-climb
                    Robot.m_climbGroup.cancel();

                    // Stop climber
                    Robot.m_climber.reset();
                }

                // Start ManualClimbController
                Robot.m_manualArmController.start();

            } else {
                // ManualClimbController is already running. Stop it.
                logger.log("[ClimbControl] Stopping ManualClimbController");

                Robot.m_manualArmController.cancel();
            }

        }

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
