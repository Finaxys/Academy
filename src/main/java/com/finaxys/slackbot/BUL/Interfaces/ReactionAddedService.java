package com.finaxys.slackbot.BUL.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;

public interface ReactionAddedService {
	
	void addReactionAddedScore(JsonNode jsonNode);

}
