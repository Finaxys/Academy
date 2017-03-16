package com.finaxys.slackbot.BUL.Matchers;

import com.finaxys.slackbot.BUL.Patterns.AddChallengeScoreArgumentsPattern;
import com.finaxys.slackbot.BUL.Patterns.ChallengeNameArgumentPattern;
import com.finaxys.slackbot.BUL.Patterns.ScoreArgumentPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bannou on 16/03/2017.
 */
public class AddChallengeScoreArgumentsMatcher {
    private Matcher matcher;
    private List<Pattern> addChallengeScoreArgumentsPatterns;
    private Pattern challengeNameArgument;
    private Pattern scoreArgument;

    public AddChallengeScoreArgumentsMatcher() {
        addChallengeScoreArgumentsPatterns = new ArrayList<>();
        addChallengeScoreArgumentsPatterns.add(AddChallengeScoreArgumentsPattern.getPattern());
        challengeNameArgument = ChallengeNameArgumentPattern.getPattern();
        scoreArgument = ScoreArgumentPattern.getPattern();
    }

    public boolean isCorrectAddChallengeScoreArguments(String message) {
        for (Pattern pattern : addChallengeScoreArgumentsPatterns)
            if (!pattern.matcher(message).matches()) return false;
        return true;
    }

    public String getChallengeName(String message) {
        matcher = challengeNameArgument.matcher(message);
        return matcher.find() ? matcher.group().substring(0, matcher.group().indexOf(" ")) : "";
    }

    public String getScore(String message) {
        matcher = scoreArgument.matcher(message);
        return matcher.find() ? matcher.group().substring(0, matcher.group().indexOf(" ")) : "";
    }
}
