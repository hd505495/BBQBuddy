package com.csce4623.bbqbuddy.utils;

import java.io.Serializable;

public class TimerItem implements Serializable {

    private String title;
    private int interval;
    private int numRepeats;


    //Following are getters and setters for all five member variables
    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getNumRepeats () {
        return numRepeats;
    }

    public void setNumRepeats(int numRepeats) {
        this.numRepeats = numRepeats;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
