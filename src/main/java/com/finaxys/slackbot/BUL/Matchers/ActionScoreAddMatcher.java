package com.finaxys.slackbot.BUL.Matchers;

import com.finaxys.slackbot.BUL.Patterns.ActionAddPattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 23/03/2017.
 */
public class ActionScoreAddMatcher {
   ActionAddPattern actionAddPattern;

    public String getActionNameArgument(String s)

    { String action= s.substring(1, s.indexOf("\"", s.indexOf("\"") + 1));
    return action;}

    public int getActionScoreArgument(String s)
    {
        String points= s.substring(1+s.indexOf("\"", s.indexOf("\"") + 1));
        if(points.contains("-")){
        return -Integer.parseInt(extractScore(points));
    }
        return Integer.parseInt(extractScore(points));
    }
    public  String extractScore(String s) {
        Pattern p = Pattern.compile("\\d+"); // Ici ton regex => ta chaine de caractere a trouver
        Matcher m = p.matcher(s); // s ta chaine titi23àde
        String result = "";
        while (m.find()) // tant qu'il arrive a matcher ton regex ds la chaine de caractere s
            result += m.group();
        return result;
    }
    public boolean isCorrect(String message) {

            if (!actionAddPattern.getPattern().matcher(message).matches()) return false;
        return true;
    }


}
