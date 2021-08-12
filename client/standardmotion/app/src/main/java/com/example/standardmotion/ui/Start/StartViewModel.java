package com.example.standardmotion.ui.Start;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class StartViewModel {
    private MutableLiveData<String> mText;

    public StartViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my start fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
