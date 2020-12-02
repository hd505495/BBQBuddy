package com.csce4623.bbqbuddy.settimersactivity;

public interface SetTimersContract  {

    interface View{
        /**
         * setPresenter - sets the presenter associated with a View
         * @param presenter - the SetTimersContract.presenter instance
         */
        void setPresenter(SetTimersContract.Presenter presenter);

        /**
         * showTimerItems - takes a list of timerItems and populates a ListView
         */
        void showTimerItems();
    }

    interface Presenter{


        /**
         * start -- All procedures that need to be started
         * Ideally, should be coupled with a stop if any running tasks need to be destroyed.
         */
        void start();

    }

}

