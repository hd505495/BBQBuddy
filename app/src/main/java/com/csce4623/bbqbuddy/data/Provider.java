package com.csce4623.bbqbuddy.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Provider is a Content Provider
 * Calls to this provider should be made at the URI "content://com.csce4623.bbqbuddy.data"
 * appending the proper uri for the call (e.g. items or items/#)
 */
public class Provider extends ContentProvider {

    public static final String TAG = Provider.class.getName();
    private ItemDao itemDao;

    public static final String AUTHORITY = "com.csce4623.bbqbuddy.data";
    public static final String ITEM_TABLE_NAME = "items";

    public static final int ID_ITEM_DATA = 1;
    public static final int ID_ITEM_DATA_ITEM = 2;

    //URI matcher for switch statements on calls to the provider
    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    //Definition of two patterns (table or table + ID)
    static{
        uriMatcher.addURI(AUTHORITY, ITEM_TABLE_NAME, ID_ITEM_DATA);
        uriMatcher.addURI(AUTHORITY, ITEM_TABLE_NAME+"/*", ID_ITEM_DATA_ITEM);
    }

    /**
     * OnCreate -- get instance of Database through the Room DAO
     * Return false always? Not sure why here. TODO - Look into this.
     * @return
     */
    @Override
    public boolean onCreate() {
        itemDao = ItemDatabase.getInstance(getContext()).getItemDao();
        return false;
    }


    /**
     * Query - Loads all items if URI is the full table
     * @param uri - URI for call to provider
     * @param projection - Columns to return
     * @param selection - Which rows to select
     * @param selectionArgs - Arguments for that selection
     * @param sortOrder - Any sort parameters
     * @return - Cursor object with Items
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG,"query");
        Cursor cursor;
        switch (uriMatcher.match(uri)){
            case ID_ITEM_DATA:
                cursor = itemDao.findAll();
                if(getContext() != null){
                    cursor.setNotificationUri(getContext()
                            .getContentResolver(),uri);
                    return cursor;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return null;
    }

    /**
     * getType not implemented yet.
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri){
        return null;
    }


    /**
     * Insert data into the database
     * @param uri - Uri associated with the request
     * @param values - A content values object representing an Item
     * @return - URI of the newly created item
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG,"insert");
        switch (uriMatcher.match(uri)){
            case ID_ITEM_DATA:
                if(getContext() != null){
                    long id = itemDao.insert(Item.fromContentValues(values));
                    if (id != 0) {
                        getContext().getContentResolver().notifyChange(uri,null);
                        return ContentUris.withAppendedId(uri,id);
                    }
                }
            case ID_ITEM_DATA_ITEM:
                throw new IllegalArgumentException("Invalid URI: Insert failed " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    /**
     * Empty Constructor
     */
    public Provider() {
    }

    /**
     * Delete an item given a particular ID
     * @param uri - ToDoItem with appended ID
     * @param selection - Not used currently
     * @param selectionArgs - Not used currently
     * @return - Number of rows deleted (should be 0 or 1).
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete");
        switch (uriMatcher.match(uri)){
            case ID_ITEM_DATA:
                throw new IllegalArgumentException("Invalid uri: cannot delete");
            case ID_ITEM_DATA_ITEM:
                if(getContext() != null){
                    //Delete item in the DAO at a given ID
                    int count = itemDao.delete(ContentUris.parseId(uri));
                    getContext().getContentResolver().notifyChange(uri,null);
                    return count;
                }
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    /**
     * Updates an item given a ContentValues object representing the item.
     * Content Values object MUST specify ID to update, and may update other columns to be updated.
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        Log.d(TAG,"update");
        Log.d(TAG,uri.toString());
        switch (uriMatcher.match(uri)){
            case ID_ITEM_DATA:
                if(getContext() != null){
                    //Update DAO from given ToDoItem
                    int count = itemDao.update(Item.fromContentValues(values));
                    if(count != 0){
                        getContext().getContentResolver().notifyChange(uri,null);
                        return count;
                    }
                }
                break;
            case ID_ITEM_DATA_ITEM:
                throw new IllegalArgumentException("Invalid URI: cannot update");
            default:
                throw new IllegalArgumentException("Unknwon URI: " + uri);
        }
        return 0;
    }
}