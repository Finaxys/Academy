package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.BUL.Matchers.ChallengeManagerArgumentsMatcher;
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
@RequestMapping("/challenge")
public class ChallengeManagerWebService {

    @Autowired
    Repository<FinaxysProfile, String> finaxysProfileRepository;
    @Autowired
    Repository<Role, Integer> roleRepository;
    @Autowired
    Repository<Challenge, Integer> challengeRepository;


    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/challenge_manager/new", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> create(@RequestParam("appVerificationToken") String appVerificationToken,
                                           @RequestParam("slackTeam") String slackTeam,
                                           @RequestParam("user_id") String adminFinaxysProfileId,
                                           @RequestParam("text") String arguments) {
        Log.info("/fxmanager_add  ");

        if (!appVerificationToken.equals(Settings.appVerificationToken)) {

            Message message = new Message("Wrong verification token !" + Settings.appVerificationToken);
            Log.info(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        if (!Settings.slackTeam.equals(slackTeam)) {
            Message message = new Message("Only for FinaxysTM members !");
            Log.info(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ChallengeManagerArgumentsMatcher challengeManagerArgumentsMatcher = new ChallengeManagerArgumentsMatcher();
        if (!challengeManagerArgumentsMatcher.isCorrect(arguments)) {
            Message message = new Message("/fxmanager_add  :" + arguments + "\n " + "Arguments should be :[challenge name] @Username");
            Log.info(message.getText().toString());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        String finaxysProfileId = challengeManagerArgumentsMatcher.getUserIdArgument(arguments);
        String finaxysProfileName = challengeManagerArgumentsMatcher.getUserNameArgument(arguments);
        String challengeName = challengeManagerArgumentsMatcher.getChallengeName(arguments);
        if (userIsChallengeManager(adminFinaxysProfileId, challengeName) || userIsAdministrator(adminFinaxysProfileId)) {
            FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(finaxysProfileId);
            List<Challenge> challenges = challengeRepository.getByCriterion("name", challengeName);
            finaxysProfile = (finaxysProfile == null) ? new FinaxysProfile(finaxysProfileId, finaxysProfileName) : finaxysProfile;
            finaxysProfileRepository.saveOrUpdate(finaxysProfile);
            if (challenges.size() == 0) {
                Message message = new Message("/fxmanager_add  :" + arguments + "\n " + "challenge does not exist");
                return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
            } else {

                Role role = new Role();
                role.setRole("challenge_manager");
                role.setChallengeId(challenges.get(0).getId());
                role.setFinaxysProfile(finaxysProfile);
                roleRepository.saveOrUpdate(role);
                Message message = new Message("/fxmanager_add  : " + arguments + "\n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfile.getId()).getName() + "> has just became a challenge manager!");
                Log.info(message.getText().toString());
                return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
            }
        }
        Message message = new Message("/fxmanager_add  : " + arguments + " you are not a challenge manager!");
        Log.info(message.getText());
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/challenge_manager/remove", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> unsetFinaxysProfileAsChallengeManager(@RequestParam("appVerificationToken") String appVerificationToken,
                                                                          @RequestParam("slackTeam") String slackTeam,
                                                                          @RequestParam("user_id") String adminFinaxysProfileId,
                                                                          @RequestParam("text") String arguments) {
        Log.info("/fxmanager_del");
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
        ChallengeManagerArgumentsMatcher challengeManagerArgumentsMatcher = new ChallengeManagerArgumentsMatcher();
        if (!challengeManagerArgumentsMatcher.isCorrect(arguments)) {
            Message message = new Message("/fxmanager_del :" + arguments + "\n " + "Arguments should be :[challenge name] @Username");
            Log.info(message.getText().toString());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }


        String finaxysProfileId = challengeManagerArgumentsMatcher.getUserIdArgument(arguments);
        String finaxysProfileName = challengeManagerArgumentsMatcher.getUserNameArgument(arguments);
        String challengeName = challengeManagerArgumentsMatcher.getChallengeName(arguments);
        List<Challenge> challenges = challengeRepository.getByCriterion("name", challengeName);
        if (userIsChallengeManager(adminFinaxysProfileId, challengeName) || userIsAdministrator(adminFinaxysProfileId)) {
            if (challenges.size() == 0) {
                Message message = new Message("challenge doesn't exist");
                Log.info(message.getText());
                return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
            } else {
                Challenge challenge = challenges.get(0);
                List<Role> roles = roleRepository.getByCriterion("challengeId", challenge.getId());
                for (Role role : roles) {
                    if (role.getFinaxysProfile().getId().equals(finaxysProfileId)) {
                        roleRepository.delete(role);
                        Message message = new Message("/fxmanager_del : " + arguments + "\n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is no more a challenge manager!");
                        Log.info(message.getText());
                        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
                    }


                }
                Message message = new Message("/fxmanager_del : " + arguments + "\n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is already not a challenge manager!");
                Log.info(message.getText());
                return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
            }
        }
        Message message = new Message("/fxmanager_del : " + arguments + "\n " + "You are neither an admin nor a challenge manager");
        Log.info(message.getText());
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);


    }

    @RequestMapping(value = "/challenge_manager/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getChallengeManagers(@RequestParam("appVerificationToken") String appVerificationToken,
                                                         @RequestParam("slackTeam") String slackTeam,
                                                         @RequestParam("text") String arguments) {
        Log.info("/fx_challenge_list");

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

        String challengeName = arguments.trim();
        List<Challenge> challenges = challengeRepository.getByCriterion("name", challengeName);
        if (challenges.size() == 0) {
            Message message = new Message("Challenge nonexistent");
            Log.info(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        List<Role> roles = roleRepository.getByCriterion("challengeId", challenges.get(0).getId());
        String messageText = "List of Challenge managers list:\n";
        for (Role role : roles) {
            messageText += "<@" + role.getFinaxysProfile().getId() + "|" + SlackBot.getSlackWebApiClient().getUserInfo(role.getFinaxysProfile().getId()).getName() + "> \n";
            Message message = new Message("/fx_challenge_list " + "\n " + messageText);
            Log.info(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        Message message = new Message("/fx_challenge_list :" + "\n " + messageText+" No challenge managers are found");
        Log.info(message.getText());
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

    public boolean userIsAdministrator(String adminFinaxysProfileId) {
        List<Role> roles = roleRepository.getByCriterion("role", "admin");
        for (Role role : roles) {
            if (role.getFinaxysProfile().getId().equals(adminFinaxysProfileId)) {
                return true;
            }
        }
        return false;
    }
}
