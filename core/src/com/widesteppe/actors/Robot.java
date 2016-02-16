package com.widesteppe.actors;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.*;
import com.widesteppe.screens.GameScreen;
import com.widesteppe.utils.AssetsLoader;
import com.widesteppe.utils.ClickPoint;
import com.widesteppe.utils.ClickPointGizmo;


public class Robot extends Astronaut{
    private final Animation idle2;
    private final Animation idle3;
    private final Slot faceSlot;
    private final int faceSlotIndex;
    private float idleTimer;
    private static final float SCALE = 0.5f;
    public static final String ROBOT_JSON_ADD = "spine/robot/skeleton.json";
    public static final float ROBOT_ANGLE_VEL = 0.09f;
    private Human humanTarget;
    private boolean isRichedPerson;

    public void setReachedPerson(boolean reachedPerson) {
        this.isRichedPerson = reachedPerson;
    }

    public enum EMOTION {
        HAPPY("smile"),
        SAD("sad"),
        CALM("calm");
        private final String name;

        EMOTION(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    public Robot(Crew.MEMBER_INFO info) {
        super(270, SCALE, ROBOT_ANGLE_VEL, ROBOT_JSON_ADD, AssetsLoader.robotSpineAtlas, info);
        idle2 = skeletonData.findAnimation("idle2");
        idle3 = skeletonData.findAnimation("idle3");
        faceSlot = skeleton.findSlot("smile");
        faceSlotIndex = skeletonData.findSlotIndex("smile");
    }

    @Override
    protected void activity(float delta) {

    }

    @Override
    public void idle(float delta) {
            idleTimer += delta;
            if (idleTimer > 10) {
                idleTimer = 0;
                if (MathUtils.randomBoolean()) {
                    mixNewAnim(idle2);
                } else {
                    mixNewAnim(idle3);
                }
            }
    }

    @Override
    public void eat(float delta) {

    }

    @Override
    public void sleep(float delta) {

    }

    @Override
    public void work(float delta) {

    }

    @Override
    protected void walk(float delta) {
        super.walk(delta);
        followHuman();
    }

    @Override
    public void sayMessage(String message) {
        super.sayMessage(message);
        clearFollowPerson();
    }

    private void followHuman() {
        if (humanTarget != null) {
            float angle;
            if (humanTarget.isLookingRight()) {
                angle = humanTarget.getCurrentPositionAngle() + 5;
            } else {
                angle = humanTarget.getCurrentPositionAngle() - 5;

            }
            angle = angle % 360;
            if (angle < 0) {
                angle = 360 + angle;
            }
            if (Math.abs(angle - currentPositionAngle) > 0.5f) {
                goToAngle(angle);
                isRichedPerson = false;
            } else {
                isRichedPerson = true;
                if (humanTarget.isLookingRight()) {
                    setLookingRight(false);
                    checkFlip();
                } else {
                    setLookingRight(true);
                    checkFlip();
                }
            }
        }
    }

    public void checkDestinationPoint(float x, float y) {
        float distanceFromCenter = Vector2.dst(x, y, 0, 0);
        if (distanceFromCenter > GameScreen.TOP_MAX_DIST && distanceFromCenter < GameScreen.WALKING_DIST) {
            if (currentState != AI_STATE.TALKING) {
                goToPoint(x, y);
                clearFollowPerson();
                ClickPointGizmo.getInstance().startNewClick(ClickPoint.CLICK_TYPE.GREEN, x, y);
                resetEmotion();
            }
        } else {
            ClickPointGizmo.getInstance().startNewClick(ClickPoint.CLICK_TYPE.RED, x, y);

        }

    }

    private void resetEmotion() {
        setEmotion(EMOTION.HAPPY);
    }

    private void clearFollowPerson() {
        humanTarget = null;
        isRichedPerson = false;
    }

    @Override
    public void setToIdle() {
        super.setToIdle();
        idleTimer = 0;
        resetEmotion();
    }

    public void goToHuman(Human human) {
        this.humanTarget = human;
        followHuman();
        resetEmotion();
    }

    public boolean isRichedPerson() {
        return isRichedPerson;
    }

    public Human getTargetHuman() {
        return humanTarget;
    }

    public void setEmotion(EMOTION emotion) {
        faceSlot.setAttachment(skeletonData.getDefaultSkin().getAttachment(faceSlotIndex, emotion.getName()));
    }

}
