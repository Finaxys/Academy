package com.finaxys.slackbot.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentsReader
{
	public ArgumentsReader()
	{
	}
	
	public List<Token> parse(String arguments)
	{
		if (arguments == null)
			return null;
		
		List<Token> argsParsed = new ArrayList<Token>();
		
		Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
		Matcher regexMatcher = regex.matcher(arguments);
		
		while (regexMatcher.find())
		{
			String type  = "";
			String value = "";
			
			if (regexMatcher.group(1) != null)
				value = regexMatcher.group(1);
			else if (regexMatcher.group(2) != null)
				value = regexMatcher.group(2);
			else
				value = regexMatcher.group();
			
			if (value.contains("<@"))
			{
				type = "UserId";
			}
			else if (isInteger(value))
			{
				type = "Integer";
			}
			else if (value.equals("group") || value.equals("individual"))
			{
				type = "Type";
			}
			else
			{
				type = "String";
			}
			
			argsParsed.add(new Token(type, value));
		}
		
		if (argsParsed.isEmpty())
			return null;
		
		return argsParsed;
	}
	
	public static boolean isInteger(String str) 
	{
	    if (str == null) 
	    {
	        return false;
	    }
	    
	    int length = str.length();
	    if (length == 0) 
	    {
	        return false;
	    }
	    
	    int i = 0;
	    if (str.charAt(0) == '-')
	    {
	        if (length == 1) 
	        {
	            return false;
	        }
	        i = 1;
	    }
	    
	    for (; i < length; i++) 
	    {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') 
	        {
	            return false;
	        }
	    }
	    
	    return true;
	}
}