package com.finaxys.slackbot.BusinessLogic.Classes;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.type.Channel;
import allbegray.slack.type.User;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BusinessLogic.Interfaces.*;
import com.finaxys.slackbot.DAL.Interfaces.GenericRepository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;



@Service
public class InnovateServiceImpl implements InnovateService {


	@Autowired
	private GenericRepository<FinaxysProfile, String> finaxysProfileRepository;

	public GenericRepository<FinaxysProfile, String> getFinaxysProfileManager() {
		return finaxysProfileRepository;
	}

	public void setFinaxysProfileManager(GenericRepository<FinaxysProfile, String> finaxysProfileRepository) {
		this.finaxysProfileRepository = finaxysProfileRepository;
	}

	
	public void addInnovateScore(User u) {
		FinaxysProfile userProfile=finaxysProfileRepository.findById(u.getId());
		      if(userProfile== null)
		      {
		    	  userProfile = new FinaxysProfile();
		    	  userProfile.setId(u.getId());
		    	  userProfile.setName(u.getName());
					finaxysProfileRepository.addEntity(userProfile);
					
						
		      }
		        userProfile.setScore(userProfile.getScore() + 10);
				finaxysProfileRepository.updateEntity(userProfile);
					
				}
			
			
			
			
	}

		
	
