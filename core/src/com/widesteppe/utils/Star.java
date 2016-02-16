package com.widesteppe.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


public class Star {
    private Sprite sprite;
    private float initialX;
    private float initialY;
    public static final float ANGLE_ROTATION_VELOCITY = 0.1f;
    public boolean isBoom;
    private float boomTimer;
    private final float MAX_BOOM_TIME = 4;
    private float initialRadius;
    private float initialAngle;
    private boolean shouldPaused;
    private float pauseTime;

    public Star(float physWorldWidth) {
        createSprite(physWorldWidth);
    }

    private void createSprite(float physWorldWidth) {
        sprite = new Sprite(AssetsLoader.starTex);
        float randomSize = MathUtils.random(4, 8);
        sprite.setSize(randomSize, randomSize);
        sprite.setScale(MathUtils.random(0.95f, 1.05f), MathUtils.random(0.95f, 1.05f));
        initialX = MathUtils.random(-physWorldWidth * 0.6f, physWorldWidth * 0.6f);
        initialY = MathUtils.random(-physWorldWidth * 0.6f, physWorldWidth * 0.6f);
        initialRadius = (float) Math.sqrt(initialX * initialX + initialY * initialY);
        initialAngle = MathUtils.atan2(initialY, initialX) * MathUtils.radDeg;
        if (initialAngle < 0) initialAngle += 360;
        sprite.setOrigin(sprite.getWidth() / 2 - initialX, sprite.getHeight() / 2 - initialY);
        sprite.setPosition(initialX, initialY);
        sprite.setColor(MathUtils.random(0.8f, 1), MathUtils.random(0.8f, 1), MathUtils.random(0.8f, 1), MathUtils.random(0.8f, 1));
        lastVector.set(initialX, initialY);
    }

    private Vector2 lastVector = new Vector2();
    private Vector2 tempVector = new Vector2();
    private float boomRadius;
    private float boomAngle;
    private float boomAngleModifier = 1;

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void update(float delta) {
        if (isBoom) {
            if (shouldPaused && boomTimer > MAX_BOOM_TIME / 2) {
                pauseTime += delta;
                if (pauseTime > 2) {
                    pauseTime = 0;
                    shouldPaused = false;
                }
                return;
            }
            if (boomTimer == 0) {
                sprite.setOriginCenter();
                sprite.setPosition(initialRadius * MathUtils.cosDeg(sprite.getRotation() + initialAngle), initialRadius * MathUtils.sinDeg(sprite.getRotation() + initialAngle));
                boomRadius = initialRadius;
                boomAngle = sprite.getRotation() + initialAngle;
                initialAngle = MathUtils.random(0, 360);
                boomAngleModifier = MathUtils.random(0.5f, 6f);
            }
            boomTimer += delta;

            if (boomTimer < MAX_BOOM_TIME / 2) {
                shouldPaused = true;
                boomAngle += delta * 240 * MathUtils.clamp(boomTimer / (MAX_BOOM_TIME / 2), 0, 1) * boomAngleModifier;
                float tempRadius = MathUtils.lerp(boomRadius, 0, MathUtils.clamp(boomTimer / (MAX_BOOM_TIME / 2), 0, 1));
                sprite.setPosition(tempRadius * MathUtils.cosDeg(sprite.getRotation() + boomAngle), tempRadius * MathUtils.sinDeg(sprite.getRotation() + boomAngle));
            } else if (boomTimer > MAX_BOOM_TIME / 2 && boomTimer < MAX_BOOM_TIME) {
                boomAngle -= delta * 240 * MathUtils.clamp((boomTimer - MAX_BOOM_TIME / 2) / (MAX_BOOM_TIME / 2), 0, 1) * boomAngleModifier;
                float alpha = MathUtils.clamp((boomTimer - MAX_BOOM_TIME / 2) / (MAX_BOOM_TIME / 2), 0, 1);
                float tempRadius = MathUtils.lerp(0, boomRadius, alpha);
                sprite.setPosition(tempRadius * MathUtils.cosDeg(sprite.getRotation() + boomAngle), tempRadius * MathUtils.sinDeg(sprite.getRotation() + boomAngle));
            } else {
                sprite.setOrigin(sprite.getWidth() / 2 - sprite.getX(), sprite.getHeight() / 2 - sprite.getY());
                isBoom = false;
                boomTimer = 0;
            }


        } else {
            sprite.rotate(delta * 60 * ANGLE_ROTATION_VELOCITY);
        }

    }
}
