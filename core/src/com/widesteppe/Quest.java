package com.widesteppe;


import com.widesteppe.actors.Astronaut;
import com.widesteppe.actors.Crew;
import com.widesteppe.actors.Human;
import com.widesteppe.actors.Robot;
import com.widesteppe.screens.GameScreen;
import com.widesteppe.screens.MenuScreen;
import com.widesteppe.utils.*;

import java.util.ArrayList;

public class Quest {
    private int id = 0;
    private String currentQuestMessage;
    private Crew.MEMBER_INFO currentQuestPerson;
    private Dialog currentDialog;
    private Director director;
    private boolean isDialog;
    private final float MAX_IDLE_TIME = 30;
    private float idleTimer = MAX_IDLE_TIME;
    private Robot robot;
    private Human currentQuestHuman;
    private int currentReplicI = 0;
    private ArrayList<Speech> currentSpeech;
    private ArrayList<Message> currentMessage;
    private boolean isRobotTalking;
    private boolean isHumanTalking;
    private boolean responceChoosen;
    private int lastMessageId;


    public Quest(Director director, int initialId) {
        id = initialId;
        this.director = director;
        robot = director.getCrew().getRobot();
        createQuest();

    }

    private void createQuest() {
        currentDialog = AssetsLoader.dialogs.get(id);
        currentQuestPerson = Crew.MEMBER_INFO.getInfoByName(currentDialog.person);
        currentQuestMessage = currentDialog.quest;
        currentQuestHuman = director.getCrew().getHumanByInfo(currentQuestPerson);
        currentSpeech = currentDialog.speech;

    }


    public void update(float delta) {
        if (!isDialog && !MainGUI.isGameOVER) {
            idleTimer += delta;
            if (idleTimer > MAX_IDLE_TIME) {
                idleTimer = 0;
                robotSayQuestMessage();
            }
        }
        if (robot.isRichedPerson() && !isDialog && robot.getTargetHuman() != null && robot.getTargetHuman().getInfo() == currentQuestPerson) {
            startDialog();
        }
        if (isDialog) {
            if (isRobotTalking && robot.getCurrentState() == Astronaut.AI_STATE.IDLE) {
                iterateReplica();
            }
            if (isHumanTalking && currentQuestHuman.getCurrentState() == Astronaut.AI_STATE.IDLE) {
                iterateReplica();
            }
            if (!isDialog) return;
            currentMessage = currentSpeech.get(currentReplicI).message;

            if (currentReplicI + 1 < currentSpeech.size() && currentSpeech.get(currentReplicI + 1).message.size() > 1 && currentSpeech.get(currentReplicI + 1).isRobot && !responceChoosen) {
                currentQuestHuman.isWaitingForResponce = true;
                responceChoosen = true;
                ChooserPopup.getInstance().open(false, currentSpeech.get(currentReplicI + 1).message.get(0).text,
                        currentSpeech.get(currentReplicI + 1).message.get(1).text,
                        currentSpeech.get(currentReplicI + 1).message.get(2).text, new ThreeOptionChooser() {
                            @Override
                            public void optionOne() {
                                lastMessageId = 0;
                            }

                            @Override
                            public void optionTwo() {
                                lastMessageId = 1;
                            }

                            @Override
                            public void optionThree() {
                                lastMessageId = 2;
                            }

                            @Override
                            public void close() {
                                currentQuestHuman.isWaitingForResponce = false;
                            }
                        });
            }

            if (currentSpeech.get(currentReplicI).isRobot && robot.getCurrentState() == Astronaut.AI_STATE.IDLE) {
                if (currentMessage.size() == 1) {
                    robot.sayMessage(currentMessage.get(0).text);
                } else {
                    robot.sayMessage(currentMessage.get(lastMessageId).text);

                }
                responceChoosen = false;
            } else if (!currentSpeech.get(currentReplicI).isRobot && currentQuestHuman.getCurrentState() == Astronaut.AI_STATE.IDLE) {
                if (currentMessage.size() == 1) {
                    currentQuestHuman.sayMessage(currentMessage.get(0).text);
                    updateEmo(currentMessage.get(0).emo);
                } else {
                    currentQuestHuman.sayMessage(currentMessage.get(lastMessageId).text);
                    updateEmo(currentMessage.get(lastMessageId).emo);
                }

            }


            if (robot.getCurrentState() == Astronaut.AI_STATE.TALKING) {
                isRobotTalking = true;
            }
            if (currentQuestHuman.getCurrentState() == Astronaut.AI_STATE.TALKING) {
                isHumanTalking = true;
            }


        }
    }

    private void updateEmo(int emo) {
        switch (emo) {
            case 0:
                return;
            case 1:
                currentQuestHuman.setEmotion(Human.EMOTION.HAPPY);
                break;
            case 2:
                currentQuestHuman.setEmotion(Human.EMOTION.SAD);
                break;
            case 3:
                currentQuestHuman.setEmotion(Human.EMOTION.TIRED);
                break;
            case 4:
                currentQuestHuman.setEmotion(Human.EMOTION.ANGRY);
                break;
            case 5:
                currentQuestHuman.setEmotion(Human.EMOTION.SURPRISED);
                break;
            default:
                currentQuestHuman.setEmotion(Human.EMOTION.HAPPY);
        }
    }

    private void iterateReplica() {
        if (currentReplicI < currentSpeech.size() - 1) {
            currentReplicI++;
        } else {
            stopDialog();
        }
        isHumanTalking = false;
        isRobotTalking = false;
    }

    private void stopDialog() {
        isDialog = false;
        isRobotTalking = false;
        isHumanTalking = false;
        robot.isDialog = false;
        currentQuestHuman.isDialog = false;
        currentReplicI = 0;
        checkPlot();
    }

    private void checkPlot() {
        if (id < AssetsLoader.dialogs.size() - 1) {
            id++;
            createQuest();
            MyPrefs.getInstance().saveProgress(id, MainTimer.getTimeInSeconds());
        } else {
            MainGUI.isGameOVER = true;
            MyPrefs.getInstance().saveGameOver();
        }
    }

    private void startDialog() {
        if (!MainGUI.isGameOVER) {
            isDialog = true;
            robot.isDialog = true;
            currentQuestHuman.isDialog = true;
            currentQuestHuman.setToIdle();
            currentQuestHuman.setCurrentActivity(Human.ACTIVITY.NONE);
        }
    }

    private void robotSayQuestMessage() {
        robot.sayMessage(currentQuestMessage);
    }

    public boolean isDialog() {
        return isDialog;
    }

    public void forceFinishReplic() {
        if (isRobotTalking) {
            robot.forceFinishMessage();
        }
        if (isHumanTalking) {

            currentQuestHuman.forceFinishMessage();
        }
    }

    public void setId(int id) {
        this.id = id;
        createQuest();
    }

    public void reset() {
        if (isDialog) {
            isDialog = false;
            isRobotTalking = false;
            isHumanTalking = false;
            robot.isDialog = false;
            currentQuestHuman.isDialog = false;
            currentReplicI = 0;
            robot.forceFinishMessage();
            currentQuestHuman.forceFinishMessage();
        } else if (robot.getCurrentState() == Astronaut.AI_STATE.TALKING){
            idleTimer = MAX_IDLE_TIME - 2;
            robot.forceFinishMessage();
        }
    }
}
