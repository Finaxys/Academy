import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Bannou on 08/03/2017.
 */

public class TribeChannelPatternTest {
    @Test
    public void testEmpty() {
        final String message = "";
        TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher(message);
        assertEquals(false, tribeChannelMatcher.isTribe());
    }

    @Test
    public void testCorrectShortName() {
        final String message = "tribu-";
        TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher(message);
        assertEquals(true, tribeChannelMatcher.isTribe());
    }

    @Test
    public void testCorrectFullName() {
        final String message = "tribu-*Algo1";
        TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher(message);
        assertEquals(true, tribeChannelMatcher.isTribe());
    }

    @Test
    public void testUpperCaseSameName() {
        final String message = "TRIBU-";
        TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher(message);
        assertEquals(false, tribeChannelMatcher.isTribe());
    }


}
