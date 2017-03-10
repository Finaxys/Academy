package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Bannou on 10/03/2017.
 */
public class NonAlphabeticMessagePattern {
    private static Pattern pattern;
    final String nonAlphabeticMessagePattern;

    private NonAlphabeticMessagePattern() {
        nonAlphabeticMessagePattern = "[^a-zA-Z]";
        pattern = Pattern.compile(nonAlphabeticMessagePattern);
    }

    public static Pattern getNonAlphabeticMessagePattern() {
        if (pattern == null) new NonAlphabeticMessagePattern();
        return pattern;
    }
}
