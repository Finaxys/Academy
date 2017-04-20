//package com.finaxys.slackbot.WebServices;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.finaxys.slackbot.BUL.Matchers.ContestScoreAddMatcher;
//import com.finaxys.slackbot.DAL.ContestScore;
//import com.finaxys.slackbot.DAL.SlackUser;
//import com.finaxys.slackbot.DAL.Message;
//import com.finaxys.slackbot.DAL.Repository;
//import com.finaxys.slackbot.Utilities.Log;
//import com.finaxys.slackbot.Utilities.Settings;
//import com.finaxys.slackbot.Utilities.Timer;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/contests")
//public class ContestScoreWebService extends BaseWebService {
//
//    @Autowired
//    private Repository<ContestScore, String> contestScoreRepository;
//    int score;
//    String contest;
//
//    
//    @RequestMapping(value = "/create", method = RequestMethod.POST)
//    @ResponseBody
//    public ResponseEntity<JsonNode> contestAdd(@RequestParam("token") 		String appVerificationToken,
//                                               @RequestParam("team_domain") String slackTeam,
//                                               @RequestParam("text") 		String text) {
//    	
//    	Timer timer = new Timer();
//
//        if (noAccess(appVerificationToken, slackTeam))
//            return noAccessResponseEntity(appVerificationToken, slackTeam);
//        
//        timer.capture();
//        
//        ContestScoreAddMatcher contestScoreAddMatcher = new ContestScoreAddMatcher();
//        
//        if (!contestScoreAddMatcher.isCorrect(text))
//            return newResponseEntity("/fx_contest_add " + text + " \n" + "Arguments should be : [\"contest\"] [points earned]" + timer , true);
//
//        score = contestScoreAddMatcher.getActionScoreArgument(text);
//        contest = contestScoreAddMatcher.getActionNameArgument (text);
//        
//        timer.capture();
//        
//        new Thread(new Runnable()
//        {
//        	public void run()
//        	{
//        		contestScoreRepository.saveOrUpdate(new ContestScore(contest, score));
//        	}
//        }).start();
//        
//        timer.capture();
//        
//        return newResponseEntity("/fx_contest_add " + text + " \n" + "contest added successfully " + timer, true);
//    }
//}