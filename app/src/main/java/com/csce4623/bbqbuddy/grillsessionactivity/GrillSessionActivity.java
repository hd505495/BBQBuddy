package com.csce4623.bbqbuddy.grillsessionactivity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.csce4623.bbqbuddy.R;
import com.csce4623.bbqbuddy.data.Repository;

import util.AppExecutors;

public class GrillSessionActivity extends AppCompatActivity implements GrillSessionContract.View {

    private GrillSessionContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_layout);
        ImageView recipeImage = (ImageView) findViewById(R.id.imageView);

        mPresenter = new GrillSessionPresenter(Repository.getInstance(new AppExecutors(),getApplicationContext()), this);

    }

    @Override
    public void onResume(){
        super.onResume();
        mPresenter.start();
    }


    /**
     * set the presenter for this view
     * @param presenter - the GrillSessionContract.presenter instance
     */
    @Override
    public void setPresenter(GrillSessionContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
