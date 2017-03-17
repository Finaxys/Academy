package com.finaxys.slackbot.BUL.Matchers;

import com.finaxys.slackbot.BUL.Patterns.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bannou on 16/03/2017.
 */
public class ChallengeScoreArgumentsMatcher {
    private Matcher matcher;
    private List<Pattern> addChallengeScoreArgumentsPatterns;
    private Pattern listChallengeScoreArgumentsPatterns;
    private Pattern challengeNameArgument;
    private Pattern scoreArgument;
    private Pattern finaxysProfileIdArgument;

    public ChallengeScoreArgumentsMatcher() {
        addChallengeScoreArgumentsPatterns = new ArrayList<>();
        addChallengeScoreArgumentsPatterns.add(AddChallengeScoreArgumentsPattern.getPattern());
        listChallengeScoreArgumentsPatterns = ListScoresByChallengsArgumentsPattern.getPattern();
        challengeNameArgument = ChallengeNameArgumentPattern.getPattern();
        scoreArgument = ScoreArgumentPattern.getPattern();
        finaxysProfileIdArgument = UserIdArgumentPattern.getPattern();
    }

    public boolean isCorrect(String message) {
        for (Pattern pattern : addChallengeScoreArgumentsPatterns)
            if (!pattern.matcher(message).matches()) return false;
        return true;
    }

    public boolean isCorrectListRequest(String message) {
        return listChallengeScoreArgumentsPatterns.matcher(message).matches();
    }

    public String getChallengeName(String message) {
        matcher = challengeNameArgument.matcher(message);
        return matcher.find() ? matcher.group().substring(0, matcher.group().indexOf(" ")) : "";
    }

    public String getScore(String message) {
        matcher = scoreArgument.matcher(message);
        return matcher.find() ? matcher.group().substring(0, matcher.group().indexOf(" ")) : "";
    }

    public String getFinaxysProfileId(String message) {
        matcher = finaxysProfileIdArgument.matcher(message);
        return matcher.find() ? matcher.group().substring(1) : "";
    }


}
