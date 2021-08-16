package com.example.standardmotion.ui.mymessage.recent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyMessageViewModelRecent extends ViewModel {
    private MutableLiveData<String> mText;

    public MyMessageViewModelRecent() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my message fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
