package com.finaxys.slackbot.RestServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.Challenge;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Domains.Message;
import com.finaxys.slackbot.Utilities.PropertyLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private Repository<FinaxysProfile, String> finaxysProfileRepository;

    private ObjectMapper objectMapper;

    public ChallengesWebService() {
        objectMapper = new ObjectMapper();
    }

    private boolean tokenIsValid(String token) {
        return token.equals(PropertyLoader.loadSlackBotProperties().getProperty("verification_token"));
    }

    private boolean teamIdIsValid(String teamDomain) {
        return teamDomain.equals(PropertyLoader.loadSlackBotProperties().getProperty("finaxys_team_name"));
    }

    @RequestMapping(value = "/type", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<Challenge>> getChallengesByType(@RequestParam("type") String type) {
        List<Challenge> challenges = challengeRepository.getByCriterion("type", type);
        return new ResponseEntity<List<Challenge>>(challenges, HttpStatus.OK);
    }

    @RequestMapping(value = "/name", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<Challenge>> getChallengesByName(@RequestParam("name") String name) {
        List<Challenge> challenges = challengeRepository.getByCriterion("name", name);
        return new ResponseEntity<List<Challenge>>(challenges, HttpStatus.OK);
    }

    @RequestMapping(value = "/date", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<Challenge>> getChallengesByDate(@RequestParam("date") String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dates = new ArrayList<>();
        Date wantedDate = new Date();
        try {
            wantedDate = dateFormat.parse(date);
            dates.add(wantedDate.toString());
        } catch (Exception e) {
            dates.add(e.toString());
        }
        List<Challenge> challenges = challengeRepository.getByCriterion("creationDate", wantedDate);
        return new ResponseEntity<List<Challenge>>(challenges, HttpStatus.OK);
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getAllChallenges(@RequestParam("token") String token,
                                                            @RequestParam("team_domain") String teamDomain
                                                            ) {

        if (tokenIsValid(token) && teamIdIsValid(teamDomain)) {
            List<Challenge> challenges = challengeRepository.getAll();
            if (challenges.isEmpty()) {
                Message message = new Message("There no previous challenges! Come on create one!");
                return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
            }
            return new ResponseEntity(objectMapper.convertValue(challenges, JsonNode.class), HttpStatus.OK);
        }
        else if (!tokenIsValid(token)) {
            Message message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        else {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

    }
}
