package com.finaxys.slackbot;


import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 23/03/2017.
 */
public class main {

    public static String extractScore(String s) {
        Pattern p = Pattern.compile("\\d+"); // Ici ton regex => ta chaine de caractere a trouver
        Matcher m = p.matcher(s); // s ta chaine titi23àde
        String result = "";
        while (m.find()) // tant qu'il arrive a matcher ton regex ds la chaine de caractere s
            result += m.group();
        return result;
    }


    public static void main(String[] args) {
     String text="\"le commercial a cree une collaboration avec  \" 50 points";
      int i=text.indexOf("\"",text.indexOf("\"")+1);
        String action= text.substring(1, text.indexOf("\"", text.indexOf("\"") + 1));
        String points= text.substring(1+text.indexOf("\"", text.indexOf("\"") + 1));

                System.out.println("action: " + action + " \n" + "points:" + extractScore(points));
    }


}
