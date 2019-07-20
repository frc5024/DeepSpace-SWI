package frc.robot.autonomous.actions;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;
import frc.robot.Robot;

public class MoveArmsWithGradualForce extends Command {
    RobotLogger logger = RobotLogger.getInstance();

    double start,end = 0.0;
    double cap = 0.0;

    double speed = 0.0;

    Timer m_timer;

    public MoveArmsWithGradualForce(double starting_speed, double ending_speed, double cap_time_secs) {
        start = starting_speed;
        end = ending_speed;
        cap = cap_time_secs;

        logger.log("[MoveArmsWithGradualForce] Creating timer", Level.kRobot);
        m_timer = new Timer();

    }

    @Override
    protected void initialize() {
        logger.log("[MoveArmsWithGradualForce] Starting timer");
        m_timer.reset();
        m_timer.start();
    }

    @Override
    protected void execute() {
        // Moves arm from start% to end% throughout cap seconds, then caps at end%
        speed = (start + Math.min(m_timer.get(), cap) * 0.325) * end;

        // Send speed to arms
        Robot.m_climber.setArmMovementRate(speed);

    }

    @Override
    protected void end() {
        m_timer.stop();
    }
    
    @Override
    protected boolean isFinished() {
        // If the arm sensor is tripped, we are finished
        return Robot.m_climber.getArmSensor();
    }
    
}