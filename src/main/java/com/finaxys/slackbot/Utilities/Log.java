package com.finaxys.slackbot.Utilities;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.stereotype.Component;

import com.finaxys.slackbot.DAL.LogEvent;
import com.finaxys.slackbot.DAL.Repository;

@Component
public class Log extends AppenderSkeleton {

	public static Logger logger = Logger.getLogger(Log.class);

	private static Repository<LogEvent, Integer> logRepository;

	public static void error(String message) {
		SlackBot.postMessageToDebugChannelAsync(message);
		logger.error(message);
	}

	public static void fatal(String message) {
		SlackBot.postMessageToDebugChannelAsync(message);
		logger.fatal(message);
	}

	public static void info(String message) {
		SlackBot.postMessageToDebugChannelAsync(message);
		logger.info(message);
	}

	public static void logChannelTribeCreated(String name, String tribuChannel) {
		info(name + " created " + tribuChannel);
	}

	public static void logChannelTribeJoined(String name, String tribuChannel) {
		info(name + " joined " + tribuChannel);
	}

	public static void logPostedFile(String name, String channelName) {
		info(name + "posted a File on " + channelName);
	}

	public static void logReactionAdded(String userName, String itemUser) {
		info(userName + " posted an Emoji on  " + itemUser + "'s" + " message");
	}

	public static void logReactionRemoved(String userName, String itemUser) {
		info(userName + " removed an Emoji on  " + itemUser + "'s" + " message");
	}

	public static void logMemberLeftChannel(String userName, String channelName) {
		info(userName + " left " + channelName);
	}

	@Override
	public void close() {
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	public void append(LoggingEvent event) {
		if (logRepository != null && !event.getLoggerName().toLowerCase().contains("hibernate")) {
			LogEvent le = new LogEvent(event);
			logRepository.saveOrUpdate(le);
		}
	}

	public static void setLogRepository(Repository<LogEvent, Integer> logRepository) {
		Log.logRepository = logRepository;
	}
}
