package frc.robot.autonomous.actions;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.robot.Robot;

public class Drive extends TimedCommand {

    double m_driveSpeedStart;
    double m_driveSpeedEnd;
    double m_driveSpeed = 0.0;

    Timer m_timer;

    public Drive(double drive_speed_start, double drive_speed_end, double timeout) {
        super(timeout);

        m_driveSpeedStart = drive_speed_start;
        m_driveSpeedEnd = drive_speed_end;

        m_timer = new Timer();
    }

    @Override
    protected void initialize() {
        m_timer.reset();
        m_timer.start();
    }

    @Override
    protected void execute() {

        m_driveSpeed = (m_driveSpeedStart + Math.min(m_timer.get(), 2.0) * 0.15) * m_driveSpeedEnd;

        Robot.m_driveTrain.arcadeDrive(m_driveSpeed, 0.0);
    }

    @Override
    protected void end() {
        m_timer.stop();

        Robot.m_driveTrain.arcadeDrive(0.0, 0.0);
    }
}