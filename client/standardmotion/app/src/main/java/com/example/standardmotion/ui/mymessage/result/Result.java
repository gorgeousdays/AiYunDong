package com.example.standardmotion.ui.mymessage.result;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

//implements Serializable
//implements Parcelable 尝试用接口 出Bug
public class Result  {
    private String id;
    private String time;
    private String num;
    private String advice;
    private String energy;
    private String img;

    public String getId() {
        return id;
    }
    public String getTime() {
        return time;
    }
    public String getNum(){
        return  num;
    }
    public String getAdvice() {
        return advice;
    }
    public String getEnergy(){
        return  energy;
    }
    public String getImgurl(){return  img;}
}
