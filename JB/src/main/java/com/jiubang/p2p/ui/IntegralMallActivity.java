package com.jiubang.p2p.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.adapter.IntegralMallAdapter;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.NetWorkUtils;
import com.jiubang.p2p.utils.ToastCommom;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;

/**
 * 积分商城
 * */
public class IntegralMallActivity extends KJActivity {

	@BindView(id = R.id.tv_title_left, click = true)
	private TextView tv_title_left;
	@BindView(id = R.id.tv_title_right, click = true)
	private TextView tv__title_right;
	@BindView(id = R.id.tv_usable_point_m)
	private TextView tv_usable_point_m;
	@BindView(id = R.id.tv_sign, click = true)
	private TextView tv_sign;
	@BindView(id = R.id.tv_text)
	private TextView tv_text;
	@BindView(id = R.id.tv_sign_cnt)
	private TextView tv_sign_cnt;
	@BindView(id = R.id.tv_sign_point)
	private TextView tv_sign_point;
	@BindView(id = R.id.iv_background)
	private ImageView iv_background;
	@BindView(id = R.id.tv_qiandao)
	private TextView tv_qiandao;
	@BindView(id = R.id.empty)
	private TextView empty;
	@BindView(id = R.id.btn_ok, click = true)
	private Button btn_ok;
	@BindView(id = R.id.ll_pop_sign, click = true)
	private LinearLayout ll_pop_sign;
	@BindView(id = R.id.tv_integral_rule, click = true)
	private TextView tv_integral_rule;

	@BindView(id = R.id.gv_gridview)
	private GridView gv_gridview;
	
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;
	
	private TitlePopup titlePopup;

	private SimpleAdapter gridviewAdapter;
	private List<Map<String, Object>> data_list;

	private String type = "";

	private HttpParams params;
	private KJHttp http;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_integral_mall);

		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		ActivityUtil.getActivityUtil().addActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		NetWorkUtils.show_wifi_empty(this, empty);
		if(!NetWorkUtils.isNetworkConnected(this)) {
			empty.setVisibility(View.VISIBLE);
		} else {
			empty.setVisibility(View.GONE);
		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void initData() {

		params = new HttpParams();
		http = new KJHttp();
		getData();
	}

	private void getData() {
		params.put("sid", AppVariables.sid);
		params.put("type", type);
		http.post(AppConstants.MY_INTEGRAL_MALL, params, httpCallback);
	}

	private HttpCallBack httpCallback = new HttpCallBack(
			IntegralMallActivity.this) {

		@Override
		public void onSuccess(String t) {
			try {
				JSONObject ret = new JSONObject(t);
				JSONArray mallArray = ret.getJSONArray("mallMapList");

				tv_usable_point_m.setText(ret.getString("usable_point_m"));
				if(ret.getBoolean("is_sign")) {
					tv_sign.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_text_grey));
					tv_sign.setText("已签到");
					tv_sign.setTextColor(getResources().getColor(R.color.app_font_light));
					if(ret.getInt("signday") == 0) {
						tv_sign_cnt.setText("已连续签到7天");
					} else {
						tv_sign_cnt.setText("已连续签到" + ret.getInt("signday") + "天");
					}
					
					if(ret.getInt("signday") == 6) {
						tv_sign_point.setText("明天继续签到将获得 60 点积分");
					} else {
						tv_sign_point.setText("明天继续签到将获得 10 点积分");
					}
					
				} else {
					tv_sign_cnt.setText("今日未签到");
					if(ret.getInt("signday") == 6) {
						tv_sign_point.setText("今日签到将获得 60 点积分");
					} else {
						tv_sign_point.setText("今日签到将获得 10 点积分");
					}
				}
				
				data_list = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < mallArray.length(); i++) {
					JSONObject o = (JSONObject) mallArray.get(i);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("img_path", o.getString("img_path"));
					map.put("cost_point", o.getString("cost_point"));
					map.put("description", o.getString("description"));
					map.put("title", o.getString("title"));
					map.put("cost_money", o.getString("cost_money"));
					map.put("red_packet_id", o.getString("red_packet_id"));
					map.put("commodity_id", o.getString("commodity_id"));
					map.put("stock", o.getInt("stock"));
					data_list.add(map);
				}
				
				if(data_list.size() == 0) {
					empty.setVisibility(View.VISIBLE);
				} else {
					empty.setVisibility(View.GONE);
				}
				String[] from = { "img_path", "cost_point", "description" };
				int[] to = { R.id.iv_image, R.id.tv_integral, R.id.tv_describe };
				gridviewAdapter = new IntegralMallAdapter(IntegralMallActivity.this, data_list, R.layout.item_integral_mall, from, to, type, tv_usable_point_m);
				gv_gridview.setAdapter(gridviewAdapter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void onFinish() {
			super.onFinish();
		}
	};

	@Override
	public void widgetClick(View v) {
		Intent intent;
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.tv_title_left:
			finish();
			break;
		case R.id.tv_title_right:
			intent = new Intent(IntegralMallActivity.this, IntegralDetailActivity.class);
			intent.putExtra("type", "1");// 积分明细
			startActivity(intent);
			break;
		case R.id.tv_sign:
			sign();
			break;
		case R.id.btn_ok:
			ll_pop_sign.setVisibility(View.GONE);
			break;
		case R.id.tv_integral_rule:
			intent = new Intent(IntegralMallActivity.this, IntegralRuleActivity.class);
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
	
	/**
	 * 签到
	 * */
	private void sign() {
		http = new KJHttp();
		params = new HttpParams();
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.MY_USER_SIGN, params, new HttpCallBack(IntegralMallActivity.this) {
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
					if (ret.getBoolean("usersign")) {
						tv_sign.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_text_grey));;
						tv_sign.setText("已签到");
						tv_sign.setTextColor(getResources().getColor(R.color.app_font_light));
						if (ret.getInt("signCnt") == 0) {
							iv_background.setImageDrawable(getResources().getDrawable(R.drawable.click_60));
							tv_text.setVisibility(View.GONE);
							tv_qiandao.setText("连续签到7天");
							tv_sign_cnt.setText("已连续签到7天");
						} else {
							iv_background.setImageDrawable(getResources().getDrawable(R.drawable.click_10));
							tv_qiandao.setText("连续签到" + ret.getInt("signCnt") + "天");
							tv_sign_cnt.setText("已连续签到" + ret.getInt("signCnt") + "天");
						}
						if(ret.getInt("signCnt") == 6){
							tv_sign_point.setText("明天继续签到将获得 60 点积分");
						} else {
							tv_sign_point.setText("明天继续签到将获得 10 点积分");
						}
						ll_pop_sign.setVisibility(View.VISIBLE);
					} else {
						ToastCommom toastCommom = ToastCommom.createToastConfig();
						toastCommom.ToastShow(IntegralMallActivity.this, (ViewGroup)findViewById(R.id.toast_layout_root), "已签到");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
