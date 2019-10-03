package frc.robot.subsystems;

import frc.lib5k.loops.loopables.LoopableSubsystem;
import frc.lib5k.utils.RobotLogger;

/**
 * The intake includes the slider, piston, finger, and all related sensors
 */
public class Intake extends LoopableSubsystem {
    RobotLogger logger = RobotLogger.getInstance();
    public static Intake m_instance = null;

    public Intake() {

        // Set the subsystem name for logging
        name = "Intake";
    }

    /**
     * Get the current Intake instance
     * 
     * @return Intake instance
     */
    public static Intake getInstance() {
        if (m_instance == null) {
            m_instance = new Intake();
        }

        return m_instance;
    }

    public void setFingerLowered(boolean lowered){
    }

    public void setPistonExtended(boolean extended){

    }

    public void setSliderSpeed(double speed){
        
    }

    @Override
    public void periodicOutput() {

    }

    public void periodicInput() {
    }

    @Override
    public void outputTelemetry() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void reset() {
    }

}