package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Bannou on 16/03/2017.
 */
public class OneUsernameArgumentPattern {
    private static Pattern pattern;
    final String oneUsernameArgumentPattern;

    private OneUsernameArgumentPattern() {
        oneUsernameArgumentPattern = ".*(<@\\w{10}|.*>){1}.*";
        pattern = Pattern.compile(oneUsernameArgumentPattern);
    }

    public static Pattern getPattern() {
        if (pattern == null) new OneUsernameArgumentPattern();
        return pattern;
    }
}
