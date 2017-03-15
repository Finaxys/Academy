package com.finaxys.slackbot.restServices;

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
public class ChallengeManagerWS {

    @Autowired
    private Repository<Challenge,Integer> challengeRepository;

    @Autowired
    private Repository<FinaxysProfile,String> finaxysProfileRepository;

    @RequestMapping(value = "/type/{type}" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Challenge>> getChallengesByType(@PathVariable("type") String type) {
        List<Challenge> challenges = challengeRepository.getByCriterion("type",type);
        return new ResponseEntity<List<Challenge>>(challenges, HttpStatus.OK);
    }

    @RequestMapping(value = "/name/{name}" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Challenge>> getChallengesByName(@PathVariable("name") String name) {
        List<Challenge> challenges = challengeRepository.getByCriterion("name",name);
        return new ResponseEntity<List<Challenge>>(challenges, HttpStatus.OK);
    }

    @RequestMapping(value = "/date/{date}" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Challenge>> getChallengesByDate(@PathVariable("date") String date) {
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

    @RequestMapping(value = "/listAll" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Challenge>> getAllChallenges() {
        List<Challenge> challenges = challengeRepository.getAll();
        return new ResponseEntity<List<Challenge>>(challenges, HttpStatus.OK);
    }

    @RequestMapping(value = "/create" , method = RequestMethod.POST)
    public void createChallenge(@RequestParam("challengeName") String challengeName,
                                                  @RequestParam("description") String description ,
                                                  @RequestParam("type") String type,
                                                  @RequestParam("creatorId") String creatorId) {
        Challenge challenge = new Challenge();
        challenge.setId(null
        );
        challenge.setName(challengeName);
        challenge.setDescription(description);
        challenge.setType(type);
        FinaxysProfile profile = finaxysProfileRepository.findById(creatorId);
        challenge.setCreator(profile);
        challenge.setCreationDate(new Date());
        challenge.setParticipants(new ArrayList<>());

        challengeRepository.addEntity(challenge);

    }
}
