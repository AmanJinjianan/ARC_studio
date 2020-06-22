package com.myview;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView{

	public MyTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}
	private void init(Context context) {
		AssetManager assertMgr = context.getAssets();
		Typeface font = Typeface.createFromAsset(assertMgr, "fonts/myfont.ttf");
		setTypeface(font);
	}

}
