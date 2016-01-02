package com.widesteppe.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.widesteppe.Controller;
import com.widesteppe.utils.AssetsLoader;
import com.widesteppe.utils.Star;
import com.widesteppe.utils.StarGenerator;


public class MenuScreen implements Screen {
    private final SpriteBatch spriteBatch;

    public MenuScreen() {
        float physWorldHeight = 600;
        float physWorldWidth = ((float) Controller.WIDTH / (float) Controller.HEIGHT) * physWorldHeight;
        OrthographicCamera cam = new OrthographicCamera(physWorldWidth, physWorldHeight);
        cam.position.set(0f, 0f, 0f);
        cam.update();
        spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(cam.combined);
        StarGenerator.generateStars(physWorldWidth);
    }

    @Override
    public void show() {
        AssetsLoader.mainMusic.play();
        AssetsLoader.mainMusic.setLooping(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        for(Star star : StarGenerator.getStars()) {
            star.update(delta);
            star.render(spriteBatch);
        }
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        AssetsLoader.mainMusic.play();

    }

    @Override
    public void hide() {
        AssetsLoader.mainMusic.stop();
    }

    @Override
    public void dispose() {

    }
}
