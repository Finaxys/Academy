package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class DatePattern {
	
    private static Pattern pattern;
    private String regex;

    private DatePattern() {
    	
        regex 	= "^\\d{4}-\\d{2}-\\d{2}$";
        pattern = Pattern.compile(regex);
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new DatePattern();
        return pattern;
    }
}
