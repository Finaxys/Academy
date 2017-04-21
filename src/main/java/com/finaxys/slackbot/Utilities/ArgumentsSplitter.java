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
						
			if (reader.parse(arguments) == null)
				return;
			
			for (Token token : reader.parse(arguments))
			{
				if (token.type.equals("UserId"))
				{					
					userId   = token.value.split("\\|")[0].split("<@")[1];
					userName = token.value.split("\\|")[1].split(">")[0];
				}
				else if (token.type.equals("Integer"))
				{					
					nbOfPts = token.value;
				}
				else if (token.type.equals("Type"))
				{					
					eventType = token.value;
				}
				else if (token.type.equals("String"))
				{					
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
	
	public String getEventType()
	{
		return eventType;
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
