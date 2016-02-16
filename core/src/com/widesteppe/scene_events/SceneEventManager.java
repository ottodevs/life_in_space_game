package com.widesteppe.scene_events;


import com.widesteppe.screens.GameScreen;

import java.util.ArrayList;

public class SceneEventManager {
    private GameScreen gameScreen;
    private ArrayList<SceneEvenListener> events = new ArrayList<SceneEvenListener>();
    public SceneEventManager(GameScreen gs) {
        this.gameScreen = gs;
        createEvents();

    }

    private void createEvents() {
        StarBoomEvent starBoomEvent = new StarBoomEvent(gameScreen.getDirector().getQuest());
        events.add(starBoomEvent);
    }

    public void update(float delta){
        for (SceneEvenListener event : events) {
            if (event.checkTrigger()) {
                event.update(delta);
            }
        }
    }
}
