package com.finaxys.slackbot.BUL.Listeners;

import com.finaxys.slackbot.BUL.Interfaces.ChannelLeftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.BUL.Interfaces.NewTributeJoinedService;
import com.finaxys.slackbot.BUL.Interfaces.RealMessageReward;

import allbegray.slack.rtm.EventListener;

/**
 * Created by inesnefoussi on 3/7/17.
 */
@Component
public class MessageListener implements EventListener {

	@Autowired
	private NewTributeJoinedService newTributeJoinedService;

	@Autowired
	private RealMessageReward realMessageReward;

	@Autowired
	private InnovateService innovateService;

	@Autowired
	private ChannelLeftService channelLeftService;

	public MessageListener() {
	}

	public void handleMessage(JsonNode jsonNode) {
		System.out.print(jsonNode.toString());

		newTributeJoinedService.onNewTributeJoined(jsonNode);
		realMessageReward.rewardReadlMessage(jsonNode);
		innovateService.addInnovateScore(jsonNode, this);
		channelLeftService.onChannelLeaveMessage(jsonNode);
	}
}
