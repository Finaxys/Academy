package com.finaxys.slackbot.WebServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.DAL.Action;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Message;
import com.finaxys.slackbot.Utilities.ArgumentsSplitter;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.SlackBotTimer;
import com.finaxys.slackbot.interfaces.ActionService;
import com.finaxys.slackbot.interfaces.SlackUserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@RestController
@RequestMapping("/entreprise")
public class EntrepriseWebService extends BaseWebService {
	
	@Autowired
	SlackUserService slackUserService;
	
	@RequestMapping(value = "/start", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<JsonNode> startDiscover(@RequestParam("user_id") String userId){ 
    	
    	SlackBotTimer timer          = new SlackBotTimer();
    	JSONObject    response       = new JSONObject();
    	JSONArray     attachements   = new JSONArray();
    	JSONObject    firstQuestion  = new JSONObject();
    	JSONArray     suggestions    = new JSONArray();
    	JSONObject    finaxysAcademy = new JSONObject();
    	JSONObject    finaxysLab     = new JSONObject();
    	Log.info("The entreprise POST method is triggered!!");
    	System.out.println("he entreprise POST method is triggered!!");
    	try {
    		// hello world 
    		response.put("text","Bienvenu dans notre présentation de Finaxys!");
	    	
	    	firstQuestion.put("text", "Découvrir Finaxys IT !");
	    	firstQuestion.put("fallback", "Shame... buttons aren't supported in this land");
	    	firstQuestion.put("callback_id", "discover_finaxys");
	    	firstQuestion.put("color", "#3AA3E3");
	    	firstQuestion.put("attachement_type", "default");
	    	
	    	
	    	// declare first action
	    	finaxysAcademy.put("name", "Finaxys Academy");
	    	finaxysAcademy.put("text", "Finaxys Academy");
	    	finaxysAcademy.put("type", "button");
	    	finaxysAcademy.put("value", "yes");

	    	// declare second action
	    	
	    	finaxysLab.put("name", "Finaxys Lab");
	    	finaxysLab.put("text", "Finaxys Lab");
	    	finaxysLab.put("type", "button");
	    	finaxysLab.put("value", "no");
	    	
	    	
	    	// add the two action to suggestions 
	    	suggestions.put(finaxysAcademy);
	    	suggestions.put(finaxysLab);

	    	
	    	firstQuestion.put("actions", suggestions);
	    	attachements.put(firstQuestion);
	    	response.put("attachments",attachements);
    	
    	} catch (JSONException e) {
			e.printStackTrace();
		}
    	
    	Log.info("JSONOBJECT="+response.toString());
    	System.out.println("JSONOBJECT="+response.toString());
    	
    	// return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
        return newResponseEntity(response.toString(), true);

    }
    
}
