import com.finaxys.slackbot.BUL.Matchers.ChallengeScoreArgumentsMatcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Bannou on 16/03/2017.
 */
public class AddChallengeScoreArgumentsMatcherTest {
    private ChallengeScoreArgumentsMatcher addChallengeScoreArgumentsMatcher;

    @Before
    public void setup() {
        addChallengeScoreArgumentsMatcher = new ChallengeScoreArgumentsMatcher();
    }

    @Test
    public void nonArgumentsTest() {
        final String message = "Lorem Ipsum";
        assertEquals(false, addChallengeScoreArgumentsMatcher.isCorrect(message));
    }
@Ignore
    @Test
    public void missingChallengeArgumentTest() {
        final String message = "Yo Bot ! <@U012ABCEF|ernie> got 20 points in JavaEE challenges ! :D";
        assertEquals(false, addChallengeScoreArgumentsMatcher.isCorrect(message));
    }

    @Test
    public void threeCorrectArgumentsFirstTest() {
        final String message = "Yo Bot ! <@U012ABCDF|ernie> got 20 points in JavaEE challenge ! :D";
        assertEquals(true, addChallengeScoreArgumentsMatcher.isCorrect(message));
    }

    @Test
    public void threeCorrectArgumentsSecondTest() {
        final String message = "Yo Bot ! <@U012ACDF|ernie> got 130 points in JavaEE challenge ! Looool";
        assertEquals(true, addChallengeScoreArgumentsMatcher.isCorrect(message));
    }

    @Test
    public void moreThanThreeArgumentsTest() {
        final String message = "*./+";
        assertEquals(false, addChallengeScoreArgumentsMatcher.isCorrect(message));
    }

    @Test
    public void extractChallengeNameTest() {
        final String message = "Yo Bot ! <@U012ACDEF|ernie> got 130 points in JavaEE challenge ! Looool";
        assertEquals("JavaEE", addChallengeScoreArgumentsMatcher.getChallengeName(message));
    }

    @Test
    public void extractScoreTest() {
        final String message = "Yo Bot ! <@U012ABCDEF|ernie> got 130 points in JavaEE challenge ! Looool";
        assertEquals("130", addChallengeScoreArgumentsMatcher.getScore(message));
    }

    @Test
    public void extractFinaxysProfileId() {
        final String message = "Yo Bot ! <@U012ABCDEF|ernie> got 130 points in JavaEE challenge ! Looool";
        assertEquals("U012ABCDE", addChallengeScoreArgumentsMatcher.getFinaxysProfileId(message));
    }
}