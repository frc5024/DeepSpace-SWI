package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib5k.loops.loopables.LoopableSubsystem;
import frc.robot.autonomous.commandgroups.Climb;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI extends LoopableSubsystem {
	String name = "oi";
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());

	// Constrollers
	public XboxController driverController = new XboxController(0);
	public XboxController operatorController = new XboxController(1);

	// notification
	boolean m_shouldNotifyDriver = false;
	boolean m_shouldNotifyOperator = false;
	double m_driverNotifyTime = 0.0;
	double m_operatorNofityTime = 0.0;

	/**
	 * Notify the driver via haptics <br>
	 * This will tell the looper to handle the vibration
	 */
	public void notifyDriver() {
		m_shouldNotifyDriver = true;
		m_driverNotifyTime = Timer.getFPGATimestamp();
	}

	/**
	 * Notify the operator via haptics <br>
	 * This will tell the looper to handle the vibration
	 */
	public void notifyOperator() {
		m_shouldNotifyOperator = true;
		m_operatorNofityTime = Timer.getFPGATimestamp();
	}

	/**
	 * Get the DriveTrain throttle value from driverstation
	 * 
	 * @return Throttle (from -1.0 to 1.0)
	 */
	public double getThrottle() {
		double speed = 0.0;

		speed += driverController.getTriggerAxis(GenericHID.Hand.kRight);
		speed -= driverController.getTriggerAxis(GenericHID.Hand.kLeft);

		return speed;
	}

	/**
	 * Get the DriveTrain turn rate value from driverstation
	 * 
	 * @return Turn rate (from -1.0 to 1.0)
	 */
	public double getTurn() {
		return driverController.getX(GenericHID.Hand.kLeft);
	}

	/**
	 * Should the bot flip it's orientation (toggle input)
	 * 
	 * @return Output
	 */
	public boolean getDriveTrainInvert() {
		return driverController.getXButtonPressed();
	}

	/**
	 * @return Has the operator requested an intake action?
	 */
	public boolean getIntake() {
		return operatorController.getYButtonPressed();
	}

	/**
	 * @return Has the operator requested an outtake action?
	 */
	public boolean getOuttake() {
		return operatorController.getBButtonPressed();
	}

	/**
	 * @return Has the operator requested an outtake action for cargo?
	 */
	public boolean getCargo() {
		return operatorController.getXButtonPressed();
	}

	/**
	 * 
	 * @return Overriden slider input from operator
	 */
	public double getSliderOverride() {
		return operatorController.getX(GenericHID.Hand.kLeft);
	}

	public boolean getCompressorEnable() {
		return operatorController.getStartButton() || driverController.getStartButton();
	}

	public boolean getCompressorDisable() {
		return operatorController.getBackButton() || driverController.getBackButton();
	}

	public boolean getClimb2FA() {
		return operatorController.getBumperPressed(GenericHID.Hand.kRight) && operatorController.getPOV() == 0;
	}

	public boolean getManualArmToggle() {
		return operatorController.getBumperPressed(GenericHID.Hand.kRight) && operatorController.getPOV() == 180;
	}

	public double getArmMovementOverrideSpeed() {
		return operatorController.getY(GenericHID.Hand.kRight) * -1;
	}

	/* -- BEGIN SUBSYSTEM METHODS -- */

	@Override
	public void periodicOutput() {
		double current_time = Timer.getFPGATimestamp();

		// Handle driver haptics
		if (m_shouldNotifyDriver) {
			driverController.setRumble(RumbleType.kLeftRumble, 0.5);

		} else if (m_shouldNotifyDriver && (current_time - m_driverNotifyTime) < 1) {
			// If vibration has been enabled for over 1 second, disable it
			driverController.setRumble(RumbleType.kLeftRumble, 0.0);

			m_shouldNotifyDriver = false;

		}

		// Handle operator haptics
		if (m_shouldNotifyOperator) {
			operatorController.setRumble(RumbleType.kLeftRumble, 0.5);

		} else if (m_shouldNotifyOperator && (current_time - m_operatorNofityTime) < 1) {
			// If vibration has been enabled for over 1 second, disable it
			operatorController.setRumble(RumbleType.kLeftRumble, 0.0);

			m_shouldNotifyOperator = false;

		}

	}

	@Override
	public void outputTelemetry() {
		SmartDashboard.putBoolean("[OI] Is notifying driver?", m_shouldNotifyDriver);
		SmartDashboard.putBoolean("[OI] Is notifying operator?", m_shouldNotifyOperator);

	}

	@Override
	public void stop() {
		m_shouldNotifyDriver = false;
		m_shouldNotifyOperator = false;

	}

	@Override
	public void reset() {
		stop();

	}

}
