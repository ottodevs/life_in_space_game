package com.widesteppe.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;


public class AssetsLoader implements Disposable {
    public static final Color consoleColor = Color.valueOf("#66d9ef");
    public static final Color spaceColor = Color.valueOf("#05051e");
    public static final String logoSoundAddress = "sounds/logo.wav";
    public static final String font1Address = "fonts/font1.fnt";
    public static final String starTexAddress = "star.png";
    public static final String menuStationAddress = "screens/main_menu/station.png";
    public static final String menuPlayAddress = "screens/main_menu/start_btn.png";
    public static final String menuOptionsAddress = "screens/main_menu/options.png";
    public static final String menuMusicAddress = "sounds/main_music.mp3";
    public static final String menuTitleAddress = "screens/main_menu/title.png";
    public static final String stationAddress = "station.png";
    public static BitmapFont font1;

    public static Music mainMusic;
    public static Sound logoSound;
    public static Texture starTex;
    public static Texture menuStationTex;
    public static Texture menuPlayTex;
    public static Texture menuOptionsTex;
    public static Texture menuTitleTex;
    public static Texture stationTex;

    private AssetsLoader() {
    }

    public static void load(AssetManager manager) {


        manager.load(logoSoundAddress, Sound.class);
        manager.load(menuMusicAddress, Music.class);
        manager.load(font1Address, BitmapFont.class);
        manager.load(starTexAddress, Texture.class);

        manager.load(menuStationAddress, Texture.class);
        manager.load(menuPlayAddress, Texture.class);
        manager.load(menuOptionsAddress, Texture.class);
        manager.load(menuTitleAddress, Texture.class);


        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.genMipMaps = true;
        manager.load(stationAddress, Texture.class, param);

    }

    public static void initializeAssets(AssetManager manager) {
        logoSound = manager.get(logoSoundAddress, Sound.class);
        font1 = manager.get(font1Address,BitmapFont.class);
        starTex = manager.get(starTexAddress,Texture.class);
        menuStationTex = manager.get(menuStationAddress, Texture.class);
        menuOptionsTex = manager.get(menuOptionsAddress, Texture.class);
        menuPlayTex = manager.get(menuPlayAddress, Texture.class);
        menuTitleTex = manager.get(menuTitleAddress, Texture.class);
        mainMusic = manager.get(menuMusicAddress, Music.class);
        stationTex = manager.get(stationAddress, Texture.class);
        setLinearFilter(starTex);
        setLinearFilter(menuTitleTex);
        setLinearFilter(menuPlayTex);
        setLinearFilter(menuStationTex);
        setLinearFilter(menuOptionsTex);
        //setLinearFilter(stationTex);
        stationTex.setFilter(Texture.TextureFilter.MipMap, Texture.TextureFilter.Linear);
    }

    private static void setLinearFilter(Texture texture) {
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public void dispose() {
        logoSound.dispose();
    }

}
