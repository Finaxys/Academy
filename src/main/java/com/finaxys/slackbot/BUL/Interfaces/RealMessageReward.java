package com.finaxys.slackbot.BUL.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;

public interface RealMessageReward {
    void rewardReadMessage(JsonNode jsonNode);
}
