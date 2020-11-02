package com.csce4623.bbqbuddy.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.csce4623.bbqbuddy.R;
import com.csce4623.bbqbuddy.data.Repository;
import util.AppExecutors;
import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends AppCompatActivity {

    //local instance of the toDoListPresenter, passed through into the toDoListFragment
    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //MainFragment -- Main view for the MainActivity
        MainFragment mainFragment =
                (MainFragment) getSupportFragmentManager().findFragmentById(R.id.mainFragmentFrame);
        if (mainFragment == null) {
            // Create the fragment
            mainFragment = MainFragment.newInstance();
            // Check that it is not null
            checkNotNull(mainFragment);
            // Populate the fragment into the activity
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.mainFragmentFrame, mainFragment);
            transaction.commit();
        }
        //Get an instance of the MainPresenter
        //Parameters - Repository - Instance of the Repository
        //Fragment - the View to be communicated to by the presenter
        // Repository needs a thread pool to execute database/network calls in other threads
        // Repository needs the application context to be able to make calls to the ContentProvider
        mPresenter = new MainPresenter(Repository.getInstance(new AppExecutors(),getApplicationContext()),mainFragment);

    }
}