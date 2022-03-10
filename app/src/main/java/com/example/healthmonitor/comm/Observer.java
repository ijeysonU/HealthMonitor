package com.example.healthmonitor.comm;


import com.example.healthmonitor.BTLibrary.data.BleDevice;

public interface Observer {

    void disConnected(BleDevice bleDevice);
}
