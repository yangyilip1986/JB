package com.jiubang.p2p.ui;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.js.JavaScriptInterface;
import com.jiubang.p2p.utils.ActivityUtil;

/**
 * 易宝
 * */
public class YibaoActivity extends Activity implements OnClickListener {

	private WebView webView;
	private TextView title_left;
	private TextView title_right;
	
	private ImageView drop_down_menu;//右上角下拉菜单按钮
	private TitlePopup titlePopup;

	private String mode;// 第三方模式：yeepay：易宝，gateway：宝付
	
	private String req;
	private String sign;
	private String uri;

	private String call_back;

	private boolean back = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yibao);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		init();

		WebSettings webSettings = webView.getSettings();
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		webView.addJavascriptInterface(new JavaScriptInterface(
				YibaoActivity.this, this), "JsObject");

//		if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT) { // web测试
//		   WebView.setWebContentsDebuggingEnabled(true);  
//		}
		
		if("gateway".equals(mode)) { // 宝付
			webView.setWebViewClient(new MyClientBF());
			webView.loadUrl(AppConstants.SPECIALHOST + "/" + call_back);
		} else { // 易宝
			webView.setWebViewClient(new MyClient());
			webView.loadUrl("file:///android_asset/CommonWebView.html");
		}
	}

	class MyClientBF extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			title_left.setVisibility(View.VISIBLE);
			back = true;
		}
	}

	class MyClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			webView.loadUrl("javascript:connectYiBao('" + req + "','" + sign
					+ "','" + uri + "')");
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			if (url.contains("member/bhawireless/swiftWithdraw") || url.contains("member/bhawireless/swiftRecharge")) {
				title_left.setVisibility(View.GONE);
				back = false;
			} else {
				title_left.setVisibility(View.VISIBLE);
				back = true;
			}
			super.onPageStarted(view, url, favicon);
		}
	}

	private void init() {
		title_left = (TextView) findViewById(R.id.title_left);
		title_right = (TextView) findViewById(R.id.title_right);

		drop_down_menu = (ImageView) findViewById(R.id.drop_down_menu);
		
		title_left.setText("");
		title_right.setText("");

		title_left.setOnClickListener(this);
		drop_down_menu.setOnClickListener(this);

		Intent intent = getIntent();
		mode = intent.getStringExtra("mode");
		
		if("gateway".equals(mode)) { // 宝付
			call_back = intent.getStringExtra("call_back");
		} else { // 易宝
			req = intent.getStringExtra("req");
			sign = intent.getStringExtra("sign");
			uri = intent.getStringExtra("uri");
		}

		webView = (WebView) findViewById(R.id.webView);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			if (back) {
				Intent intent = new Intent();
	            YibaoActivity.this.setResult(2, intent);
				YibaoActivity.this.finish();
			}
			break;
		case R.id.drop_down_menu:
			titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			// 给标题栏弹窗添加子类
			titlePopup.addAction(new ActionItem(this, "首页", R.drawable.index_menu));
			titlePopup.addAction(new ActionItem(this, "投资", R.drawable.product_menu));
			titlePopup.addAction(new ActionItem(this, "更多", R.drawable.more_menu));
			titlePopup.addAction(new ActionItem(this, "我的", R.drawable.account_menu));
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
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			if (back) {
				Intent intent = new Intent();
                YibaoActivity.this.setResult(2, intent);
				YibaoActivity.this.finish();
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
