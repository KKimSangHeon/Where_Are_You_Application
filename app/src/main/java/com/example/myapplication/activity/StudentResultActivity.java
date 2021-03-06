package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.forbeacon.RecoRangingListAdapter;
import com.example.myapplication.socketdata.AverageRSSI;

import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;

import java.util.ArrayList;

/**
 * Created by 상헌 on 2016-07-18.
 * 안드로이드 통신+보안 프로그래밍 기초편 195page 참고
 */
public class StudentResultActivity extends Activity {

    String classID;
    String name;
    TextView textviewresult;
    Button stopButton;
    static boolean quitChecker = true;
    static String ipaddress = "tkdgjsone.iptime.org";
    int counter = 0;
    protected RECOBeaconManager mRecoManager;
    protected ArrayList<RECOBeaconRegion> mRegions;
    private RecoRangingListAdapter mRangingListAdapter;
    ArrayList<AverageRSSI> arrayRSSI = new ArrayList<AverageRSSI>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result);
        textviewresult = (TextView) findViewById(R.id.textview_student_result);
        stopButton = (Button) findViewById(R.id.button_stop);
        quitChecker = true;
        Intent intent = getIntent();

        classID = intent.getStringExtra("classID");
        name = intent.getStringExtra("name");

        stopButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (quitChecker == true)    //stop thread
                {
                    quitChecker = false;
                    finish();   //테스트 해볼것 , 액티비티 꺼지면 스레드 바로 종료되는지 아니면
                }
            }
        });

        try {
            NioClient client = new NioClient(ipaddress, 8097, name,classID, getApplicationContext());
            client.execute();
        } catch (Exception e) {
        }
    }


}
