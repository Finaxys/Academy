package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.finaxys.slackbot.BUL.Interfaces.ChannelLeftService;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.BUL.Interfaces.NewTribeJoinedService;
import com.finaxys.slackbot.BUL.Interfaces.RealMessageReward;

@Component
public class MessageListener implements EventListener {

	@Autowired
	private NewTribeJoinedService newTribeJoinedService;

	@Autowired
	private RealMessageReward realMessageReward;

	@Autowired
	private InnovateService innovateService;

	@Autowired
	private ChannelLeftService channelLeftService;

	public MessageListener() {
	}

	public void handleMessage(JsonNode jsonNode) {
		if (!jsonNode.has("subtype"))
		realMessageReward.rewardReadMessage(jsonNode);
		else {
			String messageSubtype = jsonNode.get("subtype").asText();

			if (messageSubtype.equals("channel_join"))
			newTribeJoinedService.onNewTribeJoined(jsonNode);
			
			else if (messageSubtype.equals("file_share"))
			innovateService.rewardFileShared(jsonNode);
			
			else if (messageSubtype.equals("channel_leave"))
			channelLeftService.onChannelLeaveMessage(jsonNode);
		}

	}
}
