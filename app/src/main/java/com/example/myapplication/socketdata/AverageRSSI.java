package com.example.myapplication.socketdata;

import java.io.Serializable;

/*
*  Created by KSH on 2016-03-19.
*  비콘 데이터의 평균치를 구하기 위한 클래스
*  getBeaconAverage 클래스를 위해 정의됨
*/
public class AverageRSSI implements Serializable {
    public int minor;
    public int count;
    public double sumOfRSSI;
    public double[] arrRSSI = new double[50];//분산을 구하기 위해 각 rssi 값을 배열에 저장
    public double sumOfAccuracy;


    public AverageRSSI() {
    }

    public AverageRSSI(int minor, int RSSI, double Accuracy) {
        this.minor = minor;
        sumOfRSSI = RSSI;
        sumOfAccuracy = Accuracy;
        count = 1;
        arrRSSI[count - 1] = RSSI;
    }

    public void setMinorAndRSSI(int minor, double RSSI) {
        this.minor = minor;
        sumOfRSSI = RSSI;
        count = 1;

    }


    public void increase(double RSSI)   //RSSI만 이용할 경우
    {
        count++;
        sumOfRSSI += RSSI;
    }

    public void increase(double RSSI, double accuracy)   //RSSI Accuracy 둘다 이용할 경우
    {
        arrRSSI[count] = RSSI;
        count++;
        sumOfAccuracy += accuracy;
        sumOfRSSI += RSSI;
    }

    public double getAverageRSSI() {
        return sumOfRSSI / count;
    }

    public double getAverageAccuracy() {
        return sumOfAccuracy / count;
    }

    public double getVar() {
        double var = 0;
        double temp = 0;

        for (int i = 0; i < count; i++) {
            temp += (arrRSSI[i] - getAverageRSSI()) * (arrRSSI[i] - getAverageRSSI());
        }
        var = temp / count;

        return var;
    }
}


