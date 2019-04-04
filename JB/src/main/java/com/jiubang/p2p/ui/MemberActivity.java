package com.jiubang.p2p.ui;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.JiubangApplication;
import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.FormatUtils;
import com.jiubang.p2p.view.CircleImageView;
import com.jiubang.p2p.volley.LruImageCache;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;

/**
 * 会员中心
 * */
public class MemberActivity extends Activity implements OnClickListener {

	private TextView tv_title_left;
	
	private TextView tv_member_rule;
	private TextView tv_my_privilege;
	private TextView tv_my_privilege_num;
	private LinearLayout ll_privilege;
	private LinearLayout ll_privilege1;
	private ImageView iv_privilege1;
	private TextView tv_privilege1;
	private LinearLayout ll_privilege2;
	private ImageView iv_privilege2;
	private TextView tv_privilege2;
	private LinearLayout ll_privilege3;
	private ImageView iv_privilege3;
	private TextView tv_privilege3;

	private TextView tv_average_assets;
	private TextView tv_coupons;
	private TextView tv_integral;
	
	private CircleImageView iv_head;
	private ImageView iv_level;
	private TextView tv_real_name;

	private LinearLayout ll_average_assets;
	private LinearLayout ll_coupons;
	private LinearLayout ll_integral;
	
	private LinearLayout ll_none;

	private KJHttp http;
	private HttpParams params;
	
	private ImageView drop_down_menu;//右上角下拉菜单
	
	private TitlePopup titlePopup;
	
	private int[] member_levels = { R.drawable.vip_01, R.drawable.vip_02,
			R.drawable.vip_03, R.drawable.vip_04,
			R.drawable.vip_05, R.drawable.vip_06,
			R.drawable.vip_07, R.drawable.vip_08 };

	private int member_level;// 会员等级
	private int sex;// 性别 1：男，0：女
	private String average_assets;// 本月日均总资产
	private int coupons;// 我的卡券
	private int integral;// 可用积分
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member);
		ActivityUtil.getActivityUtil().addActivity(this);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		tv_title_left = (TextView) findViewById(R.id.tv_title_left);
		
		tv_member_rule = (TextView) findViewById(R.id.tv_member_rule);
		tv_my_privilege = (TextView) findViewById(R.id.tv_my_privilege);
		tv_my_privilege_num = (TextView) findViewById(R.id.tv_my_privilege_num);
		ll_privilege = (LinearLayout) findViewById(R.id.ll_privilege);
		ll_privilege1 = (LinearLayout) findViewById(R.id.ll_privilege1);
		iv_privilege1 = (ImageView) findViewById(R.id.iv_privilege1);
		tv_privilege1 = (TextView) findViewById(R.id.tv_privilege1);
		ll_privilege2 = (LinearLayout) findViewById(R.id.ll_privilege2);
		iv_privilege2 = (ImageView) findViewById(R.id.iv_privilege2);
		tv_privilege2 = (TextView) findViewById(R.id.tv_privilege2);
		ll_privilege3 = (LinearLayout) findViewById(R.id.ll_privilege3);
		iv_privilege3 = (ImageView) findViewById(R.id.iv_privilege3);
		tv_privilege3 = (TextView) findViewById(R.id.tv_privilege3);
		
		iv_head = (CircleImageView) findViewById(R.id.iv_head);
		iv_level = (ImageView) findViewById(R.id.iv_level);
		tv_real_name = (TextView) findViewById(R.id.tv_real_name);

		tv_average_assets = (TextView) findViewById(R.id.tv_average_assets);
		tv_coupons = (TextView) findViewById(R.id.tv_coupons);
		tv_integral = (TextView) findViewById(R.id.tv_integral);

		ll_average_assets = (LinearLayout) findViewById(R.id.ll_average_assets);
		ll_coupons = (LinearLayout) findViewById(R.id.ll_coupons);
		ll_integral = (LinearLayout) findViewById(R.id.ll_integral);
		
		ll_none = (LinearLayout) findViewById(R.id.ll_none);
		
		drop_down_menu = (ImageView) findViewById(R.id.drop_down_menu);

		tv_title_left.setOnClickListener(this);
		
		tv_member_rule.setOnClickListener(this);
		ll_privilege1.setOnClickListener(this);
		ll_privilege2.setOnClickListener(this);
		ll_privilege3.setOnClickListener(this);

		ll_average_assets.setOnClickListener(this);
		ll_coupons.setOnClickListener(this);
		ll_integral.setOnClickListener(this);
		
		drop_down_menu.setOnClickListener(this);
		
		init();
	}

	private void init() {
		http = new KJHttp();
		params = new HttpParams();
		getData();
	}

	private void getData() {
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.MEMBER_CENTER, params, httpCallback);
	}

	private HttpCallBack httpCallback = new HttpCallBack(MemberActivity.this) {
		@Override
		public void success(JSONObject ret) {
			super.success(ret);
			try {
				member_level = ret.getInt("member_level");
				average_assets = ret.getString("average_assets");
				coupons = ret.getInt("coupons");
				integral = ret.getInt("integral");
				sex = ret.getInt("sex");
				
				// 头像
				ImageLoader imageLoader = new ImageLoader(JiubangApplication.getHttpQueues(), LruImageCache.instance());
				if (sex == 0) {
					ImageListener listener = ImageLoader.getImageListener(iv_head, R.drawable.head_woman, R.drawable.head_woman);
					imageLoader.get(ret.getString("user_icon_file_id"), listener);
				} else {
					ImageListener listener = ImageLoader.getImageListener(iv_head, R.drawable.head_woman, R.drawable.head_woman);
					imageLoader.get(ret.getString("user_icon_file_id"), listener);
				}
				
				iv_level.setImageResource(member_levels[member_level - 1]);
				tv_real_name.setText(ret.getString("realName"));

				selector(member_level - 1);// 初始化GridView显示位置

				tv_average_assets.setText((average_assets));
				tv_coupons.setText(FormatUtils.fmtMicrometer("" + coupons));
				tv_integral.setText(FormatUtils.fmtMicrometer("" + integral));

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(MemberActivity.this, R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
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

	@SuppressLint("NewApi")
	/**
	 * 选择会员等级
	 * */
	private void selector(int position) {
		switch (position) {
		case 0:
			ll_none.setVisibility(View.VISIBLE);
			tv_my_privilege.setText("V1特权");
			tv_my_privilege_num.setText("共0项");
			iv_privilege1.setImageDrawable(null);
			tv_privilege1.setText("");
			iv_privilege2.setImageDrawable(null);
			tv_privilege2.setText("");
			iv_privilege3.setImageDrawable(null);
			tv_privilege3.setText("");
			break;
		case 1:
			ll_none.setVisibility(View.VISIBLE);
			tv_my_privilege.setText("V2特权");
			tv_my_privilege_num.setText("共0项");
			iv_privilege1.setImageDrawable(null);
			tv_privilege1.setText("");
			iv_privilege2.setImageDrawable(null);
			tv_privilege2.setText("");
			iv_privilege3.setImageDrawable(null);
			tv_privilege3.setText("");
			break;
		case 2:
			ll_none.setVisibility(View.GONE);
			ll_privilege.setBackgroundColor(Color.rgb(255, 255, 255));
			tv_my_privilege.setText("V3特权");
			tv_my_privilege_num.setText("共1项");
			iv_privilege1.setImageDrawable(getResources().getDrawable(
					R.drawable.award_box));
			tv_privilege1.setText("积分奖励");
			iv_privilege2.setImageDrawable(getResources().getDrawable(
					R.drawable.more));
			tv_privilege2.setText("敬请期待");
			iv_privilege3.setImageDrawable(null);
			tv_privilege3.setText("");
			break;
		case 3:
			ll_none.setVisibility(View.GONE);
			ll_privilege.setBackgroundColor(Color.rgb(255, 255, 255));
			tv_my_privilege.setText("V4特权");
			tv_my_privilege_num.setText("共2项");
			iv_privilege1.setImageDrawable(getResources().getDrawable(
					R.drawable.award_box));
			tv_privilege1.setText("积分奖励");
			iv_privilege2.setImageDrawable(getResources().getDrawable(
					R.drawable.birthday_money));
			tv_privilege2.setText("生日红包");
			iv_privilege3.setImageDrawable(getResources().getDrawable(
					R.drawable.more));
			tv_privilege3.setText("敬请期待");
			break;
		case 4:
			ll_none.setVisibility(View.GONE);
			ll_privilege.setBackgroundColor(Color.rgb(255, 255, 255));
			tv_my_privilege.setText("V5特权");
			tv_my_privilege_num.setText("共2项");
			iv_privilege1.setImageDrawable(getResources().getDrawable(
					R.drawable.award_box));
			tv_privilege1.setText("积分奖励");
			iv_privilege2.setImageDrawable(getResources().getDrawable(
					R.drawable.birthday_money));
			tv_privilege2.setText("生日红包");
			iv_privilege3.setImageDrawable(getResources().getDrawable(
					R.drawable.more));
			tv_privilege3.setText("敬请期待");
			break;
		case 5:
			ll_none.setVisibility(View.GONE);
			ll_privilege.setBackgroundColor(Color.rgb(255, 255, 255));
			tv_my_privilege.setText("V6特权");
			tv_my_privilege_num.setText("共2项");
			iv_privilege1.setImageDrawable(getResources().getDrawable(
					R.drawable.award_box));
			tv_privilege1.setText("积分奖励");
			iv_privilege2.setImageDrawable(getResources().getDrawable(
					R.drawable.birthday_money));
			tv_privilege2.setText("生日红包");
			iv_privilege3.setImageDrawable(getResources().getDrawable(
					R.drawable.more));
			tv_privilege3.setText("敬请期待");
			break;
		case 6:
			ll_none.setVisibility(View.GONE);
			ll_privilege.setBackgroundColor(Color.rgb(255, 255, 255));
			tv_my_privilege.setText("V7特权");
			tv_my_privilege_num.setText("共2项");
			iv_privilege1.setImageDrawable(getResources().getDrawable(
					R.drawable.award_box));
			tv_privilege1.setText("积分奖励");
			iv_privilege2.setImageDrawable(getResources().getDrawable(
					R.drawable.birthday_money));
			tv_privilege2.setText("生日红包");
			iv_privilege3.setImageDrawable(getResources().getDrawable(
					R.drawable.more));
			tv_privilege3.setText("敬请期待");
			break;
		case 7:
			ll_none.setVisibility(View.GONE);
			ll_privilege.setBackgroundColor(Color.rgb(255, 255, 255));
			tv_my_privilege.setText("V8特权");
			tv_my_privilege_num.setText("共2项");
			iv_privilege1.setImageDrawable(getResources().getDrawable(
					R.drawable.award_box));
			tv_privilege1.setText("积分奖励");
			iv_privilege2.setImageDrawable(getResources().getDrawable(
					R.drawable.birthday_money));
			tv_privilege2.setText("生日红包");
			iv_privilege3.setImageDrawable(getResources().getDrawable(
					R.drawable.more));
			tv_privilege3.setText("敬请期待");
			break;
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

	@Override
	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.tv_title_left:
			finish();
			break;
		case R.id.tv_member_rule:
			intent = new Intent(MemberActivity.this, MemberRuleActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_privilege1:
			if ("积分奖励".equals(tv_privilege1.getText().toString())) {
				intent = new Intent(MemberActivity.this,
						IntegralRewardActivity.class);
				intent.putExtra("member_level", member_level);
				startActivity(intent);
			} else if ("生日红包".equals(tv_privilege1.getText().toString())) {
				intent = new Intent(MemberActivity.this,
						BirthdayRedActivity.class);
				intent.putExtra("member_level", member_level);
				startActivity(intent);
			} else if ("敬请期待".equals(tv_privilege1.getText().toString())) {
			}
			break;
		case R.id.ll_privilege2:
			if ("积分奖励".equals(tv_privilege2.getText().toString())) {
				intent = new Intent(MemberActivity.this,
						IntegralRewardActivity.class);
				intent.putExtra("member_level", member_level);
				startActivity(intent);
			} else if ("生日红包".equals(tv_privilege2.getText().toString())) {
				intent = new Intent(MemberActivity.this,
						BirthdayRedActivity.class);
				intent.putExtra("member_level", member_level);
				startActivity(intent);
			} else if ("敬请期待".equals(tv_privilege2.getText().toString())) {
			}
			break;
		case R.id.ll_privilege3:
			if ("积分奖励".equals(tv_privilege3.getText().toString())) {
				intent = new Intent(MemberActivity.this,
						IntegralRewardActivity.class);
				intent.putExtra("member_level", member_level);
				startActivity(intent);
			} else if ("生日红包".equals(tv_privilege3.getText().toString())) {
				intent = new Intent(MemberActivity.this,
						BirthdayRedActivity.class);
				intent.putExtra("member_level", member_level);
				startActivity(intent);
			} else if ("敬请期待".equals(tv_privilege3.getText().toString())) {
			}
			break;
		case R.id.ll_coupons:
			intent = new Intent(MemberActivity.this, RedActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_integral:
			intent = new Intent(MemberActivity.this, IntegralMallActivity.class);
			intent.putExtra("type", "1");// 积分明细
			startActivity(intent);
			break;
		case R.id.ll_average_assets:
			intent = new Intent(MemberActivity.this, MemberRuleActivity.class);
			intent.putExtra("type", 2);
			startActivity(intent);
			break;
		case R.id.drop_down_menu:
			titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			// 给标题栏弹窗添加子类
			titlePopup.addAction(new ActionItem(this, "首页", R.drawable.index_menu));
			titlePopup.addAction(new ActionItem(this, "投资", R.drawable.product_menu));
			titlePopup.addAction(new ActionItem(this, "更多", R.drawable.more_menu));
			titlePopup.addAction(new ActionItem(this, "我的", R.drawable.account_menu));
			titlePopup.show(view);
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
