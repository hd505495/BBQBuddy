package com.csce4623.bbqbuddy.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class TimerItem implements Parcelable {

    private String title;
    private int interval;
    private int numRepeats;

    // Constructor
    public TimerItem(String title, int interval, int numRepeats){
        this.title = title;
        this.interval = interval;
        this.numRepeats = numRepeats;
    }
    public TimerItem(Parcel in) {
        title = in.readString();
        interval = in.readInt();
        numRepeats = in.readInt();
    }

    public static final Creator<TimerItem> CREATOR = new Creator<TimerItem>() {
        @Override
        public TimerItem createFromParcel(Parcel in) {
            return new TimerItem(in);
        }

        @Override
        public TimerItem[] newArray(int size) {
            return new TimerItem[size];
        }
    };

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


    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeInt(interval);
        parcel.writeInt(numRepeats);
    }
}
