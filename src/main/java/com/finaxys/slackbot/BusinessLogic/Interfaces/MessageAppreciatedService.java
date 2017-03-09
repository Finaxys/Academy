package com.finaxys.slackbot.BusinessLogic.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;

public interface MessageAppreciatedService {
	
	void addMessageAppreciatedScore(JsonNode jsonNode);

}
