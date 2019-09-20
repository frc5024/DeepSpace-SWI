package frc.robot.autonomous.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.autonomous.actions.ConfigureEdgeLight;
import frc.robot.autonomous.actions.Drive;
import frc.robot.autonomous.actions.SetFinger;
import frc.robot.autonomous.actions.SetLedring;
import frc.robot.autonomous.actions.SetPiston;
import frc.robot.subsystems.EdgeLight.EdgeLightConfig;

/**
 * Handles everything required to shoot a hatch and drop a cargo 
 * 
 * Procedure: 
 *  - Display robot status with lighting system
 *  - Lower the finger (and wait, so that it does not stick)
 *  - Fire the hatch
 *  - Pull back from the wall to make sure hatch is correctly placed
 *  - Return control to the drivers
 */
public class Outtake extends CommandGroup {
    
    public Outtake() {
        addSequential(new ConfigureEdgeLight(EdgeLightConfig.kWaiting)); 
        addSequential(new SetLedring(true));
        addSequential(new SetFinger(true, 0.5));
        addSequential(new SetPiston(true, 0.15)); 
        addSequential(new ConfigureEdgeLight(EdgeLightConfig.kSuccess));
        addSequential(new SetLedring(false));
        addSequential(new SetPiston(false, 0.1));
        addSequential(new Drive(-0.6, 0.0, 0.3));
        addSequential(new SetFinger(false, 0.1));
        addSequential(new ConfigureEdgeLight(EdgeLightConfig.kDisabled));
    }
}