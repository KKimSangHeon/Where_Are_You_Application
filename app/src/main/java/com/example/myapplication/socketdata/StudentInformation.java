package com.example.myapplication.socketdata;

import java.io.Serializable;

/**
 * Created by 상헌 on 2016-07-05.
 */
public class StudentInformation implements Serializable {
    public String name;
    public String classID;
    public boolean checker;

    public StudentInformation(String name, String classID)
    {
        this.name=name;
        this.classID=classID;
        checker=true;
    }

    public String getName()
    {
        return name;
    }

    public String getClassID()
    {
        return classID;
    }
}
