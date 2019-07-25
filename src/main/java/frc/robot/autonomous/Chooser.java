package frc.robot.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.autonomous.commandgroups.FailedAutonomous;
import frc.robot.autonomous.commandgroups.HabLeftHatchFront;
import frc.robot.autonomous.commandgroups.HabLeftHatchSide;
import frc.robot.autonomous.commandgroups.HabRightHatchFront;
import frc.robot.autonomous.commandgroups.HabRightHatchSide;

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
        m_positionChooser.setDefaultOption("HAB 1 Right Corner", 0);
        m_positionChooser.addOption("HAB 1 Left Corner", 1);

        // Load target positions
        m_targetChooser.setName("Autonomous target");
        m_targetChooser.setDefaultOption("Do nothing", 0);
        m_targetChooser.addOption("Front hatch", 10);
        m_targetChooser.addOption("Side hatch", 20);

        // Push choosers to dashboard
        Shuffleboard.getTab("DriverStation").add(m_positionChooser);
        Shuffleboard.getTab("DriverStation").add(m_targetChooser);
    }
    
    public CommandGroup getAutonomousCommand() {
        // Calculate autonomous key from choosers
        int key = m_positionChooser.getSelected() + m_targetChooser.getSelected();

        // Detect commandgorup to use
        switch (key) {
        case 10:
            return new HabRightHatchFront();
        case 20:
            return new HabRightHatchSide();
        case 11:
            return new HabLeftHatchFront();
        case 21:
            return new HabLeftHatchSide();
        default:
            break;
        }
        return new FailedAutonomous();
    }

}