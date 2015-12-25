package com.widesteppe;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.widesteppe.screens.LogoScreen;
import com.widesteppe.utils.AssetsLoader;

public class Controller extends Game {
	public static int WIDTH;
	public static int HEIGHT;

	private static LogoScreen logoScreen;
	private static AssetManager assetManager;
	private static Controller instance;
	private Controller(){
		assetManager = new AssetManager();
	}

	public static Controller getInstance(){
		if (instance == null) {
			return instance=new Controller();
		} return instance;
	}
	
	@Override
	public void create () {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		AssetsLoader.load(assetManager);
		logoScreen = new LogoScreen();
		setScreen(logoScreen);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		WIDTH = width;
		HEIGHT = height;
	}

	public static AssetManager getAssetManager() {
		return assetManager;
	}
}
