package com.example.myarcthree.wxapi;//package com.example.myarcthree.wxapi;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.bluetoothARC.R;
//import com.tencent.mm.sdk.constants.ConstantsAPI;
//import com.tencent.mm.sdk.modelbase.BaseReq;
//import com.tencent.mm.sdk.modelbase.BaseResp;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//
//public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
//	private IWXAPI api;
//	public static final String APP_ID = "wxcdbf85625fc7cc5f";
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		//Ӧ�ó����ID
//				api = WXAPIFactory.createWXAPI(this, APP_ID,true);
//				//��APP_IDע�ᵽ΢����
//				api.registerApp(APP_ID);
//		api.handleIntent(getIntent(), this);
//		Log.i("niangde", "----- onCreate  -----");
//	}
//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		setIntent(intent);
//        api.handleIntent(intent, this);
//	}
//	@Override
//	public void onReq(BaseReq req) {
//		switch (req.getType()) {
//		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
////			goToGetMsg();		
//			break;
//		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
////			goToShowMsg((ShowMessageFromWX.Req) req);
//			break;
//		default:
//			break;
//		}
//	}
//
//	@Override
//	public void onResp(BaseResp resp) {
//		int result = 0;
//		
//		switch (resp.errCode) {
//		case BaseResp.ErrCode.ERR_OK:
//			result = R.string.errcode_success;
//			break;
//		case BaseResp.ErrCode.ERR_USER_CANCEL:
//			result = R.string.errcode_cancel;
//			break;
//		case BaseResp.ErrCode.ERR_AUTH_DENIED:
//			result = R.string.errcode_deny;
//			break;
//		default:
//			result = R.string.errcode_unknown;
//			break;
//		}
//		Toast.makeText(this, result, Toast.LENGTH_LONG).show();		
//		this.finish();
//	}
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		Log.i("WXEntryActivity", "----- onDestroy  -----");
//	}
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		Log.i("WXEntryActivity", "----- onPause  -----");
//	}
//	@Override
//	protected void onRestart() {
//		// TODO Auto-generated method stub
//		super.onRestart();
//		Log.i("WXEntryActivity", "----- onRestart  -----");
//	}
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		Log.i("WXEntryActivity", "----- onResume  -----");
//	}
//	
//
//}
