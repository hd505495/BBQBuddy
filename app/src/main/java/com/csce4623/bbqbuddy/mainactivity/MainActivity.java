package com.csce4623.bbqbuddy.mainactivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csce4623.bbqbuddy.R;
import com.csce4623.bbqbuddy.data.Item;
import com.csce4623.bbqbuddy.data.Repository;
import com.csce4623.bbqbuddy.recipeactivity.RecipeActivity;

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
import java.util.List;
import java.util.concurrent.ExecutionException;

import util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends AppCompatActivity implements MainContract.View, RecyclerViewAdapter.ItemClickListener {

    private MainContract.Presenter mPresenter;
    // Inner class instance for ListView adapter
    private MainActivity.ItemsAdapter mItemsAdapter;

    SearchView searchView;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    RecyclerViewAdapter rvAdapter;

    ArrayList<String> recipeID = new ArrayList<>();
    ArrayList<String> recipeTitle = new ArrayList<>();
    ArrayList<String> recipeIngredientsName = new ArrayList<>();
    ArrayList<String> recipeIngredientsAmountValue = new ArrayList<>();
    ArrayList<String> recipeIngredientsAmountUnit = new ArrayList<>();


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

        //Get an instance of the MainPresenter
        //Parameters - Repository - Instance of the Repository
        //Fragment - the View to be communicated to by the presenter
        // Repository needs a thread pool to execute database/network calls in other threads
        // Repository needs the application context to be able to make calls to the ContentProvider
        mPresenter = new MainPresenter(Repository.getInstance(new AppExecutors(),getApplicationContext()), this);

        /* TODO -- SETUP LISTVIEW
        ListView listView = (ListView) findViewById(R.id.*****);
        listView.setAdapter(mToDoItemsAdapter);
         */

        ////////////////////
        // Get recipe ID, title, and image URL (not implemented yet)
        ///////////////////
        ///////////////////
        // On click events handled by OnItemClick method below
        //////////////////

        // Local variables to hold id and title
        String id = null;
        String title = null;

        try {
            // Get recipe id and title from URL
            String jsonRecipeData = new JsonTask().execute("https://api.spoonacular.com/recipes/complexSearch?apiKey=3fe88830eb2a4d98ab736e2da8997404&query=bbq&sort=random&number=1").get();
            JSONObject jsonRecipeDataObject = new JSONObject(jsonRecipeData);
            JSONArray jsonRecipeDataArray = jsonRecipeDataObject.getJSONArray("results");

            // Logging to check the contents of jsonIngredientDataArray
            Log.d("RecipeArray", jsonRecipeDataArray.toString());

            // Loop through acquired JSONArray and grab id and title of each recipe
            // Add to respective ArrayLists
            for(int i =0; i < jsonRecipeDataArray.length(); i++) {

                id = jsonRecipeDataArray.getJSONObject(i).getString("id");
                title = jsonRecipeDataArray.getJSONObject(i).getString("title");

                Log.d("id", id);
                Log.d("title", title);

                recipeID.add(id);
                recipeTitle.add(title);
            }

            // Configure the recycler view and set the adapter
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            rvAdapter = new RecyclerViewAdapter(this, recipeTitle);
            rvAdapter.setClickListener(this);
            recyclerView.setAdapter(rvAdapter);

            // Add a divider between TextView rows
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


            ///////////////////////
            // Get recipe ingredient information
            //////////////////////

            // Local variables to hold name and amount
            String name = null;
            String amountValue = null;
            String amountUnit = null;

            // Get recipe ingredients from URL based on recipe ID
            // Get recipe ingredient name and amount from URL
            for(int i = 0; i < recipeID.size(); i++) {

                String jsonIngredientData = new JsonTask().execute("https://api.spoonacular.com/recipes/" + recipeID.get(i) + "/ingredientWidget.json?apiKey=3fe88830eb2a4d98ab736e2da8997404").get();
                JSONObject jsonIngredientDataObject = new JSONObject(jsonIngredientData);
                JSONArray jsonIngredientDataArray = jsonIngredientDataObject.getJSONArray("ingredients");

                // Logging to check the contents of jsonIngredientDataArray
                Log.d("IngredientArray", jsonIngredientDataArray.toString());

                // Loop through acquired JSONArray and grab id and title of each recipe
                // Add to respective ArrayLists
                for(int j =0; j < jsonIngredientDataArray.length(); j++) {

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
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
     * @param ItemList - List of Items
     */
    @Override
    public void showItems(List<Item> ItemList) {
        mItemsAdapter.replaceData(ItemList);
    }

    /**
     * instance of ItemsListener with onItemClick function
     */
    MainActivity.ItemsListener mItemsListener = new MainActivity.ItemsListener() {
        @Override
        public void onItemClick(Item clickedItem) {
            Log.d("FRAGMENT","Open Item Details");
                    /* **** REFACTOR FOR RECIPE LIST ITEM CLICK  ****
            //Grab item from the ListView click and pass to presenter
            //mPresenter.showExistingToDoItem(clickedToDoItem);

                     */
        }
    };

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + rvAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        intent.putExtra("ingredientsName", recipeIngredientsName);
        intent.putExtra("ingredientsAmountValue", recipeIngredientsAmountValue);
        intent.putExtra("ingredientsAmountUnit", recipeIngredientsAmountUnit);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    /**
     * Adapter for ListView to show Items
     */
    private static class ItemsAdapter extends BaseAdapter {

        // List of all Items
        private List<Item> mItems;
        // Listener for onItemClick events
        private MainActivity.ItemsListener mItemListener;

        /**
         * Constructor for the adapter
         * @param Items - List of initial items
         * @param itemListener - onItemClick listener
         */
        public ItemsAdapter(List<Item> Items, MainActivity.ItemsListener itemListener) {
            setList(Items);
            mItemListener = itemListener;
        }

        /**
         * replace Items list with new list
         * @param Items - List of items to use in replacement
         */
        public void replaceData(List<Item> Items) {
            setList(Items);
            notifyDataSetChanged();
        }

        private void setList(List<Item> Items) {
            mItems = checkNotNull(Items);
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Item getItem(int i) {
            return mItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        /**
         * Get a View based on an index and viewgroup and populate
         * @param i -
         * @param view -
         * @param viewGroup -
         * @return
         */
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.item_layout, viewGroup, false);
            }

            //get the Item associated with a given view
            //used in the OnItemClick callback
            final Item item = getItem(i);

            /* **** REFACTOR FOR RECIPE ITEMS ****

            TextView titleTV = (TextView) rowView.findViewById(R.id.etItemTitle);
            titleTV.setText(Item.getTitle());

            TextView contentTV = (TextView) rowView.findViewById(R.id.etItemContent);
            contentTV.setText(Item.getContent());

             */

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Set onItemClick listener
                    mItemListener.onItemClick(item);
                }
            });
            return rowView;
        }
    }

    public interface ItemsListener {
        void onItemClick(Item clickedItem);
    }

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

