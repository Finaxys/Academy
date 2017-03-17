import com.finaxys.slackbot.BUL.Matchers.CreateChallengeMatcher;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by inesnefoussi on 3/16/17.
 */
public class CreateChallengeMatcherTest {
    private CreateChallengeMatcher createChallengeMatcher;

    @Before
    public void setFixture() {
        createChallengeMatcher = new CreateChallengeMatcher();
    }

    @Test
    public void validString() {
        final String command = "\"JavaEE\" \"group\" \"desc1\"";
        assertEquals(true, createChallengeMatcher.match(command));
    }

    @Test
    public void validStringWithInEnglish() {
        final String command = "ch1 individual blabla";
        assertNotEquals(true, createChallengeMatcher.match(command));
    }
}
