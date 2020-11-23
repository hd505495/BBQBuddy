package com.csce4623.bbqbuddy.mainactivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.csce4623.bbqbuddy.R;
import com.csce4623.bbqbuddy.data.Item;
import com.csce4623.bbqbuddy.data.Repository;

import java.util.List;

import util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends AppCompatActivity implements MainContract.View{

    private MainContract.Presenter mPresenter;
    // Inner class instance for ListView adapter
    private MainActivity.ItemsAdapter mItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    /**
     * Adapter for ListView to show Items
     */
    private static class ItemsAdapter extends BaseAdapter {

        //List of all Items
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
}
