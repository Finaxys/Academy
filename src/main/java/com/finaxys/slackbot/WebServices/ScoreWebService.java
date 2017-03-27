package com.finaxys.slackbot.WebServices;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Matchers.ChallengeScoreArgumentsMatcher;
import com.finaxys.slackbot.BUL.Matchers.OneUsernameArgumentMatcher;
import com.finaxys.slackbot.DAL.*;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/score")
public class ScoreWebService extends BaseWebService{
    @Autowired
    Repository<FinaxysProfile, String> finaxysProfileRepository;

    @Autowired
    Repository<Challenge, Integer> challengeRepository;

    @Autowired
    Repository<FinaxysProfile_Challenge, FinaxysProfile_Challenge_PK> finaxysProfileChallengeRepository;

    @Autowired
    Repository<Role, Integer> roleRepository;

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> addChallengeScore(@RequestParam("token") String appVerificationToken,
                                                         @RequestParam("team_domain")  String slackTeam,
                                                         @RequestParam("text") String arguments,
                                                         @RequestParam("user_id") String challengeManagerId
                                                      ) {

        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);


        ChallengeScoreArgumentsMatcher challengeScoreArgumentsMatcher = new ChallengeScoreArgumentsMatcher();

        if (!challengeScoreArgumentsMatcher.isCorrect(arguments))
            return NewResponseEntity("/fx_challenge_score_add "+arguments+" \n "+"Arguments should suit ' .... @Username ... 20 ..... <challengeName> challenge ..' Pattern !", true);



        String userId = challengeScoreArgumentsMatcher.getFinaxysProfileId(arguments);
        String challengeName = challengeScoreArgumentsMatcher.getChallengeName(arguments);
        List<Challenge> challenges = challengeRepository.getByCriterion("name", challengeName);
        if(challenges.size()==0)
            return NewResponseEntity("Nonexistent challenge", true);

      if (!isChallengeManager(challengeManagerId,challengeName ) && !isAdmin(challengeManagerId))
            return NewResponseEntity("/fx_challenge_score_add "+arguments+"\n"+"You are neither admin nor a challenge manager !", true);



        int score = Integer.parseInt(challengeScoreArgumentsMatcher.getScore(arguments));
        FinaxysProfile_Challenge finaxysProfile_challenge = new FinaxysProfile_Challenge(score, challengeRepository.getByCriterion("name", challengeName).get(0).getId(), userId);
        finaxysProfile_challenge.setFinaxysProfile(finaxysProfileRepository.findById(userId));
        finaxysProfile_challenge.setChallenge(challengeRepository.getByCriterion("name", challengeName).get(0));
        try {
            finaxysProfileChallengeRepository.saveOrUpdate(finaxysProfile_challenge);

        }catch (Exception e){
            return NewResponseEntity("/fx_challenge_score_add "+arguments+" \n"+"A problem has occured! The user may have a score for this challenge already !",true);

        }
        return NewResponseEntity("/fx_challenge_score_add "+arguments+" \n"+"Score has been added !",true);

    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> listScoreForChallenge(@RequestParam("token") String appVerificationToken,
                                                          @RequestParam("team_domain")  String slackTeam,
                                                          @RequestParam("text") String arguments) {

        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);


        String challengeName = arguments.trim();
        List<Challenge> challenges = challengeRepository.getByCriterion("name", challengeName);
        if (challenges.size() == 0)
            return NewResponseEntity("/fx_challenge_score_list "+arguments+" \n"+"No such challenge ! Check the challenge name", true);


        Challenge challenge = challenges.get(0);
        List<FinaxysProfile_Challenge> listChallenges = finaxysProfileChallengeRepository.getByCriterion("challenge", challenge);

        if (listChallenges.size() == 0) {
            NewResponseEntity("/fx_challenge_score_list "+arguments+" \n"+"No score has been saved till the moment !",true);

        }
        String textMessage = "List of scores of " + challenge.getName() + " :"+" \n ";
        for (FinaxysProfile_Challenge finaxysProfileChallenge : listChallenges) {
            FinaxysProfile finaxysProfile = finaxysProfileChallenge.getFinaxysProfile();
            textMessage += "<@" + finaxysProfile.getId() + "|" + finaxysProfile.getName() + "> "+finaxysProfileChallenge.getScore()+" \n";
        }


        return NewResponseEntity("/fx_challenge_score_list "+arguments+" \n"+textMessage, true);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> scoreList(@RequestParam("token") String appVerificationToken,
                                              @RequestParam("team_domain")  String slackTeam,
                                              @RequestParam("text") String arguments) {

        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);

        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();
        if (!oneUsernameArgumentsMatcher.isCorrect(arguments))
            return  NewResponseEntity("/fx_score  : " + arguments + " \n " + "Arguments should be :@Username" , true);

        String profileId = oneUsernameArgumentsMatcher.getUserIdArgument(arguments);
        FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(profileId);
        return NewResponseEntity("<@" + finaxysProfile.getId() + "|" + SlackBot.getSlackWebApiClient().getUserInfo(profileId).getName() + "> score :"+finaxysProfile.getScore() , true);


    }

   }
