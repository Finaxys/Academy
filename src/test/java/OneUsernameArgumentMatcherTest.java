import com.finaxys.slackbot.BUL.Matchers.OneUsernameArgumentMatcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Bannou on 17/03/2017.
 */
public class OneUsernameArgumentMatcherTest {
    private OneUsernameArgumentMatcher oneUsernameArgumentMatcher;

    @Before
    public void setup() {
        oneUsernameArgumentMatcher = new OneUsernameArgumentMatcher();
    }

    @Test
    public void nonArgumentsTest() {
        final String message = "Lorem Ipsum";
        assertEquals(false, oneUsernameArgumentMatcher.isCorrect(message));
    }

    @Test
    public void exactlyOneUsenameArgumentsTest() {
        final String message = "Yo Bot ! Make <@U012ABCDEF|ernie> a challenge manager! :D";
        assertEquals(true, oneUsernameArgumentMatcher.isCorrect(message));
    }

    @Ignore
    @Test
    public void moreThanOneUsenameArgumentsTest() {
        final String message = "Yo Bot ! Make <@U012ABCDEF|ernie> and <@U012ABCDEF|ernie> administarators! :D";
        assertEquals(false, oneUsernameArgumentMatcher.isCorrect(message));
    }

    @Test
    public void extractUserIdTest() {
        final String message = "Yo Bot ! <@U012ABCDEF|ernie> got 130 points in JavaEE challenge ! Looool";
        assertEquals("U012ABCDEF", oneUsernameArgumentMatcher.getUserIdArgument(message));
    }
}
