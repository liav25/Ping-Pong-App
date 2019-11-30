package com.example.cspingpong;

import java.util.concurrent.TimeUnit;

public class MyTurnSlot {

    private int slotImage;
    private String turnTime;
    private String turnAgainst;

    public MyTurnSlot(){

    }

    public MyTurnSlot(int slotImage, String turnTime, String turnAgainst){
        this.slotImage = slotImage;
        this.turnTime = turnTime;
        this.turnAgainst = turnAgainst;
    }

    public int getSlotImage(){
        return this.slotImage;
    }

    public String getTurnTime(){
        return this.turnTime;
    }

    public String getTurnAgainst(){
        return this.turnAgainst;
    }

    public void setSlotImage(int slotImage){
        this.slotImage = slotImage;
    }

    public void setTurnTime(String turnTime){
        this.turnTime= turnTime;
    }

    public void setTurnAgainst(String turnAgainst){
        this.turnAgainst = turnAgainst;
    }


}
