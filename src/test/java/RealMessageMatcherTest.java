import com.finaxys.slackbot.BUL.Matchers.RealMessageMatcher;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Bannou on 10/03/2017.
 */
public class RealMessageMatcherTest {
    private RealMessageMatcher realMessageMatcher;

    @Before
    public void setup() {
        realMessageMatcher = new RealMessageMatcher();
    }

    @Test
    public void regularMessageTest() {
        final String message = "aaa";
        assertEquals(false, realMessageMatcher.isRealMessage(message));
    }

    @Ignore
    @Test
    public void nonAlphabeticMessageTest() {
        final String message = "*./+";
        assertEquals(false, realMessageMatcher.isRealMessage(message));
    }
}
