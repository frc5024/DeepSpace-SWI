package frc.robot.autonomous.actions;

import edu.wpi.first.wpilibj.command.Command;
import frc.lib5k.utils.RobotLogger;
import frc.robot.Robot;

public class RaiseBotToSetpoint extends Command {
    RobotLogger logger = RobotLogger.getInstance();

    double m_armSpeed;
    double m_legSpeed;
    double m_pitchThreshold;

    public RaiseBotToSetpoint(double leg_speed, double arm_speed, double pitch_threshold) {
        m_armSpeed = arm_speed;
        m_legSpeed = leg_speed;
        m_pitchThreshold = pitch_threshold;
    }

    @Override
    protected void execute() {
        // Set arm hold speed
        Robot.m_climber.setArmMovementRate(m_armSpeed);

        // Set leg force speed
        Robot.m_climber.setLegMovementRate(m_legSpeed);
    }

    @Override
    protected boolean isFinished() {
        // This command has finished if the gyro setpoint is in range of threshold
        return Robot.m_climber.isGyroInRange(m_pitchThreshold);
    }
    
}