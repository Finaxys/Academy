package com.finaxys.slackbot.Utilities;

import java.util.ArrayList;
import java.util.List;

public class Timer {
	
	private long startTime;
	private long lastCheckTime;
	private List<Long> captures;
	
	public Timer() {
		start();
	}

	public void start() {
		startTime = System.currentTimeMillis();
		lastCheckTime = startTime;
		captures = new ArrayList<>();

	}

	public void capture() {
		long now = System.currentTimeMillis();
		captures.add((now - lastCheckTime));
		lastCheckTime = now;
	}

	@Override
	public String toString() {
		capture();
		StringBuilder sb = new StringBuilder();
		int numberOfCapture = 1;
		sb.append("\n");
		for (Long time : captures) {
			sb.append("(" + numberOfCapture++ + " : " + time + " ms ), ");
		}

		sb.append("Total : " + captures.stream().mapToLong(e -> e).sum() + " ms");
		return sb.toString();
	}

}
