package com.csce4623.bbqbuddy.activesessionactivity;

import androidx.annotation.NonNull;

import com.csce4623.bbqbuddy.data.Repository;

public class ActiveSessionPresenter implements ActiveSessionContract.Presenter{

    //Data repository instance
    //Currently has a memory leak -- need to refactor context passing
    private static Repository mRepository;
    //View instance
    private final ActiveSessionContract.View mView;

    /**
     * ActiveSessionPresenter constructor
     * @param itemRepository - Data repository instance
     * @param itemView - ActiveSessionContract.View instance
     */
    public ActiveSessionPresenter(@NonNull Repository itemRepository, @NonNull ActiveSessionContract.View itemView){
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
