package com.csce4623.bbqbuddy.grillsessionactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.csce4623.bbqbuddy.MeatSelectActivity;
import com.csce4623.bbqbuddy.R;
import com.csce4623.bbqbuddy.activesessionactivity.ActiveSessionActivity;
import com.csce4623.bbqbuddy.data.Repository;
import com.csce4623.bbqbuddy.settimersactivity.SetTimersActivity;
import com.csce4623.bbqbuddy.utils.TimerItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import util.AppExecutors;

public class GrillSessionActivity extends AppCompatActivity implements GrillSessionContract.View {

    private GrillSessionContract.Presenter mPresenter;

    LinearLayout LLMeatSelect, LLFollowRecipe, LLSetTimer, LLBluetoothSetup;
    Button btnStartSession;
    private List<TimerItem> timersList;
    String meat;

    private static final int MEAT_SELECT_REQUEST = 1;
    private static final int SET_TIMERS_REQUEST = 2;
    private static final int START_SESSION_REQUEST = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_config_layout);
        LLMeatSelect = (LinearLayout) findViewById(R.id.LLMeatSelectBar);
        LLFollowRecipe = (LinearLayout) findViewById(R.id.LLFollowRecipeBar);
        LLSetTimer = (LinearLayout) findViewById(R.id.LLSetTimerBar);
        LLBluetoothSetup = (LinearLayout) findViewById(R.id.LLBluetoothSetupBar);
        btnStartSession = (Button) findViewById(R.id.btnStartSession);
        timersList = new ArrayList<TimerItem>();

        mPresenter = new GrillSessionPresenter(Repository.getInstance(new AppExecutors(),getApplicationContext()), this);

        LLMeatSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to activity for selecting meat for grill session
                launchMeatSelectionActivity(MEAT_SELECT_REQUEST);
            }
        });

        LLFollowRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to activity for selecting a recipe to follow
            }
        });

        LLSetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to activity for setting timer(s)
                launchSetTimersActivity(SET_TIMERS_REQUEST);
            }
        });

        LLBluetoothSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to activity for configuring bluetooth connection to temp probe
            }
        });

        btnStartSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch grill session
                launchGrillSession(START_SESSION_REQUEST);
            }
        });
    }

    private void launchGrillSession(int requestCode) {
        Intent startSessionIntent = new Intent(this, ActiveSessionActivity.class);
        startSessionIntent.putExtra("timerList", (Serializable) timersList);
        startSessionIntent.putExtra("meat", meat);
        startActivityForResult(startSessionIntent, requestCode);
    }

    private void launchSetTimersActivity(int requestCode) {
        Intent setTimersIntent = new Intent(this, SetTimersActivity.class);
        startActivityForResult(setTimersIntent, requestCode);
    }

    private void launchMeatSelectionActivity(int requestCode) {
        Intent selectMeatIntent = new Intent(this, MeatSelectActivity.class);
        startActivityForResult(selectMeatIntent, requestCode);
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
            if (data != null) {
                if (requestCode == SET_TIMERS_REQUEST) {
                    if (data.hasExtra("timersList")) {
                        timersList = (List<TimerItem>) data.getSerializableExtra("timersList");
                    }
                }
                else if (requestCode == MEAT_SELECT_REQUEST) {
                    if (data.hasExtra("meat")) {
                        meat = (String) data.getStringExtra("meat");
                    }
                }
            }

        } else if (resultCode == RESULT_CANCELED) {
            // do nothing
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        mPresenter.start();
    }


    /**
     * set the presenter for this view
     * @param presenter - the GrillSessionContract.presenter instance
     */
    @Override
    public void setPresenter(GrillSessionContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
