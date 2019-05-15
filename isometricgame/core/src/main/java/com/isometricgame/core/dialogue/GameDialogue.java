package com.isometricgame.core.dialogue;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * dialog
 * 
 * General structure for dialog that needs to be added into the game. 
 * 
 */
public class GameDialogue {

    private SpriteBatch textBatch; 
    private String textmessage; 
    private double minx; 
    private double miny; 
    private double maxx; 
    private double maxy; 

    public GameDialogue(double maxx, double maxy, double minx, double miny, String textmessage){
        this.maxx = maxx; 
        this.maxy = maxy; 
        this.minx = minx; 
        this.miny = miny; 
        this.textmessage = textmessage; 
    }

    public String getTextmessage() {
        return textmessage;
    }

    /**
     * @param textmessa    private String textmessage2;ge the textmessage to set
     */
    public void setTextmessage(String textmessage) {
        this.textmessage = textmessage;
    }

    /**
     * @return the minx
     */
    public double getMinx() {
        return minx;
    }

    public void setMinx(double minx) {
        this.minx = minx;
    }

    /**
     * @return the miny
     */
    public double getMiny() {
        return miny;
    }

    /**
     * @param miny the miny to set
     */
    public void setMiny(double miny) {
        this.miny = miny;
    }

    /**
     * @return the maxx
     */
    public double getMaxx() {
        return maxx;
    }

    /**
     * @param maxx the maxx to set
     */
    public void setMaxx(double maxx) {
        this.maxx = maxx;
    }

    /**
     * @return the maxy
     */
    public double getMaxy() {
        return maxy;
    }

    /**
     * @param maxy the maxy to set
     */
    public void setMaxy(double maxy) {
        this.maxy = maxy;
    }    
}