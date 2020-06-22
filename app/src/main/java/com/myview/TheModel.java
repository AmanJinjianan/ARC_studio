package com.myview;

public class TheModel {
	
	//public static double heightScale_PanelToScreen = 1/3;
	
	public String lineXStatus = "no start";
	
	public String lineXIsDamaged = "not damaged";
	
	public String lineXIsNormal_Over = "not normal";
	
	public int pointCount = -1;
	
	public String Line = "111";
	
	public String rank = "1";
	//public static String lap = "LAP";
	public String lapValue = "0";
	
	public String fastTimeTitle = "BESTLAP";
	public String fastTime = "00:00:000";
	
	public String lastLapTimeTitle = "LASTLAP";
	public String lastLapValue = "00:00:000";
	
	public String totleTimeTitle = "TOTLE";
	public String totleTime = "00:00:000";
	/*
	public static String first_lastTimeTitle = "--:--:---";
	public static String first_lastTime = "--:--:---";*/
	//指针的旋转中心，centralX,centralY,三个指针为同一个旋转中心
	public int centralX=10,centralY=10;
	//定义三个指针的旋转角度
	public int leftPointerDegree=30;
	public int rightPointerDegree= -30;
	public int accPointerDegree=-90;
	//定义三个指针的缩放比例
	public Float leftPointerXScale,leftPointerYScale;
	public Float rightPointerXScale,rightPointerYScale;
	public Float accPointerXScale,accPointerYScale;
}
