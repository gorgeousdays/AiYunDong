package com.example.standardmotion.ui.mymessage.util;

import com.example.standardmotion.ui.mymessage.gson.ResultList;
import com.google.gson.Gson;

public class Utility {
    public static ResultList parseJsonWithGson(final String requestText){
        Gson gson = new Gson();
        return gson.fromJson(requestText, ResultList.class);
    }

}
