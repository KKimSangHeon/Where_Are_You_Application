
package com.example.myapplication.temp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.myapplication.activity.StudentInfo;
import com.example.myapplication.activity.StudentResultActivity;
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

/**
 * Created by 상헌 on 2016-07-17.
 */
/*
public class ResultSearchingByNameActivity extends Activity{
    ProgressDialog mProgress;
    String name;
    ArrayList<StudentInfo> SI=new ArrayList<StudentInfo>();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_search_result);

        Intent intent=getIntent();
        name=intent.getStringExtra("name");

        mProgress = ProgressDialog.show(ResultSearchingByNameActivity.this,
                "Wait", "Downloading...");
        DownThread thread = new DownThread("http://"+ StudentResultActivity.ipaddress+":8080/JSPServer/JSPServer.jsp");
        thread.start();

        try {
            thread.join();
            MyDraw vw = new MyDraw(getApplicationContext(),SI);
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
      //      TextView result = (TextView) findViewById(R.id.textview_teacher_result);


            //서버에 데이터를 요청하고 받은 데이터 파싱

            try {
                String Result = name+"학생의 위치\n\n";
                JSONArray ja = new JSONArray((String) msg.obj);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject order = ja.getJSONObject(i);
                    if(order.getString("name").equals(name)==true) {

              //          Result += "class ID: " + order.getString("classID") +
               //                 "  X: " + order.getInt("coordinate_x") +
               //                 "  Y: " + order.getInt("coordinate_y") +"\n";
                        SI.add(new StudentInfo(name, order.getString("classID"), order.getInt("coordinate_x"), MyDraw.SIZE_Y*100-order.getInt("coordinate_y")));
                        // 좌표 Y값은 반대라서 대입하는 방식을 달리함
                    }
                }
          //      result.setText(Result);

            } catch (JSONException e) {
                // Toast.makeText(v.getContext(), e.getMessage(), 0).show();
            }
        }
    };
}

*/