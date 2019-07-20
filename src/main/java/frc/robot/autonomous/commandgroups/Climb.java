package frc.robot.autonomous.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.autonomous.actions.Crawl;
import frc.robot.autonomous.actions.CrawlAndHoldBot;
import frc.robot.autonomous.actions.DriveAndRetractArms;
import frc.robot.autonomous.actions.MoveArmsWithGradualForce;
import frc.robot.autonomous.actions.MoveLegs;
import frc.robot.autonomous.actions.RaiseBotToSetpoint;
import frc.robot.autonomous.actions.SetBrakes;
import frc.robot.autonomous.actions.SetClimberLock;
import frc.robot.autonomous.actions.SetPitchSetpoint;

/**
 * This should be used for going lvl 1 to 3, or 2 to 3. NOT 1 to 2. That should be done with manual control
 * 
 * For manual control, a button must be held, which unlocks the arms, and forces the crawl to move forward at 100%
 */
public class Climb extends CommandGroup {

    public Climb() {
        // Enable the brakes
        addSequential(new SetBrakes(true));

        // Unlock climber
        addSequential(new SetClimberLock(false));

        // Store gyro setpoint
        addSequential(new SetPitchSetpoint());

        // lower arms with gradual force
        addSequential(new MoveArmsWithGradualForce(0.35, 1.0, 2), 5.0);

        // Parallel: crawl forwards for 2 seconds
        addParallel(new Crawl(1.0, 2.0));

        // lower legs and hold arms until gyro within range of setpoint
        addSequential(new RaiseBotToSetpoint(1.0, 0.75, 5), 6.0);

        // Crawl forward while holding the bot in the air until optics trips. Timeout after 6 seconds
        addSequential(new CrawlAndHoldBot(1.0, 1.0, 1.0, 0.6), 6.0);

        // Drive forward and retract arms for 2 seconds
        addSequential(new DriveAndRetractArms(0.6, 0.3, -0.2, 1.0, 2.0));

        // Retract legs for for 3 seconds
        addSequential(new MoveLegs(-1.0, 3.0));

        // Lock climber
        addSequential(new SetClimberLock(true));
    }

    @Override
    protected void interrupted() {
        // Release all outputs
        Robot.m_climber.unlock();
        Robot.m_climber.reset();
        Robot.m_climber.lock();
    }

    @Override
    protected void end() {
        // Release all outputs
        Robot.m_climber.unlock();
        Robot.m_climber.reset();
        Robot.m_climber.lock();
    }
}