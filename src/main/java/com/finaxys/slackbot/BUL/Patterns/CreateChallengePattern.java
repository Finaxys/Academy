package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

/**
 * Created by inesnefoussi on 3/16/17.
 */
public class CreateChallengePattern {
    private static Pattern pattern;
    private String regex;

    private CreateChallengePattern() {
        regex = ".+,(group|individual),.+";
        pattern = Pattern.compile(regex);
    }

    public static Pattern getPattern() {
        if (pattern == null) new CreateChallengePattern();
        return pattern;
    }
}
