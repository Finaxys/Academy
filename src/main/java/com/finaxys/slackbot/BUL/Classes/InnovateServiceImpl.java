package com.finaxys.slackbot.BUL.Classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;

import allbegray.slack.type.User;

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
		FinaxysProfile userProfile = finaxysProfileRepository.findById(u.getId());
		if (userProfile == null) {
			userProfile = new FinaxysProfile();
			userProfile.setId(u.getId());
			userProfile.setName(u.getName());
			finaxysProfileRepository.addEntity(userProfile);

		}
		userProfile.setScore(userProfile.getScore() + SCORE_GRID.WAS_INNOVATIVE.value());
		finaxysProfileRepository.updateEntity(userProfile);

	}

}
