package com.finaxys.slackbot.BUL.Classes;

public enum SCORE_GRID 
{
    JOINED_TRIBUTE(10), SENT_A_REAL_MESSAGE(2), APPRECIATED_MESSAGE(10), IS_INNOVATIVE(10);

    private int value;

    SCORE_GRID(int value) 
    {
        this.value = value;
    }

    public int value() 
    {
        return this.value;
    }
}
