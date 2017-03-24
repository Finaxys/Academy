import com.finaxys.slackbot.BUL.Matchers.EventScoreAddMatcher;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by user on 23/03/2017.
 */
public class EventScoreAddMatcherTest {
    EventScoreAddMatcher eventScoreAddMatcher;
    @Before
    public void setup() {
        eventScoreAddMatcher = new EventScoreAddMatcher();

    }
    @Test
    public void ScoreArgumentExtractTest() {
        final String message = "\"aaaaaaaaaaaaaaaaaaaaaaa\" 50 points" ;

        assertEquals(50, eventScoreAddMatcher.getActionScoreArgument(message));
    }
    @Test
    public void ActionArgumentExtractTest() {
        final String message = "\"action\" 50 points" ;

        assertEquals("action", eventScoreAddMatcher.getActionNameArgument(message));
    }
    @Test
    public void ArgumentsScoreShouldNotBeEmtyTest() {
        final String message = "\"Lorem Ipsum\"";
        assertEquals(false, eventScoreAddMatcher.isCorrect(message));
    }
    @Test
    public void ArgumentsActionShouldBeBetweenQuotesEmtyTest() {
        final String message = "\"Lorem Ipsum...";
        assertEquals(false, eventScoreAddMatcher.isCorrect(message));
    }
    @Test
    public void ArgumentsScoreNegativeTest() {
        final String message = "\"placer un collab\" -50";
        assertEquals(-50,  eventScoreAddMatcher.getActionScoreArgument(message));
    }
    @Test
    public void ArgumentsScorePositiveTest() {
        final String message = "\"placer un collab\" 50 points";
        assertEquals(50,  eventScoreAddMatcher.getActionScoreArgument(message));
    }
    @Test
    public void ShouldContainsScoreTest() {
        final String message = "\"placer un collab\" 50 points";
        assertEquals(true,  eventScoreAddMatcher.isCorrect(message));
    }
    @Test
    public void ScoreShouldNotBeZeroScoreTest() {
        final String message = "\"placer un collab\" 0 points";
        assertEquals(false,  eventScoreAddMatcher.isCorrect(message));
    }

}
