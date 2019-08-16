package frc.lib5k.control;

import edu.wpi.first.wpilibj.*;

import static org.junit.Assert.assertThat;

import org.junit.*;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SlewLimiterTest {
    SlewLimiter limiter;

    public SlewLimiterTest() {
        // Make a limiter with a rate of 0.02
        limiter = new SlewLimiter(0.02);
    }

    @Test
    public void testPositiveSlewRate() {

        // Test the output
        assert limiter.feed(1.0) == 0.02;

        // Test a secondary output
        assert limiter.feed(1.0) == 0.04;

    }

    @Test
    public void testSlewReset() {
        // Test a reset
        limiter.reset();
        assert limiter.feed(1.0) == 0.02;
    }

    @Test
    public void testNegativeSlewRate() {

        // Test the output
        assert limiter.feed(-1.0) == -0.02;

        // Test a secondary output
        assert limiter.feed(-1.0) == -0.04;

    }

}