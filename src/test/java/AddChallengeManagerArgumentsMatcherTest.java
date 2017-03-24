import com.finaxys.slackbot.BUL.Matchers.ChallengeManagerArgumentsMatcher;
import com.finaxys.slackbot.BUL.Matchers.ChallengeScoreArgumentsMatcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Sahar on 23/03/2017.
 */
public class AddChallengeManagerArgumentsMatcherTest {
    private ChallengeManagerArgumentsMatcher challengeManagerArgumentsMatcher;
    @Before
    public void setup() {
        challengeManagerArgumentsMatcher = new ChallengeManagerArgumentsMatcher();
    }

    @Test
    public void nonArgumentsTest() {
        final String message = "java ee <@U012ABCDF|ernie>";
        assertEquals(true, challengeManagerArgumentsMatcher.isCorrect(message));
    }
    @Test
    public void getChallengeTest() {
        final String message ="java ee <@U012ABCDF|ernie>";
        String challengeName = challengeManagerArgumentsMatcher.getChallengeName(message);
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
