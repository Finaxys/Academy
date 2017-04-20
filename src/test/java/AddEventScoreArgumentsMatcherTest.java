import com.finaxys.slackbot.BUL.Matchers.EventScoreArgumentsMatcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Bannou on 16/03/2017.
 */
public class AddEventScoreArgumentsMatcherTest {
    private EventScoreArgumentsMatcher addEventScoreArgumentsMatcher;

    @Before
    public void setup() {
        addEventScoreArgumentsMatcher = new EventScoreArgumentsMatcher();
    }

    @Test
    public void nonArgumentsTest() {
        final String message = "Lorem Ipsum";
        assertEquals(false, addEventScoreArgumentsMatcher.isCorrect(message));
    }

    @Test
    @Ignore
    public void missingEventArgumentTest() {
        final String message = "Yo Bot ! <@U012ABCEF|ernie> got 20 points in JavaEE ! :D";
        assertEquals(false, addEventScoreArgumentsMatcher.isCorrect(message));
    }

    @Test
    @Ignore
    public void threeCorrectArgumentsFirstTest() {
        final String message = "Yo Bot ! <@U012ABCDF|ernie> got 20 points in JavaEE challenge ! :D";
        assertEquals(true, addEventScoreArgumentsMatcher.isCorrect(message));
    }

    @Test
    @Ignore
    public void threeCorrectArgumentsSecondTest() {
        final String message = "<@U012ABCDF|ernie> 130 points JavaEE challenge";
        assertEquals(true, addEventScoreArgumentsMatcher.isCorrect(message));
    }

    @Test
    public void moreThanThreeArgumentsTest() {
        final String message = "*./+";
        assertEquals(false, addEventScoreArgumentsMatcher.isCorrect(message));
    }

    @Test
    @Ignore
    public void extractEventNameTest() {
        final String message = "Yo Bot ! <@U012ABCDEF|ernie> got 130 points in JavaEE challenge ! Looool";
        assertEquals("JavaEE", addEventScoreArgumentsMatcher.getEventName(message));
    }

    @Test
    @Ignore
    public void extractScoreTest() {
        final String message = "Yo Bot ! <@U012ABCDEF|ernie> got 130 points in JavaEE challenge ! Looool";
        assertEquals("130", addEventScoreArgumentsMatcher.getScore(message));
    }

    @Test
    @Ignore
    public void extractFinaxysProfileId() {
        final String message = "Yo Bot ! <@U012ABCDEF|ernie> got 130 points in JavaEE challenge ! Looool";
        assertEquals("U012ABCDE", addEventScoreArgumentsMatcher.getFinaxysProfileId(message));
    }
}