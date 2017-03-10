package com.finaxys.slackbot.BUL.Listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.Utilities.WebApiFactory;

import allbegray.slack.rtm.EventListener;
import allbegray.slack.type.Channel;
import allbegray.slack.type.User;

/**
 * Created by Bannou on 08/03/2017.
 */
@Component
public class ChannelTributeCreatedListener implements EventListener {
	@Autowired
	InnovateService innovateService;

	@Override
	public void handleMessage(JsonNode jsonNode) {

		
	}
}
