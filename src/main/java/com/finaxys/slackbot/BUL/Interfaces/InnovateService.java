package com.finaxys.slackbot.BUL.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;

public interface InnovateService {

	void rewardFileShared(JsonNode js);

	void rewardChannelCreated(JsonNode js);

}
