package frc.robot.autonomous.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.autonomous.actions.FollowPath;
import frc.robot.autonomous.actions.SetBrakes;
import frc.robot.autonomous.actions.SetLedring;

public class HabRightHatchSide extends CommandGroup {
    
    public HabRightHatchSide() {
        // Enable brakes
        addSequential(new SetBrakes(true));

        // Enable LEDring
        addSequential(new SetLedring(true));

        // Follow path
        addSequential(new FollowPath("HABR-HATCHS"), 15.0);

        // Outtake
        addSequential(new Outtake());

        // Disable LEDring
        addSequential(new SetLedring(false));
    }
}