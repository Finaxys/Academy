package com.finaxys.slackbot.BUL.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Listeners.ChannelCreatedListener;
import com.finaxys.slackbot.BUL.Listeners.MessageListener;

public interface InnovateService {

	void addInnovateScore(JsonNode js,MessageListener MessageListener);
	void addInnovateScore(JsonNode js,ChannelCreatedListener channelCreatedListener);

}
