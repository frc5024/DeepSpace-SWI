package frc.robot.autonomous.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.autonomous.actions.SetFinger;
import frc.robot.autonomous.actions.SetLedring;
import frc.robot.autonomous.actions.SetPiston;

/**
 * Handles everything required to shoot a hatch and drop a cargo 
 */
public class Outtake extends CommandGroup {
    
    public Outtake() {
        addSequential(new SetLedring(true));
        addSequential(new SetFinger(true, 0.5));
        addSequential(new SetPiston(true, 0.5));
        addSequential(new SetLedring(false));
        addSequential(new SetPiston(false, 0.1));
        addSequential(new SetFinger(false, 0.1));
    }
}