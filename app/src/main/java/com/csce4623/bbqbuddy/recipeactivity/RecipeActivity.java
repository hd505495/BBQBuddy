package com.csce4623.bbqbuddy.recipeactivity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.csce4623.bbqbuddy.R;
import com.csce4623.bbqbuddy.data.Repository;

import util.AppExecutors;

public class RecipeActivity extends AppCompatActivity implements RecipeContract.View {

    private RecipeContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_layout);
        ImageView recipeImage = (ImageView) findViewById(R.id.imageView);

        mPresenter = new RecipePresenter(Repository.getInstance(new AppExecutors(),getApplicationContext()), this);

        int imageResource = getResources().getIdentifier("drawable/cornbread_stock_img", null, this.getPackageName());
        recipeImage.setImageResource(imageResource);

    }

    @Override
    public void onResume(){
        super.onResume();
        mPresenter.start();
    }


    /**
     * set the presenter for this view
     * @param presenter - the MainContract.presenter instance
     */
    @Override
    public void setPresenter(RecipeContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
