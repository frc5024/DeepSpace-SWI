package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import frc.lib5k.control.Toggle;
import frc.lib5k.utils.RobotLogger;
import frc.robot.Robot;

public class IntakeControl extends Command{

    RobotLogger logger = RobotLogger.getInstance();

    Toggle m_intakeToggle, m_cargoToggle;

    boolean m_shouldOuttake = false;
    boolean m_lastIntakeState = false;
    double m_sliderSpeed = 0;

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

        //Force set intake systems
        m_sliderSpeed = 0;

        // Force a CAN message
        Robot.m_intake.setFingerLowered(false);
    }

    @Override
    protected void execute() {
        //Read Inputs
        m_shouldOuttake = Robot.m_oi.getOuttake();
        m_intakeToggle.feed(Robot.m_oi.getIntake());
        m_cargoToggle.feed(Robot.m_oi.getCargo());
        m_sliderSpeed = Robot.m_oi.getSliderOverride();

        //Execute
        if (DriverStation.getInstance().isEnabled()) {
            
            //Auto-cancel Intake if Outtaking
            if(m_shouldOuttake){
                m_intakeToggle.reset();
            }

            //Only Allow Slider Movement During Intake
            if(!m_intakeToggle.get()){
                m_sliderSpeed = 0;
            }

            //Set Subsystem States
            Robot.m_cargoflap.setFlapLowered(m_cargoToggle.get());

        }

        //Reset Stored Inputs
        m_shouldOuttake = false;
        m_sliderSpeed = 0;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}