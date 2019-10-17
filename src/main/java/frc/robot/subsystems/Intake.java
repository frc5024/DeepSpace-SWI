package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib5k.loops.loopables.LoopableSubsystem;
import frc.lib5k.utils.RobotLogger;
import frc.robot.Constants;

public class Intake extends LoopableSubsystem {
    static Intake m_instance = null;
    RobotLogger logger = RobotLogger.getInstance();

    // Creating components

    DoubleSolenoid m_finger;
    Solenoid m_piston;
    WPI_TalonSRX m_slider;  

    // Creating sensors
    DigitalInput m_leftHall;
    DigitalInput m_centreHall;
    DigitalInput m_rightHall;
    
    // Variables
    boolean isFingerLowered = false;
    boolean isPistonExtended = false;
    double sliderSpeed = 0;
    
    boolean left,right,centre = false;
    int side = 0;
    
    public Intake() {
        // Constructing Finger
        m_finger = new DoubleSolenoid(Constants.PCM.can_id,Constants.PCM.finger_forward,Constants.PCM.finger_reverse);
        logger.log("[Intake] Constructing Double Solenoid");

        // Constructing Soleniod
        m_piston = new Solenoid(Constants.PCM.can_id,Constants.PCM.piston);
        logger.log("[Intake] Constructing Solenoid");
              
        // Constructing Talon
        m_slider = new WPI_TalonSRX(Constants.slider_id);
        logger.log("[Intake] Constructing Talon");

        // Construction sensors
        m_leftHall = new DigitalInput(Constants.DIO.slider_left_limit);
        m_centreHall = new DigitalInput(Constants.DIO.slider_centre_limit);
        m_rightHall = new DigitalInput(Constants.DIO.slider_right_limit);
        logger.log("[Intake] Constructing Sensors");

        // Set the subsystem name for logging
        name = "Intake";
    }

    public static Intake getInstance() {
        if (m_instance == null) {
            m_instance = new Intake();
        }

        return m_instance;
    }
                            
    /**
     * @param lowered Should the finger be lowered
     */
    public void setFingerLowered(boolean lowered) {
        isFingerLowered = lowered;
    }

    /**
     * @param extended Should the piston be extended
     */
    public void setPistonExtended(boolean extended) {
        isPistonExtended = extended;
    }

    /**
     * @param speed Sets the speed of the slider
     */
    public void setSliderSpeed(double speed) {
        double s = speed;

        if (left) {
            if (s < 0) {
                s = 0;
            }
        }
        if (right) {
            if (s > 0) {
                s = 0;
            }                            
        }

        sliderSpeed = s;
    } 

    // when called, the slider speed will be set to the value needed to center the slider
    public void centerSlider() {
        double speed = 0;

        if (!centre) {
            if (side == 1) {
                speed = -1;
            }
            if (side == -1) {
                speed = 1;
            }
        } else {
            side = 0;
        }

        sliderSpeed = speed;
    } 

     @Override
    public void periodicOutput() {

        // toggle the finger
        if (isFingerLowered) {
            m_finger.set(DoubleSolenoid.Value.kForward);
        } else {
            m_finger.set(DoubleSolenoid.Value.kReverse);
        } 
        
        // toggle the piston
        if (isPistonExtended) {
            m_piston.set(true);
        } else {
            m_piston.set(false);
        }

        // set slider speed
        m_slider.set(sliderSpeed);
    }

    @Override
    public void periodicInput() {
        left = !m_leftHall.get();
        centre = !m_centreHall.get();
        right = !m_rightHall.get();

        if(this.centre) {
            if (m_slider.get() > 0) {
                side = 1;
            }
            if (m_slider.get() < 0 ) {
                side = -1;
            }
        }
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putBoolean("[Intake] is finger extended", isFingerLowered);
        SmartDashboard.putBoolean("[Intake] is piston extended", isPistonExtended);
        SmartDashboard.putNumber("[Intake] slider speed", sliderSpeed);
        SmartDashboard.putBoolean("[Intake] Right Hall Sensor", right);
        SmartDashboard.putBoolean("[Intake] Centre Hall Sensor", centre);
        SmartDashboard.putBoolean("[Intake] Left Hall Sensor", left);
    }

    @Override
    public void stop() {
        sliderSpeed = 0.0;
        isFingerLowered = false;
        isPistonExtended = false;  
    }

    @Override
    public void reset() {
        this.stop();
    }

    

}