package com.finaxys.slackbot.BUL.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;

public interface NewTribeJoinedService {
	
    void onNewTribeJoined(JsonNode jsonNode);
}
