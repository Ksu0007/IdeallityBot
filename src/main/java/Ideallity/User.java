package Ideallity;

import java.awt.*;

public class User {
    private Long chatId;
    private Double idealWeight;
    private Double normalWeight;
    private Double realWeight;

    private Double wishWeight;
    private Float wristSize;
    private int height;
    private String gender;

    private int age;
    private double activityLevel;

    private int goalDays;

    private BotState botState = BotState.START;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Double getIdealWeight() {
        return idealWeight;
    }

    public void setIdealWeight(Double idealWeight) {
        this.idealWeight = idealWeight;
    }

    public Double getNormalWeight() {
        return normalWeight;
    }

    public void setNormalWeight(Double normalWeight) {
        this.normalWeight = normalWeight;
    }

    public Double getRealWeight() {
        return realWeight;
    }

    public void setRealWeight(Double realWeight) {
        this.realWeight = realWeight;
    }

    public Double getWishWeight() {
        return wishWeight;
    }

    public void setWishWeight(Double wishWeight) {
        this.wishWeight = wishWeight;
    }

    public Float getWristSize() {
        return wristSize;
    }

    public void setWristSize(Float wristSize) {
        this.wristSize = wristSize;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(double activityLevel) {
        this.activityLevel = activityLevel;
    }

    public int getGoalDays() {
        return goalDays;
    }

    public void setGoalDays(int goalDays) {
        this.goalDays = goalDays;
    }

    public BotState getBotState() {
        return botState;
    }

    public void setBotState(BotState botState) {
        this.botState = botState;
    }



}
