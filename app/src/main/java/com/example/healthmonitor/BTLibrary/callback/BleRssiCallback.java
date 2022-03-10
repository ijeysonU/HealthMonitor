package com.example.healthmonitor.BTLibrary.callback;


import com.example.healthmonitor.BTLibrary.exception.BleException;

public abstract class BleRssiCallback extends BleBaseCallback{

    public abstract void onRssiFailure(BleException exception);

    public abstract void onRssiSuccess(int rssi);

}