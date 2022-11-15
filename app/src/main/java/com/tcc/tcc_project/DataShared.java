package com.tcc.tcc_project;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;

public class DataShared extends ViewModel {
    private MutableLiveData<String> gravity;
    private MutableLiveData<String> velocity;

    public void init() {
        gravity = new MutableLiveData<>();
        velocity = new MutableLiveData<>();
    }

    public void sendString(String msn){
        gravity.setValue(msn);
    }

    public void sendVelocity(String msn){
        velocity.setValue(msn);
    }

    public LiveData<String> getVel() {
        return velocity;
    }

    public LiveData<String> getMsn() {
        return gravity;
    }
}
