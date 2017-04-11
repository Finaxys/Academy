package com.finaxys.slackbot.DAL;

import javax.persistence.*;

import org.apache.log4j.spi.LoggingEvent;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "LOGS")
public class LogEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String logger;
	private String level;
	
	@Column(columnDefinition = "TEXT")
	private String message;

	private Date date;

	public LogEvent(String logger, String level, String message, Date date) {
		super();
		this.logger 	= logger;
		this.level 		= level;
		this.message 	= message;
		this.date 		= date;
	}

	public LogEvent(LoggingEvent event) {
		logger 	= event.getLoggerName();
		level 	= event.getLevel().toString();
		message = event.getMessage().toString();
		date 	= new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogger() {
		return logger;
	}

	public void setLogger(String logger) {
		this.logger = logger;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return level + "\t" + logger + "\t" + message;
	}
}
