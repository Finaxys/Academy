import com.finaxys.slackbot.BUL.Matchers.AddChallengeScoreArgumentsMatcher;
import com.finaxys.slackbot.BUL.Matchers.RealMessageMatcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Bannou on 16/03/2017.
 */
public class AddChallengeScoreArgumentsMatcherTest {
    private AddChallengeScoreArgumentsMatcher addChallengeScoreArgumentsMatcher;

    @Before
    public void setup() {
        addChallengeScoreArgumentsMatcher = new AddChallengeScoreArgumentsMatcher();
    }

    @Test
    public void nonArgumentsTest() {
        final String message = "Lorem Ipsum";
        assertEquals(false, addChallengeScoreArgumentsMatcher.isCorrectAddChallengeScoreArguments(message));
    }

    @Test
    public void missingChallengeArgumentTest() {
        final String message = "Yo Bot ! <@U012ABCDEF|ernie> got 20 points in JavaEE challenges ! :D";
        assertEquals(false, addChallengeScoreArgumentsMatcher.isCorrectAddChallengeScoreArguments(message));
    }

    @Test
    public void threeCorrectArgumentsFirstTest() {
        final String message = "Yo Bot ! <@U012ABCDEF|ernie> got 20 points in JavaEE challenge ! :D";
        assertEquals(true, addChallengeScoreArgumentsMatcher.isCorrectAddChallengeScoreArguments(message));
    }

    @Test
    public void threeCorrectArgumentsSecondTest() {
        final String message = "Yo Bot ! <@U012ABCDEF|ernie> got 130 points in JavaEE challenge ! Looool";
        assertEquals(true, addChallengeScoreArgumentsMatcher.isCorrectAddChallengeScoreArguments(message));
    }

    @Test
    public void moreThanThreeArgumentsTest() {
        final String message = "*./+";
        assertEquals(false, addChallengeScoreArgumentsMatcher.isCorrectAddChallengeScoreArguments(message));
    }

    @Test
    public void extractChallengeNameTest() {
        final String message = "Yo Bot ! <@U012ABCDEF|ernie> got 130 points in JavaEE challenge ! Looool";
        assertEquals("JavaEE", addChallengeScoreArgumentsMatcher.getChallengeName(message));
    }

    @Test
    public void extractScoreTest() {
        final String message = "Yo Bot ! <@U012ABCDEF|ernie> got 130 points in JavaEE challenge ! Looool";
        assertEquals("130", addChallengeScoreArgumentsMatcher.getScore(message));
    }
}