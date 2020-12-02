package com.csce4623.bbqbuddy.settimersactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.csce4623.bbqbuddy.NewTimerActivity;
import com.csce4623.bbqbuddy.R;
import com.csce4623.bbqbuddy.data.Repository;
import com.csce4623.bbqbuddy.utils.TimerItem;

import java.util.ArrayList;
import java.util.List;

import util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

public class SetTimersActivity extends AppCompatActivity implements SetTimersContract.View{

    // Presenter instance for view
    private SetTimersContract.Presenter mPresenter;
    // Inner class instance for ListView adapter
    private TimerItemsAdapter mTimerItemsAdapter;

    private List<TimerItem> timersList;

    private static final int NEW_TIMER_REQUEST = 1;

    ListView listView;
    Button btnAddTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timers);

        mTimerItemsAdapter = new TimerItemsAdapter(new ArrayList<TimerItem>(0), mTimerItemsListener);

        btnAddTimer = (Button) findViewById(R.id.button);
        btnAddTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newTimer(NEW_TIMER_REQUEST);
            }
        });

        // Set up timers list view
        listView = (ListView) findViewById(R.id.LVTimersList);
        listView.setAdapter(mTimerItemsAdapter);

        TimerItem tempTimer = new TimerItem();
        tempTimer.setTitle("Baste");
        tempTimer.setInterval(30);
        tempTimer.setNumRepeats(6);

        timersList = new ArrayList<TimerItem>();
        timersList.add(tempTimer);

        mPresenter = new SetTimersPresenter(Repository.getInstance(new AppExecutors(),getApplicationContext()), this);
    }

    @Override
    public void onResume(){
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(SetTimersContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void newTimer(int requestCode) {
        Intent setTimerIntent = new Intent(this, NewTimerActivity.class);
        startActivityForResult(setTimerIntent, requestCode);
    }

    /**
     * callback function for startActivityForResult
     * Data intent should contain a date and time
     * @param requestCode -
     * @param resultCode -
     * @param data -
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d("SetTimersActivity", "returned from NewTimerActivity");
            if (data != null) {
                if (data.hasExtra("timer")) {
                    TimerItem timer = (TimerItem) data.getSerializableExtra("timer");
                    timersList.add(timer);
                    Log.d("SetTimersActivity", "added new timer to local list of timers for UI");
                    showTimerItems();
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            // do nothing
        }

    }


    @Override
    public void showTimerItems() {
        mTimerItemsAdapter.replaceData(timersList);
    }


// ******************************************************************************
    /**
     * instance of TimerItemsListener with onTimerItemClick function
     */
    TimerItemsListener mTimerItemsListener = new TimerItemsListener() {
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
        private TimerItemsListener mItemListener;

        /**
         * Constructor for the adapter
         * @param timerItems - List of initial items
         * @param itemListener - onItemClick listener
         */
        public TimerItemsAdapter(List<TimerItem> timerItems, TimerItemsListener itemListener) {
            setList(timerItems);
            mItemListener = itemListener;
        }

        /**
         * replace timerItems list with new list
         * @param timerItems
         */
        public void replaceData(List<TimerItem> timerItems) {
            setList(timerItems);
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