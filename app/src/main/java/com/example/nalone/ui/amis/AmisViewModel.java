package com.example.nalone.ui.amis;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AmisViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AmisViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard amis");
    }

    public LiveData<String> getText() {
        return mText;
    }
}