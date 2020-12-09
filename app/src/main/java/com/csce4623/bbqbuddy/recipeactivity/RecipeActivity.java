package com.csce4623.bbqbuddy.recipeactivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.csce4623.bbqbuddy.R;
import com.csce4623.bbqbuddy.data.Repository;

import java.io.InputStream;
import java.util.ArrayList;

import util.AppExecutors;

public class RecipeActivity extends AppCompatActivity implements RecipeContract.View {

    private RecipeContract.Presenter mPresenter;

    ArrayList<String> recipeImageURL = new ArrayList<>();
    ArrayList<String> recipeTitle = new ArrayList<>();
    ArrayList<String> recipeIngredientsName = new ArrayList<>();
    ArrayList<String> recipeIngredientsAmountValue = new ArrayList<>();
    ArrayList<String> recipeIngredientsAmountUnit = new ArrayList<>();
    ArrayList<String> recipeNutritionTitles = new ArrayList<>();
    ArrayList<String> recipeNutritionValues = new ArrayList<>();
    ArrayList<String> recipeInstructionsSteps = new ArrayList<>();
    Integer position;

    TextView tvRecipeTitle;
    TextView tvIngredients;
    TextView tvNutrition;
    TextView tvInstructions;
    ImageView recipeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_layout);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recipe");
        recipeImage = (ImageView) findViewById(R.id.imageView);
        tvRecipeTitle = (TextView) findViewById(R.id.tvRecipeTitle);
        tvNutrition = (TextView) findViewById(R.id.tvNutrition);
        tvInstructions = (TextView) findViewById(R.id.tvInstructions);

        mPresenter = new RecipePresenter(Repository.getInstance(new AppExecutors(),getApplicationContext()), this);


        ///////////////////

                // ArrayLists that contain the recipe image, ingredient name, value of ingredient, and unit of ingredient
        recipeImageURL = (ArrayList<String>) getIntent().getSerializableExtra("imageURL");
        recipeTitle = (ArrayList<String>) getIntent().getSerializableExtra("recipeTitle");
        recipeIngredientsName = (ArrayList<String>) getIntent().getSerializableExtra("ingredientsName");
        recipeIngredientsAmountValue = (ArrayList<String>) getIntent().getSerializableExtra("ingredientsAmountValue");
        recipeIngredientsAmountUnit = (ArrayList<String>) getIntent().getSerializableExtra("ingredientsAmountUnit");
        recipeNutritionTitles = (ArrayList<String>) getIntent().getSerializableExtra("nutritionTitles");
        recipeNutritionValues = (ArrayList<String>) getIntent().getSerializableExtra("nutritionValues");
        recipeInstructionsSteps = (ArrayList<String>) getIntent().getSerializableExtra("instructionsSteps");

        // Position variable used to determine which recipe was clicked on
        position = (Integer) getIntent().getIntExtra("position", 0);

        // Bind tvIngredients to its respective view
        tvIngredients = (TextView) findViewById(R.id.tvIngredients);

        String imageURL = recipeImageURL.get(position);
        if (imageURL != null) {
            new DownloadImageTask(recipeImage).execute(imageURL);
        }

        // String to hold entire ingredient info on a recipe
        String ingredientInfo = "";

        for(int i = 0; i < recipeIngredientsName.size(); i++) {
            ingredientInfo = ingredientInfo + recipeIngredientsAmountValue.get(i) + recipeIngredientsAmountUnit.get(i) + " " + recipeIngredientsName.get(i) + ",  ";
        }

        Log.d("IngredientInfo", ingredientInfo);

        tvIngredients.setText(ingredientInfo);
        tvRecipeTitle.setText(recipeTitle.get(position));
        String nutritionText = "Per Serving: ";
        for (int i = 0; i < recipeNutritionTitles.size(); i++) {
            nutritionText += recipeNutritionTitles.get(i);
            nutritionText += ": ";
            nutritionText += recipeNutritionValues.get(i);
            nutritionText += "; ";
        }
        tvNutrition.setText(nutritionText);

        String instructionText = "";
        for (int i = 0; i < recipeInstructionsSteps.size(); i++) {
            int stepNumber = i + 1;
            instructionText += Integer.toString(stepNumber) + ") " + recipeInstructionsSteps.get(i) + " \n";
        }
        tvInstructions.setText(instructionText);
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

    /**
     * Downloads web image from URL, loads bitmap into imageview
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
