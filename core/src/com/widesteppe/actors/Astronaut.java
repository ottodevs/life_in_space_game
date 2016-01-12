package com.widesteppe.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.*;
import com.widesteppe.screens.GameScreen;
import com.widesteppe.utils.AssetsLoader;


public abstract class Astronaut {
    protected final SkeletonData skeletonData;
    protected final Skeleton skeleton;
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


    public enum AI_STATE {
        WALKING,
        IDLE,
        TALKING
    }

    protected AI_STATE currentRobotState = AI_STATE.IDLE;

    public Astronaut(float initialAngle, float scale, float walkAngleVelocity, String skeletonJsonAddress, TextureAtlas atlas) {
        this.scale = scale;
        this.currentPositionAngle = initialAngle;
        this.walkAngleVelocity = walkAngleVelocity;
        SkeletonJson json = new SkeletonJson(atlas);
        json.setScale(scale);
        skeletonData = json.readSkeletonData(Gdx.files.internal(skeletonJsonAddress));
        skeleton = new Skeleton(skeletonData);
        changePositionAccordingToAngle();


        walk = skeletonData.findAnimation("walk");
        idle1 = skeletonData.findAnimation("idle");

        currentAnim = idle1;

        renderer = new SkeletonRenderer();
    }

    public void update(float delta) {
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

        switch (currentRobotState) {
            case WALKING:
                walk(delta);
                break;
            case IDLE:
                idle(delta);
                break;
            case TALKING:
                break;

        }
        rotateRootAccordingToCurrentPosition();



        // walkAround(delta);
        skeleton.updateWorldTransform();
        skeleton.update(delta);
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

    private void walk(float delta) {
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

        if (Math.abs(currentPositionAngle - walkDestinationAngle) < 0.2f) {
            setToIdle();
        }
    }

    private void changePositionAccordingToAngle() {
        skeleton.setPosition(MathUtils.cosDeg(currentPositionAngle) * GameScreen.WALKING_DIST, MathUtils.sinDeg(currentPositionAngle) * GameScreen.WALKING_DIST);
    }

    protected void setToIdle() {
        currentRobotState = AI_STATE.IDLE;
        mixNewAnim(idle1);
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
        currentRobotState = AI_STATE.WALKING;
        mixNewAnim(walk);
        walkDestinationAngle = (float) Math.atan2(y, x) * MathUtils.radiansToDegrees;
        if (walkDestinationAngle < 0) {
            walkDestinationAngle = 360 + walkDestinationAngle;
        }
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

    private void checkFlip() {
        if (isLookingRight) {
            skeleton.setFlipX(false);
        } else {
            skeleton.setFlipX(true);
        }
    }

    public float getCurrentPositionAngle() {
        return currentPositionAngle;
    }

    public boolean isLookingRight() {
        return isLookingRight;
    }

    public AI_STATE getCurrentRobotState() {
        return currentRobotState;
    }
}
