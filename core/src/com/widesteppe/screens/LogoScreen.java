/**
 * LogoScreen.java
 * pattern Singleton
 * @version 1.0
 * @author Oleg Morozov
 * 12.11.2012
 * COPYRIGHT (C) 2012 Oleg Morozov. All Rights Reserved.
 */
package com.widesteppe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.widesteppe.Controller;
import com.widesteppe.utils.AssetsLoader;
import com.widesteppe.utils.ConsoleWriter;


public class LogoScreen implements Screen {


    private float textX;
    private float textY;
    public static final String LOGO_STRING = "time to play";
    private SpriteBatch spriteBatch;
    private Sprite poweredSprite;
    private Sprite loadingSprite;
    private boolean loading = true;
    private GlyphLayout layoutText;
    private boolean isSoundPlaying;

    public LogoScreen() {
        layoutText = new GlyphLayout();
    }

    private float textTimer;

    @Override
    public void render(float delta) {

        if (loading && Controller.getAssetManager().update()) {
            loading = false;
            AssetsLoader.initializeAssets(Controller.getAssetManager());
            layoutText.setText(AssetsLoader.font1, LOGO_STRING);
            textX = Controller.WIDTH / 2 - layoutText.width / 2;
            textY = Controller.HEIGHT / 2;
            AssetsLoader.font1.setColor(AssetsLoader.consoleColor);
        } else {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            spriteBatch.begin();
            if (loading) {
                loadingSprite.draw(spriteBatch);
            } else {
                if (!isSoundPlaying) {
                    AssetsLoader.logoSound.play();
                    isSoundPlaying = true;
                }
                textTimer += Gdx.graphics.getDeltaTime();
                if (Controller.IS_DEBUG_MODE) {
                    layoutText.setText(AssetsLoader.font1, ConsoleWriter.generateString(LOGO_STRING, textTimer, ConsoleWriter.VERY_FAST_TYPE_SPEED));
                    if (ConsoleWriter.isReady(LOGO_STRING, textTimer, ConsoleWriter.VERY_FAST_TYPE_SPEED)) {
                        Controller.setMenuScreen();
                    }
                } else {
                    layoutText.setText(AssetsLoader.font1, ConsoleWriter.generateString(LOGO_STRING, textTimer, ConsoleWriter.SLOW_TYPE_SPEED));
                    if (ConsoleWriter.isReady(LOGO_STRING, textTimer, ConsoleWriter.SLOW_TYPE_SPEED)) {
                        Controller.setMenuScreen();
                    }
                }

                AssetsLoader.font1.draw(spriteBatch, layoutText, textX, textY);
            }
            poweredSprite.draw(spriteBatch);
            spriteBatch.end();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        OrthographicCamera cam = new OrthographicCamera(Controller.WIDTH, Controller.HEIGHT);
        cam.position.set(Controller.WIDTH / 2, Controller.HEIGHT / 2, 0);
        cam.update();
        spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(cam.combined);

        Texture poweredTexture = new Texture(Gdx.files.internal("screens/powered.png"));
        poweredTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Texture loadingTexture = new Texture(Gdx.files.internal("screens/loading.png"));
        loadingTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        poweredSprite = new Sprite(poweredTexture);
        float logoSizeScale = ((float) Controller.HEIGHT / 10) / poweredSprite.getHeight();
        poweredSprite.setSize(poweredSprite.getWidth() * logoSizeScale, poweredSprite.getHeight() * logoSizeScale);
        poweredSprite.setPosition(Controller.WIDTH - poweredSprite.getWidth() - Controller.HEIGHT / 20, Controller.HEIGHT / 20);
        loadingSprite = new Sprite(loadingTexture);
        float loadingSizeScale = ((float) Controller.HEIGHT / 10) / loadingSprite.getHeight();
        loadingSprite.setSize(loadingSprite.getWidth() * loadingSizeScale, loadingSprite.getHeight() * loadingSizeScale);
        loadingSprite.setPosition(Controller.WIDTH / 2 - loadingSprite.getWidth() / 2, Controller.HEIGHT / 2 - loadingSprite.getHeight() / 2);
    }

    @Override
    public void hide() {


    }

    @Override
    public void pause() {


    }

    @Override
    public void resume() {


    }

    @Override
    public void dispose() {

    }
}
