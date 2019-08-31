package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.lib5k.control.Toggle;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;
import frc.robot.Robot;

/**
 * Command for controlling intake commands from drivers.
 */
public class IntakeControl extends Command {
    RobotLogger logger = RobotLogger.getInstance();

    Toggle m_intakeToggle;
    Toggle m_cargoToggle;

    double m_sliderSpeed = 0.0;
    boolean m_shouldIntake = false;
    boolean m_shouldOuttake = false;
    boolean m_shouldDropCargo = false;

    // Latch for intaking. This latch is needed because the OP might want to
    // immediately go from intake to outtake. This will fix data conflicts.
    boolean m_lastIntakeState = false;


    @SuppressWarnings("checkstyle:JavadocMethod")
    public IntakeControl() {
        logger.log("[IntakeControl] Creating Toggle objects", Level.kRobot);
        m_intakeToggle = new Toggle();
        m_cargoToggle = new Toggle();
    }

    @Override
    protected void initialize() {
        m_sliderSpeed = 0.0;
        m_shouldIntake = false;
        m_shouldOuttake = false;
        // Do NOT reset lastIntakeState here

        // Force a CAN update to the finger
        Robot.m_intake.setFingerLowered(false);
    }

    @Override
    protected void execute() {
        /* OI inputs */

        // Read desired slider speed from operator controller
        m_sliderSpeed = Robot.m_oi.getSliderOverride();

        // Feed intake button through a toggle to get desired intake info
        m_shouldIntake = m_intakeToggle.feed(Robot.m_oi.getIntake());

        // Check if we should be outtaking
        m_shouldOuttake = Robot.m_oi.getOuttake();

        // Feed cargo outtake button through a togglePressed
        m_shouldDropCargo = m_cargoToggle.feed(Robot.m_oi.getCargo());

        // Do not allow an intake and an outtake at the same time
        if (m_shouldOuttake) {
            m_shouldIntake = false;
        }

        /* Send outputs to the subsystems */

        // Send slider speed
        Robot.m_intake.setSliderSpeed(m_sliderSpeed);

        // Send intake data to finger only if the intakeState is new. This will ensure
        // that we are not overriding other commands
        if (m_shouldIntake != m_lastIntakeState) {
            Robot.m_intake.setFingerLowered(m_shouldIntake);
        }

        // Set lastIntakeState to current state
        m_lastIntakeState = m_shouldIntake;

        // Set cargo flap state
        Robot.m_cargoflap.setFlapLowered(m_shouldDropCargo);

        // Enable LED ring if intaking
        Robot.m_ledRing.setEnabled(m_shouldIntake);

        // Start the outtake commandgroup if outtaking
        if (m_shouldOuttake) {
            // Only start if not already running
            if (!Robot.m_outtakeGroup.isRunning()) {
                Robot.m_outtakeGroup.start();
            } else {
                // Otherwise, warn to the logfile
                logger.log(
                        "[IntakeControl] Operator tried to outtake while the robot was already outtaking. Ignoring action");
            }

            m_shouldOuttake = false;
        }

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
