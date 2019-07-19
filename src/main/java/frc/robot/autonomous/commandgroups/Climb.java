package frc.robot.autonomous.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * This should be used for going lvl 1 to 3, or 2 to 3. NOT 1 to 2. That should be done with manual control
 * 
 * For manual control, a button must be held, which unlocks the arms, and forces the crawl to move forward at 100%
 */
public class Climb extends CommandGroup {

    public Climb() {
        // Enable brakes
        // Store gyro setpoint
        // lower arms at 80% until current spike 
        // Parallel: Lower arms at 100% until drivetrain current spike
        // Crawl forward for quarter second
        // lower legs until gyro within range of setpoint
        // Parallel: hold legs
        // Parallel: Crawl forward until rangefinder
        // Drive forward for 1.4 seconds
        // Retract legs for timeout 
    }
}