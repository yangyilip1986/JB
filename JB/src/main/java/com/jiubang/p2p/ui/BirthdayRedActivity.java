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
 * 生日红包
 * */
public class BirthdayRedActivity extends Activity implements OnClickListener {

	private TextView tv_level;

	private TextView tv_explain1;
	private TextView tv_explain2;
	private TextView tv_explain3;
	
	private TextView tv_dole;
	private TextView tv_text;
	private TextView tv_vip;
	
    private ImageView drop_down_menu;//右上角下拉菜单
	
	private TitlePopup titlePopup;

	private int member_level;

	private KJHttp http;
	private HttpParams params;

	private boolean birthdayRedFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_birthday);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		UIHelper.setTitleView(this, "", "生日红包");

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
		
		tv_dole = (TextView) findViewById(R.id.tv_dole);
		tv_text = (TextView) findViewById(R.id.tv_text);
		tv_vip = (TextView) findViewById(R.id.tv_vip);

		drop_down_menu = (ImageView) findViewById(R.id.drop_down_menu);
		
		tv_level.setText("V" + member_level + "会员专享");

		tv_dole.setOnClickListener(this);
		drop_down_menu.setOnClickListener(this);

		getData(member_level);

	}

	private void getData(int vip) {
		params.put("sid", AppVariables.sid);
		params.put("vip", vip);
		http.post(AppConstants.BIRTHDAY, params, httpCallback);
	}

	private HttpCallBack httpCallback = new HttpCallBack(
			BirthdayRedActivity.this) {
		@Override
		public void success(JSONObject ret) {
			super.success(ret);
			try {

				birthdayRedFlag = ret.getBoolean("birthdayRedFlag");
				int redMoney = (int) Double.parseDouble(ret.getString("redMoney"));
				tv_vip.setText(redMoney + "元");
				if (birthdayRedFlag) {
					tv_dole.setText("点击领取");
					tv_dole.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_text_blue));
					tv_dole.setTextColor(getResources().getColor(R.color.app_blue));
					tv_text.setText("您这个月还有生日红包未领取，请点击领取");
				} else {
					tv_dole.setText("已过期");
					tv_dole.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_text_grey));
					tv_dole.setTextColor(getResources().getColor(R.color.app_font_light));
					tv_text.setText("");
					if ("1".equals(ret.getString("unavailable"))) {
						// 已领取
						tv_dole.setText("已领取");
						tv_text.setText("您已领取");
					}
				}

				tv_explain1.setText("V4-V8会员生日当月，九邦信投网会送出10-100元不等金额现金券");
				tv_explain2.setText("生日当月内可领取，每位会员每年限领1次");
				tv_explain3.setText("已完成实名验证的V4，V5，V6，V7，V8会员");

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(BirthdayRedActivity.this, R.string.app_data_error, Toast.LENGTH_SHORT).show();
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
			if(birthdayRedFlag) {
				// 领取现金券
				params = new HttpParams();
				params.put("sid", AppVariables.sid);
				http.post(AppConstants.GET_BIRTHDAY_RED, params, httpCallback2);
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
			BirthdayRedActivity.this) {
		@Override
		public void success(JSONObject ret) {
			super.success(ret);
			try {
				if (ret.getBoolean("successFlag")) {
					onCreate(null);
					ToastCommom toastCommom = ToastCommom.createToastConfig();
					toastCommom.ToastShow(BirthdayRedActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "生日现金券领取成功");
				} else {
					ToastCommom toastCommom = ToastCommom.createToastConfig();
					toastCommom.ToastShow(BirthdayRedActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "生日现金券领取失败");
				}

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(BirthdayRedActivity.this,
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
