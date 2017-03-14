package com.finaxys.slackbot.restServices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.finaxys.slackbot.BUL.Classes.SlackBotCommandServiceImpl;
import com.finaxys.slackbot.BUL.Interfaces.SlackBotCommandService;
import com.finaxys.slackbot.Domains.FinaxysProfile;

@RestController
public class CommandsRestController {

	@Autowired
	SlackBotCommandService slackBotCommandServiceImpl;

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<FinaxysProfile>> listAllUsers() {
		System.out.println();
		List<FinaxysProfile> users = slackBotCommandServiceImpl.listerUsers();
		return new ResponseEntity<List<FinaxysProfile>>(users, HttpStatus.OK);
	}
}
