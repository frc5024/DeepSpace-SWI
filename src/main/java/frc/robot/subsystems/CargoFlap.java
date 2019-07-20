package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib5k.loops.loopables.LoopableSubsystem;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;
import frc.robot.Constants;

public class CargoFlap extends LoopableSubsystem {
    RobotLogger logger = RobotLogger.getInstance();

    DoubleSolenoid m_flapSolenoid;

    // Buffered data
    boolean isFlapLowered = false;

    public CargoFlap() {
        logger.log("[CargoFlap] Constructing solenoid", Level.kRobot);
        m_flapSolenoid = new DoubleSolenoid(Constants.PCM.can_id, Constants.PCM.flap_forward, Constants.PCM.flap_reverse);
    }

    @Override
    public void periodicOutput() {
        
        // Just send CAN data to specify if flap should be lowered
        if (isFlapLowered) {
            m_flapSolenoid.set(Value.kForward);
        } else {
            m_flapSolenoid.set(Value.kReverse);
        }
    }

    /**
     * @param isFlapLowered Should the flap be lowered
     */
    public void setFlapLowered(boolean isFlapLowered) {
        this.isFlapLowered = isFlapLowered;
    }


    @Override
    public void outputTelemetry() {
        SmartDashboard.putBoolean("[CargoFlap] Is flap lowered", isFlapLowered);
    }

    @Override
    public void stop() {
        isFlapLowered = false;
    }

    @Override
    public void reset() {
        stop();
    }
    
}