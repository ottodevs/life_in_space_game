package com.widesteppe.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;


public class Star {
    private Sprite sprite;
    private float initialX;
    private float initialY;
    public static final float ANGLE_ROTATION_VELOCITY = 0.1f;
    public Star(float physWorldWidth) {
        createSprite(physWorldWidth);
    }

    private void createSprite(float physWorldWidth) {
        sprite = new Sprite(AssetsLoader.starTex);
        float randomSize = MathUtils.random(4, 8);
        sprite.setSize(randomSize, randomSize);
        sprite.setScale(MathUtils.random(0.95f, 1.05f), MathUtils.random(0.95f, 1.05f));
        initialX = MathUtils.random(-physWorldWidth/2, physWorldWidth / 2);
        initialY = MathUtils.random(-physWorldWidth/2, physWorldWidth / 2);
        sprite.setOrigin(sprite.getWidth() / 2 - initialX, sprite.getHeight() / 2 - initialY);
        sprite.setPosition(initialX, initialY);
        sprite.setColor(MathUtils.random(0.8f,1),MathUtils.random(0.8f,1),MathUtils.random(0.8f,1),MathUtils.random(0.8f,1));
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
    public void update(float delta){
        sprite.rotate(delta * 60 * ANGLE_ROTATION_VELOCITY);
    }
}
