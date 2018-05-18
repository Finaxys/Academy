package com.finaxys.slackbot.BUL.Matchers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.finaxys.slackbot.BUL.Patterns.QuotesPattern;

public class QuotesMatcher {
	private Matcher matcher;
	private List<Pattern> quotesPatterns;
	private Pattern quotesPattern;

	public QuotesMatcher() {
    	
		quotesPatterns 	= new ArrayList<>();
		quotesPatterns.add(QuotesPattern.getPattern());
		quotesPattern 		= QuotesPattern.getPattern();
    }

	public boolean isCorrect(String message) {

		for (Pattern pattern : quotesPatterns)
			if (!pattern.matcher(message).matches())
				return false;

		return true;
	}

	public String getQuotesArgument(String message) {

		matcher = quotesPattern.matcher(message);

		return matcher.find() ? matcher.group().substring(1) : "";
	}

}
