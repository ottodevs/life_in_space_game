package com.widesteppe.utils;


import com.badlogic.gdx.math.MathUtils;

public class MainTimer {
    private static double mainTimer = 0;
    public final static float ONE_GAME_HOUR_IN_SEC = 300;
    public static void update(float delta) {
        mainTimer += delta;
    }
    public static int getCurrentGameDay() {
        int currentDay = (int)(mainTimer / ONE_GAME_HOUR_IN_SEC);
        return currentDay;
    }

    public static int getCurrentGameHour(){ //return current hour from 0 to 23
        float currentHour = (float)mainTimer % ONE_GAME_HOUR_IN_SEC;
        currentHour = MathUtils.lerp(0, 24, MathUtils.clamp(currentHour / ONE_GAME_HOUR_IN_SEC, 0f, 0.9999f));
        return (int)currentHour;
    }

    public static int getTimeInSeconds() {
        return (int)mainTimer;
    }

    public static void setTimeInSeconds(int timeInSeconds) {
        MainTimer.mainTimer = timeInSeconds;
    }
}
