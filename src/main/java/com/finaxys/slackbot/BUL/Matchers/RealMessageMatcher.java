package com.finaxys.slackbot.BUL.Matchers;

import com.finaxys.slackbot.BUL.Patterns.NonAlphabeticMessagePattern;
import com.finaxys.slackbot.BUL.Patterns.RegularMessagePattern;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bannou on 09/03/2017.
 */
public class RealMessageMatcher {
    private Matcher matcher;
    private List<Pattern> unrealMessagePatterns;

    public RealMessageMatcher() {
        unrealMessagePatterns = new ArrayList<>();
        unrealMessagePatterns.add(RegularMessagePattern.getRegularMessagePattern());
        //unrealMessagePatterns.add(NonAlphabeticMessagePattern.getNonAlphabeticMessagePattern());
    }

    public boolean isRealMessage(String message) {
        for(Pattern pattern: unrealMessagePatterns)
            if (pattern.matcher(message).matches()) return false;
        return true;
    }
}
