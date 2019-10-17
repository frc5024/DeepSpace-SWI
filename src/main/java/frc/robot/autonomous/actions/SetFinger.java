package frc.robot.autonomous.actions;

import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.lib5k.utils.RobotLogger;
import frc.robot.Robot;

public class SetFinger extends TimedCommand {
    RobotLogger logger = RobotLogger.getInstance();

    boolean m_desiredState;

    public SetFinger(boolean lowered, double timeout) {
        // Set the command timeout time
        super(timeout);

        // Set the desired state
        m_desiredState = lowered;
    }

    @Override
    protected void initialize() {
        // This is only needed for logging
        logger.log("[SetFinger] Requested state: " + m_desiredState);
    }

    @Override
    protected void execute() {
        // Tell the subsystem to move the finger
        Robot.m_intake.setFingerLowered(m_desiredState);
    }

    
}