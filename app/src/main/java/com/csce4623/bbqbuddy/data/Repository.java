package com.csce4623.bbqbuddy.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import util.AppExecutors;

/**
 * Repository class - implements the DataSource interface
 */
public class Repository implements DataSource {

    private SessionDao sessionDao;
    //Memory leak here by including the context - Fix this at some point
    private static volatile Repository INSTANCE;

    //Thread pool for execution on other threads
    private AppExecutors mAppExecutors;
    //Context for calling Provider
    private Context mContext;

    /**
     * private constructor - prevent direct instantiation
     * @param appExecutors - thread pool
     * @param context
     */
    private Repository(@NonNull AppExecutors appExecutors, @NonNull Context context){
        sessionDao = SessionDatabase.getInstance(context).getSessionDao();
        mAppExecutors = appExecutors;
        mContext = context;
    }

    /**
     * public constructor - prevent creation of instance if one already exists
     * @param appExecutors
     * @param context
     * @return
     */
    public static Repository getInstance(@NonNull AppExecutors appExecutors, @NonNull Context context){
        if(INSTANCE == null){
            synchronized (Repository.class){
                if(INSTANCE == null){
                    INSTANCE = new Repository(appExecutors, context);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * getItems runs query in a separate thread, and on success loads data from cursor into a list
     * @param callback
     */
    @Override
    public void getItems(@NonNull final LoadItemsCallback callback) {
        Log.d("REPOSITORY","Loading...");
        Runnable runnable = new Runnable(){
            @Override
            public void run() {

                final Cursor c = sessionDao.findAll();
                final List<Session> sessions = new ArrayList<Session>(0);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(c == null){
                            callback.onDataNotAvailable();
                        } else{
                            while(c.moveToNext()) {
                                Session session = new Session();
                                session.setId(c.getInt(c.getColumnIndex(Session.SESSION_ID)));
                                session.setDate(c.getLong(c.getColumnIndex(Session.SESSION_DATE)));
                                session.setMeat(c.getString(c.getColumnIndex(Session.SESSION_MEAT)));
                                //session.setTimers((List<TimerItem>) (c.getColumnIndex(Session.SESSION_TIMERS)));
                                session.setNotes(c.getString(c.getColumnIndex(Session.SESSION_NOTES)));
                                sessions.add(session);
                            }
                            c.close();
                            callback.onItemsLoaded(sessions);
                        }
                    }
                });

            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    /**
     * Not implemented yet
     * @param ItemId
     * @param callback
     */
    @Override
    public void getItem(@NonNull String ItemId, @NonNull GetItemCallback callback) {
        Log.d("REPOSITORY","GetItem");
    }

    /**
     * saveItem runs contentProvider update in separate thread
     * @param session
     */
    @Override
    public void saveItem(@NonNull final Session session) {
        Log.d("REPOSITORY","SaveItem");
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                final int numUpdated = sessionDao.update(session);
                Log.d("REPOSITORY","Update Session updated " + String.valueOf(numUpdated) + " rows");
            }
        };
        mAppExecutors.diskIO().execute(runnable);

    }

    /**
     * createItem runs contentProvider insert in separate thread
     * @param session
     */
    @Override
    public void createItem(@NonNull final Session session) {
        Log.d("REPOSITORY","CreateItem");
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                final long id = sessionDao.insert(session);
                Log.d("REPOSITORY","Create Item finished with id" + id);
            }
        };
        mAppExecutors.diskIO().execute(runnable);

    }

    @Override
    public void deleteItem(@NonNull final long id) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int isDeleted = sessionDao.delete(id);
                if (isDeleted == 0) {
                    Log.d("REPOSITORY", "Not Deleted");
                } else {
                    Log.d("REPOSITORY", "Deleted");
                }
            }
        };
        mAppExecutors.diskIO().execute(runnable);

    }
}
