import com.finaxys.slackbot.BUL.Matchers.EventTypeMatcher;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by inesnefoussi on 3/17/17.
 */
public class EventTypeMatcherTester {
    private EventTypeMatcher eventTypeMatcher;

    @Before
    public void setupFixture() {
        eventTypeMatcher = new EventTypeMatcher();
    }

    @Test
    public void groupTypeTest() {
        final String testedType = " group ";
        assertEquals(true , eventTypeMatcher.match(testedType));
    }

    @Test
    public void individualTypeTest() {
        final String testedType = "individual";
        assertEquals(true,eventTypeMatcher.match(testedType));
    }

    @Test
    public void nonValidTypeTest() {
        final String testedType = "whatever";
        assertNotEquals(true,eventTypeMatcher.match(testedType));
    }
}
