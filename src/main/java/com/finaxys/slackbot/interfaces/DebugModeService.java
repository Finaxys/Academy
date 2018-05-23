package com.finaxys.slackbot.interfaces;

import com.finaxys.slackbot.DAL.DebugMode;

public interface DebugModeService {
	
		public DebugMode get(int id);
		public DebugMode save(DebugMode debugMode);
		public String enableDebugMode();		
		public String disableDebugMode();
		public boolean isOnDebugMode();
		

}
