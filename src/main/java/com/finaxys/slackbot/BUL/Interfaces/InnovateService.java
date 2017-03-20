package com.finaxys.slackbot.BUL.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;

public interface InnovateService {

    void rewardTribeCreator(JsonNode js);

    void rewardFileSharing(JsonNode js);
}
