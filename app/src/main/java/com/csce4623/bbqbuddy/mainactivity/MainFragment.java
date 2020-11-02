package com.csce4623.bbqbuddy.mainactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.csce4623.bbqbuddy.data.Item;
import com.csce4623.bbqbuddy.R;

import java.util.ArrayList;
import java.util.List;


import static com.google.common.base.Preconditions.checkNotNull;

/**
 * MainFragment implements the MainContract.View class.
 * Populates into MainActivity content frame
 */
public class MainFragment extends Fragment implements MainContract.View {

    // Presenter instance for view
    private MainContract.Presenter mPresenter;
    // Inner class instance for ListView adapter
    private ItemsAdapter mItemsAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ToDoListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    /**
     * When fragment is created, create new instance of ItemsAdapter with empty ArrayList and static ItemsListener
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemsAdapter = new ItemsAdapter(new ArrayList<Item>(0),mItemsListener);
    }

    /**
     * start presenter during onResume
     * Ideally coupled with stopping during onPause (not needed here)
     */
    @Override
    public void onResume(){
        super.onResume();
        mPresenter.start();
    }

    /**
     * onCreateView inflates the fragment, finds the ListView and Button, returns the root view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return root view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main, container, false);

                /* **** REFACTOR FOR RECIPE LIST ****
        // Set up tasks view
        ListView listView = (ListView) root.findViewById(R.id.rvToDoList);
        listView.setAdapter(mToDoItemsAdapter);
        //Find button and set onClickMethod to add a New ToDoItem
        root.findViewById(R.id.btnNewToDo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.addNewToDoItem();
            }
        });

         */
        return root;

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
    ItemsListener mItemsListener = new ItemsListener() {
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
        private ItemsListener mItemListener;

        /**
         * Constructor for the adapter
         * @param Items - List of initial items
         * @param itemListener - onItemClick listener
         */
        public ItemsAdapter(List<Item> Items, ItemsListener itemListener) {
            setList(Items);
            mItemListener = itemListener;
        }

        /**
         * replace Items list with new list
         * @param Items
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
         * @param i
         * @param view
         * @param viewGroup
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