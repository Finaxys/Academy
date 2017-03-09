package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import allbegray.slack.type.Channel;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.Utilities.WebApiFactory;

/**
 * Created by Bannou on 08/03/2017.
 */
public class MessagePostedListener implements EventListener {
    @Override
    public void handleMessage(JsonNode jsonNode) {
        String message = jsonNode.get("message").asText();
        String channelId = jsonNode.get("channel").asText();
        System.out.println("Message = " + message + " - from channel = " + channelId);
        Channel channel = WebApiFactory.getSlackWebApiClient().getChannelInfo(channelId);
        TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher(channel.getName());
        if (!tribeChannelMatcher.isTribe()) return;
        System.out.print(channel.getName() + " is a Tribu");
    }
}
