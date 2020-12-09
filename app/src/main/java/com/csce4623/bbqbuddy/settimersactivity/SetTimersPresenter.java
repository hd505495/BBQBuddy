package com.csce4623.bbqbuddy.settimersactivity;

import androidx.annotation.NonNull;

import com.csce4623.bbqbuddy.data.Repository;

public class SetTimersPresenter implements SetTimersContract.Presenter{

    //Data repository instance
    //Currently has a memory leak -- need to refactor context passing
    private static Repository mRepository;
    //View instance
    private final SetTimersContract.View mView;

    /**
     * SetTimersPresenter constructor
     * @param itemRepository - Data repository instance
     * @param itemView - SetTimersContract.View instance
     */
    public SetTimersPresenter(@NonNull Repository itemRepository, @NonNull SetTimersContract.View itemView){
        mRepository = itemRepository;
        mView = itemView;
        //Make sure to pass the presenter into the view!
        mView.setPresenter(this);
    }

    @Override
    public void start(){
        mView.showTimerItems();
    }

}
