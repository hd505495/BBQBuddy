package com.csce4623.bbqbuddy.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;

import com.csce4623.bbqbuddy.R;
import com.csce4623.bbqbuddy.data.Repository;
import util.AppExecutors;
import static com.google.common.base.Preconditions.checkNotNull;


public class MainActivity extends AppCompatActivity {

    //local instance of the toDoListPresenter, passed through into the toDoListFragment
    private MainPresenter mPresenter;
    SearchView searchView;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);

        // Set toolbar title color
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        setSupportActionBar(toolbar);

        // Create navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        // Set navigation drawer button and arrow color
        drawerToggle.getDrawerArrowDrawable().setColor(Color.parseColor("#ffffff"));

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    // Method that implements action bar navigation drawer icon when clicked on
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Method for setting menu items in our toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem menuItem = menu.findItem(R.id.searchIcon);
        return super.onCreateOptionsMenu(menu);
    }
}