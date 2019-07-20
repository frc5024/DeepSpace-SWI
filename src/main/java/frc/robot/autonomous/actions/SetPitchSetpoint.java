package frc.robot.autonomous.actions;

import edu.wpi.first.wpilibj.command.Command;
import frc.lib5k.utils.RobotLogger;
import frc.robot.Robot;

public class SetPitchSetpoint extends Command {
    RobotLogger logger = RobotLogger.getInstance();

    @Override
    protected void initialize() {
        double currentPitch = Robot.m_climber.getGyroPitch();

        logger.log("[SetPitchSetpoint] Setting pitch offset to: " + currentPitch);

        Robot.m_climber.setGyroOffset();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
    
}