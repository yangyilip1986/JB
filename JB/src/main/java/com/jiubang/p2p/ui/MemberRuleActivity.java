package com.jiubang.p2p.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.UIHelper;

/**
 * 会员规则
 * */
public class MemberRuleActivity extends Activity implements OnClickListener {

	private RelativeLayout rl_text1;
	private RelativeLayout rl_text2;
	private RelativeLayout rl_text3;
	private RelativeLayout rl_text4;
	private LinearLayout ll_text1;
	private LinearLayout ll_text2;
	private LinearLayout ll_text3;
	private LinearLayout ll_text4;
	private ImageView iv_arrow1;
	private ImageView iv_arrow2;
	private ImageView iv_arrow3;
	private ImageView iv_arrow4;

	private TextView tv_checked;
	
    private ImageView drop_down_menu;//右上角下拉菜单
	
	private TitlePopup titlePopup;

	private boolean text1 = true;
	private boolean text2 = true;
	private boolean text3 = true;
	private boolean text4 = true;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_member_rule);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		UIHelper.setTitleView(this, "", "会员规则");

		init();
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

	private void init() {
		rl_text1 = (RelativeLayout) findViewById(R.id.rl_text1);
		rl_text2 = (RelativeLayout) findViewById(R.id.rl_text2);
		rl_text3 = (RelativeLayout) findViewById(R.id.rl_text3);
		rl_text4 = (RelativeLayout) findViewById(R.id.rl_text4);
		ll_text1 = (LinearLayout) findViewById(R.id.ll_text1);
		ll_text2 = (LinearLayout) findViewById(R.id.ll_text2);
		ll_text3 = (LinearLayout) findViewById(R.id.ll_text3);
		ll_text4 = (LinearLayout) findViewById(R.id.ll_text4);
		iv_arrow1 = (ImageView) findViewById(R.id.iv_arrow1);
		iv_arrow2 = (ImageView) findViewById(R.id.iv_arrow2);
		iv_arrow3 = (ImageView) findViewById(R.id.iv_arrow3);
		iv_arrow4 = (ImageView) findViewById(R.id.iv_arrow4);

		tv_checked = (TextView) findViewById(R.id.tv_checked);
		
		drop_down_menu = (ImageView) findViewById(R.id.drop_down_menu);

		rl_text1.setOnClickListener(this);
		rl_text2.setOnClickListener(this);
		rl_text3.setOnClickListener(this);
		rl_text4.setOnClickListener(this);

		tv_checked.setOnClickListener(this);
		drop_down_menu.setOnClickListener(this);

		int type = getIntent().getIntExtra("type", 0);

		switch (type) {
		case 1:
			text1 = false;
			iv_arrow1.setImageResource(R.drawable.top_arrow);
			ll_text1.setVisibility(View.VISIBLE);
			break;
		case 2:
			text2 = false;
			iv_arrow2.setImageResource(R.drawable.top_arrow);
			ll_text2.setVisibility(View.VISIBLE);
			break;
		case 3:
			text3 = false;
			iv_arrow3.setImageResource(R.drawable.top_arrow);
			ll_text3.setVisibility(View.VISIBLE);
			break;
		case 4:
			text4 = false;
			iv_arrow4.setImageResource(R.drawable.top_arrow);
			ll_text4.setVisibility(View.VISIBLE);
			break;
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rl_text1:
			if (text1) {
				ll_text1.setVisibility(View.VISIBLE);
				iv_arrow1.setImageResource(R.drawable.top_arrow);
				text1 = false;
			} else {
				ll_text1.setVisibility(View.GONE);
				iv_arrow1.setImageResource(R.drawable.down_arrow);
				text1 = true;
			}
			break;
		case R.id.rl_text2:
			if (text2) {
				ll_text2.setVisibility(View.VISIBLE);
				iv_arrow2.setImageResource(R.drawable.top_arrow);
				text2 = false;
			} else {
				ll_text2.setVisibility(View.GONE);
				iv_arrow2.setImageResource(R.drawable.down_arrow);
				text2 = true;
			}
			break;
		case R.id.rl_text3:
			if (text3) {
				ll_text3.setVisibility(View.VISIBLE);
				iv_arrow3.setImageResource(R.drawable.top_arrow);
				text3 = false;
			} else {
				ll_text3.setVisibility(View.GONE);
				iv_arrow3.setImageResource(R.drawable.down_arrow);
				text3 = true;
			}
			break;
		case R.id.rl_text4:
			if (text4) {
				ll_text4.setVisibility(View.VISIBLE);
				iv_arrow4.setImageResource(R.drawable.top_arrow);
				text4 = false;
			} else {
				ll_text4.setVisibility(View.GONE);
				iv_arrow4.setImageResource(R.drawable.down_arrow);
				text4 = true;
			}
			break;
		case R.id.tv_checked:
			Intent intent = new Intent(MemberRuleActivity.this, InvestActivity.class);
			intent.putExtra("state", 1);
			startActivity(intent);
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
