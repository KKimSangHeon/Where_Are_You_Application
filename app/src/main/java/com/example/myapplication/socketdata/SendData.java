package com.example.myapplication.socketdata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 상헌 on 2016-07-05.
 */
public class SendData implements Serializable {
    public ArrayList<AverageRSSI> averageRSSI;
    public StudentInformation studentInformation;

    public SendData(ArrayList<AverageRSSI> averageRSSI,StudentInformation studentInformation)
    {
        this.averageRSSI=averageRSSI;
        this.studentInformation=studentInformation;
    }
    public ArrayList<AverageRSSI> getAverageRSSIlist()
    {
        return  averageRSSI;
    }
    public StudentInformation getStudentInformation()
    {
        return  studentInformation;
    }
}

