import com.finaxys.slackbot.BUL.Matchers.ChallengeTypeMatcher;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by inesnefoussi on 3/17/17.
 */
public class ChallengeTypeMatcherTester {
    private ChallengeTypeMatcher challengeTypeMatcher;

    @Before
    public void setupFixture() {
        challengeTypeMatcher = new ChallengeTypeMatcher();
    }

    @Test
    public void groupTypeTest() {
        final String testedType = " group ";
        assertEquals(true , challengeTypeMatcher.match(testedType));
    }

    @Test
    public void individualTypeTest() {
        final String testedType = "individual";
        assertEquals(true,challengeTypeMatcher.match(testedType));
    }

    @Test
    public void nonValidTypeTest() {
        final String testedType = "whatever";
        assertNotEquals(true,challengeTypeMatcher.match(testedType));
    }
}
