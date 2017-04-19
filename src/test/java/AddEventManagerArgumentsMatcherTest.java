import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.finaxys.slackbot.BUL.Matchers.EventManagerArgumentsMatcher;

/**
 * Created by Sahar on 23/03/2017.
 */
public class AddEventManagerArgumentsMatcherTest {
    private EventManagerArgumentsMatcher challengeManagerArgumentsMatcher;
    @Before
    public void setup() {
        challengeManagerArgumentsMatcher = new EventManagerArgumentsMatcher();
    }

    @Test
    public void nonArgumentsTest() {
        final String message = "java ee <@U012ABCDF|ernie>";
        assertEquals(true, challengeManagerArgumentsMatcher.isCorrect(message));
    }
    @Test
    public void getEventTest() {
        final String message ="java ee <@U012ABCDF|ernie>";
        String challengeName = challengeManagerArgumentsMatcher.getEventName(message);
        Assert.assertEquals("java ee",challengeName);
    }
    @Test
    public void getNameTest() {
        final String message = "java ee <@U012ABCDF|ernie>";
        String name = challengeManagerArgumentsMatcher.getUserName(message);
        Assert.assertEquals("<@U012ABCDF|ernie>", name);
    }

    @Test
    public void getUserIdTest() {
        final String message ="java ee <@U012ABCDF|ernie>";
        String name = challengeManagerArgumentsMatcher.getUserIdArgument(message);
        Assert.assertEquals("U012ABCDF",name);
    }
}
