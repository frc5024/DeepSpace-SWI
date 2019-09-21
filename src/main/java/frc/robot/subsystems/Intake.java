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
    DigitalInput m_hatchDetector;

    // Buffer readings
    boolean isLeftHall, isRightHall = false;
    boolean isLeft, isCentre, isRight = false;
    boolean isPistonExtended, isFingerLowered = false;
    boolean hasHatch = false;
    double sliderSpeed = 0.0;

    int sliderSide = 0;
    boolean sliderCanMove = true;

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

        logger.log("[Intake] Constructing hatch detection sensor", Level.kRobot);
        m_hatchDetector = new DigitalInput(Constants.DIO.hatch_sensor);

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
        double output = speed;

        if (sliderCanMove == false) {
            output = 0;
        }

        return output;
    }

    /**
     * Returns a speed the slider should move at to center itself
     * 
     * 
     */
    private double getDirectionToCentre() {
        if (!isCentre) {
            if (sliderSide == 1) {
                return -1;
            }
            if (sliderSide == -1) {
                return 1;
            }
        }

        return 0;
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
        if (m_leftHall.get()) {
            isLeft = false;
        } else {
            isLeft = true;
        }
        if (m_rightHall.get()) {
            isRight = false;
        } else {
            isRight = true;
        }
        if (m_centreHall.get()) {
            isCentre = false;
        } else {
            isCentre = true;
        }

        // stop slider
        if (sliderSpeed < 0 && isLeft) {
            sliderCanMove = false;
        } else if (sliderSpeed > 0 && isRight) {
            sliderCanMove = false;
        } else {
            sliderCanMove = true;
        }

        // what side the slider is on
        if (m_centreHall.get()) {
            if (sliderSpeed > 0) {
                sliderSide = 1;
            } else if (sliderSpeed < 0) {
                sliderSide = -1;
            }
        }

        // Set bounding data
        isLeftHall = m_leftHall.get();
        isRightHall = m_rightHall.get();

        // Read Hatch verification switch
        hasHatch = m_hatchDetector.get();
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

    /**
     * Check if the robot has a hatch panel
     * @return Is a hatch being held by the intake?
     */
    public boolean isHatchObtained() {
        return hasHatch;
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
        hasHatch = false;
    }

    @Override
    public void reset() {
        stop();
        isLeft = false;
        isRight = false;
    }

}