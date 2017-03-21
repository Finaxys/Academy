import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by jihed on 20/03/2017.
 */
public class ListScoresMatcher {

    @Test
    public void onlyNumberArgumentsTest() {
        final String numberArg = "658";
        assertEquals(true, numberArg.trim().matches("^[1-9][0-9]*"));
    }


    @Test
    public void doNotBeginByZeroTest() {
        final String numberArg = "050";
        assertEquals(false, numberArg.trim().matches("^[1-9][0-9]*"));
    }
    @Test
    public void containsCarachterTest() {
        final String numberArg = "A50";
        assertEquals(false , numberArg.trim().matches("^[1-9][0-9]*"));
    }



}
