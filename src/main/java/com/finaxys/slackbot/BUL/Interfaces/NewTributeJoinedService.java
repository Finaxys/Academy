package com.finaxys.slackbot.BUL.Interfaces;

import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by inesnefoussi on 3/7/17.
 */
public interface NewTributeJoinedService {

    public void onNewTributeJoined(JsonNode jsonNode , SlackWebApiClient webApiClient);
}
