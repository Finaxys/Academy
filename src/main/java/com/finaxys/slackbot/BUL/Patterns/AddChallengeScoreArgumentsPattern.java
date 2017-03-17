package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Bannou on 16/03/2017.
 */
public class AddChallengeScoreArgumentsPattern {
    private static Pattern pattern;
    final String challengeScoreArgumentsPattern;

    private AddChallengeScoreArgumentsPattern() {
        challengeScoreArgumentsPattern = ".*<@\\w{9}|.*>.*\\d{1,3}.*\\s\\w*\\schallenge\\s.*";
        pattern = Pattern.compile(challengeScoreArgumentsPattern);
    }

    public static Pattern getPattern() {
        if (pattern == null) new AddChallengeScoreArgumentsPattern();
        return pattern;
    }
}
