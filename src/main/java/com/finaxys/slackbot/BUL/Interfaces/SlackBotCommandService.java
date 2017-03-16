package com.finaxys.slackbot.BUL.Interfaces;

import com.finaxys.slackbot.Domains.FinaxysProfile;

import java.util.List;

public interface SlackBotCommandService {

    List<FinaxysProfile> listerUsers();
    List<FinaxysProfile> listeScores(int n);

}
