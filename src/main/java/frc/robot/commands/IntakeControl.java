package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.lib5k.control.Toggle;
import frc.lib5k.utils.RobotLogger;

public class IntakeControl extends Command{

    RobotLogger logger = RobotLogger.getInstance();

    Toggle m_intakeToggle, m_cargoToggle;

    IntakeControl(){
        logger.log("[IntakeControl] Constructing Command");
        m_intakeToggle = new Toggle();
        m_cargoToggle = new Toggle();
    }

    @Override
    protected void initialize() {
        logger.log("[IntakeControl] Resetting Toggles");
        
        //Reset Toggles
        m_intakeToggle.reset();
        m_cargoToggle.reset();
    }

    @Override
    protected void execute() {
        
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}