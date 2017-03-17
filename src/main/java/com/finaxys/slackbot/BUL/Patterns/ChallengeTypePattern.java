package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

/**
 * Created by inesnefoussi on 3/17/17.
 */
public class ChallengeTypePattern {
    private static Pattern pattern;
    private String regex;

    private ChallengeTypePattern() {
        regex = "\\s*(group|individual)\\s*";
        pattern = Pattern.compile(regex);
    }

    public static Pattern getPattern() {
        if (pattern == null) new ChallengeTypePattern();
        return pattern;
    }
}
