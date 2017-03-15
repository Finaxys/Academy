package com.finaxys.slackbot.BUL.Interfaces;

import com.fasterxml.jackson.databind.JsonNode;

public interface ReactionRemovedService {
    void substituteReactionRemovedScore(JsonNode jsonNode);

}
