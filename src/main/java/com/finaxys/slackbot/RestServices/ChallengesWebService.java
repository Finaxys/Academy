package com.finaxys.slackbot.RestServices;

import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.Challenge;
import com.finaxys.slackbot.Domains.FinaxysProfile;
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
    public ResponseEntity<List<Challenge>> getAllChallenges() {
        List<Challenge> challenges = challengeRepository.getAll();
        return new ResponseEntity<List<Challenge>>(challenges, HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void createChallenge(@RequestParam("challengeName") String challengeName,
                                @RequestParam("description") String description,
                                @RequestParam("type") String type) {
        Challenge challenge = new Challenge();
        challenge.setName(challengeName);
        challenge.setDescription(description);
        challenge.setType(type);
        challenge.setCreationDate(new Date());

        challengeRepository.addEntity(challenge);

    }
}
