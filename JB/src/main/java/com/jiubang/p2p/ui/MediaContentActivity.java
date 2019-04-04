package com.jiubang.p2p.ui;

import android.content.Intent;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.TextView;

import com.jiubang.p2p.R;
import com.jiubang.p2p.utils.UIHelper;
import com.louding.frame.KJActivity;
import com.louding.frame.ui.BindView;

/**
 * 新闻报道 详情
 * */
public class MediaContentActivity extends KJActivity {

	@BindView(id = R.id.title)
	private TextView mTitle;
	@BindView(id = R.id.data)
	private TextView mData;
	@BindView(id = R.id.tv_source)
	private TextView tv_source;
	@BindView(id = R.id.content)
	private WebView mContent;

	private String title;
	private String data;
	private String content;
	private String source;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_media_content);
		UIHelper.setTitleView(this, "", "");
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
		source = intent.getStringExtra("source");
	}

	@Override
	public void initWidget() {
		super.initWidget();
		mTitle.setText(title);
		mData.setText(data);
		tv_source.setText(source);
		
		StringBuilder sb = new StringBuilder();
		sb.append("<html><head>");
		sb.append("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
//		sb.append("<meta name='viewport' content='width=640, initial-scale=1.0, minimum-scale=1.0, maximum-scale=2.0, user-scalable=yes'>");
		sb.append("<title>媒体报道</title>");
		sb.append("</head><body>");
		sb.append(content);
		sb.append("<script type='text/javascript'>var list=document.getElementsByTagName('img');for (var i = 0; i < list.length; i++) {list[i].width=300;}</script>");
		sb.append("</body></html>");
		
		WebSettings ws = mContent.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setSupportZoom(false);
		ws.setLoadWithOverviewMode(true);
		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mContent.loadDataWithBaseURL(null, sb.toString(), "text/html", "UTF-8", null);
	}

}
