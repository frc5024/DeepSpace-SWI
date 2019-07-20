package frc.robot.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.autonomous.commandgroups.FailedAutonomous;

/**
 * Reads inputs from smartdashboard to determine correct auto to run
 */
public class Chooser{
    SendableChooser<Integer> m_positionChooser = new SendableChooser<Integer>();
    SendableChooser<Integer> m_targetChooser = new SendableChooser<Integer>();

    /**
     * Create a chooser and register with smartdashboard
     */
    public Chooser() {

        // Load starting positions
        m_positionChooser.setName("Robot position");
        m_positionChooser.setDefaultOption("HAB 1 Right", 0);
        m_positionChooser.addOption("HAB 1 Left", 1);

        // Load target positions
        m_targetChooser.setName("Autonomous target");
        m_targetChooser.setDefaultOption("Do nothing", 0);
        m_targetChooser.addOption("Front hatch", 10);
        m_targetChooser.addOption("Side hatch", 20);
    }
    
    public CommandGroup getAutonomousCommand() {
        // Calculate autonomous key from choosers
        int key = m_positionChooser.getSelected() + m_targetChooser.getSelected();

        // Detect commandgorup to use
        switch (key) {
        case 10:
            break;
        case 20:
            break;
        case 11:
            break;
        case 21:
            break;
        default:
            break;
        }
        return new FailedAutonomous();
    }

}