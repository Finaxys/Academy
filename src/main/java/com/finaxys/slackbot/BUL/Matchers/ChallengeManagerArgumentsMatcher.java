package com.finaxys.slackbot.BUL.Matchers;

import com.finaxys.slackbot.BUL.Patterns.AddChallengeManagerArgumentsPattern;
import com.finaxys.slackbot.BUL.Patterns.UserIdArgumentPattern;
import com.finaxys.slackbot.BUL.Patterns.UserNameArgumentPattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sahar on 23/03/2017.
 */
public class ChallengeManagerArgumentsMatcher {
    private Matcher matcher;
    private Pattern addChallengeManagerArgumentsPattern;
    private Pattern userIdArgumentPattern;
    private Pattern userNameArgumentPattern;

    public ChallengeManagerArgumentsMatcher() {
        addChallengeManagerArgumentsPattern = AddChallengeManagerArgumentsPattern.getPattern();
        userIdArgumentPattern = UserIdArgumentPattern.getPattern();
        userNameArgumentPattern = UserNameArgumentPattern.getPattern();
    }

    public boolean isCorrect(String message) {

        if (!addChallengeManagerArgumentsPattern.matcher(message).matches()) return false;
        return true;
    }

    public String getChallengeName(String message) {
        String challengeName = message.substring(0,message.indexOf('<'));
        return challengeName.trim();
    }

    public String getUserName(String message) {
        String  userName = message.substring(message.indexOf('<'),message.length());
        return  userName.trim();
    }
    public String getUserIdArgument(String message) {
        matcher = userIdArgumentPattern.matcher(message);
        return matcher.find() ? matcher.group().substring(1) : "";
    }

    public String getUserNameArgument(String message) {
        matcher = userNameArgumentPattern.matcher(message);
        return matcher.find() ? matcher.group().substring(0) : "";
    }

}

