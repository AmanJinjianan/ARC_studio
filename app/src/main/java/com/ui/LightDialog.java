package com.ui;

import android.R.string;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluetoothARC.R;

/**
 * the light dialog race-begin indicator light
 *
 * @author likun@stu.zzu.edu.cn
 */
public class LightDialog extends ProgressDialog {
    ImageView greenOne, greenTwo, greenThree, greenFour, goView, light_logo;

    LinearLayout theLinearLayout;
    RelativeLayout theLayout;

    TextView textView, textViewLap, textViewTheLine;

    Bitmap mBgBitmap = null;

    // private MenuActivity theMenuActivity;
    // 保留位置
    // 获取屏幕的尺寸信息。

    int screenHeight = AppContext.screenHeight;
    int screenWidth = AppContext.screenWidth;
    int pianYi = 0;
    float scaledDensity = AppContext.scaledDensity;
    float textSize = screenHeight / 11 / scaledDensity;

    public LightDialog(Context c) {
        super(c);
        setCanceledOnTouchOutside(false);
    }

    public LightDialog(Context c, int style) {
        super(c, style);
        setCanceledOnTouchOutside(false);
    }

    private boolean isVoice = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // no title
        // requestWindowFeature(Window.FEATURE_NO_TITLE);

        System.gc();
        setContentView(R.layout.dialog_light);
        Log.i("info", "light MenuActivity.isVoice" + MenuActivity.isVoice);
        if (MenuActivity.isVoice) {
            isVoice = true;
            Log.i("info", "light isVoice" + isVoice);
        }

        float per = (float) screenWidth / (float) screenHeight;
        Log.i("diedie", "yes!!!" + per);
        if (per > 0.7) {
            Log.e("DEBUG", "yes!!!");
            textSize = textSize + 30;
            pianYi = pianYi + 7;
        }

        // 阻止使用返回键
        setCancelable(false);


        init();

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }


    private void init() {

        goView = (ImageView) findViewById(R.id.goview);

        goView.setVisibility(View.INVISIBLE);
        final ScaleAnimation animation = new ScaleAnimation(0.0f, 3f, 0.0f, 3f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setDuration(400);
        animation.setStartOffset(4800);
        goView.setAnimation(animation);

        textViewLap = (TextView) findViewById(R.id.thelaps);
        //textViewLap.setBackgroundColor(Color.BLUE);
        textViewLap.setGravity(Gravity.CENTER);
        textViewLap.setTextSize((float) (screenWidth * 0.08));
        textViewLap.setText(Integer.toString(AppContext.laps));
		
		/*textViewLap2  = new TextView(null);
		textViewLap2.setText("9");
		textViewLap2.setTextSize((float) (5*AppContext.inch));*/

        Log.i("Hello", "textViewLap " + Integer.toString(AppContext.laps));

        greenOne = (ImageView) findViewById(R.id.dialog_light_green_one);

        greenTwo = (ImageView) findViewById(R.id.dialog_light_green_two);

        greenThree = (ImageView) findViewById(R.id.dialog_light_green_three);

        greenFour = (ImageView) findViewById(R.id.dialog_light_green_four);

        light_logo = (ImageView) findViewById(R.id.light_logo);
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (screenWidth * 0.38),(int) (screenWidth * 0.14));//346
//		params.setMargins((int) (screenWidth * 0.31),(int) (screenHeight * 0.83), 0, 0);
//		light_logo.setLayoutParams(params);

        if (AppContext.device == "phone") {

            //RelativeLayout.LayoutParams paramX = new RelativeLayout.LayoutParams((int) (screenWidth * 0.25), (int) (screenWidth * 0.25));
            //paramX.setMargins((int) (screenWidth * 0.35), (int) (screenHeight * 0.642), 0, 0);//0.672
            //goView.setLayoutParams(paramX);

//            RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.2), (int) (screenWidth * 0.2));
//            params0.setMargins((int) (screenWidth * 0.41), (int) (screenHeight * 0.12), 0, 0);//0.05
//            textViewLap.setLayoutParams(params0);
            textViewLap.setTextSize((float) (11 * AppContext.inch));
            //textViewLap.setText(String))

            setPosiForPhone(greenOne, 0.122, 0.28, 1);//337
            setPosiForPhone(greenTwo, 0.311, 0.28, 1);//34
            setPosiForPhone(greenThree, 0.497, 0.28, 1);
            setPosiForPhone(greenFour, 0.685, 0.28, 1);


            //mBgBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.light_back_p);
            //findViewById(R.id.light_background).setBackgroundDrawable(new BitmapDrawable(mBgBitmap));
	        
			/*findViewById(R.id.light_background).setBackgroundResource(
					R.drawable.light_back_p);*/
            if (AppContext.lanuage) {// zh
                light_logo.setBackgroundResource(
                        R.drawable.logo);
            } else {
                light_logo.setBackgroundResource(
                        R.drawable.logo2);
            }
        } else if (AppContext.device == "pad") {

            light_logo.setVisibility(View.INVISIBLE);

            LinearLayout.LayoutParams paramX = new LinearLayout.LayoutParams(
                    (int) (screenWidth * 0.25), (int) (screenWidth * 0.25));
            paramX.setMargins((int) (screenWidth * 0.35),
                    (int) (screenHeight * 0.681), 0, 0);
            goView.setLayoutParams(paramX);

            LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(
                    (int) (screenWidth * 0.26), (int) (screenWidth * 0.25));
            params0.setMargins((int) (screenWidth * 0.37),
                    (int) (screenHeight * 0.107), 0, 0);
            textViewLap.setLayoutParams(params0);

            setPosi(greenOne, 0.122, 0.344, 1);
            setPosi(greenTwo, 0.311, 0.344, 1);
            setPosi(greenThree, 0.497, 0.344, 1);
            setPosi(greenFour, 0.682, 0.344, 1);//0.685

            if (AppContext.lanuage) {// zh
                findViewById(R.id.light_background).setBackgroundResource(
                        R.drawable.background_light);
            } else {
                findViewById(R.id.light_background).setBackgroundResource(
                        R.drawable.background_lighte);
            }
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.i("info", "light isVoice" + isVoice);
                // TODO Auto-generated method stub
                if (isVoice)
                    //ScanActivity.soundPlaylight();

                    greenOne.setVisibility(View.VISIBLE);

            }
        }, 1000);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.i("info", "light isVoice" + isVoice);
                // TODO Auto-generated method stub
                if (isVoice)
                    ScanActivity.three();

                    greenTwo.setVisibility(View.VISIBLE);
            }
        }, 2000);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.i("info", "light isVoice" + isVoice);
                // TODO Auto-generated method stub
                if (isVoice)
                    //ScanActivity.two();

                    greenThree.setVisibility(View.VISIBLE);
            }
        }, 3000);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (isVoice)
                    //ScanActivity.one();
                    greenFour.setVisibility(View.VISIBLE);
            }
        }, 4000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // goView.setAnimation(animation);
                if (isVoice) {
                    //ScanActivity.the_go_voice();
                }

            }
        }, 5000);

        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface arg0) {
                // TODO Auto-generated method stub
                Log.e("info", "dismiss.......");
                greenOne = null;
                greenTwo = null;
                greenThree = null;
                greenFour = null;
                goView = null;

                theLinearLayout = null;
                theLayout = null;

                textView = null;
                textViewLap = null;
                textViewTheLine = null;

                //setContentView(R.layout.null_a);

                System.gc();
            }
        });

    }

    // 设置控件的大小和位置
    public void setPosi(ImageView imageView, double leftPer, double topPer,
                        double per) {

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                (int) (screenWidth * 0.201 * per),
                (int) (screenWidth * 0.338 * per));//346
        params.setMargins((int) (screenWidth * leftPer),
                (int) (screenHeight * topPer), 0, 0);
        imageView.setLayoutParams(params);
    }
    public void setPosiForPhone(ImageView imageView, double leftPer, double topPer,
                        double per) {

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                (int) (screenWidth * 0.201 * per),
                (int) (screenWidth * 0.338 * per));//346
        params.setMargins((int) (screenWidth * leftPer),
                (int) (screenWidth/8 * topPer), 0, 0);
        imageView.setLayoutParams(params);
    }
}
