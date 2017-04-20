package com.finaxys.slackbot.Utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.finaxys.slackbot.DAL.LogEvent;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.interfaces.ParameterService;

@PropertySource(value = "file:${catalina.home}/credentials.properties")
@Component
public class Settings {

	private static Environment environment;

	@Autowired
	public void setEnvironment(Environment environment) {
		Settings.environment = environment;
		load();
	}

	private static Repository<LogEvent, Integer> logRepository;

	@Autowired
	public void setLogRepository(Repository<LogEvent, Integer> logRepository) {
		Settings.logRepository = logRepository;
		Log.setLogRepository(logRepository);
	}

	private static ParameterService parameterService;

	@Autowired
	public void setParameterService(ParameterService parameterService) {
		System.out.println("parameterService" + parameterService);
		this.parameterService = parameterService;
		System.out.println(getParameter("param01"));
	}

	
	
	public static String appVerificationToken;
	public static String botUserOauthAccessToken;
	public static String slackTeam;
	public static String slackTeamId;
	public static String debugChannelId;

	private static void load() {
		appVerificationToken = environment.getRequiredProperty("appVerificationToken");
		botUserOauthAccessToken = environment.getRequiredProperty("botUserOauthAccessToken");
		slackTeam = environment.getRequiredProperty("slackTeam");
		slackTeamId = environment.getRequiredProperty("slackTeamId");
		debugChannelId = environment.getRequiredProperty("debugChannelId");
	}
	
	
	public static String getParameter(String parameterName){
		return parameterService.get(parameterName).getValue();
	}
	
}
