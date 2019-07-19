package frc.robot.autonomous.actions;

import edu.wpi.first.wpilibj.command.Command;
import frc.lib5k.utils.RobotLogger;
import frc.robot.Robot;

public class SetLedring extends Command {
    RobotLogger logger = RobotLogger.getInstance();

    boolean m_desiredState;
    boolean m_finished = false;

    public SetLedring(boolean enabled) {

        // Set the desired state
        m_desiredState = enabled;
    }

    @Override
    protected void initialize() {
        // This is only needed for logging
        logger.log("[SetLedring] Requested state: " + m_desiredState);

        // Reset finished check
        m_finished = false;
    }

    @Override
    protected void execute() {
        // Tell the subsystem to move the finger
        Robot.m_ledRing.setEnabled(m_desiredState);

        // We are done
        m_finished = true;
    }

    @Override
    protected boolean isFinished() {
        return m_finished;
    }

    
}