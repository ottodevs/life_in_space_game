package com.widesteppe.actors;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.widesteppe.Director;
import com.widesteppe.utils.ClickPoint;
import com.widesteppe.utils.ClickPointGizmo;

import java.util.ArrayList;

public class Crew {
   public static final int MEMBERS_NUMBER = 13;
    private ArrayList<Human> members = new ArrayList<Human>();
    private Robot robot;
    private Stage spaceshipStage;


    public ArrayList<Human> getMembers() {
        return members;
    }

    public Human getHumanByInfo(MEMBER_INFO info) {
        for (Human human : members) {
            if (human.getInfo() == info) {
                return human;
            }
        }
        return null;
    }


    public enum ROLE {
        CAPTAIN,
        SCIENTIST,
        MECHANIC,
        SCOUT,
        ROBOT
    }
    public enum MEMBER_INFO {
        JACK(0, "Jack Miller", ROLE.CAPTAIN, Director.SCHEDULE.FIRST_SHIFT, 345, 260, 255, 265, false),
        CLARA(1, "Clara Johnson", ROLE.SCIENTIST, Director.SCHEDULE.FIRST_SHIFT, 60,46, 33, 23, false),
        SOFIA(2, "Sofia Coleman", ROLE.SCIENTIST, Director.SCHEDULE.FIRST_SHIFT, 53,67, 4, 14, true),
        SEB(3, "Sebastian Reed", ROLE.SCIENTIST, Director.SCHEDULE.SECOND_SHIFT, 61,47, 33, 24, false),
        MICHAEL(4, "Michael Murphy", ROLE.SCIENTIST, Director.SCHEDULE.SECOND_SHIFT, 54,68, 4, 15, true),
        IVAN(5, "Ivan Petrovich", ROLE.SCOUT, Director.SCHEDULE.SECOND_SHIFT, 178.5f, 100, 125, 135, false),
        PATRICK(6, "Patrick Green", ROLE.SCOUT, Director.SCHEDULE.SECOND_SHIFT, 191.5f, 108, 154, 143, true),
        GAVIN(7, "Gavin King", ROLE.SCOUT, Director.SCHEDULE.FIRST_SHIFT, 178.5f, 103, 154, 144, false),
        ISAAC(8, "Isaac Young", ROLE.SCOUT, Director.SCHEDULE.FIRST_SHIFT, 191.5f, 111, 125, 136, true),
        AVI(9, "Avi Goldberg", ROLE.MECHANIC, Director.SCHEDULE.FIRST_SHIFT, 310, 310, 243, 224, false),
        DAVID(10, "David Wood", ROLE.MECHANIC, Director.SCHEDULE.SECOND_SHIFT, 300, 290, 216, 225, false),
        DANIEL(11, "Daniel Wood", ROLE.MECHANIC, Director.SCHEDULE.SECOND_SHIFT, 290, 300, 243, 235, true),
        MARIO(12, "Mario Zeiler", ROLE.MECHANIC, Director.SCHEDULE.FIRST_SHIFT, 272, 272, 216, 234, true),
        ROBOT(13, "Sam", ROLE.ROBOT, null, 0, 0, 0, 0, false);
        private final int id;
        private final ROLE role;
        private Director.SCHEDULE schedule;
        private float workPlaceAngle;
        private float workPlaceAngle2;
        private float sleepPlaceAngle;
        private float eatPlaceAngle;
        private boolean isSleepOnTop;
        private String name;
        MEMBER_INFO(int id, String name, ROLE role, Director.SCHEDULE schedule,
                    float workPlaceAngle, float workPlaceAngle2, float sleepPlaceAngle, float eatPlaceAngle, boolean isSleepOnTop) {
            this.id = id;
            this.name = name;
            this.role = role;
            this.schedule = schedule;
            this.workPlaceAngle = workPlaceAngle;
            this.workPlaceAngle2 = workPlaceAngle2;
            this.sleepPlaceAngle = sleepPlaceAngle;
            this.eatPlaceAngle = eatPlaceAngle;
            this.isSleepOnTop = isSleepOnTop;
        }

        public float getWorkPlaceAngle2() {
            return workPlaceAngle2;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public ROLE getRole() {
            return role;
        }

        public Director.SCHEDULE getSchedule() {
            return schedule;
        }

        public float getWorkPlaceAngle() {
            return workPlaceAngle;
        }

        public float getSleepPlaceAngle() {
            return sleepPlaceAngle;
        }

        public float getEatPlaceAngle() {
            return eatPlaceAngle;
        }

        public boolean isSleepOnTop() {
            return isSleepOnTop;
        }
        public static MEMBER_INFO getInfoByName(String name) {
            for (MEMBER_INFO info : MEMBER_INFO.values()) {
                if (info.toString().equals(name)) {
                    return info;
                }
            }
            return null;
        }
    }

    public Crew(Stage spaceshipStage) {
        this.spaceshipStage = spaceshipStage;
        createCrew();
    }

    private void createCrew() {
        robot = new Robot(MEMBER_INFO.ROBOT);
        spaceshipStage.addActor(robot.getLabelContainer());
        spaceshipStage.addActor(robot.getMessageContainer());
        Human captain = new Human(MEMBER_INFO.JACK, 290f);
        members.add(captain);
        Human clara = new Human(MEMBER_INFO.CLARA, 5);
        members.add(clara);
        Human sofia = new Human(MEMBER_INFO.SOFIA, 20);
        members.add(sofia);
        Human seb = new Human(MEMBER_INFO.SEB, 40);
        members.add(seb);
        Human michael = new Human(MEMBER_INFO.MICHAEL, 60);
        members.add(michael);
       Human ivan = new Human(MEMBER_INFO.IVAN, 120);
        members.add(ivan);
        Human patrick = new Human(MEMBER_INFO.PATRICK, 140);
        members.add(patrick);
        Human gavin = new Human(MEMBER_INFO.GAVIN, 160);
        members.add(gavin);
        Human isaac = new Human(MEMBER_INFO.ISAAC, 180);
        members.add(isaac);

        Human avi = new Human(MEMBER_INFO.AVI, 260);
        members.add(avi);
        Human david = new Human(MEMBER_INFO.DAVID, 280);
        members.add(david);
        Human daniel = new Human(MEMBER_INFO.DANIEL, 300);
        members.add(daniel);
        Human mario = new Human(MEMBER_INFO.MARIO, 240);
        members.add(mario);

        for (Human human : members) {
            spaceshipStage.addActor(human.getLabelContainer());
            spaceshipStage.addActor(human.getMessageContainer());
        }

    }

    public void update(float delta) {
        robot.update(delta);
        for (Human human : members) {
            human.update(delta);
        }
        checkLabelCollisions();
    }

    private void checkLabelCollisions() {

        checkAstronautLabelCollision(robot);
        for (int i = 0; i < members.size(); i++) {
            checkAstronautLabelCollision(members.get(i));
        }
    }

    private void checkAstronautLabelCollision(Astronaut astronaut) {
        boolean isLabelCollided = false;
        for (Human human : members) {
            if (astronaut.getInfo().getId() == human.getInfo().getId()) continue;
            if (Vector2.dst(astronaut.getCenterX(), astronaut.getCenterY(), human.getCenterX(), human.getCenterY()) < Astronaut.PERSONAL_AREA_RADIUS * 2) {
                astronaut.getLabelContainer().addAction(Actions.alpha(0, 0.2f));
                human.getLabelContainer().addAction(Actions.alpha(0, 0.2f));
                isLabelCollided = true;
                break;
            }
        }
        if (!isLabelCollided && astronaut.getLabelContainer().getColor().a == 0) {
            astronaut.getLabelContainer().addAction(Actions.alpha(1, 0.2f));
        }
    }

    public void render(PolygonSpriteBatch polygonSpriteBatch) {
        for (Human human : members) {
            if (human.getCurrentActivity() == Human.ACTIVITY.SLEEP)
            human.render(polygonSpriteBatch);
        }
        for (Human human : members) {
            if (human.getCurrentJob() != Human.JOB.RUN && human.getCurrentActivity() != Human.ACTIVITY.SLEEP)
                human.render(polygonSpriteBatch);
        }
        robot.render(polygonSpriteBatch);
        for (Human human : members) {
            if (human.getCurrentJob() == Human.JOB.RUN)
                human.render(polygonSpriteBatch);
        }

    }

    public boolean checkTouchOnHuman(float x, float y) {
        for (Human human : members) {
            if (human.isContains(x, y)) {
                robot.goToHuman(human);
                ClickPointGizmo.getInstance().startNewClick(ClickPoint.CLICK_TYPE.BLUE, x, y);
                return true;
            }
        }
        return false;
    }
    public Robot getRobot() {
        return robot;
    }
}
