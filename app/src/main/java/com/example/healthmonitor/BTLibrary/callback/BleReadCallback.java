package com.example.healthmonitor.BTLibrary.callback;



import com.example.healthmonitor.BTLibrary.exception.BleException;

public abstract class BleReadCallback extends BleBaseCallback {

    public abstract void onReadSuccess(byte[] data);

    public abstract void onReadFailure(BleException exception);

}
