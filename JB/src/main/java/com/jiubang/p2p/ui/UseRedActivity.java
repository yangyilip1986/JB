package com.jiubang.p2p.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.adapter.CommonAdapter;
import com.jiubang.p2p.adapter.ViewHolder;
import com.jiubang.p2p.bean.Red;
import com.jiubang.p2p.bean.RedList;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.UIHelper;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;

/**
 * 使用现金券
 * */
public class UseRedActivity extends KJActivity {

	@BindView(id = R.id.listview)
	private ListView listview;
	@BindView(id = R.id.drop_down_menu, click = true)
	private ImageView drop_down_menu;//右上角下拉菜单按钮

	private TitlePopup titlePopup;
	
	private KJHttp http;
	private HttpParams params;

	private CommonAdapter<Red> adapter;
	private List<Red> data;

	private int page = 1;
	private String status = "1";
	private String amount = "0";
	private String user_type = "";
	private int productid = 0;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_usecash);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		UIHelper.setTitleView(this, "", "使用卡券");
		
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
		amount = intent.getStringExtra("amount");
		productid = intent.getIntExtra("productid", 0);
		user_type = intent.getStringExtra("user_type");
		
		data = new ArrayList<Red>();
		getData(page);
	}

	private void getData(int page) {
		http = new KJHttp();
		params = new HttpParams();
		
		params.put("page", page);
		params.put("status", status);
		params.put("amount", amount);
		params.put("productid", productid);
		params.put("sid", AppVariables.sid);
		if("add_interest_use".equals(user_type)){
			http.post(AppConstants.NEWADDINTEREST, params, httpCallback);
		}else if("cash_use".equals(user_type)){
		    http.post(AppConstants.NEWCASH2, params, httpCallback);
		}
	}
	
	private HttpCallBack httpCallback = new HttpCallBack(UseRedActivity.this) {
		public void success(org.json.JSONObject ret) {
			try {
				data = new RedList(ret.getJSONArray("val")).getList();
				adapter.setList(data);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(UseRedActivity.this, R.string.app_data_error, Toast.LENGTH_SHORT).show();
			}
		}
		
		public void onFinish() {
			super.onFinish();
		}
	};

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
		}
	
	}

	@Override
	public void initWidget() {
		super.initWidget();
		adapter = new CommonAdapter<Red>(UseRedActivity.this, R.layout.item_use_red) {
			@Override
			public void canvas(ViewHolder holder, Red item) {
				holder.addClick(R.id.tv_text3);
				final ImageView notice = holder.getView(R.id.iv_notice);
				notice.setVisibility(View.GONE);
				ImageView i = holder.getView(R.id.cash_check);
				if (item.isChecked()) {
					i.setImageResource(R.drawable.checked);
				} else {
					i.setImageResource(R.drawable.no_checked);
				}
				
				TextView tv_cash_type = holder.getView(R.id.tv_cash_type);
				if("2".equals(item.getType_flag())){
					tv_cash_type.setText("现金券");
//					TextView tv_unit1 = holder.getView(R.id.tv_unit1);
//					tv_unit1.setText("¥");
					TextView tv_unit2 = holder.getView(R.id.tv_unit2);
					tv_unit2.setText("元");
				}else if("1".equals(item.getType_flag())){
					tv_cash_type.setText("加息券");
//					TextView tv_unit1 = holder.getView(R.id.tv_unit1);
//					tv_unit1.setText("+");
					TextView tv_unit2 = holder.getView(R.id.tv_unit2);
					tv_unit2.setText("%加息券");
				}
				
				holder.setText(R.id.tv_cash_price, item.getCash_price(), false);
				holder.setText(R.id.tv_text1, item.getCash_desc(), false);
//				if("长期有效".equals(item.getEnd_time())){
					holder.setText(R.id.tv_text2, "(有效期至：" + item.getEnd_time() + ")", false);
//				} else {
//					holder.setText(R.id.tv_text2, item.getStart_time() + "至\n" + item.getEnd_time(), false);
//				}
			}

			@Override
			public void click(int id, List<Red> list, int position, ViewHolder viewHolder) {
				Intent intent = new Intent();
				if("add_interest_use".equals(user_type)){
					intent.putExtra("rate_coupon_send_id", list.get(position).getRate_coupon_send_id());
				    intent.putExtra("type_flag", list.get(position).getType_flag());
				}
				if("cash_use".equals(user_type)) {
					intent.putExtra("cash", list.get(position).getId());
				}
				intent.putExtra("price", Double.parseDouble(list.get(position).getCash_price()));
				intent.putExtra("positionItem", position);
				setResult(1, intent);
				finish();
			}
		};
		adapter.setList(data);
		listview.setAdapter(adapter);
		listview.setEmptyView(findViewById(R.id.empty));
	}
}
