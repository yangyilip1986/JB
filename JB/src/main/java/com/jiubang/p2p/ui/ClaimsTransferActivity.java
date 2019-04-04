package com.jiubang.p2p.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.widget.KJListView;
import com.louding.frame.widget.KJRefreshListener;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.adapter.CommonAdapter;
import com.jiubang.p2p.adapter.ViewHolder;
import com.jiubang.p2p.bean.Transfer;
import com.jiubang.p2p.bean.TransferList;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.NetWorkUtils;

/**
 * 转让项目
 * */
@SuppressWarnings("deprecation")
public class ClaimsTransferActivity extends KJActivity {

	@BindView(id = R.id.ll_one, click = true)
	private LinearLayout ll_one;
	@BindView(id = R.id.one)
	private TextView one;
	
	@BindView(id = R.id.ll_two, click = true)
	private LinearLayout ll_two;
	@BindView(id = R.id.two)
	private TextView two;
	
	@BindView(id = R.id.ll_three, click = true)
	private LinearLayout ll_three;
	@BindView(id = R.id.three)
	private TextView three;

	@BindView(id = R.id.empty01)
	private TextView empty01;
	@BindView(id = R.id.empty02)
	private TextView empty02;
	@BindView(id = R.id.empty03)
	private TextView empty03;
	
	@BindView(id = R.id.title_left, click = true)
	private TextView title_left;
	@BindView(id = R.id.title_right, click = true)
	private TextView title_right;
	@BindView(id = R.id.fl_01)
	private FrameLayout fl_01;
	@BindView(id = R.id.fl_02)
	private FrameLayout fl_02;
	@BindView(id = R.id.fl_03)
	private FrameLayout fl_03;

	private KJHttp http;
	private HttpParams params;

	private List<Transfer> transferData;

	@BindView(id = R.id.listview01)
	private KJListView listview01;
	private CommonAdapter<Transfer> adapter01;

	@BindView(id = R.id.listview02)
	private KJListView listview02;
	private CommonAdapter<Transfer> adapter02;

	@BindView(id = R.id.listview03)
	private KJListView listview03;
	private CommonAdapter<Transfer> adapter03;
	
	private int page = 1;
	private String url;
	private boolean noMoreData;
	private boolean clickable = true;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_claims_transfer);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		ActivityUtil.getActivityUtil().addActivity(this);
	}

	@Override
	public void initData() {
		super.initData();
		url = AppConstants.TRANSFER_CAN;
		http = new KJHttp();
		params = new HttpParams();
	}

	private void getData(int page) {
		params.put("sid", AppVariables.sid);
		params.put("page", page);
		http.post(url, params, httpCallback);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getData(page);

		NetWorkUtils.show_wifi_empty(this, empty01);
		NetWorkUtils.show_wifi_empty(this, empty02);
		NetWorkUtils.show_wifi_empty(this, empty03);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void initWidget() {
		super.initWidget();
		
		// 可转让
		adapter01 = new CommonAdapter<Transfer>(ClaimsTransferActivity.this, R.layout.item_transfer_can) {
			@Override
			public void canvas(ViewHolder holder, Transfer item) {
				
				holder.addClick(R.id.invest_protocol);

				if (item.getProducts_title() != null)
					holder.setText(R.id.name, item.getProducts_title(), false);
				if (item.getFinance_interest_rate() != null) {
					TextView price = holder.getView(R.id.price);
					if("".equals(item.getExtra_rate())) {
						String str = item.getFinance_interest_rate() + "%";
						SpannableStringBuilder builder = new SpannableStringBuilder(str);
						CharacterStyle cs = new AbsoluteSizeSpan(32);//字体大小
						builder.setSpan(cs, str.length() - 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						price.setText(builder);
					}else {
						String str = item.getFinance_interest_rate() + "%"+ "+" + item.getExtra_rate() + "%";
						SpannableStringBuilder builder = new SpannableStringBuilder(str);
						CharacterStyle cs1 = new AbsoluteSizeSpan(32);//字体大小
						CharacterStyle cs2 = new AbsoluteSizeSpan(32);//字体大小
						builder.setSpan(cs1, item.getFinance_interest_rate().length(), item.getFinance_interest_rate().length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						builder.setSpan(cs2, str.length() - 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						price.setText(builder);
					}
				}
				if (item.getTender_amount() != null)
					holder.setText(R.id.rate, item.getTender_amount(), false);
				if (item.getLave_date() != null)
					holder.setText(R.id.lastReturn, item.getLave_date(), false);
				if (item.getFinance_start_interest_date() != null)
					holder.setText(R.id.repayTime, "起息日：" + item.getFinance_start_interest_date(), false);
				if (item.getFinance_end_interest_date() != null)
					holder.setText(R.id.endTime, "到期日：" + item.getFinance_end_interest_date(), false);
				
			}

			@Override
			public void click(int id, List<Transfer> list, int position, ViewHolder viewHolder) {
				switch (id) {
				case R.id.invest_protocol:
					Intent intent = new Intent(ClaimsTransferActivity.this, TransferRuleActivity.class);
					intent.putExtra("flag", "");
					intent.putExtra("oid_tender_id", list.get(position).getOid_tender_id());
					intent.putExtra("tender_from", list.get(position).getTender_from());
					startActivity(intent);
					break;
				}
			}
		};
		adapter01.setList(transferData);
		listview01.setAdapter(adapter01);
		listview01.setOnRefreshListener(refreshListener);
		listview01.setEmptyView(findViewById(R.id.empty01));
		
		// 转让中
		adapter02 = new CommonAdapter<Transfer>(ClaimsTransferActivity.this, R.layout.item_transfer_ing) {
			@Override
			public void canvas(ViewHolder holder, Transfer item) {
				
				if (item.getProducts_title() != null)
					holder.setText(R.id.name, item.getProducts_title(), false);
				if (item.getTransfer_interest_rate() != null) {
					TextView price = holder.getView(R.id.price);
					String str = item.getTransfer_interest_rate() + "%";
					SpannableStringBuilder builder = new SpannableStringBuilder(str);
					CharacterStyle cs = new AbsoluteSizeSpan(32);//字体大小
					builder.setSpan(cs, str.length() - 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					price.setText(builder);
				}
				if (item.getTransfer_capital() != null)
					holder.setText(R.id.rate, item.getTransfer_capital(), false);
				if (item.getFinance_period() != null)
					holder.setText(R.id.lastReturn, item.getFinance_period(), false);
				if (item.getTransfer_time() != null)
					holder.setText(R.id.repayTime, "转让挂牌日:" + item.getTransfer_time(), false);
				if (item.getTransfer_period() != null)
					holder.setText(R.id.endTime, "转让下架日:" + item.getTransfer_period(), false);
			}

			@Override
			public void click(int id, List<Transfer> list, int position, ViewHolder viewHolder) {
			}
		};
		adapter02.setList(transferData);
		listview02.setAdapter(adapter02);
		listview02.setOnRefreshListener(refreshListener);
		listview02.setEmptyView(findViewById(R.id.empty02));
		
		// 已转让
		adapter03 = new CommonAdapter<Transfer>(ClaimsTransferActivity.this, R.layout.item_transfer_already) {
			@Override
			public void canvas(ViewHolder holder, Transfer item) {
				
				if (item.getProducts_title() != null)
					holder.setText(R.id.name, item.getProducts_title(), false);
				if (item.getTransfer_success_time() != null)
					holder.setText(R.id.repayTime, "转让挂牌日:" + item.getTransfer_success_time(), false);
				if (item.getTransfer_capital_yes() != null)
					holder.setText(R.id.tv_price, item.getTransfer_capital_yes(), false);
			}

			@Override
			public void click(int id, List<Transfer> list, int position,
					ViewHolder viewHolder) {
			}
		};
		adapter03.setList(transferData);
		listview03.setAdapter(adapter03);
		listview03.setOnRefreshListener(refreshListener);
		listview03.setEmptyView(findViewById(R.id.empty03));
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

	private HttpCallBack httpCallback = new HttpCallBack(ClaimsTransferActivity.this) {

		@Override
		public void failure(JSONObject ret) {
			super.failure(ret);
		}

		@Override
		public void success(JSONObject ret) {
			super.success(ret);
			try {
				
				JSONArray items;
				JSONObject pager;
				
				int maxPage;
				switch (url) {
				case AppConstants.TRANSFER_CAN:
					items = ret.getJSONArray("orders");
					pager = ret.getJSONObject("pager");
					page = pager.getInt("currentPage");
					maxPage = pager.getInt("maxPage");
					if (page >= maxPage) {
						listview01.hideFooter();
						noMoreData = true;
					} else {
						listview01.showFooter();
						noMoreData = false;
					}
					if (page < 2) {
						transferData = new TransferList(items).getTransferList();
					} else {
						transferData = new TransferList(transferData, items).getTransferList();
					}
					adapter01.setList(transferData);
					break;
				case AppConstants.TRANSFER_ING:
					items = ret.getJSONArray("orders");
					pager = ret.getJSONObject("pager");
					page = pager.getInt("currentPage");
					maxPage = pager.getInt("maxPage");
					if (page >= maxPage) {
						listview02.hideFooter();
						noMoreData = true;
					} else {
						listview02.showFooter();
						noMoreData = false;
					}
					if (page < 2) {
						transferData = new TransferList(items).getTransferList();
					} else {
						transferData = new TransferList(transferData, items).getTransferList();
					}
					
					adapter02.setList(transferData);
					break;
				case AppConstants.TRANSFER_ALREADY:
					items = ret.getJSONArray("orders");
					pager = ret.getJSONObject("pager");
					page = pager.getInt("currentPage");
					maxPage = pager.getInt("maxPage");
					if (page >= maxPage) {
						listview03.hideFooter();
						noMoreData = true;
					} else {
						listview03.showFooter();
						noMoreData = false;
					}
					if (page < 2) {
						transferData = new TransferList(items).getTransferList();
					} else {
						transferData = new TransferList(transferData, items).getTransferList();
					}
					adapter03.setList(transferData);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(ClaimsTransferActivity.this, R.string.app_data_error, Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
			switch (url) {
			case AppConstants.TRANSFER_CAN:
				listview01.stopRefreshData();
				break;
			case AppConstants.TRANSFER_ING:
				listview02.stopRefreshData();
				break;
			case AppConstants.TRANSFER_ALREADY:
				listview03.stopRefreshData();
				break;
			}
			
			clickable = true;
		}
	};

	public void widgetClick(android.view.View v) {
		Intent intent;

		switch (v.getId()) {
		case R.id.ll_one:
			if (!clickable) {
				break;
			}
			clickable = false;
			one.setTextColor(getResources().getColor(R.color.white));
			one.setBackgroundResource(R.drawable.bg_title_left_solid);
			two.setTextColor(getResources().getColor(R.color.app_blue));
			two.setBackgroundResource(R.drawable.bg_title_center);
			three.setTextColor(getResources().getColor(R.color.app_blue));
			three.setBackgroundResource(R.drawable.bg_title_right);
			url = AppConstants.TRANSFER_CAN;

			fl_01.setVisibility(View.VISIBLE);
			fl_02.setVisibility(View.GONE);
			fl_03.setVisibility(View.GONE);
			page = 1;
			transferData = new ArrayList<Transfer>();
			getData(page);
			
			break;
		case R.id.ll_two:
			if (!clickable) {
				break;
			}
			clickable = false;
			one.setTextColor(getResources().getColor(R.color.app_blue));
			one.setBackgroundResource(R.drawable.bg_title_left);
			two.setTextColor(getResources().getColor(R.color.white));
			two.setBackgroundResource(R.drawable.bg_title_center_solid);
			three.setTextColor(getResources().getColor(R.color.app_blue));
			three.setBackgroundResource(R.drawable.bg_title_right);
			url = AppConstants.TRANSFER_ING;

			fl_01.setVisibility(View.GONE);
			fl_02.setVisibility(View.VISIBLE);
			fl_03.setVisibility(View.GONE);
			page = 1;
			transferData = new ArrayList<Transfer>();
			getData(page);
			
			break;
		case R.id.ll_three:
			if (!clickable) {
				break;
			}
			clickable = false;
			one.setTextColor(getResources().getColor(R.color.app_blue));
			one.setBackgroundResource(R.drawable.bg_title_left);
			two.setTextColor(getResources().getColor(R.color.app_blue));
			two.setBackgroundResource(R.drawable.bg_title_center);
			three.setTextColor(getResources().getColor(R.color.white));
			three.setBackgroundResource(R.drawable.bg_title_right_solid);
			url = AppConstants.TRANSFER_ALREADY;

			fl_01.setVisibility(View.GONE);
			fl_02.setVisibility(View.GONE);
			fl_03.setVisibility(View.VISIBLE);
			page = 1;
			transferData = new ArrayList<Transfer>();
			getData(page);
			
			break;
		case R.id.title_right:
			intent = new Intent(ClaimsTransferActivity.this, TransferRuleActivity.class);
			intent.putExtra("flag", "flag");
			startActivity(intent);
			break;
		case R.id.title_left:
			ClaimsTransferActivity.this.finish();
			break;
		}
	};

}
