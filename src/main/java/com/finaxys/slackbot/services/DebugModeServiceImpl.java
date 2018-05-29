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
	
	@Override
	public String enableDebugMode() {
		DebugMode dm = this.get(1);
		this.save(dm.enableDebugMode());
		return "Debug mode has been successfully enabled !";
	}
	
	@Override
	public String disableDebugMode() {
		DebugMode dm = this.get(1);
		this.save(dm.disableDebugMode());
		return "Debug mode has been successfully disabled !";
	}

	@Override
	public boolean isOnDebugMode() {
		if (debugmodeRepository.getAll().size() == 0)
		{
			DebugMode dm = new DebugMode();
			this.save(dm);
		}
		return this.get(1).isOnDebugMode();
	}
	

}
