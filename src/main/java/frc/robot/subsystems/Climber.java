package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.lib5k.components.GearBox;
import frc.lib5k.loops.loopables.LoopableSubsystem;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;
import frc.robot.Constants;

public class Climber extends LoopableSubsystem {
    RobotLogger logger = RobotLogger.getInstance();

    /* All required components for climb */

    // Arms
    WPI_TalonSRX m_armTalonFront;
    WPI_TalonSRX m_armTalonRear;
    GearBox m_armGearBox;
    DigitalInput m_armHall;

    // Crawl
    Spark m_leftCrawler;
    Spark m_rightCrawler;
    DifferentialDrive m_crawlDrive;

    // Legs
    WPI_TalonSRX m_legMotor;
    DigitalInput m_legTopLimit;
    DigitalInput m_legMidLimit;
    DigitalInput m_legLowLimit;

    // Gyro
    AHRS m_gyro = Gyroscope.getInstance().getGyro();

    // Optic sensor
    DigitalInput m_opticSensor;

    /* Required data */

    // Pitch
    double m_currentPitch = 0.0;
    double m_pitchOffset = 0.0;

    // Desired speeds
    double m_desiredArmSpeed = 0.0;
    double m_desiredLegSpeed = 0.0;
    double m_desiredCrawlSpeed = 0.0;

    // Climb lock
    boolean m_isLocked = true;

    public Climber() {
        logger.log("[Climber] Constructing objects", Level.kRobot);

        // Set up arms and link them through a GearBox
        logger.log("[Climber] Configuring arms", Level.kRobot);
        m_armTalonFront = new WPI_TalonSRX(Constants.Climb.Arms.frontMotor);
        m_armTalonRear = new WPI_TalonSRX(Constants.Climb.Arms.rearMotor);

        m_armTalonFront.setSafetyEnabled(false);
        m_armTalonRear.setSafetyEnabled(false);

        logger.log("[Climber] Configuring arm GearBox", Level.kRobot);
        m_armGearBox = new GearBox(m_armTalonFront, m_armTalonRear);
        m_armGearBox.limitCurrent(Constants.Climb.Arms.peakCurrent, Constants.Climb.Arms.holdCurrent,
                Constants.Climb.Arms.currentTimeout);

        logger.log("[Climber] Configuring arm Sensor", Level.kRobot);
        m_armHall = new DigitalInput(Constants.DIO.arm_sensor);

        // Set up Crawl motors
        logger.log("[Climber] Configuring crawl motors", Level.kRobot);
        m_leftCrawler = new Spark(Constants.Climb.Crawlers.leftId);
        m_rightCrawler = new Spark(Constants.Climb.Crawlers.rightId);
        
        logger.log("[Climber] Configuring crawl differential", Level.kRobot);
        m_crawlDrive = new DifferentialDrive(m_leftCrawler, m_rightCrawler);
        m_crawlDrive.setSafetyEnabled(false);

        // Set up legs
        logger.log("[Climber] Configuring Legs", Level.kRobot);
        m_legMotor = new WPI_TalonSRX(Constants.Climb.Legs.motorId);

        logger.log("[Climber] Configuring Leg sensors", Level.kRobot);
        m_legTopLimit = new DigitalInput(Constants.DIO.leg_top);
        m_legMidLimit = new DigitalInput(Constants.DIO.leg_mid);
        m_legLowLimit = new DigitalInput(Constants.DIO.leg_low);

        // Optics sensor
        logger.log("[Climber] Configuring Optics sensor", Level.kRobot);
        m_opticSensor = new DigitalInput(Constants.DIO.optics_sensor);
    }

    @Override
    public void periodicOutput() {

    }

    @Override
    public void periodicInput() {

    }
    
    private boolean getArmSensor() {
        return !m_armHall.get();
    }
    
    private boolean getOpticSensor() {
        return m_opticSensor.get();
    }

    /**
     * Unlock the climber to allow new data
     */
    public void unlock() {
        logger.log("[Climber] System unlocked!");
        m_isLocked = false;
    }

    /**
     * Safety lock the climber. No inputs will be passed to the motors while locked.
     * 
     * When lock is called, all desired outputs will be reset to prevent the "HAL
     * lag" effect
     */
    public void lock() {
        logger.log("[Climber] System locked!");
        m_isLocked = true;

        m_desiredArmSpeed = 0.0;
        m_desiredCrawlSpeed = 0.0;
        m_desiredLegSpeed = 0.0;
    }

    /**
     * Used to check if the subsystem should be allowing new data. This is a safety
     * feature
     * 
     * @return Is it safe to control the climber?
     */
    private boolean checkLock() {
        if (m_isLocked) {
            logger.log("[Climber] Climber is locked, but data was requested! Ignoring request.", Level.kWarning);
        }
        return !m_isLocked;
    }

    /**
     * Set the desired arm movement rate. Positive values will move the arms
     * downward.
     * 
     * Keep in mind that this will not obey the arm holders' limits because we do
     * not have sensors on the arms
     * 
     * @param rate Movement rate to send to arms.
     */
    public void setArmMovementRate(double rate) {
        // If the climber is unlocked
        if (checkLock()) {
            // Set the desired speed
            m_desiredArmSpeed = rate;
        }
    }

    public void setCrawlRate(double rate) {

    }

    public void setLegMovementRate(double rate) {

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