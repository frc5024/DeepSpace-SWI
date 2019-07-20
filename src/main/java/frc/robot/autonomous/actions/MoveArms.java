package frc.robot.autonomous.actions;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;
import frc.robot.Robot;

public class MoveArms extends TimedCommand {
    RobotLogger logger = RobotLogger.getInstance();
    
    double speed = 0.0;

    public MoveArms(double speed, double timeout) {
        super(timeout);
        this.speed = speed;
    }

    @Override
    protected void execute() {
        // Send speed to arms
        Robot.m_climber.setArmMovementRate(speed);

    }
}