
package com.example.healthmonitor.BTLibrary.callback;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.os.Build;

import com.example.healthmonitor.BTLibrary.data.BleDevice;
import com.example.healthmonitor.BTLibrary.exception.BleException;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public abstract class BleGattCallback extends BluetoothGattCallback {

    public abstract void onStartConnect();

    public abstract void onConnectFail(BleDevice bleDevice, BleException exception);

    public abstract void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status);

    public abstract void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status);

}