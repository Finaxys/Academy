package com.finaxys.slackbot.DAL;

import java.io.Serializable;

import javax.persistence.Id;

public class SlackUser_Event_PK implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer eventIdpk;
	private String slackUserIdpk;
}
