package com.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bluetoothARC.R;

import java.util.ArrayList;

public class TestAct extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.hjy);
		checkBluetoothPermission() ;
	}

	ArrayList<String> unPermissionList;
	private void checkBluetoothPermission() { 
		if (Build.VERSION.SDK_INT >= 23) { 
			
			String[] mPermission = new String[]{
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.BLUETOOTH,
				Manifest.permission.BLUETOOTH_ADMIN
			};
			unPermissionList = new ArrayList<String>();
			try {
				for (int i = 0; i < mPermission.length; i++) {
					if (ContextCompat.checkSelfPermission(TestAct.this, mPermission[i]) != PackageManager.PERMISSION_GRANTED) {
						unPermissionList.add(mPermission[i]);
					}
				}
				for (int i = 0; i < mPermission.length; i++) {
					if (ContextCompat.checkSelfPermission(TestAct.this, "android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
						if (isLocationEnabled()) {
							
						}else {
							Toast.makeText(TestAct.this, "Please open the location switch.", Toast.LENGTH_LONG).show();
							Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivityForResult(intent, 10111);
						}
					}
				}
			} catch (Exception e) {
				Toast.makeText(TestAct.this, "Exception0:"+e.toString(), Toast.LENGTH_LONG).show();
			}
			
		}else {
			//InitBle();
			return;
		}
			if (unPermissionList.isEmpty()) {
				//都授权了。。。
			}else {
				
				try {
					String[] permissionStrings = unPermissionList.toArray(new String[unPermissionList.size()]);
					ActivityCompat.requestPermissions(this, permissionStrings, 1212);
				} catch (Exception e) {
					Toast.makeText(TestAct.this, "Exception11:"+e.toString(), Toast.LENGTH_LONG).show();
				}
			}
			
			
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					;
					//Toast.makeText(TestAct.this, ".............:"+sBuilder.toString(), Toast.LENGTH_LONG).show();
				}
			}, 5000);
			
	} 
	
	StringBuilder sBuilder = new StringBuilder();
	@SuppressLint("NewApi")
	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		
		sBuilder.append("permissions:"+permissions.length);
		
		//Toast.makeText(TestAct.this, "requestCode:"+requestCode, Toast.LENGTH_LONG).show();
		for (int i = 0; i < grantResults.length; i++) {
			Toast.makeText(TestAct.this, permissions[i]+"     "+grantResults[i], Toast.LENGTH_LONG).show();
			
			if (permissions[i].equals("android.permission.ACCESS_COARSE_LOCATION")) {
				if ((grantResults[i] ==0)) {
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivityForResult(intent, 10111);
				}else {
					finish();
				}
				
			}
		}
		
	}
	/**
     * 判断定位服务是否开启
     *
     * @param
     * @return true 表示开启
     */
    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 10111){
			if (isLocationEnabled()) {
			}else {
				Toast.makeText(TestAct.this, "Please open the location switch.", Toast.LENGTH_LONG).show();
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivityForResult(intent, 10111);
					}
				}, 1000);
			}
		}
	}
	
	

}
