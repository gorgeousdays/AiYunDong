package com.example.standardmotion.ui.mymessage.result;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyMessageViewModelResult extends ViewModel {
    private MutableLiveData<String> mText;

    public MyMessageViewModelResult() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my message fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
