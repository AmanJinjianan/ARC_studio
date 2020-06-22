package com.ui;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluetoothARC.R;

public class MyResultDialog extends Dialog implements View.OnClickListener{
	
	private TextView textViewLine1;
	private TextView textViewTime1;
	private TextView textViewLap1;
	private TextView textViewfast1;
	private ImageView imageViewTui1,face1;
	
	private ImageButton imageShare,imageback,imageBluetooth;
	//ProgressBar theBar;
	
	private TextView textViewLine2;
	private TextView textViewTime2;
	private TextView textViewLap2;
	private TextView textViewfast2;
	private TextView useName1,useName2;
	private ImageView imageViewTui2,face2;
	
	private ImageButton mapButton;
	
	private Handler theHandler;

	public MyResultDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	//private RaceActivity theRaceActivity = new RaceActivity();
	public MyResultDialog(Context context, int theme,Handler thehHandler) {
		super(context, theme);
		
		this.theHandler = thehHandler;
	}
	
	/*private TextView endLineOne,endLineTwo;
	private TextView endTimeOne,endTimeTwo;
	private TextView endLapOne,endLapTwo;*/
	//RaceResult raceResult;
	RaceActivity raceActivity;
	
	RelativeLayout center_end;
	
	int screenWidth = AppContext.screenWidth;
	int screenHeight = AppContext.screenHeight;
	//float value = RaceActivity.scaledDensity;
	
	float scaledDensity = AppContext.scaledDensity;
	float textSize = screenWidth/27/scaledDensity;
	
	RaceActivity theActivity;
	Dialog dialog;
	boolean result;
	
	public int y = 0;
	
	Window dialogWindow;
	
	boolean flag1=true,theFlag =false;
	
	Handler mHandler;
	private boolean lightState = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		System.gc();
		
		setContentView(R.layout.result);
		
		LinearLayout result_r = (LinearLayout)findViewById(R.id.result_r);
		
		
		theActivity = new RaceActivity();
		
		center_end = (RelativeLayout) findViewById(R.id.center_end110);
		
		imageBluetooth = (ImageButton) findViewById(R.id.result_bluetooth);
		
		imageback = (ImageButton)findViewById(R.id.result_back);
		imageback.setOnClickListener(this);
		
		imageShare = (ImageButton)findViewById(R.id.result_share);
		imageShare.setOnClickListener(this);
		imageShare.setEnabled(false);
		imageShare.setAlpha(0);
		
		//限定中间核心部位宽高
		/*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth,(int)(screenWidth*0.67));
		params.setMargins((int) (screenWidth*0.00), (int) (screenHeight*0.00), 0, 0);
		center_end.setLayoutParams(params);*/
		
		useName1 = new TextView(getContext());
		useName1.setTextSize(textSize-5);
		useName1.setSingleLine(true);
		
		useName2 = new TextView(getContext());
		useName2.setTextSize(textSize-5);
		useName2.setSingleLine(true);
		
		//the first Line
		textViewLine1 = new TextView(getContext());
		//the second line
		textViewLine2 = new TextView(getContext());
		
		textViewfast1 = new TextView(getContext());
		textViewfast1.setText("00:00:000");
		textViewfast1.setTextSize(textSize);
		textViewfast1.setTextColor(Color.YELLOW);
		
		textViewfast2 = new TextView(getContext());
				
		imageViewTui1 = (ImageView)findViewById(R.id.imagetui1);
		imageViewTui1.setVisibility(View.INVISIBLE);

		imageViewTui2 = (ImageView)findViewById(R.id.imagetui2);
		imageViewTui2.setVisibility(View.INVISIBLE);
		
		face1 = (ImageView)findViewById(R.id.face1);
		face1.setVisibility(View.VISIBLE);
		face2 = (ImageView)findViewById(R.id.face2);
		face2.setVisibility(View.VISIBLE);
		
		textViewTime1 = new TextView(getContext());
		textViewTime2 = new TextView(getContext());
		
		textViewLap1 = new TextView(getContext());
		textViewLap2 = new TextView(getContext());
	
		if (AppContext.device == "phone"){
			
			RelativeLayout.LayoutParams paramsjh = new RelativeLayout.LayoutParams((int) (screenWidth*0.07),(int)(screenWidth*0.1));
			paramsjh.setMargins((int)(screenWidth*0.00), (int) (screenHeight*0.00), 0, 0);
			imageBluetooth.setLayoutParams(paramsjh);
			
			RelativeLayout.LayoutParams paramsj = new RelativeLayout.LayoutParams((int) (screenWidth*0.1),(int)(screenWidth*0.1));
			paramsj.setMargins((int)(screenWidth*0.78), (int) (screenHeight*0.00), 0, 0);//0.85
			imageback.setLayoutParams(paramsj);
			
			RelativeLayout.LayoutParams paramsjk = new RelativeLayout.LayoutParams((int) (screenWidth*0.1),(int)(screenWidth*0.1));
			paramsjk.setMargins((int)(screenWidth*0.65), (int) (screenHeight*0.00),0 , 0);//0.73
			imageShare.setLayoutParams(paramsjk);
			//useName1
			setPosi(useName1, "胖胖", Color.YELLOW, 0.64, 0.1538);//169
			//useName1.setTextSize(textSize-5);
			//useName2
			setPosi(useName2, "胖胖", Color.WHITE, 0.64, 0.4267);//5007
			//useName2.setTextSize(textSize-5);
			
			//textViewLine1
			setPosi(textViewLine1, "Line", Color.YELLOW, 0.64, 0.1818);//1868
			//textViewLine2
			setPosi(textViewLine2, "Line", Color.WHITE, 0.64, 0.4567);//4617
			
			//textViewfast1
			RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params1.setMargins((int) (screenWidth*0.64), (int) (screenHeight*0.2797), 0, 0);
			center_end.addView(textViewfast1, params1);
			//textViewfast2
			setPosi(textViewfast2, "00:00:000", Color.WHITE, 0.64, 0.5547);
			
			//imageViewTui1
			RelativeLayout.LayoutParams paramsN = new RelativeLayout.LayoutParams((int)(screenWidth*0.14),(int)(screenWidth*0.13));
			paramsN.setMargins((int) (screenWidth*0.64), (int) (screenHeight*0.1978), 0, 0);
			imageViewTui1.setLayoutParams(paramsN);
			//imageViewTui1.setVisibility(View.VISIBLE);
			//imageViewTui2
			RelativeLayout.LayoutParams paramsM = new RelativeLayout.LayoutParams((int)(screenWidth*0.14),(int)(screenWidth*0.13));
			paramsM.setMargins((int) (screenWidth*0.64), (int) (screenHeight*0.4697), 0, 0);
			imageViewTui2.setLayoutParams(paramsM);
			//imageViewTui2.setVisibility(View.VISIBLE);
			//face1
			RelativeLayout.LayoutParams paramsN1 = new RelativeLayout.LayoutParams((int)(screenWidth*0.18),(int)(screenWidth*0.18));
			paramsN1.setMargins((int) (screenWidth*0.15), (int) (screenHeight*0.1867), 0, 0);//2067
			face1.setLayoutParams(paramsN1);
			//face2
			RelativeLayout.LayoutParams paramsN2 = new RelativeLayout.LayoutParams((int)(screenWidth*0.18),(int)(screenWidth*0.18));
			paramsN2.setMargins((int) (screenWidth*0.15), (int) (screenHeight*0.454), 0, 0);//534
			face2.setLayoutParams(paramsN2);
			
			//textViewTime1
			setPosi(textViewTime1, "00:00:000", Color.YELLOW,  0.64, 0.2178);//2488
			//textViewTime2
			setPosi(textViewTime2, "00:00:000", Color.WHITE, 0.64, 0.4897);
			
			//textViewLap1
			setPosi(textViewLap1, "99", Color.YELLOW, 0.64,0.2487);//2887
			//textViewLap2
			setPosi(textViewLap2, "99", Color.WHITE, 0.64, 0.5237);
			
			if (AppContext.lanuage) {//zh
					result_r.setBackgroundResource(R.drawable.background_result_rp);
				
					imageback.setBackgroundResource(R.drawable.back_btn);
					imageShare.setBackgroundResource(R.drawable.share);
				}else {
					imageback.setBackgroundResource(R.drawable.back_btne);
					imageShare.setBackgroundResource(R.drawable.sharee);
					
					result_r.setBackgroundResource(R.drawable.background_result_rpe);
				}
			}else if(AppContext.device == "pad"){
				
				RelativeLayout.LayoutParams paramsjh = new RelativeLayout.LayoutParams((int) (screenWidth*0.07),(int)(screenWidth*0.1));
				paramsjh.setMargins((int)(screenWidth*0.00), (int) (screenHeight*0.00), 0, 0);
				imageBluetooth.setLayoutParams(paramsjh);
				
				RelativeLayout.LayoutParams paramsj = new RelativeLayout.LayoutParams((int) (screenWidth*0.1),(int)(screenWidth*0.1));
				paramsj.setMargins((int)(screenWidth*0.85), (int) (screenHeight*0.00), 0, 0);
				imageback.setLayoutParams(paramsj);
				
				RelativeLayout.LayoutParams paramsjk = new RelativeLayout.LayoutParams((int) (screenWidth*0.1),(int)(screenWidth*0.1));
				paramsjk.setMargins((int)(screenWidth*0.73), (int) (screenHeight*0.00),0 , 0);
				imageShare.setLayoutParams(paramsjk);
				//useName1
				setPosi(useName1, "胖胖", Color.YELLOW, 0.64, 0.169);
				useName1.setTextSize(textSize-5);
				
				
				//textViewLine1
				setPosi(textViewLine1, "Line", Color.YELLOW, 0.64, 0.206);//
				
				
				//textViewfast1
				setPosi(textViewfast1, "00:00:000", Color.YELLOW, 0.64, 0.3285);
				//textViewfast2
				setPosi(textViewfast2, "00:00:000", Color.WHITE, 0.64, 0.657);//0.652
				
				//imageViewTui1
				RelativeLayout.LayoutParams paramsN = new RelativeLayout.LayoutParams((int)(screenWidth*0.14),(int)(screenWidth*0.13));
				paramsN.setMargins((int) (screenWidth*0.64), (int) (screenHeight*0.223), 0, 0);
				imageViewTui1.setLayoutParams(paramsN);
				//imageViewTui1.setVisibility(View.VISIBLE);
				//imageViewTui2
				RelativeLayout.LayoutParams paramsM = new RelativeLayout.LayoutParams((int)(screenWidth*0.14),(int)(screenWidth*0.13));
				paramsM.setMargins((int) (screenWidth*0.64), (int) (screenHeight*0.5446), 0, 0);
				imageViewTui2.setLayoutParams(paramsM);
				//imageViewTui2.setVisibility(View.VISIBLE);
				//face1
				RelativeLayout.LayoutParams paramsN1 = new RelativeLayout.LayoutParams((int)(screenWidth*0.18),(int)(screenWidth*0.18));
				paramsN1.setMargins((int) (screenWidth*0.15), (int) (screenHeight*0.2067), 0, 0);
				face1.setLayoutParams(paramsN1);
				//face2
				RelativeLayout.LayoutParams paramsN2 = new RelativeLayout.LayoutParams((int)(screenWidth*0.18),(int)(screenWidth*0.18));
				paramsN2.setMargins((int) (screenWidth*0.15), (int) (screenHeight*0.534), 0, 0);
				face2.setLayoutParams(paramsN2);
				
				//textViewTime1
				setPosi(textViewTime1, "00:00:000", Color.YELLOW,  0.64, 0.2488);
				
				
				//textViewLap1
				setPosi(textViewLap1, "99", Color.YELLOW, 0.64,0.2887);
				//textViewLap2
				setPosi(textViewLap2, "99", Color.WHITE, 0.64, 0.617);//612
				
				if (AppContext.lanuage) {//zh
					
					//useName2
					setPosi(useName2, "胖胖", Color.WHITE, 0.64, 0.498);//493
					useName2.setTextSize(textSize-5);
					
					//textViewLine2
					setPosi(textViewLine2, "Line", Color.WHITE, 0.64, 0.5366);//5316
					
					//textViewTime2
					setPosi(textViewTime2, "00:00:000", Color.WHITE, 0.64, 0.577);//572
					
					result_r.setBackgroundResource(R.drawable.background_resultr);
					
					imageback.setBackgroundResource(R.drawable.back_btn);
					imageShare.setBackgroundResource(R.drawable.share);
				}else {
					//useName2
					setPosi(useName2, "胖胖", Color.WHITE, 0.64, 0.494);//493
					useName2.setTextSize(textSize-5);
					
					//textViewLine2
					setPosi(textViewLine2, "Line", Color.WHITE, 0.64, 0.5316);//5316
					
					//textViewTime2
					setPosi(textViewTime2, "00:00:000", Color.WHITE, 0.64, 0.572);//572
					
					imageback.setBackgroundResource(R.drawable.back_btne);
					imageShare.setBackgroundResource(R.drawable.sharee);
					
					result_r.setBackgroundResource(R.drawable.background_result_re);
				}
			}
		
		boolean flag = false;
			if (RaceActivity.line1FastTime <= RaceActivity.line2FastTime) {
				flag = true;
			}else{
				flag = false;
			}
		
		
		Log.i("result", "RaceActivity.resultFlag"+RaceActivity.resultFlag);
		
		if(RaceActivity.resultFlag == "line1 win and all are tui" || RaceActivity.resultFlag == "line1 win nobody is tui" || RaceActivity.resultFlag =="line1 win and line2 is tui"){
			if(AppContext.useName1 == " "){
				if (AppContext.lanuage) 
					useName1.setText("未设置");
				if (!AppContext.lanuage) 
					useName1.setText("No Set");
			}else{
				useName1.setText(AppContext.useName1);
			}
			if(AppContext.useName2 == " "){
				if (AppContext.lanuage) 
					useName2.setText("未设置");
				if (!AppContext.lanuage) 
					useName2.setText("No Set");
			}else{
				useName2.setText(AppContext.useName2);
			}
		}else{
			if(AppContext.useName2 == " "){
				if (AppContext.lanuage) 
					useName1.setText("未设置");
				if (!AppContext.lanuage) 
					useName1.setText("No Set");
			}else{
				useName1.setText(AppContext.useName2);
			}
			if(AppContext.useName1 == " "){
				if (AppContext.lanuage) 
					useName2.setText("未设置");
				if (!AppContext.lanuage) 
					useName2.setText("No Set");
			}else{
				useName2.setText(AppContext.useName1);
			}
		}
		
		if (RaceActivity.resultFlag == "line1 win and all are tui") {
			resultLogic("Line1",RaceActivity.line1TotleTime,RaceActivity.lapsOne,RaceActivity.line1FastTime,true,
					"Line2",RaceActivity.line2TotleTime,RaceActivity.lapsTwo,RaceActivity.line2FastTime,true);
			if (flag) {
				textViewfast1.setTextColor(Color.RED);
			}else{
				textViewfast2.setTextColor(Color.RED);
			}
			
		}else if(RaceActivity.resultFlag == "line2 win and all are tui"){
			resultLogic("Line2",RaceActivity.line2TotleTime,RaceActivity.lapsTwo,RaceActivity.line2FastTime,true,
					"Line1",RaceActivity.line1TotleTime,RaceActivity.lapsOne,RaceActivity.line1FastTime,true);
			if (flag) {
				textViewfast2.setTextColor(Color.RED);
			}else{
				textViewfast1.setTextColor(Color.RED);
			}
			
		}else if(RaceActivity.resultFlag == "line1 win nobody is tui"){
			resultLogic("Line1",RaceActivity.line1TotleTime,RaceActivity.lapsOne,RaceActivity.line1FastTime,false,
					"Line2",RaceActivity.line2TotleTime,RaceActivity.lapsTwo,RaceActivity.line2FastTime,false);
			if (flag) {
				textViewfast1.setTextColor(Color.RED);
			}else{
				textViewfast2.setTextColor(Color.RED);
			}
			
		}else if(RaceActivity.resultFlag == "line2 win nobody is tui"){
			resultLogic("Line2",RaceActivity.line2TotleTime,RaceActivity.lapsTwo,RaceActivity.line2FastTime,false,
					"Line1",RaceActivity.line1TotleTime,RaceActivity.lapsOne,RaceActivity.line1FastTime,false);
			if (flag) {
				textViewfast2.setTextColor(Color.RED);
			}else{
				textViewfast1.setTextColor(Color.RED);
			}
		}
		else if(RaceActivity.resultFlag == "line2 win and line1 is tui"){
			resultLogic("Line2",RaceActivity.line2TotleTime,RaceActivity.lapsTwo,RaceActivity.line2FastTime,false,
					"Line1",RaceActivity.line1TotleTime,RaceActivity.lapsOne,RaceActivity.line1FastTime,true);
			if (flag) {
				textViewfast2.setTextColor(Color.RED);
			}else{
				textViewfast1.setTextColor(Color.RED);
			}
			
		}else if(RaceActivity.resultFlag == "line1 win and line2 is tui"){
			resultLogic("Line1",RaceActivity.line1TotleTime,RaceActivity.lapsOne,RaceActivity.line1FastTime,false,
					"Line2",RaceActivity.line2TotleTime,RaceActivity.lapsTwo,RaceActivity.line2FastTime,true);
			if (flag) {
				textViewfast1.setTextColor(Color.RED);
			}else{
				textViewfast2.setTextColor(Color.RED);
			}
		}
		
		
		
		dialog = this;
		//临时注释
	      
	     //mapButton =  (ImageButton) findViewById(R.id.mapid);
	    /* RelativeLayout.LayoutParams mapLayoutParams =(android.widget.RelativeLayout.LayoutParams) mapButton.getLayoutParams();
	     mapLayoutParams.width = screenWidth*3/5;
	     mapButton.setLayoutParams(mapLayoutParams);*/
	     
		//底部logo
	     /*ImageButton logButton =  (ImageButton) findViewById(R.id.logid);
	     RelativeLayout.LayoutParams logLayoutParams =(android.widget.RelativeLayout.LayoutParams) logButton.getLayoutParams();
	     logLayoutParams.width = screenWidth*1/3;
	     logLayoutParams.height = screenWidth*1/10;
	     logButton.setLayoutParams(logLayoutParams);*/
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1202) {
					imageShare.setAlpha(255);
					imageShare.setEnabled(true);
				}
				super.handleMessage(msg);
			}
		};
		
		dialogWindow = dialog.getWindow();
		
		
		new Thread(){
			public void run() {
				while(flag1){
					
					try {
						sleep(20);
						
						if(theActivity.screenShot(dialogWindow)){
	 						flag1 = false;
	 						mHandler.sendEmptyMessage(1202);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
				
				}
			};
		}.start();
		
		
	     
	     
	}

	
	private void resultLogic(String line1,long totletime1,int laps1,long fasttime1,boolean visible1,String line2,long totletime2,int laps2,long fasttime2,boolean visible2) {

		Log.i("result", "line1:"+line1+" fasttime1:"+fasttime1);
		
		if (line1.equals("Line2")) {
			Log.i("result", "YES:"+line1);
			face1.setBackgroundResource(R.drawable.blueface);
			face2.setBackgroundResource(R.drawable.redface);
		}
		//useName1.setText(name1);
		textViewLine1.setText(line1);
		textViewTime1.setText(timeFormat(totletime1));
		textViewLap1.setText(String.valueOf(laps1));
		textViewfast1.setText(timeFormat(fasttime1));
		if(visible1){
			textViewTime1.setVisibility(View.INVISIBLE);
			imageViewTui1.setVisibility(View.VISIBLE);
		}
		
		//useName2.setText(name2);
		textViewLine2.setText(line2);
		textViewTime2.setText(timeFormat(totletime2));
		textViewLap2.setText(String.valueOf(laps2));
		textViewfast2.setText(timeFormat(fasttime2));
		if(visible2){
			textViewTime2.setVisibility(View.INVISIBLE);
			imageViewTui2.setVisibility(View.VISIBLE);
		}
			
	}
	
	private void setPosi(TextView textView,String string,int colors,Double leftPer,Double rightPer) {
		textView.setText(string);
		textView.setTextSize(textSize);
		textView.setTextColor(colors);
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params1.setMargins((int) (screenWidth*leftPer), (int) (screenHeight*rightPer), 0, 0);
		center_end.addView(textView, params1);
	}
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
		//"0" filling
		for (int i = 0; i <(3-z.length()); i++) {
			z = "0"+z;
		}
		for (int i = 0; i <(2-y.length()); i++) {
			y = "0"+y;
		}
		for (int i = 0; i <(2-x.length()); i++) {
			x = "0"+x;
		}
		String s = x+":"+y+":"+z;
		//s=s.substring(0, 8);
		return s;
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0 == imageShare){
			//theBar.setVisibility(View.VISIBLE);
			
			Log.i("result", "111111111.."+RaceActivity.resultFlag);
			
			//theActivity.share(RaceActivity.theWindow11,dialogWindow);
			theActivity.theShare();
		}else if (arg0 == imageback) {
			
			/*textViewLine1 = null;
			textViewTime1= null;
			 textViewLap1 = null;
			 textViewfast1 = null;
			 imageViewTui1 = null;
			
			 imageShare = null;
			 imageback = null;
			//ProgressBar theBar;
			
			 textViewLine2 = null;
			 textViewTime2 = null;
			 textViewLap2 = null;
			 textViewfast2 = null;
			 useName1 = null;
			 useName2 = null;
			 imageViewTui2 = null;
			
			 mapButton = null;*/
			
			dismiss();
			theHandler.sendEmptyMessage(1011);
		}
	}
	


}
