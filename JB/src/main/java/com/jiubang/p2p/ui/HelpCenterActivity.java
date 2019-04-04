package com.jiubang.p2p.ui;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.UIHelper;

/**
 * 帮助中心
 * */
public class HelpCenterActivity extends Activity {
	private WebView web;
	
    private ImageView drop_down_menu;//右上角下拉菜单
	
	private TitlePopup titlePopup;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.helpcenter);
		UIHelper.setTitleView(this, "", "帮助中心");
		ActivityUtil.getActivityUtil().addActivity(this);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		web = (WebView) findViewById(R.id.helpcenter);
		WebSettings ws = web.getSettings();
		ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		// 设置支持缩放
		ws.setSupportZoom(true);
		ws.setUseWideViewPort(true);
		ws.setJavaScriptEnabled(true);
		ws.setLoadWithOverviewMode(true);
		web.loadUrl(AppConstants.FAQ);

		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
		
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		AppVariables.lastTime = new Date().getTime();
		
	}
	
	public void init(){
		drop_down_menu = (ImageView) findViewById(R.id.drop_down_menu);
		drop_down_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				titlePopup = new TitlePopup(HelpCenterActivity.this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				// 给标题栏弹窗添加子类
				titlePopup.addAction(new ActionItem(HelpCenterActivity.this, "首页", R.drawable.index_menu)); 
				titlePopup.addAction(new ActionItem(HelpCenterActivity.this, "投资", R.drawable.product_menu));
				titlePopup.addAction(new ActionItem(HelpCenterActivity.this, "更多", R.drawable.more_menu));
				titlePopup.addAction(new ActionItem(HelpCenterActivity.this, "我的", R.drawable.account_menu));
				titlePopup.show(v);
				titlePopup.setItemOnClickListener(new OnItemOnClickListener() {
					
					public void onItemClick(ActionItem item, int position) {
						Intent intent;
						
						ActivityUtil.getActivityUtil().finishAllActivity();
						
						switch (position) {
						case 0:
							intent = new Intent();  
				            intent.setAction("tab");
				            intent.putExtra("tab", "index");
				            sendBroadcast(intent);
							break;
						case 1:
							intent = new Intent();  
				            intent.setAction("tab");
				            intent.putExtra("tab", "product");
				            sendBroadcast(intent);
							break;
						case 2:
							intent = new Intent();  
				            intent.setAction("tab");
				            intent.putExtra("tab", "more");
				            sendBroadcast(intent);
							break;
						case 3:
							intent = new Intent();  
				            intent.setAction("tab");
				            intent.putExtra("tab", "account");
				            sendBroadcast(intent);
							break;
						}
					}
				});
			}
		});
	}
}
