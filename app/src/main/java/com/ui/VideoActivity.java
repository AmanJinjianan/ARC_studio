package com.ui;

import java.io.IOException;

import com.bluetoothARC.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class VideoActivity extends Activity {

	private SurfaceView sv;
	private SurfaceHolder holder;
	private Callback surfaceHolderCallback = new Callback() {
		
		private MediaPlayer mp;
		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			mp.release();
			Log.i("niangde", "surfaceDestroyed....");
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			mp = MediaPlayer.create(VideoActivity.this, R.raw.arc_video);
			mp.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					Log.i("niangde", "over....");
					Intent intent = new Intent(VideoActivity.this,ScanActivity.class);
					startActivity(intent);
					VideoActivity.this.finish();
				}
			});
			//mp.setOnC
			try {
				mp.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mp.setDisplay(holder);
			mp.start();
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}
	};
	
	AudioManager audioManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		//默认隐藏虚拟按键
		//getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION); 
		//禁止息屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		System.gc();
		
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		if(!(MenuActivity.isVoice))
		audioManager.setStreamMute(AudioManager.STREAM_MUSIC , true);
		 
		sv= new SurfaceView(this);
		sv.setOnClickListener(new MyDoubleClick());
		
		holder = sv.getHolder();
		holder.addCallback(surfaceHolderCallback);
		
		setContentView(sv);
		 if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
		   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		 /*if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
			   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);*/
		
	}
	int count=0;
	long firClick=0,secClick=0;
	class MyDoubleClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			
		            count++;  
		            if(count == 1){  
		               firClick = System.currentTimeMillis();  
		                  
		            } else if (count == 2){  
		                secClick = System.currentTimeMillis();  
		                if(secClick - firClick < 1000){
		                	Log.i("niangde", "双击。。...");
							Intent intent = new Intent(VideoActivity.this,ScanActivity.class);
							startActivity(intent);
							VideoActivity.this.finish();
		                    //双击事件  
		                }  
		                count = 0;  
		                firClick = 0;  
		                secClick = 0;  
		                  
		            }  
		       
		}
		
	}
	@Override
	protected void onPause() {
new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				audioManager.setStreamMute(AudioManager.STREAM_MUSIC , false);
			}
		}, 200);
		super.onPause();
	}
	@Override
	protected void onResume() {
		if (!MenuActivity.isVoice) 
		audioManager.setStreamMute(AudioManager.STREAM_MUSIC , true);
		super.onResume();
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
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
	@Override
	protected void onDestroy() {
		new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						audioManager.setStreamMute(AudioManager.STREAM_MUSIC , false);
					}
				}, 200);
		
		
		super.onDestroy();
	}

	
}
