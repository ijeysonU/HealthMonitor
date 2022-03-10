package com.example.healthmonitor.BTLibrary.callback;


import com.example.healthmonitor.BTLibrary.exception.BleException;

public abstract class BleWriteCallback extends BleBaseCallback{

    public abstract void onWriteSuccess(int current, int total, byte[] justWrite);

    public abstract void onWriteFailure(BleException exception);

}
