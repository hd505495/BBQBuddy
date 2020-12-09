package com.csce4623.bbqbuddy.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * ToDoItem class
 * Implements serializable for easy pass through between intents
 * Includes Room annotations for five columns for each of five private members
 */
@Entity
public class Session implements Serializable {

    //Static strings for the column names usable by other classes
    public static final String SESSION_ID = "id";
    public static final String SESSION_DATE = "date";
    public static final String SESSION_MEAT = "meat";
   // public static final String SESSION_TIMERS = "timers";
    public static final String SESSION_NOTES = "notes";


    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "date")
    private long date;

    @ColumnInfo(name = "meat")
    private String meat;
/*
    @ColumnInfo(name = "timers")
    private List<TimerItem> timers;
*/
    @ColumnInfo(name = "notes")
    private String notes;

    //Following are getters and setters for all five member variables
    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getMeat() {
        return meat;
    }

    public void setMeat(String meat) {
        this.meat = meat;
    }
/*
    public List<TimerItem> getTimers() {
        return timers;
    }

    public void setTimers( (List<TimerItem> timers) {
        this.timers = timers;
    }
*/
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
