package com.finaxys.slackbot.Utilities;

import java.util.ArrayList;
import java.util.List;

public class ArgumentsSplitter 
{
	private String userName;
	private String userId;
	private String eventType;
	
	private List<String> integers;
	private List<String> otherStrings;

	private String eventDate;
	
	public ArgumentsVerifier verifier;
	
	public ArgumentsSplitter(String arguments, String command)
	{
		otherStrings = new ArrayList<String>();
		integers = new ArrayList<String>();
		
		verifier = new ArgumentsVerifier();
		ArgumentsReader reader = new ArgumentsReader();
		System.out.println("---------------DEBUG 1.1 -------------------");

		if (verifier.Verify(arguments, command))
		{
			System.out.println("---------------DEBUG 1.2 -------------------");

			reader = verifier.reader;
						
			if (reader.parse(arguments) == null)
				return;
			System.out.println("---------------DEBUG 1.3 -------------------");

			for (Token token : reader.parse(arguments))
			{
				System.out.println(token.type);
				
				if (token.type.equals("UserId"))
				{					
					 userId   = token.value.split("\\|")[0].split("<@")[1];
	                 userName = token.value.split("\\|")[1].split(">")[0];
					System.out.println("---------------DEBUG 1.3 -------------------");
					System.out.println("UserId = "+userId+" userName ="+userName);

				}
				else if (token.type.equals("Integer"))
				{	
					integers.add(token.value);
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
			System.out.println("---------------DEBUG 1.4 -------------------");
		}
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public String getIntegers(int position) {
		return integers.get(position);
	}
	
	public String getEventType() {
		return eventType;
	}
	
	public String getEventDate() {
		return eventDate;
	}

	public String getString(int position) {
		return otherStrings.get(position);
	}
}
