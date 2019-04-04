package com.jiubang.p2p.ui;

import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.adapter.CommonAdapter;
import com.jiubang.p2p.adapter.ViewHolder;
import com.jiubang.p2p.bean.Integral;
import com.jiubang.p2p.bean.IntegralList;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.NetWorkUtils;
import com.jiubang.p2p.utils.UIHelper;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.widget.KJListView;
import com.louding.frame.widget.KJRefreshListener;

/**
 * 积分明细、收入、支出、已过期
 * */
@SuppressWarnings("deprecation")
public class IntegralDetailActivity extends KJActivity {

	@BindView(id = R.id.listview)
	private KJListView listview;
	
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;
	
	@BindView(id = R.id.empty)
	private TextView empty;
	
	private TitlePopup titlePopup;

	private boolean noMoreData;

	private String type;

	private int page = 1;

	private HttpParams params;
	private KJHttp http;
	
	private List<Integral> data;
	private CommonAdapter<Integral> adapter;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_integral_detail);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		Intent intent = getIntent();
		type = intent.getStringExtra("type");

		switch (type) {
		case "1":
			UIHelper.setTitleView(this, "", "积分明细");
			break;
		case "2":
			UIHelper.setTitleView(this, "", "积分收入");
			break;
		case "3":
			UIHelper.setTitleView(this, "", "积分支出");
			break;
		case "4":
			UIHelper.setTitleView(this, "", "已过期");
			break;
		}
		ActivityUtil.getActivityUtil().addActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		NetWorkUtils.show_wifi_empty(this, empty);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void initData() {
		super.initData();
		params = new HttpParams();
		http = new KJHttp();

		getData(page);
	}

	private void getData(int page) {
		params.put("sid", AppVariables.sid);
		params.put("page", page);
		params.put("type", type);
		http.post(AppConstants.MY_INTEGRAL_DETAIL, params, httpCallback);
	}

	private HttpCallBack httpCallback = new HttpCallBack(
			IntegralDetailActivity.this) {

		@Override
		public void onSuccess(String t) {
			try {
				JSONObject ret = new JSONObject(t);
				JSONObject invitations = ret.getJSONObject("pager");
				page = invitations.getInt("page");
				int maxPage = invitations.getInt("maxPage");
				if (page >= maxPage) {
					listview.hideFooter();
					noMoreData = true;
				} else {
					listview.showFooter();
					noMoreData = false;
				}
				if (page < 2) {
					data = new IntegralList(ret.getJSONArray("integralMapList"))
							.getIntegrals();
				} else {
					data = new IntegralList(data,
							ret.getJSONArray("integralMapList")).getIntegrals();
				}
				adapter.setList(data);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(IntegralDetailActivity.this,
						R.string.app_data_error, Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
			listview.stopRefreshData();
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
		default:
			break;
		}
	}

	@Override
	public void initWidget() {
		super.initWidget();
		adapter = new CommonAdapter<Integral>(IntegralDetailActivity.this,
				R.layout.item_integral_detail) {

			@Override
			public void canvas(ViewHolder holder, Integral item) {
				if (item.getDate() != null) {
					holder.setText(R.id.tv_date, item.getDate(), false);
					
					TextView tv_integral = holder.getView(R.id.tv_integral);
					tv_integral.setText(item.getPoint());
					if(Double.parseDouble(item.getPoint()) >= 0){
						tv_integral.setTextColor(getResources().getColor(R.color.app_font_green));
					} else {
						tv_integral.setTextColor(getResources().getColor(R.color.app_font_red));
					}
					
					holder.setText(R.id.tv_type, item.getPoint_description(), false);
				} else {
					holder.setText(R.id.tv_date, item.getBatch_run_time(), false);
					holder.setText(R.id.tv_integral, item.getBefor_point(), false);
					holder.setText(R.id.tv_type, "已过期", false);
				}
			}

			@Override
			public void click(int id, List<Integral> list, int position,
					ViewHolder viewHolder) {
			}

		};
		adapter.setList(data);
		listview.setAdapter(adapter);
		listview.setOnRefreshListener(refreshListener);
		listview.setEmptyView(findViewById(R.id.empty));
	}

	private KJRefreshListener refreshListener = new KJRefreshListener() {
		@Override
		public void onRefresh() {
			getData(1);
		}

		@Override
		public void onLoadMore() {
			if (!noMoreData) {
				getData(page + 1);
			}
		}
	};

}
