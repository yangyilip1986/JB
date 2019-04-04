package com.jiubang.p2p.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.js.JavaScriptInterface;
import com.jiubang.p2p.utils.ActivityUtil;
import com.louding.frame.KJActivity;
import com.louding.frame.ui.BindView;

/**
 * 宝付充值、提现 web页
 * */
@SuppressLint("NewApi")
public class ChargeCashBFActivity extends KJActivity {

	@BindView(id = R.id.webView)
	private WebView webView;
	@BindView(id = R.id.title_left, click=true)
	private TextView title_left;
	@BindView(id = R.id.title_center)
	private TextView title_center;
	@BindView(id = R.id.title_right, click=true)
	private TextView title_right;
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;//右上角下拉菜单
	@BindView(id = R.id.pb_progressBar)
	private ProgressBar pb_progressBar;
	
	private TitlePopup titlePopup;
	
	private String type = "";
	private String bankAccount = "";
	private String bf_bank_id = "";
	private int cardStatus = 0;// 绑卡标识 2：有效 0无效
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_charge_bf);
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		ActivityUtil.getActivityUtil().addActivity(this);
		
	}
	
	@Override
	public void initData() {
		super.initData();
		
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		bankAccount = intent.getStringExtra("bankAccount");
		bf_bank_id = intent.getStringExtra("bf_bank_id");
		cardStatus = intent.getIntExtra("cardStatus", 0);
		
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSaveFormData(true);
		webSettings.setSupportZoom(true);// 设定支持缩放   
		webView.addJavascriptInterface(new JavaScriptInterface(ChargeCashBFActivity.this, this), "JsObject");
		webView.setWebViewClient(new MyClient());
//		if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT) { // web测试
//		   WebView.setWebContentsDebuggingEnabled(true);  
//		}
		
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					pb_progressBar.setVisibility(View.INVISIBLE);
				} else {
					if (View.INVISIBLE == pb_progressBar.getVisibility()) {
						pb_progressBar.setVisibility(View.VISIBLE);
					}
					pb_progressBar.setProgress(newProgress);
	          }
				super.onProgressChanged(view, newProgress);
			}
		});
		
		switch (type) {
		case "charge":
			title_left.setText("");
			title_center.setText("充值");
			drop_down_menu.setVisibility(View.GONE);
			if(cardStatus == 0) {// 无效
				webView.loadUrl(AppConstants.RECHARGE_NO_WAPP + "?uid=" + AppVariables.uid + "&bf_bank_id=" + "&fromWhere=1");
			} else {
				webView.loadUrl(AppConstants.RECHARGE_YES_WAPP + "?uid=" + AppVariables.uid + "&bf_bank_id=" + bf_bank_id + "&bankAccount=" + bankAccount);
				title_right.setText("充值记录");
			}
			break;
		case "cash":
			title_left.setText("");
			title_center.setText("提现");
			
			if(cardStatus == 0) {// 无效
				webView.loadUrl(AppConstants.CASH_NO_WAPP + "?uid=" + AppVariables.uid + "&bf_bank_id=" + "&fromWhere=2");
			} else {
				webView.loadUrl(AppConstants.CASH_YES_WAPP + "?uid=" + AppVariables.uid + "&bf_bank_id=" + bf_bank_id + "&bankAccount=" + bankAccount);
			}
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	class MyClient extends WebViewClient {
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
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void initWidget() {
		super.initWidget();
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.title_left:
			ChargeCashBFActivity.this.finish();
			break;
		case R.id.title_right:
			switch (type) {
			case "charge":
				intent = new Intent(this, ChargeNotesActivity.class);
				startActivity(intent);
				break;
			case "cash":
				intent = new Intent(this, CashCaptionActivity.class);
				startActivity(intent);
				break;
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
}
