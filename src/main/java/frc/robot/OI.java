package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import frc.robot.autonomous.commandgroups.Climb;
import edu.wpi.first.wpilibj.GenericHID;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
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

	/**
	 * Limits a trigger to positive values only. This allows the use of a steam
	 * controller for driving
	 */
	private double limitTrigger(double value) {
		if (value <= 0.0) {
			return 0.0;
		}
		return value;
	}

	/**
	 * Get the DriveTrain throttle value from driverstation
	 * 
	 * @return Throttle (from -1.0 to 1.0)
	 */
	public double getThrottle() {
		double speed = 0.0;

		speed += limitTrigger(driverController.getTriggerAxis(GenericHID.Hand.kRight));
		speed -= limitTrigger(driverController.getTriggerAxis(GenericHID.Hand.kLeft));

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

	public boolean getManualArmToggle(){
		return operatorController.getBumperPressed(GenericHID.Hand.kRight) && operatorController.getPOV() == 180;
	}

	public double getArmMovementOverrideSpeed() {
		return operatorController.getY(GenericHID.Hand.kRight) * -1;
	}


}
