package com.widesteppe.screens;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.widesteppe.utils.AssetsLoader;

import java.util.ArrayList;


public class Foreground {
    private static final String ROUND_TABLE = "roundTable";
    private static final String RUNNING_TRAIN = "runningTrain";
    private static final String SCI_TBL1 = "scientist_table";
    private static final String SCI_TBL2 = "scientist_table2";
    private static final String SCI_TBL3 = "scientist_table3";
    private static final String TOOL_TABLE = "tool_table";
    private static final String BUFFER = "buffer";
    private static final String COMP_TBL = "comp_table";
    private static final String CRAFTING_TBL = "crafting_table";
    private ArrayList<Sprite> foreElements = new ArrayList<Sprite>();
    private float dist;

    public Foreground() {
        createSprites();
    }

    private void createSprites() {
        dist = GameScreen.WALKING_DIST + 20;
        createNewSprite(BUFFER, 55, dist);
        createNewSprite(BUFFER, -65.5f, dist);
        createNewSprite(BUFFER, 175, dist +15);
        createNewSprite(TOOL_TABLE, 17.5f, dist - 10);
        createNewSprite(CRAFTING_TBL, 32f, dist - 10);
        createNewSprite(COMP_TBL, 42f, dist - 10);
        createNewSprite(ROUND_TABLE, 109f, dist +5);
        createNewSprite(ROUND_TABLE, -130f, dist +5);
        createNewSprite(ROUND_TABLE, -40f, dist +5);
        createNewSprite(COMP_TBL, -105, dist -10);
        createNewSprite(RUNNING_TRAIN, -92.5f, dist -10);
        createNewSprite(RUNNING_TRAIN, -80f, dist -10);
        createNewSprite(SCI_TBL1, 136f, dist -10);
        createNewSprite(SCI_TBL2, 148f, dist -10);
        createNewSprite(SCI_TBL3, 160f, dist -10);
    }

    private void createNewSprite(String spriteName, float angle,float distance) {
        Sprite sprite = AssetsLoader.foreAtlas.createSprite(spriteName);
        sprite.setPosition(-sprite.getWidth() / 2, -distance);
        sprite.setOrigin(sprite.getWidth() / 2, distance);
        sprite.setRotation(angle);
        foreElements.add(sprite);
    }

    public void render(SpriteBatch batch) {
        for (Sprite sprite : foreElements) {
            sprite.draw(batch);
        }
    }
}
