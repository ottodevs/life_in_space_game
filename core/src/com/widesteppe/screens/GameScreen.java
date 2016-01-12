package com.widesteppe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.widesteppe.Controller;
import com.widesteppe.actors.Astronaut;
import com.widesteppe.actors.Crew;
import com.widesteppe.actors.Robot;
import com.widesteppe.utils.AssetsLoader;
import com.widesteppe.utils.GameInputHandler;
import com.widesteppe.utils.Star;
import com.widesteppe.utils.StarGenerator;


public class GameScreen implements Screen {
    private final float physWorldHeight;
    private final float physWorldWidth;
    private final OrthographicCamera cam;
    private final SpriteBatch spriteBatch;
    private final Matrix4 starMatrix;

    private final PolygonSpriteBatch polygonSpriteBatch;
    private Sprite[] backSprites = new Sprite[AssetsLoader.STATION_NUMBER];
    private GameInputHandler inputHandler;
    public static final float MIN_ZOMM = 2;
    public static final float MAX_ZOMM = 8;


    private Vector2 cameraTargetPosition = new Vector2();
    private Robot robot;
    public static final float TOP_MAX_DIST = 1600;
    public static final float WALKING_DIST = 1850;
    private boolean cameraTargetChanged;
    public boolean isZooming = false;
    private Crew crew;

    public GameScreen() {
        physWorldHeight = 600f;
        physWorldWidth = ((float) Controller.WIDTH / (float) Controller.HEIGHT) * physWorldHeight;
        robot = new Robot();
        crew = new Crew();

        cam = new OrthographicCamera(physWorldWidth, physWorldHeight);
        spriteBatch = new SpriteBatch();
        starMatrix = new Matrix4(cam.combined);
        createBack();
        inputHandler = new GameInputHandler(this);
        Gdx.input.setInputProcessor(inputHandler);
        Gdx.input.setCatchBackKey(true);
        polygonSpriteBatch = new PolygonSpriteBatch();
        cam.position.set(robot.getX(), robot.getY(), 0f);
        changeCameraZoomPosTarget();
        cam.update();

    }

    private void createBack() {
        for (int i = 0; i < AssetsLoader.STATION_NUMBER; i++) {
            backSprites[i] = new Sprite(AssetsLoader.stationTextures[i]);
            switch (i) {
                case 0:
                    backSprites[i].setPosition(-backSprites[i].getWidth(), 0);
                    break;
                case 1:
                    backSprites[i].setPosition(0, 0);
                    break;
                case 2:
                    backSprites[i].setPosition(-backSprites[i].getWidth(), -backSprites[i].getHeight());
                    break;
                case 3:
                    backSprites[i].setPosition(0, -backSprites[i].getHeight());
                    break;
            }
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputHandler);

    }

    @Override
    public void render(float delta) {
        updateState(delta);

        Gdx.gl.glClearColor(AssetsLoader.spaceColor.r, AssetsLoader.spaceColor.g, AssetsLoader.spaceColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.setProjectionMatrix(starMatrix);
        spriteBatch.begin();
        for (Star star : StarGenerator.getStars()) {
            star.render(spriteBatch);
        }
        spriteBatch.end();

        spriteBatch.setProjectionMatrix(cam.combined);
        spriteBatch.begin();
        for (int i = 0; i < AssetsLoader.STATION_NUMBER; i++) {
            backSprites[i].draw(spriteBatch);
        }
        spriteBatch.end();


        polygonSpriteBatch.setProjectionMatrix(cam.combined);
        polygonSpriteBatch.begin();
        robot.render(polygonSpriteBatch);
        crew.render(polygonSpriteBatch);
        polygonSpriteBatch.end();

        spriteBatch.setProjectionMatrix(starMatrix);
        spriteBatch.begin();
       // AssetsLoader.font1.draw(spriteBatch, String.valueOf(currentDistance), 0, 0);
        spriteBatch.end();


    }

    private void updateState(float delta) {
        updateCamera(delta);
        for (Star star : StarGenerator.getStars()) star.update(delta);
        robot.update(delta);
        crew.update(delta);

    }

    private void updateCamera(float delta) {
        if (cameraTargetChanged) {
            cam.zoom = MathUtils.lerp(cam.zoom, cameraZoom, 0.015f * delta * 60);
            float x = MathUtils.lerp(cam.position.x, cameraTargetPosition.x, 0.015f * delta * 60);
            float y = MathUtils.lerp(cam.position.y, cameraTargetPosition.y, 0.015f * delta * 60);
            cam.position.set(x, y, 0);
            cam.update();
        }
        if (Math.abs(cam.zoom - cameraZoom) < 0.01f) {
            cameraTargetChanged = false;
        }
        if (robot.getCurrentRobotState() == Astronaut.AI_STATE.WALKING) {
            changeCameraZoomPosTarget();
            System.out.println(getCameraCurrentXYAngle(cam));
            cam.rotate((getCameraCurrentXYAngle(cam) + 90) % 360 - robot.getCurrentPositionAngle());
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void returnToMainScreen() {
        Controller.setMenuScreen();
    }

    private float cameraZoom = 1;
    private Vector2 tempVector = new Vector2();

    public void scrollCamera(int amount) {
        cameraZoom += amount;

        changeCameraZoomPosTarget();
    }

    public void changeCameraZoomPosTarget() {
        cameraZoom = MathUtils.clamp(cameraZoom, MIN_ZOMM, MAX_ZOMM);
        tempVector.set(MathUtils.cosDeg(robot.getCurrentPositionAngle()) * TOP_MAX_DIST, MathUtils.sinDeg(robot.getCurrentPositionAngle()) * TOP_MAX_DIST);
        float a = MathUtils.clamp((cameraZoom - MIN_ZOMM) / (MAX_ZOMM - MIN_ZOMM), 0, 1);
        cameraTargetPosition.set(tempVector.lerp(Vector2.Zero, a));
        cameraTargetChanged = true;
    }

    float lastZoomDistance;

    public void zoomByGesture(float distance) {
        if (!isZooming) {
            lastZoomDistance = distance;
            isZooming = true;
        } else {
            if (distance > lastZoomDistance) {
                if (distance - lastZoomDistance > Gdx.graphics.getWidth() / 20) {
                    scrollCamera(-1);
                    lastZoomDistance = distance;

                }
            } else {
                if (lastZoomDistance - distance > Gdx.graphics.getWidth() / 20) {
                    scrollCamera(1);
                    lastZoomDistance = distance;

                }
            }
        }
    }

    public float getCameraCurrentXYAngle(OrthographicCamera cam)
    {
        return 180 - (float)Math.atan2(cam.up.x, cam.up.y)*MathUtils.radiansToDegrees;
    }

    public Robot getRobot() {
        return robot;
    }

    public OrthographicCamera getCam() {
        return cam;
    }
}
