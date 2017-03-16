package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Bannou on 16/03/2017.
 */
public class ScoreArgumentPattern {
    private static Pattern pattern;
    final String scoreArgumentPattern;

    private ScoreArgumentPattern() {
        scoreArgumentPattern = "\\d{1,3}\\spoints";
        pattern = Pattern.compile(scoreArgumentPattern);
    }

    public static Pattern getPattern() {
        if (pattern == null) new ScoreArgumentPattern();
        return pattern;
    }
}
