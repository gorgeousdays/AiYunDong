package com.example.standardmotion.ui.sportnews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SportNewsViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public SportNewsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is sport news fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
