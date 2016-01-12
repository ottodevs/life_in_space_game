package com.widesteppe.actors;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class Crew {
   public static final int MEMBERS_NUMBER = 13;
    private ArrayList<Human> members = new ArrayList<Human>();
    public enum ROLE {
        CAPTAIN,
        SCIENTIST,
        MECHANIC,
        SCOUT
    }
    public enum NAMES {
        JACK(0, "Jack Miller", ROLE.CAPTAIN),
        CLARA(1, "Clara Johnson", ROLE.SCIENTIST),
        SOFIA(2, "Sofia Coleman", ROLE.SCIENTIST),
        SEB(3, "Sebastian Reed", ROLE.SCIENTIST),
        MICHAEL(4, "Michael Murphy", ROLE.SCIENTIST),
        IVAN(5, "Ivan Petrovich", ROLE.SCOUT),
        PATRICK(6, "Patrick Green", ROLE.SCOUT),
        GAVIN(7, "Gavin King", ROLE.SCOUT),
        ISAAC(8, "Isaac Young", ROLE.SCOUT),
        AVI(9, "Avi Goldberg", ROLE.MECHANIC),
        DAVID(10, "David Wood", ROLE.MECHANIC),
        DANIEL(11, "Daniel Wood", ROLE.MECHANIC),
        MARIO(12, "Mario Zeiler", ROLE.MECHANIC);
        private final int id;
        private final ROLE role;
        private String name;
        NAMES(int id, String name, ROLE role) {
            this.id = id;
            this.name = name;
            this.role = role;
        }

        public String getName() {
            return name;
        }
    }

    public Crew() {
        createCrew();
    }

    private void createCrew() {
        Human captain = new Human();
        members.add(captain);
    }

    public void update(float delta) {
        for (Human human : members) {
            human.update(delta);
        }
    }

    public void render(PolygonSpriteBatch polygonSpriteBatch) {
        for (Human human : members) {
            human.render(polygonSpriteBatch);
        }
    }
}
