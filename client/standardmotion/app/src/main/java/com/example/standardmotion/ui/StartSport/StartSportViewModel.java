package com.example.standardmotion.ui.StartSport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StartSportViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StartSportViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is startsport fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
