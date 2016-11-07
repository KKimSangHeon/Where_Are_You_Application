package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;

/**
 * Created by 상헌 on 2016-07-17.
 */
public class SearchingByNameActivity extends Activity{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_by_name);

        Button searchButton = (Button) findViewById(R.id.button_search);
        searchButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EditText inputName = (EditText) findViewById(R.id.edittext_teacher_name);
                String name = inputName.getText().toString();

                Intent intent = new Intent(SearchingByNameActivity.this, ResultOfSearching.class);
                intent.putExtra("name", name);
                intent.putExtra("classID","null");
                startActivity(intent);
            }
        });
    }
}
