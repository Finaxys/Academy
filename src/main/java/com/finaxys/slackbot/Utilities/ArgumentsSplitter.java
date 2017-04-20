package com.finaxys.slackbot.Utilities;

import java.util.ArrayList;
import java.util.List;

public class ArgumentsSplitter 
{
	private String userName;
	private String userId;
	private String nbOfPts;
	private String eventType;
	
	private List<String> otherStrings;
	
	private String eventName;
	private String eventDescription;
	private String eventDate;
	
	public ArgumentsVerifier verifier;
	
	public ArgumentsSplitter(String arguments, String command)
	{
		otherStrings = new ArrayList<String>();
		
		verifier = new ArgumentsVerifier();
		
		ArgumentsReader reader = new ArgumentsReader();
		
		if (verifier.Verify(arguments, command))
		{
			reader = verifier.reader;
			
			System.out.println(reader);
			
			if (reader.parse(arguments) == null)
				return;
			
			for (Token token : reader.parse(arguments))
			{
				if (token.type.equals("UserId"))
				{
					System.out.println(token.value + " UserId");
					
					userId   = token.value.split("\\|")[0].split("<@")[1];
					userName = token.value.split("\\|")[1].split(">")[0];
				}
				else if (token.type.equals("Integer"))
				{
					System.out.println(token.value + " nbOfPts");
					
					nbOfPts = token.value;
				}
				else if (token.type.equals("Type"))
				{
					System.out.println(token.value + " Type");
					
					eventType = token.value;
				}
				else if (token.type.equals("String"))
				{
					System.out.println(token.value + " String");
					
					otherStrings.add(token.value);
				}
			}
		}
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public String getUserId()
	{
		return userId;
	}
	
	public String getNbOfPts()
	{
		return nbOfPts;
	}
	
	public String getEventName()
	{
		return eventName;
	}
	
	public String getEventType()
	{
		return eventType;
	}
	
	public String getEventDescription()
	{
		return eventDescription;
	}
	
	public String getEventDate()
	{
		return eventDate;
	}

	public String getString(int position)
	{
		return otherStrings.get(position);
	}
}
