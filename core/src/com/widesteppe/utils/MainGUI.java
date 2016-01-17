package com.widesteppe.utils;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.widesteppe.Controller;
import com.widesteppe.screens.GameScreen;

public class MainGUI {
    private Stage guiStage;
    private GameScreen gs;
    private Table table;
    private Label timeLabel;
    public static boolean isGameOVER;
    public MainGUI(GameScreen gs) {
        this.gs = gs;
        guiStage = new Stage(new FitViewport(gs.getPhysWorldWidth(), gs.getPhysWorldHeight()));
        table = new Table();
        createTimeLabel();
    }

    private void createTimeLabel() {
        Label.LabelStyle ls = new Label.LabelStyle(AssetsLoader.font2, AssetsLoader.consoleColor);

        timeLabel = new Label("Day 1", ls);
        Container<Label> labelContainer = new Container<Label>(timeLabel);
        SpriteDrawable sd = new SpriteDrawable(new Sprite(AssetsLoader.guiBackTex));
        sd.getSprite().setSize(guiStage.getWidth(), timeLabel.getHeight());
        labelContainer.setBackground(sd);
        Container<Container> parentContainer = new Container<Container>(labelContainer);
        parentContainer.setFillParent(true);
        parentContainer.align(Align.top);

        guiStage.addActor(parentContainer);



        //timeLabel.setAlignment(Align.topLeft);
    }

    public void render() {
        updateStats();
        guiStage.act();
        guiStage.draw();
        if (Controller.IS_DEBUG_MODE) {
            guiStage.setDebugAll(true);
        }
    }

    private void updateStats() {
        if (isGameOVER) {
            timeLabel.setText("To be continued... Thank you for playing. Vote it please.  ");
        } else {
            timeLabel.setText("Day: " + MainTimer.getCurrentGameDay() + " Hour: " + MainTimer.getCurrentGameHour());
        }
    }
}
