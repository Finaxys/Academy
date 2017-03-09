package com.finaxys.slackbot.BusinessLogic.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;

import allbegray.slack.type.User;

public interface InnovateService {
	
	void addInnovateScore(User u);

}
