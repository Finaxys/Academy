package com.finaxys.slackbot.RestServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.BUL.Matchers.ChallengeScoreArgumentsMatcher;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.*;
import com.finaxys.slackbot.Utilities.PropertyLoader;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Bannou on 16/03/2017.
 */
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
    PropertyLoader propertyLoader;

    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> addScoreForChallenge(@RequestParam("token") String token,
                                                         @RequestParam("team_domain") String teamId,
                                                         @RequestParam("user_id") String challengeManagerFinaxysProfileId,
                                                         @RequestParam("text") String arguments) {
        if (propertiesAreNotEqual("verification_token", token)) {
            Message message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        if (propertiesAreNotEqual("finaxys_team_name", teamId)) {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        if (userIsNotChallengeManager(challengeManagerFinaxysProfileId)) {
            Message message = new Message("You don't have challenge manager authorization !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        ChallengeScoreArgumentsMatcher challengeScoreArgumentsMatcher = new ChallengeScoreArgumentsMatcher();

        if (!challengeScoreArgumentsMatcher.isCorrect(arguments)) {
            Message message = new Message("Arguments should suit ' .... @Username ... 20 ..... <challengeName> challenge ..' Pattern !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        String extractFinaxysProfileId = challengeScoreArgumentsMatcher.getFinaxysProfileId(arguments);
        String challengeName = challengeScoreArgumentsMatcher.getChallengeName(arguments);
        Challenge challenge = challengeRepository.getByCriterion("name", challengeName).get(0);

        int score = Integer.parseInt(challengeScoreArgumentsMatcher.getScore(arguments));

        FinaxysProfile_Challenge finaxysProfile_challenge = new FinaxysProfile_Challenge(score, challenge.getId(), extractFinaxysProfileId);
        if (finaxysProfileChallengeRepository.addEntity(finaxysProfile_challenge) == null) {
            Message message = new Message("A problem has occured! The user may have a score for this challengs already !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        Message message = new Message("Score has been added !");
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> listScoreForChallenge(@RequestParam("token") String token,
                                                          @RequestParam("team_domain") String teamId,
                                                          @RequestParam("text") String arguments) {
        if (propertiesAreNotEqual("verification_token", token)) {
            Message message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        if (propertiesAreNotEqual("finaxys_team_name", teamId)) {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        ChallengeScoreArgumentsMatcher challengeScoreArgumentsMatcher = new ChallengeScoreArgumentsMatcher();

        if (!challengeScoreArgumentsMatcher.isCorrectListRequest(arguments)) {
            Message message = new Message("Arguments should suit ' ..... <challengeName> challenge ....' Pattern !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        String challengeName = challengeScoreArgumentsMatcher.getChallengeName(arguments);
        List<Challenge> challenges = challengeRepository.getByCriterion("name", challengeName);
        if (challenges.size() == 0) {
            Message message = new Message("No such challenge ! Check the challenge name");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        Challenge challenge = challenges.get(0);
        List<FinaxysProfile_Challenge> finaxysProfileChallenges = finaxysProfileChallengeRepository.getByCriterion("Challenge", challenge);

        if (finaxysProfileChallenges.size() == 0) {
            Message message = new Message("No score has been saved till the moment !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        String textMessage = "List of scores of " + challenge.getName() + " : \n";
        for (FinaxysProfile_Challenge finaxysProfileChallenge : finaxysProfileChallenges) {
            FinaxysProfile finaxysProfile = finaxysProfileChallenge.getFinaxysProfile();
            textMessage += "<@" + finaxysProfile.getId() + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfile.getId()).getName() + "> \n";
        }
        return new ResponseEntity(objectMapper.convertValue(textMessage, JsonNode.class), HttpStatus.OK);
    }

    public boolean propertiesAreNotEqual(String propertyName, String propertyValue) {
        return !propertyValue.equals(propertyLoader.loadSlackBotProperties().getProperty(propertyName));
    }

    public boolean userIsNotChallengeManager(String finaxysProfileId) {
        FinaxysProfile adminFinaxysProfile = finaxysProfileRepository.findById(finaxysProfileId);
        return !adminFinaxysProfile.isAdministrator();
    }
}