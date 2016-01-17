package com.widesteppe.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.widesteppe.Controller;

public class ChooserPopup {
    private static ChooserPopup instance;
    private final TextButton.TextButtonStyle txs;
    private Stage popupStage;
    public static boolean isOpen;
    public static boolean isMenu;
    private TextButton textButton1;
    private TextButton textButton2;
    private TextButton textButton3;
    private Container<Table> container;
    private Table table;
    private InputProcessor lastInputProcessor;
    private static boolean isClosing;
    private ThreeOptionChooser threeOptionChooser;

    private ChooserPopup(){
        popupStage = new Stage(new StretchViewport(1060, 600));
        popupStage.addListener(new InputListener() {
                                   @Override
                                   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button ) {
                                       if (y > 300 && isMenu) ChooserPopup.getInstance().close();
                                       return super.touchDown(event, x, y, pointer, button);
                                   }

                                   @Override
                                   public boolean keyDown(InputEvent event, int keycode) {
                                       if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK && isMenu && !isClosing) {
                                           ChooserPopup.getInstance().close();
                                       }
                                       return super.keyDown(event, keycode);
                                   }
                               }

        );
        Sprite background = AssetsLoader.popupAtlas.createSprite("background");
        Sprite buttonUp = AssetsLoader.popupAtlas.createSprite("buttonUp");
        Sprite buttonDown = AssetsLoader.popupAtlas.createSprite("buttonDown");
        txs = new TextButton.TextButtonStyle(new SpriteDrawable(buttonDown), new SpriteDrawable(buttonUp), null, AssetsLoader.font3);
        textButton1 = new TextButton("I think you are wrong my friend. It is harder than you think", txs);
        textButton2 = new TextButton("test2", txs);
        textButton3 = new TextButton("test3", txs);
        float buttonWidth = buttonUp.getWidth();
        float buttonHeight = buttonUp.getHeight();
        textButton1.setSize(buttonWidth, buttonHeight);
        textButton2.setSize(buttonWidth, buttonHeight);
        textButton3.setSize(buttonWidth, buttonHeight);

        textButton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                threeOptionChooser.optionOne();
                close();
            }
        });
        textButton2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                threeOptionChooser.optionTwo();
                close();
            }
        });
        textButton3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                threeOptionChooser.optionThree();
                close();
            }
        });
        container = new Container<Table>();
        container.setBackground(new SpriteDrawable(background));
        table = new Table();
        table.add(textButton1).pad(5);
        table.row();
        table.add(textButton2).pad(5);
        table.row();
        table.add(textButton3).pad(5);
        container.setActor(table);
        container.setSize(1060, 300);
        container.setPosition(0, -300);
        popupStage.addActor(container);

    }
    public static ChooserPopup getInstance(){
        if (instance == null) {
            instance = new ChooserPopup();
        }
        return instance;
    }

    public void open(boolean isMenu, String case1, String case2, final ThreeOptionChooser threeOptionChooser){
        this.threeOptionChooser = threeOptionChooser;
        defaulOpen(isMenu);
        table.clear();
        textButton1.setText(case1);
        textButton2.setText(case2);

        table.add(textButton1).pad(5);
        table.row();
        table.add(textButton2).pad(5);
    }
    public void open(boolean isMenu, String case1, String case2, String case3, final ThreeOptionChooser threeOptionChooser){
        this.threeOptionChooser = threeOptionChooser;
        defaulOpen(isMenu);
        table.clear();
        textButton1.setText(case1);
        textButton2.setText(case2);
        textButton3.setText(case3);


        table.add(textButton1).pad(5);
        table.row();
        table.add(textButton2).pad(5);
        table.row();
        table.add(textButton3).pad(5);
    }

    private void defaulOpen(boolean isMenu) {
        if (isClosing) return;
        this.isMenu = isMenu;
        if (isMenu) {
            txs.font = AssetsLoader.font2;
        } else {
            txs.font = AssetsLoader.font3;
        }
        textButton1.setStyle(txs);
        textButton2.setStyle(txs);
        textButton3.setStyle(txs);
        lastInputProcessor = Gdx.input.getInputProcessor();
        isOpen = true;
        Gdx.input.setInputProcessor(popupStage);
        Gdx.input.setCatchBackKey(true);
        container.addAction(Actions.moveTo(0, 0, 0.3f, Interpolation.exp10Out));
    }

    public void close(){
        isClosing = true;
        container.addAction(Actions.sequence(Actions.moveTo(0, -300, 0.3f, Interpolation.exp10Out), Actions.run(new Runnable() {
            @Override
            public void run() {
                isOpen = false;
                isClosing = false;
                threeOptionChooser.close();
            }
        })));
        Gdx.input.setInputProcessor(lastInputProcessor);
    }

    public void update() {
        popupStage.act();
        popupStage.draw();
        if (Controller.IS_DEBUG_MODE) {
           // popupStage.setDebugAll(true);
        }
    }

}
