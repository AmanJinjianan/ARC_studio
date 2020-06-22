package com.myview;

import com.bluetoothARC.R;
import com.myview.PanelView.MyThread;
import com.ui.AppContext;
//import com.myview.BaseView.MyThread;
import com.ui.MenuActivity;
import com.ui.RaceActivity;
import com.ui.TrainActivity;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;

public class PanelViewA extends View {

	public String nullString = AppContext.nullString;
	public String please_refuelString = AppContext.please_refuelString;
	public String refuelingString = AppContext.refuelingString;
	public String damagedString = AppContext.damagedString;
	public String over_perfectString = AppContext.over_perfectString;
	public String oversString = AppContext.oversString;

	public static int oilSpeed = 18;
	private MyThread thread;

	Paint paint1, paint2, paint3, paintAlp1, paintAlp2;
	private Bitmap leftPointer, rightPointer, accPointer, oil_box_orange,
			oil_box_red, oil_box_damaged, oil_box_green, wheel_blue, wheel_red,
			wheel_green, wheel_damaged;

	private Bitmap oil_1, oil_2, oil_3, oil_4, oil_5, oil_6, oil_7,
			oil_2_please, oil_4_please,oil_end;
	private int count = 0, oil_X_count,oil_HZ=0;
	// 透明度参数alp
	public int alp1 = 0;
	// 透明度参数alp
	public int alp2 = 0;

	public int perDegree = 4;
	// 加油状态
	public String line1OilState = "normal";

	public TheModel theModel = new TheModel();
	int width = AppContext.screenWidth;
	// int height=(int)(ScanActivity.screenHeight*1/3);
	int height = (int) (AppContext.screenWidth * 0.55);

	public int leftDegree = 30, rightDegree = -30, oilDegree = -90;
	public String sidePointerState = "null";
	public String oilPointerState = "null";
	public String voiceState = "null";

	public int accPosition = 0;

	int myYellow = 0xffff9900;
	int leftPointerWidth, leftPointerHeight, rightPointerWidth,
			rightPointerHeight, accPointerWidth, accPointerHeight;
	int oil_boxWidth, oil_boxHeight, wheelWidth, wheelHeight;

	private static PanelViewA thePanelA = null;
	
	private Bitmap lineOneBackBitmap = null;
	
	public boolean oil_4_pleaseState = false,oil_2_pleaseState = false;

	private PanelViewA(Context context) {
		super(context);
		
		lineOneBackBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.line_one_back);
		this.setBackgroundDrawable(new BitmapDrawable(lineOneBackBitmap));
		
		//this.setBackgroundResource(R.drawable.line_one_back);
		getImage();
		// TODO Auto-generated constructor stub
	}

	public static PanelViewA getInstance(Context context) {
		if (thePanelA == null) {
			thePanelA = new PanelViewA(context);
		}

		return thePanelA;
	}

	public double lapValuePosition = 0.148;

	@Override
	protected void onDraw(Canvas canvas) {
		
		

		if (thread == null) {
			thread = new MyThread();
			thread.start();
		}

		// 设置文本的三个大小的字号
		paint1 = new Paint();
		paint1.setColor(Color.WHITE);
		paint1.setTextSize((float) (height * 0.097));

		paint2 = new Paint();
		paint2.setColor(Color.WHITE);
		paint2.setTextSize((float) (height * 0.078));

		paint3 = new Paint();
		paint3.setColor(Color.WHITE);
		paint3.setTextSize((float) (height * 0.071));

		// last alp paint
		paintAlp1 = new Paint();
		paintAlp1.setColor(Color.WHITE);
		paintAlp1.setAlpha(alp1);
		paintAlp1.setTextSize((float) (height * 0.071));

		// totle alp paint
		paintAlp2 = new Paint();
		paintAlp2.setColor(Color.WHITE);
		paintAlp2.setAlpha(alp2);
		paintAlp2.setTextSize((float) (height * 0.071));
		// 设置抗锯齿，防止过多失真
		// paint.setAntiAlias(true);

		// text_line
		canvas.drawText(theModel.Line, (int) (width * 0.191),
				(int) (height * 0.113), paint1);
		// text_rank
		canvas.drawText(theModel.rank, (int) (width * 0.879),
				(int) (height * 0.113), paint1);

		// text_lapValue
		canvas.drawText(theModel.lapValue, (int) (width * lapValuePosition),
				(int) (height * 0.75), paint2);

		// text_fastestTitle
		canvas.drawText(theModel.fastTimeTitle, (int) (width * 0.346),
				(int) (height * 0.97), paint3);
		// text_fastestValue
		canvas.drawText(theModel.fastTime, (int) (width * 0.514),
				(int) (height * 0.97), paint3);

		// text_totleTimeTitle
		canvas.drawText(theModel.totleTimeTitle, (int) (width * 0.346),
				(int) (height * 1.05), paintAlp2);
		// text_totleTimeValue
		canvas.drawText(theModel.totleTime, (int) (width * 0.514),
				(int) (height * 1.05), paintAlp2);

		// text_lastLapTitle
		canvas.drawText(theModel.lastLapTimeTitle, (int) (width * 0.346),
				(int) (height * 1.05), paintAlp1);
		// text_lastLapvalue
		canvas.drawText(theModel.lastLapValue, (int) (width * 0.514),
				(int) (height * 1.05), paintAlp1);

		// Log.i("this", "First panel draw end!");
		// 左边油表指针
		// 定义矩阵对象
		Matrix matrix = new Matrix();
		matrix.postScale((float) (width * 0.083 / leftPointerWidth),
				(float) (width * 0.104 / leftPointerHeight));
		matrix.postTranslate((float) (width * 0.259), (float) (height * 0.469));
		matrix.postRotate(leftDegree, (float) (width * 0.518),
				(float) (height * 0.563));
		// Bitmap bmp1 = Bitmap.createBitmap(leftPointer, 0, 0,
		// (int)(leftPointer.getWidth()*0.800), leftPointer.getHeight(), matrix,
		// true);
		canvas.drawBitmap(leftPointer, matrix, null);
		// Log.i("this",
		// "left width id"+leftPointer.getWidth()+"height is"+leftPointer.getHeight());

		// 右边磨损度指针
		// 定义矩阵对象
		Matrix matrix1 = new Matrix();
		matrix1.postScale((float) (width * 0.083 / rightPointerWidth),
				(float) (width * 0.104 / rightPointerHeight));
		matrix1.postTranslate((float) (width * 0.670), (float) (height * 0.469));
		matrix1.postRotate(rightDegree, (float) (width * 0.500),
				(float) (height * 0.563));
		// Bitmap bmp1 = Bitmap.createBitmap(leftPointer, 0, 0,
		// (int)(leftPointer.getWidth()*0.800), leftPointer.getHeight(), matrix,
		// true);
		canvas.drawBitmap(rightPointer, matrix1, null);
		// Log.i("this",
		// "right width id"+rightPointer.getWidth()+"height is"+rightPointer.getHeight());

		// 中间油门指针
		// 定义矩阵对象
		Matrix matrix2 = new Matrix();
		matrix2.postScale((float) (width * 0.142 / accPointerWidth),
				(float) (width * 0.142 / accPointerWidth));
		matrix2.postTranslate((float) (width * 0.396), (float) (height * 0.379));
		matrix2.postRotate(oilDegree, (float) (width * 0.51),
				(float) (height * 0.508));
		// Bitmap bmp1 = Bitmap.createBitmap(leftPointer, 0, 0,
		// (int)(leftPointer.getWidth()*0.800), leftPointer.getHeight(), matrix,
		// true);
		canvas.drawBitmap(accPointer, matrix2, null);

		if (theModel.lineXStatus == "refuel"
				&& theModel.leftPointerDegree != 30) {

			// 画2油箱
			Paint paintX = new Paint();

			Matrix matrix5 = new Matrix();
			matrix5.postScale((float) (width * 0.058 / wheelWidth),(float) (width * 0.08 / wheelWidth));
			matrix5.postTranslate((float) (width * 0.73),(float) (height * 0.92));
			oil_X_count++;
			
			switch (oil_X_count) {
			case 1:
				canvas.drawBitmap(oil_1, matrix5, paintX);break;
			case 2:
				canvas.drawBitmap(oil_1, matrix5, paintX);break;
			case 3:
				canvas.drawBitmap(oil_1, matrix5, paintX);break;
			case 4:
				canvas.drawBitmap(oil_2, matrix5, paintX);break;
			case 5:
				canvas.drawBitmap(oil_2, matrix5, paintX);break;
			case 6:
				canvas.drawBitmap(oil_2, matrix5, paintX);break;
			case 7:
				canvas.drawBitmap(oil_3, matrix5, paintX);break;
			case 8:
				canvas.drawBitmap(oil_3, matrix5, paintX);break;
			case 9:
				canvas.drawBitmap(oil_3, matrix5, paintX);break;
			case 10:
				canvas.drawBitmap(oil_4, matrix5, paintX);break;
			case 11:
				canvas.drawBitmap(oil_4, matrix5, paintX);break;
			case 12:
				canvas.drawBitmap(oil_4, matrix5, paintX);break;
			case 13:
				canvas.drawBitmap(oil_5, matrix5, paintX);break;
			case 14:
				canvas.drawBitmap(oil_5, matrix5, paintX);break;
			case 15:
				canvas.drawBitmap(oil_5, matrix5, paintX);break;
			case 16:
				canvas.drawBitmap(oil_6, matrix5, paintX);break;
			case 17:
				canvas.drawBitmap(oil_6, matrix5, paintX);break;
			case 18:
				canvas.drawBitmap(oil_6, matrix5, paintX);break;
			case 19:
				canvas.drawBitmap(oil_7, matrix5, paintX);break;
			case 20:
				canvas.drawBitmap(oil_7, matrix5, paintX);break;
			case 21:
				canvas.drawBitmap(oil_7, matrix5, paintX);
				if (oil_X_count==21) {
					oil_X_count=0;
				}
				break;
			default:
				break;
			}
			// 此处控制闪烁频率
			count++;
			if (count <= 60 && count > 20) {
				drawOil_wheel(Color.GREEN, canvas, oil_box_green, wheel_green,
						refuelingString);
				if (count == 60)
					count = 0;
			}
		} else if (theModel.lineXStatus == "normal"
				|| theModel.leftPointerDegree == 30) {

			drawOil_wheel(Color.WHITE, canvas, oil_box_orange, wheel_blue, " ");
		} else if (theModel.lineXStatus == "oilless" && oil_4_pleaseState) {

			Paint paintX = new Paint();
			Matrix matrix5 = new Matrix();
			matrix5.postScale((float) (width * 0.058 / wheelWidth),(float) (width * 0.08 / wheelWidth));
			matrix5.postTranslate((float) (width * 0.73),(float) (height * 0.92));
			canvas.drawBitmap(oil_4_please, matrix5, paintX);
			// 此处控制闪烁频率
			count++;
			if (count <= 60 && count > 20) {
				drawOil_wheel(Color.YELLOW, canvas, oil_box_orange, wheel_blue,
						please_refuelString);
				if (count == 60)
					count = 0;
			}
		} else if (theModel.lineXStatus == "please_refuel" && oil_2_pleaseState) {
			Paint paintX = new Paint();
			Matrix matrix5 = new Matrix();
			matrix5.postScale((float) (width * 0.058 / wheelWidth),(float) (width * 0.08 / wheelWidth));
			matrix5.postTranslate((float) (width * 0.73),(float) (height * 0.92));
			canvas.drawBitmap(oil_2_please, matrix5, paintX);
			// 此处控制闪烁频率
			count++;
			if (count <= 60 && count > 20) {
				drawOil_wheel(Color.RED, canvas, oil_box_red, wheel_red,
						please_refuelString);
				if (count == 60)
					count = 0;
			}
		}

		else if (theModel.lineXStatus == "lineX_gameover"
				&& theModel.lineXIsDamaged == "lineX_damaged") {
			Paint paintX = new Paint();
			Matrix matrix5 = new Matrix();
			matrix5.postScale((float) (width * 0.058 / wheelWidth),(float) (width * 0.08 / wheelWidth));
			matrix5.postTranslate((float) (width * 0.73),(float) (height * 0.92));
			canvas.drawBitmap(oil_end, matrix5, paintX);
			
			drawOil_wheel(Color.RED, canvas, oil_box_damaged, wheel_damaged,
					damagedString);

		} else if (theModel.lineXStatus == "lineX_gameover"
				&& theModel.lineXIsNormal_Over == "lineX_normal_over") {

			if (theModel.pointCount == 0) {
				drawOil_wheel(Color.GREEN, canvas, oil_box_green, wheel_green,
						over_perfectString);
			} else if (theModel.pointCount <= 2) {
				drawOil_wheel(Color.GREEN, canvas, oil_box_red, wheel_red,
						oversString);
			} else if (theModel.pointCount > 2) {
				drawOil_wheel(Color.GREEN, canvas, oil_box_orange, wheel_blue,
						oversString);
			}

		}

	}

	// 绘制邮箱，轮子，和提示信息
	public void drawOil_wheel(int color, Canvas canvas, Bitmap oil_bitmap,
			Bitmap wheel_bitmap, String warnMessage) {

		Paint paint4 = new Paint();
		paint4.setColor(color);
		paint4.setTextSize((float) (height * 0.128));
		// 画邮箱图标
		Matrix matrix3 = new Matrix();
		matrix3.postScale((float) (width * 0.068 / oil_boxWidth),
				(float) (width * 0.068 / oil_boxWidth));
		matrix3.postTranslate((float) (width * 0.239), (float) (height * 0.739));
		canvas.drawBitmap(oil_bitmap, matrix3, paint4);
		// 画轮子
		Matrix matrix4 = new Matrix();
		matrix4.postScale((float) (width * 0.109 / wheelWidth),
				(float) (width * 0.109 / wheelWidth));
		matrix4.postTranslate((float) (width * 0.786), (float) (height * 0.538));
		canvas.drawBitmap(wheel_bitmap, matrix4, paint4);
		/*
		 * // 画2油箱 Matrix matrix5 = new Matrix(); matrix5.postScale((float)
		 * (width * 0.109 / wheelWidth), (float) (width * 0.109 / wheelWidth));
		 * matrix5.postTranslate((float) (width * 0.786), (float) (height *
		 * 0.538)); canvas.drawBitmap(wheel_bitmap, matrix5, paint4);
		 */

		canvas.drawText(warnMessage, (float) (width * 0.049),
				(float) (height * 1.035), paint4);

	}

	// 两边指针变动动画逻辑
	int x = 0;// 计数变量
	int p = 0;

	private void sidePointerLogic() {
		p++;
		if (p == oilSpeed) {
			if (sidePointerState == "reduceState") {
				if (x <= perDegree) {
					x++;
					leftDegree = leftDegree - 1;
					rightDegree = rightDegree + 1;
				} else {
					x = 0;
					sidePointerState = "stop";
				}
				Log.i("dayima", "x=" + x);
				Log.i("dayima", "status=" + sidePointerState);
			}
			if (sidePointerState == "plusState") {
				if (x <= perDegree) {
					x++;
					leftDegree = leftDegree + 1;
					rightDegree = rightDegree - 1;
				} else {
					x = 0;
					sidePointerState = "stop";
				}
			}
			p = 0;
		}
	}

	int speed = 1;

	// 油门指针变动逻辑,-90为初始角度，总跨度为270度，分十级，一级27度。
	private void oilPointerLogic() {
		// 档位RaceActivity.mama
		if (oilDegree <= -90 + accPosition * 27) {
			// int xx = -90+accPosition*27-oilDegree;
			oilDegree = oilDegree + speed;
		}
		if (oilDegree > -90 + accPosition * 27) {
			oilDegree = oilDegree - speed;
		}
	}

	class MyThread extends Thread {
		@Override
		public void run() {
			while (true) {

				sidePointerLogic();

				oilPointerLogic();
				postInvalidate();

				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void getImage() {

		leftPointer = BitmapFactory.decodeResource(getResources(),
				R.drawable.left_pointer);
		leftPointerWidth = leftPointer.getWidth();
		leftPointerHeight = leftPointer.getHeight();

		// Log.i("mama", "leftPointer getWidth"+leftPointer.getWidth());
		rightPointer = BitmapFactory.decodeResource(getResources(),
				R.drawable.right_pointer);
		rightPointerWidth = rightPointer.getWidth();
		rightPointerHeight = rightPointer.getHeight();

		// Log.i("mama", "rightPointer getWidth"+rightPointer.getWidth());
		accPointer = BitmapFactory.decodeResource(getResources(),
				R.drawable.inner_pointer_m);
		accPointerWidth = accPointer.getWidth();
		// Log.i("mama", "accPointer getWidth"+accPointer.getWidth());

		oil_box_red = BitmapFactory.decodeResource(getResources(),
				R.drawable.oil_red);
		oil_box_green = BitmapFactory.decodeResource(getResources(),
				R.drawable.oil_green);
		oil_box_damaged = BitmapFactory.decodeResource(getResources(),
				R.drawable.oil_damaged);
		oil_box_orange = BitmapFactory.decodeResource(getResources(),
				R.drawable.oil_yellow);
		oil_boxWidth = oil_box_orange.getWidth();
		// Log.i("mama", "height"+oil_box_orange.getWidth());
		
		if (AppContext.lanuage) {
			oil_1 = BitmapFactory.decodeResource(getResources(), R.drawable.oil_1);
			oil_2 = BitmapFactory.decodeResource(getResources(), R.drawable.oil_2);
			oil_3 = BitmapFactory.decodeResource(getResources(), R.drawable.oil_3);
			oil_4 = BitmapFactory.decodeResource(getResources(), R.drawable.oil_4);
			oil_5 = BitmapFactory.decodeResource(getResources(), R.drawable.oil_5);
			oil_6 = BitmapFactory.decodeResource(getResources(), R.drawable.oil_6);
			oil_7 = BitmapFactory.decodeResource(getResources(), R.drawable.oil_7);

			oil_2_please = BitmapFactory.decodeResource(getResources(),
					R.drawable.oil_2_please);
			oil_4_please = BitmapFactory.decodeResource(getResources(),
					R.drawable.oil_4_please);
			oil_end = BitmapFactory.decodeResource(getResources(),
					R.drawable.oil_end);
		}else {
			oil_1 = BitmapFactory.decodeResource(getResources(), R.drawable.oil_1e);
			oil_2 = BitmapFactory.decodeResource(getResources(), R.drawable.oil_2e);
			oil_3 = BitmapFactory.decodeResource(getResources(), R.drawable.oil_3e);
			oil_4 = BitmapFactory.decodeResource(getResources(), R.drawable.oil_4e);
			oil_5 = BitmapFactory.decodeResource(getResources(), R.drawable.oil_5e);
			oil_6 = BitmapFactory.decodeResource(getResources(), R.drawable.oil_6e);
			oil_7 = BitmapFactory.decodeResource(getResources(), R.drawable.oil_7e);

			oil_2_please = BitmapFactory.decodeResource(getResources(),
					R.drawable.oil_2e_please);
			oil_4_please = BitmapFactory.decodeResource(getResources(),
					R.drawable.oil_4e_please);
			oil_end = BitmapFactory.decodeResource(getResources(),
					R.drawable.oil_ende);
		}
		
		

		wheel_blue = BitmapFactory.decodeResource(getResources(),
				R.drawable.wheel_yellow);
		wheel_red = BitmapFactory.decodeResource(getResources(),
				R.drawable.wheel_red);
		wheel_green = BitmapFactory.decodeResource(getResources(),
				R.drawable.wheel_green);
		wheel_damaged = BitmapFactory.decodeResource(getResources(),
				R.drawable.wheel_damaged);
		wheelWidth = wheel_blue.getWidth();
		// Log.i("mama", "wheel_blue getWidth"+wheel_blue.getWidth());
	}

}
