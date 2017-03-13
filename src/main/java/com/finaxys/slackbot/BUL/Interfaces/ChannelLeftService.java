package com.finaxys.slackbot.BUL.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by inesnefoussi on 3/13/17.
 */
public interface ChannelLeftService {
    void onChannelLeaveMessage(JsonNode jsonNode);
}
