package com.finaxys.slackbot.BUL.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by Bannou on 08/03/2017.
 */
public interface RealMessageReward {
    void rewardReadMessage(JsonNode jsonNode);
}
