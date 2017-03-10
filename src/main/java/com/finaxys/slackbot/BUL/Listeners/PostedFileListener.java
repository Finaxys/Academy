package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import allbegray.slack.type.Channel;
import allbegray.slack.type.User;
import allbegray.slack.webapi.SlackWebApiClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;

@Component
public class PostedFileListener implements EventListener {
	@Autowired
	InnovateService innovateService;

	private SlackWebApiClient slackWebApiClient;

	public PostedFileListener(SlackWebApiClient slackWebApiClient) {
		super();

	}

	public SlackWebApiClient getSlackWebApiClient() {
		return slackWebApiClient;
	}

	public void setSlackWebApiClient(SlackWebApiClient slackWebApiClient) {
		this.slackWebApiClient = slackWebApiClient;
	}

	public PostedFileListener() {
	}

	public void handleMessage(JsonNode jsonNode) {
		FinaxysSlackBotLogger.logPostedFile();
	}
}
