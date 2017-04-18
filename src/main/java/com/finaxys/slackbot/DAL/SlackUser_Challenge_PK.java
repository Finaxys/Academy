package com.finaxys.slackbot.DAL;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class SlackUser_Challenge_PK implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer challenge;
	private String finaxysProfile;

	public SlackUser_Challenge_PK() {

	}

	@Column(name = "CHALLENGE_ID")
	public Integer getChallenge() {
		return challenge;
	}

	public void setChallenge(Integer challenge) {
		this.challenge = challenge;
	}

	@Column(name = "FINAXYSPROFILE_ID")
	public String getFinaxysProfile() {
		return finaxysProfile;
	}

	public void setFinaxysProfile(String finaxysProfile) {
		this.finaxysProfile = finaxysProfile;
	}
}
