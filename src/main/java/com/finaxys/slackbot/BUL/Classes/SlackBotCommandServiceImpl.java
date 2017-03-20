package com.finaxys.slackbot.BUL.Classes;

import com.finaxys.slackbot.BUL.Interfaces.SlackBotCommandService;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class SlackBotCommandServiceImpl implements SlackBotCommandService {
    @Autowired
    private Repository<FinaxysProfile, String> finaxysProfileRepository;


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

        return finaxysProfiles;
    }
}
