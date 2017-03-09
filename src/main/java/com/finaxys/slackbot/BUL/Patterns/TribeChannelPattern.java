package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Bannou on 08/03/2017.
 */
public class TribeChannelPattern {
    private static Pattern pattern;
    final String tribeChannelPattern;

    private TribeChannelPattern() {
        tribeChannelPattern = "tribu-.*";
        pattern = Pattern.compile(tribeChannelPattern);
    }

    public static Pattern getTribeChannelPattern() {
        if (pattern == null) new TribeChannelPattern();
        return pattern;
    }
}
