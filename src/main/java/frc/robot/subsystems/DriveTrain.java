package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib5k.components.GearBox;
import frc.lib5k.control.SlewLimiter;
import frc.lib5k.loops.loopables.LoopableSubsystem;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;
import frc.robot.Constants;

/**
 * DriveTrain is the only class to not buffer it's outputs. We are currently
 * waiting on pr #1691 before this is implemented.
 */
public class DriveTrain extends LoopableSubsystem {
    RobotLogger logger = RobotLogger.getInstance();
    public static DriveTrain m_instance = null;

    // Gearboxes
    GearBox m_leftGearBox;
    GearBox m_rightGearBox;

    // WPI wrapper
    private DifferentialDrive m_DifferentialDrive;
    public boolean is_moving, is_turning = false;

    // Slew limiter
    SlewLimiter m_SlewLimiter;

    public DriveTrain() {
        // Create GearBoxes
        logger.log("[DriveTrain] Constructing GearBoxes", Level.kRobot);
        m_leftGearBox = new GearBox(new WPI_TalonSRX(Constants.DriveTrain.leftFrontMotor),
                new WPI_TalonSRX(Constants.DriveTrain.leftRearMotor));
        m_rightGearBox = new GearBox(new WPI_TalonSRX(Constants.DriveTrain.rightFrontMotor),
                new WPI_TalonSRX(Constants.DriveTrain.rightRearMotor));

        /* Enable current limiting on each gearbox */
        logger.log("[DriveTrain] Limiting current on both gearboxes. Peak: " + Constants.DriveTrain.peakCurrent
                + "A, Hold: " + Constants.DriveTrain.holdCurrent + "A, Timeout: " + Constants.DriveTrain.currentTimeout
                + "ms", Level.kRobot);
        m_leftGearBox.limitCurrent(Constants.DriveTrain.peakCurrent, Constants.DriveTrain.holdCurrent,
                Constants.DriveTrain.currentTimeout);
        m_rightGearBox.limitCurrent(Constants.DriveTrain.peakCurrent, Constants.DriveTrain.holdCurrent,
                Constants.DriveTrain.currentTimeout);

        /* Create DifferentialDrive out of gearboxes */
        m_DifferentialDrive = new DifferentialDrive(m_leftGearBox.getMaster(), m_rightGearBox.getMaster());
        m_DifferentialDrive.setSafetyEnabled(false); // Make sure the robot doesn't lock up
        logger.log("[DriveTrain] DifferentialDrive has been set to: Unsafe", Level.kRobot);

        /* Configure SlewLimiter */
        m_SlewLimiter = new SlewLimiter(Constants.accelerationStep);

    }

    /**
     * Get the current DriveTrain instance
     * 
     * @return DriveTrain instance
     */
    public static DriveTrain getInstance() {
        if (m_instance == null) {
            m_instance = new DriveTrain();
        }

        return m_instance;
    }

    /**
     * The standard wrapper around WPIlib's joystick-based drive function
     * 
     * @param speed    Forward speed (from -1.0 to 1.0)
     * @param rotation Rotation of robot (from -1.0 to 1.0)
     */
    public void arcadeDrive(double speed, double rotation) {
        is_moving = (speed != 0.0);
        is_turning = (rotation != 0.0);

        m_DifferentialDrive.arcadeDrive(speed, rotation, false);
    }

    /**
     * Drive the robot with artificial acceleration and gear shifting
     * 
     * @param speed    Forward speed (from -1.0 to 1.0)
     * @param rotation Rotation of robot (from -1.0 to 1.0)
     */
    public void raiderDrive(double speed, double rotation) {
        is_moving = (speed != 0.0);
        is_turning = (rotation != 0.0);

        /* Feed the accelerator */
        speed = m_SlewLimiter.feed(speed);

        /* Send motor data to the mDrivebase */
        m_DifferentialDrive.arcadeDrive(speed, rotation, false);
    }

    /**
     * The standard wrapper around WPIlib's joystick-based drive function with
     * optional input scaling
     * 
     * @param speed             Forward speed (from -1.0 to 1.0)
     * @param rotation          Rotation of robot (from -1.0 to 1.0)
     * @param is_inputs_squared Should WPIlib try to scale the inputs
     */
    public void arcadeDrive(double speed, double rotation, boolean is_inputs_squared) {
        is_moving = (speed != 0.0);
        is_turning = (rotation != 0.0);

        m_DifferentialDrive.arcadeDrive(speed, rotation, is_inputs_squared);
    }

    /**
     * Wrapper around 254's drive interface.
     * 
     * The "turning" stick controls the curvature of the robot's path rather than
     * its rate of heading change. This helps make the robot more controllable at
     * high speeds. Also handles the robot's quick turn functionality - "quick turn"
     * overrides constant-curvature turning for turn-in-place maneuvers.
     * 
     * @param speed         Forward speed (from -1.0 to 1.0)
     * @param rotation      Rotation of robot (from -1.0 to 1.0)
     * @param is_quick_turn Is quick turn functionality enabled?
     */
    public void cheesyDrive(double speed, double rotation, boolean is_quick_turn) {
        is_moving = (speed != 0.0);
        is_turning = (rotation != 0.0);
        m_DifferentialDrive.curvatureDrive(speed, rotation, is_quick_turn);
    }

    /**
     * A passthrough to WPILib's TankDrive method
     * 
     * @param l Left speed
     * @param r Right speed
     */
    public void tankDrive(double l, double r) {
        is_moving = (l + r != 0.0);
        is_turning = (l != r);
        m_DifferentialDrive.tankDrive(l, r);
    }

    /**
     * Enables or disables brake mode on all drivebase talons
     * 
     * @param on Should the brakes be enabled?
     */
    public void setBrakes(boolean on) {
        NeutralMode mode = on ? NeutralMode.Brake : NeutralMode.Coast;
        String mode_string = on ? "Brake" : "Coast";

        logger.log("[DriveTrain] NeutralMode has been set to: " + mode_string);

        m_leftGearBox.front.setNeutralMode(mode);
        m_leftGearBox.rear.setNeutralMode(mode);
        m_rightGearBox.front.setNeutralMode(mode);
        m_rightGearBox.rear.setNeutralMode(mode);
    }

    /**
     * Get the number of ticks recorded by the left GearBox's encoder
     * 
     * @return Number of ticks
     */
    public int getLeftGearboxTicks() {
        return m_leftGearBox.getTicks();
    }

    /**
     * Get the number of ticks recorded by the right GearBox's encoder
     * 
     * @return Number of ticks
     */
    public int getRightGearboxTicks() {
        return m_rightGearBox.getTicks();
    }


    @Override
    public void outputTelemetry() {
        SmartDashboard.putNumber("[DriveTrain] Left gearbox sensor", getLeftGearboxTicks());
        SmartDashboard.putNumber("[DriveTrain] Right gearbox sensor", getRightGearboxTicks());
    }

    @Override
    public void stop() {
        tankDrive(0,0);
    }

    @Override
    public void reset() {
        stop();
        m_SlewLimiter.reset();
    }

}