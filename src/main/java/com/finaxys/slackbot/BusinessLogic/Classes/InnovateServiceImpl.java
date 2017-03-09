package com.finaxys.slackbot.BusinessLogic.Classes;
import allbegray.slack.type.User;
import com.finaxys.slackbot.BusinessLogic.Interfaces.*;
import com.finaxys.slackbot.DAL.Classes.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class InnovateServiceImpl implements InnovateService {


	@Autowired
	private Repository<FinaxysProfile, String> finaxysProfileRepository;

	public Repository<FinaxysProfile, String> getFinaxysProfileManager() {
		return finaxysProfileRepository;
	}

	public void setFinaxysProfileManager(Repository<FinaxysProfile, String> finaxysProfileRepository) {
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

		
	
