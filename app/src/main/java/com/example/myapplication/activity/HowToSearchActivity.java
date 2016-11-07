package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

/**
 * Created by 상헌 on 2016-07-17.
 */
public class HowToSearchActivity extends Activity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_search);


        Button searchingByClassidButton = (Button) findViewById(R.id.button_searching_by_classid);
        Button searchingByNameidButton = (Button) findViewById(R.id.button_searching_by_name);

        searchingByClassidButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HowToSearchActivity.this, SearchingByClassidActivity.class);
                startActivity(intent);
            }
        });

        searchingByNameidButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HowToSearchActivity.this, SearchingByNameActivity.class);
                startActivity(intent);
            }
        });

    }
}
