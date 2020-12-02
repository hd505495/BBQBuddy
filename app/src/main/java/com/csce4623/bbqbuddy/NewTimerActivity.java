package com.csce4623.bbqbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.csce4623.bbqbuddy.utils.TimerItem;

public class NewTimerActivity extends AppCompatActivity {

    TimerItem timer;
    EditText etTitle, etInterval, etNumRepeats;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_timer);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etInterval = (EditText) findViewById(R.id.etInterval);
        etNumRepeats = (EditText) findViewById(R.id.etNumRepeats);
        btnSave = (Button) findViewById(R.id.btnSaveItem);

        timer = new TimerItem();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });
    }

    private void saveItem() {
        String title = etTitle.getText().toString();
        int interval = Integer.parseInt(etInterval.getText().toString());
        int numRepeats = Integer.parseInt(etNumRepeats.getText().toString());

        if ((!title.equals("")) && (interval > 0) && (numRepeats >= 0)) {
            timer.setTitle(title);
            timer.setInterval(interval);
            timer.setNumRepeats(numRepeats);

            Intent dataIntent = new Intent();
            dataIntent.putExtra("timer", timer);
            setResult(RESULT_OK, dataIntent);
            finish();
        }
        else {
            Toast.makeText(this, "Fill out all empty fields", Toast.LENGTH_SHORT).show();
        }
    }
}