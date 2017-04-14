package com.finaxys.slackbot.BUL.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;

public interface ChannelLeftService {
	
    void onChannelLeaveMessage(JsonNode jsonNode);
}
