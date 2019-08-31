package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/**
 * This command reads driver input to enable and disable the robot's compressor.
 */
public class CompressorControl extends Command {

    boolean m_compressorCommand = false;
    boolean m_shouldUpdateCompressor = false;

    @Override
    protected void execute() {
        // Check for a compressor signal
        if (Robot.m_oi.getCompressorEnable()) {
            // If enable signal
            m_compressorCommand = true;
            m_shouldUpdateCompressor = true;
        } else if (Robot.m_oi.getCompressorDisable()) {
            // If disable signal:
            m_compressorCommand = false;
            m_shouldUpdateCompressor = true;
        }

        // Send command if update required
        if (m_shouldUpdateCompressor) {
            Robot.m_pneumatics.setPrimaryCompressorEnabled(m_compressorCommand);

            // Request complete, no new data
            m_shouldUpdateCompressor = false;
        }

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
