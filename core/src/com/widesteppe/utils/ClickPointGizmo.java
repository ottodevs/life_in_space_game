package com.widesteppe.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class ClickPointGizmo {
    private static ClickPointGizmo instance;
    private ArrayList<ClickPoint> clickPoints = new ArrayList<ClickPoint>();
    private ClickPointGizmo(){
        for (int i = 0; i< 10; i++) {
            clickPoints.add(new ClickPoint());
        }
    }
    public static ClickPointGizmo getInstance(){
        if (instance == null) instance = new ClickPointGizmo();
        return instance;
    }
    public void update(float delta){
        for(ClickPoint clickPoint : clickPoints) clickPoint.update(delta);
    }
    public void render(SpriteBatch batch){
        for(ClickPoint clickPoint: clickPoints) clickPoint.render(batch);
    }

    public void startNewClick(ClickPoint.CLICK_TYPE type, float x, float y) {
        for (ClickPoint clickPoint : clickPoints) {
            if (!clickPoint.isActive) {
                clickPoint.currentType = type;
                clickPoint.startAnimation(x, y);
                return;
            }
        }
        clickPoints.get(0).currentType = type;
        clickPoints.get(0).startAnimation(x,y);
    }

}
