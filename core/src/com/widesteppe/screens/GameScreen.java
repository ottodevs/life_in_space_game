package com.widesteppe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.*;
import com.widesteppe.Controller;
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
    private final TextureAtlas atlas;
    private final SkeletonData skeletonData;
    private final Skeleton skeleton;
    private final Animation walk;
    private final PolygonSpriteBatch batch;
    private Sprite back;
    private GameInputHandler inputHandler;
    private float time;
    private Array<Event> events;
    private SkeletonRenderer renderer;


    public GameScreen() {
        physWorldHeight = 600f;
        physWorldWidth = ((float) Controller.WIDTH / (float) Controller.HEIGHT) * physWorldHeight;
        cam = new OrthographicCamera(physWorldWidth, physWorldHeight);
        cam.position.set(0f, 0f, 0f);
        cam.update();
        spriteBatch = new SpriteBatch();
        starMatrix = new Matrix4(cam.combined);
        createBack();
        inputHandler = new GameInputHandler(this);
        Gdx.input.setInputProcessor(inputHandler);
        Gdx.input.setCatchBackKey(true);

        atlas = new TextureAtlas(Gdx.files.internal("spine/robot/skeleton.atlas"));
        SkeletonJson json = new SkeletonJson(atlas);
        skeletonData = json.readSkeletonData(Gdx.files.internal("spine/robot/skeleton.json"));
        skeleton = new Skeleton(skeletonData);
        skeleton.setX(0);
        skeleton.setY(-10);
        walk = skeletonData.getAnimations().first();
        batch = new PolygonSpriteBatch();
        renderer = new SkeletonRenderer();

    }

    private void createBack() {
        back = new Sprite(AssetsLoader.stationTex);
        back.setPosition(-back.getWidth() / 2, -back.getHeight() / 2);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(AssetsLoader.spaceColor.r, AssetsLoader.spaceColor.g, AssetsLoader.spaceColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.setProjectionMatrix(starMatrix);
        spriteBatch.begin();
        for(Star star : StarGenerator.getStars()) {
            star.update(delta);
            star.render(spriteBatch);
        }
        spriteBatch.end();

        updateCamera(delta);
        spriteBatch.setProjectionMatrix(cam.combined);
        /*spriteBatch.begin();
            back.draw(spriteBatch);
        spriteBatch.end();*/

        time += delta;
        skeleton.updateWorldTransform();
        skeleton.update(delta);
        walk.apply(skeleton, time, time, true, events);
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        renderer.draw(batch, skeleton);

        batch.end();

    }

    private void updateCamera(float delta) {
        cam.zoom = MathUtils.lerp(cam.zoom, cameraZoom, 0.015f * delta * 60);
        System.out.println(cam.zoom);
        cam.update();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(inputHandler);
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
    public void scrollCamera(int amount) {
        cameraZoom += amount;
        cameraZoom = MathUtils.clamp(cameraZoom, 1, 8);
    }
}
