package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Bannou on 17/03/2017.
 */
public class ListScoresByChallengsArgumentsPattern {
    private static Pattern pattern;
    final String challengeScoreArgumentsPattern;

    private ListScoresByChallengsArgumentsPattern() {
        challengeScoreArgumentsPattern = ".*\\w*\\schallenge.*";
        pattern = Pattern.compile(challengeScoreArgumentsPattern);
    }

    public static Pattern getPattern() {
        if (pattern == null) new ListScoresByChallengsArgumentsPattern();
        return pattern;
    }
}
