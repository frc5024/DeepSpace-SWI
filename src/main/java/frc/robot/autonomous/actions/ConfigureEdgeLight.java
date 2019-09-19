package frc.robot.autonomous.actions;

import edu.wpi.first.wpilibj.command.Command;
import frc.lib5k.utils.RobotLogger;
import frc.robot.Robot;
import frc.robot.subsystems.EdgeLight.EdgeLightConfig;

public class ConfigureEdgeLight extends Command {
    RobotLogger logger = RobotLogger.getInstance();

    EdgeLightConfig m_desiredState;
    boolean m_finished = false;

    public ConfigureEdgeLight(EdgeLightConfig cfg) {

        // Set the desired state
        m_desiredState = cfg;
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
        Robot.m_edgeLight.setDesiredLightingConfig(m_desiredState);

        // We are done
        m_finished = true;
    }

    @Override
    protected boolean isFinished() {
        return m_finished;
    }

    
}