package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

/**
 * Created by user on 23/03/2017.
 */
public class EventAddPattern {
    private static Pattern pattern;
    final String  EventAddArgumentPattern;
   ;

    public static Pattern getPattern() {
        if (pattern == null) new EventAddPattern();
        return pattern;
    }
    private EventAddPattern() {
        EventAddArgumentPattern = "^\".+\".+[1-9][0-9]*.*";
        pattern = Pattern.compile( EventAddArgumentPattern);
    }

}