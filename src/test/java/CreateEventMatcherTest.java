import com.finaxys.slackbot.BUL.Matchers.CreateEventMatcher;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by inesnefoussi on 3/16/17.
 */
public class CreateEventMatcherTest {
    private CreateEventMatcher createEventMatcher;

    @Before
    public void setFixture() {
        createEventMatcher = new CreateEventMatcher();
    }

    @Test
    public void validString() {
        final String command = "Java EE,group,desc1";
        assertEquals(true, createEventMatcher.match(command));
    }

    @Test
    public void validStringWithInEnglish() {
        final String command = "ch1 individual blabla";
        assertNotEquals(true, createEventMatcher.match(command));
    }
}
