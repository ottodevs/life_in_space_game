package com.widesteppe.scene_events;


import com.widesteppe.Quest;
import com.widesteppe.utils.StarGenerator;

public class StarBoomEvent implements SceneEvenListener {
    private boolean isEnabled = true;
    private Quest quest;

    public StarBoomEvent(Quest quest) {
        this.quest = quest;
    }

    @Override
    public boolean checkTrigger() {
        if (isEnabled) {
            if (quest.getId() == 4) {
                if (quest.getCurrentReplicI() == 16) {
                    StarGenerator.boom();
                    isEnabled = false;
                }
            }
        }
        return false;
    }

    @Override
    public void update(float delta) {    }
}
