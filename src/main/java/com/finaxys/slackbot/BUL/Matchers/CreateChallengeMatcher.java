package com.finaxys.slackbot.BUL.Matchers;

import com.finaxys.slackbot.BUL.Patterns.CreateChallengePattern;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by inesnefoussi on 3/16/17.
 */
public class CreateChallengeMatcher {
    private Matcher matcher;
    private List<Pattern> createChallengePatterns;

    public CreateChallengeMatcher() {
        createChallengePatterns = new ArrayList<>();
        createChallengePatterns.add(CreateChallengePattern.getPattern());
    }

    public boolean match(String message) {
        for (Pattern pattern : createChallengePatterns)
            if (!pattern.matcher(message).matches()) return false;
        return true;
    }
}
