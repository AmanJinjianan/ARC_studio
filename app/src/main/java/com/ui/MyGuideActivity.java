package com.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bluetoothARC.R;

public class MyGuideActivity extends Activity implements OnClickListener {

	private int screenWidth, screenHeight;
	//private DisplayMetrics dm;
	ImageButton jButton;
	private ImageButton settingBtn, guidingBtn, disBtn, chinaBtn, engBtn,
			confirmBtn;
	ImageView imageView1, imageView2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// NO_TITLE
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// FULLSCREEN
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// hide virtual button
		// getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		// KEEP_SCREEN_ON
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_myguide);

		//dm = new DisplayMetrics();
		//getWindowManager().getDefaultDisplay().getMetrics(dm);

		screenHeight = AppContext.screenHeight;
		screenWidth = AppContext.screenWidth;

		float per = (float) screenWidth / (float) screenHeight;
		
		//guidingBtn
		guidingBtn = (ImageButton) findViewById(R.id.guide_guidingBtn);
		guidingBtn.setOnClickListener(this);
		
		// setting_btn
		settingBtn = (ImageButton) findViewById(R.id.guide_settingBtn);
		settingBtn.setOnClickListener(this);
		
		// disBtn = (ImageButton) findViewById(R.id.imageButton_en);
		disBtn = (ImageButton) findViewById(R.id.imageButton_exit);
		disBtn.setOnClickListener(this);
		
		confirmBtn = (ImageButton) findViewById(R.id.imageButton_yes);
		confirmBtn.setOnClickListener(this);
		
		if (AppContext.device == "phone"){
			
				//guidingBtn
			RelativeLayout.LayoutParams params12 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.13), (int) (screenWidth * 0.16));
				params12.setMargins((int) (screenWidth * 4 / 20),(int) ((screenHeight * 1 / 16)), 0, 0);
				guidingBtn.setLayoutParams(params12);
				
				// setting_btn
				RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.13), (int) (screenWidth * 0.16));
				params1.setMargins((screenWidth * 1 / 20), screenHeight * 1 / 16, 0, 0);
				settingBtn.setLayoutParams(params1);
				
				// disBtn 
				RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(screenWidth * 1 / 9, screenWidth * 1 / 9);
				params3.setMargins((int) (screenWidth * 0.88),(int) ((screenHeight-screenWidth/0.84)/2)+39, 0, 0);
				disBtn.setLayoutParams(params3);
				
				//confirm_btn
				FrameLayout.LayoutParams params4 = new FrameLayout.LayoutParams(screenWidth * 1 / 4, screenWidth * 1 / 9);
				params4.setMargins((int)(screenWidth * 3 / 8*0.95), (int)(screenWidth/0.84*0.733), 0,0);
				confirmBtn.setLayoutParams(params4);
			
			if (AppContext.lanuage) {//zh
					findViewById(R.id.myguide_back).setBackgroundResource(R.drawable.bg_guide);
					guidingBtn.setBackgroundResource(R.drawable.guiding);
					settingBtn.setBackgroundResource(R.drawable.setting);
					confirmBtn.setBackgroundResource(R.drawable.confirm);
					
					//chinese_view
				setPosiForPhoneZh(chinaBtn, R.id.imageButton_cn, (float) 3.14, (float)0, (float)1.3);
					//English_view
				setPosiForPhoneZh(engBtn, R.id.imageButton_en, (float) 10.44, (float) 0, (float)1.3);
					
				}else {
					findViewById(R.id.myguide_back).setBackgroundResource(R.drawable.bg_guide_e);
					guidingBtn.setBackgroundResource(R.drawable.guidinge);
					settingBtn.setBackgroundResource(R.drawable.settinge);
					confirmBtn.setBackgroundResource(R.drawable.confirme);
					
					//chinese_view
				setPosiForPhoneEn(chinaBtn, R.id.imageButton_cn, (float)0.20, (float) 0.5, (float)1.3);
					//English_view
				setPosiForPhoneEn(engBtn, R.id.imageButton_en, (float) 0.20, (float) 0.23, (float)1.3);
				}
			}else if(AppContext.device == "pad"){
				
				//guidingBtn
			RelativeLayout.LayoutParams params12 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.13), (int) (screenWidth * 0.16));
				params12.setMargins((int) (screenWidth * 4 / 20),(int) ((screenHeight * 1 / 20)), 0, 0);
				guidingBtn.setLayoutParams(params12);
				
				// setting_btn
			RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.13), (int) (screenWidth * 0.16));
				params1.setMargins((screenWidth * 1 / 20), screenHeight * 1 / 20, 0, 0);
				settingBtn.setLayoutParams(params1);
				
				// disBtn 
				FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(screenWidth * 1 / 9, screenWidth * 1 / 9);
				params3.setMargins((int) (screenWidth * 0.78),(int) ((screenHeight * 0.146)), 0, 0);
				disBtn.setLayoutParams(params3);
				
				//confirm_btn
				FrameLayout.LayoutParams params4 = new FrameLayout.LayoutParams(screenWidth * 1 / 4, screenWidth * 1 / 9);
				params4.setMargins(screenWidth * 3 / 8, (int) (screenHeight * 0.6616), 0,0);
				confirmBtn.setLayoutParams(params4);
				
				if (AppContext.lanuage) {//zh
					findViewById(R.id.myguide_back).setBackgroundResource(R.drawable.background_guide);
					guidingBtn.setBackgroundResource(R.drawable.guiding);
					settingBtn.setBackgroundResource(R.drawable.setting);
					confirmBtn.setBackgroundResource(R.drawable.confirm);
					
					//chinese_view
					setPosi(chinaBtn, R.id.imageButton_cn, (float) 4.8, (float) 9, 1);
					//English_view
					setPosi(engBtn, R.id.imageButton_en, (float) 10.7, (float) 9, 1);
					
				}else {
					findViewById(R.id.myguide_back).setBackgroundResource(R.drawable.background_guidee);
					guidingBtn.setBackgroundResource(R.drawable.guidinge);
					settingBtn.setBackgroundResource(R.drawable.settinge);
					confirmBtn.setBackgroundResource(R.drawable.confirme);
					
					//chinese_view
					setPosi(chinaBtn, R.id.imageButton_cn, (float) 5.1, (float) 13.3, 1);
					//English_view
					setPosi(engBtn, R.id.imageButton_en, (float) 5.1, (float) 8.7, 1);
				}
			}

		
		/*
		 * if (per > 0.7) { if(!AppContext.lanuage){
		 * imageView1.setBackgroundResource(R.drawable.guide_train1_ae);
		 * ViewPosi(imageView1);
		 * imageView2.setBackgroundResource(R.drawable.guide_train2_ae);
		 * ViewPosi(imageView2); }else{
		 * imageView1.setBackgroundResource(R.drawable.guide_train1_a);
		 * ViewPosi(imageView1);
		 * imageView2.setBackgroundResource(R.drawable.guide_train2_a);
		 * ViewPosi(imageView2); } }else { if(!AppContext.lanuage){
		 * imageView1.setBackgroundResource(R.drawable.guide_train1e);
		 * imageView2.setBackgroundResource(R.drawable.guide_train2e); }else{
		 * imageView1.setBackgroundResource(R.drawable.guide_train1);
		 * imageView2.setBackgroundResource(R.drawable.guide_train2); }
		 * 
		 * }
		 */

		// jButton = (ImageButton) findViewById(R.id.imageButton1);
		// jButton.setOnClickListener(new OnClickListener() {
		/*
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub Guide_train.this.finish(); } });
		 */

	}

	public void setPosi(ImageButton imageButton, int id, float left, float top,float per) {
		imageButton = (ImageButton) findViewById(id);
		FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams((int) (screenWidth * 0.247*per), (int) (screenWidth * 0.1862*per));
		params3.setMargins((int) (screenWidth * left / 20), (int) (screenHeight* top *0.0376), 0, 0);
		imageButton.setLayoutParams(params3);

		imageButton.setOnClickListener(this);

	}

	public void setPosiForPhoneZh(ImageButton imageButton, int id, float left, float top,float per) {
		imageButton = (ImageButton) findViewById(id);
		FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams((int) (screenWidth * 0.247*per), (int) (screenWidth * 0.1862*per));
		params3.setMargins((int) (screenWidth * left / 20*0.95), (int) (screenWidth/0.84* 0.24), 0, 0);
		imageButton.setLayoutParams(params3);
		imageButton.setOnClickListener(this);
	}
	public void setPosiForPhoneEn(ImageButton imageButton, int id, float left, float top,float per) {
		imageButton = (ImageButton) findViewById(id);
		FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams((int) (screenWidth * 0.247*per), (int) (screenWidth * 0.1862*per));
		params3.setMargins((int) (screenWidth * left), (int) (screenWidth/0.84* top), 0, 0);
		imageButton.setLayoutParams(params3);
		imageButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButton_exit:
			this.finish();
			break;
		case R.id.imageButton_cn:
			Uri uri = Uri.parse("http://v.youku.com/v_show/id_XMTYxNTc1NjQyMA==.html?from=s1.8-1-1.2&spm=a2h0k.8191407.0.0");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			break;
		case R.id.imageButton_en:
			Uri uri1 = Uri.parse("http://v.youku.com/v_show/id_XMTYxNTc1NjQyMA==.html?from=s1.8-1-1.2&spm=a2h0k.8191407.0.0");
			Intent intent2 = new Intent(Intent.ACTION_VIEW, uri1);
			startActivity(intent2);
			break;
		case R.id.imageButton_yes:
			this.finish();
			break;
		case R.id.guide_settingBtn:
			
			Intent intent1 = new Intent(MyGuideActivity.this,
					MySettingActivity.class);
			startActivity(intent1);
			finish();
			Log.i("info", "guide_settingBtn");
			break;
		case R.id.guide_guidingBtn:
			
			Toast.makeText(this, R.string.guiding, Toast.LENGTH_SHORT).show(); 
			//Toast.makeText(this, "Honey,鐜板湪宸茬粡鏄柊鎵嬫寚瀵奸〉闈簡鍛�", Toast.LENGTH_SHORT).show(); 
			/*Intent intent11 = new Intent(MyGuideActivity.this,
					MyGuideActivity.class);
			startActivity(intent11);*/
			break;
		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
