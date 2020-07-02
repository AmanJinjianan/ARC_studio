package com.ui;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bluetoothARC.R;
import com.connect.BLEconfig;
import com.connect.BLEconfig.OnServiceDiscoverListener;
import com.connect.BLEconfig.OnWriteOverCallback;
import com.connect.DataCleanManager;
import com.connect.MyScanThread;
import com.connect.Tools;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This activity present a scanning-radar image,for scanning BLE devices
 * IF success ,jump to the menu
 * ELSE keep scanning
 * @author likun@stu.zzu.edu.cn
 *
 */
public class ScanActivity extends Activity{
	
	private boolean connected_flag;
	 private boolean exit_activity = false;
	private BluetoothDevice theDevice;
	static SoundPool lightsoundPool;
	static HashMap<Integer, Integer> soundMap;
	//private WindowManager wm;
	public String scanStatue = "no";
	
	//private DisplayMetrics dm;
	public int screenWidth=0,screenHeight=0;
	private int viewHeight;
	
	//BLEconfig mBLE;
	MyScanThread mScanThread;
	
	private BluetoothAdapter mBluetoothAdapter;
	Handler mHandler;
	ImageButton btnBack,scan_bluetooth;
	ImageView scan_frontImage,scan_backImage,scan_textImage;
	
	private DataCleanManager theDataCleanManager;
	
	TheVoiceThread VoiceThread ;
	
	Bitmap mBgBitmap = null,backBitmap = null;  
	
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
	            Tools.setLog("log1", "LeScanCallback.........."+device.getName());
	        	try {
	        		 if(null != device && null != device.getName()){
	 	                if (theDevice == null) {
	 	                    if (device.getName().equals("YinSuARC")) {
	 	                        theDevice = device;
	 	                        Log.e("falter","88888888888888888888888888888888888888888");
	 	                        myHandler.sendEmptyMessage(13);
	 	                    }
	 	                }
	 	            }
				} catch (Exception e) {
					myHandler.sendEmptyMessage(1003);
				}
	           
	        }
	    };
	    
	 private Handler myHandler = new Handler(){
	        public void handleMessage(Message msg) {
	            //Tools.setLog("log1", "myHandler.........."+msg.what);
	            switch (msg.what) {
	                case 10:
	                    bindService(new Intent(ScanActivity.this,BLEconfig.class), connection, Context.BIND_AUTO_CREATE);
	                    break;
	                case 11:
	                    if(Tools.mBleService != null)
	                        Tools.mBleService.scanBle(mLeScanCallback);
	                    break;
	                case 1003:
	                	Toast.makeText(ScanActivity.this, "Device not support.", Toast.LENGTH_LONG).show();
	                    break;
	                case 12:
	                    if(Tools.mBleService != null){
	                        Tools.mBleService.stopscanBle(mLeScanCallback);
	                        Tools.mBleService.setOnWriteOverCallback(mOnWriteOverCallback);
	                    }
	                    //Toast.makeText(ScanActivity.this, "连接成功。。。。。。。。。", Toast.LENGTH_LONG).show();
	                    Intent intent2 = new Intent("com.qixiang.blesuccess");
	                    sendBroadcast(intent2);

	                    Intent intent = new Intent(ScanActivity.this,MenuActivity.class);
	                    startActivity(intent);
	                    break;
	                case 122:
	                    if(Tools.mBleService != null){
	                        Tools.mBleService.stopscanBle(mLeScanCallback);
	                    }
	                    break;
	                case 13:
	                	Log.e("falter","1131313131133333333333333333333333333333333333333333333333333");
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
   
   private ServiceConnection connection = new ServiceConnection() {

       @Override
       public void onServiceDisconnected(ComponentName arg0) {}

       @Override
       public void onServiceConnected(ComponentName arg0, IBinder service) {
           // TODO Auto-generated method stub

           BLEconfig.LocalBinder binder = (BLEconfig.LocalBinder)service;
           Tools.mBleService = binder.getService();
           if (Tools.mBleService.initBle()) {
               if(!Tools.mBleService.mBluetoothAdapter.isEnabled()){
                   final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                   startActivityForResult(enableBtIntent, 2001);
               }else {
                   myHandler.sendEmptyMessage(11);
                   //scanBle();
               }
           }
       }
   };
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		//默认隐藏虚拟按键
		//getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION); 
		//禁止息屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_scan);

		initPosi();
		//The animation of radar
        Animation rotateAnim = AnimationUtils.loadAnimation(this, R.anim.image_rotation);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnim.setInterpolator(lin);
        
        if(rotateAnim != null){
        	scan_frontImage.startAnimation(rotateAnim);
        }
		//清除外部cache下的内容
		DataCleanManager.cleanExternalCache(this);
		//清除本应用内部缓存
		DataCleanManager.cleanInternalCache(this);
		//清除本应用数据库
		DataCleanManager.cleanDatabases(this);


		scan_bluetooth = (ImageButton) findViewById(R.id.btn_scan_activity_bluetooth_blue);
		btnBack = (ImageButton)findViewById(R.id.btn_scan_activity_back);
		btnBack.setOnClickListener(new backOnClickListener());
		
		
		 if (VoiceThread == null) {
				VoiceThread = new TheVoiceThread();
				VoiceThread.start();
			}
		 new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 checkBluetoothPermission();
			}
		}, 1000);
		
		
		
	
		if (!AppContext.lanuage) {
			RelativeLayout.LayoutParams paramC = new RelativeLayout.LayoutParams(screenWidth/3, screenWidth/8);
			paramC.setMargins((screenWidth-screenWidth/3)/2, (int)(screenWidth*0.9*0.26), 0, 0);
			scan_textImage.setLayoutParams(paramC);
			
	        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scantext2);  
	        scan_textImage.setBackgroundDrawable(new BitmapDrawable(mBgBitmap));  			
			btnBack.setBackgroundResource(R.drawable.back_btne);
		}
		if (AppContext.device == "phone"){
			RelativeLayout.LayoutParams paramsjh = new RelativeLayout.LayoutParams((int) (screenWidth*0.07),(int)(screenWidth*0.1));
			paramsjh.setMargins((int)(screenWidth*0.00), (int) (screenHeight*0.00), 0, 0);
			scan_bluetooth.setLayoutParams(paramsjh);
			RelativeLayout.LayoutParams paramsj = new RelativeLayout.LayoutParams((int) (screenWidth*0.1),(int)(screenWidth*0.1));
			paramsj.setMargins((int)(screenWidth*0.85), (int) (screenHeight*0.00), 0, 0);
			btnBack.setLayoutParams(paramsj);
			if (AppContext.lanuage) {//zh
				
				}else {
					
				}
			}else if(AppContext.device == "pad"){
				RelativeLayout.LayoutParams paramsjh = new RelativeLayout.LayoutParams((int) (screenWidth*0.07),(int)(screenWidth*0.1));
				paramsjh.setMargins((int)(screenWidth*0.00), (int) (screenHeight*0.00), 0, 0);
				scan_bluetooth.setLayoutParams(paramsjh);
				
				RelativeLayout.LayoutParams paramsj = new RelativeLayout.LayoutParams((int) (screenWidth*0.1),(int)(screenWidth*0.1));
				paramsj.setMargins((int)(screenWidth*0.85), (int) (screenHeight*0.00), 0, 0);
				btnBack.setLayoutParams(paramsj);
				if (AppContext.lanuage) {//zh
				}else {
				}
			}
	}
	//初始化蓝牙 和服务
	void InitBle2(){
		BluetoothManager bManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bAdapter = bManager.getAdapter();
        if(bAdapter == null){
            Toast.makeText(ScanActivity.this, "not support", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (bAdapter.isEnabled()) {
        	bindService(new Intent(this,BLEconfig.class), connection, Context.BIND_AUTO_CREATE);
		}else {
			bAdapter.enable();
		}
        

        
        setBroadcastReveiver();

        
	}
	 //设置广播接收器
    private void setBroadcastReveiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLEconfig.ACTION_STATE_CONNECTED);
        intentFilter.addAction(BLEconfig.ACTION_STATE_DISCONNECTED);
        intentFilter.addAction(BLEconfig.ACTION_WRITE_DESCRIPTOR_OVER);
        intentFilter.addAction(BLEconfig.ACTION_CHARACTER_CHANGE);

        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        bluetoothReceiver = new BluetoothReceiver();
        registerReceiver(bluetoothReceiver, intentFilter);
    }

    private BluetoothReceiver bluetoothReceiver = null;
    public class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();

            if(BLEconfig.ACTION_CHARACTER_CHANGE.equals(action)){

            }else if(BLEconfig.ACTION_STATE_CONNECTED.equals(action)){

            }else if(BLEconfig.ACTION_STATE_DISCONNECTED.equals(action)){
                connected_flag = false;
                theDevice = null;
                myHandler.sendEmptyMessage(11);

            }else if (BLEconfig.ACTION_WRITE_DESCRIPTOR_OVER.equals(action)) {
               connected_flag = true;
                myHandler.sendEmptyMessage(12);
            }else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            	int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch(blueState){

                    case BluetoothAdapter.STATE_ON:
                        //开始扫描
                    	bindService(new Intent(ScanActivity.this,BLEconfig.class), connection, Context.BIND_AUTO_CREATE);
                        break;

                }
               
            }
        }
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
				Manifest.permission.BLUETOOTH_ADMIN,
			};
			unPermissionList = new ArrayList<String>();
			try {
				for (int i = 0; i < mPermission.length; i++) {
					if (ContextCompat.checkSelfPermission(ScanActivity.this, mPermission[i]) != PackageManager.PERMISSION_GRANTED) {
						unPermissionList.add(mPermission[i]);
					}
				}
				for (int i = 0; i < mPermission.length; i++) {
					if (ContextCompat.checkSelfPermission(ScanActivity.this, "android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
						if (isLocationEnabled()) {
							
						}else {
							Toast.makeText(ScanActivity.this, "Please open the location switch.", Toast.LENGTH_LONG).show();
							Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivityForResult(intent, 0);
							break;
						}
					}
				}
			} catch (Exception e) {
				Toast.makeText(ScanActivity.this, "Exception0:"+e.toString(), Toast.LENGTH_LONG).show();
			}
			
		}else {
			InitBle2();
			return;
		}
			if (unPermissionList.isEmpty()) {
				if (isLocationEnabled())
				InitBle2();
				//都授权了。。。
			}else {
				
				try {
					String[] permissionStrings = unPermissionList.toArray(new String[unPermissionList.size()]);
					ActivityCompat.requestPermissions(this,permissionStrings, 1);
				} catch (Exception e) {
					Toast.makeText(ScanActivity.this, "Exception11:"+e.toString(), Toast.LENGTH_LONG).show();
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
    
    //StringBuilder sBuilder = new StringBuilder();
	@SuppressLint("NewApi")
	@Override
	public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		//sBuilder.append("permissions:"+permissions.length);
		
		//Toast.makeText(TestAct.this, "requestCode:"+requestCode, Toast.LENGTH_LONG).show();
		for (int i = 0; i < grantResults.length; i++) {
			//Toast.makeText(ScanActivity.this, permissions[i]+"     "+grantResults[i], Toast.LENGTH_LONG).show();
			
			if (permissions[i].equals("android.permission.ACCESS_COARSE_LOCATION")) {
				if ((grantResults[i] ==0)) {
					if(!isLocationEnabled()){
						Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivityForResult(intent, 0);
					}else {
						InitBle2();
					}
					
				}else {
					finish();
				}
				
			}
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0){
			if (isLocationEnabled()) {
				checkBluetoothPermission();
			}else {
				Toast.makeText(ScanActivity.this, "Please open the location switch.", Toast.LENGTH_LONG).show();
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivityForResult(intent, 0);
					}
				}, 1000);
			}
		}
	}

	@Override
    protected void onResume() {
		
		
		//checkBluetoothPermission();
		if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
			  //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			 }
		
        super.onResume();
       
		//start scanning thread
        //mScanThread.start();
		
		//The animation of radar
        
        Animation rotateAnim = AnimationUtils.loadAnimation(this, R.anim.image_rotation);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnim.setInterpolator(lin);
        
        if(rotateAnim!=null){
        	scan_frontImage.startAnimation(rotateAnim);
        }
    }
	@Override
	protected void onDestroy() {
		Toast.makeText(ScanActivity.this, "onDestroy...........", Toast.LENGTH_LONG).show();
		if(mBgBitmap != null  && !mBgBitmap.isRecycled())  
        {  
            mBgBitmap.recycle();  
            mBgBitmap = null;  
        }
        if(backBitmap != null  && !backBitmap.isRecycled())  
        {  
        	backBitmap.recycle();  
        	backBitmap = null;  
        }
		super.onDestroy();
		 exit_activity = true;
	     unbindService(connection);
	}
	class TheVoiceThread extends Thread{
		@SuppressLint("UseSparseArrays") @Override
		public void run() {
			lightsoundPool = new SoundPool(10,AudioManager.STREAM_MUSIC,0);
			
			soundMap = new HashMap<Integer, Integer>();
			
			soundMap.put(29, lightsoundPool.load(ScanActivity.this, R.raw.di_sound, 1));
			soundMap.put(28, lightsoundPool.load(ScanActivity.this, R.raw.three, 1));
			soundMap.put(27, lightsoundPool.load(ScanActivity.this, R.raw.two, 1));
			soundMap.put(26, lightsoundPool.load(ScanActivity.this, R.raw.one, 1));
			soundMap.put(25, lightsoundPool.load(ScanActivity.this, R.raw.go, 1));
			//soundMap.put(key, lightsoundPool.loa)
			//soundMap.put(1, lightsoundPool.load(ScanActivity.this, R.raw.di_sound, 1));
			soundMap.put(4, lightsoundPool.load(ScanActivity.this, R.raw.over_take, 1));
			soundMap.put(6, lightsoundPool.load(ScanActivity.this, R.raw.exchange, 1));
			soundMap.put(5, lightsoundPool.load(ScanActivity.this, R.raw.congradulate, 1));
			/*soundMap.put(2, lightsoundPool.load(ScanActivity.this, R.raw.count_down, 1));
			soundMap.put(3, lightsoundPool.load(ScanActivity.this, R.raw.over_game, 1));*/
			
			//if(AppContext.lanuage){
				/*Log.i("diedie", "1111111111");
				soundMap.put(25, lightsoundPool.load(ScanActivity.this, R.raw.go, 1));
				soundMap.put(26, lightsoundPool.load(ScanActivity.this, R.raw.one, 1));
				soundMap.put(27, lightsoundPool.load(ScanActivity.this, R.raw.two, 1));
				soundMap.put(28, lightsoundPool.load(ScanActivity.this, R.raw.three, 1));*/
				/*Log.i("diedie", "2");
				soundMap.put(7, lightsoundPool.load(ScanActivity.this, R.raw.a_oiless, 1));
				soundMap.put(8, lightsoundPool.load(ScanActivity.this, R.raw.a_please_refuel, 1));
				soundMap.put(9, lightsoundPool.load(ScanActivity.this, R.raw.a_refueling, 1));
				soundMap.put(10, lightsoundPool.load(ScanActivity.this, R.raw.a_refueled, 1));
				soundMap.put(11, lightsoundPool.load(ScanActivity.this, R.raw.a_rest_one_laps, 1));
				soundMap.put(12, lightsoundPool.load(ScanActivity.this, R.raw.a_rest_two_laps, 1));
				soundMap.put(13, lightsoundPool.load(ScanActivity.this, R.raw.a_damaged_over, 1));
				soundMap.put(14, lightsoundPool.load(ScanActivity.this, R.raw.a_normal_over, 1));
				soundMap.put(15, lightsoundPool.load(ScanActivity.this, R.raw.a_lead, 1));*/
				/*Log.i("diedie", "33");
				soundMap.put(16, lightsoundPool.load(ScanActivity.this, R.raw.b_oiless, 1));
				soundMap.put(17, lightsoundPool.load(ScanActivity.this, R.raw.b_please_refuel, 1));
				soundMap.put(18, lightsoundPool.load(ScanActivity.this, R.raw.b_refueling, 1));
				soundMap.put(19, lightsoundPool.load(ScanActivity.this, R.raw.b_refueled, 1));
				soundMap.put(20, lightsoundPool.load(ScanActivity.this, R.raw.b_rest_one_laps, 1));
				soundMap.put(21, lightsoundPool.load(ScanActivity.this, R.raw.b_rest_two_laps, 1));
				soundMap.put(22, lightsoundPool.load(ScanActivity.this, R.raw.b_damaged_over, 1));
				soundMap.put(23, lightsoundPool.load(ScanActivity.this, R.raw.b_normal_over, 1));
				soundMap.put(24, lightsoundPool.load(ScanActivity.this, R.raw.b_lead, 1));*/
				Log.i("diedie", "44");
				
			/*}else{
				Log.i("diedie", "0000000000");
				soundMap.put(25, lightsoundPool.load(ScanActivity.this, R.raw.go2, 1));
				soundMap.put(26, lightsoundPool.load(ScanActivity.this, R.raw.one2, 1));
				soundMap.put(27, lightsoundPool.load(ScanActivity.this, R.raw.two2, 1));
				soundMap.put(28, lightsoundPool.load(ScanActivity.this, R.raw.three2, 1));
				Log.i("diedie", "voice3333");
				soundMap.put(7, lightsoundPool.load(ScanActivity.this, R.raw.a_oilessp, 1));
				soundMap.put(8, lightsoundPool.load(ScanActivity.this, R.raw.a_please_refuelp, 1));
				soundMap.put(9, lightsoundPool.load(ScanActivity.this, R.raw.a_refuelingp, 1));
				soundMap.put(10, lightsoundPool.load(ScanActivity.this, R.raw.a_refueledp, 1));
				soundMap.put(11, lightsoundPool.load(ScanActivity.this, R.raw.a_rest_onep, 1));
				soundMap.put(12, lightsoundPool.load(ScanActivity.this, R.raw.a_rest_twop, 1));
				soundMap.put(13, lightsoundPool.load(ScanActivity.this, R.raw.a_damagedp, 1));
				soundMap.put(14, lightsoundPool.load(ScanActivity.this, R.raw.a_normalp, 1));
				soundMap.put(15, lightsoundPool.load(ScanActivity.this, R.raw.a_leadp, 1));
				
				soundMap.put(16, lightsoundPool.load(ScanActivity.this, R.raw.b_oilessp, 1));
				soundMap.put(17, lightsoundPool.load(ScanActivity.this, R.raw.b_please_refuelp, 1));
				soundMap.put(18, lightsoundPool.load(ScanActivity.this, R.raw.b_refuelingp, 1));
				soundMap.put(19, lightsoundPool.load(ScanActivity.this, R.raw.b_refueledp, 1));
				soundMap.put(20, lightsoundPool.load(ScanActivity.this, R.raw.b_rest_onep, 1));
				soundMap.put(21, lightsoundPool.load(ScanActivity.this, R.raw.b_rest_twop, 1));
				soundMap.put(22, lightsoundPool.load(ScanActivity.this, R.raw.b_damagedp, 1));
				soundMap.put(23, lightsoundPool.load(ScanActivity.this, R.raw.b_normalp, 1));
				soundMap.put(24, lightsoundPool.load(ScanActivity.this, R.raw.b_leadp, 1));
				
			}*/
			
			
			Log.i("diedie", "111");
		}
	}
	@Override
    protected void onPause() {
		super.onPause();
		//mScanThread.stop();
		if(Tools.mBleService !=null)
		Tools.mBleService.deviceScanStop();
	}
	
	@Override
    protected void onStop() {
		super.onStop();
		//Tools.mBleService.scanClose();
	}
	
	/**
	 * call when the service is discovered
	 * start the MenuActivity
	 */
	Intent mIntent;
	private BLEconfig.OnServiceDiscoverListener mOnServiceDiscover = new OnServiceDiscoverListener(){
		@Override
		public void onServiceDiscover(BluetoothGatt gatt) {
			//Log.i("niangde", "stop?");
			//Stop scanning thread
			mScanThread.stop =true;
			
			if(!mScanThread.isInterrupted() || mScanThread.isAlive()){
				/*mIntent.putExtra(BLEconfig.EXTRA_COMMAND, BLEconfig.COMMAND_CONNECT);
				startService(mIntent);*/
				if (scanStatue == "no") {
					Intent intent = new Intent(ScanActivity.this,MenuActivity.class);
					startActivity(intent);
					ScanActivity.this.finish();
					Log.i("diedie", "#######");
					scanStatue = "yes";
				}
				
			}else{
				mScanThread.interrupt();
			}
		}
	};
	
	/**
	 * listener of back button
	 *
	 */
	public class backOnClickListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			Tools.mBleService.close();
			ScanActivity.this.finish();
		}
	}
	private void initPosi() {
		// 得到屏幕宽高
		/*wm = this.getWindowManager();
		screenWidth = wm.getDefaultDisplay().getWidth();
		screenHeight = wm.getDefaultDisplay().getHeight();*/
		
		backBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		findViewById(R.id.scan_back).setBackgroundDrawable(new BitmapDrawable(backBitmap));
		
		
		
		//dm = new DisplayMetrics();
		//getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		screenHeight = AppContext.screenHeight;
		screenWidth = AppContext.screenWidth;
		//screenWidth = screenHeight*9/16;
		
		viewHeight = (int)(screenHeight*0.36);//得到scan_backImage的高度以及宽
		
		scan_backImage = (ImageView) findViewById(R.id.scan_back_image);
        //由于此scan背景是正方形，所以设置宽高相同
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(viewHeight, viewHeight);
		//设置位置
		param.setMargins((screenWidth-viewHeight)/2, (int)(screenHeight*0.9*0.3), 0, 0);
		scan_backImage.setLayoutParams(param);
		
		//设置前部雷达指针的宽高和位置
		scan_frontImage = (ImageView)findViewById(R.id.scan_front_image);
		RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(viewHeight/2, viewHeight/2);
		param1.setMargins(screenWidth/2, (int)(screenHeight*0.9*0.307), 0, 0);
		scan_frontImage.setLayoutParams(param1);
		
		scan_textImage = (ImageView)findViewById(R.id.scan_text_image);
		RelativeLayout.LayoutParams paramC = new RelativeLayout.LayoutParams((int)(screenWidth/4.5), screenWidth/8);
		paramC.setMargins((int)((screenWidth-screenWidth/4.5)/2), (int)(screenWidth*0.9*0.26), 0, 0);
		scan_textImage.setLayoutParams(paramC);
	}
	//the function of play voice
	/*public static void the_go_voice() {
  		lightsoundPool.play(soundMap.get(25), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	public static void one() {
  		//float i = 1.5;
  		lightsoundPool.play(soundMap.get(26), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	public static void two() {
  		lightsoundPool.play(soundMap.get(27), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	public static void three() {
  		Log.i("diedie", "voice33339999");
  		lightsoundPool.play(soundMap.get(28), 1, 1, 0, 0, 1(float)(1.5));
  	}*/
  	
  	
  	/*public static void soundPlaylight() {
  		lightsoundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
  	}*/
  	/*public static void soundPlay_countdown() {
  		lightsoundPool.play(soundMap.get(2), 1, 1, 0, 0, 1);
  	}
  	public static void soundPlayovergame() {
  		lightsoundPool.play(soundMap.get(3), 1, 1, 0, 0, 1);
  	}*/
	public static void the_go_voice() {
  		lightsoundPool.play(soundMap.get(25), 1, 1, 0, 0, 1);
  	}
  	public static void one() {
  		//float i = 1.5;
  		lightsoundPool.play(soundMap.get(26), 1, 1, 0, 0, 1);
  	}
  	public static void two() {
  		lightsoundPool.play(soundMap.get(27), 1, 1, 0, 0, 1);
  	}
  	public static void three() {
  		Log.i("DaYe", "333.......");
  		lightsoundPool.play(soundMap.get(28), 1, 1, 0, 0, 1);
  	}
  	public static void soundPlaylight() {
  		Log.i("DaYe", "didi......");
  		lightsoundPool.play(soundMap.get(29), 1, 1, 0, 0, 1);
  	}
  	
  	public static void soundPlay_overtake() {
  		lightsoundPool.play(soundMap.get(4), 1, 1, 0, 0, 1);
  	}
  	public static void soundPlay_congradulate() {
  		//lightsoundPool.p
  		lightsoundPool.play(soundMap.get(5), 1, 1, 0, 0, 1);
  	}
  	public static void soundPlay_exchange() {
		lightsoundPool.play(soundMap.get(6), 1, 1, 0, 0, 1);
	}
  	/*//a........
  	public static int a_oiless() {
  		return lightsoundPool.play(soundMap.get(7), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	public static int a_please_refuel() {
  		return lightsoundPool.play(soundMap.get(8), 1, 1, 0, 0, 1);
  	}
  	public static void a_refueling(){
  		lightsoundPool.play(soundMap.get(9), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	public static void a_refueled() {
  		lightsoundPool.play(soundMap.get(10), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	public static int a_rest_one_laps() {
  		return lightsoundPool.play(soundMap.get(11), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	public static int a_rest_two_laps() {
  		return lightsoundPool.play(soundMap.get(12), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	
  	
  	public static void a_damaged() {
  		lightsoundPool.play(soundMap.get(13), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	public static void a_normal_over() {
  		//lightsoundPool.play
  		lightsoundPool.play(soundMap.get(14), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	public static void a_lead() {
  		lightsoundPool.play(soundMap.get(15), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	//b.......
  	public static int b_oiless() {
  		return lightsoundPool.play(soundMap.get(16), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	public static int b_please_refuel() {
  		
  		return lightsoundPool.play(soundMap.get(17), 1, 1, 0, 0, 1);
  	}
  	public static void b_refueling() {
  		
  		lightsoundPool.play(soundMap.get(18), 1, 1, 0, 0, 1(float)(1.5));
  		
  	}
  	public static void b_refueled() {
  		lightsoundPool.play(soundMap.get(19), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	public static int b_rest_one_laps() {
  		return lightsoundPool.play(soundMap.get(20), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	public static int b_rest_two_laps() {
  		return lightsoundPool.play(soundMap.get(21), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	public static void b_damaged() {
  		lightsoundPool.play(soundMap.get(22), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	public static void b_normal_over() {
  		lightsoundPool.play(soundMap.get(23), 1, 1, 0, 0, 1(float)(1.5));
  	}
  	public static void b_lead() {
  		lightsoundPool.play(soundMap.get(24), 1, 1, 0, 0, 1(float)(1.5));
  	}*/
  	
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