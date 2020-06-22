package com.connect;

import com.connect.BLEconfig;

import android.util.Log;

public class MyReConnThread extends Thread{

	BLEconfig mBLE;
	
	public volatile boolean stop = false;
	//volatile : every time use this variable ,system will check this value
	public boolean theThreadState = true;
	public MyReConnThread(){
		mBLE = BLEconfig.getInstance();
	}
	
	@Override
	public void run(){
		while(!stop){
			
			if(theThreadState){
			mBLE.reConn();
			Log.i("address", "000000000000000000...");
			}
			Log.i("address", "run!!!!!!!!!");
			//Wait Time
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
