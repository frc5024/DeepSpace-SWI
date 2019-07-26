package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib5k.loops.loopables.LoopableSubsystem;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;
import frc.robot.Constants;

public class Pneumatics extends LoopableSubsystem {
    static Pneumatics m_instance = null;
    RobotLogger logger = RobotLogger.getInstance();

    boolean m_shouldEnable = false;

    // This is force-set to true to make sure the compressor gets disabled on startup
    boolean m_newDataReceived = true;

    Compressor m_primaryCompressor;

    public Pneumatics() {
        logger.log("[Pneumatics] Creating Compressor object", Level.kRobot);
        m_primaryCompressor = new Compressor(Constants.PCM.can_id);

        // Set the subsystem name for logging
        name = "Pneumatics";
    }

    public static Pneumatics getInstance() {
        if (m_instance == null) {
            m_instance = new Pneumatics();
        }

        return m_instance;
    }

    @Override
    public void periodicOutput() {
        // Check for new data
        if (m_newDataReceived) {
            // Set the compressor state
            m_primaryCompressor.setClosedLoopControl(m_shouldEnable);

            // Record event in the logfile
            logger.log("[Pneumatics] Primary Compressor's closed loop control mode set to: " + m_shouldEnable);

            // We have completed the requested action, set new data to false
            m_newDataReceived = false;
        }
    }

    /**
     * 
     * @param on Should the primary compressor be enabled?
     */
    public void setPrimaryCompressorEnabled(boolean on) {
        // If this is diffrent from the last input, it is new data
        if (m_shouldEnable != on) {
            m_newDataReceived = true;
        }
       
        // Set requested mode
        m_shouldEnable = on;

    }


    @Override
    public void outputTelemetry() {
        SmartDashboard.putBoolean("[Pneumatics] Compressor Enabled", m_shouldEnable);
    }

    @Override
    public void stop() {
        // Force-disable the compressor
        m_primaryCompressor.setClosedLoopControl(false);
    }

    @Override
    public void reset() {
        stop();
        m_shouldEnable = false;
        m_newDataReceived = true;
    }
    
}