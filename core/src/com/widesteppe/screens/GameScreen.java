package com.widesteppe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.widesteppe.Controller;
import com.widesteppe.Director;
import com.widesteppe.Quest;
import com.widesteppe.actors.Astronaut;
import com.widesteppe.actors.Crew;
import com.widesteppe.actors.Human;
import com.widesteppe.utils.*;


public class GameScreen implements Screen {
    public static boolean isPause;
    private final float physWorldHeight;
    private final float physWorldWidth;
    private final OrthographicCamera cam;
    private final SpriteBatch spriteBatch;

    private final PolygonSpriteBatch polygonSpriteBatch;
    private final Foreground foreground;
    private final MainGUI mainGui;
    private Sprite[] backSprites = new Sprite[AssetsLoader.STATION_NUMBER];
    private GameInputHandler inputHandler;
    public static final float MIN_ZOOM = 1;
    public static final float MAX_ZOOM = 4;


    private Vector2 cameraTargetPosition = new Vector2();
    public static final float TOP_MAX_DIST = 1600;
    public static final float WALKING_DIST = 1850;
    private boolean cameraTargetChanged;
    public boolean isZooming = false;
    private Crew crew;
    private Stage spaceshipStage;
    public static final float PHYS_WORLD_WIDTH = 1200f;
    private Director director;

    public GameScreen() {
        physWorldHeight = PHYS_WORLD_WIDTH;
        physWorldWidth = ((float) Controller.WIDTH / (float) Controller.HEIGHT) * physWorldHeight;
        foreground = new Foreground();
        cam = new OrthographicCamera(physWorldWidth, physWorldHeight);
        spriteBatch = new SpriteBatch();
        createBack();
        inputHandler = new GameInputHandler(this);
        Gdx.input.setInputProcessor(inputHandler);
        Gdx.input.setCatchBackKey(true);
        polygonSpriteBatch = new PolygonSpriteBatch();
        spaceshipStage = new Stage(new FitViewport(physWorldWidth, physWorldHeight, cam), spriteBatch);
        crew = new Crew(spaceshipStage);
        changeCameraZoomPosTarget();
        cam.position.set(cameraTargetPosition.x, cameraTargetPosition.y, 0f);

        cam.update();
        mainGui = new MainGUI(this);
        director = new Director(crew);
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
        setPause(false);
        MainTimer.setTimeInSeconds(MyPrefs.getInstance().getTimerValue());
        MainGUI.isGameOVER = MyPrefs.getInstance().isGameOver();
        director.getQuest().setId(MyPrefs.getInstance().getMissionId());
        Gdx.input.setInputProcessor(inputHandler);
        director.getQuest().reset();

    }

    @Override
    public void render(float delta) {
        updateState(delta);

        Gdx.gl.glClearColor(AssetsLoader.spaceColor.r, AssetsLoader.spaceColor.g, AssetsLoader.spaceColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //BACKGROUND
        spriteBatch.setProjectionMatrix(MenuScreen.starMatrix);
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

        //CHARACTERS

        polygonSpriteBatch.setProjectionMatrix(cam.combined);
        polygonSpriteBatch.begin();
        crew.render(polygonSpriteBatch);
        polygonSpriteBatch.end();

        //FOREGROUND
        spriteBatch.begin();
        foreground.render(spriteBatch);
        spriteBatch.end();

        spriteBatch.begin();
       // AssetsLoader.font1.draw(spriteBatch, String.valueOf(currentDistance), 0, 0);

        spriteBatch.end();
        spaceshipStage.act();
        spaceshipStage.draw();
        if (Controller.IS_DEBUG_MODE) {
            spaceshipStage.setDebugAll(true);
        }
        mainGui.render();
    }

    private void updateState(float delta) {
        if (!isPause) {
            MainTimer.update(delta);
            updateCamera(delta);
            for (Star star : StarGenerator.getStars()) star.update(delta);
            crew.update(delta);
            director.update(delta);
            if (director.getQuest().isDialog() && cam.zoom != MIN_ZOOM) {
                scrollCamera(-10);
            }
        }

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
            //cam.zoom = cameraZoom;
            //cam.position.set(cameraTargetPosition.x, cameraTargetPosition.y, 0);
            cam.update();
            cameraTargetChanged = false;
        }
        if (crew.getRobot().getCurrentState() == Astronaut.AI_STATE.WALKING) {
            changeCameraZoomPosTarget();
            cam.rotate((getCameraCurrentXYAngle(cam) + 90) % 360 - crew.getRobot().getCurrentPositionAngle());
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        isPause = true;
    }

    @Override
    public void resume() {
        isPause = false;
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
        cameraZoom = MathUtils.clamp(cameraZoom, MIN_ZOOM, MAX_ZOOM);
        tempVector.set(MathUtils.cosDeg(crew.getRobot().getCurrentPositionAngle()) * TOP_MAX_DIST, MathUtils.sinDeg(crew.getRobot().getCurrentPositionAngle()) * TOP_MAX_DIST);
        float a = MathUtils.clamp((cameraZoom - MIN_ZOOM) / (MAX_ZOOM - MIN_ZOOM), 0, 1);
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

    public OrthographicCamera getCam() {
        return cam;
    }

    public Crew getCrew() {
        return crew;
    }

    public float getPhysWorldHeight() {
        return physWorldHeight;
    }

    public float getPhysWorldWidth() {
        return physWorldWidth;
    }

    public void setPause(boolean pause) {
        this.isPause = pause;
    }

    public Director getDirector() {
        return director;
    }
}
