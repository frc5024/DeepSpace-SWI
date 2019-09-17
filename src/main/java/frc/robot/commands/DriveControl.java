package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.lib5k.control.CubicDeadband;
import frc.lib5k.control.Toggle;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;
import frc.robot.Constants;
import frc.robot.Robot;

/**
 * Control the Robot's drivebase with OI feedback.
 */
public class DriveControl extends Command {
	RobotLogger logger = RobotLogger.getInstance();

	// Stored movement data
	double speed, rotation = 0.0;
	Toggle m_dtInvertToggle;
	boolean m_shouldInvertControl = false;

	// Deadband
	CubicDeadband m_speedDeadband;
	CubicDeadband m_rotationDeadband;

	@SuppressWarnings("checkstyle:JavadocMethod")
	public DriveControl() {
		logger.log("[DriveControl] Configuring Deadband", Level.kRobot);
		m_speedDeadband = new CubicDeadband(0.0, Constants.Deadbands.speed_percision);
		m_rotationDeadband = new CubicDeadband(Constants.Deadbands.rotation_deadband,
				Constants.Deadbands.roataion_percision);

		m_dtInvertToggle = new Toggle();
	}

	@Override
	protected void execute() {

		// Read movement controls from driver
		speed = Robot.m_oi.getThrottle();
		rotation = Robot.m_oi.getTurn();

		// Read movement inversion
		m_shouldInvertControl = m_dtInvertToggle.feed(Robot.m_oi.getDriveTrainInvert());

		// Pass data through deadbands
		// speed = m_speedDeadband.feed(speed);
		rotation = m_rotationDeadband.feed(rotation);

		// Send movement speeds to DriveTrain
		Robot.m_driveTrain.raiderDrive(speed, rotation, m_shouldInvertControl);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

}
