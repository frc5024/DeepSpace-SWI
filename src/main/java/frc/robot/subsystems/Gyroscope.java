package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib5k.loops.loopables.LoopableSubsystem;
import frc.lib5k.utils.RobotLogger;
import frc.lib5k.utils.RobotLogger.Level;

public class Gyroscope extends LoopableSubsystem{
    static Gyroscope m_instance = null;
    RobotLogger logger = RobotLogger.getInstance();

    AHRS m_gyro;

    public Gyroscope() {
        logger.log("[Gyroscope] Attaching to MXP gyro", Level.kRobot);
        m_gyro = new AHRS(Port.kMXP);

        Shuffleboard.getTab("DriverStation").add(m_gyro);

    }

    public static Gyroscope getInstance() {
        if (m_instance == null) {
            m_instance = new Gyroscope();
        }

        return m_instance;
    }

    public AHRS getGyro() {
        return m_gyro;
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putNumber("[Gyroscope] Angle", m_gyro.getAngle());

    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }

}