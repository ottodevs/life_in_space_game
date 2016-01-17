package com.widesteppe.actors;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.Slot;
import com.widesteppe.Director;
import com.widesteppe.screens.GameScreen;
import com.widesteppe.utils.AssetsLoader;
import com.widesteppe.utils.MainTimer;


public class Human extends Astronaut {
    private static final float SCALE = 1f;
    public static final String HUMAN_JSON_ADD = "spine/crew/skeleton.json";
    public static final float HUMAN_ANGLE_VELOCITY = 0.09f;
    private final Animation runAnim;
    private final Animation microAnim;
    private final Animation tool1Anim;
    private final Animation tool2Anim;
    private Slot faceSlot;
    private Slot hairSlot;
    private int hairSlotIndex;
    private Skin defaultSkin;
    private ACTIVITY currentActivity = ACTIVITY.NONE;
    private ACTIVITY targetActivity;
    private Animation eatAnim;
    private Animation sleepAnim;
    private Animation workWithTabletAnim;
    private JOB currentJob = JOB.NONE;
    private int faceSlotIndex;
    private Slot toolSlot;
    private int toolSlotIndex;
    private float gotoSleepTimer;
    private final float GOTO_SLEEP_MAX_TIME = 0.2f;
    private float currentWorkPlaceAngle;
    public void setTargetActivity(ACTIVITY targetActivity) {
        this.targetActivity = targetActivity;
    }

    public enum SKIN_NAMES {
        captain,
        clara,
        sofia,
        mech,
        scout,
        scientist
    }

    public enum JOB {
        TABLET,
        RUN,
        MICRO,
        TOOL1,
        TOOL2,
        NONE
    }

    public enum HAIR {
        CLARA_HAIR("clara_hair"),
        SOFIA_HAIR("sofia_hair"),
        HAIR1("hair1"),
        HAIR2("hair2"),
        HAIR3("hair3"),
        HAIR4("hair4"),
        HAIR5("hair5"),
        HAIR6("hair6"),
        HAIR7("hair7"),
        HAIR8("hair8"),
        HAIR9("hair9"),
        HAIR10("hair10");
        private final String name;

        HAIR(String name) {
            String prefix = "head_and_smile/";
            this.name = prefix + name;
        }

        public String getName() {
            return name;
        }
    }

    public enum EMOTION {
        SLEEP("sleep"),
        HAPPY("happy"),
        SAD("sad"),
        SURPRISED("surprised"),
        TIRED("tired"),
        ANGRY("angry");
        private final String name;

        EMOTION(String name) {
            String prefix = "head_and_smile/";
            String suffix = "_face";
            this.name = prefix + name + suffix;
        }

        public String getName() {
            return name;
        }
    }

    @Override
    public void setToIdle() {
        super.setToIdle();
        resetToolAndEmotion();
    }

    public enum TOOL {
        FOOD("food"),
        FIRE("tools/fire"),
        TABLET("tools/tablet"),
        TOOL1("tools/tool1"),
        TOOL2("tools/tool2"),
        NONE("");
        private final String name;

        TOOL(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


    public enum ACTIVITY {
        WORK,
        SLEEP,
        EAT,
        NONE
    }


    public Human(Crew.MEMBER_INFO info, float initialAngle) {
        super(initialAngle, SCALE, HUMAN_ANGLE_VELOCITY, HUMAN_JSON_ADD, AssetsLoader.crewSpineAtlas, info);
        initializeSkeleton(info);
        eatAnim = skeletonData.findAnimation("eat");
        workWithTabletAnim = skeletonData.findAnimation("work_with_tablet");
        sleepAnim = skeletonData.findAnimation("sleep");
        runAnim = skeletonData.findAnimation("run");
        tool1Anim = skeletonData.findAnimation("work_tool1");
        tool2Anim = skeletonData.findAnimation("work_tool2");
        microAnim = skeletonData.findAnimation("look_to_micro");


    }

    private void initializeSkeleton(Crew.MEMBER_INFO info) {
        faceSlot = skeleton.findSlot("face");
        faceSlotIndex = skeletonData.findSlotIndex("face");
        hairSlot = skeleton.findSlot("hair");
        hairSlotIndex = skeletonData.findSlotIndex("hair");
        toolSlot = skeleton.findSlot("tool");
        toolSlotIndex = skeletonData.findSlotIndex("tool");
        defaultSkin = skeletonData.getDefaultSkin();
        switch (info.getRole()) {
            case CAPTAIN: {
                skeleton.setSkin(SKIN_NAMES.captain.toString());
                faceSlot.setAttachment(null);
                break;
            }
            case MECHANIC: {
                skeleton.setSkin(SKIN_NAMES.mech.toString());
                switch (info) {
                    case MARIO: {
                        setHairAttachment(HAIR.HAIR9.getName());
                        break;
                    }
                    case DAVID: {
                        setHairAttachment(HAIR.HAIR5.getName());
                        break;
                    }
                    case DANIEL: {
                        setHairAttachment(HAIR.HAIR3.getName());
                        break;
                    }
                    case AVI: {
                        setHairAttachment(HAIR.HAIR8.getName());
                        break;
                    }
                }
                break;
            }
            case SCIENTIST: {
                skeleton.setSkin(SKIN_NAMES.scientist.toString());
                switch (info) {
                    case CLARA: {
                        skeleton.setSkin(SKIN_NAMES.clara.toString());
                        setHairAttachment(HAIR.CLARA_HAIR.getName());
                        break;
                    }
                    case SOFIA: {
                        skeleton.setSkin(SKIN_NAMES.sofia.toString());
                        setHairAttachment(HAIR.SOFIA_HAIR.getName());
                        break;
                    }
                    case SEB: {
                        setHairAttachment(HAIR.HAIR1.getName());
                        break;
                    }
                    case MICHAEL: {
                        setHairAttachment(HAIR.HAIR10.getName());
                        break;
                    }
                }
                break;
            }
            case SCOUT: {
                skeleton.setSkin(SKIN_NAMES.scout.toString());
                switch (info) {
                    case IVAN: {
                        setHairAttachment(HAIR.HAIR4.getName());
                        break;
                    }
                    case GAVIN: {
                        setHairAttachment(HAIR.HAIR6.getName());
                        break;
                    }
                    case PATRICK: {
                        setHairAttachment(HAIR.HAIR2.getName());
                        break;
                    }
                    case ISAAC: {
                        setHairAttachment(HAIR.HAIR7.getName());
                        break;
                    }
                }
                break;
            }
        }
        skeleton.findSlot("tool").setAttachment(null);
    }

    private void setHairAttachment(String attachmentName) {
        hairSlot.setAttachment(defaultSkin.getAttachment(hairSlotIndex, attachmentName));
    }

    @Override
    public void idle(float delta) {

    }

    @Override
    public void eat(float delta) {

    }

    @Override
    public void sleep(float delta) {
        float pos;

        if (getInfo().isSleepOnTop()) {
            pos = GameScreen.WALKING_DIST - 190;
        } else {
            pos = GameScreen.WALKING_DIST - 70;
        }

        skeleton.setPosition(MathUtils.cosDeg(currentPositionAngle) * pos, MathUtils.sinDeg(currentPositionAngle) * pos);


    }

    @Override
    public void work(float delta) {

    }

    public void checkDestinationPoint(float x, float y) {
        float distanceFromCenter = Vector2.dst(x, y, 0, 0);
        if (distanceFromCenter > GameScreen.TOP_MAX_DIST && distanceFromCenter < GameScreen.WALKING_DIST) {
            if (currentState != AI_STATE.TALKING) {
                goToPoint(x, y);
            }
        }
    }

    public ACTIVITY getCurrentActivity() {
        return currentActivity;
    }


    @Override
    protected void activity(float delta) {
        if (targetActivity != currentActivity) {
            updateActivity();
        }
        switch (currentActivity) {
            case SLEEP:
                sleep(delta);
                break;
            case EAT:
                eat(delta);
                break;
            case WORK:
                work(delta);
                break;

        }
    }


    private void updateActivity() {
        switch (targetActivity) {
            case SLEEP: {
                if (Math.abs(currentPositionAngle - getInfo().getSleepPlaceAngle()) < 0.5f) {
                    setToSleep();
                } else {
                    resetToolAndEmotion();
                    goToAngle(getInfo().getSleepPlaceAngle());
                    currentActivity = ACTIVITY.NONE;
                }
                break;
            }
            case EAT: {
                if (Math.abs(currentPositionAngle - getInfo().getEatPlaceAngle()) < 0.5f) {
                    setToEat();
                } else {
                    resetToolAndEmotion();
                    goToAngle(getInfo().getEatPlaceAngle());
                    currentActivity = ACTIVITY.NONE;
                }
                break;
            }
            case WORK: {
                if (Math.abs(currentPositionAngle - currentWorkPlaceAngle) < 0.5f) {
                    setToWork();
                } else {
                    resetToolAndEmotion();
                    if (isFirstHalfOfShift()) {
                        goToAngle(getInfo().getWorkPlaceAngle());
                        currentWorkPlaceAngle = getInfo().getWorkPlaceAngle();
                    } else {
                        goToAngle(getInfo().getWorkPlaceAngle2());
                        currentWorkPlaceAngle = getInfo().getWorkPlaceAngle2();

                    }
                    currentActivity = ACTIVITY.NONE;
                }
                break;
            }
        }
    }

    private void resetToolAndEmotion() {
        setEmotion(EMOTION.HAPPY);
        setTool(TOOL.NONE);
    }

    private void setToWork() {
        currentActivity = ACTIVITY.WORK;

        if (getInfo() == Crew.MEMBER_INFO.DAVID || getInfo() == Crew.MEMBER_INFO.DANIEL) {
            if (MathUtils.randomBoolean()) {
                mixNewAnim(tool1Anim);
                currentJob = JOB.TOOL1;
                setTool(TOOL.TOOL1);
            } else {
                mixNewAnim(tool2Anim);
                currentJob = JOB.TOOL2;
                setTool(TOOL.TOOL2);
            }
        } else if (isFirstHalfOfShift() && getInfo().getRole() == Crew.ROLE.SCOUT) {
            mixNewAnim(runAnim);
            currentJob = JOB.RUN;
            setTool(TOOL.NONE);
            setLookingRight(false);
            checkFlip();
        } else if (isFirstHalfOfShift() && getInfo().getRole() == Crew.ROLE.SCIENTIST) {
            mixNewAnim(microAnim);
            currentJob = JOB.MICRO;
            setTool(TOOL.NONE);
        }
        else {
            mixNewAnim(workWithTabletAnim);
            currentJob = JOB.TABLET;
            setTool(TOOL.TABLET);
        }
    }

    private void setToEat() {
        currentActivity = ACTIVITY.EAT;
        mixNewAnim(eatAnim);
        setTool(TOOL.FOOD);
    }

    private void setToSleep() {
        currentActivity = ACTIVITY.SLEEP;
        gotoSleepTimer = 0;
        mixNewAnim(sleepAnim);
        if (getInfo().getRole() != Crew.ROLE.CAPTAIN) {
            setEmotion(EMOTION.SLEEP);
        }
    }

    public void setEmotion(EMOTION emotion) {
        if (getInfo() != Crew.MEMBER_INFO.JACK) {
            faceSlot.setAttachment(defaultSkin.getAttachment(faceSlotIndex, emotion.getName()));
        }
    }

    public void setTool(TOOL tool) {
        if (tool == TOOL.NONE) {
            toolSlot.setAttachment(null);
        } else {
            toolSlot.setAttachment(defaultSkin.getAttachment(toolSlotIndex, tool.getName()));
        }

    }

    private boolean isFirstHalfOfShift(){
        if (getInfo().getSchedule() == Director.SCHEDULE.FIRST_SHIFT) {
            return MainTimer.getCurrentGameHour() < getInfo().getSchedule().getSecondHalfWorkStart();
        } else {
            return MainTimer.getCurrentGameHour() > getInfo().getSchedule().getSecondHalfWorkStart() + Director.WORK_DURATION;
        }
    }

    public JOB getCurrentJob() {
        return currentJob;
    }

    public void setCurrentActivity(ACTIVITY currentActivity) {
        this.currentActivity = currentActivity;
    }
}
