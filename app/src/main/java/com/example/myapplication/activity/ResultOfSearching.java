package com.example.myapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.myapplication.drawimage.MyDraw;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ResultOfSearching extends Activity {
    ProgressDialog mProgress;
    String classID;
    String name;
    ArrayList<StudentInfo> SI = new ArrayList<StudentInfo>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        classID = intent.getStringExtra("classID");
        name = intent.getStringExtra("name");

        mProgress = ProgressDialog.show(ResultOfSearching.this,
                "Wait", "Downloading...");

        DownThread thread;
        if (name.equals("null")) {  // classID로 조회
            thread = new DownThread("http://" + StudentResultActivity.ipaddress + ":8088/Pages/JSONPage.jsp");
        }
        else{
            thread = new DownThread("http://" + StudentResultActivity.ipaddress + ":8088/Pages/JSONPage2.jsp");
        }
        thread.start();
        try {
            thread.join();
            MyDraw vw;
            if (name.equals("null")) {  // classID로 조회
                vw= new MyDraw(getApplicationContext(), SI,0);
            }
            else{
                vw=new MyDraw(getApplicationContext(), SI,1);
            }
            setContentView(vw);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class DownThread extends Thread {
        String mAddr;

        DownThread(String addr) {
            mAddr = addr;
        }

        public void run() {
            HttpGet get = new HttpGet(mAddr);
            DefaultHttpClient client = new DefaultHttpClient();
            try {
                client.execute(get, mResHandler);
            } catch (Exception e) {
                ;
            }
        }
    }

    ResponseHandler<String> mResHandler = new ResponseHandler<String>() {
        public String handleResponse(HttpResponse response) {
            StringBuilder html = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));
                for (; ; ) {
                    String line = br.readLine();
                    if (line == null) break;
                    html.append(line + '\n');
                }
                br.close();

                Message message = mAfterDown.obtainMessage();
                message.obj = html.toString();
                mAfterDown.sendMessage(message);
            } catch (Exception e) {
                ;
            }
            return html.toString();
        }
    };

    Handler mAfterDown = new Handler() {
        public void handleMessage(Message msg) {
            mProgress.dismiss();

            if (name.equals("null")) {  // classID로 조회
                try {
                    JSONArray ja = new JSONArray((String) msg.obj);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject order = ja.getJSONObject(i);
                        if (order.getString("classID").equals(classID) == true) {
                            SI.add(new StudentInfo(order.getString("name"), classID, order.getInt("coordinate_x"), MyDraw.SIZE_Y * 100 - order.getInt("coordinate_y")));
                        }
                    }
                } catch (JSONException e) {    }
            } else {    // 이름으로 조회
                try {
                    JSONArray ja = new JSONArray((String) msg.obj);
                      for (int i = 0; i < ja.length(); i++) {
                        JSONObject order = ja.getJSONObject(i);
                        if (order.getString("name").equals(name) == true) {
                            Log.i("시간",order.getString("date"));
                            SI.add(new StudentInfo(name, order.getString("classID"), order.getInt("coordinate_x"), MyDraw.SIZE_Y * 100 - order.getInt("coordinate_y")));
                        }
                    }
                } catch (JSONException e) {       }

            }
        }
    };
}

