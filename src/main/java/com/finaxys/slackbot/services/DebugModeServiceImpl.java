package com.finaxys.slackbot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.DAL.DebugMode;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.interfaces.DebugModeService;

@Service
public class DebugModeServiceImpl implements DebugModeService{
	@Autowired
	Repository<DebugMode, Integer> debugmodeRepository;

	@Override
	public DebugMode get(int id) {
		return debugmodeRepository.findById(id);
	}

	@Override
	public DebugMode save(DebugMode debugMode) {
		debugmodeRepository.saveOrUpdate(debugMode);
		return debugMode;
	}

}
