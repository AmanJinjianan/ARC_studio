package com.ui;


import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.connect.BLEconfig;
import com.connect.BLEconfig.OnWriteOverCallback;
import com.connect.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/28.
 */

public class BleConnectActivity extends Activity {

    private final static int REQUEST_ENABLE_BT=2001;
    private BluetoothDevice theDevice;
    private List<String> fdArrayList = new ArrayList<String>();
    private boolean connected_flag;
    private boolean exit_activity = false;
    public String tmp,hex;

        private OnWriteOverCallback mOnWriteOverCallback = new OnWriteOverCallback(){

        @Override
        public void OnWriteOver(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int statue) {

           /* if(statue == BluetoothGatt.GATT_SUCCESS){
                if(fdArrayList.size()!= 0){
                    fdArrayList.remove(0);
                }
            }else if(statue == BluetoothGatt.GATT_FAILURE){
                myHandler.sendEmptyMessage(8);
            }*/
        }

    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            //Tools.setLog("log1", "LeScanCallback.........."+device.getName());
            if(null != device && null != device.getName()){
                if (theDevice == null) {
                    if (device.getName().equals("TSnet")) {
                        theDevice = device;
                        myHandler.sendEmptyMessage(13);
                    }
                }
            }
        }
    };
        private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder service) {
            // TODO Auto-generated method stub

            Tools.setLog("log1", "onServiceConnected..........");

            BLEconfig.LocalBinder binder = (BLEconfig.LocalBinder)service;
            Tools.mBleService = binder.getService();
            if (Tools.mBleService.initBle()) {
                if(!Tools.mBleService.mBluetoothAdapter.isEnabled()){
                    final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }else {
                    myHandler.sendEmptyMessage(11);
                    //scanBle();
                }
            }
        }
    };
        private Handler myHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            //Tools.setLog("log1", "myHandler.........."+msg.what);
            switch (msg.what) {
                case 111:
                    //Toast.makeText(MainActivity.this, "startActivityForResult..", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(MainActivity.this,VideoActivity.class);
                    //startActivityForResult(intent, 12);
                    break;
                case 8:
                    //Toast.makeText(MainActivity.this, "write fail..", Toast.LENGTH_SHORT).show();
                    break;
                case 9:
                    //Toast.makeText(MainActivity.this, msg.getData().getString("daa"), Toast.LENGTH_SHORT).show();
                    break;
                case 10:
                    bindService(new Intent(BleConnectActivity.this,BLEconfig.class), connection, Context.BIND_AUTO_CREATE);
                    break;
                case 11:
                    if(Tools.mBleService != null)
                        Tools.mBleService.scanBle(mLeScanCallback);
                    break;

                case 12:
                    if(Tools.mBleService != null){
                        Tools.mBleService.stopscanBle(mLeScanCallback);
                        Tools.mBleService.setOnWriteOverCallback(mOnWriteOverCallback);
                    }
                    Toast.makeText(BleConnectActivity.this, "连接成功。。。。。。。。。", Toast.LENGTH_LONG).show();
                    Intent intent2 = new Intent("com.qixiang.blesuccess");
                    sendBroadcast(intent2);

                   /* Intent intent = new Intent(BleConnectActivity.this,MainActivity.class);
                    startActivity(intent);*/
                    break;
                case 122:
                    if(Tools.mBleService != null){
                        Tools.mBleService.stopscanBle(mLeScanCallback);
                    }
                    break;
                case 13:
                    new MyConnectedThread().start();
                    break;
                case 14:
                    Bundle bundle=msg.getData();
                    String string =(String) bundle.get("Key");
                    Tools.setLog("log1", "msg.what 14:"+string);
                    //Tools.mBleService.characterWrite1.setValue(hexToBytes(string));
                    //Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
                    break;
                default:
                    break;
            }
        };
    };
    Animation myAnimation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bleconnect);

        BluetoothManager bManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bAdapter = bManager.getAdapter();
        if(bAdapter == null){
            Toast.makeText(BleConnectActivity.this, "not support", Toast.LENGTH_SHORT).show();
            finish();
        }
        bAdapter.enable();

        getPermission();
        setBroadcastReveiver();

        bindService(new Intent(this,BLEconfig.class), connection, Context.BIND_AUTO_CREATE);

        /*findViewById(R.id.btn_ble_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.mBleService.characterWrite1.setValue(hexToBytes("06241112131410"));
                Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
            }
        });*/
        //myAnimation = AnimationUtils.loadAnimation(this, R.anim.rotation1);
        //findViewById(R.id.imageView111).startAnimation(myAnimation);
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT<23) {
            return;
        }

        String[] mPermissionList = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
        };

        ArrayList<String> unPermissionList = new ArrayList<String>();

        for(int i=0;i<mPermissionList.length;i++){
            if(ContextCompat.checkSelfPermission(BleConnectActivity.this, mPermissionList[i]) != PackageManager.PERMISSION_GRANTED){
                unPermissionList.add(mPermissionList[i]);
            }
        }
        if (unPermissionList.isEmpty()) {
            //都授权了
        }else {
            String[] ggStrings= unPermissionList.toArray(new String[unPermissionList.size()]);
            ActivityCompat.requestPermissions(BleConnectActivity.this, ggStrings, 12);
        }
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.e("sunlei","FirstActivity---onPause");
    }
    //设置广播接收器
    private void setBroadcastReveiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLEconfig.ACTION_STATE_CONNECTED);
        intentFilter.addAction(BLEconfig.ACTION_STATE_DISCONNECTED);
        intentFilter.addAction(BLEconfig.ACTION_WRITE_DESCRIPTOR_OVER);
        //intentFilter.addAction(BLEService.ACTION_CHARACTER_CHANGE);

        bluetoothReceiver = new BluetoothReceiver();
        registerReceiver(bluetoothReceiver, intentFilter);
    }

    private BluetoothReceiver bluetoothReceiver = null;
    public class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {/*
            // TODO Auto-generated method stub
            String action = intent.getAction();

            if(BLEService.ACTION_CHARACTER_CHANGE.equals(action)){
                //tmp_byte = characteristic.getValue();
                byte[] tmp_byte = intent.getByteArrayExtra("value");
                tmp = "";
                for (int i = 0; i < tmp_byte.length; i++) {
                    hex = Integer.toHexString(tmp_byte[i] & 0xFF);
                    if (hex.length() == 1) {
                        hex = '0' + hex;
                    }
                    tmp = tmp + hex;
                }
            }else if(BLEService.ACTION_STATE_CONNECTED.equals(action)){

            }else if(BLEService.ACTION_STATE_DISCONNECTED.equals(action)){
                connected_flag = false;
                theDevice = null;
                myHandler.sendEmptyMessage(11);

            }else if (BLEService.ACTION_WRITE_DESCRIPTOR_OVER.equals(action)) {
               connected_flag = true;
                myHandler.sendEmptyMessage(12);
            }
        */}
    }

        @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        exit_activity = true;
        unbindService(connection);
    }

     public byte[] hexToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] bytes = new byte[length];
        String hexDigits = "0123456789ABCDEF";
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            int h = hexDigits.indexOf(hexChars[pos]) << 4;
            int l = hexDigits.indexOf(hexChars[pos + 1]);
            if (h == -1 || l == -1) {
                return null;
            }
            bytes[i] = (byte) (h | l);
        }
        return bytes;
    }

        public class MyConnectedThread extends Thread{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();

            try {
                while (true) {
                    connected_flag = false;
                    if (exit_activity) return;
                    Tools.setLog("log1", "connectBle..........");
                    Tools.mBleService.connectBle(theDevice);

                    for(int j=0;j<50;j++){

                        if (connected_flag) {
                            break;
                        }
                        sleep(100);
                    }

                    if(connected_flag)
                        break;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
}
