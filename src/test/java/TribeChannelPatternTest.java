import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Bannou on 08/03/2017.
 */

public class TribeChannelPatternTest {
    private TribeChannelMatcher tribeChannelMatcher;

    @BeforeClass
    public void setup() {
        tribeChannelMatcher = new TribeChannelMatcher();
    }

    @Test
    public void testEmpty() {
        final String message = "";
        assertEquals(true, tribeChannelMatcher.isNotTribe(message));
    }

    @Test
    public void testCorrectShortName() {
        final String message = "tribu-";
        assertEquals(false, tribeChannelMatcher.isNotTribe(message));
    }

    @Test
    public void testCorrectFullName() {
        final String message = "tribu-*Algo1";
        assertEquals(false, tribeChannelMatcher.isNotTribe(message));
    }

    @Test
    public void testUpperCaseSameName() {
        final String message = "TRIBU-";
        assertEquals(true, tribeChannelMatcher.isNotTribe(message));
    }
}
