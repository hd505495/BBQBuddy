package com.csce4623.bbqbuddy.recipeactivity;

public interface RecipeContract {

    interface View{
        /**
         * setPresenter - sets the presenter associated with a View
         * @param presenter - the ToDoListContract.presenter instance
         */
        void setPresenter(RecipeContract.Presenter presenter);

        /**
         * showItems - takes a list of Items and populates a ListView
         * @param ItemList - List of Items
         */
        //void showItems(List<Item> ItemList);

    }

    interface Presenter{
        /**
         * loadItems - Loads all Items from the Repository
         */
        //void loadItems();

        /**
         * start -- All procedures that need to be started
         * Ideally, should be coupled with a stop if any running tasks need to be destroyed.
         */
        void start();

    }

}
