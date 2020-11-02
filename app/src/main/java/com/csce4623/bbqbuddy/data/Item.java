package com.csce4623.bbqbuddy.data;

import android.content.ContentValues;
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
public class Item implements Serializable {

    //Static strings for the column names usable by other classes
    public static final String ITEM_ID = "id";
    public static final String ITEM_TITLE = "title";
    /*
    public static final String ITEM_CONTENT = "content";
    public static final String ITEM_COMPLETED = "completed";
    public static final String ITEM_DUEDATE = "dueDate";

     */


    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "title")
    private String title;

    /*
    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "completed")
    private Boolean completed;

    @ColumnInfo(name = "dueDate")
    private long dueDate;

     */

    //Following are getters and setters for all five member variables
    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
/*
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

 */

    //Create an Item from a ContentValues object
    public static Item fromContentValues(ContentValues contentValues){
        Item item = new Item();
        if(contentValues.containsKey(ITEM_ID)){
            item.setId(contentValues.getAsInteger(ITEM_ID));
        }
        if(contentValues.containsKey(ITEM_TITLE)){
            item.setTitle(contentValues.getAsString(ITEM_TITLE));
        }
        /*
        if(contentValues.containsKey(TODOITEM_CONTENT)){
            item.setContent(contentValues.getAsString(TODOITEM_CONTENT));
        }
        if(contentValues.containsKey(TODOITEM_COMPLETED)){
            item.setCompleted(contentValues.getAsBoolean(TODOITEM_COMPLETED));
        }
        if (contentValues.containsKey(TODOITEM_DUEDATE)){
            item.setDueDate(contentValues.getAsLong(TODOITEM_DUEDATE));
        }
        */
        return item;
    }


}
