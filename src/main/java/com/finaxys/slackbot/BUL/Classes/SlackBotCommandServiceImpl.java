package com.finaxys.slackbot.BUL.Classes;

import com.finaxys.slackbot.BUL.Interfaces.SlackBotCommandService;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class SlackBotCommandServiceImpl implements SlackBotCommandService {
    @Autowired
    private Repository<SlackUser, String> finaxysProfileRepository;

    @Override
    @Transactional
    public List<SlackUser> listerUsers() {

        return finaxysProfileRepository.getAll();
    }

    @Override
    @Transactional
    public List<SlackUser> listeScores(int profilesCount) {
    	System.out.println("listeScores!");
        
        List<SlackUser> finaxysProfiles = finaxysProfileRepository.getAllOrderedByAsList("score", false, profilesCount);

        return finaxysProfiles;
    }
}
