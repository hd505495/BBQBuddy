package com.csce4623.bbqbuddy.mainactivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csce4623.bbqbuddy.R;
import com.csce4623.bbqbuddy.data.Repository;
import com.csce4623.bbqbuddy.grillsessionactivity.GrillSessionActivity;
import com.csce4623.bbqbuddy.recipeactivity.RecipeActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import util.AppExecutors;

public class MainActivity extends AppCompatActivity implements MainContract.View, RecyclerViewAdapter.ItemClickListener {

    // Inner class instance for ListView adapter
    //private MainActivity.ItemsAdapter mItemsAdapter;

    private MainContract.Presenter mPresenter;

    // SearchView (not implemented)
    SearchView searchView;

    // Variables for the navigation drawer and an actionbar toggle
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    // RecyclerView adapter for displaying recipes
    RecyclerViewAdapter rvAdapter;

    // Global ArrayLists to hold recipe ID, title, and imageURL
    ArrayList<String> recipeID = new ArrayList<>();
    ArrayList<String> recipeTitle = new ArrayList<>();
    ArrayList<String> recipeImageURL = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);

        // Set toolbar title color
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        // Set toolbar background to match theme of other activities
        toolbar.setBackground(new ColorDrawable(Color.DKGRAY));

        // Set actionbar to our toolbar
        setSupportActionBar(toolbar);

        // Create navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        // Set navigation drawer button and arrow color
        drawerToggle.getDrawerArrowDrawable().setColor(Color.parseColor("#ffffff"));

        // Add drawerListener and sync the state
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.start_grill_session) {
                    startGrillSession();
                } else if (id == R.id.previous_grill_sessions) {

                } else if (id == R.id.feedback) {

                } else if (id == R.id.settings) {

                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //Get an instance of the MainPresenter
        //Parameters - Repository - Instance of the Repository
        //Fragment - the View to be communicated to by the presenter
        // Repository needs a thread pool to execute database/network calls in other threads
        // Repository needs the application context to be able to make calls to the ContentProvider
        mPresenter = new MainPresenter(Repository.getInstance(new AppExecutors(),getApplicationContext()), this);

        //////////////////////////////////////
        // Get recipe ID, title, and image URL
        /////////////////////////////////////
        ///////////////////////////////////////////////////////
        // On click events handled by OnItemClick method below
        //////////////////////////////////////////////////////

        // Local variables to hold id, title, and imageURL
        String id = null;
        String title = null;
        String imageURL = null;

        try {
            // Get recipe id and title from URL
            String jsonRecipeData = new JsonTask().execute("https://api.spoonacular.com/recipes/complexSearch?apiKey=3fe88830eb2a4d98ab736e2da8997404&query=bbq&sort=random&number=10").get();
            JSONObject jsonRecipeDataObject = new JSONObject(jsonRecipeData);
            JSONArray jsonRecipeDataArray = jsonRecipeDataObject.getJSONArray("results");

            // Logging to check the contents of jsonRecipeDataArray
            Log.d("RecipeArray", jsonRecipeDataArray.toString());

            // Loop through acquired JSONArray and grab id and title of each recipe
            // Add to respective ArrayLists
            for(int i =0; i < jsonRecipeDataArray.length(); i++) {

                id = jsonRecipeDataArray.getJSONObject(i).getString("id");
                title = jsonRecipeDataArray.getJSONObject(i).getString("title");
                imageURL = jsonRecipeDataArray.getJSONObject(i).getString("image");

                recipeID.add(id);
                recipeTitle.add(title);
                recipeImageURL.add(imageURL);
            }

            //////////////////////////////////
            // Logging for recipe id and title
            //////////////////////////////////
            String logID = "";
            String logTitle = "";

            for(int i = 0; i < recipeID.size(); i++) {
                logID = logID + recipeID.get(i) + ", ";
            }

            for(int j = 0; j < recipeTitle.size(); j++) {
                logTitle = logTitle + recipeTitle.get(j) + ", ";
            }

            Log.d("recipeID List", logID);
            Log.d("recipeTitle List", logTitle);
            /////////////////////////////////

            // Configure the recycler view and set the adapter
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            rvAdapter = new RecyclerViewAdapter(this, recipeTitle, recipeImageURL);
            rvAdapter.setClickListener(this);
            recyclerView.setAdapter(rvAdapter);

            // Add a divider between TextView rows
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Method to start the grill session activity
    private void startGrillSession() {
        Intent startSessionIntent = new Intent(this, GrillSessionActivity.class);
        startActivity(startSessionIntent);
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

    @Override
    public void onResume(){
        super.onResume();
        mPresenter.start();
    }

    // Method to handle onClickEvents for recipe items in the recycler view
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + rvAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();

        // Local ArrayLists to hold ingredients, nutrition, and instructions information
        ArrayList<String> recipeIngredientsName = new ArrayList<>();
        ArrayList<String> recipeIngredientsAmountValue = new ArrayList<>();
        ArrayList<String> recipeIngredientsAmountUnit = new ArrayList<>();
        ArrayList<String> recipeNutritionTitles = new ArrayList<>();
        ArrayList<String> recipeNutritionValues = new ArrayList<>();
        ArrayList<String> recipeInstructionsSteps = new ArrayList<>();

        // Local variables to hold name and amount
        String name = null;
        String amountValue = null;
        String amountUnit = null;

        try {
            // Get recipe ingredients from URL based on recipe ID
            // Get recipe ingredient name and amount from URL
            String jsonIngredientData = new JsonTask().execute("https://api.spoonacular.com/recipes/" + recipeID.get(position) + "/ingredientWidget.json?apiKey=3fe88830eb2a4d98ab736e2da8997404").get();
            JSONObject jsonIngredientDataObject = new JSONObject(jsonIngredientData);
            JSONArray jsonIngredientDataArray = jsonIngredientDataObject.getJSONArray("ingredients");

            // Logging to check the contents of jsonIngredientDataArray
            Log.d("IngredientArray", jsonIngredientDataArray.toString());

            // Loop through acquired JSONArray and grab id and title of each recipe
            // Add to respective ArrayLists
            for (int j = 0; j < jsonIngredientDataArray.length(); j++) {

                name = jsonIngredientDataArray.getJSONObject(j).getString("name");
                amountValue = jsonIngredientDataArray.getJSONObject(j).getJSONObject("amount").getJSONObject("metric").getString("value");
                amountUnit = jsonIngredientDataArray.getJSONObject(j).getJSONObject("amount").getJSONObject("metric").getString("unit");

                Log.d("name", name);
                Log.d("amountValue", amountValue);
                Log.d("amountUnit", amountUnit);

                recipeIngredientsName.add(name);
                recipeIngredientsAmountValue.add(amountValue);
                recipeIngredientsAmountUnit.add(amountUnit);
            }

            // Get nutrition info from URL
            String jsonNutritionData = new JsonTask().execute("https://api.spoonacular.com/recipes/" + recipeID.get(position) + "/nutritionWidget.json?apiKey=3fe88830eb2a4d98ab736e2da8997404").get();
            JSONObject jsonNutritionDataObject = new JSONObject(jsonNutritionData);

            // Logging to check the contents of jsonIngredientDataArray
            Log.d("RecipeInfoplusNutString", jsonNutritionDataObject.toString());

            String calories = jsonNutritionDataObject.getString("calories");
            recipeNutritionTitles.add("calories");
            recipeNutritionValues.add(calories);
            String carbs = jsonNutritionDataObject.getString("carbs");
            recipeNutritionTitles.add("carbs");
            recipeNutritionValues.add(carbs);
            String fat = jsonNutritionDataObject.getString("fat");
            recipeNutritionTitles.add("fat");
            recipeNutritionValues.add(fat);
            String protein = jsonNutritionDataObject.getString("protein");
            recipeNutritionTitles.add("protein");
            recipeNutritionValues.add(protein);

            // Get instructions info from URL
            String jsonInstructionData = new JsonTask().execute("https://api.spoonacular.com/recipes/" + recipeID.get(position) + "/analyzedInstructions?apiKey=3fe88830eb2a4d98ab736e2da8997404").get();

            //JSONObject jsonInstructionsObj = new JSONObject(jsonInstructionData);
            JSONArray jsonInstructionsArray = new JSONArray(jsonInstructionData);

            Log.d("RecipeInstructionString", jsonInstructionsArray.toString());

            JSONArray tempArray = jsonInstructionsArray.getJSONObject(0).getJSONArray("steps");

            for (int j = 0; j < tempArray.length(); j++) {
                recipeInstructionsSteps.add(tempArray.getJSONObject(j).getString("step"));
            }

        }  catch (ExecutionException e) {
            e.printStackTrace();
        }  catch (InterruptedException e) {
            e.printStackTrace();
        }  catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        intent.putExtra("imageURL", recipeImageURL);
        intent.putExtra("recipeTitle", recipeTitle);
        intent.putExtra("ingredientsName", recipeIngredientsName);
        intent.putExtra("ingredientsAmountValue", recipeIngredientsAmountValue);
        intent.putExtra("ingredientsAmountUnit", recipeIngredientsAmountUnit);
        intent.putExtra("nutritionTitles", recipeNutritionTitles);
        intent.putExtra("nutritionValues", recipeNutritionValues);
        intent.putExtra("instructionsSteps", recipeInstructionsSteps);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    /**
     * set the presenter for this view
     * @param presenter - the MainContract.presenter instance
     */
    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * Replace the items in the ItemsAdapter
     * @param

    @Override
    public void showItems(List<Item> ItemList) {
        mItemsAdapter.replaceData(ItemList);
    }
    */


    // Private class that handles getting Json data from an input URL
    private class JsonTask extends AsyncTask<String, String, String> {

        // Pre-execute method. Does nothing particularly interesting in this case
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // Background method. Opens the URL connection and reads in the JSON data
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream stream = urlConnection.getInputStream();

                bufferedReader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line+"\n");
                    //Log.d("Response: ", "> " + line);

                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        // Post-execute method. Does nothing particularly interesting in this case
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}

