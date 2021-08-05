package com.example.standardmotion.ui.sportnews.util;

import com.example.standardmotion.ui.sportnews.gson.NewsList;
import com.google.gson.Gson;

public class Utility {
    public static NewsList parseJsonWithGson(final String requestText){
        Gson gson = new Gson();
        return gson.fromJson(requestText, NewsList.class);
    }

}