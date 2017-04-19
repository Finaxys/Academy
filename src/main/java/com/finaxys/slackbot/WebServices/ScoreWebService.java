package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.BUL.Matchers.ChallengeScoreArgumentsMatcher;
import com.finaxys.slackbot.BUL.Matchers.OneUsernameArgumentMatcher;
import com.finaxys.slackbot.DAL.*;
import com.finaxys.slackbot.Utilities.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/score")
public class ScoreWebService extends BaseWebService {
	@Autowired
	private SlackApiAccessService slackApiAccessService;

	@Autowired
	Repository<SlackUser, String> finaxysProfileRepository;

	@Autowired
	Repository<Event, Integer> challengeRepository;

	@Autowired
	Repository<SlackUser_Event, SlackUser_Event_PK> finaxysProfileChallengeRepository;

	@Autowired
	Repository<Role, Integer> roleRepository;

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> addChallengeScore(@RequestParam("token") String appVerificationToken,
			@RequestParam("team_domain") String slackTeam, @RequestParam("text") String arguments,
			@RequestParam("user_id") String challengeManagerId) {

		Timer timer = new Timer();

		if (noAccess(appVerificationToken, slackTeam))
			return noAccessResponseEntity(appVerificationToken, slackTeam);

		timer.capture();

		ChallengeScoreArgumentsMatcher challengeScoreArgumentsMatcher = new ChallengeScoreArgumentsMatcher();

		if (!challengeScoreArgumentsMatcher.isCorrect(arguments))
			return newResponseEntity("/fx_challenge_score_add " + arguments + " \n "
					+ "Arguments should suit ' .... @Username ... 20 ..... <challengeName> challenge ..' Pattern !"
					+ timer, true);

		String userId = challengeScoreArgumentsMatcher.getFinaxysProfileId(arguments);
		String challengeName = challengeScoreArgumentsMatcher.getChallengeName(arguments);

		timer.capture();

		List<Event> challenges = challengeRepository.getByCriterion("name", challengeName);

		timer.capture();

		if (challenges.size() == 0)
			return newResponseEntity("Nonexistent challenge" + timer, true);

		if (!isChallengeManager(challengeManagerId, challengeName) && !isAdmin(challengeManagerId))
			return newResponseEntity("/fx_challenge_score_add " + arguments + "\n"
					+ "You are neither admin nor a challenge manager !" + timer, true);

		int score = Integer.parseInt(challengeScoreArgumentsMatcher.getScore(arguments));

		SlackUser_Event finaxysProfile_challenge = new SlackUser_Event(score,
				challengeRepository.getByCriterion("name", challengeName).get(0).getId(), userId);

		timer.capture();

		finaxysProfile_challenge.setSlackUser(finaxysProfileRepository.findById(userId));
		finaxysProfile_challenge.setEvent(challengeRepository.getByCriterion("name", challengeName).get(0));

		timer.capture();

		try {
			timer.capture();

			new Thread(new Runnable()
			{
				public void run()
				{
					finaxysProfileChallengeRepository.saveOrUpdate(finaxysProfile_challenge);
				}
			}).start();

			timer.capture();
		} catch (Exception e) {
			return newResponseEntity(
					"/fx_challenge_score_add " + arguments + " \n"
							+ "A problem has occured! The user may have a score for this challenge already !" + timer,
					true);
		}

		return newResponseEntity("/fx_challenge_score_add " + arguments + " \n" + "Score has been added !" + timer,
				true);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> listScoreForChallenge(@RequestParam("token") String appVerificationToken,
			@RequestParam("team_domain") String slackTeam, @RequestParam("text") String arguments) {

		Timer timer = new Timer();

		if (noAccess(appVerificationToken, slackTeam))
			return noAccessResponseEntity(appVerificationToken, slackTeam);
		
		timer.capture();
		
		String challengeName = arguments.trim();
		List<Event> challenges = challengeRepository.getByCriterion("name", challengeName);

		if (challenges.size() == 0)
			return newResponseEntity(
					"/fx_challenge_score_list " + arguments + " \n" + "No such challenge ! Check the challenge name" +timer,
					true);

		Event challenge = challenges.get(0);
		
		timer.capture();
		
		List<SlackUser_Event> listChallenges = finaxysProfileChallengeRepository.getByCriterion("challenge",
				challenge);

		if (listChallenges.size() == 0)
			newResponseEntity(
					"/fx_challenge_score_list " + arguments + " \n" + "No score has been saved till the moment !" + timer ,
					true);

		String textMessage = "List of scores of " + challenge.getName() + " :" + " \n ";

		for (SlackUser_Event finaxysProfileChallenge : listChallenges) {
			SlackUser finaxysProfile = finaxysProfileChallenge.getSlackUser();

			textMessage += "<@" + finaxysProfile.getId() + "|" + finaxysProfile.getName() + "> "
					+ finaxysProfileChallenge.getScore() + " \n";
		}

		return newResponseEntity("/fx_challenge_score_list " + arguments + " \n" + textMessage + timer, true);
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> scoreList(@RequestParam("token") String appVerificationToken,
			@RequestParam("team_domain") String slackTeam, @RequestParam("text") String arguments) {
		
		Timer timer = new Timer();
		
		if (noAccess(appVerificationToken, slackTeam))
			return noAccessResponseEntity(appVerificationToken, slackTeam);

		OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();
		
		timer.capture();
		
		if (!oneUsernameArgumentsMatcher.isCorrect(arguments))
			return newResponseEntity("/fx_score  : " + arguments + " \n " + "Arguments should be :@Username" + timer , true);

		String profileId = oneUsernameArgumentsMatcher.getUserIdArgument(arguments);
		SlackUser finaxysProfile = finaxysProfileRepository.findById(profileId);
		
		timer.capture();
		
		return newResponseEntity("<@" + finaxysProfile.getId() + "|"
				+ slackApiAccessService.getUser(profileId).getName() + "> score :" + finaxysProfile.getScore() + timer , true);
	}
}

