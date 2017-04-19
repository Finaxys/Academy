import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.finaxys.slackbot.BUL.Matchers.ContestScoreAddMatcher;

/**
 * Created by user on 23/03/2017.
 */
public class ContestScoreAddMatcherTest {
    ContestScoreAddMatcher contestScoreAddMatcher;
    @Before
    public void setup() {
        contestScoreAddMatcher = new ContestScoreAddMatcher();

    }
    @Test
    public void ScoreArgumentExtractTest() {
        final String message = "\"aaaaaaaaaaaaaaaaaaaaaaa\" 50 points" ;

        assertEquals(50, contestScoreAddMatcher.getActionScoreArgument(message));
    }
    @Test
    public void ActionArgumentExtractTest() {
        final String message = "\"action\" 50 points" ;

        assertEquals("action", contestScoreAddMatcher.getActionNameArgument(message));
    }
    @Test
    public void ArgumentsScoreShouldNotBeEmtyTest() {
        final String message = "\"Lorem Ipsum\"";
        assertEquals(false, contestScoreAddMatcher.isCorrect(message));
    }
    @Test
    public void ArgumentsActionShouldBeBetweenQuotesEmtyTest() {
        final String message = "\"Lorem Ipsum...";
        assertEquals(false, contestScoreAddMatcher.isCorrect(message));
    }
    @Test
    public void ArgumentsScoreNegativeTest() {
        final String message = "\"placer un collab\" -50";
        assertEquals(-50,  contestScoreAddMatcher.getActionScoreArgument(message));
    }
    @Test
    public void ArgumentsScorePositiveTest() {
        final String message = "\"placer un collab\" 50 points";
        assertEquals(50,  contestScoreAddMatcher.getActionScoreArgument(message));
    }
    @Test
    public void ShouldContainsScoreTest() {
        final String message = "\"placer un collab\" 50 points";
        assertEquals(true,  contestScoreAddMatcher.isCorrect(message));
    }
    @Test
    public void ScoreShouldNotBeZeroScoreTest() {
        final String message = "\"placer un collab\" 0 points";
        assertEquals(false,  contestScoreAddMatcher.isCorrect(message));
    }

}
