package com.widesteppe.actors;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.*;
import com.widesteppe.screens.GameScreen;
import com.widesteppe.utils.AssetsLoader;


public class Robot extends Astronaut{
    private final Animation idle2;
    private final Animation idle3;
    private float idleTimer;
    private static final float SCALE = 0.5f;
    public static final String ROBOT_JSON_ADD = "spine/robot/skeleton.json";
    public static final float ROBOT_ANGLE_VEL = 0.09f;

    public Robot() {
        super(270, SCALE, ROBOT_ANGLE_VEL, ROBOT_JSON_ADD, AssetsLoader.robotSpineAtlas);
        idle2 = skeletonData.findAnimation("idle2");
        idle3 = skeletonData.findAnimation("idle3");
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

    public void checkDestinationPoint(float x, float y) {
        float distanceFromCenter = Vector2.dst(x, y, 0, 0);
        if (distanceFromCenter > GameScreen.TOP_MAX_DIST && distanceFromCenter < GameScreen.WALKING_DIST) {
            if (currentRobotState != AI_STATE.TALKING) {
                goToPoint(x, y);
            }
        }
    }

    @Override
    protected void setToIdle() {
        super.setToIdle();
        idleTimer = 0;
    }
}
