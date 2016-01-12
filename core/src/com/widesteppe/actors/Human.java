package com.widesteppe.actors;


import com.badlogic.gdx.math.Vector2;
import com.widesteppe.screens.GameScreen;
import com.widesteppe.utils.AssetsLoader;


public class Human extends Astronaut{
    private static final float SCALE = 1f;
    public static final String HUMAN_JSON_ADD = "spine/human/skeleton.json";
    public static final float HUMAN_ANGLE_VELOCITY = 0.09f;

    public Human() {
        super(290, SCALE, HUMAN_ANGLE_VELOCITY, HUMAN_JSON_ADD, AssetsLoader.humanSpineAtlas);
    }

    @Override
    public void idle(float delta) {

    }

    public void checkDestinationPoint(float x, float y) {
        float distanceFromCenter = Vector2.dst(x, y, 0, 0);
        if (distanceFromCenter > GameScreen.TOP_MAX_DIST && distanceFromCenter < GameScreen.WALKING_DIST) {
            if (currentRobotState != AI_STATE.TALKING) {
                goToPoint(x, y);
            }
        }
    }

}
