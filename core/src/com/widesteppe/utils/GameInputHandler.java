package com.widesteppe.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) gameScreen.returnToMainScreen();
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

        gameScreen.getRobot().checkDestinationPoint(vc3.x, vc3.y);
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
