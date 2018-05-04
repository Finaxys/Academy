package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.finaxys.slackbot.BUL.Interfaces.ChannelLeftService;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.BUL.Interfaces.NewTribeJoinedService;
import com.finaxys.slackbot.BUL.Interfaces.RealMessageReward;
import com.finaxys.slackbot.Utilities.SlackBot;

@Component
public class MessageListener implements EventListener 
{

	@Autowired
	private NewTribeJoinedService newTribeJoinedService;

	@Autowired
	private RealMessageReward realMessageReward;

	@Autowired
	private InnovateService innovateService;

	@Autowired
	private ChannelLeftService channelLeftService;

	public MessageListener() {}
	
	private void analyseMessage(JsonNode jsonNode) {
		String message = jsonNode.get("text").asText();
		SlackBot.postMessageToDebugChannelAsync("Hi " + SlackBot.getSlackWebApiClient().getUserInfo(jsonNode.get("user").asText()).getName());
		
	}

	public void handleMessage(JsonNode jsonNode) 
	{
		if (!jsonNode.has("subtype")) {
			realMessageReward.rewardReadMessage(jsonNode);
			
			System.out.println(jsonNode.get("text").asText());
			
			analyseMessage(jsonNode);
			
		}else 
		{
			System.out.println("coucoucou");
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
