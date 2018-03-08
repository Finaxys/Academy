package com.finaxys.slackbot.WebServices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.DAL.Parameter;
import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.Utilities.AppParameters;
import com.finaxys.slackbot.Utilities.ArgumentsSplitter;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.SlackBot;
import com.finaxys.slackbot.Utilities.SlackBotTimer;
import com.finaxys.slackbot.interfaces.RoleService;
import com.finaxys.slackbot.interfaces.SlackUserService;

@RestController
@RequestMapping("/admins")

public class AdministratorWebService extends BaseWebService {

	@Autowired
	private RoleService roleService;

	@Autowired
	public SlackApiAccessService slackApiAccessService;

	@Autowired
	private AppParameters parameters;

	@Autowired
	private SlackUserService slackUserService;

	@RequestMapping(value = "/fxadmin_add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> create(@RequestParam("user_id") String profileId,
			@RequestParam("text") String arguments) {
		SlackBotTimer timer = new SlackBotTimer();

		timer.capture();

		if (!slackUserService.isAdmin(profileId) && roleService.getAllAdmins().size() != 0)
			return newResponseEntity("/fxadmin_add " + arguments + " \n " + "You are not an admin!" + timer, true);

		timer.capture();

		ArgumentsSplitter argumentsSplitter = new ArgumentsSplitter(arguments, "/fxadmin_add");

		String userId = argumentsSplitter.getUserId();

		if (!slackUserService.isAdmin(userId)) {
			SlackUser slackUser = slackUserService.get(userId);

			Role role = new Role("admin", slackUser, null);
			new Thread(() -> {
				roleService.save(role);
			}).start();

			timer.capture();

			return newResponseEntity("/fxadmin_add  : " + arguments + " \n " + "<@" + userId + "|"
					+ SlackBot.getSlackWebApiClient().getUserInfo(userId).getName()
					+ "> has just became an administrator! " + timer, true);
		} else
			return newResponseEntity("/fxadmin_add  : " + arguments + " \n " + "<@" + userId + "|"
					+ SlackBot.getSlackWebApiClient().getUserInfo(userId).getName() + "> is already an administrator!"
					+ timer, true);

	}

	@RequestMapping(value = "/fxadmin_del", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> remove(@RequestParam("user_id") String userId,
			@RequestParam("text") String arguments) {
		SlackBotTimer timer = new SlackBotTimer();

		timer.capture();
		if (!isAdmin(userId))
			return newResponseEntity("/fxadmin_del " + arguments + " \n " + "You are not an admin!" + timer);

		timer.capture();

		ArgumentsSplitter argumentsSplitter = new ArgumentsSplitter(arguments, "/fxadmin_del");

		String id = argumentsSplitter.getUserId();

		if (!isAdmin(id))
			return newResponseEntity("/fxadmin_del : " + arguments + " \n " + "<@" + id + "|"
					+ SlackBot.getSlackWebApiClient().getUserInfo(id).getName() + "> is already not an administrator!"
					+ timer, true);

		timer.capture();

		List<Role> roles = roleService.getAllAdmins();

		for (Role role : roles) {
			if (role.getSlackUser().getSlackUserId().equals(id)) {
				new Thread(() -> {
					roleService.remove(role);
				}).start();

				return newResponseEntity("/fxadmin_del : " + arguments + " \n " + "<@" + id + "|"
						+ SlackBot.getSlackWebApiClient().getUserInfo(id).getName() + "> is no more an administrator!"
						+ timer, true);
			}
		}
		timer.capture();
		return newResponseEntity("/fxadmin_del : " + arguments + " \n " + "<@" + id + "|"
				+ SlackBot.getSlackWebApiClient().getUserInfo(id).getName() + "> is not an administrator!" + timer,
				true);
	}

	@RequestMapping(value = "/fxadmin_list", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getAdministrators(@RequestParam("token") String appVerificationToken,
			@RequestParam("team_domain") String slackTeam) {
		SlackBotTimer timer = new SlackBotTimer();

		Log.info("/fxadmin_list");
		timer.capture();
		List<Role> roles = roleService.getAllAdmins();
		String messageText = "";
		if (roles.isEmpty())
			messageText = "There are no admins!";
		else {

			messageText = "List of Admins: \n";

			timer.capture();

			for (Role role : roles)
				messageText += "< @" + slackApiAccessService.getUser(role.getSlackUser().getSlackUserId()).getName()
						+ "|" + slackApiAccessService.getUser(role.getSlackUser().getSlackUserId()).getName() + "> \n";

			messageText = (roles.size() > 0) ? messageText : "";

			timer.capture();
		}

		return newResponseEntity("/fxadmin_list :" + " \n" + messageText + timer, true);
	}

	@RequestMapping(value = "/fxadmin_param", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> param(@RequestParam("user_id") String userId,
			@RequestParam("text") String arguments) {
		SlackBotTimer timer = new SlackBotTimer();

		if (!isAdmin(userId))
			return newResponseEntity("/fxadmin_param " + arguments + " \n " + "You are not an admin!" + timer);
		timer.capture();
		Parameter param = parameters.get(arguments.split(" ")[0]);
		param.setValue(arguments.split(" ")[1]);
		parameters.save(param);

		timer.capture();

		return newResponseEntity("/fxadmin_param :" + " \n" + "Parameter edited correctly!" + timer, true); // TODO
																											// change
																											// OK!!
	}

	@RequestMapping(value = "/fxadmin_list_params", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> listParams(@RequestParam("user_id") String userId) {
		SlackBotTimer timer = new SlackBotTimer();

		if (!isAdmin(userId))
			return newResponseEntity("/fxadmin_list_params : You are not an admin!" + timer);
		timer.capture();
		return newResponseEntity("/fxadmin_list_params :" + " \n" + parameters.getAllAsLines() + timer, true); // TODO
																												// change
																												// OK!!
	}
}
