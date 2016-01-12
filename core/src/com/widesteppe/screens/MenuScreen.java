package com.widesteppe.screens;


import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Quint;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.widesteppe.Controller;
import com.widesteppe.utils.AssetsLoader;
import com.widesteppe.utils.SpriteTween;
import com.widesteppe.utils.Star;
import com.widesteppe.utils.StarGenerator;


public class MenuScreen implements Screen, InputProcessor {
    private final SpriteBatch spriteBatch;
    private final float physWorldHeight;
    private final float physWorldWidth;
    private final OrthographicCamera cam;
    private Sprite station;
    private Sprite title;
    private Sprite playButton;
    private Sprite optionsButton;
    private Vector3 vc3 = new Vector3();
    private boolean playButtonTouched;
    private boolean optionsButtonTouched;


    public MenuScreen() {
        physWorldHeight = 600;
        physWorldWidth = ((float) Controller.WIDTH / (float) Controller.HEIGHT) * physWorldHeight;
        cam = new OrthographicCamera(physWorldWidth, physWorldHeight);
        cam.position.set(0f, 0f, 0f);
        cam.update();
        spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(cam.combined);
        StarGenerator.generateStars(physWorldWidth);
        createSprites();
    }

    @Override
    public void show() {
        AssetsLoader.mainMusic.play();
        AssetsLoader.mainMusic.setVolume(0.2f);
        AssetsLoader.mainMusic.setLooping(true);
        playButton.setScale(1f);
        Gdx.input.setInputProcessor(this);
    }

    private void createSprites() {
        station = new Sprite(AssetsLoader.menuStationTex);
        float stationSizeScale = (physWorldHeight / 2) / station.getHeight();
        station.setSize(station.getWidth() * stationSizeScale, station.getHeight() * stationSizeScale);
        station.setOrigin(station.getWidth() / 2, station.getHeight() / 2);

        title = new Sprite(AssetsLoader.menuTitleTex);
        float titleHeight = physWorldHeight * 0.15f;
        title.setSize((titleHeight / title.getHeight()) * title.getWidth(), titleHeight);
        title.setPosition(-title.getWidth() / 2, physWorldHeight * 0.3f);

        playButton = new Sprite(AssetsLoader.menuPlayTex);
        playButton.setSize(playButton.getWidth() * stationSizeScale, playButton.getHeight() * stationSizeScale);
        playButton.setOrigin(playButton.getWidth() / 2, playButton.getHeight() / 2);

        optionsButton = new Sprite(AssetsLoader.menuOptionsTex);
        optionsButton.setSize(optionsButton.getWidth() * stationSizeScale, optionsButton.getHeight() * stationSizeScale);
        optionsButton.setOrigin(optionsButton.getWidth() / 2, optionsButton.getHeight() / 2);
        optionsButton.setPosition(physWorldWidth / 2 - optionsButton.getWidth() - physWorldHeight *0.05f, -physWorldHeight * 0.45f);

        float stationY = (title.getY() - optionsButton.getY() - optionsButton.getHeight()) / 2 + optionsButton.getY() + optionsButton.getHeight();
        station.setPosition(-station.getWidth() / 2, stationY - station.getHeight() / 2);
        playButton.setPosition(-playButton.getWidth() / 2, stationY - playButton.getHeight() / 2);



    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(AssetsLoader.spaceColor.r, AssetsLoader.spaceColor.g, AssetsLoader.spaceColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        for(Star star : StarGenerator.getStars()) {
            //star.update(delta);
            star.render(spriteBatch);
        }
        updateStation(delta);
        station.draw(spriteBatch);
        title.draw(spriteBatch);
        playButton.draw(spriteBatch);
        optionsButton.draw(spriteBatch);
        spriteBatch.end();
        Controller.getTweenManager().update(delta);
    }

    private void updateStation(float delta) {
        station.rotate(-Star.ANGLE_ROTATION_VELOCITY * delta * 60);
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
        AssetsLoader.mainMusic.stop();
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) Gdx.app.exit();
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        cam.unproject(vc3.set(screenX, screenY, 0));
        if (playButton.getBoundingRectangle().contains(vc3.x, vc3.y)) {
            Tween.to(playButton, SpriteTween.SCALE_XY, 0.5f).target(0.7f, 0.7f).ease(Quint.OUT).start(Controller.getTweenManager());
            playButtonTouched = true;
        }
        if (optionsButton.getBoundingRectangle().contains(vc3.x, vc3.y)) {
            optionsButtonTouched = true;
            Tween.to(optionsButton, SpriteTween.ROT, 0.5f).target(-90).ease(Quint.OUT).start(Controller.getTweenManager());
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        cam.unproject(vc3.set(screenX, screenY, 0));
        if (playButton.getBoundingRectangle().contains(vc3.x, vc3.y) && playButtonTouched) {
            startGame();
            return true;
        }
        if (optionsButton.getBoundingRectangle().contains(vc3.x, vc3.y) && optionsButtonTouched) {
            openOptionsPopup();
        }
        if (playButtonTouched) {
            Tween.to(playButton, SpriteTween.SCALE_XY, 0.5f).target(1f, 1f).ease(Quint.OUT).start(Controller.getTweenManager());
            playButtonTouched = false;
        }
        if (optionsButtonTouched) {
            optionsButtonTouched = false;
            Tween.to(optionsButton, SpriteTween.ROT, 0.5f).target(0).ease(Quint.OUT).start(Controller.getTweenManager());

        }
        return false;
    }

    private void openOptionsPopup() {
        //TODO create optionsPopup
    }

    private void startGame() {
        Controller.setGameScreen();
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        cam.unproject(vc3.set(screenX, screenY, 0));

        if (playButtonTouched && !playButton.getBoundingRectangle().contains(vc3.x, vc3.y)) {
            playButtonTouched = false;
            Tween.to(playButton, SpriteTween.SCALE_XY, 0.5f).target(1f, 1f).ease(Quint.OUT).start(Controller.getTweenManager());
        }
        if (optionsButtonTouched && !optionsButton.getBoundingRectangle().contains(vc3.x, vc3.y)) {
            optionsButtonTouched = false;
            Tween.to(optionsButton, SpriteTween.ROT, 0.5f).target(0).ease(Quint.OUT).start(Controller.getTweenManager());

        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
