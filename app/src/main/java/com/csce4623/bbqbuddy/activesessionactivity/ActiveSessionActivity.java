package com.csce4623.bbqbuddy.activesessionactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.csce4623.bbqbuddy.R;
import com.csce4623.bbqbuddy.data.Repository;
import com.csce4623.bbqbuddy.utils.TimerItem;

import java.util.ArrayList;
import java.util.List;

import util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

public class ActiveSessionActivity extends AppCompatActivity implements ActiveSessionContract.View{

    // Presenter instance for view
    private ActiveSessionContract.Presenter mPresenter;
    // Inner class instance for ListView adapter
    private ActiveSessionActivity.TimerItemsAdapter mTimerItemsAdapter;

    private List<TimerItem> timersList = new ArrayList<TimerItem>();

    private String meat;

    ListView listView;
    Button btnEndSession;
    ImageView IVMeatDisplay;
    TextView TVMeatTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_session);

        mTimerItemsAdapter = new ActiveSessionActivity.TimerItemsAdapter(new ArrayList<TimerItem>(0), mTimerItemsListener);

        IVMeatDisplay = (ImageView) findViewById(R.id.IVMeat);
        TVMeatTitle = (TextView) findViewById(R.id.TVMeatTitleClickable);
        btnEndSession = (Button) findViewById(R.id.btnEndSession);
        btnEndSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // --
            }
        });

        // *** TODO: REMINDER LIST DOESN'T SHOW UP
        // Set up timers list view
        listView = (ListView) findViewById(R.id.LVSessionTimersList);
        listView.setAdapter(mTimerItemsAdapter);

        Intent callingIntent = getIntent();
        if (callingIntent != null) {
            if (callingIntent.hasExtra("timerList")) {
                timersList = (List<TimerItem>) callingIntent.getSerializableExtra("timerList");
                showTimerItems();
            }
            if (callingIntent.hasExtra("meat")){
                meat = (String) callingIntent.getStringExtra("meat");
                setMeatDisplay();
            }
        }

        mPresenter = new ActiveSessionPresenter(Repository.getInstance(new AppExecutors(),getApplicationContext()), this);
    }

    private void setMeatDisplay() {
        TVMeatTitle.setText(meat);
        if (meat == "chicken") {
            int imageResource = getResources().getIdentifier("drawable/chicken_stock_img", null, this.getPackageName());
            IVMeatDisplay.setImageResource(imageResource);
        }
        else if (meat == "beef") {
            int imageResource = getResources().getIdentifier("drawable/beef_stock_img", null, this.getPackageName());
            IVMeatDisplay.setImageResource(imageResource);
        }
        else if (meat == "fish") {
            int imageResource = getResources().getIdentifier("drawable/fish_stock_img", null, this.getPackageName());
            IVMeatDisplay.setImageResource(imageResource);
        }
        else if (meat == "pork") {
            int imageResource = getResources().getIdentifier("drawable/pork_stock_img", null, this.getPackageName());
            IVMeatDisplay.setImageResource(imageResource);
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(ActiveSessionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTimerItems() {
        mTimerItemsAdapter.replaceData(timersList);
    }


// ******************************************************************************
    /**
     * instance of TimerItemsListener with onTimerItemClick function
     */
    ActiveSessionActivity.TimerItemsListener mTimerItemsListener = new ActiveSessionActivity.TimerItemsListener() {
        @Override
        public void onTimerItemClick(TimerItem clickedTimerItem) {
////////////////////////////// TODO: timer item click listener
        }
    };

    /**
     * Adapter for ListView to show TimerItems
     */
    private static class TimerItemsAdapter extends BaseAdapter {

        //List of all TimerItems
        private List<TimerItem> mTimerItems;
        // Listener for onItemClick events
        private ActiveSessionActivity.TimerItemsListener mItemListener;

        /**
         * Constructor for the adapter
         * @param timerItems - List of initial items
         * @param itemListener - onItemClick listener
         */
        public TimerItemsAdapter(List<TimerItem> timerItems, ActiveSessionActivity.TimerItemsListener itemListener) {
            setList(timerItems);
            mItemListener = itemListener;
        }

        /**
         * replace timerItems list with new list
         * @param timerItems
         */
        public void replaceData(List<TimerItem> timerItems) {
            setList(timerItems);
            Log.d("ActiveSessionActivity", "loading listview with timerItems list in replaceData in adapter");
            notifyDataSetChanged();
        }

        private void setList(List<TimerItem> timerItems) {
            mTimerItems = checkNotNull(timerItems);
        }

        @Override
        public int getCount() {
            return mTimerItems.size();
        }

        @Override
        public TimerItem getItem(int i) {
            return mTimerItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        /**
         * Get a View based on an index and viewgroup and populate
         * @param i -
         * @param rowView -
         * @param viewGroup -
         * @return
         */
        @Override
        public View getView(int i, View rowView, ViewGroup viewGroup) {
            //get the TimerItem associated with a given view
            //used in the OnItemClick callback and to determine completed status
            //and thereby choose the correct layout to inflate
            final TimerItem timerItem = getItem(i);

            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            rowView = inflater.inflate(R.layout.timer_item_layout, viewGroup, false);

            TextView titleTV = (TextView) rowView.findViewById(R.id.etItemTitle);
            titleTV.setText(timerItem.getTitle());

            TextView intervalTV = (TextView) rowView.findViewById(R.id.etItemInterval);
            intervalTV.setText(timerItem.getInterval() + "");

            TextView numRepeatsTV = (TextView) rowView.findViewById(R.id.etItemNumRepeats);
            numRepeatsTV.setText(timerItem.getNumRepeats() + "");

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Set onItemClick listener
                    mItemListener.onTimerItemClick(timerItem);
                }
            });
            return rowView;
        }
    }

    public interface TimerItemsListener {
        void onTimerItemClick(TimerItem clickedTimerItem);
    }
}
