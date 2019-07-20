package frc.robot.autonomous.actions;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.robot.Robot;

public class DriveAndRetractArms extends TimedCommand {
    
    double m_driveSpeedStart;
    double m_driveSpeedEnd;
    double m_driveSpeed = 0.0;

    double m_armSpeed;
    double m_legSpeed;

    Timer m_timer;


    public DriveAndRetractArms(double drive_speed_start, double drive_speed_end,  double arm_speed, double leg_speed, double timeout) {
        super(timeout);

        m_driveSpeedStart = drive_speed_start;
        m_driveSpeedEnd = drive_speed_end;
        m_armSpeed = arm_speed;
        m_legSpeed = leg_speed;

        m_timer = new Timer();
    }

    @Override
    protected void initialize() {
        m_timer.reset();
        m_timer.start();
    }

    @Override
    protected void execute() {
        // Move motors
        Robot.m_climber.setArmMovementRate(m_armSpeed);
        Robot.m_climber.setLegMovementRate(m_legSpeed);

        m_driveSpeed = (m_driveSpeedStart + Math.min(m_timer.get(), 2.0) * 0.15) * m_driveSpeedEnd;

        Robot.m_driveTrain.arcadeDrive(m_driveSpeed, 0.0);
    }

    @Override
    protected void end() {
        m_timer.stop();

        // Stop arms and drivetrain
        Robot.m_climber.setArmMovementRate(0.0);
        Robot.m_driveTrain.arcadeDrive(0.0, 0.0);
    }
}