package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;

/**
 * Created by 상헌 on 2016-07-03.
 */
public class StudentMainActivity extends Activity{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        Button searchButton = (Button) findViewById(R.id.button_senddata);
        searchButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EditText inputIDEditText = (EditText) findViewById(R.id.edittext_student_id);
                String classID = inputIDEditText.getText().toString();

                EditText inputNameEditText = (EditText) findViewById(R.id.edittext_student_name);
                String name = inputNameEditText.getText().toString();

                Intent intent = new Intent(StudentMainActivity.this, StudentResultActivity.class);
                intent.putExtra("classID", classID);
                intent.putExtra("name", name);

                startActivity(intent);

            }
        });




    }
}
