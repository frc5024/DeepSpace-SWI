package frc.robot.autonomous.actions;

import java.io.IOException;

import edu.wpi.first.wpilibj.command.Command;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.Gyroscope;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.PathfinderFRC;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;

public class FollowPath extends Command {
    RobotLogger logger = RobotLogger.getInstance();

    Trajectory m_leftTrajectory;
    Trajectory m_rightTrajectory;

    EncoderFollower m_leftFollower;
    EncoderFollower m_rightFollower;

    double m_leftSpeed = 0.0;
    double m_rightSpeed = 0.0;

    double m_heading = 0.0;
    double m_desiredHeading = 0.0;
    double m_headingError = 0.0;
    double m_headingCorrection = 0.0;

    // This will be set to true and skip the entire command in the case of an
    // incorrect file
    boolean m_ioFailure = false;

    public FollowPath(String filename) {
        // These are backwards due to an issue with PathWeaver v2019.2.1
        try {
            m_leftTrajectory = PathfinderFRC.getTrajectory(filename + ".right.pf1.csv");
            m_rightTrajectory = PathfinderFRC.getTrajectory(filename + ".left.pf1.csv");
        } catch (IOException e) {
            logger.log("[FollowPath] IOException for path:" + filename, Level.kWarning);
            m_ioFailure = true;
            return;
        }

        // Create the followers
        m_leftFollower = new EncoderFollower(m_leftTrajectory);
        m_rightFollower = new EncoderFollower(m_rightTrajectory);

    }

    @Override
    protected void initialize() {
        // Reset the paths
        m_leftFollower.reset();
        m_rightFollower.reset();

        // Configure left encoder
        m_leftFollower.configureEncoder(Robot.m_driveTrain.getLeftGearboxTicks(), Constants.EncoderInfo.ticks_per_rev,
                Constants.Robot.wheel_diameter);
        m_leftFollower.configurePIDVA(Constants.PathingPIDA.kP, Constants.PathingPIDA.kI, Constants.PathingPIDA.kD, 1 / Constants.Robot.max_velocity,
                Constants.PathingPIDA.kA);

        // Configure right encoder
        m_rightFollower.configureEncoder(Robot.m_driveTrain.getRightGearboxTicks(), Constants.EncoderInfo.ticks_per_rev,
                Constants.Robot.wheel_diameter);
        m_rightFollower.configurePIDVA(Constants.PathingPIDA.kP, Constants.PathingPIDA.kI, Constants.PathingPIDA.kD, 1 / Constants.Robot.max_velocity,
                Constants.PathingPIDA.kA);

        // Reset Gyro
        Gyroscope.getInstance().getGyro().reset();
        
    }

    @Override
    protected void execute() {
        // Calculate base speeds
        m_leftSpeed = m_leftFollower.calculate(Robot.m_driveTrain.getLeftGearboxTicks());
        m_rightSpeed = m_rightFollower.calculate(Robot.m_driveTrain.getRightGearboxTicks());

        // Get current heading
        m_heading = Gyroscope.getInstance().getGyro().getAngle();

        // Calculate desired heading
        m_desiredHeading = Pathfinder.r2d(m_leftFollower.getHeading());

        // Calculate heading error
        m_headingError = Pathfinder.boundHalfDegrees(m_desiredHeading - m_heading);

        // Calculate heading correction
        m_headingCorrection = 0.8 * (-1.0 / 80.0) * m_headingError;
        
        // Output to motors
        Robot.m_driveTrain.rawDrive(m_leftSpeed + m_headingCorrection, m_rightSpeed - m_headingCorrection);

    }

    @Override
    protected void end() {
        Robot.m_driveTrain.rawDrive(0.0, 0.0);
    }
    
    @Override
    protected boolean isFinished() {
        if (m_ioFailure) {
            logger.log("[FollowPath] IO Failure. Finishing early!", Level.kWarning);
            return true;
        }

        return m_leftFollower.isFinished() || m_rightFollower.isFinished();
    }

}