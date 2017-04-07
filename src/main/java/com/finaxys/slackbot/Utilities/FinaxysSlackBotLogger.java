package com.finaxys.slackbot.Utilities;

import allbegray.slack.webapi.SlackWebApiClient;
import org.apache.log4j.Logger;

public class FinaxysSlackBotLogger {

    public static Logger logger = Logger.getLogger(FinaxysSlackBotLogger.class);

    public static void logChannelCreated(String name,String channelName) {
    	System.out.println("eee");
        String msg = name+" created "+ channelName;
        logger.fatal(msg);
        postDebugMessageToDebugChannel(msg);
    }

    public static void logChannelTribuCreated(String name , String tribuChannel) {
    	System.out.println("eee");
        
        String msg = name+" created "+tribuChannel;
        logger.fatal(msg);
        postDebugMessageToDebugChannel(msg);
    }
    public static void logChannelTributeJoined(String name,String tribuChannel) {
    	System.out.println("eee");
        
        String msg = name+" joined "+tribuChannel;
        logger.fatal(msg);
        postDebugMessageToDebugChannel(msg);
    }
    public static void logPostedFile(String name) {
    	System.out.println("eee");
        String msg = name+" posted a File ";
        logger.fatal(msg);
        postDebugMessageToDebugChannel(msg);
    }

    public static void logReactionAdded(String userName,String itemUser) {
    	System.out.println("eee");
        String msg =userName+" posted an Emoji on  "+itemUser +"'s"+" message";
        logger.fatal(msg);
        postDebugMessageToDebugChannel(msg);
    }

    public static void logReactionRemoved(String userName,String itemUser) {
    	System.out.println("eee");
        String msg =userName +" removed an Emoji on  "+itemUser +"'s"+" message";
        logger.fatal(msg);
        postDebugMessageToDebugChannel(msg);
    }
    public static void logMemberLeftChannel(String userName,String channelName) {
    	System.out.println("eee");
        String msg =userName +" left "+channelName;
        logger.fatal(msg);
        postDebugMessageToDebugChannel(msg);
    }

    public static void postDebugMessageToDebugChannel(String message) {
    	System.out.println("eee");
        String debugChannel = "C4G0TER6C";//TODO PropertyLoader.loadSlackBotProperties().getProperty("debugChannel");
        SlackWebApiClient slackWebApi = SlackBot.getSlackWebApiClient();
        new Thread(() -> {
            slackWebApi.postMessage(debugChannel, message);
        }).start();
    }
}
