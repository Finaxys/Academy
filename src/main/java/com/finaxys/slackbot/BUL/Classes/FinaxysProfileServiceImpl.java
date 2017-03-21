package com.finaxys.slackbot.BUL.Classes;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.FinaxysProfileService;

/**
 * Created by Bannou on 21/03/2017.
 */
public class FinaxysProfileServiceImpl implements FinaxysProfileService {
    @Override
    public void onChannelLeaveMessage(JsonNode jsonNode) {
        System.out.print(jsonNode.toString());
        JsonNode user = jsonNode.get("user");

    }
}
