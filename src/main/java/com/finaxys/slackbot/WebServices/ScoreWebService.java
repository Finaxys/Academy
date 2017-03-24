package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.BUL.Matchers.ChallengeScoreArgumentsMatcher;
import com.finaxys.slackbot.BUL.Matchers.OneUsernameArgumentMatcher;
import com.finaxys.slackbot.DAL.*;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.Settings;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/score")
public class ScoreWebService {
    @Autowired
    Repository<FinaxysProfile, String> finaxysProfileRepository;

    @Autowired
    Repository<Challenge, Integer> challengeRepository;

    @Autowired
    Repository<FinaxysProfile_Challenge, FinaxysProfile_Challenge_PK> finaxysProfileChallengeRepository;

    @Autowired
    Repository<Role, Integer> roleRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> addScoreForChallenge(@RequestParam("appVerificationToken") String appVerificationToken,
                                                         @RequestParam("slackTeam") String slackTeam,
                                                         @RequestParam("user_id") String challengeManagerFinaxysProfileId,
                                                         @RequestParam("text") String arguments) {
        Log.info("/fxmanager_challenge_score_add "+arguments);
        if (!Settings.appVerificationToken.equals(appVerificationToken)) {

            Message message = new Message("Wrong verification token !" + Settings.appVerificationToken);
            Log.info(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        if (!Settings.slackTeam.equals(slackTeam)) {
            Message message = new Message("Only for FinaxysTM members !");
            Log.info(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }



        ChallengeScoreArgumentsMatcher challengeScoreArgumentsMatcher = new ChallengeScoreArgumentsMatcher();

        if (!challengeScoreArgumentsMatcher.isCorrect(arguments)) {
            Message message = new Message("fxmanager_challenge_score_add "+arguments+" \n "+"Arguments should suit ' .... @Username ... 20 ..... <challengeName> challenge ..' Pattern !");
            Log.info(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        String extractFinaxysProfileId = challengeScoreArgumentsMatcher.getFinaxysProfileId(arguments);
        String challengeName = challengeScoreArgumentsMatcher.getChallengeName(arguments);
        Challenge challenge = challengeRepository.getByCriterion("name", challengeName).get(0);
        if (!userIsChallengeManager(challengeManagerFinaxysProfileId,challengeName)) {
            Message message = new Message("fxmanager_challenge_score_add "+arguments+"You are neither admin nor a challenge manager !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        int score = Integer.parseInt(challengeScoreArgumentsMatcher.getScore(arguments));
        FinaxysProfile_Challenge finaxysProfile_challenge = new FinaxysProfile_Challenge(score, challenge.getId(), extractFinaxysProfileId);
        finaxysProfile_challenge.setFinaxysProfile(finaxysProfileRepository.findById(extractFinaxysProfileId));
        finaxysProfile_challenge.setChallenge(challenge);
        try {
            finaxysProfileChallengeRepository.saveOrUpdate(finaxysProfile_challenge);

        }catch (Exception e){
            Message message = new Message("/fx_add_score "+arguments+" \n"+"A problem has occured! The user may have a score for this challenge already !");
            Log.info(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        Message message = new Message("/fx_add_score "+arguments+" \n"+"Score has been added !");
        Log.info(message.getText());
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> listScoreForChallenge(@RequestParam("appVerificationToken") String appVerificationToken,
                                                          @RequestParam("slackTeam") String slackTeam,
                                                          @RequestParam("text") String arguments) {
        Log.info("fx_display_challenge_scores");
        if (!Settings.appVerificationToken.equals(appVerificationToken)) {

            Message message = new Message("Wrong verification token !" + Settings.appVerificationToken);
            Log.info(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        if (!Settings.slackTeam.equals(slackTeam)) {
            Message message = new Message("Only for FinaxysTM members !");
            Log.info(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        } ;

        ChallengeScoreArgumentsMatcher challengeScoreArgumentsMatcher = new ChallengeScoreArgumentsMatcher();

        if (!challengeScoreArgumentsMatcher.isCorrectListRequest(arguments)) {
            Message message = new Message("fx_display_challenge_scores was invoked with args "+arguments+" \n"+"Arguments should suit ' ..... <challengeName> challenge ....' Pattern !");
            Log.info(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        String challengeName = challengeScoreArgumentsMatcher.getChallengeName(arguments);
        List<Challenge> challenges = challengeRepository.getByCriterion("name", challengeName);
        if (challenges.size() == 0) {
            Message message = new Message("/fx_display_challenge_scores was invoked with args "+arguments+" \n"+"No such challenge ! Check the challenge name");
            Log.info(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        Challenge challenge = challenges.get(0);
        List<FinaxysProfile_Challenge> finaxysProfileChallenges = finaxysProfileChallengeRepository.getByCriterion("challenge", challenge);

        if (finaxysProfileChallenges.size() == 0) {
            Message message = new Message("/fx_display_challenge_scores was invoked with args "+arguments+" \n"+"No score has been saved till the moment !");
            Log.info(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        String textMessage = "List of scores of " + challenge.getName() + " :"+" \n ";
        for (FinaxysProfile_Challenge finaxysProfileChallenge : finaxysProfileChallenges) {
            FinaxysProfile finaxysProfile = finaxysProfileChallenge.getFinaxysProfile();
            textMessage += "<@" + finaxysProfile.getId() + "|" + finaxysProfile.getName() + "> "+finaxysProfileChallenge.getScore()+" \n";
        }

        Log.info(textMessage);
        return new ResponseEntity(objectMapper.convertValue("fx_display_challenge_scores was invoked with args "+arguments+" \n"+textMessage, JsonNode.class), HttpStatus.OK);
    }





    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> setFinaxysProfileAsAdministrator(@RequestParam("appVerificationToken") String appVerificationToken,
                                                                     @RequestParam("slackTeam") String slackTeam,
                                                                     @RequestParam("text") String arguments) {
        Log.info("/fx_score_list " + arguments);
        if (!Settings.appVerificationToken.equals(appVerificationToken)) {
            Message message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        if (!Settings.slackTeam.equals(slackTeam)) {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();
        if (!oneUsernameArgumentsMatcher.isCorrect(arguments)) {
            Message message = new Message("/fx_score_list : " + arguments + " \n " + "Arguments should be :@Username");
            Log.info("/fx_score_list :" + arguments + " \n " + "Arguments should be : @Username");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }


        String finaxysProfileId = oneUsernameArgumentsMatcher.getUserIdArgument(arguments);
        FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(finaxysProfileId);
        Message message = new Message("<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> score :"+finaxysProfile.getScore() );
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);

    }


    public boolean userIsChallengeManager(String adminFinaxysProfileId, String challengeName) {
        List<Role> roles = roleRepository.getByCriterion("role", "challenge_manager");
        int challengeId = challengeRepository.getByCriterion("name", challengeName).get(0).getId();
        for (Role role : roles) {
            if (role.getFinaxysProfile().getId().equals(adminFinaxysProfileId) && role.getChallengeId() == challengeId) {
                return true;
            }
        }
        return false;
    }



}
