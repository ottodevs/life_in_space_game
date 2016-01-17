package com.widesteppe;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.widesteppe.screens.GameScreen;
import com.widesteppe.screens.LogoScreen;
import com.widesteppe.screens.MenuScreen;
import com.widesteppe.utils.AssetsLoader;
import com.widesteppe.utils.ChooserPopup;
import com.widesteppe.utils.SpriteTween;

public class Controller extends Game {
	public static int WIDTH;
	public static int HEIGHT;

	private static LogoScreen logoScreen;
	private static AssetManager assetManager;
	private static MenuScreen menuScreen;
	private static boolean shouldSetGameScreen;
	private SpriteBatch spriteBatch;
	private BitmapFont font;
	public static final boolean IS_DEBUG_MODE = false;
	private static boolean shouldSetMenuScreen;
	private static TweenManager tweenManager;
	private GameScreen gameScreen;

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
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteTween());

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
			if (menuScreen == null) {
				menuScreen = new MenuScreen();
			}
			setScreen(menuScreen);
			shouldSetMenuScreen = false;
		}
		if (shouldSetGameScreen) {
			if (gameScreen == null) {
				gameScreen = new GameScreen();
			}
			setScreen(gameScreen);
			shouldSetGameScreen = false;
		}
		if (ChooserPopup.isOpen) {
			ChooserPopup.getInstance().update();
		}
	}



	public static AssetManager getAssetManager() {
		return assetManager;
	}

	public static void setMenuScreen() {
		shouldSetMenuScreen = true;
	}

	public static TweenManager getTweenManager() {
		return tweenManager;
	}

	public static void setGameScreen() {
		shouldSetGameScreen = true;
	}

	public static void exitGame() {
		System.exit(0);
	}
}
