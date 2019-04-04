package com.jiubang.p2p.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.UIHelper;

/**
 * 债权转让规则
 * */
public class TransferRuleActivity extends Activity implements OnClickListener {

	private RelativeLayout rl_agree;
	private TextView tv_agree;
    private ImageView drop_down_menu;//右上角下拉菜单
	private TitlePopup titlePopup;
	private WebView wv_transferrule;

	private String oid_tender_id;
	private String tender_from;
	private String flag;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_transfer_rule);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		UIHelper.setTitleView(this, "", "转让规则");
		ActivityUtil.getActivityUtil().addActivity(this);
		init();
		initdata();

		Bundle extras = getIntent().getExtras();
		flag = extras.getString("flag");
		if ("".equals(flag)) {
			oid_tender_id = extras.getString("oid_tender_id");
			tender_from = extras.getString("tender_from");
		} else {
			rl_agree.setVisibility(View.GONE);
		}
	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void init() {
		rl_agree = (RelativeLayout) findViewById(R.id.rl_agree);
		tv_agree = (TextView) findViewById(R.id.tv_agree);
		drop_down_menu = (ImageView) findViewById(R.id.drop_down_menu);
		wv_transferrule = (WebView) findViewById(R.id.wv_transferrule);
		drop_down_menu.setOnClickListener(this);
		tv_agree.setOnClickListener(this);
	}
	
	private void initdata(){
		wv_transferrule.loadUrl(AppConstants.SPECIALHOST+"/TransferRule.html");
		wv_transferrule.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_agree:
			Intent intent = new Intent(TransferRuleActivity.this, TransferActivity.class);
			intent.putExtra("oid_tender_id", oid_tender_id);
			intent.putExtra("tender_from", tender_from);
			startActivity(intent);
			TransferRuleActivity.this.finish();
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
