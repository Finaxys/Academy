package com.finaxys.slackbot.BUL.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;

public interface MessageAppreciatedService {
	
	void addMessageAppreciatedScore(JsonNode jsonNode);

}
