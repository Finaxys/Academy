package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class QuotesPattern {

	private static Pattern pattern;
	final String quotesPattern;

	private QuotesPattern() {

		quotesPattern = "([^\"]\\S*|\".+?\")\\s*";
		pattern = Pattern.compile(quotesPattern);
	}

	public static Pattern getPattern() {

		if (pattern == null)
			new QuotesPattern();
		return pattern;
	}
}
