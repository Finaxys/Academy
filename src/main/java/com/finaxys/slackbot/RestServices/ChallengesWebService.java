package com.finaxys.slackbot.RestServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.BUL.Matchers.ChallengeTypeMatcher;
import com.finaxys.slackbot.BUL.Matchers.CreateChallengeMatcher;
import com.finaxys.slackbot.BUL.Matchers.DateMatcher;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.Challenge;
import com.finaxys.slackbot.Domains.Message;
import com.finaxys.slackbot.Utilities.PropertyLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by inesnefoussi on 3/14/17.
 */
@RestController
@RequestMapping("/challenges")
public class ChallengesWebService {

    @Autowired
    private Repository<Challenge, Integer> challengeRepository;

    @Autowired
    private PropertyLoader propertyLoader;

    private ObjectMapper objectMapper;

    public ChallengesWebService() {
        objectMapper = new ObjectMapper();
    }

    private boolean tokenIsValid(String token) {
        return token.equals(propertyLoader.loadSlackBotProperties().getProperty("verification_token"));
    }

    private boolean teamIdIsValid(String teamDomain) {
        return teamDomain.equals(propertyLoader.loadSlackBotProperties().getProperty("finaxys_team_name"));
    }

    private ResponseEntity<JsonNode> showMessage(String token, String teamDomain, Message message) {
        if (!tokenIsValid(token)) {
            message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        if (!teamIdIsValid(teamDomain)) {
            message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    private boolean requestParametersAreValid(String... parameters) {
        for (String parameter : parameters) {
            if (parameter == null || parameter.isEmpty())
                return false;
        }
        return true;
    }

    private String getStringFromList(List<Challenge> challenges) {
        String result = "";
        for (Challenge challenge : challenges) {
            result += "Challenge name: "+challenge.getName()+" , number of participants: "+challenge.getParticipants().size()+"  \n ";
        }
        return result;
    }

    @RequestMapping(value = "/type", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getChallengesByType(@RequestParam("text") String text,
                                                        @RequestParam("token") String token,
                                                        @RequestParam("team_domain") String teamDomain) {
        Message message = new Message("");
        if (requestParametersAreValid(new String[]{text, token, teamDomain})) {
            if (tokenIsValid(token) && teamIdIsValid(teamDomain)) {
                ChallengeTypeMatcher challengeTypeMatcher = new ChallengeTypeMatcher();
                if (!challengeTypeMatcher.match(text))
                    message.setText("Sorry! Wrong type format! Available types are: group , individual");
                else {
                    List<Challenge> challenges = challengeRepository.getByCriterion("type", text);
                    if (challenges.isEmpty())
                        message.setText("There are no challenges having the type " + text);
                    else {
                        String result = getStringFromList(challenges);
                        return new ResponseEntity(objectMapper.convertValue(result, JsonNode.class), HttpStatus.OK);
                    }

                }
            }
        } else
            message.setText("There was a problem treating your request. Please try again.");
        return this.showMessage(token, teamDomain, message);

    }

    @RequestMapping(value = "/name", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getChallengesByName(@RequestParam("text") String text,
                                                        @RequestParam("token") String token,
                                                        @RequestParam("team_domain") String teamDomain) {
        Message message = new Message("");
        if (requestParametersAreValid(new String[]{text, token, teamDomain})) {
            if (tokenIsValid(token) && teamIdIsValid(teamDomain)) {
                List<Challenge> challenges = challengeRepository.getByCriterion("name", text);
                if (challenges.isEmpty())
                    message.setText("There are no challenges with such a name.");
                else {
                    String result = getStringFromList(challenges);
                    return new ResponseEntity(objectMapper.convertValue(result, JsonNode.class), HttpStatus.OK);
                }
            }

        } else
            message.setText("There was a problem treating your request. Please try again.");
        return this.showMessage(token, teamDomain, message);
    }

    @RequestMapping(value = "/date", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getChallengesByDate(@RequestParam("text") String text,
                                                        @RequestParam("token") String token,
                                                        @RequestParam("team_domain") String teamDomain) {
        Message message = new Message("");
        if (requestParametersAreValid(new String[]{text, token, teamDomain})) {
            if (tokenIsValid(token) && teamIdIsValid(teamDomain)) {
                DateMatcher dateMatcher = new DateMatcher();
                if (!dateMatcher.match(text.trim()))
                    message.setText("Sorry! Wrong date format! Please make sure to enter a date in this format: yyyy-MM-dd");
                else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date wantedDate = new Date();
                    try {
                        wantedDate = dateFormat.parse(text);
                    } catch (Exception e) {
                    }
                    List<Challenge> challenges = challengeRepository.getByCriterion("creationDate", wantedDate);
                    if (challenges.isEmpty())
                        message.setText("There are no challenges on this date: " + text);
                    else {
                        String result = getStringFromList(challenges);
                        return new ResponseEntity(objectMapper.convertValue(result, JsonNode.class), HttpStatus.OK);
                    }
                }
            }
        } else
            message.setText("There was a problem treating your request. Please try again.");
        return this.showMessage(token, teamDomain, message);
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getAllChallenges(@RequestParam("token") String token,
                                                     @RequestParam("team_domain") String teamDomain
    ) {
        Message message = new Message("");
        if (requestParametersAreValid(new String[]{token, teamDomain})) {
            if (tokenIsValid(token) && teamIdIsValid(teamDomain)) {
                List<Challenge> challenges = challengeRepository.getAll();
                if (challenges.isEmpty())
                    message.setText("There no previous challenges! Come on create one!");
                else {
                    String result = getStringFromList(challenges);
                    return new ResponseEntity(objectMapper.convertValue(result, JsonNode.class), HttpStatus.OK);
                }
            }
        } else
            message.setText("There was a problem treating your request. Please try again.");
        return this.showMessage(token, teamDomain, message);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> createChallenge(@RequestParam("token") String token,
                                                    @RequestParam("team_domain") String teamDomain,
                                                    @RequestParam("text") String text) {
        Message message = new Message("");
        if (requestParametersAreValid(new String[]{token, teamDomain, text})) {
            if (tokenIsValid(token) && teamIdIsValid(teamDomain)) {
                CreateChallengeMatcher createChallengeMatcher = new CreateChallengeMatcher();
                if (!createChallengeMatcher.match(text.trim()))
                    message.setText("Sorry! Wrong request format! Your request should have the following format: challengeName,group|individual,descriptionText");
                else {
                    String[] challengeInfo = text.trim().split(",");
                    Challenge challenge = new Challenge();
                    challenge.setName(challengeInfo[0]);
                    challenge.setType(challengeInfo[1]);
                    challenge.setDescription(challengeInfo[2]);
                    challengeRepository.addEntity(challenge);
                    message.setText("Challenge successfully added.");
                }
            }
        } else
            message.setText("There was a problem treating your request. Please try again.");
        return this.showMessage(token, teamDomain, message);
    }
}
