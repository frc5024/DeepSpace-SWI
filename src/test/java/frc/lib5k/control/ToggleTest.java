package frc.lib5k.control;

import static org.junit.Assert.assertThat;
import org.junit.*;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ToggleTest {
    Toggle toggle;
    
    public ToggleTest() {
        toggle = new Toggle();
    }

    @Test
    public void testToggle() {
        toggle.feed(true);

        assert toggle.get() == true;

        toggle.feed(false);

        assert toggle.get() == true;
    }

    @Test
    public void testToggleReset() {
        toggle.reset();

        assert toggle.get() == false;
    }
}