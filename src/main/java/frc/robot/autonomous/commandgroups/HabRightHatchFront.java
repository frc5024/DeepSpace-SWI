package frc.robot.autonomous.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.autonomous.actions.FollowPath;
import frc.robot.autonomous.actions.SetBrakes;
import frc.robot.autonomous.actions.SetLedring;

public class HabRightHatchFront extends CommandGroup {
    
    public HabRightHatchFront() {
        // Enable brakes
        addSequential(new SetBrakes(true));

        // Enable LEDring
        addSequential(new SetLedring(true));

        // Follow path
        addSequential(new FollowPath("HABR-HATCHF"), 15.0);

        // Outtake
        addSequential(new Outtake());

        // Disable LEDring
        addSequential(new SetLedring(false));
    }
}