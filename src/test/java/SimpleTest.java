import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SimpleTest {

    @Test
    public void simpleTest() {
        assertThat(new Main().calculate(1), is(2));
    }
}
