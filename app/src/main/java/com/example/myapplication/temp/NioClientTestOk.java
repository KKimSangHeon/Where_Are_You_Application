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
import com.example.myapplication.forbeacon.RecoRangingListAdapter;
import com.example.myapplication.forbeacon.References;
import com.example.myapplication.forbeacon.getBeaconAverage;
import com.example.myapplication.socketdata.AverageRSSI;
import com.example.myapplication.socketdata.SendData;
import com.example.myapplication.socketdata.StudentInformation;
import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECORangingListener;
import com.perples.recosdk.RECOServiceConnectListener;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by 상헌 on 2016-07-18.
 * 안드로이드 통신+보안 프로그래밍 기초편 195page 참고
 */
public class NioClientTestOk extends Activity {

    String classID;
    String name;
    TextView textviewresult;
    Button stopButton;
    boolean quitChecker = true;
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
            NioClient client = new NioClient(ipaddress, 8097);
            client.execute();
        } catch (Exception e) {
        }
    }


    class NioClient extends AsyncTask<Void, Void, Void> implements RECORangingListener, RECOServiceConnectListener {
        private final String hostAddress;
        private final int port;
        private final Selector selector;
        private int PORT_NUMBER = 8097;
        private static final long TIME_OUT = 3000;
        private ByteBuffer readBuffer = ByteBuffer.allocate(10000000);
        SelectionKey tempkey;

        public NioClient(String hostAddress, int port) throws IOException {
            this.hostAddress = hostAddress;
            this.port = port;
            this.selector = Selector.open();
        }

        public SocketChannel createSocketChannel() throws IOException {
            //open 메소드를 통해 서버에 접속
            SocketChannel client = SocketChannel.open(new InetSocketAddress(hostAddress, port));

            client.configureBlocking(false);
            return client;
        }

        protected Void doInBackground(Void... arg0) {
            while (true) {
                SocketChannel client = null;
                try {
                    client = createSocketChannel();

                    if (!client.isConnected())
                        client.finishConnect();

                    client.register(selector, SelectionKey.OP_WRITE);

                    while (selector.select(TIME_OUT) != 0) {
                        Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();

                        while (selectedKeys.hasNext()) {
                            SelectionKey key = selectedKeys.next();
                            selectedKeys.remove();

                            if (!key.isValid()) continue;

                            if (key.isWritable()) {
                                {
                                    try {
                                        Thread.sleep(10000);
                                    } catch (InterruptedException e) {       }

                                    writeData(key);

                                    if(quitChecker==false) {
                                        return null;
                                    }
                                }
                            }
                        }
                    }

                } catch (EOFException e) {
                } //서버에서 접속 종료
                catch (IOException e) {
                } finally {
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

        }

        private void writeData(SelectionKey key) throws IOException {

            mRecoManager = RECOBeaconManager.getInstance(getApplicationContext(), References.SCAN_RECO_ONLY, References.ENABLE_BACKGROUND_RANGING_TIMEOUT);
            mRegions = this.generateBeaconRegion();

            mRecoManager.setRangingListener(this);
            mRecoManager.bind(this);
            StudentInformation si = new StudentInformation(name, classID);

            if (quitChecker == false) {
                si.checker = false;
            }

            SendData temp = new SendData(arrayRSSI, si);
            SocketChannel client = (SocketChannel) key.channel();
            readBuffer.clear();
            readBuffer.put(toByteArray(temp));
            readBuffer.flip();
            client.write(readBuffer);



            arrayRSSI = new ArrayList<AverageRSSI>();
        }

        public byte[] toByteArray(Object obj) {
            byte[] bytes = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(obj);
                oos.flush();
                oos.close();
                bos.close();
                bytes = bos.toByteArray();
            } catch (IOException ex) {
                //TODO: Handle the exception
            }
            return bytes;
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
