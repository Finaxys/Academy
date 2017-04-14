package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.BUL.Matchers.ChallengeTypeMatcher;
import com.finaxys.slackbot.BUL.Matchers.CreateChallengeMatcher;
import com.finaxys.slackbot.BUL.Matchers.DateMatcher;
import com.finaxys.slackbot.DAL.*;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.Settings;
import com.finaxys.slackbot.Utilities.Timer;

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

		Timer timer = new Timer();
		
		if (NoAccess(appVerificationToken, slackTeam))
			return NoAccessResponseEntity(appVerificationToken, slackTeam);
		
		timer.capture();

		if (!requestParametersAreValid(new String[]{text, appVerificationToken, slackTeam}))
			return NewResponseEntity( " /fx_challenges_by_type " + text + " \n " + "There was a problem treating your request. Please try again." + timer , true);
		
		ChallengeTypeMatcher challengeTypeMatcher = new ChallengeTypeMatcher();
		
		if (!challengeTypeMatcher.match(text))
			return NewResponseEntity(" /fx_challenges_by_type " + text + " \n " + "type has to be:[group|individual]" + timer , true);
		timer.capture();
		
		List<Challenge> challenges = challengeRepository.getByCriterion("type", text);
		
		if (challenges.isEmpty())
			return NewResponseEntity(" /fx_challenges_by_type " + text + " \n " + "No challenges with type" + text + timer ,true);

		return NewResponseEntity(" /fx_challenges_by_type " + text + " \n " + getStringFromList(challenges) + timer , true);

	}


	@RequestMapping(value = "/name", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getChallengesByName(@RequestParam("text") 		 String text,
														@RequestParam("token") 		 String appVerificationToken,
														@RequestParam("team_domain") String slackTeam) {
		Timer timer = new Timer();
		
		Log.info("/fx_challenge_named " + text);
		
		if (NoAccess(appVerificationToken, slackTeam))
			return NoAccessResponseEntity(appVerificationToken, slackTeam);
		
		timer.capture();
		
		if (!requestParametersAreValid(new String[]{text, appVerificationToken, slackTeam}))
			return NewResponseEntity( " /fx_challenge_named " + text + " \n " + "There was a problem treating your request. Please try again." + timer , true);
		
		
		List<Challenge> challenges = challengeRepository.getByCriterion("name", text);
		
		timer.capture();
		
		if (challenges.isEmpty())
			return NewResponseEntity("/fx_challenge_named "+text+"\n"+"Nonexistent challenge." + timer,true);
		else
			return NewResponseEntity( "/fx_challenges_named " + text + "\n " + getStringFromList(challenges) + timer , true);

	}


	@RequestMapping(value = "/date", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getChallengesByDate(@RequestParam("text")  		  String text,
														@RequestParam("token") 		  String appVerificationToken,
														@RequestParam("team_domain")  String slackTeam) {

		Timer timer = new Timer();
		
		if (NoAccess(appVerificationToken, slackTeam))
			return NoAccessResponseEntity(appVerificationToken, slackTeam);
		
		timer.capture();

		if (!requestParametersAreValid(new String[]{text, appVerificationToken, slackTeam}))
			return NewResponseEntity( " /fx_challenges_by_date " + text + " \n " + "There was a problem treating your request. Please try again." + timer , true);

		DateMatcher dateMatcher = new DateMatcher();
		
		if (!dateMatcher.match(text.trim()))
			return NewResponseEntity(" /fx_challenges_by_date " + text + " \n " + "Date format: yyyy-MM-dd" + timer ,true);

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
		
		timer.capture();
		
		if (challenges.isEmpty())
			return NewResponseEntity(" /fx_challenges_by_date " + text + " \n " + "There are no challenges on this date: " + text + timer ,true);
		else
			return NewResponseEntity(getStringFromList(challenges) + timer ,true);
	}


	@RequestMapping(value = "/listAll", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getAllChallenges(@RequestParam("token") 	  String appVerificationToken,
													 @RequestParam("team_domain") String slackTeam) {
		
		Timer timer = new Timer();

		if (NoAccess(appVerificationToken, slackTeam))
			return NoAccessResponseEntity(appVerificationToken, slackTeam);
		
		timer.capture();
		
		if (!requestParametersAreValid(new String[]{appVerificationToken, slackTeam}))
			return NewResponseEntity(" /fx_challenge_list " + " \n " + "There was a problem treating your request. Please try again." + timer, true);
		
		timer.capture();
		
		List<Challenge> challenges = challengeRepository.getAll();
		
		timer.capture();
		
		if (challenges.isEmpty())
			return NewResponseEntity("/fx_challenge_list" + " \n " + "There no previous challenges! Come on create one!" + timer, true);
		else
			return NewResponseEntity(getStringFromList(challenges) + timer, true);
	}
	

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> create(@RequestParam("token") 		String appVerificationToken,
										   @RequestParam("team_domain") String slackTeam,
										   @RequestParam("text") 		String text,
										   @RequestParam("user_id") 	String userId) {
		
		Timer timer = new Timer();

		if(NoAccess(appVerificationToken, slackTeam))
			return NoAccessResponseEntity(appVerificationToken, slackTeam);
		timer.capture();
		
		if (!requestParametersAreValid(new String[]{text,appVerificationToken, slackTeam}))
			return NewResponseEntity(" /fx_challenge_add " + text+ " \n " + "There was a problem treating your request. Please try again." + timer, true);


		CreateChallengeMatcher createChallengeMatcher = new CreateChallengeMatcher();
		
		timer.capture();
		
		if (!createChallengeMatcher.match(text.trim()))
			return  NewResponseEntity(" /fx_challenge_add "+text+" \n "+"Your request should have the following format: [challengeName],[group|individual],[descriptionText]" + timer ,true);
		else 
		{	
			timer.capture();
			
			String[]  challengeInfo = text.trim().split(",");
			Challenge challenge 	= new Challenge();
			
			challenge.setName		(challengeInfo[0]);
			challenge.setType		(challengeInfo[1]);
			challenge.setDescription(challengeInfo[2]);
			
			new Thread(new Runnable()
			{
				public void run()
				{
					challengeRepository.addEntity(challenge);
					
					Role role = new Role();
					
					role.setRole		  ("challenge_manager");
					role.setSlackUser(finaxysProfileRepository.findById(userId));
					role.setChallenge  (challenge);
					
					roleRepository.addEntity(role);
					
					NewResponseEntity("/fx_challenge_add "+text+" \n "+"Traitement terminé" + timer , true);
				}
			}).start();

			
			timer.capture();
			
			return NewResponseEntity("/fx_challenge_add "+text+" \n "+"Traitement en cours" + timer , true);
		}

	}

	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> delete(@RequestParam("token") 		String appVerificationToken,
										   @RequestParam("team_domain") String slackTeam,
										   @RequestParam("user_id") 	String profileId,
										   @RequestParam("text") 		String text) {
		
		Timer timer = new Timer();

		if(NoAccess(appVerificationToken, slackTeam))
			return NoAccessResponseEntity(appVerificationToken, slackTeam);
		
		timer.capture();
		
		String  		challengeName = text.trim();
		List<Challenge> challenges 	  = challengeRepository.getByCriterion("name",challengeName);
		
		timer.capture();
		
		if(challenges.size()==0)
			return NewResponseEntity("fx_challenge_del "+"\n"+"Non existent challenge." + timer ,true);

		if (!requestParametersAreValid(new String[]{text,appVerificationToken, slackTeam}))
			return NewResponseEntity(" /fx_challenge_del " + text+ " \n " + "There was a problem treating your request. Please try again." + timer , true);

		if(!isChallengeManager(profileId,challengeName) && !isAdmin(profileId))
			return NewResponseEntity("fx_challenge_del "+"\n"+"You are neither an admin nor a challenge manager!" + timer ,true);
		
		timer.capture();
		
		Challenge  challenge =  challenges.get(0);
		
		new Thread(new Runnable()
		{
			public void run()
			{
				List<Role> roles = roleRepository.getByCriterion("challengeId",challenges.get(0).getId());
				
				for(Role role : roles)
				{
					roleRepository.delete(role);
				}
				
				challengeRepository.delete(challenge);
			}
		}).start();
		
		timer.capture();
		
		return NewResponseEntity("fx_challenge_del "+text+" \n "+"Challenge successfully removed." + timer , true);
	}
}
