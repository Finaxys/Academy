package com.finaxys.slackbot.Utilities;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Log {

    public static Logger logger = Logger.getLogger(Log.class);

    public static void error(String message) {
        SlackBot.postMessageToDebugChannelAsync(message);
        logger.error(message);
    }
    public static void fatal(String message) {
        SlackBot.postMessageToDebugChannelAsync(message);
        logger.fatal(message);
    }
    public static void info(String message) {
    	System.out.println("eee");
        SlackBot.postMessageToDebugChannelAsync(message);
        logger.info(message);
    }

    // --------------------------------------------------------------------

    public static void logChannelTribuCreated(String name , String tribuChannel) {
    	System.out.println("eee");
        info(name+" created "+tribuChannel);
    }
    public static void logChannelTributeJoined(String name,String tribuChannel) {
    	System.out.println("eee");
        info(name+" joined "+tribuChannel);
    }
    public static void logPostedFile(String name,String channelName) {
    	System.out.println("eee");
        info(name+"posted a File on "+channelName);
    }
    public static void logReactionAdded(String userName,String itemUser) {
    	System.out.println("eee");
        info(userName+" posted an Emoji on  "+itemUser +"'s"+" message");
    }
    public static void logReactionRemoved(String userName,String itemUser) {
    	System.out.println("eee");
        info(userName +" removed an Emoji on  "+itemUser +"'s"+" message");
    }
    public static void logMemberLeftChannel(String userName,String channelName) {
    	System.out.println("eee");
        info(userName +" left "+channelName);
    }
}


