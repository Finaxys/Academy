package com.finaxys.slackbot.BUL.Classes;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.webapi.SlackWebApiClient;
import com.finaxys.slackbot.BUL.Interfaces.SlackBotCommandService;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Utilities.PropertyLoader;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class SlackBotCommandServiceImpl implements SlackBotCommandService {
    @Autowired
    private Repository<FinaxysProfile, String> finaxysProfileRepository;
    @Autowired
    private  PropertyLoader propertyLoader;


    @Override
    @Transactional
    public List<FinaxysProfile> listerUsers() {

        return finaxysProfileRepository.getAll();
    }

    @Override
    @Transactional
    public List<FinaxysProfile> listeScores(int n) {
        List<FinaxysProfile> finaxysProfiles = finaxysProfileRepository.getSomeUsers(n);
        Collections.sort(finaxysProfiles, Collections.reverseOrder());

        for (FinaxysProfile finaxysProfile:finaxysProfiles) {
           SlackWebApiClient slackWebApiClient = SlackBot.getSlackWebApiClient();
            String finaxysProfileName = slackWebApiClient.getUserInfo(finaxysProfile.getId()).getName();
            finaxysProfile.setName(finaxysProfileName);
        }
        return finaxysProfiles;
    }
}
