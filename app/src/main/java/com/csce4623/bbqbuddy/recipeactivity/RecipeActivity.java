package com.csce4623.bbqbuddy.recipeactivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.csce4623.bbqbuddy.R;
import com.csce4623.bbqbuddy.data.Repository;

import java.util.ArrayList;

import util.AppExecutors;

public class RecipeActivity extends AppCompatActivity implements RecipeContract.View {

    private RecipeContract.Presenter mPresenter;

    ArrayList<String> recipeIngredientsName = new ArrayList<>();
    ArrayList<String> recipeIngredientsAmountValue = new ArrayList<>();
    ArrayList<String> recipeIngredientsAmountUnit = new ArrayList<>();
    Integer position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_layout);
        ImageView recipeImage = (ImageView) findViewById(R.id.imageView);

        mPresenter = new RecipePresenter(Repository.getInstance(new AppExecutors(),getApplicationContext()), this);

        // Commented out for now. Throws errors.
        //int imageResource = getResources().getIdentifier("drawable/cornbread_stock_img", null, this.getPackageName());
        //recipeImage.setImageResource(imageResource);

        ///////////////////

        // Bind tvIngredients to its respective view
        TextView tvIngredients = (TextView) findViewById(R.id.tvIngredients);

        // ArrayLists that contain the recipe ingredient name, value of ingredient, and unit of ingredient
        recipeIngredientsName = (ArrayList<String>) getIntent().getSerializableExtra("ingredientsName");
        recipeIngredientsAmountValue = (ArrayList<String>) getIntent().getSerializableExtra("ingredientsAmountValue");
        recipeIngredientsAmountUnit = (ArrayList<String>) getIntent().getSerializableExtra("ingredientsAmountUnit");

        // Position variable used to determine which recipe was clicked on
        position = (Integer) getIntent().getIntExtra("position", 0);

        // String to hold entire ingredient info on a recipe
        String ingredientInfo = "";

        for(int i = 0; i < recipeIngredientsName.size(); i++) {
            ingredientInfo = ingredientInfo + recipeIngredientsAmountValue.get(i) + recipeIngredientsAmountUnit.get(i) + " " + recipeIngredientsName.get(i) + ", ";
        }

        Log.d("IngredientInfo", ingredientInfo);

        tvIngredients.setText(ingredientInfo);


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
