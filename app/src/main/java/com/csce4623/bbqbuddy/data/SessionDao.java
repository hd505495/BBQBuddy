package com.csce4623.bbqbuddy.data;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SessionDao {
    /**
     * Insert an item into the table
     * @return row ID for newly inserted data
     */
    @Insert
    long insert(Session session);    /**
     * select all items
     * @return A {@link Cursor} of all items in the table
     */
    @Query("SELECT * FROM Session")
    Cursor findAll();      /**
     * Delete a item by ID
     * @return A number of items deleted
     */
    @Query("DELETE FROM Session WHERE id = :id ")
    int delete(long id);    /**
     * Update the item
     * @return A number of items updated
     */
    @Update
    int update(Session session);
}
