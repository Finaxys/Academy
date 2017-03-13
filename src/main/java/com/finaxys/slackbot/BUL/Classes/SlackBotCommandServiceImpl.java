package com.finaxys.slackbot.BUL.Classes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.finaxys.slackbot.BUL.Interfaces.SlackBotCommandService;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;

public class SlackBotCommandServiceImpl implements SlackBotCommandService {
	 @Autowired
	    private Repository<FinaxysProfile, String> finaxysProfileRepository;

	    @Override
	    @Transactional
	public List<FinaxysProfile> listerUsers(int number) {
		
		return  finaxysProfileRepository.getSomeUsers(number);
	}

}
