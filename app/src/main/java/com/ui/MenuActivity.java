package com.ui;

import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.bluetoothARC.R;
import com.connect.MyReConnThread;
import com.connect.Tools;
//import com.ui.LapSelectDialog.OnDialogButtonClickListener;
import com.myview.PanelViewA;
import com.myview.PanelViewB;

import com.connect.BLEconfig;
import com.connect.BLEconfig.OnServiceDiscoverListener;
/**
 * This activity is the main menu
 * three options below:
 * 1. train : goto the train activity
 * 2. race  : goto the race  activity
 * 3. setting : set laps number
 *  
 * @author likun@stu.zzu.edu.cn
 *
 */
public class MenuActivity extends Activity implements OnClickListener
{	
	//public static String isVoice= "be voice";
	public static boolean isVoice = true;
	//static SoundPool lightsoundPool;
	//static HashMap<Integer, Integer> soundMap;
	//private DisplayMetrics dm;
	private ImageButton setButton,guideButton;
	private ImageButton settingBtn,guidingBtn;
	private ImageButton athensBtn,rioBtn,beijing,new_yorkBtn,londonBtn,  
						paris,oslo,prague,copenhague,rome,stockholm,
						berlin,tokyo,mexico,sydney,toronto,saint_peter,custom;
	public int screenWidth,screenHeight;
	public float scaledDensity;
	MyReConnThread mScanThread = null;
	Intent mIntent;
	LightDialog lightDialog;
	
	public static Context theContext;
	//姝ゅ閽堝鍦ㄧ獥鍙ｉ〉闈㈡寜home閿�
	public static String activity_state = "not Exit";
	
	BLEconfig mBLE;
	
	@SuppressLint("UseSparseArrays") @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//NO_TITLE
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//FULLSCREEN
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		//hide virtual button
		//getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION); 
		//KEEP_SCREEN_ON
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_menu);
		
		
		System.gc();
		
		Log.i("diedie", "*********");
		
		mIntent = new Intent(MenuActivity.this,BLEconfig.class);
		if(mIntent != null)
		Log.i("niangde", "not null");
		if(mIntent == null)
			Log.i("niangde", "null..");


		initPosi();
		
		
		
		theContext = this;
		
		@SuppressWarnings("unused")
		class MyReceiver extends BroadcastReceiver {

			@Override
			public void onReceive(Context arg0, Intent arg1){ 
				// TODO Auto-generated method stub
				Log.i("address", "55disconnected...............");
				
				//findViewById(R.id.btn_menu_activity_bluetooth_blue).setBackgroundResource(R.drawable.bluetooth_red);
				
				
				if(mScanThread == null){
					Log.i("address", "MyScanThread is null...............");
					mScanThread = new MyReConnThread();
					Log.i("niangde", "mScanThread...............");
					
					mScanThread.start();
				}
				if(mScanThread != null)
				mScanThread.theThreadState =true;
				
			}
			}
		
		/*IntentFilter filter = new IntentFilter("theScanResult");
		MyReceiver receiver = new MyReceiver();
		registerReceiver(receiver, filter);*/
		
		@SuppressWarnings("unused")
		class MyReceiverD extends BroadcastReceiver {

			@Override
			public void onReceive(Context arg0, Intent arg1){ 
				// TODO Auto-generated method stub
				Log.i("address", "CCconnected...............");
				
				//findViewById(R.id.btn_menu_activity_bluetooth_blue).setBackgroundResource(R.drawable.bluetooth_blue);
			
				
			}
			}
		
		/*IntentFilter filterD = new IntentFilter("theScanResultD");
		MyReceiverD receiverD = new MyReceiverD();
		registerReceiver(receiverD, filterD);*/
		
		//mBLE = BLEconfig.getInstance();
		//mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
		
		
	}
	
	public static byte[] LongToBytes(long values) {  
	     byte[] buffer = new byte[8]; 
	     for (int i = 0; i < 8; i++) {   
	          int offset = 64 - (i + 1) * 8;    
	          buffer[i] = (byte) ((values >> offset) & 0xff); 
	      }
	     return buffer;  
	}

	 
	private BLEconfig.OnServiceDiscoverListener mOnServiceDiscover = new OnServiceDiscoverListener(){
		@Override
		public void onServiceDiscover(BluetoothGatt gatt) {
			//findViewById(R.id.btn_menu_activity_bluetooth_blue).setBackgroundResource(R.drawable.bluetooth_blue);
			Log.i("niangde", "stop????11111111111122");
			//Stop scanning thread
			if(mScanThread != null){
			mScanThread.theThreadState =false;
			//mScanThread = null;
			}
			
			//BLEconfig.theState1 = true;
			
		}
	};
	
	@Override
    protected void onResume() {
		WindowManager.LayoutParams lp1 = getWindow().getAttributes();
		lp1.alpha=1f;
		lp1.dimAmount=1f;
		getWindow().setAttributes(lp1);
		
		activity_state = "not Exit";
		Log.i("DaYe", "M_resume");
        super.onResume();
        
        
	}
	
	@Override
    protected void onPause() {
		//lightDialog.dismiss();
		activity_state = "Exit";
		Log.i("DaYe", "M_pause");
		Log.i("DaYe", MenuActivity.activity_state);
        super.onPause();
       
    }
	
    @Override
    protected void onStop() {
    	//Log.i("DaYe", "M_stop");
        super.onStop();
    }
    
    /**
     * button listener of train
     * start the TrainActivity
     * @param v
     */
    public void train(View v){
    	//soundPlay_congradulate();
    	Intent intent = new Intent(MenuActivity.this,TrainActivity.class);
    	startActivity(intent);
    }
    
    /**
     * button listener of race
     * start the light dialog : the race-begin indicator light [4s]
     * start the RaceActivity
     * @param v
     */
    
    public void race(View v){
    	
    	lightDialog = new LightDialog(this);
    	
    	lightDialog.show();
    	
    	byte[] bb = {(byte) 0xa6};
		/*mIntent.putExtra(BLEconfig.EXTRA_COMMAND, BLEconfig.COMMAND_WRITE_CHARACTERISTIC);
		mIntent.putExtra(BLEconfig.WRITE_VALUE, bb);
		startService(mIntent);*/
		
		Tools.mBleService.characteristicWrite.setValue(bb);
        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characteristicWrite);
    	
    	//WindowManager windowManager = getWindowManager();
    	//Display display = windowManager.getDefaultDisplay();
    	WindowManager.LayoutParams lp = lightDialog.getWindow().getAttributes();
    	
    	lp.height = screenHeight;
    	lp.width = screenWidth;
    	lightDialog.getWindow().setAttributes(lp);
    	
    	WindowManager.LayoutParams lp1 = getWindow().getAttributes();
		lp1.alpha=0f;
		lp1.dimAmount=0f;
		getWindow().setAttributes(lp1);
    	
    	new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MenuActivity.this,RaceActivity.class);
		    	startActivity(intent);
		    	//淇creat RaceActivity鏃剁殑鏃堕棿鍋忓樊涓�0姣
		    	new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
				    	lightDialog.dismiss();
				    	
					}
				}, 16);
		    	
			}
		}, 5030);
    	
    }
    
   
	/**
     * button listener of setting ,the number of laps setting
     * @param v
     */
    public void setting(View v){
    	
    	Intent intent = new Intent(MenuActivity.this,MySettingActivity.class);
    	startActivity(intent);
    
    }
    public void guide(View v){
    	
    	Intent intent = new Intent(MenuActivity.this,MyGuideActivity.class);
    	startActivity(intent);
    
    }
    
    /**
     * button listener of back
     * @param v
     */
    public void activity_back(View v){
    	BLEconfig.getInstance().close();
		MenuActivity.this.finish();
    }
    private void initPosi() {
		// TODO Auto-generated method stub
		
		//dm = new DisplayMetrics();
		//getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		screenHeight = AppContext.screenHeight;
		screenWidth = AppContext.screenWidth;
		
		//scaledDensity = dm.scaledDensity;
		float per = (float)screenWidth/(float)screenHeight;
		Log.i("diedie", "yes!!!"+per);
		if (per > 0.7) {//说明是pad的比例关系
			AppContext.device = "pad";
		}else{
			AppContext.device = "phone";
		}
		
		//setting_btn
		settingBtn = (ImageButton) findViewById(R.id.menu_settingBtn);
		FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams((int) (screenWidth*0.13), (int) (screenWidth*0.16));
		params1.setMargins((screenWidth*1/20), screenHeight*1/20, 0, 0);
		settingBtn.setLayoutParams(params1);
		settingBtn.setOnClickListener(this);
		
		//guidingBtn
		guidingBtn = (ImageButton) findViewById(R.id.menu_guidingBtn);
		FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams((int) (screenWidth*0.13), (int) (screenWidth*0.16));
		params2.setMargins((int) (screenWidth*0.2), (int) (screenHeight*0.05), 0, 0);
		guidingBtn.setLayoutParams(params2);
		guidingBtn.setOnClickListener(this);
		
		
		
		//athensBtn
		athensBtn = (ImageButton) findViewById(R.id.athensBtn);
		setPosi(athensBtn,R.id.athensBtn,(float)7.2,(float)2.1);
			
		//rioBtn
		rioBtn = (ImageButton) findViewById(R.id.rioBtn);
		setPosi(rioBtn,R.id.rioBtn,(float)12.2,(float)1.6);
		
		//new_yorkBtn
		new_yorkBtn = (ImageButton) findViewById(R.id.new_yorkBtn);
		setPosi(new_yorkBtn,R.id.new_yorkBtn,(float)2.3,(float)4.5);
			
		//londonBtn
		londonBtn = (ImageButton) findViewById(R.id.londonBtn);
		setPosi(new_yorkBtn,R.id.londonBtn,(float)15.3,(float)3.5);
		
		//beijing
		beijing = (ImageButton) findViewById(R.id.beijing);
		setPosi(beijing,R.id.beijing, (float)(15.2), (float)(8.8));
		
		//paris
		paris = (ImageButton) findViewById(R.id.paris);
		setPosi(paris,R.id.paris,(float)9.5,(float)5);
			
		//oslo
		oslo = (ImageButton) findViewById(R.id.oslo);
		setPosi(oslo,R.id.oslo,(float)13.3,(float)6.5);
		
		//prague
		prague = (ImageButton) findViewById(R.id.prague);
		setPosi(prague,R.id.prague, (float)(6), (float)(6.9));
		
		//copenhague
		copenhague = (ImageButton) findViewById(R.id.copenhague);
		setPosi(copenhague,R.id.copenhague,(float)1.3,(float)8);
			
		//rome
		rome = (ImageButton) findViewById(R.id.rome);
		setPosi(rome,R.id.rome,(float)5.8,(float)10);
		
		//stockholm
		stockholm = (ImageButton) findViewById(R.id.stockholm);
		setPosi(stockholm,R.id.stockholm, (float)(9.7), (float)(9));
		
		//berlin
		berlin = (ImageButton) findViewById(R.id.berlin);
		setPosi(berlin,R.id.berlin, (float)(12.3), (float)(14.2));		
		 
		//tokyo
		tokyo = (ImageButton) findViewById(R.id.tokyo);
		setPosi(tokyo,R.id.tokyo,(float)15.2,(float)12.4);
		
		//mexico
		mexico = (ImageButton) findViewById(R.id.mexico);
		setPosi(mexico,R.id.mexico, (float)(7.1), (float)(13.3));
		
		//sydney
		sydney = (ImageButton) findViewById(R.id.sydney);
		setPosi(sydney,R.id.sydney,(float)(1.3), (float)(11.3));
			
		//toronto
		toronto = (ImageButton) findViewById(R.id.toronto);
		setPosi(toronto,R.id.toronto,(float)12.1,(float)11);
		
		//saint_peter
		saint_peter = (ImageButton) findViewById(R.id.saint_peter);
		setPosi(saint_peter,R.id.saint_peter, (float)(1.8), (float)(15));		
		
		//custom
		custom = (ImageButton) findViewById(R.id.custom);
		FrameLayout.LayoutParams paramsX = new FrameLayout.LayoutParams((int)(screenHeight*0.20), (int) (screenHeight*0.12));
		paramsX.setMargins((screenWidth-(int)(screenHeight*0.2))/2, (int)(screenHeight*16.3/20), 0, 0);
		custom.setLayoutParams(paramsX);	
		custom.setOnClickListener(this);
		
		if(!AppContext.lanuage){
			
			settingBtn.setBackgroundResource(R.drawable.settinge);
			guidingBtn.setBackgroundResource(R.drawable.guidinge);
			
			custom.setBackgroundResource(R.drawable.custom_e);
		}
		
		//set button_guide
		//setPosi(guideButton,R.id.menu_guide_btn,12);
		/*guideButton = (ImageButton) findViewById(R.id.menu_guide_btn);
		FrameLayout.LayoutParams params6 = new FrameLayout.LayoutParams(screenHeight*1/10*3, screenHeight*1/10);
		params6.setMargins((screenWidth-screenHeight*1/10*3)/2, screenHeight*12/20, 0, 0);
		guideButton.setLayoutParams(params6);
		if(!AppContext.lanuage){

			FrameLayout.LayoutParams params66 = new FrameLayout.LayoutParams(screenHeight*1/10*3, screenHeight*1/7);
			params66.setMargins((screenWidth-screenHeight*1/10*3)/2, screenHeight*12/20, 0, 0);
			guideButton.setLayoutParams(params66);
			
			guideButton.setBackgroundResource(R.drawable.menu_guideing2);
		}*/
		
	}
    public void showCityDialog(int index){
    	
    	AppContext.index = index;
    	
    	Intent intent = new Intent(MenuActivity.this,MyCityActivity.class);
    	startActivity(intent);
    	/*MyCityDialog mtCityDialog = new MyCityDialog(this);
    	mtCityDialog.index = index;
    	WindowManager.LayoutParams wl = mtCityDialog.getWindow().getAttributes();
    	
    	Log.i("info", "screenWidth:"+screenWidth+" screenHeight:"+screenHeight);
    	
    	wl.width = screenWidth;
    	wl.height = screenHeight;
    	mtCityDialog.getWindow().setAttributes(wl);
    	
    	mtCityDialog.show();*/
    }
    public void setPosi(ImageButton imageButton,int id,float left,float top) {
    	imageButton = (ImageButton) findViewById(id);
		FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams((int) (screenWidth*0.17), (int) (screenWidth*0.186));
		params3.setMargins((int) (screenWidth*left/20), (int) (screenHeight*top/20), 0, 0);
		imageButton.setLayoutParams(params3);
		
		imageButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_settingBtn:
			Intent intent2 = new Intent(MenuActivity.this,MySettingActivity.class);
			startActivity(intent2);
			break;
		case R.id.menu_guidingBtn:
			Intent intent3 = new Intent(MenuActivity.this,MyGuideActivity.class);
			startActivity(intent3);
			break;
		case R.id.athensBtn:
			showCityDialog(R.id.athensBtn);
			break;
		case R.id.rioBtn:
			showCityDialog(R.id.rioBtn);
			break;
		case R.id.new_yorkBtn:
			showCityDialog(R.id.new_yorkBtn);
			break;
		case R.id.londonBtn:
			showCityDialog(R.id.londonBtn);
			break;
		case R.id.beijing:
			showCityDialog(R.id.beijing);
			break;
		case R.id.paris:
			showCityDialog(R.id.paris);
			break;
		case R.id.oslo:
			showCityDialog(R.id.oslo);
			break;
		case R.id.prague:
			showCityDialog(R.id.prague);
			break;
		case R.id.copenhague:
			showCityDialog(R.id.copenhague);
			break;
		case R.id.rome:
			showCityDialog(R.id.rome);
			break;
		case R.id.stockholm:
			showCityDialog(R.id.stockholm);
			break;
		case R.id.berlin:
			showCityDialog(R.id.berlin);
			break;
		case R.id.tokyo:
			showCityDialog(R.id.tokyo);
			break;
		case R.id.mexico:
			showCityDialog(R.id.mexico);
			break;
		case R.id.sydney:
			showCityDialog(R.id.sydney);
			break;
		case R.id.toronto:
			showCityDialog(R.id.toronto);
			break;
		case R.id.saint_peter:
			showCityDialog(R.id.saint_peter);
			break;
		case R.id.custom:
			Log.i("info", "R.id.custom:");
			Intent intent = new Intent(MenuActivity.this,MyCustomActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		
		
	}
 

}