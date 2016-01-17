package com.widesteppe.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.widesteppe.Controller;
import com.widesteppe.actors.Astronaut;
import com.widesteppe.screens.GameScreen;

public class GameInputHandler extends GestureDetector {
    private GameScreen gameScreen;

    public GameInputHandler(GameScreen gameScreen) {
        super(new Gestures(gameScreen));
        this.gameScreen = gameScreen;
    }


    @Override
    public boolean keyDown(int keycode) {
        super.keyDown(keycode);
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
                ChooserPopup.getInstance().open(true, "RESUME", "SAVE AND QUIT", new ThreeOptionChooser() {
                    @Override
                    public void optionOne() {
                    }

                    @Override
                    public void optionTwo() {
                        gameScreen.setPause(true);
                        Controller.setMenuScreen();
                    }

                    @Override
                    public void optionThree() {

                    }

                    @Override
                    public void close() {
                        gameScreen.setPause(false);

                    }
                });
            gameScreen.setPause(true);
        }
        if (keycode == Input.Keys.K) gameScreen.getCrew().getRobot().sayMessage("Hello World!The preferred size of the label is determined by the actual text bounds, unless word wrap is enabled.");
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        super.scrolled(amount);
        gameScreen.scrollCamera(amount);
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        super.touchUp(x, y, pointer, button);
        gameScreen.isZooming = false;
        return false;

    }
}
class Gestures implements GestureDetector.GestureListener {

    private GameScreen gameScreen;
    private Vector3 vc3 = new Vector3();

    public Gestures(GameScreen gameScreen) {

        this.gameScreen = gameScreen;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        gameScreen.getCam().unproject(vc3.set(x, y, 0));
        if (!gameScreen.getCrew().getRobot().isDialog && gameScreen.getCrew().getRobot().getCurrentState() != Astronaut.AI_STATE.TALKING) {
            if (!gameScreen.getCrew().checkTouchOnHuman(vc3.x, vc3.y)) {
                gameScreen.getCrew().getRobot().checkDestinationPoint(vc3.x, vc3.y);
            }
        } else if (gameScreen.getCrew().getRobot().getCurrentState() == Astronaut.AI_STATE.TALKING){
            gameScreen.getCrew().getRobot().forceFinishMessage();
        } else if (gameScreen.getDirector().getQuest().isDialog()) {
            gameScreen.getDirector().getQuest().forceFinishReplic();
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        gameScreen.zoomByGesture(distance);
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
