package com.finaxys.slackbot.BUL.Matchers;

import java.util.regex.Matcher;

/**
 * Created by Bannou on 09/03/2017.
 */
public class RealMessageMatcher {
    private Matcher matcher;

    public RealMessageMatcher() {
    }

    public boolean isRealMessage(String message) {
        //return !TribeChannelPattern.getTribeChannelPattern().matcher(channelName).matches();
        return true;
    }
}
