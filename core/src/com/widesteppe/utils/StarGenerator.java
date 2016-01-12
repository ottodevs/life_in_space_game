package com.widesteppe.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

public class StarGenerator {
    private static ArrayList<Star> stars;
    public static final int NUMBER_OF_STARS = 480;
    public static ArrayList<Star> generateStars(float width) {
        if (stars == null) stars = new ArrayList<Star>();
        for (int i = 0; i < NUMBER_OF_STARS; i++) {
            stars.add(new Star(width));
        }
        return stars;
    }
    public static ArrayList<Star> getStars() {
        return stars;
    }
}
