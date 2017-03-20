package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.ChannelLeftService;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.BUL.Interfaces.NewTribeJoinedService;
import com.finaxys.slackbot.BUL.Interfaces.RealMessageReward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by inesnefoussi on 3/7/17.
 */
@Component
public class MessageListener implements EventListener {

    @Autowired
    private NewTribeJoinedService newTribeJoinedService;

    @Autowired
    private RealMessageReward realMessageReward;

    @Autowired
    private InnovateService innovateService;

    @Autowired
    private ChannelLeftService channelLeftService;

    public MessageListener() {
    }

    public void handleMessage(JsonNode jsonNode) {
        newTribeJoinedService.onNewTributeJoined(jsonNode);
        realMessageReward.rewardReadlMessage(jsonNode);
        innovateService.addInnovateScore(jsonNode, this);
        channelLeftService.onChannelLeaveMessage(jsonNode);
    }
}
