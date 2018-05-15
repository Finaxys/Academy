package com.finaxys.slackbot.BUL.Classes;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.DAL.Action;
import com.finaxys.slackbot.DAL.Repository;

@Service
public class ScoreService 
{
	private static Map<String, Integer> scores;
	
	@Autowired
	Repository<Action, String> scoreRepository;
	
	public ScoreService() 
	{		
		scores = new HashMap<String, Integer>();
	}

	public void init() 
	{		
		for (Action es : scoreRepository.getAll()) 
		{
			scores.put(es.getDescription(), es.getPoints());
		}
	}

	
	public int getScore(String action) 
	{
		if (scores.isEmpty()) 
		{
			init();
		}
		return scores.get(action);
	}
}
