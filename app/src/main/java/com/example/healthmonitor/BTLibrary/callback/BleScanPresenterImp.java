package com.example.healthmonitor.BTLibrary.callback;

import com.example.healthmonitor.BTLibrary.data.BleDevice;

public interface BleScanPresenterImp {

    void onScanStarted(boolean success);

    void onScanning(BleDevice bleDevice);

}
