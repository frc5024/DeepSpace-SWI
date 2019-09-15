package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib5k.loops.loopables.LoopableSubsystem;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;
import frc.robot.Constants;

/**
 * The intake includes the slider, piston, finger, and all related sensors
 */
public class Intake extends LoopableSubsystem {
    RobotLogger logger = RobotLogger.getInstance();
    public static Intake m_instance = null;

    // Required components
    DoubleSolenoid m_fingerSolenoid;
    Solenoid m_pistonSolenoid;
    WPI_TalonSRX m_slider;

    // Required sensors
    DigitalInput m_leftHall;
    DigitalInput m_centreHall;
    DigitalInput m_rightHall;

    // Buffer readings
    boolean isLeftHall, isRightHall = false;
    boolean isLeft, isCentre, isRight = false;
    boolean isPistonExtended, isFingerLowered = false;
    double sliderSpeed = 0.0;

    public Intake() {
        logger.log("[Intake] Constructing required components", Level.kRobot);
        m_fingerSolenoid = new DoubleSolenoid(Constants.PCM.can_id, Constants.PCM.finger_forward,
                Constants.PCM.finger_reverse);
        m_pistonSolenoid = new Solenoid(Constants.PCM.can_id, Constants.PCM.piston);
        m_slider = new WPI_TalonSRX(Constants.slider_id);

        logger.log("[Intake] Configuring current limits for slider", Level.kRobot);
        m_slider.configPeakCurrentLimit(Constants.DriveTrain.peakCurrent, 0);
        m_slider.configPeakCurrentDuration(Constants.DriveTrain.currentTimeout, 0);
        m_slider.configContinuousCurrentLimit(Constants.DriveTrain.holdCurrent, 0);

        logger.log("[Intake] Constructing Hall effect sensors for slider", Level.kRobot);
        m_leftHall = new DigitalInput(Constants.DIO.slider_left_limit);
        m_centreHall = new DigitalInput(Constants.DIO.slider_centre_limit);
        m_rightHall = new DigitalInput(Constants.DIO.slider_right_limit);

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

    /**
     * Restricts the slider from moving past it's limits
     * 
     * Note: sensors are wired backwards on both bots
     */
    private double limitSliderMovement(double speed) {
        //TODO: Debug this
        double output = speed;

        // Check left limit
        if (output < 0 && isLeft) {
            output = 0.0;
        }

        // Check right limit
        if (output > 0 && isRight) {
            output = 0.0;
        }

        return output;
    }

    @Override
    public void periodicOutput() {

        // Handle finger
        if (isFingerLowered) {
            m_fingerSolenoid.set(Value.kForward);
        } else {
            m_fingerSolenoid.set(Value.kReverse);
        }

        // Handle piston
        m_pistonSolenoid.set(isPistonExtended);

        // Handle slider
        m_slider.set(limitSliderMovement(sliderSpeed));
        
    }

    public void periodicInput() {
        // Read from sensors and determine slider location. note: sensor is backwards
        if (!m_centreHall.get()) {
            if (sliderSpeed > 0) {
                isRight = true;
                isCentre = false;
                isLeft = false;
            } else if (sliderSpeed < 0) {
                isRight = false;
                isCentre = false;
                isLeft = true;
            } else {
                isRight = false;
                isCentre = true;
                isLeft = false;
            }
        }

        // Make sure direction is still updated even if slider starts from unknown
        // location
        if (!m_leftHall.get()) {
            isRight = false;
            isCentre = false;
            isLeft = true;
        } else if (!m_rightHall.get()) {
            isRight = true;
            isCentre = false;
            isLeft = false;
        }

        // Set bounding data
        isLeftHall = !m_leftHall.get();
        isRightHall = !m_rightHall.get();
    }

    /**
     * @param isFingerLowered Should the finger be lowered
     */
    public void setFingerLowered(boolean isFingerLowered) {
        this.isFingerLowered = isFingerLowered;
    }

    /**
     * @param isPistonExtended Should the finger be extended
     */
    public void setPistonExtended(boolean isPistonExtended) {
        this.isPistonExtended = isPistonExtended;
    }

    /**
     * @param sliderSpeed Wanted speed for the slider
     */
    public void setSliderSpeed(double sliderSpeed) {
        this.sliderSpeed = sliderSpeed;
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putBoolean("[Intake] Is slider left", isLeft);
        SmartDashboard.putBoolean("[Intake] Is slider center", isCentre);
        SmartDashboard.putBoolean("[Intake] Is slider right", isRight);

        SmartDashboard.putBoolean("[Intake] Is piston extended", isPistonExtended);
        SmartDashboard.putBoolean("[Intake] Is finger lowered", isFingerLowered);

        SmartDashboard.putNumber("[Intake] Slider speed", sliderSpeed);
    }

    @Override
    public void stop() {
        m_slider.set(0.0);

        sliderSpeed = 0.0;
        isFingerLowered = false;
        isPistonExtended = false;
    }

    @Override
    public void reset() {
        stop();
        isLeft = false;
        isRight = false;
    }

}