package com.encrypto.android.geoencrypt.ui.termsandconditions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class tandcViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public tandcViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}