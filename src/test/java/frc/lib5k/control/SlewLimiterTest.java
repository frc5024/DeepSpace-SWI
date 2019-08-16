package frc.lib5k.control;

import edu.wpi.first.wpilibj.*;

import static org.junit.Assert.assertThat;

import org.junit.*;

public class SlewLimiterTest {

    @Test
    public void testSlewRate() {
        // Make a limiter with a rate of 0.02
        SlewLimiter limiter = new SlewLimiter(0.02);

        // Test the output
        assert limiter.feed(1.0) == 0.02;

        // Test a secondary output
        assert limiter.feed(1.0) == 0.04;

        // Test a reset
        limiter.reset();
        assert limiter.feed(1.0) == 0.02;
        
                
    }
}