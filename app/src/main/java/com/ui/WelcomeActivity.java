package com.ui;

import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluetoothARC.R;
import com.myview.PanelViewA;
import com.myview.PanelViewB;

/**
 * This activity is a welcome screen
 * @author likun@stu.zzu.edu.cn
 *
 */
public class WelcomeActivity extends Activity
{
	private SharedPreferences sharedPreferences;
	
	static SoundPool lightsoundPool,lightsoundPool1;
	static HashMap<Integer, Integer> soundMap,soundMap1;
	private TheVoiceThread VoiceThread ;
	
	private ImageView logoView;
	private TextView version;
	
	Bitmap mBgBitmap = null,bitmapBackBitmap = null;  
	
	private DisplayMetrics dm;
	private int screenWidth=0,screenHeight=0;
	private float scaledDensity = 0;
	@SuppressWarnings("deprecation")
	@SuppressLint("UseSparseArrays")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		//默认隐藏虚拟按键
		//getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION); 
		//禁止息屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.welcome);
		
		//int memClass = WelcomeActivity.this.getSystemService(Context.ACTIVITY_SERVICE).getClass().getm;
		
		bitmapBackBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background1);
		findViewById(R.id.welcomBack).setBackgroundDrawable(new BitmapDrawable(bitmapBackBitmap));
		
		String keyCommType = AppContext.getInstance().getResources().getString(R.string.usename);
		//参数1：name，参数2：存储模式
		sharedPreferences = getSharedPreferences("configdata", MODE_PRIVATE);
		//Editor editor = sharedPreferences.edit();
		Log.i("result", keyCommType+":"+sharedPreferences.getString("voice", "")+":"+sharedPreferences.getString("user1", "")+":");
		String ffvoice = sharedPreferences.getString("voice", "be voice");
		if (ffvoice.equals("be voice")) {
			Log.i("result","true");
			MenuActivity.isVoice = true;
		}else {
			Log.i("result","false");
			MenuActivity.isVoice = false;
		}
		AppContext.useName1 = sharedPreferences.getString("user1", keyCommType);
		AppContext.useName2 = sharedPreferences.getString("user2", keyCommType);
		
		AppContext.oil_consum = sharedPreferences.getInt("oil_consum", 12);
		AppContext.laps = sharedPreferences.getInt("laps", 15);
		/*editor.putString("voice", "");
		editor.putString("user1", arg1);
		editor.putString("user2", arg1);
		editor.commit();*/
		
		Locale locale = getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		
		lightsoundPool1 = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
		soundMap1 = new HashMap<Integer, Integer>();
		
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		screenHeight = dm.heightPixels;
		screenWidth = dm.widthPixels;
		AppContext.scaledDensity = dm.scaledDensity;
		AppContext.inch = (float) (Math.sqrt(screenHeight*screenHeight+screenWidth*screenWidth)/(dm.densityDpi));
		
		AppContext.screenHeight = dm.heightPixels;
		AppContext.screenWidth = dm.widthPixels;
		//float gg = dm.scaledDensity;
		
		//Log.i("dada", "densityDpi"+den+"  :scaledDensity:"+gg+"  inch:"+Math.sqrt(screenHeight*screenHeight+screenWidth*screenWidth)/den);
		
		logoView = (ImageView) findViewById(R.id.imageViewlogo);
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams((int)(screenWidth*0.7), (int)(screenWidth*0.23));
		param.setMargins((int)(screenWidth*0.15), (int)(screenHeight*0.35), 0, 0);
		logoView.setLayoutParams(param);
		
		version = (TextView) findViewById(R.id.versionNumber);
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams((int)(screenWidth*0.32), (int)(screenWidth*0.13));
		params1.setMargins((int)(screenWidth*0.04), (int)(screenHeight*0.93), 0, 0);
		version.setLayoutParams(params1);
		
		
		
		//中英变换及切换
		if(language.endsWith("zh")){
			AppContext.lanuage = true;
			soundMap1.put(1, lightsoundPool1.load(WelcomeActivity.this, R.raw.welcome, 1));
			
			mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);  
	        findViewById(R.id.imageViewlogo).setBackgroundDrawable(new BitmapDrawable(mBgBitmap));
		}else {
			
			AppContext.lanuage = false;
			//findViewById(R.id.imageViewlogo).setBackgroundResource(R.drawable.logo2);
			
			mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo2);  
	        findViewById(R.id.imageViewlogo).setBackgroundDrawable(new BitmapDrawable(mBgBitmap));
			
			soundMap1.put(1, lightsoundPool1.load(WelcomeActivity.this, R.raw.welcomep, 1));
			
			AppContext.nullString = " ";
			AppContext.please_refuelString = "Low Gas prepare to pit stop";
			AppContext.refuelingString = "Refueling";
			AppContext.damagedString = "Out of Gas";
			AppContext.over_perfectString = " Completed your game has finished";
			AppContext.oversString = " Completed your game has finished";
		}
		
		
		//show version information
		PackageManager pm = getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo("com.bluetoothARC", 0);
			//TextView versionView = (TextView)findViewById(R.id.versionNumber);
			Log.i("diedie", "hhh:"+pi.versionName);
			version.setText("Version " + pi.versionName);
		} catch (Exception e) {
			Log.i("diedie", "Exception:version information ");
			e.printStackTrace();
		}
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					if(MenuActivity.isVoice)
					lightsoundPool1.play(soundMap1.get(1), 1, 1, 0, 0, 1);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		},1000);
		// delay 2.5s , start ScanActivity
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				
				Intent intent = new Intent(WelcomeActivity.this,VideoActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
			}
		},3000);//3000
		
	}
	@Override
	protected void onDestroy() {
		if(mBgBitmap != null  && !mBgBitmap.isRecycled())  
        {  
            mBgBitmap.recycle();  
            mBgBitmap = null;  
        }
        if(bitmapBackBitmap != null  && !bitmapBackBitmap.isRecycled())  
        {  
        	bitmapBackBitmap.recycle();  
        	bitmapBackBitmap = null;  
        }
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (VoiceThread == null) {
			VoiceThread = new TheVoiceThread();
			VoiceThread.start();
		}
		super.onResume();
	}

	class TheVoiceThread extends Thread{
		@SuppressLint("UseSparseArrays") @Override
		public void run() {
			lightsoundPool = new SoundPool(26,AudioManager.STREAM_MUSIC,0);
			
			soundMap = new HashMap<Integer, Integer>();
			
			if(AppContext.lanuage){
				Log.i("diedie", "1111111111");
				soundMap.put(25, lightsoundPool.load(WelcomeActivity.this, R.raw.go, 1));
				soundMap.put(26, lightsoundPool.load(WelcomeActivity.this, R.raw.one, 1));
				soundMap.put(27, lightsoundPool.load(WelcomeActivity.this, R.raw.two, 1));
				soundMap.put(28, lightsoundPool.load(WelcomeActivity.this, R.raw.three, 1));
				
				soundMap.put(7, lightsoundPool.load(WelcomeActivity.this, R.raw.a_oiless, 1));
				soundMap.put(8, lightsoundPool.load(WelcomeActivity.this, R.raw.a_please_refuel, 1));
				soundMap.put(9, lightsoundPool.load(WelcomeActivity.this, R.raw.a_refueling, 1));
				soundMap.put(10, lightsoundPool.load(WelcomeActivity.this, R.raw.a_refueled, 1));
				soundMap.put(11, lightsoundPool.load(WelcomeActivity.this, R.raw.a_rest_one_laps, 1));
				soundMap.put(12, lightsoundPool.load(WelcomeActivity.this, R.raw.a_rest_two_laps, 1));
				soundMap.put(13, lightsoundPool.load(WelcomeActivity.this, R.raw.a_damaged_over, 1));
				soundMap.put(14, lightsoundPool.load(WelcomeActivity.this, R.raw.a_normal_over, 1));
				soundMap.put(15, lightsoundPool.load(WelcomeActivity.this, R.raw.a_lead, 1));
				
				soundMap.put(16, lightsoundPool.load(WelcomeActivity.this, R.raw.b_oiless, 1));
				soundMap.put(17, lightsoundPool.load(WelcomeActivity.this, R.raw.b_please_refuel, 1));
				soundMap.put(18, lightsoundPool.load(WelcomeActivity.this, R.raw.b_refueling, 1));
				soundMap.put(19, lightsoundPool.load(WelcomeActivity.this, R.raw.b_refueled, 1));
				soundMap.put(20, lightsoundPool.load(WelcomeActivity.this, R.raw.b_rest_one_laps, 1));
				soundMap.put(21, lightsoundPool.load(WelcomeActivity.this, R.raw.b_rest_two_laps, 1));
				soundMap.put(22, lightsoundPool.load(WelcomeActivity.this, R.raw.b_damaged_over, 1));
				soundMap.put(23, lightsoundPool.load(WelcomeActivity.this, R.raw.b_normal_over, 1));
				soundMap.put(24, lightsoundPool.load(WelcomeActivity.this, R.raw.b_lead, 1));
				
				
			}else{
				
				soundMap.put(25, lightsoundPool.load(WelcomeActivity.this, R.raw.go2, 1));
				soundMap.put(26, lightsoundPool.load(WelcomeActivity.this, R.raw.one2, 1));
				soundMap.put(27, lightsoundPool.load(WelcomeActivity.this, R.raw.two2, 1));
				soundMap.put(28, lightsoundPool.load(WelcomeActivity.this, R.raw.three2, 1));
				
				soundMap.put(7, lightsoundPool.load(WelcomeActivity.this, R.raw.a_oilessp, 1));
				soundMap.put(8, lightsoundPool.load(WelcomeActivity.this, R.raw.a_please_refuelp, 1));
				soundMap.put(9, lightsoundPool.load(WelcomeActivity.this, R.raw.a_refuelingp, 1));
				soundMap.put(10, lightsoundPool.load(WelcomeActivity.this, R.raw.a_refueledp, 1));
				soundMap.put(11, lightsoundPool.load(WelcomeActivity.this, R.raw.a_rest_onep, 1));
				soundMap.put(12, lightsoundPool.load(WelcomeActivity.this, R.raw.a_rest_twop, 1));
				soundMap.put(13, lightsoundPool.load(WelcomeActivity.this, R.raw.a_damagedp, 1));
				soundMap.put(14, lightsoundPool.load(WelcomeActivity.this, R.raw.a_normalp, 1));
				soundMap.put(15, lightsoundPool.load(WelcomeActivity.this, R.raw.a_leadp, 1));
				
				soundMap.put(16, lightsoundPool.load(WelcomeActivity.this, R.raw.b_oilessp, 1));
				soundMap.put(17, lightsoundPool.load(WelcomeActivity.this, R.raw.b_please_refuelp, 1));
				soundMap.put(18, lightsoundPool.load(WelcomeActivity.this, R.raw.b_refuelingp, 1));
				soundMap.put(19, lightsoundPool.load(WelcomeActivity.this, R.raw.b_refueledp, 1));
				soundMap.put(20, lightsoundPool.load(WelcomeActivity.this, R.raw.b_rest_onep, 1));
				soundMap.put(21, lightsoundPool.load(WelcomeActivity.this, R.raw.b_rest_twop, 1));
				soundMap.put(22, lightsoundPool.load(WelcomeActivity.this, R.raw.b_damagedp, 1));
				soundMap.put(23, lightsoundPool.load(WelcomeActivity.this, R.raw.b_normalp, 1));
				soundMap.put(24, lightsoundPool.load(WelcomeActivity.this, R.raw.b_leadp, 1));
				
			}
		}
	}
	
	//a........
  	public static int a_oiless() {
  		return lightsoundPool.play(soundMap.get(7), 1, 1, 0, 0, 1);
  	}
  	public static int a_please_refuel() {
  		return lightsoundPool.play(soundMap.get(8), 1, 1, 0, 0, 1);
  	}
  	public static void a_refueling(){
  		lightsoundPool.play(soundMap.get(9), 1, 1, 0, 0, 1);
  	}
  	public static void a_refueled() {
  		lightsoundPool.play(soundMap.get(10), 1, 1, 0, 0, 1);
  	}
  	public static int a_rest_one_laps() {
  		return lightsoundPool.play(soundMap.get(11), 1, 1, 0, 0, 1);
  	}
  	public static int a_rest_two_laps() {
  		return lightsoundPool.play(soundMap.get(12), 1, 1, 0, 0, 1);
  	}
  	public static void a_damaged() {
  		lightsoundPool.play(soundMap.get(13), 1, 1, 0, 0, 1);
  	}
  	public static void a_normal_over() {
  		//lightsoundPool.play
  		lightsoundPool.play(soundMap.get(14), 1, 1, 0, 0, 1);
  	}
  	public static void a_lead() {
  		lightsoundPool.play(soundMap.get(15), 1, 1, 0, 0, 1);
  	}
  	
  	//b.......
  	public static int b_oiless() {
  		return lightsoundPool.play(soundMap.get(16), 1, 1, 0, 0, 1);
  	}
  	public static int b_please_refuel() {
  		return lightsoundPool.play(soundMap.get(17), 1, 1, 0, 0, 1);
  	}
  	public static void b_refueling() {
  		lightsoundPool.play(soundMap.get(18), 1, 1, 0, 0, 1);
  	}
  	public static void b_refueled() {
  		lightsoundPool.play(soundMap.get(19), 1, 1, 0, 0, 1);
  	}
  	public static int b_rest_one_laps() {
  		return lightsoundPool.play(soundMap.get(20), 1, 1, 0, 0, 1);
  	}
  	public static int b_rest_two_laps() {
  		return lightsoundPool.play(soundMap.get(21), 1, 1, 0, 0, 1);
  	}
  	public static void b_damaged() {
  		lightsoundPool.play(soundMap.get(22), 1, 1, 0, 0, 1);
  	}
  	public static void b_normal_over() {
  		lightsoundPool.play(soundMap.get(23), 1, 1, 0, 0, 1);
  	}
  	public static void b_lead() {
  		lightsoundPool.play(soundMap.get(24), 1, 1, 0, 0, 1);
  	}
	
}