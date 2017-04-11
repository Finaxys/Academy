package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.BUL.Matchers.ChallengeTypeMatcher;
import com.finaxys.slackbot.BUL.Matchers.CreateChallengeMatcher;
import com.finaxys.slackbot.BUL.Matchers.DateMatcher;
import com.finaxys.slackbot.DAL.*;
import com.finaxys.slackbot.Utilities.Log;
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
public class ChallengesWebService extends BaseWebService {

	@Autowired
	private Repository<Challenge, Integer> challengeRepository;

	@Autowired
	Repository<FinaxysProfile, String> finaxysProfileRepository;

	@Autowired
	Repository<Role, Integer> roleRepository;


	private boolean requestParametersAreValid(String... parameters) 
	{
		for (String parameter : parameters) 
		{
			if (parameter == null || parameter.isEmpty())
				return false;
		}

		return true;
	}


	private String getStringFromList(List<Challenge> challenges) 
	{
		String result = "";

		for (Challenge challenge : challenges) 
		{
			result += "Challenge name: " + challenge.getName() + ", number of participants: " + challenge.getParticipants().size() + " \n ";
		}

		return result;
	}


	@RequestMapping(value = "/type", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getChallengesByType(@RequestParam("text") 		 String text,
														@RequestParam("token") 		 String appVerificationToken,
														@RequestParam("team_domain") String slackTeam) {

		if (NoAccess(appVerificationToken, slackTeam))
			return NoAccessResponseEntity(appVerificationToken, slackTeam);

		if (!requestParametersAreValid(new String[]{text, appVerificationToken, slackTeam}))
			return NewResponseEntity( " /fx_challenges_by_type " + text + " \n " + "There was a problem treating your request. Please try again.", true);
		
		ChallengeTypeMatcher challengeTypeMatcher = new ChallengeTypeMatcher();
		
		if (!challengeTypeMatcher.match(text))
			return NewResponseEntity(" /fx_challenges_by_type " + text + " \n " + "type has to be:[group|individual]", true);
		
		List<Challenge> challenges = challengeRepository.getByCriterion("type", text);
		
		if (challenges.isEmpty())
			return NewResponseEntity(" /fx_challenges_by_type " + text + " \n " + "No challenges with type" + text,true);

		return NewResponseEntity(" /fx_challenges_by_type " + text + " \n " + getStringFromList(challenges), true);

	}


	@RequestMapping(value = "/name", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getChallengesByName(@RequestParam("text") 		 String text,
														@RequestParam("token") 		 String appVerificationToken,
														@RequestParam("team_domain") String slackTeam) {
		
		Log.info("/fx_challenge_named " + text);
		
		if (NoAccess(appVerificationToken, slackTeam))
			return NoAccessResponseEntity(appVerificationToken, slackTeam);

		if (!requestParametersAreValid(new String[]{text, appVerificationToken, slackTeam}))
			return NewResponseEntity( " /fx_challenge_named " + text + " \n " + "There was a problem treating your request. Please try again.", true);

		List<Challenge> challenges = challengeRepository.getByCriterion("name", text);
		
		if (challenges.isEmpty())
			return NewResponseEntity("/fx_challenge_named "+text+"\n"+"Nonexistent challenge.",true);
		else
			return NewResponseEntity( "/fx_challenges_named " + text + "\n " + getStringFromList(challenges), true);

	}


	@RequestMapping(value = "/date", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getChallengesByDate(@RequestParam("text")  		  String text,
														@RequestParam("token") 		  String appVerificationToken,
														@RequestParam("team_domain")  String slackTeam) {

		if (NoAccess(appVerificationToken, slackTeam))
			return NoAccessResponseEntity(appVerificationToken, slackTeam);

		if (!requestParametersAreValid(new String[]{text, appVerificationToken, slackTeam}))
			return NewResponseEntity( " /fx_challenges_by_date " + text + " \n " + "There was a problem treating your request. Please try again.", true);

		DateMatcher dateMatcher = new DateMatcher();
		
		if (!dateMatcher.match(text.trim()))
			return NewResponseEntity(" /fx_challenges_by_date " + text + " \n " + "Date format: yyyy-MM-dd",true);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date wantedDate = new Date();
		try 
		{
			wantedDate = dateFormat.parse(text);
		} 
		catch (Exception e) 
		{
		}
		
		List<Challenge> challenges = challengeRepository.getByCriterion("creationDate", wantedDate);
		
		if (challenges.isEmpty())
			return NewResponseEntity(" /fx_challenges_by_date " + text + " \n " + "There are no challenges on this date: " + text,true);
		else
			return NewResponseEntity(getStringFromList(challenges),true);
	}


	@RequestMapping(value = "/listAll", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getAllChallenges(@RequestParam("token") 	  String appVerificationToken,
													 @RequestParam("team_domain") String slackTeam) {

		if (NoAccess(appVerificationToken, slackTeam))
			return NoAccessResponseEntity(appVerificationToken, slackTeam);

		if (!requestParametersAreValid(new String[]{appVerificationToken, slackTeam}))
			return NewResponseEntity(" /fx_challenge_list " + " \n " + "There was a problem treating your request. Please try again.", true);

		List<Challenge> challenges = challengeRepository.getAll();
		
		if (challenges.isEmpty())
			return NewResponseEntity("/fx_challenge_list" + " \n " + "There no previous challenges! Come on create one!", true);
		else
			return NewResponseEntity(getStringFromList(challenges), true);
	}
	

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> create(@RequestParam("token") 		String appVerificationToken,
										   @RequestParam("team_domain") String slackTeam,
										   @RequestParam("text") 		String text,
										   @RequestParam("user_id") 	String userId) {

		if(NoAccess(appVerificationToken, slackTeam))
			return NoAccessResponseEntity(appVerificationToken, slackTeam);

		if (!requestParametersAreValid(new String[]{text,appVerificationToken, slackTeam}))
			return NewResponseEntity(" /fx_challenge_add " + text+ " \n " + "There was a problem treating your request. Please try again.", true);


		CreateChallengeMatcher createChallengeMatcher = new CreateChallengeMatcher();
		
		if (!createChallengeMatcher.match(text.trim()))
			return  NewResponseEntity(" /fx_challenge_add "+text+" \n "+"Your request should have the following format: [challengeName],[group|individual],[descriptionText]",true);
		else 
		{
			String[]  challengeInfo = text.trim().split(",");
			Challenge challenge 	= new Challenge();
			
			challenge.setName		(challengeInfo[0]);
			challenge.setType		(challengeInfo[1]);
			challenge.setDescription(challengeInfo[2]);
			
			challengeRepository.addEntity(challenge);
			
			Role role = new Role();
			
			role.setRole		  ("challenge_manager");
			role.setFinaxysProfile(finaxysProfileRepository.findById(userId));
			role.setChallengeId	  (challenge.getId());
			
			roleRepository.addEntity(role);
			
			return NewResponseEntity("/fx_challenge_add "+text+" \n "+"Challenge successfully added and you are the challenge manager.", true);
		}

	}

	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> delete(@RequestParam("token") 		String appVerificationToken,
										   @RequestParam("team_domain") String slackTeam,
										   @RequestParam("user_id") 	String profileId,
										   @RequestParam("text") 		String text) {

		if(NoAccess(appVerificationToken, slackTeam))
			return NoAccessResponseEntity(appVerificationToken, slackTeam);
		
		String  		challengeName = text.trim();
		List<Challenge> challenges 	  = challengeRepository.getByCriterion("name",challengeName);
		
		if(challenges.size()==0)
			return NewResponseEntity("fx_challenge_del "+"\n"+"Non existent challenge.",true);

		if (!requestParametersAreValid(new String[]{text,appVerificationToken, slackTeam}))
			return NewResponseEntity(" /fx_challenge_del " + text+ " \n " + "There was a problem treating your request. Please try again.", true);

		if(!isChallengeManager(profileId,challengeName) || !isAdmin(profileId))
			return NewResponseEntity("fx_challenge_del "+"\n"+"You are neither an admin nor a challenge manager!",true);

		Challenge  challenge =  challenges.get(0);
		List<Role> roles 	 = roleRepository.getByCriterion("challengeId",challenges.get(0).getId());
		
		for(Role role : roles)
		{
			roleRepository.delete(role);
		}
		
		challengeRepository.delete(challenge);
		
		return NewResponseEntity("fx_challenge_del "+text+" \n "+"Challenge successfully removed.", true);
	}
}
