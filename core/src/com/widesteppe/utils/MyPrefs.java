package com.widesteppe.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class MyPrefs {
    private static MyPrefs instance;
    private Preferences prefs;
    private final String MISSION_ID_KEY = "mission_id";
    private final String TIMER_KEY = "timer_value";
    private final String IS_GAME_OVER_KEY = "game_over";
    private MyPrefs () {
        prefs = Gdx.app.getPreferences("My prefs");
    }
    public static MyPrefs getInstance(){
        if (instance == null) {
            instance = new MyPrefs();
        }
        return instance;
    }

    public void saveProgress(int missionId, int timerValue) {
        prefs.putInteger(MISSION_ID_KEY, missionId);
        prefs.putInteger(TIMER_KEY, timerValue);
        prefs.flush();
    }
    public void saveGameOver(){
        prefs.putBoolean(IS_GAME_OVER_KEY, true);
        prefs.flush();
    }
    public boolean isGameOver(){
        return prefs.getBoolean(IS_GAME_OVER_KEY, false);
    }
    public int getMissionId(){
        return prefs.getInteger(MISSION_ID_KEY, 0);
    }

    public int getTimerValue(){
        return prefs.getInteger(TIMER_KEY, 0);
    }

    public void clearProgress(){
        prefs.clear();
        prefs.flush();
    }


}
