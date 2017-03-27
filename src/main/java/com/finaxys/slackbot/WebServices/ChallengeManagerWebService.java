package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Matchers.ChallengeManagerArgumentsMatcher;
import com.finaxys.slackbot.DAL.*;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/challenge")
public class ChallengeManagerWebService extends BaseWebService{

    @Autowired
    Repository<FinaxysProfile, String> finaxysProfileRepository;
    @Autowired
    Repository<Role, Integer> roleRepository;
    @Autowired
    Repository<Challenge, Integer> challengeRepository;

    @RequestMapping(value = "/challenge_manager/new", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> create(@RequestParam("token") String appVerificationToken,
                                           @RequestParam("team_domain")  String slackTeam,
                                           @RequestParam("text") String arguments,
                                           @RequestParam("user_id") String adminFinaxysProfileId) {
        Log.info("/fx_manager_add  ");

        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);
        ChallengeManagerArgumentsMatcher challengeManagerArgumentsMatcher = new ChallengeManagerArgumentsMatcher();
        if (!challengeManagerArgumentsMatcher.isCorrect(arguments))
            return NewResponseEntity("/fx_manager_add  :" + arguments + "\n " + "Arguments should be :[challenge name] @Username",true);



        String profileId = challengeManagerArgumentsMatcher.getUserIdArgument(arguments);
        String profileName = challengeManagerArgumentsMatcher.getUserNameArgument(arguments);
        String challengeName = challengeManagerArgumentsMatcher.getChallengeName(arguments);
        if (isChallengeManager(adminFinaxysProfileId, challengeName) || isAdmin(adminFinaxysProfileId)) {
            FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(profileId);
            List<Challenge> challenges = challengeRepository.getByCriterion("name", challengeName);
            finaxysProfile = (finaxysProfile == null) ? new FinaxysProfile(profileId, profileName) : finaxysProfile;
            finaxysProfileRepository.saveOrUpdate(finaxysProfile);
            if (challenges.size() == 0)
                return NewResponseEntity("/fx_manager_add  :" + arguments + "\n " + "challenge does not exist",true);
                Role role = new Role();
                role.setRole("challenge_manager");
                role.setChallengeId(challenges.get(0).getId());
                role.setFinaxysProfile(finaxysProfile);
                roleRepository.saveOrUpdate(role);
            return NewResponseEntity("/fx_manager_add  : " + arguments + "\n " + "<@" + profileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfile.getId()).getName() + "> has just became a challenge manager!",true);

        }
        return NewResponseEntity("/fx_manager_add  : " + arguments + " you are not a challenge manager!",true);
    }

    @RequestMapping(value = "/challenge_manager/remove", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> remove(@RequestParam("token") String appVerificationToken,
                                           @RequestParam("team_domain")  String slackTeam,
                                           @RequestParam("user_id") String userId,
                                           @RequestParam("text") String arguments) {
        Log.info("/fx_manager_del"+arguments);
        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);

        ChallengeManagerArgumentsMatcher challengeManagerArgumentsMatcher = new ChallengeManagerArgumentsMatcher();
        if (!challengeManagerArgumentsMatcher.isCorrect(arguments)) {
            Message message = new Message("/fx_manager_del :" + arguments + "\n " + "Arguments should be :[challenge name] @Username");
            Log.info(message.getText().toString());
            return NewResponseEntity(message);}
        String finaxysProfileId = challengeManagerArgumentsMatcher.getUserIdArgument(arguments);
        String challengeName = challengeManagerArgumentsMatcher.getChallengeName(arguments);
        List<Challenge> challenges = challengeRepository.getByCriterion("name", challengeName);
        if (isChallengeManager(userId, challengeName) || isAdmin(userId)) {
            if (challenges.size() == 0) {
                Message message = new Message("challenge doesn't exist");
                Log.info(message.getText());
                return NewResponseEntity(message);
            }
            else {
                Challenge challenge = challenges.get(0);
                List<Role> roles = roleRepository.getByCriterion("challengeId", challenge.getId());
                for (Role role : roles) {
                    if (role.getFinaxysProfile().getId().equals(finaxysProfileId)) {
                        roleRepository.delete(role);
                        Message message = new Message("/fx_manager_del : " + arguments + "\n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is no more a challenge manager!");
                        Log.info(message.getText());
                        return NewResponseEntity(message);
                    }
                }
                Message message = new Message("/fx_manager_del : " + arguments + "\n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is already not a challenge manager!");
                Log.info(message.getText());
                return NewResponseEntity(message);
            }
        }
        Message message = new Message("/fx_manager_del : " + arguments + "\n " + "You are neither an admin nor a challenge manager");
        Log.info(message.getText());
        return NewResponseEntity(message);


    }

    @RequestMapping(value = "/challenge_manager/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getChallengeManagers(@RequestParam("token") String appVerificationToken,
                                                         @RequestParam("team_domain")  String slackTeam,
                                                         @RequestParam("text") String arguments) {
        Log.info("/fx_challenge_list");

        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);

        String challengeName = arguments.trim();
        List<Challenge> challenges = challengeRepository.getByCriterion("name", challengeName);
        if (challenges.size() == 0) {
            Message message = new Message("Challenge nonexistent");
            Log.info(message.getText());
            return NewResponseEntity(message);
        }
        List<Role> roles = roleRepository.getByCriterion("challengeId", challenges.get(0).getId());
        String messageText = "List of Challenge managers list:\n";
        for (Role role : roles) {
            messageText += "<@" + role.getFinaxysProfile().getId() + "|" + SlackBot.getSlackWebApiClient().getUserInfo(role.getFinaxysProfile().getId()).getName() + "> \n";
            Message message = new Message("/fx_challenge_list " + "\n " + messageText);
            Log.info(message.getText());
            return NewResponseEntity(message);
        }
        Message message = new Message("/fx_challenge_list :" + "\n " + messageText+" No challenge managers are found");
        Log.info(message.getText());
        return NewResponseEntity(message);
    }

}
