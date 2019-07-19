package frc.robot.autonomous.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.autonomous.actions.SetFinger;

/**
 * Handles everything required to shoot a hatch and drop a cargo 
 */
public class Outtake extends CommandGroup {
    
    public Outtake() {
        addSequential(new SetFinger(true, 0.4));
        // Piston with pause
        // Piston
        // Finger (no pause)
    }
}