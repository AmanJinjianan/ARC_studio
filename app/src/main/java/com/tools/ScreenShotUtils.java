package com.tools;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.ui.AppContext;
import com.ui.RaceActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class ScreenShotUtils {
	/**
	 * ���н�ȡ��Ļ
	 * 
	 * @param pActivity
	 * @return bitmap
	 */
	public static Bitmap takeScreenShot(Window pActivity) {
		Bitmap bitmap = null;
		//View view = pActivity.getWindow().getDecorView();
		View view = pActivity.getDecorView();
		
		
		// �����Ƿ���Խ��л�ͼ����
		view.setDrawingCacheEnabled(true);
		// ����ͼ�����޷���ǿ�ƹ�����ͼ����
		view.buildDrawingCache();
		// �������������ͼ
		bitmap = view.getDrawingCache();
		//bitmap.

		// ��ȡ״̬���߶�
		Rect frame = new Rect();
		// ������Ļ��͸�
		view.getWindowVisibleDisplayFrame(frame);
		int stautsHeight = frame.top;
		Log.d("jiangqq", "״̬���ĸ߶�Ϊ:" + stautsHeight);

		/*int width = pActivity.getWindowManager().getDefaultDisplay().getWidth();
		int height = pActivity.getWindowManager().getDefaultDisplay().getHeight();*/
		int width = AppContext.screenWidth;
		int height = (int)(AppContext.screenHeight);
		// ����������Ҫ�Ŀ�͸ߴ���bitmap
		bitmap = Bitmap.createBitmap(bitmap, 0, stautsHeight, width, height
				- stautsHeight);
		
		return bitmap;
	}
	
	/**
	 * ����ͼƬ��sdcard��
	 * 
	 * @param pBitmap
	 */
	private static boolean savePic(Bitmap pBitmap, String strName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(strName);
			if (null != fos) {
				pBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ��ͼ
	 * 
	 * @param pActivity
	 * @return ��ͼ���ұ���sdcard�ɹ�����true�����򷵻�false
	 */
	public static boolean shotBitmap(Window pActivity) {
		//public static boolean shotBitmap(Activity pActivity) {

		return ScreenShotUtils.savePic(takeScreenShot(pActivity), "sdcard/gh.png");
//				+ System.currentTimeMillis() + ".png");
		
	}

}
