package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

/**
 * Created by inesnefoussi on 3/9/17.
 */
@Component
public class ChannelCreatedListener implements EventListener {


    @Override
    public void handleMessage(JsonNode jsonNode) {
    }
}
