package com.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluetoothARC.R;
import com.myview.PanelView;

/**
 * It's the train activity more details see in activity_train.xml
 * 
 * @author likun@stu.zzu.edu.cn
 * 
 */
public class MySettingActivity extends Activity implements OnClickListener {
	private ImageButton settingBtn, guidingBtn, disBtn;
	//private DisplayMetrics dm;
	public int screenWidth, screenHeight;
	ImageButton button1, button2, button3, button4, button5;
	TextView textView, textView1;
	ImageView redface, blueface;

	private EditText editText1, editText2;

	private int imageState = 1;
	
	FrameLayout theFrameLayout;

	float scaledDensity;
	private int pianYiLeft = 0, pianYiTop = 0;
	private float textSize = 10, textSize2 = 10;

	static Boolean longClicked;
	private int lapsPlus = 1, lapsReduce = 2;

	private SharedPreferences sharedPreferences;
	private Editor editor;
	Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 璁剧疆鏃犳爣棰�
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 璁剧疆鍏ㄥ睆
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 榛樿闅愯棌铏氭嫙鎸夐
		// getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		// 绂佹鎭睆
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.activity_setting);

		// 鍙傛暟1锛歯ame锛屽弬鏁�2锛氬瓨鍌ㄦā寮�
		sharedPreferences = getSharedPreferences("configdata", MODE_PRIVATE);
		editor = sharedPreferences.edit();

		Log.i("info", "isVoice: " + MenuActivity.isVoice);

		//dm = new DisplayMetrics();
		//getWindowManager().getDefaultDisplay().getMetrics(dm);

		screenHeight = AppContext.screenHeight;
		screenWidth = AppContext.screenWidth;
		// screenWidth = screenHeight*9/16;
		scaledDensity = AppContext.scaledDensity;

		textSize = screenHeight / 18 / scaledDensity;
		textSize2 = screenHeight / 30 / scaledDensity;

		float per = (float) screenWidth / (float) screenHeight;

		theFrameLayout = (FrameLayout) findViewById(R.id.theframeset);

		Log.i("diedie", "yes!!!textSize" + textSize+"  :textSize2"+textSize2);
		if (per > 0.7) {
			Log.i("diedie", "yes!!!");
			// textSize = textSize + 30;
			pianYiLeft = pianYiLeft + 31;
			pianYiTop = pianYiTop + 31;
			textSize = screenHeight / 15 / scaledDensity;
			textSize2 = screenHeight / 23 / scaledDensity;
		}
		// guidingBtn_btn
		guidingBtn = (ImageButton) findViewById(R.id.set_guidingBtn);
		guidingBtn.setOnClickListener(this);
		// setting_btn
		settingBtn = (ImageButton) findViewById(R.id.set_settingBtn);
		settingBtn.setOnClickListener(this);

		disBtn = (ImageButton) findViewById(R.id.imageButton_set_exit);
		disBtn.setOnClickListener(this);

		// line1_edit
		editText1 = (EditText) findViewById(R.id.edittext_line1);
		
		// line2_edit
		editText2 = (EditText) findViewById(R.id.edittext_line2);
		//editText2.setGravity(Gravity.CENTER);
		// editText2.setTextSize(textSize2);

		if (AppContext.useName1.equals(R.string.usename)) {
			if(AppContext.lanuage){
			editText1.setHint(R.string.usename);
			editText1.setText(R.string.usename);
			}else {
				editText1.setHint("Please Enter");
				editText1.setText("Please Enter");
			}
		} else {
			//editText1.setHint(AppContext.useName1);
			editText1.setText(AppContext.useName1);
		}
		
		if (AppContext.useName2.equals(R.string.usename)) {
			if(AppContext.lanuage){
				editText2.setHint(R.string.usename);
				editText2.setText(R.string.usename);
				}else {
					editText2.setHint("Please Enter");
					editText2.setText("Please Enter");
				}
		} else {
			//editText2.setHint(AppContext.useName2);
			editText2.setText(AppContext.useName2);
		}

		if (!AppContext.lanuage) {

			editText1
					.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							12) });
			// editText1.setTextSize(textSize2-5);

			editText2
					.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							12) });
			// editText2.setTextSize(textSize2-5);

			
		}else {
			editText1
			.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
					6) });
			editText2
			.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
					6) });
		}

		// no_voice
		button3 = (ImageButton) findViewById(R.id.imageButton_no_voice);
		button3.setOnClickListener(this);

		// voice
		button5 = (ImageButton) findViewById(R.id.imageButton_voice);
		button5.setOnClickListener(this);

		if (MenuActivity.isVoice) {
			button5.setBackgroundResource(R.drawable.voice_blue);
			button3.setBackgroundResource(R.drawable.no_gray);
			Log.i("info", "isVoice: ............." + MenuActivity.isVoice);
			button5.setEnabled(false);
			button3.setEnabled(true);
			// 鏈夊０鎸夐挳
			button5.setVisibility(View.VISIBLE);
		} else {
			button5.setBackgroundResource(R.drawable.no_blue);
			button3.setBackgroundResource(R.drawable.voice_gray);
			Log.i("info", "isVoice: ????????????????????"
					+ MenuActivity.isVoice);
			button3.setEnabled(false);
			button5.setEnabled(true);
			// 鏃犲０鎸夐挳
			button3.setVisibility(View.VISIBLE);
		}

		// 纭畾鎸夐挳
		button4 = (ImageButton) findViewById(R.id.set_confirm);
		button4.setOnClickListener(this);

		// redface
		redface = (ImageView) findViewById(R.id.imageview_redface);

		// blueface
		blueface = (ImageView) findViewById(R.id.imageview_blueface);

		if (AppContext.device == "phone") {

			// guidingBtn
			FrameLayout.LayoutParams params12 = new FrameLayout.LayoutParams(
					(int) (screenWidth * 0.13), (int) (screenWidth * 0.16));
			params12.setMargins((int) (screenWidth * 4 / 20),
					(int) ((screenHeight * 1 / 16)), 0, 0);
			guidingBtn.setLayoutParams(params12);

			// setting_btn
			FrameLayout.LayoutParams params21 = new FrameLayout.LayoutParams(
					(int) (screenWidth * 0.13), (int) (screenWidth * 0.16));
			params21.setMargins((screenWidth * 1 / 20), screenHeight * 1 / 16,
					0, 0);
			settingBtn.setLayoutParams(params21);

			// disBtn
			FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(
					screenWidth * 1 / 9, screenWidth * 1 / 9);
			params3.setMargins((int) (screenWidth * 0.88),
					(int) ((screenHeight * 0.17)), 0, 0);
			disBtn.setLayoutParams(params3);

			// line1_edit
			setPosi(editText1, screenWidth * 2 / 5, screenWidth * 1 / 11, 0.19,
					0.60);// 058
			editText1.setTextSize(15);
			// line2_edit
			setPosi(editText2, screenWidth * 2 / 5, screenWidth * 1 / 11, 0.56,
					0.60);// 058
			editText2.setTextSize(15);

			// 鏃犲０鍠囧彮
			setPosi(button3, screenWidth * 1 / 8, screenWidth * 1 / 9, 0.52,
					0.79);
			// 鏈夊０鍠囧彮
			setPosi(button5, screenWidth * 1 / 8, screenWidth * 1 / 9, 0.38,
					0.79);

			// 纭畾鎸夐挳
			FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(
					(int) (screenWidth * 1 / 3.8), (int) (screenWidth * 0.111));
			params1.setMargins((int) (screenWidth * 0.3685),
					(int) (screenHeight * 0.691), 0, 0);// 0.691
			button4.setLayoutParams(params1);

			// redface
			setPosi(redface, (int) (screenWidth * 1 / 4.8),
					(int) (screenWidth * 1 / 4.8), 0.21, 0.44);
			// blueface
			setPosi(blueface, (int) (screenWidth * 1 / 4.8),
					(int) (screenWidth * 1 / 4.8), 0.58, 0.44);

			if (AppContext.lanuage) {// zh
				theFrameLayout
						.setBackgroundResource(R.drawable.background_setp);
				guidingBtn.setBackgroundResource(R.drawable.guiding);
				settingBtn.setBackgroundResource(R.drawable.setting);

				button4.setBackgroundResource(R.drawable.confirm);
			} else {
				theFrameLayout
						.setBackgroundResource(R.drawable.background_set_pe);
				guidingBtn.setBackgroundResource(R.drawable.guidinge);
				settingBtn.setBackgroundResource(R.drawable.settinge);

				button4.setBackgroundResource(R.drawable.confirme);
			}
		} else if (AppContext.device == "pad") {

			FrameLayout.LayoutParams params12 = new FrameLayout.LayoutParams(
					(int) (screenWidth * 0.13), (int) (screenWidth * 0.16));
			params12.setMargins((int) (screenWidth * 4 / 20),
					(int) ((screenHeight * 1 / 20)), 0, 0);
			guidingBtn.setLayoutParams(params12);

			// setting_btn
			FrameLayout.LayoutParams params21 = new FrameLayout.LayoutParams(
					(int) (screenWidth * 0.13), (int) (screenWidth * 0.16));
			params21.setMargins((screenWidth * 1 / 20), screenHeight * 1 / 20,
					0, 0);
			settingBtn.setLayoutParams(params21);

			// disBtn
			FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(
					screenWidth * 1 / 9, screenWidth * 1 / 9);
			params3.setMargins((int) (screenWidth * 0.78),
					(int) ((screenHeight * 0.146)), 0, 0);
			disBtn.setLayoutParams(params3);

			// line1_edit
			setPosi(editText1, screenWidth * 2 / 5, screenWidth * 1 / 11, 0.26,
					0.58);
			// line2_edit
			setPosi(editText2, screenWidth * 2 / 5, screenWidth * 1 / 11, 0.56,
					0.58);

			// 鏃犲０鍠囧彮
			setPosi(button3, screenWidth * 1 / 8, screenWidth * 1 / 9, 0.52,
					0.79);
			// 鏈夊０鍠囧彮
			setPosi(button5, screenWidth * 1 / 8, screenWidth * 1 / 9, 0.38,
					0.79);

			// 纭畾鎸夐挳
			FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(
					screenWidth * 1 / 4, screenWidth * 1 / 9);
			params1.setMargins(screenWidth * 3 / 8,
					(int) (screenHeight * 0.93 / 1.33), 0, 0);
			button4.setLayoutParams(params1);

			// redface
			setPosi(redface, screenWidth * 1 / 7, screenWidth * 1 / 7, 0.28,
					0.44);
			// blueface
			setPosi(blueface, screenWidth * 1 / 7, screenWidth * 1 / 7, 0.58,
					0.44);

			// ///////////////////////////////////////////////////////////////////
			if (AppContext.lanuage) {// zh
				theFrameLayout.setBackgroundResource(R.drawable.background_set);
				guidingBtn.setBackgroundResource(R.drawable.guiding);
				settingBtn.setBackgroundResource(R.drawable.setting);

				button4.setBackgroundResource(R.drawable.confirm);
			} else {
				theFrameLayout
						.setBackgroundResource(R.drawable.background_sete);
				guidingBtn.setBackgroundResource(R.drawable.guidinge);
				settingBtn.setBackgroundResource(R.drawable.settinge);

				button4.setBackgroundResource(R.drawable.confirme);

				editText1.setTextSize(26);
				editText2.setTextSize(26);
			}
		}

		

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void setPosi(ImageButton imageButton, int width, int height,
			Double leftPer, Double rightPer) {
		FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(width,
				height);
		params1.setMargins((int) (screenWidth * leftPer), (int) (screenHeight
				* rightPer / 1.33), 0, 0);
		imageButton.setLayoutParams(params1);

	}

	private void setPosi(ImageView imageView, int width, int height,
			Double leftPer, Double rightPer) {

		FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(width,
				height);
		params1.setMargins((int) (screenWidth * leftPer), (int) (screenHeight
				* rightPer / 1.33), 0, 0);
		imageView.setLayoutParams(params1);
	}

	private void setPosi(EditText editText, int width, int height,
			Double leftPer, Double topPer) {

		FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(width,
				height);
		params1.setMargins((int) (screenWidth * leftPer), (int) (screenHeight
				* topPer / 1.33), 0, 0);
		editText.setAlpha((float) 0.9);
		// editText.setSingleLine(true);
		editText.setTextColor(Color.WHITE);
		editText.setLayoutParams(params1);
		editText.setTextSize(22);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButton_set_exit:// dismiss
			finish();
			break;
		case R.id.set_settingBtn:

			Toast.makeText(this, R.string.setting, Toast.LENGTH_SHORT).show();

			break;
		case R.id.set_guidingBtn:
			Intent intent11 = new Intent(MySettingActivity.this,
					MyGuideActivity.class);
			startActivity(intent11);
			finish();
			break;
		case R.id.imageButton_voice:// 鏈夊０鍠囧彮
			button5.setEnabled(false);
			button3.setEnabled(true);
			button5.setBackgroundResource(R.drawable.voice_blue);
			button3.setBackgroundResource(R.drawable.no_gray);

			MenuActivity.isVoice = true;
			Log.i("info", "beee voice");
			break;
		case R.id.imageButton_no_voice:// 鏃犲０鍠囧彮
			button3.setEnabled(false);
			button5.setEnabled(true);
			button3.setBackgroundResource(R.drawable.voice_gray);
			button5.setBackgroundResource(R.drawable.no_blue);

			MenuActivity.isVoice = false;
			Log.i("info", "noooo voice");
			break;
		case R.id.set_confirm:
			
			AppContext.imageState = imageState;
			if (MenuActivity.isVoice) {
				editor.putString("voice", "be voice");
			} else {
				editor.putString("voice", "no voice");
			}

			editor.putString("user1", editText1.getText().toString());
			editor.putString("user2", editText2.getText().toString());
			editor.commit();

		
			Log.i("diedie", "ys.." + editText1.getText().toString() + "p"
					+ editText2.getText().toString());
			if ((!editText1.getText().toString().equals("请输入用户名"))&& (!editText1.getText().toString().equals("Please Enter"))) {
				AppContext.useName1 = editText1.getText().toString();
			}
			if ((!editText2.getText().toString().equals("请输入用户名"))&& (!editText2.getText().toString().equals("Please Enter")))
				AppContext.useName2 = editText2.getText().toString();

			MySettingActivity.this.finish();
			break;
		default:
			break;
		}

	}
}
