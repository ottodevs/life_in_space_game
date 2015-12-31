package com.widesteppe.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;


public class AssetsLoader implements Disposable {
    public static final Color consoleColor = Color.valueOf("#66d9ef");
    public static BitmapFont font1;


    public static Sound logoSound;
    public static Texture starTex;

    private AssetsLoader() {
    }

    public static void load(AssetManager manager) {


        manager.load("sounds/logo.wav", Sound.class);
        manager.load("fonts/font1.fnt", BitmapFont.class);
        manager.load("star.png", Texture.class);

    }

    public static void initializeAssets(AssetManager manager) {
        logoSound = manager.get( "sounds/logo.wav", Sound.class);
        font1 = manager.get("fonts/font1.fnt",BitmapFont.class);
        starTex = manager.get("star.png",Texture.class);
        setLinearFilter(starTex);
    }

    private static void setLinearFilter(Texture texture) {
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public void dispose() {
        logoSound.dispose();
    }

}
