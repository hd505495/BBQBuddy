package com.csce4623.bbqbuddy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MeatSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meat_select);
        ImageView chickenImage = (ImageView) findViewById(R.id.IVChicken);
        ImageView beefImage = (ImageView) findViewById(R.id.IVBeef);
        ImageView fishImage = (ImageView) findViewById(R.id.IVFish);
        ImageView porkImage = (ImageView) findViewById(R.id.IVPork);

        int chickenImageResource = getResources().getIdentifier("drawable/chicken_stock_img", null, this.getPackageName());
        chickenImage.setImageResource(chickenImageResource);

        int beefImageResource = getResources().getIdentifier("drawable/beef_stock_img", null, this.getPackageName());
        beefImage.setImageResource(beefImageResource);

        int fishImageResource = getResources().getIdentifier("drawable/fish_stock_img", null, this.getPackageName());
        fishImage.setImageResource(fishImageResource);

        int porkImageResource = getResources().getIdentifier("drawable/pork_stock_img", null, this.getPackageName());
        porkImage.setImageResource(porkImageResource);

        chickenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.LTGRAY);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("meat", "chicken");
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        beefImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.LTGRAY);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("meat", "beef");
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        fishImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.LTGRAY);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("meat", "fish");
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        porkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.LTGRAY);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("meat", "pork");
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}