package com.ui;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluetoothARC.R;
import com.myview.PanelViewA;
import com.myview.PanelViewB;
import com.connect.BLEconfig;
import com.connect.Tools;
//import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.sdk.modelmsg.WXImageObject;
//import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tools.ScreenShotUtils;
//import com.myview.PanelView1;
//import com.myview.PanelView2;
//import com.myview.TrainPanelModle1;
//import com.myview.TrainPanelModle2;
import com.ui.TrainActivity.BluetoothReceiver;


/**
 * This activity is the race gaming view
 * it's mainly consist of two view,corresponding two toy-cars
 * more details of the two view's layout is in the activity_race.xml
 * 
 * @author likun@stu.zzu.edu.cn
 *
 */
public class RaceActivity extends Activity
{
	//public static String isVoice = "be voice";
	private DisplayMetrics dm;
	
	public static Context theContext;
	public int screenWidth,screenHeight;
	public float scaledDensity;
	
	int remainder1=0,remainder2=0,remainder=0;
	int inte=0;
	int add1=0,add2=0;
	int line1Count,line2Count;
	
	private Timer timer1,timer2;
	private Boolean timer1RunningStatus = false;
	private Boolean timer2RunningStatus = false;
	
	public static String line1Status;
	public static String line2Status;
	
	public  String activity_state = "not Exit";
	
	public static long line1TotleTime,line2TotleTime;
	public static long line1FastTime,line2FastTime;
	
	//RaceResult rr = new RaceResult();
	
	public static String resultFlag = "line1 win";
	
	/**
	 * the begin time means the race's begin time,now the begin time 
	 * is the activity's creation time
	 */
	Date beginTime;
	Date currentTime;
	
	long beginDegree_first = -90;
	long beginDegree_second = -90;
	
	public static int laps,oil_consum;
	public static int lapsOne;
	public static int lapsTwo;
	//information of line one
	long fastestTimeOne = 9999;
	long timeStampOne = -1;
	
	
	//information of line two
	long fastestTimeTwo = 9999;
	long timeStampTwo = -1;
	
	
	//position of side pointer
	int leftPointerFirstDegree = 30;
	int leftPointerSecondDegree = 30;
	int rightPointerFirstDegree = -30;
	int rightPointerSecondDegree = -30;
	
	boolean isOver = false;
	//BLEconfig mBLE;
	//Handler msgHandler;
	//RaceMsgHandler msgHandler;
	
	private ImageButton redFace,blueFace;
	
	private FrameLayout layoutOne,layoutTwo;
	private boolean TAG_firstLayoutTop;
	
	private final static int MSG_DATA = 1;
	
	PanelViewA panelViewA;
	PanelViewB panelViewB;
	
	RelativeLayout logoRelativeLayout;
	//TrainPanelModle1 theTrainPanelModle1;
	//TrainPanelModle2 theTrainPanelModle2;
	private int AVoiceReturn = 0,BVoiceReturn = 0; 
	private String AVoiceSwitch = "on",BVoiceSwitch = "on",
			ADamagedVoiceSwitch = "on",BDamagedVoiceSwitch = "on";
	
	private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
	public static final String APP_ID = "wxcdbf85625fc7cc5f";
	//public static IWXAPI api;
	

	public static Window theWindow11;
	
	
	 /**
     * the data available listener 
     * handle data after received
     */
	private BLEconfig.OnDataAvailableListener mOnDataAvailable = new BLEconfig.OnDataAvailableListener(){
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			//Log.i("TAG", "change!-read");
		}
		
		@SuppressLint("NewApi") @Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			/*String s = new String(characteristic.getValue());
			
			//translate data to main thread
			Message msg=new Message();
			Bundle b = new Bundle();
			b.putString("data", s);
			msg.setData(b);
			msg.what = MSG_DATA;
			if (activity_state == "not Exit") {
				RaceActivity.this.msgHandler.sendMessage(msg);
			}*/
			
		}
		
	};
	ImageButton race_back,race_Bluetooth;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		System.gc();
		dm = new DisplayMetrics(); 
        getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		screenHeight = AppContext.screenHeight;
		screenWidth = AppContext.screenWidth;
		//screenWidth = (int) (screenHeight*9.4/16);
		scaledDensity = AppContext.scaledDensity;
	
		//no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//full_screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		//hide navigation(导航栏)
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION); 
        //keep_screen_on
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		setContentView(R.layout.activity_race);
		
		laps = AppContext.laps;
		oil_consum = AppContext.oil_consum;
		//被外部类使用
		theContext = this;
		
		lapsOne = -1;
		lapsTwo = -1;
		
		TAG_firstLayoutTop = true;
		
		race_Bluetooth = (ImageButton) findViewById(R.id.race_bluetooth_blue);
		
		race_back = (ImageButton) findViewById(R.id.race_back);
		race_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				activity_raceback();
			}
		});
		//theTrainPanelModle1 = new TrainPanelModle1();
		//initialize layoutOne
		//panelViewA = new PanelView(this,theTrainPanelModle1);
		panelViewA = PanelViewA.getInstance(this);
		//panelViewA.setBackgroundResource(R.drawable.line_one_back);
		panelViewA.setId(113);
		FrameLayout.LayoutParams LayoutParams1 = new FrameLayout.LayoutParams(screenWidth,(int)(screenWidth*0.605));
        layoutOne = (FrameLayout)findViewById(R.id.RaceLayoutOne);
        layoutOne.removeAllViews();
        layoutOne.addView(panelViewA,LayoutParams1);
        
        redFace = new ImageButton(this);
		redFace.setBackgroundResource(R.drawable.redface);
		FrameLayout.LayoutParams LayoutParamsMM = new FrameLayout.LayoutParams((int)(screenWidth*0.13),(int)(screenWidth*0.13));
		LayoutParamsMM.setMargins((int)(screenWidth*0.85), (int)(screenWidth*0.01), 0, 0);
		layoutOne.addView(redFace,LayoutParamsMM);
		
		TextView useName1 = new TextView(this);
		FrameLayout.LayoutParams LayoutParamsTT = new FrameLayout.LayoutParams((int)(screenWidth*0.13),(int)(screenWidth*0.04));
		LayoutParamsTT.setMargins((int)(screenWidth*0.847), (int)(screenWidth*0.14), 0, 0);
		layoutOne.addView(useName1,LayoutParamsTT);
		useName1.setGravity(Gravity.CENTER);
		useName1.setTextColor(Color.WHITE);
		useName1.setText(AppContext.useName1);
		useName1.setTextSize((float) (17*AppContext.inch/7.9));	
		useName1.setSingleLine(true);
        
        panelViewA.theModel.Line = "1";
        panelViewA.theModel.rank = "-";
      
        //theTrainPanelModle2 = new TrainPanelModle2();
        //initialize layoutTwo
        //panelViewB = new PanelView(this,theTrainPanelModle2);
        panelViewB = PanelViewB.getInstance(this);
        //panelViewB.setBackgroundResource(R.drawable.line_two_back);
        panelViewB.setId(114);
        FrameLayout.LayoutParams LayoutParams2 = new FrameLayout.LayoutParams(screenWidth,(int)(screenWidth*0.605));
        layoutTwo = (FrameLayout)findViewById(R.id.RaceLayoutTwo);
        layoutTwo.removeAllViews();
        layoutTwo.addView(panelViewB,LayoutParams2);
        
        blueFace = new ImageButton(this);
		blueFace.setBackgroundResource(R.drawable.blueface);
		FrameLayout.LayoutParams LayoutParamsNN = new FrameLayout.LayoutParams((int)(screenWidth*0.13),(int)(screenWidth*0.13));
		LayoutParamsNN.setMargins((int)(screenWidth*0.85), (int)(screenWidth*0.01), 0, 0);
		layoutTwo.addView(blueFace,LayoutParamsNN);
		
		TextView useName2 = new TextView(this);
		FrameLayout.LayoutParams LayoutParamsPP = new FrameLayout.LayoutParams((int)(screenWidth*0.13),(int)(screenWidth*0.04));
		LayoutParamsPP.setMargins((int)(screenWidth*0.847), (int)(screenWidth*0.14), 0, 0);
		layoutTwo.addView(useName2,LayoutParamsPP);
		useName2.setGravity(Gravity.CENTER);
		useName2.setTextColor(Color.WHITE);
		useName2.setText(AppContext.useName2);
		useName2.setTextSize((float) (17*AppContext.inch/7.9));	//17
		useName2.setSingleLine(true);
		
		if (useName1.getText()=="Please Enter" || useName1.getText() == "请输入用户名") {
			useName1.setText("");
		}
		if (useName2.getText()=="Please Enter" || useName1.getText() == "请输入用户名") {
			useName2.setText("");
		}
        panelViewB.theModel.Line = "2";
        panelViewB.theModel.rank = "-";
        
        TextView theTextView = new TextView(this);
       
		RelativeLayout.LayoutParams firstLayoutParamsX = new RelativeLayout.LayoutParams((int)(screenWidth/2.5),screenWidth/10);
		logoRelativeLayout = (RelativeLayout) findViewById(R.id.centrallogo_race);
		logoRelativeLayout.addView(theTextView,firstLayoutParamsX);
        
        if (timer1 != null) {
			timer1.cancel();
		}
		if (timer2 != null) {
			timer2.cancel();
		}
		panelViewA.alp2 = 255;
		panelViewB.alp2 = 255;
        
		setBroadcastReveiver();
        //BlueTooth configure module
		//mBLE = BLEconfig.getInstance();
		
		///lllllllllllllllllllllllllllllllllllll
		//mBLE.setOnDataAvailableListener(mOnDataAvailable);
		
		//msgHandler = new RaceMsgHandler(this);
		
		//initialize TIME when create this activity
		beginTime = new Date();
		Log.i("null", "2");
		
		mIntent = new Intent(RaceActivity.this,BLEconfig.class);
		
		byte[] cc = {(byte) 0x5a};
		/*mIntent.putExtra(BLEconfig.EXTRA_COMMAND, BLEconfig.COMMAND_WRITE_CHARACTERISTIC);
		mIntent.putExtra(BLEconfig.WRITE_VALUE, cc);
		startService(mIntent);*/
		
		Tools.mBleService.characteristicWrite.setValue(cc);
        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characteristicWrite);
		
		isOver = false;
		
		//api = WXAPIFactory.createWXAPI(this, APP_ID,true);
		//将APP_ID注册到微信中
		//api.registerApp(APP_ID);
		
		calculate();
		
		if (AppContext.device == "phone"){
			
			RelativeLayout.LayoutParams paramsjh = new RelativeLayout.LayoutParams((int) (screenWidth*0.07),(int)(screenWidth*0.1));
			paramsjh.setMargins((int)(screenWidth*0.00), (int) (screenHeight*0.00), 0, 0);
			race_Bluetooth.setLayoutParams(paramsjh);
			
			RelativeLayout.LayoutParams paramsj = new RelativeLayout.LayoutParams((int) (screenWidth*0.1),(int)(screenWidth*0.1));
			paramsj.setMargins((int)(screenWidth*0.85), (int) (screenHeight*0.00), 0, 0);
			race_back.setLayoutParams(paramsj);
			if(AppContext.lanuage){//zh
				findViewById(R.id.race_background).setBackgroundResource(R.drawable.background_race_p);
				
				race_back.setBackgroundResource(R.drawable.back_btn);
				theTextView.setBackgroundResource(R.drawable.logo);
			}else {
				findViewById(R.id.race_background).setBackgroundResource(R.drawable.background_race_pe);
				
				race_back.setBackgroundResource(R.drawable.back_btne);
				theTextView.setBackgroundResource(R.drawable.logo2);
			}
			}else if(AppContext.device == "pad"){
				
				RelativeLayout.LayoutParams paramsjh = new RelativeLayout.LayoutParams((int) (screenWidth*0.07),(int)(screenWidth*0.1));
				paramsjh.setMargins((int)(screenWidth*0.00), (int) (screenHeight*0.00), 0, 0);
				race_Bluetooth.setLayoutParams(paramsjh);
				
				RelativeLayout.LayoutParams paramsj = new RelativeLayout.LayoutParams((int) (screenWidth*0.1),(int)(screenWidth*0.1));
				paramsj.setMargins((int)(screenWidth*0.85), (int) (screenHeight*0.00), 0, 0);
				race_back.setLayoutParams(paramsj);
				if(AppContext.lanuage){//zh
					findViewById(R.id.race_background).setBackgroundResource(R.drawable.background_race);
					
					race_back.setBackgroundResource(R.drawable.back_btn);
					theTextView.setBackgroundResource(R.drawable.logo);
				}else {
					findViewById(R.id.race_background).setBackgroundResource(R.drawable.background_race_e);
					
					race_back.setBackgroundResource(R.drawable.back_btne);
					theTextView.setBackgroundResource(R.drawable.logo2);
				}
			}
		//race_text = (TextView) findViewById(R.id.race_text);
		
		
		panelViewA.lapValuePosition = 0.148;
		panelViewB.lapValuePosition = 0.148;
		
		line1StateJudge();
		line2StateJudge();
		
	}
	  private BluetoothReceiver bluetoothReceiver = null;
	    public class BluetoothReceiver extends BroadcastReceiver {
	        @Override
	        public void onReceive(Context arg0, Intent intent) {
	            // TODO Auto-generated method stub
	            String action = intent.getAction();

	            if(BLEconfig.ACTION_CHARACTER_CHANGE.equals(action)){
	            	if (!(activity_state == "not Exit"))return;
	            	int data = intent.getIntExtra("value", -2);
	            	switch (data) {
	            	case 11:
						msgPosi_11(0,0,0,0);
						break;
	            	case 12:
						msgPosi_12();
						break;
	            	case 21:
						msgPosi_21(0,0,0,0);
						break;
					case 22:
						msgPosi_22();
						break;
					}
	            }
	            else if(BLEconfig.ACTION_CHARACTER_CHANGE2.equals(action)){
	            	if (!(activity_state == "not Exit"))return;
	            	String data = intent.getStringExtra("value");
	            	msgAccChange(data);
	            }else if(BLEconfig.ACTION_STATE_CONNECTED.equals(action)){

	            }else if(BLEconfig.ACTION_STATE_DISCONNECTED.equals(action)){
	                //myHandler.sendEmptyMessage(11);
	            }else if (BLEconfig.ACTION_WRITE_DESCRIPTOR_OVER.equals(action)) {
	            }
	        }
	    }
	 //设置广播接收器
  private void setBroadcastReveiver() {
      IntentFilter intentFilter = new IntentFilter();
      intentFilter.addAction(BLEconfig.ACTION_STATE_CONNECTED);
      intentFilter.addAction(BLEconfig.ACTION_STATE_DISCONNECTED);
      intentFilter.addAction(BLEconfig.ACTION_WRITE_DESCRIPTOR_OVER);
      intentFilter.addAction(BLEconfig.ACTION_CHARACTER_CHANGE);
      intentFilter.addAction(BLEconfig.ACTION_CHARACTER_CHANGE2);
      
      bluetoothReceiver = new BluetoothReceiver();
      registerReceiver(bluetoothReceiver, intentFilter);
  }
	private void resetDataAll(){
		panelViewA.theModel.leftPointerDegree = 30;
		panelViewA.leftDegree= 30;
		panelViewA.theModel.rightPointerDegree = -30;
		panelViewA.rightDegree= -30;
		
		panelViewA.oil_4_pleaseState = false;
		panelViewA.oil_2_pleaseState = false;
		
		panelViewB.oil_4_pleaseState = false;
		panelViewB.oil_2_pleaseState = false;
		
		line1StateJudge();
		Log.i("niangde", "state"+panelViewA.theModel.lineXStatus);	
		panelViewA.theModel.lineXIsDamaged = "not damaged";
		
		//panelViewA.perDegree = 9;
		remainder1 = 4;
		
		byte[] cc = {(byte) 0x5a};
		/*mIntent.putExtra(BLEconfig.EXTRA_COMMAND, BLEconfig.COMMAND_WRITE_CHARACTERISTIC);
		mIntent.putExtra(BLEconfig.WRITE_VALUE, cc);
		startService(mIntent);*/
		
		Tools.mBleService.characteristicWrite.setValue(cc);
        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characteristicWrite);
        
		line1Count = 8;
		
		panelViewA.theModel.lapValue = "0";
		panelViewA.theModel.fastTime = "00:00:000";
		panelViewA.theModel.totleTime = "00:00:000";
		
		
		
		panelViewB.theModel.leftPointerDegree = 30;
		panelViewB.leftDegree= 30;
		panelViewB.theModel.rightPointerDegree = -30;
		panelViewB.rightDegree= -30;
		
		line2StateJudge();
		//Log.i("niangde", "state"+panelViewB.theModel.lineXStatus);	
		panelViewB.theModel.lineXIsDamaged = "not damaged";
		
		//panelViewA.perDegree = 9;
		remainder2 = 4;
		
		byte[] dd = {(byte) 0x5a};
		/*mIntent.putExtra(BLEconfig.EXTRA_COMMAND, BLEconfig.COMMAND_WRITE_CHARACTERISTIC);
		mIntent.putExtra(BLEconfig.WRITE_VALUE, dd);
		startService(mIntent);*/
		
		Tools.mBleService.characteristicWrite.setValue(dd);
        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characteristicWrite);
        
		line2Count = 8;
		
		panelViewB.theModel.lapValue = "0";
		panelViewB.theModel.fastTime = "00:00:000";
		panelViewB.theModel.totleTime = "00:00:000";
		
		panelViewA.alp1 = 0;
		panelViewB.alp1 = 0;
		panelViewA.alp2 = 0;
		panelViewB.alp2 = 0;
		
		panelViewA.destroyDrawingCache();
		panelViewB.destroyDrawingCache();
		
		
		
		panelViewA = null;
		panelViewB = null;
		
		layoutTwo.removeAllViews();
		layoutOne.removeAllViews();
	}
	private void calculate() {
		line1Count = oil_consum;
		line2Count = oil_consum;
		remainder = 60 % oil_consum;
		remainder1 = remainder;
		remainder2 = remainder;
		Log.i("niangde", "remainder1.."+remainder1);
		inte = 60 / oil_consum;
	}
	
	private void over() {
		
		
		//post "all over" command
		isOver = true;
		byte[] bb = {(byte) 0xa6};
		/*mIntent.putExtra(BLEconfig.EXTRA_COMMAND, BLEconfig.COMMAND_WRITE_CHARACTERISTIC);
		mIntent.putExtra(BLEconfig.WRITE_VALUE, bb);
		startService(mIntent);*/
		
		Tools.mBleService.characteristicWrite.setValue(bb);
        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characteristicWrite);
        
		
		if (timer1 != null) {
			timer1.cancel();
		}
		if (timer2 != null) {
			timer2.cancel();
		}
		
		
			/*if(MenuActivity.isVoice == "be voice")
			MenuActivity.soundPlay_congradulate();*/
		
			//if all is damaged
			if (lapsOne < laps && lapsTwo < laps) {
				//line1 run more laps
				if (lapsOne > lapsTwo) {
					//line1 win
					resultFlag = "line1 win and all are tui";
				}else if(lapsOne < lapsTwo){
					resultFlag = "line2 win and all are tui";
				}
				//while laps is same,compare the time
				else if (line1TotleTime <= line2TotleTime){
						resultFlag = "line1 win and all are tui";
					}else{
						resultFlag = "line2 win and all are tui";
					}
				
				
			}
			//All is over normally,little time for win
			if (lapsOne == laps && lapsTwo == laps) {
				if(line1TotleTime <= line2TotleTime){
					resultFlag = "line1 win nobody is tui";
				}else{
					resultFlag = "line2 win nobody is tui";
				}
			}
			
			//line2 over ;line1 damaged
			if (lapsOne < laps && lapsTwo == laps) {
				resultFlag = "line2 win and line1 is tui";
			}
			//line1 over ;line2 damaged
			if (lapsOne == laps && lapsTwo < laps) {
				resultFlag = "line1 win and line2 is tui";
			}
			//showDialog
			showResultDialog();
			
		}
	
	
	@Override
    protected void onResume() {
        super.onResume();
        
        activity_state = "not Exit";
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		
		if(isOver){
        byte[] bb = {(byte) 0xa6};
		/*mIntent.putExtra(BLEconfig.EXTRA_COMMAND, BLEconfig.COMMAND_WRITE_CHARACTERISTIC);
		mIntent.putExtra(BLEconfig.WRITE_VALUE, bb);
		startService(mIntent);*/
		
		Tools.mBleService.characteristicWrite.setValue(bb);
        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characteristicWrite);
        
		}
		super.onRestart();
	}

	@Override
    protected void onPause() {
		 Log.i("timer", "pause");
		 super.onPause();    
		 Log.i("address", "onPause...... ");
		 byte[] bb = {(byte) 0x5a};
			/*mIntent.putExtra(BLEconfig.EXTRA_COMMAND, BLEconfig.COMMAND_WRITE_CHARACTERISTIC);
			mIntent.putExtra(BLEconfig.WRITE_VALUE, bb);
			startService(mIntent);*/
			
			Tools.mBleService.characteristicWrite.setValue(bb);
	        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characteristicWrite);
	        
		 activity_state = "Exit";
       
    }
	@Override
    protected void onStop() {
		Log.i("timer", "stop");
        super.onStop();
        
        //mBLE.close();
    }
	//resetData
	private void resetData() {

		line1IsDamaged = "not damaged";
		line1IsNormal_Over = "not normal";
		
		line2IsDamaged = "not damaged";
		line2IsNormal_Over = "not normal";
		
		if (timer1 != null) {
			timer1.cancel();
		}
		if (timer2 != null) {
			timer2.cancel();
		}
	}
   
	
	
   
	
	/**
	 * convert data format
	 * @param bytes
	 * @return
	 */
	public String formatConverse(byte[] bytes)
	{
		String result = "";
		for(int i=0;i<bytes.length;i++)
		{
			System.out.println(bytes[i]);
		}
		if(6 == bytes.length || 4 == bytes.length){ //original is 6 ,real is 4
			result = "AA"+Byte.toString(bytes[3]);
		}
		else if(5 == bytes.length || 3 == bytes.length){ // original is 5 , real is 3
			result = Byte.toString(bytes[1]) + " "+Byte.toString(bytes[2]);
		}
		return result;
	}
	
	/**
	 * processing the time format
	 * for display the time on the view
	 * @param time
	 * @return
	 */
	private String timeFormat(long time)
	{
		long min =0;
		long sec =0;
		long milli =0;
		
		if(time<1000){
			milli = time;
		}
		else if(time<60*1000){
			sec=time/1000;
			milli=time/60%1000;
		}
		else if(time<60*60*1000)
		{
			min=time/(1000*60);
			sec=time/1000%60;
			milli=time/3600%1000;
		}
		String x = String.valueOf(min),y = String.valueOf(sec),z = String.valueOf(milli);
		
		for (int i = 0; i <(3-z.length()); i++) {
			z = "0"+z;
		}
		for (int i = 0; i <(2-y.length()); i++) {
			y = "0"+y;
		}
		for (int i = 0; i <(2-x.length()); i++) {
			x = "0"+x;
		}
		
		return x+":"+y+":"+z;
	}
	
	/**
	 * exchange the two views' position with an animation
	 */
	private void exchange()
	{
		//Log.i("yamaha","Left锛�+layoutOne.getLeft()+"Right锛�+layoutOne.getRight()+"Top锛�+layoutOne.getTop()+"Bottom锛�+layoutOne.getBottom());  
		//Log.i("yamaha","Left锛�+layoutTwo.getLeft()+"Right锛�+layoutTwo.getRight()+"Top锛�+layoutTwo.getTop()+"Bottom锛�+layoutTwo.getBottom());
		if(TAG_firstLayoutTop){
			//voice?
			if(MenuActivity.isVoice){
				BVoiceSwitch = "off";
				if(ADamagedVoiceSwitch == "on")
				WelcomeActivity.b_lead();
			}
			//move upward and downward 300
			ObjectAnimator.ofFloat(layoutTwo, "TranslationY", -(layoutTwo.getTop()-layoutOne.getTop())).setDuration(1300).start();
			ObjectAnimator.ofFloat(layoutOne, "TranslationY", (layoutTwo.getTop()-layoutOne.getTop())).setDuration(1300).start();
			
			panelViewA.theModel.rank = "2";
			panelViewB.theModel.rank = "1";
			
			
			TAG_firstLayoutTop = false;
		}else{
			if(MenuActivity.isVoice){
				AVoiceSwitch = "off";
				if(BDamagedVoiceSwitch == "on")
				WelcomeActivity.a_lead();
			}
			//back to normal position
			ObjectAnimator.ofFloat(layoutOne, "TranslationY", 0).setDuration(1300).start();
			ObjectAnimator.ofFloat(layoutTwo, "TranslationY", 0).setDuration(1300).start();
			
			panelViewA.theModel.rank = "1";
			panelViewB.theModel.rank = "2";
			
			TAG_firstLayoutTop = true;
		}
	}
	
	
	int currentValueFirst = 0;
	int currentDegreeFirst = -90;
	int currentValueSecond = 0;
	int currentDegreeSecond = -90;
	
	/**
	 * change pointer's degree
	 * @param accValue is the data received
	 * @param line is line's no 
	 */

	private void changePointer(String accValue,int line){
		
		
		if(line == 1){
			// for the line 1
			//Log.i("TAG", "*line:" + line + " *target: " + accValue + " *current: " + currentValueFirst);
			int targetValue = Integer.valueOf(accValue);
			
			if(targetValue > currentValueFirst){
				
				//target is bigger than current value
				try {
					panelViewA.accPosition = targetValue;
					
					panelViewA.oilPointerState = "plusState";
					panelViewA.theModel.accPointerDegree = panelViewA.theModel.accPointerDegree+27;
					currentValueFirst = targetValue;
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}else if (targetValue < currentValueFirst){
				//target is lower than current value
			
				try {
					panelViewA.accPosition = targetValue;
					
					panelViewA.oilPointerState = "reduceState";
					panelViewA.theModel.accPointerDegree = panelViewA.theModel.accPointerDegree-27;
					currentValueFirst = targetValue;
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}else{
			// for the line 2
			Log.i("TAG", "*line:" + line + " *target: " + accValue + " *current: " + currentValueSecond);
			int targetValue = Integer.valueOf(accValue);
			
			if(targetValue > currentValueSecond){
				
				try {
					panelViewB.accPosition = targetValue;
					panelViewB.theModel.accPointerDegree = panelViewB.theModel.accPointerDegree+27;
					currentValueSecond = targetValue;
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
			}else if (targetValue < currentValueSecond){
				//target is lower than current value
				try {
					panelViewB.accPosition = targetValue;
					panelViewB.theModel.accPointerDegree = panelViewB.theModel.accPointerDegree-27;
					currentValueSecond = targetValue;
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}
	}
	
	private void changeSidePointer(int viewId){
		
		switch (viewId) {
			//the line1 side pointer
		case 1:
			panelViewA.perDegree = inte-1+add1;
			panelViewA.sidePointerState = "reduceState";
			
			panelViewA.theModel.leftPointerDegree = panelViewA.theModel.leftPointerDegree - (inte+add1);
			panelViewA.theModel.rightPointerDegree = panelViewA.theModel.rightPointerDegree + (inte+add1);
			break;
			//the line2 side pointer
		case 2:
			panelViewB.perDegree = inte-1+add2;
			panelViewB.sidePointerState = "reduceState";
			panelViewB.theModel.leftPointerDegree = panelViewB.theModel.leftPointerDegree - (inte+add2);
			panelViewB.theModel.rightPointerDegree = panelViewB.theModel.rightPointerDegree + (inte+add2);
			break;
		default:
			break;
		}
	}
	public static String line1IsDamaged = "not damaged";
	public static String line1IsNormal_Over = "not normal";
	public static String line2IsDamaged = "not damaged";
	public static String line2IsNormal_Over = "not normal";
	//judge line1 state
	private void line1StateJudge(){
		panelViewA.theModel.pointCount = line1Count;
		if (panelViewA.theModel.leftPointerDegree == 30) {
			panelViewA.theModel.lineXStatus = "no start";
		}else if(panelViewA.theModel.leftPointerDegree < 30){
			if(oil_consum >= 6){
				if(line1Count > 4){
					panelViewA.theModel.lineXStatus = "normal";
				}else if(line1Count == 4){
					panelViewA.theModel.lineXStatus = "oilless";
				}else if(line1Count == 2){
					panelViewA.theModel.lineXStatus = "please_refuel";
				}
			}else if(oil_consum < 6){
				if(line1Count > 2){
					panelViewA.theModel.lineXStatus = "normal";
				}else if(line1Count == 2){
					panelViewA.theModel.lineXStatus = "please_refuel";
				}
			}
		}
		if(panelViewA.theModel.leftPointerDegree == -30 && lapsOne != laps){
			panelViewA.theModel.lineXStatus = "damaged";
			//line2IsDamaged = "line2_damaged";
			panelViewA.theModel.lineXIsDamaged = "lineX_damaged";
		}
		if(lapsOne == laps){
			panelViewA.theModel.lineXStatus = "normal over";
			//line2IsNormal_Over = "line2_normal_over";
			panelViewA.theModel.lineXIsNormal_Over = "lineX_normal_over";
		}
		
		if(lapsOne == laps || (panelViewA.theModel.leftPointerDegree == -30 && lapsOne != laps)){
			panelViewA.theModel.lineXStatus = "lineX_gameover";
		}
		
		/*if (panelViewA.theModel.leftPointerDegree == 30) {
			panelViewA.theModel.lineXStatus = "no start";
		}else if(panelViewA.theModel.leftPointerDegree < 30 && panelViewA.theModel.leftPointerDegree > -5){
			panelViewA.theModel.lineXStatus = "normal";
		}else if(panelViewA.theModel.leftPointerDegree <= -5 && panelViewA.theModel.leftPointerDegree > -30){
			panelViewA.theModel.lineXStatus = "oilless";
		}else if(panelViewA.theModel.leftPointerDegree == -30 && lapsOne != laps){
			panelViewA.theModel.lineXStatus = "damaged";
			//line1IsDamaged = "line1_damaged";
			panelViewA.theModel.lineXIsDamaged = "lineX_damaged";
		}
		if(lapsOne == laps){
			panelViewA.theModel.lineXStatus = "normal over";
			//line1IsNormal_Over = "line1_normal_over";
			panelViewA.theModel.lineXIsNormal_Over = "lineX_normal_over";
		}
		
		if(lapsOne == laps || (panelViewA.theModel.leftPointerDegree == -30 && lapsOne != laps)){
			panelViewA.theModel.lineXStatus = "lineX_gameover";
		}*/
	}
	//judge line2 state
	private void line2StateJudge(){
		panelViewB.theModel.pointCount = line2Count;
		if (panelViewB.theModel.leftPointerDegree == 30) {
			panelViewB.theModel.lineXStatus = "no start";
		}else if(panelViewB.theModel.leftPointerDegree < 30){
			if(oil_consum >= 6){
				if(line2Count > 4){
					panelViewB.theModel.lineXStatus = "normal";
				}else if(line2Count == 4){
					panelViewB.theModel.lineXStatus = "oilless";
				}else if(line2Count == 2){
					panelViewB.theModel.lineXStatus = "please_refuel";
				}
			}else if(oil_consum < 6){
				if(line2Count > 2){
					panelViewB.theModel.lineXStatus = "normal";
				}else if(line2Count == 2){
					panelViewB.theModel.lineXStatus = "please_refuel";
				}
			}
		}
		if(panelViewB.theModel.leftPointerDegree == -30 && lapsTwo != laps){
			panelViewB.theModel.lineXStatus = "damaged";
			//line2IsDamaged = "line2_damaged";
			panelViewB.theModel.lineXIsDamaged = "lineX_damaged";
		}
		if(lapsTwo == laps){
			panelViewB.theModel.lineXStatus = "normal over";
			//line2IsNormal_Over = "line2_normal_over";
			panelViewB.theModel.lineXIsNormal_Over = "lineX_normal_over";
		}
		
		if(lapsTwo == laps || (panelViewB.theModel.leftPointerDegree == -30 && lapsTwo != laps)){
			panelViewB.theModel.lineXStatus = "lineX_gameover";
		}
		/*if (panelViewB.theModel.leftPointerDegree == 30) {
			panelViewB.theModel.lineXStatus = "no start";
		}else if(panelViewB.theModel.leftPointerDegree < 30 && panelViewB.theModel.leftPointerDegree > -5){
			panelViewB.theModel.lineXStatus = "normal";
		}else if(panelViewB.theModel.leftPointerDegree <= -5 && panelViewB.theModel.leftPointerDegree > -30){
			panelViewB.theModel.lineXStatus = "oilless";
		}else if(panelViewB.theModel.leftPointerDegree == -30 && lapsTwo != laps){
			panelViewB.theModel.lineXStatus = "damaged";
			//line2IsDamaged = "line2_damaged";
			panelViewB.theModel.lineXIsDamaged = "lineX_damaged";
		}
		if(lapsTwo == laps){
			panelViewB.theModel.lineXStatus = "normal over";
			//line2IsNormal_Over = "line2_normal_over";
			panelViewB.theModel.lineXIsNormal_Over = "lineX_normal_over";
		}
		
		if(lapsTwo == laps || (panelViewB.theModel.leftPointerDegree == -30 && lapsTwo != laps)){
			panelViewB.theModel.lineXStatus = "lineX_gameover";
		}*/
	}
	
	Intent mIntent;
	String theVoiceStateSwitchA = "null";
	public void msgPosi_11(long deltaT,long min,long sec,long milli){
		//share();
		
		line1StateJudge();
		
		if(!timer1RunningStatus && panelViewA.theModel.lineXStatus != "lineX_gameover" && panelViewA.theModel.lineXIsDamaged != "lineX_damaged"){
			theVoiceStateSwitchA = "on";
			//be voice?
			if(MenuActivity.isVoice){
				ScanActivity.soundPlay_overtake();
			}
			
			
		currentTime = new Date();
		//handle fastest
		if(timeStampOne != -1)
		{
			if(fastestTimeOne > currentTime.getTime() - timeStampOne)
				fastestTimeOne = currentTime.getTime() - timeStampOne ;
		
			panelViewA.theModel.fastTime = timeFormat(fastestTimeOne);
		}
		//handle time
		deltaT=currentTime.getTime() - beginTime.getTime();
		
		panelViewA.theModel.totleTime = timeFormat(deltaT);
		
		//handle laps number
		if(panelViewA.theModel.lineXStatus != "lineX_gameover"){
			Log.i("hello", "lineX_status is"+panelViewA.theModel.lineXStatus);
			lapsOne++;
			if (lapsOne == 0 && lapsTwo == -1) {
				if(MenuActivity.isVoice)
				WelcomeActivity.a_lead();
				panelViewA.theModel.rank = "1";
				panelViewB.theModel.rank = "2";
			}
			
		}
		Log.i("meimei", "lapsOne"+lapsOne + " "+laps);
		line1StateJudge();
		if(lapsOne <=laps){
			panelViewA.theModel.lapValue = lapsOne+"/"+laps;
			panelViewA.lapValuePosition = 0.120;
			}
		if(!TAG_firstLayoutTop && lapsOne>lapsTwo)
			exchange();
		
		if(timeStampOne!=-1 && panelViewA.theModel.leftPointerDegree > -30 && panelViewA.theModel.rightPointerDegree < 30){
			
			if(remainder1 > 0){
				add1 = 1;
				remainder1 -- ;
			}else{
				add1 = 0;
				remainder1 -- ;
			}
			changeSidePointer(1);
			line1Count --;
		}
		line1StateJudge();
		Log.i("hello", "line1_status is"+panelViewA.theModel.lineXStatus);
		timeStampOne = new Date().getTime();
		//Log.i("hehe", "lapOne="+lapsOne);
		//Log.i("hehe", "laps="+laps);
		if(lapsOne == laps){
			
			panelViewA.theModel.lineXStatus = "normal over";
		}
		if (AVoiceSwitch == "off") {
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					panelViewAVoiceLogic();
				}
			},800);
			AVoiceSwitch = "on";
		}else{
			panelViewAVoiceLogic();
		}
		
		
		Log.i("hello", "line1_status is"+panelViewA.theModel.lineXStatus);
		//end game with line one WIN
		if(panelViewA.theModel.lineXStatus == "damaged" || panelViewA.theModel.lineXStatus == "normal over" || panelViewA.theModel.lineXStatus == "lineX_gameover"){
			
			
			if(panelViewA.theModel.lineXStatus == "normal over" && MenuActivity.isVoice){
				
				WelcomeActivity.a_normal_over();
				ScanActivity.soundPlay_congradulate();
		   }
			if(panelViewA.theModel.lineXIsDamaged == "lineX_damaged"){
				if(MenuActivity.isVoice){
					//ScanActivity.lightsoundPool.stop(AVoiceReturn);
					WelcomeActivity.lightsoundPool.stop(AVoiceReturn);
					WelcomeActivity.a_damaged();
				}
				if(panelViewA.theModel.rank == "1" && panelViewB.theModel.lineXIsDamaged != "lineX_damaged"){
					ADamagedVoiceSwitch = "off";
					exchange();
				}
			}
			line1StateJudge();
			
			if (timer1 != null) {
				timer1.cancel();
			}
			//send over command
			byte[] aa = {(byte) 0xa5,0x01};
			/*mIntent.putExtra(BLEconfig.EXTRA_COMMAND, BLEconfig.COMMAND_WRITE_CHARACTERISTIC);
			mIntent.putExtra(BLEconfig.WRITE_VALUE, aa);
			startService(mIntent);*/
			
			Tools.mBleService.characteristicWrite.setValue(aa);
	        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characteristicWrite);
			
			line1TotleTime = deltaT;
			line1FastTime = fastestTimeOne;
			//line1 is over,judge the line2 state
			if(panelViewB.theModel.lineXStatus == "lineX_gameover"){
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						over();
					}
				}, 400);
				
			}
		
		}
		
		if(panelViewA.theModel.lineXStatus != "lineX_gameover"){
			//panelViewA refueling
			panelViewARefuel();
		}
		line1StateJudge();
		//Log.i("hello", "line1_status is"+panelViewA.theModel.lineXStatus);
		timer1RunningStatus = true;
		}
	}
	
	
	
	/**
	 * toy-car pass the position 12
	 * 1.change pointer
	 * 2.update the time stamp
	 */
	public void msgPosi_12(){
		if(timer1RunningStatus){
			panelViewA.theModel.lineXStatus = "over";
			if (timer1 != null) {
				timer1.cancel();
			}
			
			line1StateJudge();
			if(panelViewA.theModel.lineXStatus != "lineX_gameover")
			line1StateJudge();
			
			timer1RunningStatus = false;
		}
		
	}
	
	/**
	 * toy-car pass the position 21
	 * @param deltaT
	 * @param min
	 * @param sec
	 * @param milli
	 * 
	 * 1. calculate and update the fastest time
	 * 2. calculate the total time
	 * 3. record laps number
	 * 4. judge the game ending
	 */
	String theVoiceStateSwitchB = "null";
	public void msgPosi_21(long deltaT,long min,long sec,long milli){
		/*Window theWindow = this.getWindow();
		share(theWindow);*/
		line2StateJudge();
		
		if(!timer2RunningStatus && panelViewB.theModel.lineXStatus != "lineX_gameover" && panelViewB.theModel.lineXIsDamaged != "lineX_damaged"){
			theVoiceStateSwitchB = "on";
			//be voice?
			if(MenuActivity.isVoice)
				ScanActivity.soundPlay_overtake();
		
		currentTime = new Date();
		//handle fastest
		if(timeStampTwo != -1)
		{
			if(fastestTimeTwo > currentTime.getTime() - timeStampTwo)
				fastestTimeTwo = currentTime.getTime() - timeStampTwo ;
			
			panelViewB.theModel.fastTime = timeFormat(fastestTimeTwo);
		}
		
		//handle time
		deltaT=currentTime.getTime() - beginTime.getTime();
		
		panelViewB.theModel.totleTime = timeFormat(deltaT);
		//panelViewB.theModel.totleTime = String.valueOf(min)+":"+String.valueOf(sec)+":"+String.valueOf(milli);
		//handle laps
		if(panelViewB.theModel.lineXStatus != "lineX_gameover"){
			lapsTwo++;
		}
		line2StateJudge();
		if(lapsTwo<=laps){
			panelViewB.theModel.lapValue = lapsTwo+"/"+laps;
				panelViewB.lapValuePosition = 0.120;
			}
		
		
		if(TAG_firstLayoutTop && lapsOne<lapsTwo)
			exchange();
		
		if(timeStampTwo!=-1 && panelViewB.theModel.leftPointerDegree > -30 && panelViewB.theModel.rightPointerDegree < 30){
			if(remainder2 > 0){
				add2 = 1;
				remainder2 -- ;
			}else{
				add2 = 0;
				remainder2 -- ;
			}
			changeSidePointer(2);
			line2Count --;
		}
		line2StateJudge();
		timeStampTwo = new Date().getTime();
		if(lapsTwo == laps){
			panelViewB.theModel.lineXStatus = "normal over";
		}
		if (BVoiceSwitch == "off") {
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					panelViewBVoiceLogic();
				}
			},800);
			BVoiceSwitch = "on";
		}else{
			panelViewBVoiceLogic();
		}
		
		
		
		//end game with line one WIN
		if(panelViewB.theModel.lineXStatus == "damaged" || panelViewB.theModel.lineXStatus == "normal over" || panelViewB.theModel.lineXStatus == "lineX_gameover"){
			
			Log.i("diedie", "state"+panelViewB.theModel.lineXStatus);
			Log.i("diedie", "state"+panelViewB.theModel.lineXIsDamaged);
			if(panelViewB.theModel.lineXStatus == "normal over" && MenuActivity.isVoice){
				
				WelcomeActivity.b_normal_over();
				ScanActivity.soundPlay_congradulate();
			}
			if(panelViewB.theModel.lineXIsDamaged == "lineX_damaged"){
				if(MenuActivity.isVoice){
					WelcomeActivity.lightsoundPool.stop(BVoiceReturn);
					WelcomeActivity.b_damaged();
				}
				if(panelViewB.theModel.rank == "1" && panelViewA.theModel.lineXIsDamaged != "lineX_damaged"){
					BDamagedVoiceSwitch = "off";
					exchange();
				}
			}
			line2StateJudge();
			if (timer2 != null) {
				timer2.cancel();
			}
			//line2 over command
			byte[] bb = {(byte) 0xa5,0x02};
			/*mIntent.putExtra(BLEconfig.EXTRA_COMMAND, BLEconfig.COMMAND_WRITE_CHARACTERISTIC);
			mIntent.putExtra(BLEconfig.WRITE_VALUE, bb);
			startService(mIntent);*/
			
			Tools.mBleService.characteristicWrite.setValue(bb);
	        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characteristicWrite);
	        
			
			line2TotleTime = deltaT;
			line2FastTime = fastestTimeTwo;
			//姝ゆ椂Line2缁撴潫锛屽垽鏂璴ine1
			if (panelViewA.theModel.lineXStatus == "lineX_gameover") {
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						over();
					}
				}, 400);
				
			}
			
		}
		
		Log.i("yeye", "line2_status is"+panelViewB.theModel.lineXStatus);
		if(panelViewB.theModel.lineXStatus != "lineX_gameover"){
			panelViewBRefuel();
		 
		}
		line2StateJudge();
		timer2RunningStatus = true;
		}
	}
	
	/**
	 * toy-car pass the position 22
	 * 1. change the pointer
	 * 2. update the time stamp
	 */
	public void msgPosi_22(){
		//if(timeStampTwo!=-1 && leftPointerSecondDegree > -30 && rightPointerSecondDegree < 30){
		if(timer2RunningStatus){
			panelViewB.theModel.lineXStatus = "over";
			if (timer2 != null) {
				timer2.cancel();
			}
			
			line2StateJudge();
			if(panelViewB.theModel.lineXStatus != "lineX_gameover")
			line2StateJudge();
		
		timer2RunningStatus = false;
		}
	}
	
	/**
	 * handle the accelerator data 
	 * @param data is received data 
	 */
		public void msgAccChange(String data){
				String[] params = data.split(" ");
			//params[0] means car's No
			//params[1] means car's accelerator
			if(params[0].equals("1")){
				
				changePointer(params[1] , 1);
				//accFirst = Integer.valueOf(params[1]);
			}else if(params[0].equals("2")){
				//accTwo.setText(params[1]);
				changePointer(params[1] , 2);
				//accSecond = Integer.valueOf(params[1]);
			}
		}
		MyResultDialog reultDialog;
		Handler theHandler = new Handler(){
			public void handleMessage(Message msg) {
				if (msg.what == 1011) {
					activity_raceback();
				}
				
			};
		};
		public void showResultDialog() {
			
			theWindow11 = this.getWindow();
			
			reultDialog = new MyResultDialog(this,R.style.Dialog_Fullscreen,theHandler);
			//reultDialog.getWindow().setBackgroundDrawableResource(R.drawable.image1);
			reultDialog.show();
			
			
			//set dialog
			WindowManager.LayoutParams lp=reultDialog.getWindow().getAttributes();
			lp.alpha=1f;
			lp.width = screenWidth;
	    	lp.height = screenHeight;
			reultDialog.getWindow().setAttributes(lp);
			//set background
			WindowManager.LayoutParams lp1 = getWindow().getAttributes();
			lp1.alpha=0.4f;
			//lp1.dimAmount=0f;
			getWindow().setAttributes(lp1);
			
		}
		private void panelViewARefuel() {
			
			timer1 = new Timer();
			timer1.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//if(TrainPanelModle1.leftPointerDegree<30 && TrainPanelModle1.leftPointerDegree != -30){
					if(panelViewA.theModel.lineXStatus != "lineX_gameover" && panelViewA.theModel.leftPointerDegree<30 && panelViewA.theModel.leftPointerDegree != -30){
					
						panelViewA.theModel.lineXStatus = "refuel";
						if(remainder1 >= 0){
							Log.i("niangde", "1");
							panelViewA.perDegree = inte-1+1;
							add1 = 1;
								
							remainder1 ++;
						}else{
							Log.i("niangde", "2");
							panelViewA.perDegree = inte-1;
							add1 = 0;
						
							remainder1 ++;
							
						}
						panelViewA.oil_2_pleaseState = false;
						panelViewA.oil_4_pleaseState = false;
						
						
						panelViewA.theModel.leftPointerDegree = panelViewA.theModel.leftPointerDegree + (inte+add1);
						panelViewA.theModel.rightPointerDegree = panelViewA.theModel.rightPointerDegree - (inte+add1);
						
                        if (MenuActivity.isVoice) {
							
							if (panelViewA.theModel.leftPointerDegree != 30) {
								if (theVoiceStateSwitchA == "on") {
									WelcomeActivity.lightsoundPool.stop(AVoiceReturn);
									WelcomeActivity.a_refueling();
									theVoiceStateSwitchA = "off";
							
								}
							}else{
								WelcomeActivity.lightsoundPool.stop(AVoiceReturn);
								WelcomeActivity.a_refueled();
							}
						}
					
						panelViewA.sidePointerState = "plusState";
						line1Count ++;
						//a_oillessSwitch = "on";
						//a_please_oilSwitch = "on";
								
					}
				}
			}, 1500, 1800);
		
		}
	private void panelViewBRefuel() {
		timer2 = new Timer();
		 timer2.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(panelViewB.theModel.lineXStatus != "lineX_gameover" && panelViewB.theModel.leftPointerDegree<30 && panelViewB.theModel.leftPointerDegree != -30){
					panelViewB.theModel.lineXStatus = "refuel";
					if(remainder2 >= 0){
						Log.i("niangde", "1");
						panelViewB.perDegree = inte-1+1;
						add2 = 1;
							
						remainder2 ++;
					}else{
						Log.i("niangde", "2");
						panelViewB.perDegree = inte-1;
						add2 = 0;
					
						remainder2 ++;
						
					}
					panelViewB.oil_2_pleaseState = false;
					panelViewB.oil_4_pleaseState = false;
					
					panelViewB.theModel.leftPointerDegree = panelViewB.theModel.leftPointerDegree + (inte+add2);
					panelViewB.theModel.rightPointerDegree = panelViewB.theModel.rightPointerDegree - (inte+add2);
					
                    if (MenuActivity.isVoice) {
						
						if (panelViewB.theModel.leftPointerDegree != 30) {
							if (theVoiceStateSwitchB == "on") {
								Log.i("kaokao", "BVoiceReturn"+BVoiceReturn);
								WelcomeActivity.lightsoundPool.stop(BVoiceReturn);
								Log.i("kaokao", "BVoiceReturn"+BVoiceReturn);
								WelcomeActivity.b_refueling();
								theVoiceStateSwitchB = "off";
						
							}
						}else{
							WelcomeActivity.lightsoundPool.stop(BVoiceReturn);
							WelcomeActivity.b_refueled();
						}
					}
				
					//panelViewA.sidePointerState = "plusState";
					panelViewB.sidePointerState = "plusState";
					line2Count ++;
					//b_oillessSwitch = "on";
					//b_please_oilSwitch = "on";
				}
			}
		}, 1500, 1800);
	}
	private void panelViewAVoiceLogic() {
		
		// TODO Auto-generated method stub
		if(panelViewA.theModel.lineXStatus != "lineX_gameover"){
			if (laps - lapsOne ==2 && line1Count >= 2){
				if(MenuActivity.isVoice)
				AVoiceReturn = WelcomeActivity.a_rest_two_laps();
				}else if (laps - lapsOne ==1 && line1Count >= 1){
					if(MenuActivity.isVoice)
				AVoiceReturn = WelcomeActivity.a_rest_one_laps();
				}
			if(oil_consum >=6 && (laps - lapsOne > line1Count) ){
				panelViewA.oil_4_pleaseState = true;
				if(line1Count == 4){
					if(MenuActivity.isVoice)
					AVoiceReturn = WelcomeActivity.a_oiless();
				}else if(line1Count == 2){
					panelViewA.oil_2_pleaseState = true;
					if(MenuActivity.isVoice)
					AVoiceReturn = WelcomeActivity.a_please_refuel();
				}
			}else if(oil_consum < 6 && (laps - lapsOne > line1Count)){
				if(line1Count == 2){
					panelViewA.oil_2_pleaseState = true;
					if(MenuActivity.isVoice)
					AVoiceReturn = WelcomeActivity.a_please_refuel();
				}
			}
		    /*if (panelViewA.theModel.leftPointerDegree <= -5 && (panelViewA.theModel.leftPointerDegree > -20) && (laps - lapsOne > line1Count)) {
		    	if(a_oillessSwitch == "on"){
		    	AVoiceReturn = ScanActivity.a_oiless();
		    	a_oillessSwitch = "off";}
				}
		    if (panelViewA.theModel.leftPointerDegree <= -20 && (panelViewA.theModel.leftPointerDegree > -30) && (laps - lapsOne > line1Count)) {
		    	if(a_please_oilSwitch == "on"){
		    		AVoiceReturn = ScanActivity.a_please_refuel();
		    		a_please_oilSwitch = "off";
		    	}
		    			
			}*/
			
		}
		//return AVoiceReturn;
	}
	private void panelViewBVoiceLogic() {
		
		// TODO Auto-generated method stub
		if(panelViewB.theModel.lineXStatus != "lineX_gameover"){
			if (laps - lapsTwo ==2 && line2Count >= 2){
				if(MenuActivity.isVoice)
				BVoiceReturn = WelcomeActivity.b_rest_two_laps();
				}else if (laps - lapsTwo ==1 && line2Count >= 1){
					if(MenuActivity.isVoice)
				BVoiceReturn = WelcomeActivity.b_rest_one_laps();
				}
			if(oil_consum >=6 && (laps - lapsTwo > line2Count) ){
				if(line2Count == 4){
					panelViewB.oil_4_pleaseState = true;
					if(MenuActivity.isVoice)
					BVoiceReturn = WelcomeActivity.b_oiless();
				}else if(line2Count == 2){
					panelViewB.oil_2_pleaseState = true;
					if(MenuActivity.isVoice)
					BVoiceReturn = WelcomeActivity.b_please_refuel();
				}
			}else if(oil_consum < 6 && (laps - lapsTwo > line2Count)){
				if(line2Count == 2){
					panelViewB.oil_2_pleaseState = true;
					if(MenuActivity.isVoice)
					BVoiceReturn = WelcomeActivity.b_please_refuel();
				}
			}
			
			/*if (laps - lapsTwo ==2) {
				BVoiceReturn = ScanActivity.b_rest_two_laps();
				}else if (laps - lapsTwo ==1)
				BVoiceReturn = ScanActivity.b_rest_one_laps();
		    if (panelViewB.theModel.leftPointerDegree <= -5 && (panelViewB.theModel.leftPointerDegree > -20) && (laps - lapsTwo > line2Count)) {
		    	if(b_oillessSwitch == "on"){
			    	BVoiceReturn = ScanActivity.b_oiless();
			    	b_oillessSwitch = "off";
			    	}
				}else if (panelViewB.theModel.leftPointerDegree <= -20 && (panelViewB.theModel.leftPointerDegree > -30) && (laps - lapsTwo > line2Count)) {
					if(b_please_oilSwitch == "on"){
			    		BVoiceReturn = ScanActivity.b_please_refuel();
			    		b_please_oilSwitch = "off";
			    	}
			}*/
		
		}
	}
	//鑷繁璁惧畾鐨勮繑鍥為敭
	public void activity_raceback(){
		//showResultDialog();
		Log.i("info", "bbbbbbbbbbbbbb");
		isExit();
		}
			
	//鍝嶅簲绯荤粺鐨勮繑鍥為敭
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
				
	//return super.onKeyDown(keyCode, event);
	if(keyCode == KeyEvent.KEYCODE_BACK){
		isExit();
		}
		return false;
	}
	
	private void isExit() {
		
		resetData();
	    activity_state = "Exit";
	    
	    //restart command!
	    byte[] cc = {(byte) 0x5a};
		/*mIntent.putExtra(BLEconfig.EXTRA_COMMAND, BLEconfig.COMMAND_WRITE_CHARACTERISTIC);
		mIntent.putExtra(BLEconfig.WRITE_VALUE, cc);
		startService(mIntent);*/
		
		Tools.mBleService.characteristicWrite.setValue(cc);
        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characteristicWrite);
				
	     try {
			RaceActivity.this.finalize();
			RaceActivity.this.finish();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
		
			}
	/**
	 * 为每一次的请求设置一个唯一的标识
	 * @param type
	 * @return
	 */
	private String buildTransaction(final String type) {
		return (type == null)?String.valueOf(System.currentTimeMillis()):type + System.currentTimeMillis();
	}
	
	/**
	 * 分享到微信朋友圈
	 * @param v
	 */
	boolean result=false;
	public boolean screenShot(Window dialogWindow) {
		
		result = ScreenShotUtils.shotBitmap(dialogWindow);
		
		return result;
	}
	public void theShare(){
		if(result){
//			String path = SDCARD_ROOT + "/gh.png";
//			File file = new File(path);
//			
//			if (!file.exists()) {
//				
//				String tip = RaceActivity.this.getString(R.string.send_img_file_not_exist);
//				Toast.makeText(RaceActivity.this, tip + " path = " + path, Toast.LENGTH_LONG).show();
//				return ;
//			}
//			
//			WXImageObject imgObj = new WXImageObject();
//			imgObj.setImagePath(path);
//			
//			
//			WXMediaMessage msg = new WXMediaMessage();
//			msg.mediaObject = imgObj;
//			msg.title = "WebPage Title";
//            msg.description = "WebPage Description";
//			
//			Bitmap bmp = BitmapFactory.decodeFile(path);
//			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 100, true);
//			bmp.recycle();
//			msg.thumbData = bmpToByteArray(thumbBmp, true);
//			
//			SendMessageToWX.Req req = new SendMessageToWX.Req();
//			req.transaction = buildTransaction("img");
//			req.message = msg;
//			
//			req.scene = SendMessageToWX.Req.WXSceneTimeline ;
//			RaceActivity.api.sendReq(req);
//
		}else {
			Toast.makeText(RaceActivity.this, "截图失败.", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		resetDataAll();
		/*Log.e("info", "onDestroy....");
		panelViewA = null;
		panelViewB = null;
		//setContentView(R.layout.null_a);
		logoRelativeLayout = null;
		reultDialog = null;
		mContext = null;
		dm = null;*/
		System.gc();
		
		super.onDestroy();
		
	}
	//将图片转化成二进制格式
	private byte[] bmpToByteArray(final Bitmap bitmap,final boolean needRecyle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
		if (needRecyle) {
			bitmap.recycle();
		}
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
		}
		return result;
	}
			
}