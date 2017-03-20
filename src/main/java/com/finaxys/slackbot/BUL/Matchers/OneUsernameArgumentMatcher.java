package com.finaxys.slackbot.BUL.Matchers;

import com.finaxys.slackbot.BUL.Patterns.OneUsernameArgumentPattern;
import com.finaxys.slackbot.BUL.Patterns.UserIdArgumentPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bannou on 16/03/2017.
 */
public class OneUsernameArgumentMatcher {
    private Matcher matcher;
    private List<Pattern> oneUsernameArgumenPatterns;
    private Pattern userIdArgumentPattern;

    public OneUsernameArgumentMatcher() {
        oneUsernameArgumenPatterns = new ArrayList<>();
        oneUsernameArgumenPatterns.add(OneUsernameArgumentPattern.getPattern());
        userIdArgumentPattern = UserIdArgumentPattern.getPattern();
    }

    public boolean isCorrect(String message) {
        for (Pattern pattern : oneUsernameArgumenPatterns)
            if (!pattern.matcher(message).matches()) return false;
        return true;
    }

    public String getUserIdArgument(String message) {
        matcher = userIdArgumentPattern.matcher(message);
        return matcher.find() ? matcher.group().substring(1) : "";
    }
}
