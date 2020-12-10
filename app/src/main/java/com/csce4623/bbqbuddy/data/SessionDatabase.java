package com.csce4623.bbqbuddy.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//Room Database implementation
//Don't touch unless you know what you are doing.
@Database(entities = {Session.class}, version = 1, exportSchema = false)
public abstract class SessionDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "session_db";
    private static SessionDatabase INSTANCE;

    public static SessionDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, SessionDatabase.class,DATABASE_NAME).build();
        }
        return INSTANCE;
    }

    public abstract SessionDao getSessionDao();

}