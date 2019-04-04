package com.jiubang.p2p.ui;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.ToastCommom;
import com.jiubang.p2p.utils.UIHelper;

/**
 * 积分奖励
 * */
public class IntegralRewardActivity extends Activity implements OnClickListener {

	private TextView tv_level;

	private TextView tv_explain1;
	private TextView tv_explain2;
	private TextView tv_explain3;

	private TextView tv_vip;
	
    private ImageView drop_down_menu;//右上角下拉菜单
	
	private TitlePopup titlePopup;

	private boolean integralFlag;

	private KJHttp http;
	private HttpParams params;

	private int member_level;
	
	private TextView tv_dole;
	private TextView tv_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_reward);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		UIHelper.setTitleView(this, "", "积分奖励");

		init();
		ActivityUtil.getActivityUtil().addActivity(this);
	}

	private void init() {

		http = new KJHttp();
		params = new HttpParams();

		member_level = getIntent().getIntExtra("member_level", 1);

		tv_level = (TextView) findViewById(R.id.tv_level);
		tv_explain1 = (TextView) findViewById(R.id.tv_explain1);
		tv_explain2 = (TextView) findViewById(R.id.tv_explain2);
		tv_explain3 = (TextView) findViewById(R.id.tv_explain3);
		
		drop_down_menu = (ImageView) findViewById(R.id.drop_down_menu);

		tv_dole = (TextView) findViewById(R.id.tv_dole);
		tv_text = (TextView) findViewById(R.id.tv_text);
		
		tv_vip = (TextView) findViewById(R.id.tv_vip);

		tv_level.setText("V" + member_level + "会员专享");

		tv_dole.setOnClickListener(this);
		drop_down_menu.setOnClickListener(this);

		getData(member_level);
	}

	private void getData(int vip) {
		params.put("sid", AppVariables.sid);
		params.put("vip", vip);
		http.post(AppConstants.VIP, params, httpCallback);
	}

	private HttpCallBack httpCallback = new HttpCallBack(
			IntegralRewardActivity.this) {
		@Override
		public void success(JSONObject ret) {
			super.success(ret);
			try {

				integralFlag = ret.getBoolean("integralFlag");
				
				tv_vip.setText(ret.getInt("vip") + "积分");

				if (integralFlag) {
					tv_text.setText("");
					tv_dole.setText("点击领取");
					tv_dole.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_text_blue));
					tv_dole.setTextColor(getResources().getColor(R.color.app_blue));
				} else {
					tv_dole.setText("已领取");
					tv_dole.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_text_grey));
					tv_dole.setTextColor(getResources().getColor(R.color.app_font_light));
					// unavailable 0:未领取 1：已领取 2：已过期
					if ("0".equals(ret.getString("unavailable"))) {
						tv_text.setText("");
					} else if ("1".equals(ret.getString("unavailable"))) {
						tv_text.setText("您这个月已领取，请下个月再来哦");
					} else if ("2".equals(ret.getString("unavailable"))) {
						tv_dole.setText("已过期");
						tv_text.setText("还未到领取时间");
					}
				}

				tv_explain1.setText("V3-V8会员，九邦信投网每月奖励200-10000不等数量积分");
				tv_explain2.setText("每月1-10号领取，每个会员每月限领1次");
				tv_explain3.setText("已完成实名验证的V3，V4，V5，V6，V7，V8会员");

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(IntegralRewardActivity.this,
						R.string.app_data_error, Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
		}

		@Override
		public void failure(JSONObject ret) {
			super.failure(ret);
		}
	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_dole:
			if(integralFlag) {
				// 领取积分
				params = new HttpParams();
				params.put("sid", AppVariables.sid);
				http.post(AppConstants.GET_POINTS, params, httpCallback2);
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

	private HttpCallBack httpCallback2 = new HttpCallBack(
			IntegralRewardActivity.this) {
		@Override
		public void success(JSONObject ret) {
			super.success(ret);
			try {
				if (ret.getBoolean("successFlag")) {
					onCreate(null);
					ToastCommom toastCommom = ToastCommom.createToastConfig();
					toastCommom.ToastShow(IntegralRewardActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "积分领取成功");
				} else {
					ToastCommom toastCommom = ToastCommom.createToastConfig();
					toastCommom.ToastShow(IntegralRewardActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "积分领取失败");
				}

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(IntegralRewardActivity.this,
						R.string.app_data_error, Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
		}

		@Override
		public void failure(JSONObject ret) {
			super.failure(ret);
		}
	};

}
