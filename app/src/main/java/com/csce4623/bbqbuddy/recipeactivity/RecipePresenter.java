package com.csce4623.bbqbuddy.recipeactivity;

import androidx.annotation.NonNull;

import com.csce4623.bbqbuddy.data.Repository;

public class RecipePresenter implements RecipeContract.Presenter{

    //Data repository instance
    //Currently has a memory leak -- need to refactor context passing
    private static Repository mRepository;
    //View instance
    private final RecipeContract.View mView;

    /**
     * ToDoListPresenter constructor
     * @param itemRepository - Data repository instance
     * @param itemView - MainContract.View instance
     */
    public RecipePresenter(@NonNull Repository itemRepository, @NonNull RecipeContract.View itemView){
        mRepository = itemRepository;
        mView = itemView;
        //Make sure to pass the presenter into the view!
        mView.setPresenter(this);
    }

    @Override
    public void start(){
    }

}
