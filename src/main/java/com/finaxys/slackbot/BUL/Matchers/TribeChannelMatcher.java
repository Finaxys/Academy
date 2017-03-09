package com.finaxys.slackbot.BUL.Matchers;

import com.finaxys.slackbot.BUL.Patterns.TribeChannelPattern;

import java.util.regex.Matcher;

/**
 * Created by Bannou on 08/03/2017.
 */
public class TribeChannelMatcher {
    private Matcher matcher;

    public TribeChannelMatcher() {
    }

    public TribeChannelMatcher(String channelName) {
        this.matcher = TribeChannelPattern.getTribeChannelPattern().matcher(channelName);
    }

    public boolean isTribe() {
        return matcher.matches();
    }
}
