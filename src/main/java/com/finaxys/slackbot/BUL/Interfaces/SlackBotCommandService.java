package com.finaxys.slackbot.BUL.Interfaces;

import com.finaxys.slackbot.DAL.SlackUser;

import java.util.List;

public interface SlackBotCommandService {

    List<SlackUser> listerUsers();
    List<SlackUser> listeScores(int n);

}
