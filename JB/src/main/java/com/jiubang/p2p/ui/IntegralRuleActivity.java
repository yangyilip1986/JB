package com.jiubang.p2p.ui;

import org.json.JSONException;
import org.json.JSONObject;

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

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.FormatUtils;
import com.jiubang.p2p.utils.UIHelper;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;

/**
 * 积分使用规则
 * */
public class IntegralRuleActivity extends Activity implements OnClickListener {

	private TextView tv_1;
	private TextView tv_2;
	private TextView tv_3;
	private TextView tv_4;
	private TextView tv_5;
	private TextView tv_6;
	
	private RelativeLayout rl_text1;
	private RelativeLayout rl_text2;
	private RelativeLayout rl_text3;
	private LinearLayout ll_text1;
	private LinearLayout ll_text2;
	private LinearLayout ll_text3;
	private ImageView iv_arrow1;
	private ImageView iv_arrow2;
	private ImageView iv_arrow3;

	private TextView tv_integral_mall;

	private boolean text1 = true;
	private boolean text2 = true;
	private boolean text3 = true;
	
	private ImageView drop_down_menu;
	
	private TitlePopup titlePopup;
	
	private KJHttp http;
	private HttpParams params;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_integral_rule);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		UIHelper.setTitleView(this, "", "积分使用规则");

		init();
		
		ActivityUtil.getActivityUtil().addActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		http = new KJHttp();
		params = new HttpParams();
		getData();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	private void init() {
		tv_1 = (TextView) findViewById(R.id.tv_1);
		tv_2 = (TextView) findViewById(R.id.tv_2);
		tv_3 = (TextView) findViewById(R.id.tv_3);
		tv_4 = (TextView) findViewById(R.id.tv_4);
		tv_5 = (TextView) findViewById(R.id.tv_5);
		tv_6 = (TextView) findViewById(R.id.tv_6);
		
		rl_text1 = (RelativeLayout) findViewById(R.id.rl_text1);
		rl_text2 = (RelativeLayout) findViewById(R.id.rl_text2);
		rl_text3 = (RelativeLayout) findViewById(R.id.rl_text3);
		ll_text1 = (LinearLayout) findViewById(R.id.ll_text1);
		ll_text2 = (LinearLayout) findViewById(R.id.ll_text2);
		ll_text3 = (LinearLayout) findViewById(R.id.ll_text3);
		iv_arrow1 = (ImageView) findViewById(R.id.iv_arrow1);
		iv_arrow2 = (ImageView) findViewById(R.id.iv_arrow2);
		iv_arrow3 = (ImageView) findViewById(R.id.iv_arrow3);
		
		drop_down_menu = (ImageView) findViewById(R.id.drop_down_menu);

		tv_integral_mall = (TextView) findViewById(R.id.tv_integral_mall);

		rl_text1.setOnClickListener(this);
		rl_text2.setOnClickListener(this);
		rl_text3.setOnClickListener(this);
		drop_down_menu.setOnClickListener(this);

		tv_integral_mall.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
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
		case R.id.tv_integral_mall:
			intent = new Intent(IntegralRuleActivity.this,
					IntegralMallActivity.class);
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
	
	
	
	private void getData() {
		http.post(AppConstants.POINT_ALL, params, new HttpCallBack(IntegralRuleActivity.this) {
			// 下面这两个方法必须重写覆盖掉父方法中的对话框显示
			@Override
			public void onPreStart() {
			}
			
			@Override
			public void onFinish() {
			}
			
			@Override
			public void onSuccess(String t) {
				try {
					JSONObject ret = new JSONObject(t);
					
					
					tv_1.setText("1、新手注册送" + ret.getString("POINT_TYPE_REGIST") + "积分;");
					tv_2.setText("2、邀请好友注册送"+ ret.getString("POINT_TYPE_FRIEND_REGIST") +"积分;");
					tv_3.setText("3、每日签到奖励10积分，连续7日签到额外奖励50积分;");
					tv_4.setText("4、投资产品按照投资金额年化"+ FormatUtils.numFormat(ret.getDouble("POINT_TYPE_TENDER_PERSENT") * 100 + "") + "%送相应积分;\n投资奖励积分=投资金额*项目期限/365天*"+ ret.getString("POINT_TYPE_TENDER_PERSENT") +";");
					tv_5.setText("5、会员奖励积分及活动返还积分，具体见会员奖励规则;");
					tv_6.setText("6、其他。");
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
