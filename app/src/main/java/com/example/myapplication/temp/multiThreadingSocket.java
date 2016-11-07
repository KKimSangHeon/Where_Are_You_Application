package com.example.myapplication.temp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.forbeacon.References;
import com.example.myapplication.forbeacon.RecoRangingListAdapter;
import com.example.myapplication.forbeacon.getBeaconAverage;
import com.example.myapplication.socketdata.*;
import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECORangingListener;
import com.perples.recosdk.RECOServiceConnectListener;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Created by 상헌 on 2016-07-03.`
 */
public class multiThreadingSocket extends Activity {
    String classID;
    String name;
    TextView textviewresult;
    Button stopButton;
    boolean quitChecker = true;
    static String ipaddress="tkdgjsone.iptime.org";

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

        Intent intent = getIntent();

        classID = intent.getStringExtra("classID");
        name = intent.getStringExtra("name");


        stopButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (quitChecker == true)    //stop thread
                {
                    quitChecker = false;


                    //아래부분 위로 끌어올려보기(종료 소켓)

                    finish();   //테스트 해볼것 , 액티비티 꺼지면 스레드 바로 종료되는지 아니면
                }
                else {  //restart thread
                    quitChecker = true;
                    MyClientTask myClientTask = new MyClientTask(ipaddress, 8097);
                    myClientTask.execute();

                }
            }
        });


        MyClientTask myClientTask = new MyClientTask(ipaddress, 8097);
        myClientTask.execute();

    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> implements RECORangingListener, RECOServiceConnectListener {
        String dstAddress;
        int dstPort;
        String response = "";

        MyClientTask(String addr, int port) {
            dstAddress = addr;
            dstPort = port;
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            while (quitChecker == true) {
                Socket socket = null;
                try {
                    socket = new Socket(dstAddress, dstPort);

            /* 이름과 classID를 서버로 보내는 코드(test ok)
             이름, 클래스ID, 인스턴스를 보내면 error발생으로 보류

                //------------------send class ID to Server
                String returnCode = classID + (char) 13;
                BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
                OutputStreamWriter osw = new OutputStreamWriter(os, "euc-kr");
                osw.write(returnCode);
                osw.flush();

                //------------------send name to Server
                returnCode = name + (char) 13;
                osw.write(returnCode);
                osw.flush();



                */
                    mRecoManager = RECOBeaconManager.getInstance(getApplicationContext(), References.SCAN_RECO_ONLY, References.ENABLE_BACKGROUND_RANGING_TIMEOUT);
                    mRegions = this.generateBeaconRegion();

                    mRecoManager.setRangingListener(this);
                    mRecoManager.bind(this);


                    StudentInformation si = new StudentInformation(name, classID);
                    SendData temp = new SendData(arrayRSSI, si);

                    BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(os);

                    oos.writeObject(temp);

                    arrayRSSI = new ArrayList<AverageRSSI>();
                    oos.close();

                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    response = "UnknownHostException: " + e.toString();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    response = "IOException: " + e.toString();
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                }
            }


            // send stop message to server
            SendData temp;
            try (Socket socket = new Socket(ipaddress, 8097)) {
                StudentInformation si = new StudentInformation(name, classID);
                si.checker = false;
                temp = new SendData(arrayRSSI, si);

                BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(temp);
                oos.close();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            textviewresult.setText("restart");
            super.onPostExecute(result);
        }


        //------------------------for beacon methods

        private ArrayList<RECOBeaconRegion> generateBeaconRegion() {
            ArrayList<RECOBeaconRegion> regions = new ArrayList<RECOBeaconRegion>();

            RECOBeaconRegion recoRegion;
            recoRegion = new RECOBeaconRegion(References.RECO_UUID, "RECO Sample Region");
            regions.add(recoRegion);

            return regions;
        }


        private void unbind() {
            try {
                mRecoManager.unbind();
            } catch (RemoteException e) {
                Log.i("RECORangingActivity", "Remote Exception");
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceConnect() {
            Log.i("RECORangingActivity", "onServiceConnect()");
            mRecoManager.setDiscontinuousScan(References.DISCONTINUOUS_SCAN);
            this.start(mRegions);
            //Write the code when RECOBeaconManager is bound to RECOBeaconService
        }

        protected void stop(ArrayList<RECOBeaconRegion> regions) {
            for (RECOBeaconRegion region : regions) {
                try {
                    mRecoManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    Log.i("RECORangingActivity", "Remote Exception");
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    Log.i("RECORangingActivity", "Null Pointer Exception");
                    e.printStackTrace();
                }
            }
        }

        protected void start(ArrayList<RECOBeaconRegion> regions) {

            /**
             * There is a known android bug that some android devices scan BLE devices only once. (link: http://code.google.com/p/android/issues/detail?id=65863)
             * To resolve the bug in our SDK, you can use setDiscontinuousScan() method of the RECOBeaconManager.
             * This method is to set whether the device scans BLE devices continuously or discontinuously.
             * The default is set as FALSE. Please set TRUE only for specific devices.
             *
             * mRecoManager.setDiscontinuousScan(true);
             */

            for (RECOBeaconRegion region : regions) {
                try {
                    mRecoManager.startRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    Log.i("RECORangingActivity", "Remote Exception");
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    Log.i("RECORangingActivity", "Null Pointer Exception");
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceFail(RECOErrorCode errorCode) {
            //Write the code when the RECOBeaconService is failed.
            //See the RECOErrorCode in the documents.
            return;
        }

        @Override
        public void rangingBeaconsDidFailForRegion(RECOBeaconRegion region, RECOErrorCode errorCode) {
            //Write the code when the RECOBeaconService is failed to range beacons in the region.
            //See the RECOErrorCode in the documents.
            return;
        }

        public void didRangeBeaconsInRegion(Collection<RECOBeacon> recoBeacons, RECOBeaconRegion recoRegion) {
            Log.i("RECORangingActivity", "didRangeBeaconsInRegion() region: " + recoRegion.getUniqueIdentifier() + ", number of beacons ranged: " + recoBeacons.size());


     /*
       ArrayList<RECOBeacon> Beacon = mRangingListAdapter.getAllBeacons(recoBeacons);
        String[] result;

        try {
            mRecoManager.unbind();

        }
        catch (RemoteException e) {
            Log.i("RECORangingActivity", "Remote Exception");
            e.printStackTrace();
        }
*/
            ArrayList<RECOBeacon> rb = (ArrayList<RECOBeacon>) recoBeacons;

            counter++;


            if (counter > 3) { //세번째 이상의 측정부터 제대로 측정을 하기 떄문에
                getBeaconAverage getAverage = new getBeaconAverage(); // 비콘데이터의 평균을 구하기 위한 클래스의 인스턴스 생성
                arrayRSSI = getAverage.mGetBeaconAverage(rb, arrayRSSI);   //평균을 구하는 메소드호출
                Log.i(counter + "", "");
                if (counter == 4) { //한번 읽어서 그 평균치로 구한다.
                    counter = 0;
                    try {
                        mRecoManager.unbind();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }


                }
            }
        }

    }
}
