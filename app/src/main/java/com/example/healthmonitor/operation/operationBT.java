package com.example.healthmonitor.operation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.fragment.app.FragmentTransaction;

import com.example.healthmonitor.R;
import com.example.healthmonitor.comm.Observer;
import com.example.healthmonitor.comm.ObserverManager;
import com.example.healthmonitor.BTLibrary.BleManager;
import com.example.healthmonitor.BTLibrary.data.BleDevice;

import java.util.ArrayList;
import java.util.List;

import com.example.healthmonitor.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link operationBT#newInstance} factory method to
 * create an instance of this fragment.
 */
public class operationBT extends Fragment implements Observer {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String KEY_DATA = "key_data";

    private BleDevice bleDevice;
    private BluetoothGattService bluetoothGattService;
    private BluetoothGattCharacteristic characteristic;
    private int charaProp;
    View view;

    //private Toolbar toolbar;
    private List<Fragment> fragments = new ArrayList<>();
    private int currentPage = 0;
    private String[] titles = new String[3];
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public operationBT() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment operationBT.
     */
    // TODO: Rename and change types and number of parameters
    public static operationBT newInstance(String param1, String param2) {
        operationBT fragment = new operationBT();
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
        view = inflater.inflate(R.layout.fragment_operation_b_t, container, false);

        initData();
        //initView();
        initPage();

        ObserverManager.getInstance().addObserver(this);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().clearCharacterCallback(bleDevice);
        ObserverManager.getInstance().deleteObserver(this);
    }

    @Override
    public void disConnected(BleDevice device) {
        if (device != null && bleDevice != null && device.getKey().equals(bleDevice.getKey())) {
            getActivity().finish();
        }
    }

    //@Override
    //public boolean onKeyDown(int keyCode, KeyEvent event) {
    //    if (keyCode == KeyEvent.KEYCODE_BACK) {
    //        if (currentPage != 0) {
    //            currentPage--;
    //            changePage(currentPage);
    //            return true;
    //        } else {
    //           getActivity().finish();
    //            return true;
    //        }
    //    }
    //    return super.getActivity().onKeyDown(keyCode, event);
    //}



    private void initData() {
        bleDevice = getActivity().getIntent().getParcelableExtra(KEY_DATA);
        if (bleDevice == null)
            getActivity().finish();

        titles = new String[]{
                getString(R.string.service_list),
                getString(R.string.characteristic_list),
                getString(R.string.console)};
    }

    private void initPage() {
        prepareFragment();
        changePage(0);
    }

    public void changePage(int page) {
        currentPage = page;
        //toolbar.setTitle(titles[page]);
        updateFragment(page);
        if (currentPage == 1) {
            ((CharacteristicListFragment) fragments.get(1)).showData();
        } else if (currentPage == 2) {
            ((CharacteristicOperationFragment) fragments.get(2)).showData();
        }
    }

    private void prepareFragment() {
        fragments.add(new ServiceListFragment());
        fragments.add(new CharacteristicListFragment());
        fragments.add(new CharacteristicOperationFragment());
        for (Fragment fragment : fragments) {
            this.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment).hide(fragment).commit();
        }
    }

    private void updateFragment(int position) {
        if (position > fragments.size() - 1) {
            return;
        }
        for (int i = 0; i < fragments.size(); i++) {
            FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
            Fragment fragment = fragments.get(i);
            if (i == position) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
            transaction.commit();
        }
    }

    public BleDevice getBleDevice() {
        return bleDevice;
    }

    public BluetoothGattService getBluetoothGattService() {
        return bluetoothGattService;
    }

    public void setBluetoothGattService(BluetoothGattService bluetoothGattService) {
        this.bluetoothGattService = bluetoothGattService;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
    }

    public int getCharaProp() {
        return charaProp;
    }

    public void setCharaProp(int charaProp) {
        this.charaProp = charaProp;
    }

}