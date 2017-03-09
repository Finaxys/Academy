package test;
import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.Event;
import allbegray.slack.rtm.EventListener;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.type.Authentication;
import allbegray.slack.type.Channel;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;


public class main {

   private final static String ACCESS_TOKEN = "xoxb-147362574370-6doJjktVe3cReptdmyHKIQxV";

   public static void main(String[] args) {
     SlackWebApiClient webApiClient = SlackClientFactory.createWebApiClient(ACCESS_TOKEN);
     SlackRealTimeMessagingClient slackRealTimeMessagingClient = SlackClientFactory.createSlackRealTimeMessagingClient(ACCESS_TOKEN);
     slackRealTimeMessagingClient.addListener(Event.REACTION_ADDED, new MessageAppreciatedListner());
     slackRealTimeMessagingClient.connect();
     
   }
}