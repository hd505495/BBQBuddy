package com.csce4623.bbqbuddy.grillsessionactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.csce4623.bbqbuddy.R;
import com.csce4623.bbqbuddy.data.Repository;

import util.AppExecutors;

public class GrillSessionActivity extends AppCompatActivity implements GrillSessionContract.View {

    private GrillSessionContract.Presenter mPresenter;

    LinearLayout LLMeatSelect, LLFollowRecipe, LLSetTimer, LLBluetoothSetup;
    Button btnStartSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_config_layout);
        LLMeatSelect = (LinearLayout) findViewById(R.id.LLMeatSelectBar);
        LLFollowRecipe = (LinearLayout) findViewById(R.id.LLFollowRecipeBar);
        LLSetTimer = (LinearLayout) findViewById(R.id.LLSetTimerBar);
        LLBluetoothSetup = (LinearLayout) findViewById(R.id.LLBluetoothSetupBar);
        btnStartSession = (Button) findViewById(R.id.btnStartSession);

        mPresenter = new GrillSessionPresenter(Repository.getInstance(new AppExecutors(),getApplicationContext()), this);

        LLMeatSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to activity/fragment for selecting meat for grill session
            }
        });

        LLFollowRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to activity/fragment for selecting a recipe to follow
            }
        });

        LLSetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to activity/fragment for setting timer(s)
            }
        });

        LLBluetoothSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to activity/fragment for configuring bluetooth connection to temp probe
            }
        });

        btnStartSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch grill session
            }
        });
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
