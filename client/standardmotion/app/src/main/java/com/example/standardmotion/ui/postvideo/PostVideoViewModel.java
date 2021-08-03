package com.example.standardmotion.ui.postvideo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PostVideoViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public PostVideoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my post video fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
