package com.connect;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.ui.AppContext;
import com.ui.MenuActivity;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo.State;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * service for managing connection and data communication with a GATT server
 * hosted on a given BLE device
 * @author likun@stu.zzu.edu.cn
 *
 */
public class BLEconfig extends Service
{
	MyScanThread theThread1;
	public boolean theState1 = true;

	private String bleName = "YinSuARC";//  
	
	public static final int COMMAND_CONNECT = 0;
	public static final int COMMAND_DISCONNECT = 1;
	public static final int COMMAND_DISCOVER_SERVICE = 2;
	public static final int COMMAND_READ_RSSI = 3;
	public static final int COMMAND_WRITE_CHARACTERISTIC = 4;
	
	public static final String EXTRA_COMMAND = "com.man.fbleclient.EXTRA_COMMAND";
	public static final String CONNECT_ADDRESS = "com.man.fbleclient.CONNECT_ADDRESS";
	public static final String WRITE_VALUE = "com.man.fbleclient.WRITE_VALUE";
	
	public final static String ACTION_STATE_CONNECTED = "com.bluetoothARC.ACTION_STATE_CONNECTED";
	public final static String ACTION_STATE_DISCONNECTED = "com.bluetoothARC.ACTION_STATE_DISCONNECTED";
	public final static String ACTION_SERVICESDISCOVERED_OVER = "com.bluetoothARC.ACTION_SERVICESDISCOVERED_OVER";
	public final static String ACTION_WRITE_DESCRIPTOR_OVER = "com.bluetoothARC.ACTION_WRITE_DESCRIPTOR_OVER";
	public final static String ACTION_WRITE_DATA_OVER = "com.bluetoothARC.ACTION_WRITE_DATA_OVER";
	
	public final static String ACTION_CHARACTER_CHANGE = "com.bluetoothARC.ACTION_CHARACTER_CHANGE";
	public final static String ACTION_CHARACTER_CHANGE2 = "com.bluetoothARC.ACTION_CHARACTER_CHANGE2";
	    
	BluetoothGattService service,service1;
	public BluetoothGattCharacteristic characteristic,characteristicWrite;
	
	private boolean connected_flag = false;
	//Constructor
	public BLEconfig(){}
	
	public static BLEconfig blEconfig = null;
	//Single Instance
	public static BLEconfig getInstance()
	{
		if(blEconfig == null){
			blEconfig = new BLEconfig();
		}
		return blEconfig;
	}
	
	public interface OnConnectListener {
		public void onConnect(BluetoothGatt gatt);
	}
	public interface OnDisconnectListener {
		public void onDisconnect(BluetoothGatt gatt);
	}
	public interface OnServiceDiscoverListener {
		public void onServiceDiscover(BluetoothGatt gatt);
	}
	public interface OnDataAvailableListener {
		public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status);
		public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic);
	}
	//写回调接口
    public interface OnWriteOverCallback {
        public void OnWriteOver(BluetoothGatt gatt, BluetoothGattCharacteristic character, int statue);
    }
    private OnWriteOverCallback onWriteOverCallback;
    public void setOnWriteOverCallback(OnWriteOverCallback l) {
        onWriteOverCallback =l;
    }
    
	/*private OnConnectListener mOnConnectListener;
	private OnDisconnectListener mOnDisconnectListener;
	private OnServiceDiscoverListener mOnServiceDiscoverListener;
	private OnDataAvailableListener mOnDataAvailableListener;*/
	/*public void setOnConnectListener(OnConnectListener l){
		mOnConnectListener = l;
	}
	public void setOnDisconnectListener(OnDisconnectListener l){
		mOnDisconnectListener = l;
	}
	public void setOnServiceDiscoverListener(OnServiceDiscoverListener l){
		mOnServiceDiscoverListener = l;
	}
	public void setOnDataAvailableListener(OnDataAvailableListener l){
		mOnDataAvailableListener = l;
	}*/
	
	public BluetoothAdapter mBluetoothAdapter;
	public static BluetoothGatt mBluetoothGatt;
	public BluetoothGattCharacteristic mBlueGattcharNotify;
	private BluetoothDevice mDevice;	//toString is the MAC of device
	private Thread scanDeviceThread;
	//Handler mHandler;
	
	/*UUID uuid_s=UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
	UUID uuid_w=UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
	UUID uuid_c=UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
	UUID uuid_d=UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");*/
	
	UUID uuid_w=UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
	UUID uuid_s=UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
	UUID uuid_c=UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
	UUID uuid_d=UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
	
	public BluetoothManager mBluetoothManager;
	//public BluetoothAdapter mBluetoothAdapter;
	//private static UUID UUID_SERVER = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    //private static UUID UUID_CHARREAD = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    //private static UUID UUID_CHARWRITE = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    //private static UUID UUID_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
	
	/**
	 * Initializes a reference to the local Bluetooth adapter
	 * @param mBluetoothManager
	 * @return true if success 
	 */
	public boolean initialize(BluetoothManager mBluetoothManager){
	//For API level 18 and above, get a reference to BluetoothAdapter through BluetoothManager
		if (mBluetoothManager == null) {
			Log.e("TAG", "Unable to initialize BluetoothManager.");
            return false;
		}
		
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
            Log.e("TAG", "Unable to obtain a BluetoothAdapter.");
            return false;
        }
		return true;
	}
	
	 public boolean connectBle(BluetoothDevice device) {
	        disConnectedBle();
	        BluetoothDevice  device_tmp = mBluetoothAdapter.getRemoteDevice(device.getAddress());
	        if (device_tmp == null) {
	            return false;
	        }
	        mBluetoothGatt = device_tmp.connectGatt(getApplicationContext(), false, mGattCallback);
	        return true;
	    }
	 public void disConnectedBle() {
	        if (mBluetoothGatt != null) {
	            mBluetoothGatt.disconnect();
	            mBluetoothGatt.close();
	            mBluetoothGatt = null;
	            connected_flag = false;
	        }
	    }
	/**
	 * start scan after 3s, in fact a improve is needed 
	 * this method below waste 3s to show the scanning effect
	 * to improve ,the scanning should begin during three seconds
	 */
	public void deviceScanStart(){
		Log.i("address", "11111111111111111111...");
		mBluetoothAdapter.startLeScan(mLeScanCallback);
		
		try {
			
			Thread.sleep(1800);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		
		if (mDevice == null)
            Log.i("address", "Device not found.  Try again...");
		else{
			Log.i("address", "1212121212121...");
			//mBluetoothGatt = mDevice.connectGatt(AppContext.getInstance(), true, mGattCallback);
			mBluetoothGatt = mDevice.connectGatt(this, true, mGattCallback);
		}
			
	}
	public void reConn() {
		mBluetoothGatt = mDevice.connectGatt(this, true, mGattCallback);
	}
	/**
	 * stop scanning
	 */
	public void deviceScanStop(){
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
	}
	
	/**
	 * thread for scan
	 */
	public BluetoothAdapter.LeScanCallback mLeScanCallback =new BluetoothAdapter.LeScanCallback() {
		@Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
			scanDeviceThread =new Thread( 
					new Runnable() {
						
						@Override
						public void run(){
							Log.i("address", "address...."+device.getName());
							//if (device.getAddress().equals("00:18:55:00:00:0D")) {
							if(device != null && device.getName() != null){
								if (device.getName().equals(bleName)) {
								mDevice=device;
							}
						}
						}
					});
			scanDeviceThread.start();
		}	
	};
	
	/**
	 * Implements callback methods for GATT events that the application needs
	 * Such as connection change and services discovered
	 */
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,int newState){
			
			if(BluetoothProfile.STATE_CONNECTED==newState){
				Log.i("address", "CONNECTED22........");
				
				 connected_flag = true;
	             mBluetoothGatt.discoverServices();
	             broadcastUpdate(ACTION_STATE_CONNECTED);
				
				/*Intent intent22 = new Intent();
				intent22.putExtra("device", "deviceA.........");
				intent22.setAction("theScanResultD");
				if(MenuActivity.theContext != null)
				MenuActivity.theContext.sendBroadcast(intent22);
				
				if(mOnConnectListener!=null)
            		mOnConnectListener.onConnect(gatt);
				Log.i("TAG", "Connected to GATT server.");
				Log.i("TAG", "Attempting to start service discovery:" +mBluetoothGatt.discoverServices());*/
			}else if(BluetoothProfile.STATE_DISCONNECTED==newState){

                Tools.connectedFlag = false;
                connected_flag = false;
                broadcastUpdate(ACTION_STATE_DISCONNECTED);
                
//				theState1 = false;
//				if(theThread1 != null){
//	        		theThread1.stop = false;
//	        	}
//				
//				mBluetoothGatt.close();
//				mBluetoothGatt = null;
//				
//				Intent intent11 = new Intent();
//				intent11.putExtra("device", "deviceA.........");
//				intent11.setAction("theScanResult");
//				if(MenuActivity.theContext != null)
//				MenuActivity.theContext.sendBroadcast(intent11);			
//				if(mOnDisconnectListener!=null)
//                	mOnDisconnectListener.onDisconnect(gatt);
			}
		}
		
		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			super.onServicesDiscovered(gatt, status);
            //Log.e("falter","status:::::::::::::::::::::::::::::::"+status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
            	mBluetoothGatt.getServices();
            	Log.i("address", "00000000000000000000000000000000000000000000000000000000000000...");
                service = mBluetoothGatt.getService(uuid_s);
                broadcastUpdate(ACTION_SERVICESDISCOVERED_OVER);

                if (service != null) {
                	mBlueGattcharNotify = service.getCharacteristic(uuid_c);
                	characteristicWrite = service.getCharacteristic(uuid_w);

                    if(mBlueGattcharNotify != null){
                        mBluetoothGatt.setCharacteristicNotification(mBlueGattcharNotify, true);
                        BluetoothGattDescriptor descriptor = mBlueGattcharNotify.getDescriptor(uuid_d);
                        if(descriptor != null){
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            mBluetoothGatt.writeDescriptor(descriptor);
                        }
                    }
                }
            }
            
			/*if(BluetoothGatt.GATT_SUCCESS==status && mOnServiceDiscoverListener!=null){
				Log.i("address", "Yes Service: " + mBluetoothGatt.getServices());
				service=mBluetoothGatt.getService(uuid_s);//
				if(service!=null){
					mBlueGattchar=service.getCharacteristic(uuid_c);
					if(mBlueGattchar!=null){
						Log.i("TAG", "Yes mBlueGattchar");
						mBluetoothGatt.setCharacteristicNotification(mBlueGattchar, true);
						
						BluetoothGattDescriptor descriptor = mBlueGattchar.getDescriptor(uuid_d);
						if(descriptor != null){
							descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
							Log.i("TAG", "Descriptor write: " + mBluetoothGatt.writeDescriptor(descriptor));
						}
					}
				}
				String send ="SHAKE";
				if(mBlueGattchar.setValue(send))
					if(mBluetoothGatt.writeCharacteristic(mBlueGattchar))
						Log.i("TAG", "Shake!  " + send);
			}*/
		};
		
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
        	/*if (mOnDataAvailableListener!=null)
        		mOnDataAvailableListener.onCharacteristicRead(gatt, characteristic, status);*/
        }
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);

            //Tools.setLog("log1", "onCharacteristicWrite............................status."+status);

            broadcastUpdate(ACTION_WRITE_DATA_OVER, status);
            /*if(onWriteOverCallback != null)
                onWriteOverCallback.OnWriteOver(gatt, characteristic, status);*/
        };
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.e("falter","::::::::::::::::::::::::::::::。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。onDescriptorWrite:");
            Tools.connectedFlag = true;
            broadcastUpdate(ACTION_WRITE_DESCRIPTOR_OVER, status);

        };
		
        @Override
		public void onCharacteristicChanged(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic){
        	 super.onCharacteristicChanged(gatt, characteristic);
             Tools.connectedFlag = true;
             //broadcastUpdate(ACTION_CHARACTER_CHANGE, characteristic.getValue());
             
             
             String s = new String(characteristic.getValue());
             
             String data = formatConverse(s.getBytes());

             if(data.startsWith("AA")){
 				
 				data = data.replaceAll("AA", "");
 				
 				if(data.equals("17")){			//11
 					Log.i("address", "hello1");
 					//theActivity.msgPosi_11(deltaT,min,sec,milli);
 					broadcastUpdate(ACTION_CHARACTER_CHANGE, 11);
 				}else if(data.equals("33")){	//12
 					Log.i("address", "hello2");
 					//theActivity.msgPosi_12();
 					broadcastUpdate(ACTION_CHARACTER_CHANGE, 12);
 				}else if(data.equals("18")){	//21
 					Log.i("address", "hello3");
 					//theActivity.msgPosi_21(deltaT,min,sec,milli);
 					broadcastUpdate(ACTION_CHARACTER_CHANGE, 21);
 				}else if(data.equals("34")){	//22
 					Log.i("address", "hello4");
 					//theActivity.msgPosi_22();
 					broadcastUpdate(ACTION_CHARACTER_CHANGE, 22);
 				}
 				
 			}
 			else{
 				//theActivity.msgAccChange(data);
 				broadcastUpdate(ACTION_CHARACTER_CHANGE2, data);
 			}
             
             
// 			if (onDataAvailableListener != null) {
// 				onDataAvailableListener.OnDataAvailable(gatt, characteristic);
// 			}
         
  
        	/*theState = false;
        	theState1 = true;
        	mOnServiceDiscoverListener.onServiceDiscover(gatt);
        	
        	String s = null;
			try {
				s = new String(characteristic.getValue(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
*/
             
             
        	/*if(theThread1 != null){
        		theThread1.stop = true;
        	}*/
        	
        	/*if(timer == null){
        	timer = new Timer();
        	timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(theState && theState1){
					mBluetoothGatt.disconnect();
					theState1 = false;
					}
	        		//mBluetoothGatt = mDevice.connectGatt(getApplicationContext(), true, mGattCallback);
					theState = true;
				}
			}, 1,1200);
        	}*/
			//if (mOnDataAvailableListener!=null)
        		//mOnDataAvailableListener.onCharacteristicWrite(gatt, characteristic);
		}
        
		/*@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			Log.i("laolao", "write over callBack");
			super.onCharacteristicWrite(gatt, characteristic, status);
			
		}*/
		
	};
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
	
	 public static long bytes2Long(byte[] byteNum) {  
	        long num = 0;  
	        for (int ix = 0; ix < 8; ++ix) {  
	            num <<= 8;  
	            num |= (byteNum[ix] & 0xff);  
	        }  
	        Date dd=new Date();
			long ddLong = dd.getTime();
			Log.e("niangde", "原来    "+num);
			Log.e("niangde", "现在    "+ddLong);
			
	        return ddLong-num;  
	    }
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent == null){Log.i("niangde", "intent null....");}
		if(intent != null){
		int command = intent.getIntExtra(EXTRA_COMMAND,-1);
		
		Log.i("niangde", "onStartCommand!!!"+command);
		//String address = "null";
		switch (command) {
		case COMMAND_CONNECT:
			Log.i("niangde", "got connect command");
			if (mDevice != null) {
				Log.i("niangde", "mDevice != null!!");
				mBluetoothGatt = mDevice.connectGatt(this,false,mGattCallback);
			}
			break;
		case COMMAND_DISCOVER_SERVICE:
			if(mBluetoothGatt != null){
				Log.i("niangde", "got service");
				mBluetoothGatt.discoverServices();
			}
			break;
		case COMMAND_WRITE_CHARACTERISTIC:
			Log.i("niangde", "got write command!");
			if(mBluetoothGatt != null){
				/*byte[] writeString = intent.getByteArrayExtra(WRITE_VALUE);
				service = mBluetoothGatt.getService(uuid_s);
				characteristic1 = service.getCharacteristic(uuid_w);
				characteristic1.setValue(writeString);
				mBluetoothGatt.writeCharacteristic(characteristic1);*/
			}
			break;
		default:
			break;
		}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	public static String toHex(byte b) {  
        String result = Integer.toHexString(b & 0xFF);  
        if (result.length() == 1) {  
            result = '0' + result;  
        }  
        return result;  
    }  
	/**
	 * To end the bluetoothGatt
	 */
	public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }
	//初始BLe
    public boolean initBle() {
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (null == mBluetoothManager) {
            return false;
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (null == mBluetoothAdapter) {
            return false;
        }
        return true;
    }

    //发送广播消息1
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
    //发送广播消息2
    private void broadcastUpdate(final String action,int value) {
        final Intent intent = new Intent(action);
        intent.putExtra("value", value);
        sendBroadcast(intent);
    }
    //发送广播消息3
    private void broadcastUpdate(final String action,byte[] value) {

        final Intent intent = new Intent(action);
        intent.putExtra("value", value);
        sendBroadcast(intent);

    }
    private void broadcastUpdate(final String action,String value) {
        final Intent intent = new Intent(action);
        intent.putExtra("value", value);
        sendBroadcast(intent);
    }
    public void scanBle(BluetoothAdapter.LeScanCallback callback) {
        mBluetoothAdapter.startLeScan(callback);
    }

    public void stopscanBle(BluetoothAdapter.LeScanCallback callback){
        mBluetoothAdapter.stopLeScan(callback);
    }
	public void scanClose(){}
	 private final IBinder mBinder = new LocalBinder();
	    @Override
	    public IBinder onBind(Intent arg0) {
	        // TODO Auto-generated method stub
	        return mBinder;
	    }

	    public class LocalBinder extends Binder{

	        public BLEconfig getService() {
	            return BLEconfig.this;
	        }
	    }
}