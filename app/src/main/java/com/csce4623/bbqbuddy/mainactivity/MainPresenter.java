package com.csce4623.bbqbuddy.mainactivity;

import android.util.Log;

import androidx.annotation.NonNull;

import com.csce4623.bbqbuddy.data.DataSource;
import com.csce4623.bbqbuddy.data.Item;
import com.csce4623.bbqbuddy.data.Repository;

import java.util.List;

public class MainPresenter implements MainContract.Presenter {

    //Data repository instance
    //Currently has a memory leak -- need to refactor context passing
    private static Repository mRepository;
    //View instance
    private final MainContract.View mView;

    /**
     * ToDoListPresenter constructor
     * @param itemRepository - Data repository instance
     * @param itemView - MainContract.View instance
     */
    public MainPresenter(@NonNull Repository itemRepository, @NonNull MainContract.View itemView){
        mRepository = itemRepository;
        mView = itemView;
        //Make sure to pass the presenter into the view!
        mView.setPresenter(this);
    }

    @Override
    public void start(){
        //Load all toDoItems
        loadItems();
    }

    /**
     * loadItems -- loads all items from Repository
     * Two callbacks -- success/failure
     */
    @Override
    public void loadItems(){
        Log.d("Presenter","Loading Items");
        mRepository.getItems(new DataSource.LoadItemsCallback() {
            @Override
            public void onItemsLoaded(List<Item> Items) {
                Log.d("PRESENTER","Loaded");

                //mView.showItems(Items);
            }

            @Override
            public void onDataNotAvailable() {
                Log.d("PRESENTER","Not Loaded");
            }
        });
    }

}

