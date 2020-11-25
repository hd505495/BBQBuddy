package com.csce4623.bbqbuddy.grillsessionactivity;

import androidx.annotation.NonNull;

import com.csce4623.bbqbuddy.data.Repository;

public class GrillSessionPresenter implements GrillSessionContract.Presenter{

    //Data repository instance
    //Currently has a memory leak -- need to refactor context passing
    private static Repository mRepository;
    //View instance
    private final GrillSessionContract.View mView;

    /**
     * ToDoListPresenter constructor
     * @param itemRepository - Data repository instance
     * @param itemView - GrillSessionContract.View instance
     */
    public GrillSessionPresenter(@NonNull Repository itemRepository, @NonNull GrillSessionContract.View itemView){
        mRepository = itemRepository;
        mView = itemView;
        //Make sure to pass the presenter into the view!
        mView.setPresenter(this);
    }

    @Override
    public void start() {
    }
}
