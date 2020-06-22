package com.ui;

import com.bluetoothARC.R;

import android.app.Application;

/**
 * store application global variable (context)
 123
 */
public class AppContext extends Application
{
	public static float inch = 0;
	public static int index = R.id.new_yorkBtn;
	public final static String DEVICE_NAME = "YinSuARC";
	public static int laps = 15;
	public static int oil_consum = 12;
	public static int imageState = 1;
	public static int mapFlag = 7;
	public static String useName1 = " ",useName2 = " ";
	//true-chanese,false-Englash
	public static Boolean lanuage = true;
	public static String device = "phone";
	
	public static String nullString = "";
	public static String please_refuelString = "请加油.....";
	public static String refuelingString = "正在加油.....";
	public static String damagedString = "小车损毁.....";
	public static String over_perfectString = "比赛完成,太棒了.....";
	public static String oversString = "比赛完成.....";
	
	public static int screenWidth=0,screenHeight=0;
	public static float  scaledDensity = 0;
	/**
	 * use case below:
	 * 1. appContext = (AppContext)this.getApplicationContext()
	 * 2. AppContext.getInstance().XXXX;
	 */
	private static AppContext instance;
	public static AppContext getInstance(){
		return instance;
	}
	@Override
	public void onCreate()
	{
		super.onCreate();
		instance = this;
	}
}