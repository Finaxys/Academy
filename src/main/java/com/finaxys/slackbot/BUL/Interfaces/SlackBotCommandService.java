package com.finaxys.slackbot.BUL.Interfaces;

import java.util.List;

import com.finaxys.slackbot.Domains.FinaxysProfile;

public interface SlackBotCommandService {

	List<FinaxysProfile> listerUsers(int number);
}
