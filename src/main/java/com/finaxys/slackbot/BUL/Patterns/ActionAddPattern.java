package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

/**
 * Created by user on 23/03/2017.
 */
public class ActionAddPattern {
    private static Pattern pattern;
    final String  ActionAddArgumentPattern;
   ;

    public static Pattern getPattern() {
        if (pattern == null) new ActionAddPattern();
        return pattern;
    }
    private ActionAddPattern() {
        ActionAddArgumentPattern = "^\".+\".+[1-9][0-9]*.*";
        pattern = Pattern.compile( ActionAddArgumentPattern);
    }

}
