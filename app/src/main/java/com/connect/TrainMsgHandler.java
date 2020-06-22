package com.connect;

import java.lang.ref.WeakReference;

import com.ui.TrainActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * message handler
 * @author likun@stu.zzu.edu.cn
 *
 */
public class TrainMsgHandler extends Handler{
	public static final int MSG_DATA = 1;
	WeakReference<TrainActivity> trainActivity;
	public TrainMsgHandler(TrainActivity activity){
		trainActivity  =  new WeakReference<TrainActivity>(activity);
	}
	
	@Override
	public void handleMessage(Message msg){
		TrainActivity theActivity = trainActivity.get();
		
		if(theActivity == null)
			return;
		switch (msg.what) {
		case MSG_DATA:
			//receive data
			Bundle b= msg.getData();
			String data = b.getString("data");
			//data format converse
			data = formatConverse(data.getBytes());
			
			long deltaT = 0;  // initialize problem?
			long min =0;
			long sec =0;
			long milli =0;
			
			if(data.startsWith("AA")){
				
				data = data.replaceAll("AA", "");
				
				if(data.equals("17")){			//11
					Log.i("address", "hello1");
					theActivity.msgPosi_11(deltaT,min,sec,milli);
				}else if(data.equals("33")){	//12
					Log.i("address", "hello2");
					theActivity.msgPosi_12();
				}else if(data.equals("18")){	//21
					Log.i("address", "hello3");
					theActivity.msgPosi_21(deltaT,min,sec,milli);
				}else if(data.equals("34")){	//22
					Log.i("address", "hello4");
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
	
	/**
	 * convert data format
	 * @param bytes
	 * @return
	 */
	private String formatConverse(byte[] bytes)
	{
		String result = "";
		for(int i=0;i<bytes.length;i++)
		{
			System.out.println(bytes[i]);
		}
		if(6 == bytes.length || 4 == bytes.length){ //ori is 6 ,real is 4
			result = "AA"+Byte.toString(bytes[3]);
		}
		else if(5 == bytes.length || 3 == bytes.length){ // ori is 5 , real is 3
			result = Byte.toString(bytes[1]) + " "+Byte.toString(bytes[2]);
		}
		return result;
	}
}