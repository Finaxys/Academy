package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Bannou on 10/03/2017.
 */
public class RegularMessagePattern {
    private static Pattern pattern;
    final String regularMessagePattern;

    private RegularMessagePattern() {
        regularMessagePattern = ".{3}";
        pattern = Pattern.compile(regularMessagePattern);
    }

    public static Pattern getRegularMessagePattern() {
        if (pattern == null) new RegularMessagePattern();
        return pattern;
    }
}
