import com.finaxys.slackbot.BUL.Matchers.ActionScoreAddMatcher;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by user on 23/03/2017.
 */
public class ActionScoreAddMatcherTest {
    ActionScoreAddMatcher actionScoreAddMatcher;
    @Before
    public void setup() {
        actionScoreAddMatcher= new ActionScoreAddMatcher();

    }
    @Test
    public void ScoreArgumentExtractTest() {
        final String message = "\"aaaaaaaaaaaaaaaaaaaaaaa\" 50 points" ;

        assertEquals(50, actionScoreAddMatcher.getActionScoreArgument(message));
    }
    @Test
    public void ActionArgumentExtractTest() {
        final String message = "\"action\" 50 points" ;

        assertEquals("action", actionScoreAddMatcher.getActionNameArgument(message));
    }
    @Test
    public void ArgumentsScoreShouldNotBeEmtyTest() {
        final String message = "\"Lorem Ipsum\"";
        assertEquals(false, actionScoreAddMatcher.isCorrect(message));
    }
    @Test
    public void ArgumentsActionShouldBeBetweenQuotesEmtyTest() {
        final String message = "\"Lorem Ipsum...";
        assertEquals(false, actionScoreAddMatcher.isCorrect(message));
    }
    @Test
    public void ArgumentsScoreNegativeTest() {
        final String message = "\"placer un collab\" -50 points";
        assertEquals(-50,  actionScoreAddMatcher.getActionScoreArgument(message));
    }
    @Test
    public void ArgumentsScorePositiveTest() {
        final String message = "\"placer un collab\" 50 points";
        assertEquals(50,  actionScoreAddMatcher.getActionScoreArgument(message));
    }
    @Test
    public void ShouldContainsScoreTest() {
        final String message = "\"placer un collab\" 50 points";
        assertEquals(true,  actionScoreAddMatcher.isCorrect(message));
    }
    @Test
    public void ScoreShouldNotBeZeroScoreTest() {
        final String message = "\"placer un collab\" 0 points";
        assertEquals(false,  actionScoreAddMatcher.isCorrect(message));
    }

}
