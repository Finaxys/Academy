import com.finaxys.slackbot.BUL.Matchers.DateMatcher;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by inesnefoussi on 3/17/17.
 */
public class DateMatcherTester {
    private DateMatcher dateMatcher;

    @Before
    public void setupFixture() {
        dateMatcher = new DateMatcher();
    }

    @Test
    public void correctFormatTest() {
        final String testedDate = "2017-05-04";
        assertEquals(true , dateMatcher.match(testedDate));
    }

    @Test
    public void dateWithSlashTest() {
        final String testedDate = "2017/05/04";
        assertNotEquals(true , dateMatcher.match(testedDate));
    }

    @Test
    public void ddMMyyyyFormatTest() {
        final String testedDate = "04-05-2017";
        assertNotEquals(true , dateMatcher.match(testedDate));
    }
}
