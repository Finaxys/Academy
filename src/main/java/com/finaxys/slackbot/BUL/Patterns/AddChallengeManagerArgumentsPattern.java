package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Sahar on 23/03/2017.
 */
public class AddChallengeManagerArgumentsPattern {
    private static Pattern pattern;
    final String challengeManagerArgumentsPattern;

    private AddChallengeManagerArgumentsPattern() {
        challengeManagerArgumentsPattern = ".+(<@\\w{9}|.*>){1}";
        pattern = Pattern.compile(challengeManagerArgumentsPattern);
    }

    public static Pattern getPattern() {
        if (pattern == null) new AddChallengeManagerArgumentsPattern();
        return pattern;
    }
}
