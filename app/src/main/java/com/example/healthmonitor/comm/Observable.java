package com.example.healthmonitor.comm;


import com.example.healthmonitor.BTLibrary.data.BleDevice;

public interface Observable {

    void addObserver(Observer obj);

    void deleteObserver(Observer obj);

    void notifyObserver(BleDevice bleDevice);
}
