package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Bannou on 17/03/2017.
 */
public class UserIdArgumentPattern {
    private static Pattern pattern;
    final String userIdArgumentPattern;

    private UserIdArgumentPattern() {
        userIdArgumentPattern = "@\\w{10}";
        pattern = Pattern.compile(userIdArgumentPattern);
    }

    public static Pattern getPattern() {
        if (pattern == null) new UserIdArgumentPattern();
        return pattern;
    }
}
