package com.finaxys.slackbot.Utilities;

/**
 * Created by user on 21/03/2017.
 */
public class Timer {
    private static long _startTime;
    private static long _lastCheckTime;

    public static void start() {
        _startTime     = System.currentTimeMillis();
        _lastCheckTime = _startTime;
    }
    public static void elapsed(String logPrefix) {
        long now = System.currentTimeMillis();
        System.out.println(logPrefix + (now-_startTime)+"ms (+"+(now-_lastCheckTime)+"ms)");
        _lastCheckTime = now;
    }
}
