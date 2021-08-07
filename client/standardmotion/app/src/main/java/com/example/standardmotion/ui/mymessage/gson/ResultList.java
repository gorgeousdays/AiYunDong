package com.example.standardmotion.ui.mymessage.gson;

import com.example.standardmotion.ui.mymessage.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultList {
    public String message;

    @SerializedName("resultlist")
    public List<Result> resultList ;
}
