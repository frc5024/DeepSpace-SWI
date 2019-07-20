package frc.robot.autonomous.actions;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class CrawlAndHoldBot extends Command {

    double m_crawlSpeed;
    double m_armSpeed;
    double m_legSpeed;
    double m_driveSpeed;

    boolean m_currentOpticReading;
    boolean m_lastOpticReading;
    boolean m_finished = false;


    public CrawlAndHoldBot(double crawl_speed, double arm_hold, double leg_hold, double passive_drive_speed) {
        m_crawlSpeed = crawl_speed;
        m_armSpeed = arm_hold;
        m_legSpeed = leg_hold;
        m_driveSpeed = passive_drive_speed;

    }

    @Override
    protected void initialize() {
        // Reset readings
        m_finished = false;
        m_lastOpticReading = false;
        m_currentOpticReading = false;
    }

    @Override
    protected void execute() {
        // Get the current optic sensor reading
        m_currentOpticReading = Robot.m_climber.getOpticSensor();

        // Set constant motor speeds
        Robot.m_climber.setArmMovementRate(m_armSpeed);
        Robot.m_climber.setLegMovementRate(m_legSpeed);
        Robot.m_climber.setCrawlRate(m_crawlSpeed);
        Robot.m_driveTrain.arcadeDrive(m_driveSpeed, 0.0);

        // Check if we went from hanging to supported since the last cycle
        if (!m_lastOpticReading && m_currentOpticReading) {
            // If so, we have finished this command
            m_finished = true;
        } else {
            // Set last reading to current reading
            m_lastOpticReading = m_currentOpticReading;
        }

    }

    @Override
    protected void end() {
        Robot.m_climber.setCrawlRate(0.0);
    }

    @Override
    protected boolean isFinished() {
        return m_finished;
    }
    
}