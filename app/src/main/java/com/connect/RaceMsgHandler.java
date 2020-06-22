package com.connect;

import java.lang.ref.WeakReference;

import com.ui.RaceActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * message handler
 * @author likun@stu.zzu.edu.cn
 *
 */
public class RaceMsgHandler extends Handler{
	public static final int MSG_DATA = 1;
	WeakReference<RaceActivity> raceActivity;
	
	public RaceMsgHandler(RaceActivity activity){
		raceActivity  =  new WeakReference<RaceActivity>(activity);
	}
	
	@Override
	public void handleMessage(Message msg){
		RaceActivity theActivity = raceActivity.get();
		
		if(theActivity == null)
			return;
		switch (msg.what) {
		case MSG_DATA:
			//receive data
			Bundle b= msg.getData();
			String data = b.getString("data");
			//data format converse
			data = theActivity.formatConverse(data.getBytes());
			
			long deltaT = 0;  // initialize problem?
			long min =0;
			long sec =0;
			long milli =0;
			
			if(data.startsWith("AA")){
				data = data.replaceAll("AA", "");
				if(data.equals("17")){			//11
					theActivity.msgPosi_11(deltaT,min,sec,milli);
				}else if(data.equals("33")){	//12
					theActivity.msgPosi_12();
				}else if(data.equals("18")){	//21
					theActivity.msgPosi_21(deltaT,min,sec,milli);
				}else if(data.equals("34")){	//22
					theActivity.msgPosi_22();
				}
			}
			else{
				theActivity.msgAccChange(data);
			}
			
			break;
			
		default:
			break;
		}
	}
}
