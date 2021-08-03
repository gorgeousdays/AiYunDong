package com.example.standardmotion.ui.mymessage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyMessageViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public MyMessageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my message fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
