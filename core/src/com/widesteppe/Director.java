package com.widesteppe;

import com.widesteppe.actors.Astronaut;
import com.widesteppe.actors.Crew;
import com.widesteppe.actors.Human;
import com.widesteppe.utils.MainTimer;
import com.widesteppe.utils.MyPrefs;

public class Director {
    public static final int SLEEP_DURATION = 6;
    public static final int WORK_DURATION = 6;
    private Crew crew;
    private Quest quest;

    public Quest getQuest() {
        return quest;
    }

    public enum SCHEDULE {
        FIRST_SHIFT(16, 0, 8),
        SECOND_SHIFT(10, 18, 2);
        private float sleepTimeStart;
        private float firstHalfWorkStart;
        private float secondHalfWorkStart;

        SCHEDULE(float sleepTimeStart, float firstHalfWorkStart, float secondHalfWorkStart) {
            this.sleepTimeStart = sleepTimeStart;
            this.firstHalfWorkStart = firstHalfWorkStart;
            this.secondHalfWorkStart = secondHalfWorkStart;
        }

        public float getSleepTimeStart() {
            return sleepTimeStart;
        }

        public float getFirstHalfWorkStart() {
            return firstHalfWorkStart;
        }

        public float getSecondHalfWorkStart() {
            return secondHalfWorkStart;
        }
    }

    public Director(Crew crew) {
        this.crew = crew;
        quest = new Quest(this, MyPrefs.getInstance().getMissionId());

    }

    public void update(float delta) {
        updateSchedule();
        quest.update(delta);
    }

    private void updateSchedule() {
        for (Astronaut astronaut : crew.getMembers()) {
            if (astronaut.getInfo().getRole() == Crew.ROLE.ROBOT) continue;
            if (astronaut.isDialog) {
                continue;
            }
            Human human = (Human) astronaut;
            if (human.getCurrentState() == Astronaut.AI_STATE.WALKING) continue;
            switch (human.getInfo().getSchedule()) {
                case FIRST_SHIFT: {
                    generateTargetActivity(SCHEDULE.FIRST_SHIFT, human);
                    break;
                }
                case SECOND_SHIFT: {
                    generateTargetActivity(SCHEDULE.SECOND_SHIFT, human);
                    break;
                }
            }
        }
    }

    private void generateTargetActivity(SCHEDULE shift, Human human) {
        human.setCurrentState(Astronaut.AI_STATE.ACTIVITY);
        int hour = MainTimer.getCurrentGameHour();
        Human.ACTIVITY targetActivity;
        if (hour >= shift.getSleepTimeStart() && hour < shift.getSleepTimeStart() + SLEEP_DURATION) {
            targetActivity = Human.ACTIVITY.SLEEP;
        } else if ((hour >= shift.getFirstHalfWorkStart() && hour < shift.getFirstHalfWorkStart() + WORK_DURATION)
                || (hour >= shift.getSecondHalfWorkStart() && hour < shift.getSecondHalfWorkStart() + WORK_DURATION)) {
            targetActivity = Human.ACTIVITY.WORK;
        } else {
            targetActivity = Human.ACTIVITY.EAT;
        }
        human.setTargetActivity(targetActivity);
    }

    public Crew getCrew() {
        return crew;
    }
}
