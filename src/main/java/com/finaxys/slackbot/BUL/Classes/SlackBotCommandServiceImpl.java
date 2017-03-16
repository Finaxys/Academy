package com.finaxys.slackbot.BUL.Classes;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finaxys.slackbot.BUL.Interfaces.SlackBotCommandService;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;

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

	 List<FinaxysProfile> users=finaxysProfileRepository.getSomeUsers(n);
	 Collections.sort(users, Collections.reverseOrder());
	
	 return users;
}




}
