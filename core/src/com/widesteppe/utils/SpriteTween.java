package com.widesteppe.utils;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class SpriteTween implements TweenAccessor<Sprite> {
    public static final int POS_X = 1;
    public static final int POS_Y = 2;
    public static final int SCALE_X = 3;
    public static final int SCALE_Y = 4;
    public static final int SCALE_XY = 5;
    public static final int COLOR = 6;
    public static final int ROT = 7;
    public static final int ALPHA = 8;

    @Override
    public int getValues(Sprite sprite, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case POS_X:
                returnValues[0] = sprite.getX();
                return 1;
            case POS_Y:
                returnValues[0] = sprite.getY();
                return 1;
            case SCALE_X:
                returnValues[0] = sprite.getScaleX();
                return 1;
            case SCALE_Y:
                returnValues[0] = sprite.getScaleY();
                return 1;
            case ROT:
                returnValues[0] = sprite.getRotation();
                return 1;
            case ALPHA:
                returnValues[0] = sprite.getColor().a;
                return 1;
            case SCALE_XY:
                returnValues[0] = sprite.getScaleX();
                returnValues[1] = sprite.getScaleY();
                return 2;
            case COLOR:
                returnValues[0] = sprite.getColor().r;
                returnValues[1] = sprite.getColor().g;
                returnValues[2] = sprite.getColor().b;
                return 3;
            default:
                return -1;
        }
    }

    @Override
    public void setValues(Sprite sprite, int tweenType, float[] newValues) {
        switch (tweenType) {
            case POS_X:
                sprite.setX(newValues[0]);
                break;
            case POS_Y:
                sprite.setY(newValues[0]);
                break;
            case SCALE_X:
                sprite.setScale(newValues[0], sprite.getScaleY());
                break;
            case SCALE_Y:
                sprite.setScale(sprite.getScaleX(), newValues[0]);
                break;
            case ALPHA:
                sprite.setAlpha(newValues[0]);
                break;
            case ROT:
                sprite.setRotation(newValues[0]);
                break;
            case SCALE_XY:
                sprite.setScale(newValues[0], newValues[1]);
                break;
            case COLOR:
                sprite.setColor(newValues[0], newValues[1], newValues[2], sprite.getColor().a);
                break;
            default:
                break;
        }
    }
}
