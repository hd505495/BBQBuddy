package com.csce4623.bbqbuddy.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//Room Database implementation
//Don't touch unless you know what you are doing.
@Database(entities = {Item.class}, version = 1, exportSchema = false)
public abstract class ItemDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "item_db";
    private static ItemDatabase INSTANCE;

    public static ItemDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context,ItemDatabase.class,DATABASE_NAME).build();
        }
        return INSTANCE;
    }

    public abstract ItemDao getItemDao();

}