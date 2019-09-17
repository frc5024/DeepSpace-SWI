package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib5k.components.BlinkinDriver;
import frc.lib5k.components.BlinkinDriver.LEDSetting;
import frc.lib5k.loops.loopables.LoopableSubsystem;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;
import frc.robot.Constants;

public class EdgeLight extends LoopableSubsystem {
    RobotLogger logger = RobotLogger.getInstance();

    BlinkinDriver m_controller;

    /**
     * Used to specify the desired frame config
     */
    public enum EdgeLightConfig {
        kDisabled, kComputing, kWaiting, kSuccess, kError, KDoublePulseSuccess
    }

    // Desidred data
    EdgeLightConfig m_desiredConfig = EdgeLightConfig.kDisabled;
    boolean m_isNewData = true; // Set to true to flush system

    // Timed shutoff
    boolean m_isTimedFrame = false;
    double m_shutoffTime = 0.0;

    public EdgeLight() {
        logger.log("[EdgeLight] Connecting to LED driver", Level.kRobot);
        m_controller = new BlinkinDriver(Constants.edgelight_port);

    }

    /**
     * On new data, the desired input will be handled. If this config needs to
     * automatically disable itself, A timer wil be started, and this method will
     * handle the lighting, and shutoff.
     */
    @Override
    public void periodicOutput() {
        // Check if there is a new action to complete
        if (m_isNewData) {
            // On new data, the old timer must be reset (this may be redundant)
            m_isTimedFrame = false;
            m_shutoffTime = 0.0;

            // Handle the correct action for the config
            switch (m_desiredConfig) {
            case kDisabled:
                m_controller.set(LEDSetting.kOff);
                break;

            case kComputing:
                m_controller.set(LEDSetting.kChase);
                break;

            case kWaiting:
                m_controller.set(LEDSetting.kOrange);
                break;

            case kSuccess:
                m_controller.set(LEDSetting.kGreen);
                break;

            case kError:
                m_controller.set(LEDSetting.kStrobeRed);

                // Configure timed frame
                m_isTimedFrame = true;
                m_shutoffTime = Timer.getFPGATimestamp() + Constants.edgelight_time_gap;
                break;

            case KDoublePulseSuccess:
                m_controller.set(LEDSetting.kStrobeBlue);

                // Configure timed frame
                m_isTimedFrame = true;
                m_shutoffTime = Timer.getFPGATimestamp() + Constants.edgelight_time_gap;
                break;

            default:
                m_controller.set(LEDSetting.kOff);
                break;

            }

            // All new data has been handled
            m_isNewData = false;

        } else {
            // Handle shutoff
            // if (m_isTimedFrame && Timer.getFPGATimestamp() >= m_shutoffTime) {
            //     // Set the desired config to kDisabled
            //     setDesiredLightingConfig(EdgeLightConfig.kDisabled);

            //     // Reset timer data
            //     m_isTimedFrame = false;
            //     m_shutoffTime = 0.0;

            // }


            System.out.println("TTTTTT");
        }

    }

    /**
     * Set the desired action for the edgelights
     * 
     * @param cfg
     */
    public void setDesiredLightingConfig(EdgeLightConfig cfg) {
        m_desiredConfig = cfg;
        m_isNewData = true;
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putString("[EdgeLight] Config", m_desiredConfig.toString());
        SmartDashboard.putBoolean("[EdgeLight] Has new data?", m_isNewData);
        SmartDashboard.putNumber("[EdgeLight] Lighting shutoff timestamp", m_shutoffTime);

    }

    @Override
    public void stop() {
        setDesiredLightingConfig(EdgeLightConfig.kDisabled);

    }

    @Override
    public void reset() {
        stop();

    }

}