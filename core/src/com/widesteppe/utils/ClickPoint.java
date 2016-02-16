package com.widesteppe.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class ClickPoint {
    private Sprite sprite;
    public CLICK_TYPE currentType = CLICK_TYPE.RED;
    public boolean isActive;
    private float animTimer;
    private float MAX_ANIM_TIME = 0.3f;

    public enum CLICK_TYPE{
        RED,
        GREEN,
        BLUE
    }
    public ClickPoint(){
        sprite = new Sprite(AssetsLoader.starTex);
    }
    public void startAnimation(float x, float y) {
        isActive = true;
        sprite.setAlpha(1);
        sprite.setScale(2);
        sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getHeight() / 2);
        animTimer = 0;
        switch (currentType) {
            case RED: sprite.setColor(AssetsLoader.redConsoleColor); break;
            case GREEN: sprite.setColor(AssetsLoader.greenConsoleColor); break;
            case BLUE: sprite.setColor(AssetsLoader.consoleColor); break;
        }
    }
    public void update(float delta) {
        if (isActive) {
            animTimer += delta;
            float alpha = MathUtils.lerp(1, 0, MathUtils.clamp(animTimer / MAX_ANIM_TIME, 0, 1));
            sprite.setAlpha(alpha);
            sprite.setScale(alpha * 2);
            if (animTimer > MAX_ANIM_TIME) {
                isActive = false;

            }
        }
    }
    public void render(SpriteBatch batch) {
        if (isActive) sprite.draw(batch);
    }


}
