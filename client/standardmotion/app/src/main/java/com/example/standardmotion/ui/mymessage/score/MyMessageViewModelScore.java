package com.example.standardmotion.ui.mymessage.score;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyMessageViewModelScore extends ViewModel {
    private MutableLiveData<String> mText;

    public MyMessageViewModelScore() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my message fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
