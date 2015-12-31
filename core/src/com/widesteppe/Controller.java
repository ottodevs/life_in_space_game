package com.widesteppe;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.widesteppe.screens.LogoScreen;
import com.widesteppe.screens.MenuScreen;
import com.widesteppe.utils.AssetsLoader;

public class Controller extends Game {
	public static int WIDTH;
	public static int HEIGHT;

	private static LogoScreen logoScreen;
	private static AssetManager assetManager;
	private static MenuScreen menuScreen;
	private SpriteBatch spriteBatch;
	private BitmapFont font;
	public static final boolean IS_DEBUG_MODE = true;
	private static boolean shouldSetMenuScreen;

	public Controller(){
		assetManager = new AssetManager();
	}


	
	@Override
	public void create () {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		AssetsLoader.load(assetManager);
		logoScreen = new LogoScreen();
		setScreen(logoScreen);

		if(IS_DEBUG_MODE) {
			OrthographicCamera cam = new OrthographicCamera(WIDTH, HEIGHT);
			cam.position.set(0, 0, 0);
			cam.update();
			font = new BitmapFont(Gdx.files.internal("fonts/font1.fnt"));
			spriteBatch = new SpriteBatch();
			spriteBatch.setProjectionMatrix(cam.combined);
		}
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		WIDTH = width;
		HEIGHT = height;
	}

	@Override
	public void render() {
		super.render();
		if (IS_DEBUG_MODE) {
			spriteBatch.begin();
			font.draw(spriteBatch, String.valueOf(Gdx.graphics.getFramesPerSecond()), -(float) WIDTH / 2 + 20, -(float) HEIGHT / 2 + 40);
			spriteBatch.end();
		}
		if (shouldSetMenuScreen) {
			menuScreen = new MenuScreen();
			setScreen(menuScreen);
			shouldSetMenuScreen = false;
		}
	}

	public static AssetManager getAssetManager() {
		return assetManager;
	}

	public static void setMenuScreen() {
		shouldSetMenuScreen = true;
	}
}
