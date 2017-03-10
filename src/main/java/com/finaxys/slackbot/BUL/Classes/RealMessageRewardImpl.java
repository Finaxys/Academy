package com.finaxys.slackbot.BUL.Classes;

import allbegray.slack.type.Channel;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.RealMessageReward;
import com.finaxys.slackbot.BUL.Matchers.RealMessageMatcher;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Utilities.WebApiFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Bannou on 08/03/2017.
 */
@Service
public class RealMessageRewardImpl implements RealMessageReward {

    @Autowired
    private Repository<FinaxysProfile, String> finaxysProfileRepository;

    @Override
    @Transactional
    public void rewardReadlMessage(JsonNode jsonNode) {
        String channelId = jsonNode.get("channel").asText();
        Channel channel = getChannelById(channelId);
        TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher();
        if (tribeChannelMatcher.isNotTribe(channel.getName())) return;

        RealMessageMatcher realMessageMatcher = new RealMessageMatcher();
        String message = jsonNode.get("text").asText();
        if (realMessageMatcher.isRealMessage(message)) {
            String userId = jsonNode.get("user").asText();
            increaseSlackUserScore(userId, SCORE_GRID.SENT_A_REAL_MESSAGE);
        }
    }

    private void increaseSlackUserScore(String userId, SCORE_GRID sentARealMessage) {
        FinaxysProfile profile = finaxysProfileRepository.findById(userId);
        profile = (profile == null) ? new FinaxysProfile(userId) : profile;
        profile.incrementScore(sentARealMessage.value());
        finaxysProfileRepository.saveOrUpdate(profile);
    }

    private Channel getChannelById(String channelId) {
        return WebApiFactory.getSlackWebApiClient().getChannelInfo(channelId);
    }
}
