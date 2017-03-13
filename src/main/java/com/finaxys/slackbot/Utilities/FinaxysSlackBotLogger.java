package com.finaxys.slackbot.Utilities;

import org.apache.log4j.Logger;

import allbegray.slack.webapi.SlackWebApiClient;
/**
 * Created by inesnefoussi on 3/9/17.
 */
public class FinaxysSlackBotLogger {

    public static Logger logger = Logger.getLogger(FinaxysSlackBotLogger.class);
    
    public static void logChannelCreated() {
    	String msg = "Channel created";
    	logger.fatal(msg);
    	postDebugMessageToDebugChannel(msg);
    }
    public static void logChannelTributeCreated() {
    	
    	String msg ="ChannelTributeCreated";
    	logger.fatal(msg);
    	postDebugMessageToDebugChannel(msg);
    }
    
    public static void logPostedFile() {
    	String msg ="A File is posted";
    	logger.fatal(msg);
    	postDebugMessageToDebugChannel(msg);
    }
    public static void logReactionAdded() {
    	String msg ="An Emoji is posted";
    	logger.fatal(msg);
    	postDebugMessageToDebugChannel(msg);
    }
    public static void logReactionRemoved() {
    	String msg = "An Emoji is Removed" ;
    	logger.fatal(msg);
    	postDebugMessageToDebugChannel(msg);
    }
    
    public static void postDebugMessageToDebugChannel(String message)
    {
    	String debugChannel = PropertyLoader.loadSlackBotProperties().getProperty("debugChannel");
    	SlackWebApiClient slackWebApi = SlackBot.getSlackWebApiClient();
    	slackWebApi.postMessage(debugChannel, message);
    }

    public static void logChannelTributeLeft() {

        String msg ="ChannelTributeLeft";
        System.out.println(msg);
        //logger.info(msg);
        //postDebugMessageToDebugChannel(msg);
    }
    }
    


