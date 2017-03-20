package com.finaxys.slackbot.BUL.Matchers;

import com.finaxys.slackbot.BUL.Patterns.DatePattern;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by inesnefoussi on 3/17/17.
 */
public class DateMatcher {
    private List<Pattern> dateMatcherPatterns;

    public DateMatcher() {
        dateMatcherPatterns = new ArrayList<>();
        dateMatcherPatterns.add(DatePattern.getPattern());
    }

    public boolean match(String message) {
        for (Pattern pattern : dateMatcherPatterns)
            if (!pattern.matcher(message).matches()) return false;
        return true;
    }
}
