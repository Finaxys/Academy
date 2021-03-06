package com.finaxys.slackbot.DAL;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DebugMode {
	@Id
	private int id;
	private boolean flag;
	
	public DebugMode() {
		id = 1;
		flag = false;	
	}
	
	public static DebugMode beginDebugMode() {
		return new DebugMode();
	}
	
	public DebugMode enableDebugMode() {
		this.flag = true;
		return this;
	}
	
	public DebugMode disableDebugMode() {
		this.flag = false;
		return this;
	}
	
	public boolean isOnDebugMode() {
		return flag;
	}
	
}
