package frc.robot.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Reads inputs from smartdashboard to determine correct auto to run
 */
public class Chooser{
    SendableChooser<Integer> m_positionChooser = new SendableChooser<Integer>();
    SendableChooser<Integer> m_targetChooser = new SendableChooser<Integer>();

    /**
     * Create a chooser and register with smartdashboard
     */
    public Chooser(){

        // Load starting positions
        // m_positionChooser.
    }

}