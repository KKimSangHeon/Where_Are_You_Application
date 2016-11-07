package com.example.myapplication.forbeacon;

/**
 * Created by 상헌 on 2016-03-19.
 */
import android.content.Context;

import com.perples.recosdk.RECOBeacon;

import java.util.ArrayList;
import java.util.Collection;

public class RecoRangingListAdapter {
    private ArrayList<RECOBeacon> mRangedBeacons;
    String resultString;


    public RecoRangingListAdapter(Context context) {
        super();
        mRangedBeacons = new ArrayList<RECOBeacon>();
      }

    public void updateBeacon(RECOBeacon beacon) {
        synchronized (mRangedBeacons) {
            if(mRangedBeacons.contains(beacon)) {
                mRangedBeacons.remove(beacon);
            }
            mRangedBeacons.add(beacon);

        }
    }

    /*
    * method : updateNearstBeacons(Collection<RECOBeacon> beacons)
    * 기존 반환형이 였던 void를 String로 바꿈.
    * getNearBeacon()메소드에 비콘의 ArrayList를 매개변수로 전달하여 가장 가까운 비콘의 값을 알아낸다.
    * 추가된 코드
    *        nearBeacon=getNearBeacon(mRangedBeacons);
    *        return nearBeacon;
    */

    public String updateBeacons(Collection<RECOBeacon> beacons,int select) {
        synchronized (beacons) {
            mRangedBeacons = new ArrayList<RECOBeacon>(beacons);
            if(select==0) {
                resultString = getNearBeacon(mRangedBeacons);
            }
            return resultString;
        }
    }

    public ArrayList<RECOBeacon> getAllBeacons(Collection<RECOBeacon> beacons) {
        synchronized (beacons) {
            mRangedBeacons = new ArrayList<RECOBeacon>(beacons);
            return mRangedBeacons;
        }
    }


    public void clear() {
        mRangedBeacons.clear();
    }

    /* method name :getNearBeaconString()
    * return data : Closest Beacon
    * */
    public String getNearBeacon( ArrayList<RECOBeacon> beacons)
    {
        int maxRSSI=-99999;
        String result="비콘없음";
        for(int i=0;i<beacons.size();i++)
        {
            if(beacons.get(i).getRssi()>maxRSSI)
            {
              maxRSSI=beacons.get(i).getRssi();
             result=beacons.get(i).getMinor()+"가 가장 가까운 비콘입니다.";
            }
        }
    return result;
    }
}

