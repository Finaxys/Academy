package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.BUL.Matchers.ChallengeTypeMatcher;
import com.finaxys.slackbot.BUL.Matchers.CreateChallengeMatcher;
import com.finaxys.slackbot.BUL.Matchers.DateMatcher;
import com.finaxys.slackbot.DAL.*;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import com.finaxys.slackbot.Utilities.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/challenges")
public class ChallengesWebService {

    @Autowired
    private Repository<Challenge, Integer> challengeRepository;
    @Autowired
    Repository<FinaxysProfile, String> finaxysProfileRepository;
    @Autowired
    Repository<Role, Integer> roleRepository;

    private ObjectMapper objectMapper;

    public ChallengesWebService() {
        objectMapper = new ObjectMapper();
    }

    private boolean tokenIsValid(String appVerificationToken) {
        return appVerificationToken.equals(Settings.appVerificationToken);
    }

    private boolean teamIdIsValid(String slackTeam) {
        return slackTeam.equals(Settings.slackTeam);
    }

    private ResponseEntity<JsonNode> showMessage(String token, String teamDomain, Message message) {
        if (!tokenIsValid(token)) {
            message = new Message("Wrong verification token !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        if (!teamIdIsValid(teamDomain)) {
            message = new Message("Only for FinaxysTM members !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
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
            result += "Challenge name: "+ challenge.getName()+", number of participants: "+ challenge.getParticipants().size() +" \n ";
        }
        return result;
    }


    @RequestMapping(value = "/type", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getChallengesByType(@RequestParam("text") String text,
                                                        @RequestParam("AppVerificationToken") String appVerificationToken,
                                                        @RequestParam("slackTeam") String slackTeam) {
        FinaxysSlackBotLogger.logCommandRequest("/fx_challenges_by_type");
        Message message = new Message("");
        if (requestParametersAreValid(new String[]{text, appVerificationToken, slackTeam})) {
            if (tokenIsValid(appVerificationToken) && teamIdIsValid(slackTeam)) {
                ChallengeTypeMatcher challengeTypeMatcher = new ChallengeTypeMatcher();
                if (!challengeTypeMatcher.match(text))
                    message.setText(" /fx_challenges_by_type "+text+" \n "+"type has to be:[group|individual]");
                else {
                    List<Challenge> challenges = challengeRepository.getByCriterion("type", text);
                    if (challenges.isEmpty())
                        message.setText(" /fx_challenges_by_type "+text+" \n "+"No challenges with type" + text);
                    else {
                        String result = " /fx_challenges_by_type "+text+" \n "+getStringFromList(challenges);
                        message.setText(result);

                        FinaxysSlackBotLogger.logCommandResponse(message.toString());
                        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
                    }

                }
            }
        } else
            message.setText(" /fx_challenges_by_type "+text+" \n "+"There was a problem treating your request. Please try again.");
        FinaxysSlackBotLogger.logCommandResponse(message.getText());
        return this.showMessage(appVerificationToken, slackTeam, message);

    }

    @RequestMapping(value = "/name", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getChallengesByName(@RequestParam("text") String text,
                                                        @RequestParam("appVerificationToken") String appVerificationToken,
                                                        @RequestParam("slackTeam") String slackTeam) {
        FinaxysSlackBotLogger.logCommandRequest("/fx_challenge_named");
        Message message = new Message("");
        if (requestParametersAreValid(new String[]{text, appVerificationToken, slackTeam})) {
            if (tokenIsValid(appVerificationToken) && teamIdIsValid(slackTeam)) {
                List<Challenge> challenges = challengeRepository.getByCriterion("name", text);
                if (challenges.isEmpty())
                    message.setText("Nonexistent challenge.");
                else {
                    String result = "/fx_challenges_named "+text+"\n "+getStringFromList(challenges);
                    message.setText(result);
                    FinaxysSlackBotLogger.logCommandResponse(message.toString());
                    return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
                }
            }

        } else
            message.setText(" /fx_challenges_named "+text+" \n "+"There was a problem treating your request. Please try again.");
        FinaxysSlackBotLogger.logCommandResponse(message.getText());
        return this.showMessage(appVerificationToken, slackTeam, message);
    }

    @RequestMapping(value = "/date", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getChallengesByDate(@RequestParam("text") String text,
                                                        @RequestParam("appVerificationToken") String appVerificationToken,
                                                        @RequestParam("slackTeam") String slackTeam) {
        FinaxysSlackBotLogger.logCommandRequest("/fx_challenges_by_date");
        Message message = new Message("");
        if (requestParametersAreValid(new String[]{text, appVerificationToken, slackTeam})) {
            if (tokenIsValid(appVerificationToken) && teamIdIsValid(slackTeam)) {
                DateMatcher dateMatcher = new DateMatcher();
                if (!dateMatcher.match(text.trim()))
                    message.setText(" /fx_challenges_by_date "+text+" \n "+"Date format: yyyy-MM-dd");
                else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date wantedDate = new Date();
                    try {
                        wantedDate = dateFormat.parse(text);
                    } catch (Exception e) {
                    }
                    List<Challenge> challenges = challengeRepository.getByCriterion("creationDate", wantedDate);
                    if (challenges.isEmpty())
                        message.setText(" /fx_challenges_by_date "+text+" \n "+"There are no challenges on this date: " + text);
                    else {
                        String result = getStringFromList(challenges);
                        message.setText(result);
                        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
                    }
                }
            }
        } else
            message.setText(" /fx_challenges_by_date "+text+" \n "+"There was a problem treating your request. Please try again.");
        FinaxysSlackBotLogger.logCommandResponse(message.getText());
        return this.showMessage(appVerificationToken, slackTeam, message);
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getAllChallenges(@RequestParam("token") String token,
                                                     @RequestParam("slackTeam") String slackTeam
    ) {
        FinaxysSlackBotLogger.logCommandRequest("/fx_challenge_list");
        Message message = new Message("");
        if (requestParametersAreValid(new String[]{token, slackTeam})) {
            if (tokenIsValid(token) && teamIdIsValid(slackTeam)) {
                List<Challenge> challenges = challengeRepository.getAll();
                if (challenges.isEmpty())
                    message.setText("/fx_challenge_list" +" \n "+"There no previous challenges! Come on create one!");
                else {
                    String result = getStringFromList(challenges);
                    message.setText(result);

                    return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
                }
            }
        } else
            message.setText((" /fx_challenge_list" +" \n "+"There was a problem treating your request. Please try again."));
        return this.showMessage(token, slackTeam, message);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> create(@RequestParam("appVerificationToken") String appVerificationToken,
                                                    @RequestParam("slackTeam") String slackTeam,
                                                    @RequestParam("user_id") String profileId,
                                                    @RequestParam("text") String text) {
        FinaxysSlackBotLogger.logCommandResponse("/fx_challenge_add");
        Message message = new Message("");
        if (requestParametersAreValid(new String[]{appVerificationToken, slackTeam, text})) {
            if (tokenIsValid(appVerificationToken) && teamIdIsValid(slackTeam)) {
                CreateChallengeMatcher createChallengeMatcher = new CreateChallengeMatcher();
                if (!createChallengeMatcher.match(text.trim()))
                    message.setText(" /fx_challenge_add "+text+" \n "+"Your request should have the following format: [challengeName],[group|individual],[descriptionText]");
                else {
                    String[] challengeInfo = text.trim().split(",");
                    Challenge challenge = new Challenge();
                    challenge.setName(challengeInfo[0]);
                    challenge.setType(challengeInfo[1]);
                    challenge.setDescription(challengeInfo[2]);
                    challengeRepository.addEntity(challenge);
                    Role role = new Role();
                    role.setRole("challenge_manager");
                    role.setFinaxysProfile(finaxysProfileRepository.findById(profileId));
                    role.setChallengeId(challenge.getId());
                    roleRepository.addEntity(role);
                    message.setText("/fx_challenge_add "+text+" \n "+"Challenge successfully added and you are the challenge manager.");
                }
            }
        } else
            message.setText("/fx_challenge_add "+text+" \n "+"There was a problem treating your request. Please try again.");
        FinaxysSlackBotLogger.logCommandResponse(message.getText());
        return this.showMessage(appVerificationToken, slackTeam, message);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> delete(@RequestParam("appVerificationToken") String appVerificationToken,
                                           @RequestParam("slackTeam") String slackTeam,
                                           @RequestParam("user_id") String profileId,
                                           @RequestParam("text") String text) {
        FinaxysSlackBotLogger.logCommandResponse("/fxadmin_manager_challenge_del");
        Message message = new Message("");
        if (requestParametersAreValid(new String[]{appVerificationToken, slackTeam, text})) {
            if (tokenIsValid(appVerificationToken) && teamIdIsValid(slackTeam)) {
                String challengeName = text.trim();
               if(!userIsChallengeManager(profileId,challengeName) || !userIsAdministrator(profileId))
               {
                   message.setText("You are neither an admin nr a challenge manager!");
               }
                    List<Challenge> challenges = challengeRepository.getByCriterion("name",challengeName);

                    if(challenges.size()==0)
                    {
                        message.setText("Non existent challenge.");
                    }
                    else
                    {
                        Challenge  challenge =  challenges.get(0);
                    List<Role> roles = roleRepository.getByCriterion("challengeId",challenges.get(0).getId());
                        for(Role role : roles)
                        {
                            roleRepository.delete(role);
                        }
                        challengeRepository.delete(challenge);
                    message.setText("/fxadmin_manager_challenge "+text+" \n "+"Challenge successfully removed.");
                }
            }
        } else
            message.setText("/fxadmin_manager_challenge "+text+" \n "+"There was a problem treating your request. Please try again.");
        FinaxysSlackBotLogger.logCommandResponse(message.getText());
        return this.showMessage(appVerificationToken, slackTeam, message);
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
