package com.widesteppe.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.spine.*;
import com.widesteppe.screens.GameScreen;
import com.widesteppe.utils.AssetsLoader;
import com.widesteppe.utils.ConsoleWriter;


public abstract class Astronaut {
    protected final SkeletonData skeletonData;
    protected final Skeleton skeleton;
    private final Animation talk;
    protected final float initialAngle;
    private float time;
    private SkeletonRenderer renderer;
    private final Animation walk;
    private final Animation idle1;

    private Animation currentAnim;
    private Animation newAnim;
    protected float currentPositionAngle;

    private float walkDestinationAngle;
    private boolean isLookingRight = true;
    private float mixTime;
    private float walkAngleVelocity;
    private float scale;
    private Crew.MEMBER_INFO info;
    private Container<Label> labelContainer;
    private Container<Label> messageContainer;
    private float labelHeightShift;
    public final static float PERSONAL_AREA_RADIUS = 200;
    private String currentMessage;
    private SkeletonBounds bounds;
    public boolean isDialog;
    public boolean isWaitingForResponce;

    public void forceFinishMessage() {
        messageTimer = MAX_MESSAGE_TIME;
    }


    public enum AI_STATE {
        WALKING,
        IDLE,
        TALKING,
        ACTIVITY
    }

    protected AI_STATE currentState = AI_STATE.IDLE;

    public Astronaut(float initialAngle, float scale, float walkAngleVelocity, String skeletonJsonAddress, TextureAtlas atlas, Crew.MEMBER_INFO info) {
        this.scale = scale;
        this.initialAngle = initialAngle;
        this.currentPositionAngle = initialAngle;
        this.walkAngleVelocity = walkAngleVelocity;
        this.info = info;
        SkeletonJson json = new SkeletonJson(atlas);
        json.setScale(scale);
        skeletonData = json.readSkeletonData(Gdx.files.internal(skeletonJsonAddress));
        skeleton = new Skeleton(skeletonData);
        changePositionAccordingToAngle();
        bounds = new SkeletonBounds();


        walk = skeletonData.findAnimation("walk");
        idle1 = skeletonData.findAnimation("idle");
        talk = skeletonData.findAnimation("talk");

        currentAnim = idle1;

        renderer = new SkeletonRenderer();
        createLabelName();
        createLabelMessage();
        time = MathUtils.random(0f, 3f);
    }

    private void createLabelMessage() {
        Label.LabelStyle ls = new Label.LabelStyle(AssetsLoader.font2, AssetsLoader.blackConsoleColor);
        Label label = new Label(info.getName(), ls);
        labelContainer = new Container<Label>(label);
        labelContainer.setTransform(true);
        labelHeightShift = labelContainer.getPrefHeight() / 2;
    }

    private void createLabelName() {
        Label.LabelStyle ls = new Label.LabelStyle(AssetsLoader.font2, AssetsLoader.greenConsoleColor);
        Label label = new Label("", ls);
        label.setWrap(true);
        label.setFontScale(0.7f);
        messageContainer = new Container<Label>(label);
        messageContainer.setTransform(true);
        messageContainer.getColor().a = 0;
        Sprite sprite = new Sprite(AssetsLoader.guiMessageBoxTex);
        messageContainer.setBackground(new SpriteDrawable(sprite));
        messageContainer.setWidth(sprite.getWidth());
        messageContainer.setHeight(sprite.getHeight());
        label.setFillParent(true);

        messageContainer.align(Align.left);
        messageContainer.pad(20);


    }

    public void update(float delta) {
        updateLabelName();
        time += delta;
        currentAnim.apply(skeleton, time, time, true, null);
        if (newAnim != null) {
            mixTime += delta;
            float MAX_MIX_TIME = 0.2f;
            float alpha = MathUtils.clamp(mixTime / MAX_MIX_TIME, 0, 1);
            newAnim.mix(skeleton, time, time, true, null, alpha);
            if (alpha == 1) {
                mixTime = 0;
                currentAnim = newAnim;
                newAnim = null;
            }
        }

        switch (currentState) {
            case WALKING:
                walk(delta);
                break;
            case IDLE:
                idle(delta);
                break;
            case TALKING:
                talk(delta);
                break;
            case ACTIVITY:
                activity(delta);
                break;

        }
        rotateRootAccordingToCurrentPosition();


        skeleton.updateWorldTransform();
        skeleton.update(delta);
    }

    protected abstract void activity(float delta);

    private float messageTimer;
    private float MAX_MESSAGE_TIME = 4;

    private void talk(float delta) {
        messageTimer += delta;
        Label label = messageContainer.getActor();
        label.setText(ConsoleWriter.generateString(currentMessage, messageTimer, ConsoleWriter.VERY_FAST_TYPE_SPEED));
        if (isWaitingForResponce && label.getText().toString().contains(currentMessage)) {
            messageTimer = MAX_MESSAGE_TIME;
        }
        if (messageTimer > MAX_MESSAGE_TIME && !isWaitingForResponce) {
            messageTimer = 0;
            messageContainer.addAction(Actions.alpha(0, 0.2f));
            setToIdle();
        }
        messageContainer.setPosition(getX() - MathUtils.cosDeg(currentPositionAngle) * getHeight() + MathUtils.sinDeg(currentPositionAngle) * 300,
                getY() - MathUtils.sinDeg(currentPositionAngle) * getHeight() - MathUtils.cosDeg(currentPositionAngle) * 300);
        messageContainer.setRotation(currentPositionAngle + 90);
    }

    private void updateLabelName() {
        labelContainer.setPosition(getX() + MathUtils.cosDeg(currentPositionAngle) * labelHeightShift,
                getY() + MathUtils.sinDeg(currentPositionAngle) * labelHeightShift);
        labelContainer.setRotation(currentPositionAngle + 90);


    }

    private void rotateRootAccordingToCurrentPosition() {
        float rootBoneAngle = currentPositionAngle - 270;
        if (isLookingRight) {
            skeleton.getRootBone().setRotation(rootBoneAngle);
        } else {
            skeleton.getRootBone().setRotation(-rootBoneAngle);
        }
    }

    public abstract void idle(float delta);
    public abstract void eat(float delta);
    public abstract void sleep(float delta);
    public abstract void work(float delta);

    protected void walk(float delta) {
        float angleVelocity = walkAngleVelocity * 60 * delta;
        if (isLookingRight) {
            currentPositionAngle += angleVelocity;
        } else {
            currentPositionAngle -= angleVelocity;
        }
        currentPositionAngle %= 360;
        if (currentPositionAngle < 0) {
            currentPositionAngle = 360 - currentPositionAngle;
        }
        changePositionAccordingToAngle();

        if (Math.abs(currentPositionAngle - walkDestinationAngle) < 0.5f) {
            setToIdle();
        }
    }

    protected void changePositionAccordingToAngle() {
        skeleton.setPosition(MathUtils.cosDeg(currentPositionAngle) * GameScreen.WALKING_DIST, MathUtils.sinDeg(currentPositionAngle) * GameScreen.WALKING_DIST);
    }

    public void setToIdle() {
        currentState = AI_STATE.IDLE;
        mixNewAnim(idle1);
        changePositionAccordingToAngle();
    }

    public void render(PolygonSpriteBatch batch) {
        renderer.draw(batch, skeleton);
    }

    public float getX() {
        return skeleton.getX();
    }

    public float getY() {
        return skeleton.getY();
    }

    public float getHeight() {
        return skeletonData.getHeight() * scale;
    }


    public void goToPoint(float x, float y) {
        if (currentState == AI_STATE.TALKING) return;
        float angle = (float) Math.atan2(y, x) * MathUtils.radiansToDegrees;
        if (angle < 0) {
            angle = 360 + angle;
        }
        goToAngle(angle);

    }

    public void goToAngle(float angle) {
        if (currentState == AI_STATE.TALKING) return;
        walkDestinationAngle = angle;
        currentState = AI_STATE.WALKING;
        mixNewAnim(walk);

        if ((walkDestinationAngle - currentPositionAngle) > 0) {
            isLookingRight = Math.abs(walkDestinationAngle - currentPositionAngle) <= 180;
        } else {
            isLookingRight = Math.abs(walkDestinationAngle - currentPositionAngle) > 180;
        }
        checkFlip();
    }

    protected void mixNewAnim(Animation newAnim) {
        if (this.newAnim != null) currentAnim = this.newAnim;
        this.newAnim = newAnim;
        mixTime = 0;
    }

    protected void checkFlip() {
        if (isLookingRight) {
            skeleton.setFlipX(false);
        } else {
            skeleton.setFlipX(true);
        }
    }

    public void sayMessage(String message) {
        if (currentState != AI_STATE.TALKING) {
            currentState = AI_STATE.TALKING;
            mixNewAnim(talk);
            currentMessage = message;
            MAX_MESSAGE_TIME = (float) (currentMessage.length()) * 0.15f;
            messageContainer.addAction(Actions.alpha(1, 0.2f));
            changePositionAccordingToAngle();
        }

    }

    public boolean isContains(float x, float y) {
        bounds.update(skeleton, true);
        return bounds.aabbContainsPoint(x, y);
    }

    public float getCurrentPositionAngle() {
        return currentPositionAngle;
    }

    public boolean isLookingRight() {
        return isLookingRight;
    }

    public AI_STATE getCurrentState() {
        return currentState;
    }

    public Crew.MEMBER_INFO getInfo() {
        return info;
    }

    public Container<Label> getLabelContainer() {
        return labelContainer;
    }

    public Container<Label> getMessageContainer() {
        return messageContainer;
    }

    public float getCenterX() {
        return getX() + MathUtils.cosDeg(currentPositionAngle) * getHeight() / 2;
    }

    public float getCenterY() {
        return getY() + MathUtils.sinDeg(currentPositionAngle) * getHeight() / 2;
    }

    public void setCurrentState(AI_STATE currentState) {
        this.currentState = currentState;
    }

    public void setLookingRight(boolean isLookingRight) {
        this.isLookingRight = isLookingRight;
    }
}
