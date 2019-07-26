package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib5k.loops.loopables.LoopableSubsystem;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;
import frc.robot.Constants;

public class LEDring extends LoopableSubsystem {
    RobotLogger logger = RobotLogger.getInstance();

    boolean m_shouldEnable = false;
    boolean m_newData = false;

    Solenoid m_ledRing;

    public LEDring() {
        logger.log("[LEDring] Constructing Solenoid", Level.kRobot);
        m_ledRing = new Solenoid(Constants.PCM.can_id, Constants.PCM.ledring);

        // Set the subsystem name for logging
        name = "LEDring";
    }

    @Override
    public void periodicOutput() {
        // Check if there is new data to work with
        if (m_newData) {
            // Set PCM data
            m_ledRing.set(m_shouldEnable);

            // No more data to work with
            m_newData = false;
        }

    }

    public void setEnabled(boolean enabled) {
        // Check if the requested state has changed
        if (m_shouldEnable != enabled) {
            m_newData = true;

            // Log to logfile
            logger.log("[LEDring] Led set to: " + enabled);
        }

        // Set state
        m_shouldEnable = enabled;

    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putBoolean("[LEDring] LED enabled", m_shouldEnable);
    }

    @Override
    public void stop() {
        m_ledRing.set(false);
    }

    @Override
    public void reset() {
        stop();
        m_shouldEnable = false;
        m_newData = false;
    }
    
}