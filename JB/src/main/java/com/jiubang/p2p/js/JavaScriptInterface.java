package com.jiubang.p2p.js;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.friends.Wechat.ShareParams;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.SelectPicPopupWindow;
import com.jiubang.p2p.ui.InvestActivity;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.ToastCommom;

public class JavaScriptInterface {
	private static JavaScriptInterface mInterface = null;
	private Context mAppContext;
	private Activity mActivity;
	
	private String shareUrl;
	
	private SelectPicPopupWindow menuWindow;

	public static JavaScriptInterface getInstance(Context context) {
		if (mInterface == null) {
			mInterface = new JavaScriptInterface(context);
		}
		return mInterface;
	}
	
	private JavaScriptInterface(Context context) {
		mAppContext = context;
	}
	
	public JavaScriptInterface(Context context, Activity activity) {
		mAppContext = context;
		mActivity = activity;
	}

	/**
	 * 分享
	 * */
	@JavascriptInterface
	public void share(String type, String shareUrl) {
		if("1".equals(type)) { // 查看投资
			Intent intent = new Intent(mAppContext, InvestActivity.class);
			intent.putExtra("state", 2);
			mAppContext.startActivity(intent);
		} else {
			this.shareUrl = shareUrl;
			menuWindow = new SelectPicPopupWindow(mActivity, itemsOnClick);
			menuWindow.showAtLocation(mActivity.findViewById(R.id.rl_web), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
		}
	}
	
	private OnClickListener  itemsOnClick = new OnClickListener(){  
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
            case R.id.ll_wechat:
            	shareWechat(shareUrl, Wechat.NAME);
                break;  
            case R.id.ll_wechat_moments:
            	shareWechat(shareUrl, WechatMoments.NAME);
                break;
            default:
                break;
            }
        }  
    };
	
	/**
	 * finish
	 * */
	@JavascriptInterface
	public void finish() {
		mActivity.finish();
	}
	
	/**
	 * 返回首页
	 * */
	@JavascriptInterface
	public void backIndex() {
		ActivityUtil.getActivityUtil().finishAllActivity();
		Intent intent = new Intent();  
        intent.setAction("tab");
        intent.putExtra("tab", "index");
        mActivity.sendBroadcast(intent);
	}
	
	/**
	 * Toast
	 * */
	@JavascriptInterface
	public void showToast(String strData) {
		ToastCommom toastCommom = ToastCommom.createToastConfig();
		toastCommom.ToastShow(mAppContext, (ViewGroup) mActivity.findViewById(R.id.toast_layout_root), strData);
	}
	
	/**
	 * 微信好友分享
	 * */
	private void shareWechat(String shareUrl, String name) {
		ShareSDK.initSDK(mAppContext);
		ShareParams paramsToShare = new ShareParams();
		paramsToShare.setTitle("快来领九邦信投网加息券吧，我只想让你任性的赚钱！");
		paramsToShare.setText("最近我为什么这么有钱，全靠九邦信投网呢！");
		paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
		paramsToShare.setUrl(shareUrl);
		paramsToShare.setImageUrl("http://ylc-site-dev.oss-cn-hangzhou.aliyuncs.com/test-commodity-avatar/549640bbfaee3cd77479714bedbb4e8f-155d851c1e2.png");
		
		Platform platform;
		if(Wechat.NAME.equals(name)){
			platform = ShareSDK.getPlatform(Wechat.NAME);
			// 设置分享事件回调
			platform.setPlatformActionListener(listener); 
			// 执行图文分享
			platform.share(paramsToShare);
		} else if(WechatMoments.NAME.equals(name)) {
			platform = ShareSDK.getPlatform(WechatMoments.NAME);
			// 设置分享事件回调
			platform.setPlatformActionListener(listener); 
			// 执行图文分享
			platform.share(paramsToShare);
		}
		
	}
	
	PlatformActionListener listener = new PlatformActionListener(){
		public void onCancel(Platform arg0, int arg1) {
		}
		public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		}
		public void onError(Platform arg0, int arg1, Throwable arg2) {
		}
	};
}
