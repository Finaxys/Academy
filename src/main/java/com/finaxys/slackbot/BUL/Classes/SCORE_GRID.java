package com.finaxys.slackbot.BUL.Classes;

/**
 * Created by inesnefoussi on 3/7/17.
 */
public enum SCORE_GRID {
    JOINED_TRIBUTE(10), SENT_A_REAL_MESSAGE(10), APPRECIATED_MESSAGE(10), WAS_INNOVATIVE(10);

    private int value;

    SCORE_GRID(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
