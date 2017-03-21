package com.finaxys.slackbot.BUL.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by Bannou on 21/03/2017.
 */
public interface FinaxysProfileService {
    public void onChannelLeaveMessage(JsonNode jsonNode);
}
