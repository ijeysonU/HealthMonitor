package com.example.healthmonitor;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthmonitor.BTLibrary.BleManager;
import com.example.healthmonitor.BTLibrary.callback.BleGattCallback;
import com.example.healthmonitor.BTLibrary.callback.BleIndicateCallback;
import com.example.healthmonitor.BTLibrary.callback.BleMtuChangedCallback;
import com.example.healthmonitor.BTLibrary.callback.BleNotifyCallback;
import com.example.healthmonitor.BTLibrary.callback.BleReadCallback;
import com.example.healthmonitor.BTLibrary.callback.BleRssiCallback;
import com.example.healthmonitor.BTLibrary.callback.BleScanCallback;
import com.example.healthmonitor.BTLibrary.callback.BleWriteCallback;
import com.example.healthmonitor.BTLibrary.data.BleDevice;
import com.example.healthmonitor.BTLibrary.exception.BleException;
import com.example.healthmonitor.BTLibrary.scan.BleScanRuleConfig;
import com.example.healthmonitor.BTLibrary.utils.HexUtil;
import com.example.healthmonitor.adapter.DeviceAdapter;
import com.example.healthmonitor.comm.ObserverManager;
import com.example.healthmonitor.operation.OperationActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link paciente_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class paciente_home extends Fragment implements View.OnClickListener, Runnable{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    private final int REQUEST_PERMISSION_ACCESS_FINE_LOCATION=1;


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;

    private BleDevice bleDevice; //xx
    private BluetoothGattService bluetoothGattService;//xx
    private BluetoothGattCharacteristic characteristic;//xx
    public static final int PROPERTY_READ = 1;//xx

    //BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(bleDevice);//xx

    private LinearLayout layout_setting;
    private TextView txt_setting;
    private Button btn_scan;
    private EditText et_name, et_mac, et_uuid;
    private Switch sw_auto;
    private ImageView img_loading;

    private Animation operatingAnim;
    private DeviceAdapter mDeviceAdapter;
    private ProgressDialog progressDialog;
    Thread hilo;

    public paciente_home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment paciente_home.
     */
    // TODO: Rename and change types and number of parameters
    public static paciente_home newInstance(String param1, String param2) {
        paciente_home fragment = new paciente_home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_paciente_home, container, false);
        initView();

        BleManager.getInstance().init(this.getActivity().getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        showConnectedDevice();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                if (btn_scan.getText().equals(getString(R.string.start_scan))) {
                    checkPermissions();
                } else if (btn_scan.getText().equals(getString(R.string.stop_scan))) {
                    BleManager.getInstance().cancelScan();
                }
                break;

            case R.id.txt_setting:
                if (layout_setting.getVisibility() == View.VISIBLE) {
                    layout_setting.setVisibility(View.GONE);
                    txt_setting.setText(getString(R.string.expand_search_settings));
                } else {
                    layout_setting.setVisibility(View.VISIBLE);
                    txt_setting.setText(getString(R.string.retrieve_search_settings));
                }
                break;
        }
    }

    private void initView() {
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        btn_scan = view.findViewById(R.id.btn_scan);
        btn_scan.setText(getString(R.string.start_scan));
        btn_scan.setOnClickListener(this);

        et_name = (EditText) view.findViewById(R.id.et_name);
        et_mac = (EditText) view.findViewById(R.id.et_mac);
        et_uuid = (EditText) view.findViewById(R.id.et_uuid);
        sw_auto = (Switch) view.findViewById(R.id.sw_auto);

        layout_setting = (LinearLayout) view.findViewById(R.id.layout_setting);
        txt_setting = (TextView) view.findViewById(R.id.txt_setting);
        txt_setting.setOnClickListener(this);
        layout_setting.setVisibility(View.GONE);
        txt_setting.setText(getString(R.string.expand_search_settings));

        img_loading = (ImageView) view.findViewById(R.id.img_loading);
        operatingAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.rotate);
        operatingAnim.setInterpolator(new LinearInterpolator());
        progressDialog = new ProgressDialog(view.getContext());

        mDeviceAdapter = new DeviceAdapter(view.getContext());
        mDeviceAdapter.setOnDeviceClickListener(new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onConnect(BleDevice bleDevice) {
                if (!BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().cancelScan();
                    connect(bleDevice);
                    btn_scan.setVisibility(view.GONE);
                    txt_setting.setVisibility(view.GONE);
                    mDeviceAdapter.clearScanDevice();

                    //layout_setting.setVisibility(view.GONE);
                }
            }

            @Override
            public void onDisConnect(final BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().disconnect(bleDevice);
                    btn_scan.setVisibility(view.VISIBLE);

                    //layout_setting.setVisibility(view.VISIBLE);
                }
            }

            @Override
            public void onDetail(BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {
                    Intent intent = new Intent(view.getContext(), OperationActivity.class);
                    intent.putExtra(OperationActivity.KEY_DATA, bleDevice);
                    startActivity(intent);
                    //loadFragment(new operationBT());
                }
            }
        });
        ListView listView_device = (ListView) view.findViewById(R.id.list_device);
        listView_device.setAdapter(mDeviceAdapter);
    }

    private void showConnectedDevice() {
        List<BleDevice> deviceList = BleManager.getInstance().getAllConnectedDevice();
        mDeviceAdapter.clearConnectedDevice();
        for (BleDevice bleDevice : deviceList) {
            mDeviceAdapter.addDevice(bleDevice);
        }
        mDeviceAdapter.notifyDataSetChanged();
    }

    private void setScanRule() {
        String[] uuids;
        String str_uuid = et_uuid.getText().toString();
        if (TextUtils.isEmpty(str_uuid)) {
            uuids = null;
        } else {
            uuids = str_uuid.split(",");
        }
        UUID[] serviceUuids = null;
        if (uuids != null && uuids.length > 0) {
            serviceUuids = new UUID[uuids.length];
            for (int i = 0; i < uuids.length; i++) {
                String name = uuids[i];
                String[] components = name.split("-");
                if (components.length != 5) {
                    serviceUuids[i] = null;
                } else {
                    serviceUuids[i] = UUID.fromString(uuids[i]);
                }
            }
        }

        String[] names;
        String str_name = et_name.getText().toString();
        if (TextUtils.isEmpty(str_name)) {
            names = null;
        } else {
            names = str_name.split(",");
        }

        String mac = et_mac.getText().toString();

        boolean isAutoConnect = sw_auto.isChecked();

        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(serviceUuids)
                .setDeviceName(true, names)
                .setDeviceMac(mac)
                .setAutoConnect(isAutoConnect)
                .setScanTimeOut(10000)
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    private void startScan() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.notifyDataSetChanged();
                img_loading.startAnimation(operatingAnim);
                img_loading.setVisibility(View.VISIBLE);
                btn_scan.setText(getString(R.string.stop_scan));
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                img_loading.clearAnimation();
                img_loading.setVisibility(View.INVISIBLE);
                btn_scan.setText(getString(R.string.start_scan));
            }
        });
    }

    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                img_loading.clearAnimation();
                img_loading.setVisibility(View.INVISIBLE);
                btn_scan.setText(getString(R.string.start_scan));
                progressDialog.dismiss();
                Toast.makeText(view.getContext(), getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
                showData(bleDevice);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected,
                                       BleDevice bleDevice,
                                       BluetoothGatt gatt,
                                       int status) {
                progressDialog.dismiss();

                mDeviceAdapter.removeDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();

                if (isActiveDisConnected) {
                    Toast.makeText(view.getContext(), getString(R.string.active_disconnected), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(view.getContext(), getString(R.string.disconnected), Toast.LENGTH_LONG).show();
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                }

            }
        });
    }

    private void readRssi(BleDevice bleDevice) {
        BleManager.getInstance().readRssi(bleDevice, new BleRssiCallback() {
            @Override
            public void onRssiFailure(BleException exception) {
                Log.i(TAG, "onRssiFailure" + exception.toString());
            }

            @Override
            public void onRssiSuccess(int rssi) {
                Log.i(TAG, "onRssiSuccess: " + rssi);
            }
        });
    }

    private void setMtu(BleDevice bleDevice, int mtu) {
        BleManager.getInstance().setMtu(bleDevice, mtu, new BleMtuChangedCallback() {
            @Override
            public void onSetMTUFailure(BleException exception) {
                Log.i(TAG, "onsetMTUFailure" + exception.toString());
            }

            @Override
            public void onMtuChanged(int mtu) {
                Log.i(TAG, "onMtuChanged: " + mtu);
            }
        });
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode,
                                                 @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            onPermissionGranted(permissions[i]);
                        }
                    }
                }
                break;
        }
    }

    private void checkPermissions() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(view.getContext(), getString(R.string.please_open_blue), Toast.LENGTH_LONG).show();
            return;
        }

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(view.getContext(), permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(getActivity(), deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle(R.string.notifyTitle)
                            .setMessage(R.string.gpsNotifyMsg)
                            .setNegativeButton(R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getActivity().finish();
                                        }
                                    })
                            .setPositiveButton(R.string.setting,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                        }
                                    })

                            .setCancelable(false)
                            .show();
                } else {
                    setScanRule();
                    startScan();
                }
                break;
        }
    }

    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OPEN_GPS) {
            if (checkGPSIsOpen()) {
                setScanRule();
                startScan();
            }
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
    private List<BluetoothGattService> bluetoothGattServices;
    private List<BluetoothGattCharacteristic> characteristicList;
    void addResult(BluetoothGattService service) {
        bluetoothGattServices.add(service);
    }
    void addResult(BluetoothGattCharacteristic characteristic) {
        characteristicList.add(characteristic);
    }
    public void showData(BleDevice bleDevice1) {
        bluetoothGattServices= new ArrayList<>();
        characteristicList = new ArrayList<>();
        final BleDevice bleDevice = bleDevice1;
        System.out.println(bleDevice1.getDevice().toString());
        BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(bleDevice);

        //System.out.println(gatt.get);
        for (BluetoothGattService service : gatt.getServices()) {
            addResult(service);
        }
        System.out.println("Servicios: "+gatt.getServices().size());
        System.out.println("UIID: "+bluetoothGattServices.get(2).getUuid());
        BluetoothGattService service = bluetoothGattServices.get(2);
        for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
            addResult(characteristic);
        }
        BluetoothGattCharacteristic characteristic =  bluetoothGattServices.get(2).getCharacteristic(characteristicList.get(1).getUuid());
        System.out.println(characteristicList.size());
        for (int i = 0; i< characteristicList.size(); i++){
            System.out.println("Char: "+characteristicList.get(i).getUuid());
        }

        //bluetoothGattServices.get(2).getUuid();
        //BluetoothGattService btd = new BluetoothGattService();
        //((OperationActivity) getActivity()).getBleDevice();
        //System.out.println(characteristicList.get(0).getUuid());
                //BluetoothGattCharacteristic characteristic = bluetoothGattService.getCharacteristic(characteristicList.get(0).getUuid());
                final int charaProp = PROPERTY_READ;
                //String child = characteristic.getUuid().toString() + String.valueOf(charaProp);
        //
                    //View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation, null);
                    //view.setTag(bleDevice.getKey() + characteristic.getUuid().toString() + charaProp);
                    //LinearLayout layout_add = (LinearLayout) view.findViewById(R.id.layout_add);
                    //final TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
                    //txt_title.setText(String.valueOf(characteristic.getUuid().toString() + getActivity().getString(R.string.data_changed)));
                    final TextView txt = (TextView) view.findViewById(R.id.txtdata);
//                    txt.setMovementMethod(ScrollingMovementMethod.getInstance());
                           switch (charaProp) {
                        case PROPERTY_READ: {

                                    BleManager.getInstance().read(
                                            bleDevice1,
                                            characteristic.getService().getUuid().toString(),
                                            characteristic.getUuid().toString(),
                                            new BleReadCallback() {
                                                       @Override
                                                public void onReadSuccess(final byte[] data) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            hilo = new Thread(this);
                                                            //for (int i = 0; i< data.length; i++){
                                                            //    System.out.println(data[i]);
                                                            //}
                                                            addText(txt, HexUtil.formatHexString(data, true));
                                                            //System.out.println(HexUtil.formatHexString(data, true));
                                                            hilo.start();
                                                        }
                                                    });
                                                }
                                                       @Override
                                                public void onReadFailure(final BleException exception) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            addText(txt, exception.toString());
                                                        }
                                                    });
                                                }
                                            });



                }
                break;
            }

            //layout_container.addView(view);

    }
    private void runOnUiThread(Runnable runnable) {
        if (isAdded() && getActivity() != null)
            getActivity().runOnUiThread(runnable);
    }

    private void addText(TextView textView, String content) {
        String hex = content;
        hex = hex.replace(" ", "");

        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i+=2) {
            String str = hex.substring(i, i+2);
            output.append((char)Integer.parseInt(str, 16));
        }
        System.out.println(output.toString().trim());
        //System.out.println(output.toString().trim());
        String valor = output.toString();
        textView.setText(valor);

        int offset = textView.getLineCount() * textView.getLineHeight();
        if (offset > textView.getHeight()) {
            textView.scrollTo(0, offset - textView.getHeight());
        }
    }

    @Override
    public void run() {
        Thread th = Thread.currentThread();
        while(th== hilo){
            //showData();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}