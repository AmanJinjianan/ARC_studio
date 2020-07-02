package com.ui;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluetoothARC.R;

public class MyResultDialogTrain extends Dialog implements View.OnClickListener{
	
	private TextView textViewLine1;
	private TextView textViewTime1;
	private TextView textViewLap1;
	private TextView textViewfast1;
	private ImageView face1;
	private ImageView light_logo;
	private ImageButton imageback,imageBluetooth;
	//ProgressBar theBar;
	
	private TextView textViewLine2;
	private TextView textViewTime2;
	private TextView textViewLap2;
	private TextView textViewfast2;
	private TextView useName1,useName2;
	private ImageView face2;
	
	private ImageButton mapButton;

	public MyResultDialogTrain(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	//private TrainActivity theTrainActivity = new TrainActivity();
	public MyResultDialogTrain(Context context, int theme) {
		super(context, theme);
	}
	
	/*private TextView endLineOne,endLineTwo;
	private TextView endTimeOne,endTimeTwo;
	private TextView endLapOne,endLapTwo;*/
	//RaceResult raceResult;
	TrainActivity trainActivity;
	
	RelativeLayout center_end;
	
	int screenWidth = AppContext.screenWidth;
	int screenHeight = AppContext.screenHeight;
	float value = AppContext.scaledDensity;
	
	float scaledDensity = AppContext.scaledDensity;
	float textSize = screenWidth/27/scaledDensity;
	
	TrainActivity theActivity;
	Dialog dialog;
	boolean result;
	
	public int y = 0;
	
	Handler mHandler;
	private boolean lightState = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		System.gc();
		
		setContentView(R.layout.result_train);
		
		
		theActivity = new TrainActivity();
		
		center_end = (RelativeLayout) findViewById(R.id.center_end110_train);
		
		//限定中间核心部位宽高
		/*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth,(int)(screenWidth*0.67));
		params.setMargins((int) (screenWidth*0.00), (int) (screenHeight*0.00), 0, 0);
		center_end.setLayoutParams(params);*/
		
		imageBluetooth = (ImageButton) findViewById(R.id.result_train_bluetooth);
		
		imageback = (ImageButton)findViewById(R.id.imageback_train);
		imageback.setOnClickListener(this);
		
		useName1 = new TextView(getContext());
		useName1.setSingleLine(true);
		useName1.setTextSize(textSize-5);
		useName2 = new TextView(getContext());
		useName2.setSingleLine(true);
		useName2.setTextSize(textSize-5);
		
		//the first Line
		textViewLine1 = new TextView(getContext());
		//the second line
		textViewLine2 = new TextView(getContext());
		
		face1 = (ImageView)findViewById(R.id.face1_train);
		face1.setVisibility(View.VISIBLE);
		face2 = (ImageView)findViewById(R.id.face2_train);
		face2.setVisibility(View.VISIBLE);
		
		textViewTime1 = new TextView(getContext());
		textViewTime2 = new TextView(getContext());
		
		textViewLap1 = new TextView(getContext());
		textViewLap2 = new TextView(getContext());
		
		textViewfast1 = new TextView(getContext());
		textViewfast2 = new TextView(getContext());


		if (AppContext.device == "phone"){
			
			RelativeLayout.LayoutParams paramsjh = new RelativeLayout.LayoutParams((int) (screenWidth*0.07),(int)(screenWidth*0.1));
			paramsjh.setMargins((int)(screenWidth*0.00), (int) (screenHeight*0.00), 0, 0);
			imageBluetooth.setLayoutParams(paramsjh);
			
			RelativeLayout.LayoutParams paramsj = new RelativeLayout.LayoutParams((int) (screenWidth*0.1),(int)(screenWidth*0.1));
			paramsj.setMargins((int)(screenWidth*0.78), (int) (screenHeight*0.00), 0, 0);
			imageback.setLayoutParams(paramsj);
			
			setPosi(useName1, "胖胖", Color.YELLOW, 0.64, 0.158);//1518
			setPosi(useName2, "胖胖", Color.WHITE, 0.64, 0.613);//4307
			
			setPosi(textViewLine1, "Line", Color.YELLOW, 0.64, 0.215);
			setPosi(textViewLine2, "Line", Color.WHITE, 0.64, 0.670);
			
			RelativeLayout.LayoutParams paramsN1 = new RelativeLayout.LayoutParams((int)(screenWidth*0.18),(int)(screenWidth*0.18));
			paramsN1.setMargins((int) (screenWidth*0.15), (int) (screenWidth/0.97*0.24), 0, 0);
			face1.setLayoutParams(paramsN1);

			RelativeLayout.LayoutParams paramsN2 = new RelativeLayout.LayoutParams((int)(screenWidth*0.18),(int)(screenWidth*0.18));
			paramsN2.setMargins((int) (screenWidth*0.15), (int) (screenWidth/0.97*0.69), 0, 0);
			face2.setLayoutParams(paramsN2);
			
			setPosi(textViewTime1, "00:00:000", Color.YELLOW,  0.64, 0.27);
			setPosi(textViewTime2, "00:00:000", Color.WHITE, 0.64, 0.733);
			
			setPosi(textViewLap1, "99", Color.YELLOW, 0.64,0.333);
			setPosi(textViewLap2, "99", Color.WHITE, 0.64, 0.789);
			
			setPosi(textViewfast1, "00:00:000", Color.YELLOW, 0.64, 0.389);
			setPosi(textViewfast2, "00:00:000", Color.WHITE, 0.64, 0.848);
			if (AppContext.lanuage) {//zh
				findViewById(R.id.result_t).setBackgroundResource(R.drawable.bg_result_train);
				findViewById(R.id.center_end110_train).setBackgroundResource(R.drawable.bg_mid_train_result);
				findViewById(R.id.iv_result_train_logo).setBackgroundResource(R.drawable.logo);
				imageback.setBackgroundResource(R.drawable.back_btn);
			} else {
				findViewById(R.id.result_t).setBackgroundResource(R.drawable.bg_result_train_e);
				findViewById(R.id.center_end110_train).setBackgroundResource(R.drawable.bg_mid_train_result_e);
				findViewById(R.id.iv_result_train_logo).setBackgroundResource(R.drawable.logo2);
				imageback.setBackgroundResource(R.drawable.back_btne);
			}
			}
		else if(AppContext.device == "pad"){
				
				RelativeLayout.LayoutParams paramsjh = new RelativeLayout.LayoutParams((int) (screenWidth*0.07),(int)(screenWidth*0.1));
				paramsjh.setMargins((int)(screenWidth*0.00), (int) (screenHeight*0.00), 0, 0);
				imageBluetooth.setLayoutParams(paramsjh);
				
				RelativeLayout.LayoutParams paramsj = new RelativeLayout.LayoutParams((int) (screenWidth*0.1),(int)(screenWidth*0.1));
				paramsj.setMargins((int)(screenWidth*0.85), (int) (screenHeight*0.00), 0, 0);
				imageback.setLayoutParams(paramsj);
				
				setPosi(useName1, "胖胖", Color.YELLOW, 0.64, 0.169);
				setPosi(useName2, "胖胖", Color.WHITE, 0.64, 0.493);
				
				setPosi(textViewLine1, "Line", Color.YELLOW, 0.64, 0.206);
				setPosi(textViewLine2, "Line", Color.WHITE, 0.64, 0.5346);//5316
				
				RelativeLayout.LayoutParams paramsN1 = new RelativeLayout.LayoutParams((int)(screenWidth*0.18),(int)(screenWidth*0.18));
				paramsN1.setMargins((int) (screenWidth*0.15), (int) (screenHeight*0.2067), 0, 0);
				face1.setLayoutParams(paramsN1);
				RelativeLayout.LayoutParams paramsN2 = new RelativeLayout.LayoutParams((int)(screenWidth*0.18),(int)(screenWidth*0.18));
				paramsN2.setMargins((int) (screenWidth*0.15), (int) (screenHeight*0.534), 0, 0);
				face2.setLayoutParams(paramsN2);
				
				setPosi(textViewTime1, "00:00:000", Color.YELLOW,  0.64, 0.2468);
				setPosi(textViewTime2, "00:00:000", Color.WHITE, 0.64, 0.576);//572
				
				setPosi(textViewLap1, "99", Color.YELLOW, 0.64,0.2887);
				setPosi(textViewLap2, "99", Color.WHITE, 0.64, 0.616);//612
				
				setPosi(textViewfast1, "00:00:000", Color.YELLOW, 0.64, 0.3285);
				setPosi(textViewfast2, "00:00:000", Color.WHITE, 0.64, 0.657);//652
				
				if (AppContext.lanuage) {//zh
					findViewById(R.id.result_t).setBackgroundResource(R.drawable.bg_result_train);
					findViewById(R.id.center_end110_train).setBackgroundResource(R.drawable.bg_mid_train_result);
					imageback.setBackgroundResource(R.drawable.back_btn);
				}else {
					findViewById(R.id.result_t).setBackgroundResource(R.drawable.bg_result_train_e);
					findViewById(R.id.center_end110_train).setBackgroundResource(R.drawable.bg_mid_train_result_e);
					imageback.setBackgroundResource(R.drawable.back_btne);
				}
			}
		
		boolean flag = false;
			if (TrainActivity.line1FastTime <= TrainActivity.line2FastTime) {
				flag = true;
			}else{
				flag = false;
			}
		
		
		
		
		if(TrainActivity.resultFlag == "line1 win and all are tui" || TrainActivity.resultFlag == "line1 win nobody is tui" || TrainActivity.resultFlag =="line1 win and line2 is tui"){
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
		
		if (TrainActivity.resultFlag == "line1 win and all are tui") {
			resultLogic("Line1",TrainActivity.line1TotleTime,TrainActivity.lapsOne,TrainActivity.line1FastTime,true,
					"Line2",TrainActivity.line2TotleTime,TrainActivity.lapsTwo,TrainActivity.line2FastTime,true);
			if (flag) {
				textViewfast1.setTextColor(Color.RED);
			}else{
				textViewfast2.setTextColor(Color.RED);
			}
			
		}else if(TrainActivity.resultFlag == "line2 win and all are tui"){
			resultLogic("Line2",TrainActivity.line2TotleTime,TrainActivity.lapsTwo,TrainActivity.line2FastTime,true,
					"Line1",TrainActivity.line1TotleTime,TrainActivity.lapsOne,TrainActivity.line1FastTime,true);
			if (flag) {
				textViewfast2.setTextColor(Color.RED);
			}else{
				textViewfast1.setTextColor(Color.RED);
			}
			
		}else if(TrainActivity.resultFlag == "line1 win nobody is tui"){
			resultLogic("Line1",TrainActivity.line1TotleTime,TrainActivity.lapsOne,TrainActivity.line1FastTime,false,
					"Line2",TrainActivity.line2TotleTime,TrainActivity.lapsTwo,TrainActivity.line2FastTime,false);
			if (flag) {
				textViewfast1.setTextColor(Color.RED);
			}else{
				textViewfast2.setTextColor(Color.RED);
			}
			
		}else if(TrainActivity.resultFlag == "line2 win nobody is tui"){
			resultLogic("Line2",TrainActivity.line2TotleTime,TrainActivity.lapsTwo,TrainActivity.line2FastTime,false,
					"Line1",TrainActivity.line1TotleTime,TrainActivity.lapsOne,TrainActivity.line1FastTime,false);
			if (flag) {
				textViewfast2.setTextColor(Color.RED);
			}else{
				textViewfast1.setTextColor(Color.RED);
			}
		}
		else if(TrainActivity.resultFlag == "line2 win and line1 is tui"){
			resultLogic("Line2",TrainActivity.line2TotleTime,TrainActivity.lapsTwo,TrainActivity.line2FastTime,false,
					"Line1",TrainActivity.line1TotleTime,TrainActivity.lapsOne,TrainActivity.line1FastTime,true);
			if (flag) {
				textViewfast2.setTextColor(Color.RED);
			}else{
				textViewfast1.setTextColor(Color.RED);
			}
			
		}else if(TrainActivity.resultFlag == "line1 win and line2 is tui"){
			resultLogic("Line1",TrainActivity.line1TotleTime,TrainActivity.lapsOne,TrainActivity.line1FastTime,false,
					"Line2",TrainActivity.line2TotleTime,TrainActivity.lapsTwo,TrainActivity.line2FastTime,true);
			if (flag) {
				textViewfast1.setTextColor(Color.RED);
			}else{
				textViewfast2.setTextColor(Color.RED);
			}
		}
		
		
		
		dialog = this;
		//临时注释
	      
	     //mapButton =  (ImageButton) findViewById(R.id.mapid_train);
	    /* RelativeLayout.LayoutParams mapLayoutParams =(android.widget.RelativeLayout.LayoutParams) mapButton.getLayoutParams();
	     mapLayoutParams.width = screenWidth*3/5;
	     mapButton.setLayoutParams(mapLayoutParams);*/
	     
		//底部logo
	     /*ImageButton logButton =  (ImageButton) findViewById(R.id.logid_train);
	     RelativeLayout.LayoutParams logLayoutParams =(android.widget.RelativeLayout.LayoutParams) logButton.getLayoutParams();
	     logLayoutParams.width = screenWidth*1/3;
	     logLayoutParams.height = screenWidth*1/10;
	     logButton.setLayoutParams(logLayoutParams);*/
	    
	}

	
	private void resultLogic(String line1,long totletime1,int laps1,long fasttime1,boolean visible1,String line2,long totletime2,int laps2,long fasttime2,boolean visible2) {

		if (line1.equals("Line2")) {
			Log.i("resultF", "YESS:"+line1);
			face1.setBackgroundResource(R.drawable.blueface);
			face2.setBackgroundResource(R.drawable.redface);
		}
		Log.i("result", "totletime1:"+timeFormat(totletime1)+" textViewLap1:"+String.valueOf(laps1)+" textViewfast1:"+textViewfast1);

		//useName1.setText(name1);
		textViewLine1.setText(line1);
		textViewTime1.setText(timeFormat(totletime1));
		textViewLap1.setText(String.valueOf(laps1));
		textViewfast1.setText(timeFormat(fasttime1));
		
		
		//useName2.setText(name2);
		textViewLine2.setText(line2);
		Log.i("result", "totletime2:"+timeFormat(totletime2)+" textViewLap2:"+String.valueOf(laps2)+" textViewfast2:"+textViewfast2);
		if (String.valueOf(laps2).equals("-1")) {
			Log.i("result","***-1");
			textViewTime2.setText("60:00:000");
			textViewLap2.setText("0");
			textViewfast2.setText("60:00:000");
		}else{
			textViewTime2.setText(timeFormat(totletime2));
			textViewLap2.setText(String.valueOf(laps2));
			textViewfast2.setText(timeFormat(fasttime2));
		}
		
			
	}
	
	private void setPosi(TextView textView,String string,int colors,Double leftPer,Double topPer) {
		textView.setText(string);
		textView.setTextSize(textSize);
		textView.setTextColor(colors);
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params1.setMargins((int) (screenWidth*leftPer*1.06), (int) (screenWidth/0.97*topPer), 0, 0);
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
		if (arg0 == imageback) {
			
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
		}
	}
	


}
