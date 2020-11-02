package com.csce4623.bbqbuddy.data;

import androidx.annotation.NonNull;
import java.util.List;

/**
 * Interface for any implementation of a DataSource
 * (Currently only have one - a local ContentProvider based implementation (@ItemRepository)
 */
public interface DataSource {

    /**
     * LoadItemsCallback interface
     * Example of how to implement callback functions depending on the result of functions in interfaces
     * Currently, onDataNotAvailable is not implemented
     */
    interface LoadItemsCallback {

        void onItemsLoaded(List<Item> Items);

        void onDataNotAvailable();
    }

    /**
     * GetItemsCallback interface
     * Not currently implemented
     */
    interface GetItemCallback {

        void onItemLoaded(Item task);

        void onDataNotAvailable();
    }

    /**
     * getItems loads all Items, calls either success or failure function above
     * @param callback - Callback function
     */
    void getItems(@NonNull LoadItemsCallback callback);

    /**
     * getItem - Get a single Item - currently not implemented
     * @param ItemId - String of the current ItemID to be retrieved
     * @param callback - Callback function
     */
    void getItem(@NonNull String ItemId, @NonNull GetItemCallback callback);

    /**
     * SaveItem saves an Item to the database - No callback (should be implemented for
     * remote databases)
     * @param Item
     */
    void saveItem(@NonNull final Item Item);

    /**
     * CreateItem adds an Item to the database - No callback (should be implemented for
     * remote databases)
     * @param Item
     */
    void createItem(@NonNull Item Item);

}
