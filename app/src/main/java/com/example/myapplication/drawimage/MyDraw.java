package com.example.myapplication.drawimage;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.activity.StudentInfo;

import java.util.ArrayList;

/**
 * Created by 상헌 on 2016-08-14.
 */
public class MyDraw extends View {
    final int SCALE = 90;
    int isLine;
    public static int SIZE_Y=13;
    public static int SIZE_X=6;

    ArrayList<StudentInfo> SI;
    public MyDraw(Context context,ArrayList<StudentInfo> SI,int isLine) {
        super(context);
        this.SI=SI;
        this.isLine=isLine;
    }

    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.LTGRAY);
        Resources res = getResources();
        BitmapDrawable bd = (BitmapDrawable) res.getDrawable(R.drawable.s402);
        Bitmap bit = bd.getBitmap();
        canvas.drawBitmap(bit, null, new Rect(0, 0, SIZE_X * SCALE, SIZE_Y * SCALE), null);
        drawStudent(canvas,SI);
    }


    public void drawStudent(Canvas canvas,ArrayList<StudentInfo> SI) {
        Paint Pnt = new Paint();
        Pnt.setTextSize(30);
        if (isLine == 0) {
            for (int i = 0; i < SI.size(); i++) {
                canvas.drawCircle(SI.get(i).coordinate_x / 100 * SCALE, SI.get(i).coordinate_y / 100 * SCALE, 10, Pnt);
                canvas.drawText(" "+SI.get(i).name + "(" + SI.get(i).coordinate_x + "," + (MyDraw.SIZE_Y * 100 - SI.get(i).coordinate_y) + ")", SI.get(i).coordinate_x / 100 * SCALE, SI.get(i).coordinate_y / 100 * SCALE, Pnt);
            }
        }
        else{
            int coordinate_x=0,coordinate_y=0,p_coordinate_x,p_coordinate_y;


            for (int i = 0; i < SI.size(); i++) {

                canvas.drawCircle(SI.get(i).coordinate_x / 100 * SCALE, SI.get(i).coordinate_y / 100 * SCALE, 10, Pnt);
                canvas.drawText(" "+SI.get(i).name + "(" + SI.get(i).coordinate_x + "," + (MyDraw.SIZE_Y * 100 - SI.get(i).coordinate_y) + ")", SI.get(i).coordinate_x / 100 * SCALE, SI.get(i).coordinate_y / 100 * SCALE, Pnt);

                if(i<SI.size()-1) {
                    p_coordinate_x = SI.get(i).coordinate_x / 100 * SCALE;
                    p_coordinate_y = SI.get(i).coordinate_y / 100 * SCALE;
                    coordinate_x = SI.get(i+1).coordinate_x / 100 * SCALE;
                    coordinate_y = SI.get(i+1).coordinate_y / 100 * SCALE;
                   Pnt.setStrokeWidth(5);
                    canvas.drawLine(p_coordinate_x,p_coordinate_y,coordinate_x,coordinate_y,Pnt);
                }
            }

        }
    }
}

