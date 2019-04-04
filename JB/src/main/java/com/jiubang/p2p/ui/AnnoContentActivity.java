package com.jiubang.p2p.ui;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.UIHelper;
import com.louding.frame.KJActivity;
import com.louding.frame.ui.BindView;

/**
 * 平台公告 详情
 * */
public class AnnoContentActivity extends KJActivity {

	@BindView(id = R.id.title)
	private TextView mTitle;
	@BindView(id = R.id.data)
	private TextView mData;
	@BindView(id = R.id.content)
	private WebView mContent;
	
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;//右上角下拉菜单
	
	private TitlePopup titlePopup;

	private String title;
	private String data;
	private String content;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_anno_content);

		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		UIHelper.setTitleView(this, "", "公告详情");
		ActivityUtil.getActivityUtil().addActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void initData() {
		super.initData();
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		data = intent.getStringExtra("data");
		content = intent.getStringExtra("content");
	}

	@Override
	public void initWidget() {
		super.initWidget();
		mTitle.setText(title);
		mData.setText(data);
		String html = "<html><head>"
				+ "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>"
				+ "<title>平台公告</title>"
				+ "</head><body>"
				+ content
				+ "<script type='text/javascript'>var list=document.getElementsByTagName('img');for (var i = 0; i < list.length; i++) {list[i].width=300;}</script>"
				+ "</body></html>";
		WebSettings ws = mContent.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setSupportZoom(false);
		ws.setLoadWithOverviewMode(true);
		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mContent.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
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
		default:
			break;
		}
	}

}
