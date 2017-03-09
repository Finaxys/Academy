package com.finaxys.slackbot.BUL.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by inesnefoussi on 3/7/17.
 */
public interface NewTributeJoinedService {
    void onNewTributeJoined(JsonNode jsonNode);
}
