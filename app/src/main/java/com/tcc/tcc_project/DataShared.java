package com.tcc.tcc_project;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;

public class DataShared extends ViewModel {
    private MutableLiveData<String> gravity;

    public void init() {
        gravity = new MutableLiveData<>();
    }

    public void sendString(String msn){
        gravity.setValue(msn);
    }

    public LiveData<String> getMsn() {
        return gravity;
    }
}
