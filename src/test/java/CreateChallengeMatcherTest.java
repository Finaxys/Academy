import com.finaxys.slackbot.BUL.Matchers.CreateChallengeMatcher;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        final String command = "Hey fx, tu peux me creer un challenge dont le nom est ch1, de type group et tu mets comme description desc1?";
        assertEquals(true, createChallengeMatcher.match(command));
    }

    @Test
    public void validStringWithInEnglish() {
        final String command = "Hey fx, please create a challenge with name ch1 and type individual having this description: blabla";
        assertEquals(true, createChallengeMatcher.match(command));
    }

    @Test
    public void orderNonRespected() {
        final String command = "Hey fx, please create a challenge with name ch1 and type individual having this description: blabla";
        assertEquals(true, createChallengeMatcher.match(command));
    }
}
