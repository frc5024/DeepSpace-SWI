package frc.robot.autonomous.actions;

import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.robot.Robot;

public class MoveLegs extends TimedCommand {

    double m_legSpeed;

    public MoveLegs(double speed, double timeout) {
        super(timeout);

        m_legSpeed = speed;
    }

    @Override
    protected void execute() {
        Robot.m_climber.setLegMovementRate(m_legSpeed);
    }
}