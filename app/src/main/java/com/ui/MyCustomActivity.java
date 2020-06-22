package com.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bluetoothARC.R;
import com.myview.PanelView;
import com.connect.BLEconfig;

public class MyCustomActivity extends Activity implements OnClickListener {

	private LightDialog lightDialog;
	private Intent mIntent;
	
	private int screenWidth, screenHeight;
	//private DisplayMetrics dm;
	private ImageButton settingBtn, guideBtn,trainBtn,raceBtn;;
	private ImageButton reduce1, reduce2, plus1, plus2, dismissBtn, confirm;
	private TextView textView_lap, textView_oil;

	private int imageState = 1;
	private int theLap = AppContext.laps, oil_consum = AppContext.oil_consum;

	Handler mHandler;
	static Boolean longClicked;
	
	private SharedPreferences sharedPreferences;
	private Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_custom);
		
		sharedPreferences = getSharedPreferences("configdata", MODE_PRIVATE);
		editor = sharedPreferences.edit();

		//dm = new DisplayMetrics();
		//getWindowManager().getDefaultDisplay().getMetrics(dm);

		screenHeight = AppContext.screenHeight;
		screenWidth = AppContext.screenWidth;
		
		dismissBtn = (ImageButton) findViewById(R.id.custom_dismiss);
		dismissBtn.setOnClickListener(this);
		
		settingBtn = (ImageButton)findViewById(R.id.custom_settingBtn);
		settingBtn.setOnClickListener(this);
		
		guideBtn = (ImageButton)findViewById(R.id.custom_guidingBtn);
	
		guideBtn.setOnClickListener(this);
		
		trainBtn = (ImageButton)findViewById(R.id.custom_trainbtn);
		trainBtn.setOnClickListener(this);
		
		raceBtn = (ImageButton) findViewById(R.id.custom_racebtn);
		raceBtn.setOnClickListener(this);
		
		textView_lap = (TextView) findViewById(R.id.textView_laps);
		textView_lap.setText(String.valueOf(AppContext.laps));
		textView_lap.setTextSize((float) (29*AppContext.inch/7.9));	
		
		textView_oil = (TextView) findViewById(R.id.textView_oils);
		textView_oil.setText(String.valueOf(AppContext.oil_consum));
		textView_oil.setTextSize((float) (29*AppContext.inch/7.9));	
		
		reduce1 = (ImageButton) findViewById(R.id.imageButton_reduce1);
		reduce1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				Log.d("niangde", "---------..");
				hookMove(arg0, arg1);

				return false;
			}

		});
		
		plus1 = (ImageButton) findViewById(R.id.imageButton_plus1);
		plus1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				Log.d("niangde", "---------..");
				hookMove(arg0, arg1);

				return false;
			}

		});
		
		if (AppContext.device == "phone"){
			
			//guidingBtn
			FrameLayout.LayoutParams params12 = new FrameLayout.LayoutParams((int) (screenWidth * 0.13), (int) (screenWidth * 0.16));
			params12.setMargins((int) (screenWidth * 4 / 20),(int) ((screenHeight * 1 / 16)), 0, 0);
			guideBtn.setLayoutParams(params12);
			
			// setting_btn
			FrameLayout.LayoutParams params21 = new FrameLayout.LayoutParams((int) (screenWidth * 0.13), (int) (screenWidth * 0.16));
			params21.setMargins((screenWidth * 1 / 20), screenHeight * 1 / 16, 0, 0);
			settingBtn.setLayoutParams(params21);
			
			// disBtn 
			FrameLayout.LayoutParams params37 = new FrameLayout.LayoutParams(screenWidth * 1 / 9, screenWidth * 1 / 9);
			params37.setMargins((int) (screenWidth * 0.88),(int) ((screenHeight * 0.17)), 0, 0);
			dismissBtn.setLayoutParams(params37);
			
				FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams((int) (screenWidth*2.1/8), screenWidth*1/8);//1/10
				params3.setMargins((int) (screenWidth*4/20), (int) (screenHeight*0.66), 0, 0);
				trainBtn.setLayoutParams(params3);
				
				FrameLayout.LayoutParams params34 = new FrameLayout.LayoutParams((int) (screenWidth*2.1/8), screenWidth*1/8);//1/10
				params34.setMargins((int) (screenWidth*11/20), (int) (screenHeight*0.66), 0, 0);
				raceBtn.setLayoutParams(params34);
				
				
				setPosi2(textView_lap, (float) 0.15, (float) 0.06, (float) 0.44,(float) 0.51);
			
				setPosi2(textView_oil, (float) 0.15, (float) 0.06, (float) 0.44,(float) 0.563);
			
				FrameLayout.LayoutParams params33 = new FrameLayout.LayoutParams((int) (screenWidth * 0.06), (int) (screenWidth * 0.06));
				params33.setMargins((int) (screenWidth * 0.37),(int) (screenHeight * 0.51), 0, 0);//0.39
				reduce1.setLayoutParams(params33);
				
				FrameLayout.LayoutParams params44 = new FrameLayout.LayoutParams((int) (screenWidth * 0.06), (int) (screenWidth * 0.06));
				params44.setMargins((int) (screenWidth * 0.60),(int) (screenHeight * 0.51), 0, 0);//4125
				plus1.setLayoutParams(params44);
				
				setPosi(reduce2, R.id.imageButton_reduce2, (float) 0.06, (float) 0.06,(float) 0.37, (float) 0.563);
				
				setPosi(plus2, R.id.imageButton_plus2, (float) 0.06, (float) 0.06,(float) 0.60, (float) 0.563);//466
			if (AppContext.lanuage) {//zh
					findViewById(R.id.custom_back).setBackgroundResource(R.drawable.background_custom_rp);
					guideBtn.setBackgroundResource(R.drawable.guiding);
					settingBtn.setBackgroundResource(R.drawable.setting);
					
					trainBtn.setBackgroundResource(R.drawable.trainbtn);
					raceBtn.setBackgroundResource(R.drawable.racebtn);
				}else {
					findViewById(R.id.custom_back).setBackgroundResource(R.drawable.background_custom_pe);
					guideBtn.setBackgroundResource(R.drawable.guidinge);
					settingBtn.setBackgroundResource(R.drawable.settinge);
					
					trainBtn.setBackgroundResource(R.drawable.trainbtne);
					raceBtn.setBackgroundResource(R.drawable.racebtne);
				}
			}else if(AppContext.device == "pad"){
				
				FrameLayout.LayoutParams params12 = new FrameLayout.LayoutParams((int) (screenWidth * 0.13), (int) (screenWidth * 0.16));
				params12.setMargins((int) (screenWidth * 4 / 20),(int) ((screenHeight * 1 / 20)), 0, 0);
				guideBtn.setLayoutParams(params12);
				
				// setting_btn
				FrameLayout.LayoutParams params21 = new FrameLayout.LayoutParams((int) (screenWidth * 0.13), (int) (screenWidth * 0.16));
				params21.setMargins((screenWidth * 1 / 20), screenHeight * 1 / 20, 0, 0);
				settingBtn.setLayoutParams(params21);
				
				// disBtn 
				FrameLayout.LayoutParams params37 = new FrameLayout.LayoutParams(screenWidth * 1 / 9, screenWidth * 1 / 9);
				params37.setMargins((int) (screenWidth * 0.78),(int) ((screenHeight * 0.21)), 0, 0);
				dismissBtn.setLayoutParams(params37);
				
				FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams((int) (screenWidth*2.1/10), screenWidth*1/10);
				params3.setMargins((int) (screenWidth*4/20), (int) (screenHeight*0.73), 0, 0);//.58
				trainBtn.setLayoutParams(params3);
				
				FrameLayout.LayoutParams params34 = new FrameLayout.LayoutParams((int) (screenWidth*2.1/10), screenWidth*1/10);
				params34.setMargins((int) (screenWidth*11/20), (int) (screenHeight*0.73), 0, 0);
				raceBtn.setLayoutParams(params34);
				
				//setPosi(dismissBtn, R.id.custom_dismiss, (float) 0.1, (float) 0.1,(float) 0.78, (float) 0.0525);
				
				setPosi2(textView_lap, (float) 0.14, (float) 0.05, (float) 0.45,(float) 0.576);
			
				setPosi2(textView_oil, (float) 0.14, (float) 0.05, (float) 0.45,(float) 0.629);
			
				FrameLayout.LayoutParams params33 = new FrameLayout.LayoutParams((int) (screenWidth * 0.05), (int) (screenWidth * 0.05));
				params33.setMargins((int) (screenWidth * 0.39),(int) (screenHeight * 0.576), 0, 0);//4125
				reduce1.setLayoutParams(params33);
				
				FrameLayout.LayoutParams params44 = new FrameLayout.LayoutParams((int) (screenWidth * 0.05), (int) (screenWidth * 0.05));
				params44.setMargins((int) (screenWidth * 0.60),(int) (screenHeight * 0.576), 0, 0);
				plus1.setLayoutParams(params44);
				
				setPosi(reduce2, R.id.imageButton_reduce2, (float) 0.05, (float) 0.05,(float) 0.39, (float) 0.629);

				setPosi(plus2, R.id.imageButton_plus2, (float) 0.05, (float) 0.05,(float) 0.60, (float) 0.629);
				if (AppContext.lanuage) {//zh
					findViewById(R.id.custom_back).setBackgroundResource(R.drawable.background_custom);
					guideBtn.setBackgroundResource(R.drawable.guiding);
					settingBtn.setBackgroundResource(R.drawable.setting);
					
					trainBtn.setBackgroundResource(R.drawable.trainbtn);
					raceBtn.setBackgroundResource(R.drawable.racebtn);
				}else {
					findViewById(R.id.custom_back).setBackgroundResource(R.drawable.background_custom_e);
					guideBtn.setBackgroundResource(R.drawable.guidinge);
					settingBtn.setBackgroundResource(R.drawable.settinge);
					
					trainBtn.setBackgroundResource(R.drawable.trainbtne);
					raceBtn.setBackgroundResource(R.drawable.racebtne);
				}
			}
		

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 2:
					if (theLap < 99 && theLap > 2) {
						theLap++;
					}
					textView_lap.setText(" " + theLap + " ");
					break;
				case 1:
					if (theLap >= 4) {
						theLap--;
					}
					textView_lap.setText(" " + theLap + " ");
					break;
				default:
					break;
				}
				
				AppContext.laps = theLap;
				editor.putInt("laps",theLap);
				
			}
		};
		
		mIntent = new Intent(MyCustomActivity.this,BLEconfig.class);
	}
	@Override
	protected void onDestroy() {

		editor.commit();
		super.onDestroy();
	}
	 public void race(){
	    	
	    	lightDialog = new LightDialog(this,R.style.Dialog_Fullscreen);
	    	
	    	lightDialog.show();
	    	
	    	byte[] bb = {(byte) 0xa6};
			mIntent.putExtra(BLEconfig.EXTRA_COMMAND, BLEconfig.COMMAND_WRITE_CHARACTERISTIC);
			mIntent.putExtra(BLEconfig.WRITE_VALUE, bb);
			startService(mIntent);
	    	
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
					Intent intent = new Intent(MyCustomActivity.this,RaceActivity.class);
			    	startActivity(intent);
			    	//淇creat RaceActivity鏃剁殑鏃堕棿鍋忓樊涓�0姣
			    	new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
					    	lightDialog.dismiss();
					    	MyCustomActivity.this.finish();
						}
					}, 16);
			    	
				}
			}, 5030);
	    	
	    }
	// for guide;setting
	public void setPosi(ImageButton imageButton, int id, float width,float height, float left, float top) {
		imageButton = (ImageButton) findViewById(id);
		FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams((int) (screenWidth * width), (int) (screenWidth * height));
		params3.setMargins((int) (screenWidth * left),(int) (screenHeight * top), 0, 0);
		imageButton.setLayoutParams(params3);

		imageButton.setOnClickListener(this);

	}

	// for textView1,edittext2
	public void setPosi2(TextView textView, float width, float height,
			float left, float top) {

		FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams((int) (screenWidth * width), (int) (screenWidth * height));
		params3.setMargins((int) (screenWidth * left),(int) (screenHeight * top), 0, 0);
		textView.setLayoutParams(params3);

	}

	private void hookMove(View v, MotionEvent arg1) {
		// TODO Auto-generated method stub
		if (v == reduce1) {
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d("niangde", "0000000000..");
				longClicked = true;
				Thread t = new Thread() {
					@Override
					public void run() {
						super.run();
						while (longClicked) {
							// 发送命令
							mHandler.sendEmptyMessage(1);
							try {
								Thread.sleep(250);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				};
				t.start();

			} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
				longClicked = false;

			}

		} else if (v == plus1) {
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {

				longClicked = true;
				Thread t = new Thread() {
					@Override
					public void run() {
						super.run();
						while (longClicked) {
							Log.d("niangde", "111111111..");
							// 发送命令
							mHandler.sendEmptyMessage(2);
							try {
								Thread.sleep(250);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				};
				t.start();

			} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
				// Log.d("niangde","22222222222..");
				longClicked = false;

			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_settingBtn:
			Intent intent = new Intent(MyCustomActivity.this,
					MySettingActivity.class);
			startActivity(intent);
			
			this.finish();
			break;
		case R.id.custom_guidingBtn:
			Intent intent1 = new Intent(MyCustomActivity.this,
					MyGuideActivity.class);
			startActivity(intent1);
			
			this.finish();
			break;
		case R.id.custom_dismiss:
			this.finish();
			break;
		
		case R.id.imageButton_reduce1:

			break;
		case R.id.imageButton_reduce2:
			if (oil_consum >= 4) {
				oil_consum--;
			}
			textView_oil.setText(" " + oil_consum + " ");
			AppContext.oil_consum = oil_consum;
			editor.putInt("oil_consum",oil_consum);
			break;
		case R.id.imageButton_plus1:

			break;
		case R.id.imageButton_plus2:
			if (oil_consum < 20) {
				oil_consum++;
			}
			textView_oil.setText(" " + oil_consum + " ");
			AppContext.oil_consum = oil_consum;
			editor.putInt("oil_consum", oil_consum);
			break;
		case R.id.custom_trainbtn:
			
			AppContext.laps = theLap;
			AppContext.oil_consum = oil_consum;
			AppContext.imageState = imageState;
			
			if (oil_consum > 7 && oil_consum < 21)
				PanelView.oilSpeed = 18;
			if (oil_consum < 8)
				PanelView.oilSpeed = 9;

			Intent intentE = new Intent(this,TrainActivity.class);
			startActivity(intentE);
			finish();
			break;
		case R.id.custom_racebtn:
			
			AppContext.laps = theLap;
			AppContext.oil_consum = oil_consum;
			AppContext.imageState = imageState;

			if (oil_consum > 7 && oil_consum < 21)
				PanelView.oilSpeed = 18;
			if (oil_consum < 8)
				PanelView.oilSpeed = 9;
			
			race();
			break;
		default:
			break;
		}

	}

}
