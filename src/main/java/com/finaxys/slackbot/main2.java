package com.finaxys.slackbot;


import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.Event;

import allbegray.slack.rtm.SlackRealTimeMessagingClient;

import allbegray.slack.webapi.SlackWebApiClient;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.finaxys.slackbot.BusinessLogic.Listeners.*;
import com.finaxys.slackbot.Configuration.Classes.SpringContext;

public class main2 {

   private final static String ACCESS_TOKEN = "xoxb-147362574370-6doJjktVe3cReptdmyHKIQxV";
  static  AbstractApplicationContext context = new AnnotationConfigApplicationContext(SpringContext.class);
   
   public static void main(String[] args) {
	   
        SlackWebApiClient webApiClient = SlackClientFactory.createWebApiClient(ACCESS_TOKEN);
       
        PostedFileListener postedFileListener =(PostedFileListener)context.getBean("postedFileListener");
        postedFileListener.setSlackWebApiClient(webApiClient);
        
     SlackRealTimeMessagingClient slackRealTimeMessagingClient = SlackClientFactory.createSlackRealTimeMessagingClient(ACCESS_TOKEN);
        slackRealTimeMessagingClient.addListener(Event.MESSAGE,postedFileListener );
        
        slackRealTimeMessagingClient.connect();
    }
}