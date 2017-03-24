package com.finaxys.slackbot.Utilities;

import allbegray.slack.webapi.SlackWebApiClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FinaxysSlackBotLogger {

    public static Logger logger = Logger.getLogger(FinaxysSlackBotLogger.class);

    public static void logChannelCreated(String name,String channelName) {
        String msg = name+" created "+ channelName;
        logger.fatal(msg);
        postDebugMessageToDebugChannel(msg);
    }

    public static void logChannelTribuCreated(String name , String tribuChannel) {

        String msg = name+" created "+tribuChannel;
        logger.fatal(msg);
        postDebugMessageToDebugChannel(msg);
    }
    public static void logChannelTributeJoined(String name,String tribuChannel) {

        String msg = name+" joined "+tribuChannel;
        logger.fatal(msg);
        postDebugMessageToDebugChannel(msg);
    }
    public static void logPostedFile(String name,String channelName) {
        String msg = name+"posted a File on "+channelName;
        logger.fatal(msg);
        postDebugMessageToDebugChannel(msg);
    }

    public static void logReactionAdded(String userName,String itemUser) {
        String msg =userName+" posted an Emoji on  "+itemUser +"'s"+" message";
        logger.fatal(msg);
        postDebugMessageToDebugChannel(msg);
    }

    public static void logReactionRemoved(String userName,String itemUser) {
        String msg =userName +" removed an Emoji on  "+itemUser +"'s"+" message";
        logger.fatal(msg);
        postDebugMessageToDebugChannel(msg);
    }
    public static void logMemberLeftChannel(String userName,String channelName) {
        String msg =userName +" left "+channelName;
        logger.fatal(msg);
        postDebugMessageToDebugChannel(msg);
    }
    public static void logCommandRequest(String command) {
        logger.fatal(command);
        postDebugMessageToDebugChannel(command);
    }
    public static void logCommandResponse(String command) {
        logger.fatal(command);
        postDebugMessageToDebugChannel(command);
    }

    public static void postDebugMessageToDebugChannel(String message) {
        SlackWebApiClient slackWebApi = SlackBot.getSlackWebApiClient();
        new Thread(() -> {
            slackWebApi.postMessage(Settings.debugChannelId, message);
        }).start();
    }
}


