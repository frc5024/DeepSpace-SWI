import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import frc.lib5k.loops.loopables.LoopableSubsystem;
import frc.lib5k.utils.RobotLogger;
import frc.robot.Constants;

public class Intake extends LoopableSubsystem {
    RobotLogger logger = RobotLogger.getInstance();

    // Creating components

    DoubleSolenoid m_finger;
    Solenoid m_piston;
    WPI_TalonSRX m_slider;  

    // Variables
    boolean isFingerLowered = false;
    boolean isPistionExtended = false;
    double sliderSpeed = 0;
    
    public Intake() {
        // Constructing Finger
        m_finger = new DoubleSolenoid(Constants.PCM.finger_forward,Constants.PCM.finger_reverse);
        logger.log("[Intake] Constructing Double Solenoid");

        // Constructing Soleniod
        m_piston = new Solenoid(Constants.PCM.piston);
        logger.log("[Intake] Constructing Solenoid");
              
        // Constructing Talon
        m_slider = new WPI_TalonSRX(Constants.slider_id);
        logger.log("[Intake] Constructing Talon");
    }

    /**
     * @param lowered Should the finger be lowered
     */
    public void setFingerLowered(boolean lowered) {
        this.isFingerLowered = lowered;
    }

    /**
     * @param extended Should the piston be extended
     */
    public void setPistonExtended(boolean extended) {

        this.isPistionExtended = extended;

    }

    /**
     * @param speed Sets the speed of the slider
     */
    public void setSliderSpeed(double speed) {

        this.sliderSpeed = speed;

    } 


     @Override
    public void periodicOutput() {

    }

    @Override
    public void periodicInput() {}

    @Override
    public void outputTelemetry() {}

    @Override
    public void stop() {}

    @Override
    public void reset() {}

    

}