package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.lib5k.loops.loopers.SubsystemLooper;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;
import frc.robot.Constants;
import frc.robot.autonomous.Chooser;
import frc.robot.autonomous.commandgroups.Outtake;
import frc.robot.commands.CompressorControl;
import frc.robot.commands.DriveControl;
import frc.robot.commands.IntakeControl;
import frc.robot.subsystems.CargoFlap;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDring;
import frc.robot.subsystems.Pneumatics;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
	RobotLogger logger = RobotLogger.getInstance();

	/* AUTONOMOUS */
	Command m_autonomousCommand;
	Chooser m_chooser = new Chooser();

	/* SUBSYSTEMS */
	SubsystemLooper m_subsystemLooper;
	public static OI m_oi;
	public static DriveTrain m_driveTrain;
	public static Intake m_intake;
	public static CargoFlap m_cargoflap;
	public static Pneumatics m_pneumatics;
	public static LEDring m_ledRing;

	/* COMMANDS */
	DriveControl m_driveControl;
	IntakeControl m_intakeControl;
	CompressorControl m_compressorControl;
	
	/* COMMAND GROUPS */
	public static Outtake m_outtakeGroup;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		logger.log("Welcome 5024!", Level.kRobot);

		/* Create Subsystems */
		logger.log("Constructing Subsystems", Level.kRobot);
		m_oi = new OI();
		m_driveTrain = DriveTrain.getInstance();
		m_intake = Intake.getInstance();
		m_cargoflap = new CargoFlap();
		m_pneumatics = Pneumatics.getInstance();
		m_ledRing = new LEDring();

		logger.log("Registering Subsystems with SubsystemLooper", Level.kRobot);
		m_subsystemLooper.register(m_driveTrain);
		m_subsystemLooper.register(m_intake);
		m_subsystemLooper.register(m_cargoflap);
		m_subsystemLooper.register(m_pneumatics);
		m_subsystemLooper.register(m_ledRing);

		/* Create Commands */
		logger.log("Constructing Connamds", Level.kRobot);
		m_driveControl = new DriveControl();
		m_intakeControl = new IntakeControl();
		m_compressorControl = new CompressorControl();

		/* Create CommandGroups */
		m_outtakeGroup = new Outtake();

		/* Start Threads */
		logger.log("Starting threads", Level.kRobot);
		m_subsystemLooper.start(Constants.PeriodicTiming.robot_period);
		logger.start(Constants.PeriodicTiming.logging_period);

	}

	/**
	 * This function is called every robot packet, no matter the mode. Use this for
	 * items like diagnostics that you want ran during disabled, autonomous,
	 * teleoperated and test.
	 *
	 * <p>
	 * This runs after the mode specific periodic functions, but before LiveWindow
	 * and SmartDashboard integrated updating.
	 */
	@Override
	public void robotPeriodic() {

		// Push telemetry data from subsystems
		m_subsystemLooper.outputTelemetry();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode. You
	 * can use it to reset any subsystem information you want to clear when the
	 * robot is disabled.
	 */
	@Override
	public void disabledInit() {
		logger.log("Robot Disabled");

		// Disable the brakes on the DriveTrain
		m_driveTrain.setBrakes(false);
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString code to get the
	 * auto name from the text box below the Gyro
	 *
	 * <p>
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons to
	 * the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		logger.log("Autonomous Starting");
		m_autonomousCommand = m_chooser.getAutonomousCommand();

		// schedule the autonomous command (example)
		if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
		}

		// Disable the brakes on the DriveTrain
		m_driveTrain.setBrakes(false);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		logger.log("Teleop Starting");
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}

		// Enable the brakes on the DriveTrain
		m_driveTrain.setBrakes(true);
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
