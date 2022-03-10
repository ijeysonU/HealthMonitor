package com.example.healthmonitor.BTLibrary.callback;


import com.example.healthmonitor.BTLibrary.exception.BleException;

public abstract class BleNotifyCallback extends BleBaseCallback {

    public abstract void onNotifySuccess();

    public abstract void onNotifyFailure(BleException exception);

    public abstract void onCharacteristicChanged(byte[] data);

}
