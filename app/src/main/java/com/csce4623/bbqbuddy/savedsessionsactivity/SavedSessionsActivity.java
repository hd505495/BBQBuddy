package com.csce4623.bbqbuddy.savedsessionsactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.csce4623.bbqbuddy.R;
import com.csce4623.bbqbuddy.data.Session;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class SavedSessionsActivity extends AppCompatActivity implements SavedSessionsContract.View {

    // Presenter instance for view
    private SavedSessionsContract.Presenter mPresenter;
    // Inner class instance for ListView adapter
    private SessionsAdapter mSessionsAdapter;

    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_sessions);
    }


    // ******************************************************************************
    /**
     * instance of SessionsListener with onSessionClick function
     */
    SessionsListener mSessionsListener = new SessionsListener() {
        @Override
        public void onSessionClick(Session clickedSession) {
////////////////////////////// TODO: timer item click listener
        }
    };

    @Override
    public void setPresenter(SavedSessionsContract.Presenter presenter) {

    }

    /**
     * Adapter for ListView to show TimerItems
     */
    private static class SessionsAdapter extends BaseAdapter {

        //List of all TimerItems
        private List<Session> mSessions;
        // Listener for onSessionClick events
        private SessionsListener mSessionsListener;

        /**
         * Constructor for the adapter
         * @param sessions - List of initial items
         * @param sessionListener - onSessionClick listener
         */
        public SessionsAdapter(List<Session> sessions, SessionsListener sessionListener) {
            setList(sessions);
            mSessionsListener = sessionListener;
        }

        /**
         * replace sessions list with new list
         * @param sessions
         */
        public void replaceData(List<Session> sessions) {
            setList(sessions);
            notifyDataSetChanged();
        }

        private void setList(List<Session> sessions) {
            mSessions = checkNotNull(sessions);
        }

        @Override
        public int getCount() {
            return mSessions.size();
        }

        @Override
        public Session getItem(int i) {
            return mSessions.get(i);
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
            final Session session = getItem(i);

            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
/*
            rowView = inflater.inflate(R.layout.saved_session_item_layout, viewGroup, false);

            TextView titleTV = (TextView) rowView.findViewById(R.id.etItemTitle);
            titleTV.setText(timerItem.getTitle());

            TextView intervalTV = (TextView) rowView.findViewById(R.id.etItemInterval);
            intervalTV.setText(timerItem.getInterval() + "");

            TextView numRepeatsTV = (TextView) rowView.findViewById(R.id.etItemNumRepeats);
            numRepeatsTV.setText(timerItem.getNumRepeats() + "");
*/
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Set onItemClick listener
                    mSessionsListener.onSessionClick(session);
                }
            });
            return rowView;
        }
    }

    public interface SessionsListener {
        void onSessionClick(Session clickedSession);
    }
}