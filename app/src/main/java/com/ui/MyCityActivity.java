package com.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.bluetoothARC.R;
import com.connect.BLEconfig;
import com.connect.Tools;

public class MyCityActivity extends Activity implements View.OnClickListener{

	private LightDialog lightDialog;
	private Intent mIntent;
	
	public int index = 0;
	//private DisplayMetrics dm;
	public int screenWidth,screenHeight;
	private ImageButton disBtn,trainBtn,raceBtn;
	
	private FrameLayout fLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_city);
		
		fLayout = (FrameLayout) findViewById(R.id.thecity);
		
		if (AppContext.device == "pad"){
			if (AppContext.lanuage) {//zh
				index = AppContext.index;
				switch (index) {
				case R.id.athensBtn:
					AppContext.laps = 10;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.athensk);
					break;
				case R.id.rioBtn:
					AppContext.laps = 15;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.riok);
					break;
				case R.id.new_yorkBtn:
					AppContext.laps = 16;
					AppContext.oil_consum = 10;
					fLayout.setBackgroundResource(R.drawable.new_yorkk);
					break;
				case R.id.londonBtn:
					AppContext.laps = 10;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.londonk);
					break;
				case R.id.beijing:
					AppContext.laps = 17;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.beijingk);
					break;
				case R.id.paris:
					AppContext.laps = 13;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.parisk);
					break;
				case R.id.oslo:
					AppContext.laps = 12;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.oslok);
					break;
				case R.id.prague:
					AppContext.laps = 9;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.praguek);
					break;
				case R.id.copenhague:
					AppContext.laps = 9;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.copenhaguek);
					break;
				case R.id.rome:
					AppContext.laps = 9;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.romek);
					break;
				case R.id.stockholm:
					AppContext.laps = 13;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.stockholmk);
					break;
				case R.id.berlin:
					AppContext.laps = 11;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.berlink);
					break;
				case R.id.tokyo:
					AppContext.laps = 12;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.tokyok);
					break;
				case R.id.mexico:
					AppContext.laps = 14;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.mexicok);
					break;
				case R.id.sydney:
					AppContext.laps = 14;
					AppContext.oil_consum = 10;
					fLayout.setBackgroundResource(R.drawable.sydneyk);
					break;
				case R.id.toronto:
					AppContext.laps = 18;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.torontok);
					break;
				case R.id.saint_peter:
					AppContext.laps = 19;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.saint_peterk);
					break;
				default:
					break;
				}
			}else {//非汉语--英文
				index = AppContext.index;
				switch (index) {
				case R.id.athensBtn:
					AppContext.laps = 10;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.athenske);
					break;
				case R.id.rioBtn:
					AppContext.laps = 15;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.rioke);
					break;
				case R.id.new_yorkBtn:
					AppContext.laps = 16;
					AppContext.oil_consum = 10;
					fLayout.setBackgroundResource(R.drawable.new_yorkke);
					break;
				case R.id.londonBtn:
					AppContext.laps = 10;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.londonke);
					break;
				case R.id.beijing:
					AppContext.laps = 17;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.beijingke);
					break;
				case R.id.paris:
					AppContext.laps = 13;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.pariske);
					break;
				case R.id.oslo:
					AppContext.laps = 12;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.osloke);
					break;
				case R.id.prague:
					AppContext.laps = 9;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.pragueke);
					break;
				case R.id.copenhague:
					AppContext.laps = 9;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.copenhagueke);
					break;
				case R.id.rome:
					AppContext.laps = 9;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.romeke);
					break;
				case R.id.stockholm:
					AppContext.laps = 13;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.stockholmke);
					break;
				case R.id.berlin:
					AppContext.laps = 11;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.berlinke);
					break;
				case R.id.tokyo:
					AppContext.laps = 12;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.tokyoke);
					break;
				case R.id.mexico:
					AppContext.laps = 14;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.mexicoke);
					break;
				case R.id.sydney:
					AppContext.laps = 14;
					AppContext.oil_consum = 10;
					fLayout.setBackgroundResource(R.drawable.sydneyke);
					break;
				case R.id.toronto:
					AppContext.laps = 18;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.torontoke);
					break;
				case R.id.saint_peter:
					AppContext.laps = 19;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.saint_peterke);
					break;
				default:
					break;
				}
			}
		}else if(AppContext.device == "phone"){
			if (AppContext.lanuage) {//zh
				index = AppContext.index;
				switch (index) {
				case R.id.athensBtn:
					AppContext.laps = 10;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.athenskphone);
					break;
				case R.id.rioBtn:
					AppContext.laps = 15;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.riokphone);
					break;
				case R.id.new_yorkBtn:
					AppContext.laps = 16;
					AppContext.oil_consum = 10;
					fLayout.setBackgroundResource(R.drawable.new_yorkkphone);
					break;
				case R.id.londonBtn:
					AppContext.laps = 10;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.londonkphone);
					break;
				case R.id.beijing:
					AppContext.laps = 17;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.beijingkphone);
					break;
				case R.id.paris:
					AppContext.laps = 13;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.pariskphone);
					break;
				case R.id.oslo:
					AppContext.laps = 12;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.oslokphone);
					break;
				case R.id.prague:
					AppContext.laps = 9;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.praguekphone);
					break;
				case R.id.copenhague:
					AppContext.laps = 9;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.copenhaguekphone);
					break;
				case R.id.rome:
					AppContext.laps = 9;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.romekphone);
					break;
				case R.id.stockholm:
					AppContext.laps = 13;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.stockholmkphone);
					break;
				case R.id.berlin:
					AppContext.laps = 11;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.berlinkphone);
					break;
				case R.id.tokyo:
					AppContext.laps = 12;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.tokyokphone);
					break;
				case R.id.mexico:
					AppContext.laps = 14;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.mexicokphone);
					break;
				case R.id.sydney:
					AppContext.laps = 14;
					AppContext.oil_consum = 10;
					fLayout.setBackgroundResource(R.drawable.sydneykphone);
					break;
				case R.id.toronto:
					AppContext.laps = 18;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.torontokphone);
					break;
				case R.id.saint_peter:
					AppContext.laps = 19;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.saint_peterkphone);
					break;
				default:
					break;
				}
			}else {
				index = AppContext.index;
				switch (index) {
				case R.id.athensBtn:
					AppContext.laps = 10;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.athenskephone);
					break;
				case R.id.rioBtn:
					AppContext.laps = 15;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.riokephone);
					break;
				case R.id.new_yorkBtn:
					AppContext.laps = 16;
					AppContext.oil_consum = 10;
					fLayout.setBackgroundResource(R.drawable.new_yorkkephone);
					break;
				case R.id.londonBtn:
					AppContext.laps = 10;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.londonkephone);
					break;
				case R.id.beijing:
					AppContext.laps = 17;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.beijingkephone);
					break;
				case R.id.paris:
					AppContext.laps = 13;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.pariskephone);
					break;
				case R.id.oslo:
					AppContext.laps = 12;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.oslokephone);
					break;
				case R.id.prague:
					AppContext.laps = 9;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.praguekephone);
					break;
				case R.id.copenhague:
					AppContext.laps = 9;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.copenhaguekephone);
					break;
				case R.id.rome:
					AppContext.laps = 9;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.romekephone);
					break;
				case R.id.stockholm:
					AppContext.laps = 13;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.stockholmkephone);
					break;
				case R.id.berlin:
					AppContext.laps = 11;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.berlinkephone);
					break;
				case R.id.tokyo:
					AppContext.laps = 12;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.tokyokephone);
					break;
				case R.id.mexico:
					AppContext.laps = 14;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.mexicokephone);
					break;
				case R.id.sydney:
					AppContext.laps = 14;
					AppContext.oil_consum = 10;
					fLayout.setBackgroundResource(R.drawable.sydneykephone);
					break;
				case R.id.toronto:
					AppContext.laps = 18;
					AppContext.oil_consum = 8;
					fLayout.setBackgroundResource(R.drawable.torontokephone);
					break;
				case R.id.saint_peter:
					AppContext.laps = 19;
					AppContext.oil_consum = 9;
					fLayout.setBackgroundResource(R.drawable.saint_peterkephone);
					break;
				default:
					break;
				}
			}
		}
		
		
		mIntent = new Intent(MyCityActivity.this,BLEconfig.class);
		
		screenHeight = AppContext.screenHeight;
		screenWidth = AppContext.screenWidth;
		
		disBtn = (ImageButton) findViewById(R.id.dismiss);
		disBtn.setOnClickListener(this);
		
		trainBtn = (ImageButton)findViewById(R.id.trainbtn);
		trainBtn.setOnClickListener(this);
		
		raceBtn = (ImageButton) findViewById(R.id.racebtn);
		raceBtn.setOnClickListener(this);
		
		if (AppContext.device == "phone"){
			
			FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(screenWidth*1/10, screenWidth*1/10);
			params3.setMargins((int) (screenWidth*0.88), (int) (screenHeight*0.23), 0, 0);
			disBtn.setLayoutParams(params3);
			
			setPosi(trainBtn, (float)0.2, (float)0.76,(float)1.2);
			setPosi(raceBtn, (float)0.55, (float)0.76,(float)1.2);
			if (AppContext.lanuage) {//zh
					trainBtn.setBackgroundResource(R.drawable.trainbtn);
					raceBtn.setBackgroundResource(R.drawable.racebtn);
				}else {
					trainBtn.setBackgroundResource(R.drawable.trainbtne);
					raceBtn.setBackgroundResource(R.drawable.racebtne);
				}
			}else if(AppContext.device == "pad"){
				
				FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(screenWidth*1/10, screenWidth*1/10);
				params3.setMargins((int) (screenWidth*0.78), (int) (screenHeight*0.21), 0, 0);
				disBtn.setLayoutParams(params3);
				
				setPosi(trainBtn, (float)0.2, (float)0.76,1);
				setPosi(raceBtn, (float)0.55, (float)0.76,1);
				if (AppContext.lanuage) {//zh
					trainBtn.setBackgroundResource(R.drawable.trainbtn);
					raceBtn.setBackgroundResource(R.drawable.racebtn);
				}else {
					trainBtn.setBackgroundResource(R.drawable.trainbtne);
					raceBtn.setBackgroundResource(R.drawable.racebtne);
				}
			}
		
		
	}
	public void setPosi(ImageButton imageButton,float left,float top,float per) {
		FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams((int) (screenWidth*0.21*per), (int) (screenWidth*0.1*per));
		params3.setMargins((int) (screenWidth*left), (int) (screenHeight*top), 0, 0);
		imageButton.setLayoutParams(params3);
		
	}
	 public void race(){
	    	
	    	lightDialog = new LightDialog(this,R.style.Dialog_Fullscreen);
	    	
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
					Intent intent = new Intent(MyCityActivity.this,RaceActivity.class);
			    	startActivity(intent);
			    	//淇creat RaceActivity鏃剁殑鏃堕棿鍋忓樊涓�0姣
			    	new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
					    	lightDialog.dismiss();
					    	MyCityActivity.this.finish();
						}
					}, 16);
			    	
				}
			}, 5030);
	    	
	    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dismiss:
			/*FrameLayout.LayoutParams Rl= (FrameLayout.LayoutParams) disBtn.getLayoutParams();
			Rl.width=(int) (disBtn.getWidth()*0.9);
			Rl.height = (int) (disBtn.getHeight()*0.9);
			disBtn.setLayoutParams(Rl);*/
			
			finish();
			break;
		case R.id.trainbtn:
			
			Intent intent = new Intent(this,TrainActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.racebtn:
	    	race();
	    	
			break;
		default:
			break;
		}
		
	}

}
