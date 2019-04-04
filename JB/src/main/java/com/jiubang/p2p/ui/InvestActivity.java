package com.jiubang.p2p.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.adapter.CommonAdapter;
import com.jiubang.p2p.adapter.ViewHolder;
import com.jiubang.p2p.bean.Invest;
import com.jiubang.p2p.bean.InvestList;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.NetWorkUtils;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.widget.KJListView;
import com.louding.frame.widget.KJRefreshListener;

/**
 * 投资记录
 * */
@SuppressWarnings("deprecation")
public class InvestActivity extends KJActivity {

	@BindView(id = R.id.tv_title_left, click = true)
	private TextView tv_title_left;
	
	@BindView(id = R.id.ll_one, click = true)
	private LinearLayout ll_one;
	@BindView(id = R.id.one)
	private TextView one;
	
	@BindView(id = R.id.ll_two, click = true)
	private LinearLayout ll_two;
	@BindView(id = R.id.two)
	private TextView two;
	
	@BindView(id = R.id.empty)
	private TextView empty;
	
	@BindView(id = R.id.ll_three, click = true)
	private LinearLayout ll_three;
	@BindView(id = R.id.three)
	private TextView three;
	
	@BindView(id = R.id.tv_investAmount)
	private TextView tv_investAmount;
	@BindView(id = R.id.tv_unrepaidInterest)
	private TextView tv_unrepaidInterest;
	@BindView(id = R.id.tv_totalInterest)
	private TextView tv_totalInterest;
	
	@BindView(id = R.id.listview)
	private KJListView listview;
	
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;//右上角下拉菜单
	
	private TitlePopup titlePopup;
	
	private KJHttp http;
	private HttpParams params;

	private CommonAdapter<Invest> adapter;
	private List<Invest> data;
	
	private StringBuilder sb;
	private String str;

	private int page = 1;
	private String url;
	private int state;
	private boolean noMoreData;
	private boolean clickable = true;
	
	private boolean isClick;// 是否可点击

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_invest);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		ActivityUtil.getActivityUtil().addActivity(this);
		
		Bundle extras = getIntent().getExtras();
		state = extras.getInt("state");
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		NetWorkUtils.show_wifi_empty(this, empty);
		
		switch (state) {
		case 1:
			tv_title_left.setText("");
			break;
		case 2:
			tv_title_left.setText("");
			break;
		case 3:
			tv_title_left.setText("");
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void initData() {
		super.initData();
		switch (state) {
		case 1:
			url = AppConstants.INVEST_ORDER;
			
			one.setTextColor(getResources().getColor(R.color.white));
			one.setBackgroundResource(R.drawable.bg_title_left_solid);
			two.setTextColor(getResources().getColor(R.color.app_blue));
			two.setBackgroundResource(R.drawable.bg_title_center);
			three.setTextColor(getResources().getColor(R.color.app_blue));
			three.setBackgroundResource(R.drawable.bg_title_right);
			break;
		case 2:
			url = AppConstants.INVEST_PENDING;
			
			one.setTextColor(getResources().getColor(R.color.app_blue));
			one.setBackgroundResource(R.drawable.bg_title_left);
			two.setTextColor(getResources().getColor(R.color.white));
			two.setBackgroundResource(R.drawable.bg_title_center_solid);
			three.setTextColor(getResources().getColor(R.color.app_blue));
			three.setBackgroundResource(R.drawable.bg_title_right);
			break;
		case 3:
			url = AppConstants.INVEST_CLOSED;
			
			one.setTextColor(getResources().getColor(R.color.app_blue));
			one.setBackgroundResource(R.drawable.bg_title_left);
			two.setTextColor(getResources().getColor(R.color.app_blue));
			two.setBackgroundResource(R.drawable.bg_title_center);
			three.setTextColor(getResources().getColor(R.color.white));
			three.setBackgroundResource(R.drawable.bg_title_right_solid);
			break;
		}
		
		data = new ArrayList<Invest>();
		http = new KJHttp();
		params = new HttpParams();
		getGainData();
		getData(page);
	}
	
	private void getGainData() {
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.INVEST, params, new HttpCallBack(InvestActivity.this) {
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
					tv_investAmount.setText(ret.getString("investAmount"));
					tv_unrepaidInterest.setText(ret.getString("unrepaidInterest"));
					tv_totalInterest.setText(ret.getString("totalInterest"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void getData(int page) {
		params.put("page", page);
		params.put("sid", AppVariables.sid);
		http.post(url, params, httpCallback);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		adapter = new CommonAdapter<Invest>(InvestActivity.this, R.layout.item_invest) {
			@Override
			public void canvas(ViewHolder holder, Invest item) {
				holder.addClick(R.id.ll_item);
				holder.addClick(R.id.btn_transfer);
				TextView tv_money = holder.getView(R.id.tv_money);//<!-- 金额 -->
				TextView tv_repayTime = holder.getView(R.id.tv_repayTime);//<!-- 下期回款日 -->
				TextView tv_start_date = holder.getView(R.id.tv_start_date);//<!-- 起息日期 -->
				TextView tv_end_date = holder.getView(R.id.tv_end_date);//<!-- 到期日期 -->
				TextView tv_statusText = holder.getView(R.id.tv_statusText);
				TextView tv_total = holder.getView(R.id.tv_total);//<!-- 应回 -->
				TextView tv_add_rate = holder.getView(R.id.tv_add_rate);//加息
				//体验金券相关
				TextView tv_return_interest = holder.getView(R.id.tv_return_interest);//应还利息
				TextView tv_return_price = holder.getView(R.id.tv_return_price);//应还总额
				TextView capital = holder.getView(R.id.tv_return_capital);//应还本金
				ImageView iv_experience_gold_small = holder.getView(R.id.iv_experience_gold_small);//"体"字
				
				Button btn_transfer = holder.getView(R.id.btn_transfer);//转让
				TextView tv_price = holder.getView(R.id.tv_price);
				
				holder.setText(R.id.tv_name, item.getName(), false);//标名
				holder.setText(R.id.tv_rate, item.getRate() + "%", false);//利息
				
				switch (state) {
				case 1:
					isClick = true;
					holder.setText(R.id.tv_type, "回款中", false);
					String type = item.getProducts_exp_type();
					Drawable drawable = getResources().getDrawable(R.drawable.experience_gold_small);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					switch (type) {
					case "0":
						holder.setText(R.id.tv_price, item.getPrice() + "元",false);
						iv_experience_gold_small.setVisibility(View.GONE);// "体"字
						tv_money.setVisibility(View.GONE);// <!-- 金额 -->
						tv_end_date.setVisibility(View.GONE);// <!-- 到期日期 -->
						tv_total.setVisibility(View.GONE);// <!-- 应回 -->

						sb = new StringBuilder();
						sb.append("<font color=\"#999999\">应还利息：</font>");
						sb.append("<font color=\"#808080\"><big>%s元</big></font>");
						str = String.format(sb.toString(),item.getCapital_accrual());
						tv_return_interest.setText(Html.fromHtml(str));
						tv_return_interest.setCompoundDrawables(null,null,null,null); 
						tv_return_interest.setVisibility(View.VISIBLE);

						sb = new StringBuilder();
						sb.append("<font color=\"#999999\">应还总额：</font>");
						sb.append("<font color=\"#fc6262\"><big>%s元</big></font>");
						str = String.format(sb.toString(),item.getCapital_all());
						tv_return_price.setText(Html.fromHtml(str));
						tv_return_price.setCompoundDrawables(null,null,null,null); 
						tv_return_price.setVisibility(View.VISIBLE);

						sb = new StringBuilder();
						sb.append("<font color=\"#999999\">应还本金：</font>");
						sb.append("<font color=\"#808080\"><big>%s元</big></font>");
						str = String.format(sb.toString(),item.getCapital_return());
						capital.setText(Html.fromHtml(str));
						capital.setVisibility(View.VISIBLE);
						break;
					case "1":
						//纯体验金
						tv_money.setVisibility(View.GONE);
						tv_total.setVisibility(View.GONE);
						capital.setVisibility(View.GONE);
						tv_end_date.setVisibility(View.GONE);
						iv_experience_gold_small.setVisibility(View.VISIBLE);
						
						holder.setText(R.id.tv_price, item.getExperience_amount() + "元", false);
						sb = new StringBuilder();
						sb.append("<font color=\"#999999\">应还利息：</font>");
						sb.append("<font color=\"#808080\"><big>%s元</big></font>");
						str = String.format(sb.toString(), item.getExperience_accrual());
						tv_return_interest.setText(Html.fromHtml(str));
						tv_return_interest.setCompoundDrawables(null,null,drawable,null); 
						
						tv_return_interest.setVisibility(View.VISIBLE);
						
						sb = new StringBuilder();
						sb.append("<font color=\"#999999\">应还总额：</font>");
						sb.append("<font color=\"#fc6262\"><big>%s元</big></font>");
						str = String.format(sb.toString(), item.getExperience_accrual());
						tv_return_price.setText(Html.fromHtml(str));
						tv_return_price.setCompoundDrawables(null,null,drawable,null); 
						tv_return_price.setVisibility(View.VISIBLE);
						break;
					case "2":
						tv_money.setVisibility(View.GONE);
						tv_total.setVisibility(View.GONE);
						tv_end_date.setVisibility(View.GONE);
						iv_experience_gold_small.setVisibility(View.VISIBLE);
						
						sb = new StringBuilder();
						sb.append("<font color=\"#ff772d\">%s元</font>");
						sb.append("<font color=\"#ff772d\">+</font>");
						sb.append("<font color=\"#ff772d\">%s元</font>");
						str = String.format(sb.toString(), item.getPrice(),item.getExperience_amount());
						tv_price.setText(Html.fromHtml(str));
						tv_price.setVisibility(View.VISIBLE);
						
						sb = new StringBuilder();
						sb.append("<font color=\"#999999\">应还利息：</font>");
						sb.append("<font color=\"#808080\"><big>%s元</big></font>");
						sb.append("<font color=\"#808080\"><big>+</big></font>");
						sb.append("<font color=\"#808080\"><big>%s元</big></font>");
						str = String.format(sb.toString(), item.getCapital_accrual(),item.getExperience_accrual());
						tv_return_interest.setText(Html.fromHtml(str));
						tv_return_interest.setCompoundDrawables(null,null,drawable,null);
						tv_return_interest.setVisibility(View.VISIBLE);
						
						sb = new StringBuilder();
						sb.append("<font color=\"#999999\">应还总额：</font>");
						sb.append("<font color=\"#fc6262\"><big>%s元</big></font>");
						sb.append("<font color=\"#fc6262\"><big>+</big></font>");
						sb.append("<font color=\"#fc6262\"><big>%s元</big></font>");
						str = String.format(sb.toString(), item.getCapital_all(),item.getExperience_accrual());
						tv_return_price.setText(Html.fromHtml(str));
						tv_return_price.setCompoundDrawables(null,null,drawable,null); 
						tv_return_price.setVisibility(View.VISIBLE);
						
						sb = new StringBuilder();
						sb.append("<font color=\"#999999\">应还本金：</font>");
						sb.append("<font color=\"#808080\"><big>%s元</big></font>");
						str = String.format(sb.toString(), item.getCapital_return());
						capital.setText(Html.fromHtml(str));
						capital.setVisibility(View.VISIBLE);
						break;

					}
					
					sb = new StringBuilder();
					sb.append("<font color=\"#999999\">还款期数：</font>");
					sb.append("<font color=\"#808080\"><big>%s</big></font>");
					str = String.format(sb.toString(), item.getStatusText());
					tv_statusText.setText(Html.fromHtml(str));
					tv_statusText.setVisibility(View.VISIBLE);
					
					sb = new StringBuilder();
					sb.append("<font color=\"#999999\">下期回款日期：</font>");
					sb.append("<font color=\"#808080\"><big>%s</big></font>");
					str = String.format(sb.toString(), item.getRepayTime());
					tv_repayTime.setText(Html.fromHtml(str));
					tv_repayTime.setVisibility(View.VISIBLE);
					
					sb = new StringBuilder();
					sb.append("<font color=\"#999999\">起息日期：</font>");
					sb.append("<font color=\"#808080\"><big>%s</big></font>");
					str = String.format(sb.toString(), item.getInterestBeginDate());
					tv_start_date.setText(Html.fromHtml(str));
					tv_start_date.setVisibility(View.VISIBLE);
					
					btn_transfer.setVisibility(View.VISIBLE);
					
					if("1".equals(item.getIsTransfer())) {
						btn_transfer.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_text_blue));
						btn_transfer.setTextColor(getResources().getColor(R.color.app_blue));
					} else {
						btn_transfer.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_text_grey));
						btn_transfer.setTextColor(getResources().getColor(R.color.app_font_light));
					}
					
					if("".equals(item.getExtra_rate()) || item.getExtra_rate().isEmpty()) {
						tv_add_rate.setVisibility(View.GONE);
					} else {
						tv_add_rate.setText("+" + item.getExtra_rate());
						tv_add_rate.setVisibility(View.VISIBLE);
					 }
					break;
				case 2:
					isClick = false;
					String type2 = item.getProducts_exp_type();
					if("1".equals(type2)){
						//纯体验金
						iv_experience_gold_small.setVisibility(View.VISIBLE);
						holder.setText(R.id.tv_price, item.getExperience_amount() + "元", false);
					}else if("2".equals(type2)){
						//混合标
						sb = new StringBuilder();
						sb.append("<font color=\"#ff772d\">%s元</font>");
						sb.append("<font color=\"#ff772d\">+</font>");
						sb.append("<font color=\"#ff772d\">%s元</font>");
						str = String.format(sb.toString(), item.getPrice(),item.getExperience_amount());
						tv_price.setText(Html.fromHtml(str));
						iv_experience_gold_small.setVisibility(View.VISIBLE);
						tv_price.setVisibility(View.VISIBLE);
					}else if("0".equals(type2)){
					    holder.setText(R.id.tv_price, item.getPrice() + "元", false);
					    iv_experience_gold_small.setVisibility(View.GONE);// "体"字
					}
					holder.setText(R.id.tv_type, "待确认", false);
					tv_repayTime.setText("投资日期 " + item.getCreateDate());
					tv_repayTime.setVisibility(View.VISIBLE);
					
					if("".equals(item.getExtra_rate()) || item.getExtra_rate().isEmpty()) {
						tv_add_rate.setVisibility(View.GONE);
					} else {
						tv_add_rate.setText("+" + item.getExtra_rate());
						tv_add_rate.setVisibility(View.VISIBLE);
					}
					
					tv_money.setVisibility(View.GONE);
					tv_start_date.setVisibility(View.GONE);
					tv_end_date.setVisibility(View.GONE);
					tv_statusText.setVisibility(View.GONE);
					tv_total.setVisibility(View.GONE);
					btn_transfer.setVisibility(View.GONE);
					capital.setVisibility(View.GONE);
					tv_return_price.setVisibility(View.GONE);
					tv_return_interest.setVisibility(View.GONE);
					break;
				case 3:
					isClick = false;
					holder.setText(R.id.tv_type, "已结清", false);
					String type3 = item.getProducts_exp_type();
					if ("1".equals(type3)) {
						// 纯体验金
						holder.setText(R.id.tv_price,item.getExperience_amount() + "元", false);
						str = String.format(sb.toString(), item.getLastReturn());
						tv_total.setText(Html.fromHtml(str));
						tv_total.setVisibility(View.VISIBLE);
						iv_experience_gold_small.setVisibility(View.VISIBLE);
					}else if ("2".equals(type3)) {
						// 混合标
						sb = new StringBuilder();
						sb.append("<font color=\"#ff772d\">%s元</font>");
						sb.append("<font color=\"#ff772d\">+</font>");
						sb.append("<font color=\"#ff772d\">%s元</font>");
						str = String.format(sb.toString(), item.getPrice(),item.getExperience_amount());
						tv_price.setText(Html.fromHtml(str));
						tv_price.setVisibility(View.VISIBLE);
						iv_experience_gold_small.setVisibility(View.VISIBLE);
						
						sb = new StringBuilder();
						sb.append("<font color=\"#999999\">已回：</font>");
						sb.append("<font color=\"#808080\"><big>%s元</big></font>");
						str = String.format(sb.toString(), item.getLastReturn());
						tv_total.setText(Html.fromHtml(str));
						tv_total.setVisibility(View.VISIBLE);
					} else if("0".equals(type3)){
						holder.setText(R.id.tv_price, item.getPrice() + "元",false);
						iv_experience_gold_small.setVisibility(View.GONE);// "体"字
						
						sb = new StringBuilder();
						sb.append("<font color=\"#999999\">已回：</font>");
						sb.append("<font color=\"#808080\"><big>%s元</big></font>");
						str = String.format(sb.toString(), item.getLastReturn());
						tv_total.setText(Html.fromHtml(str));
						tv_total.setVisibility(View.VISIBLE);
					}
					sb = new StringBuilder();
					sb.append("<font color=\"#999999\">期数：</font>");
					sb.append("<font color=\"#808080\"><big>%s</big></font>");
					str = String.format(sb.toString(), item.getStatusText());
					tv_statusText.setText(Html.fromHtml(str));
					tv_statusText.setVisibility(View.VISIBLE);
					
					sb = new StringBuilder();
					sb.append("<font color=\"#999999\">起息日期：</font>");
					sb.append("<font color=\"#808080\"><big>%s</big></font>");
					str = String.format(sb.toString(), item.getInterestBeginDate());
					tv_start_date.setText(Html.fromHtml(str));
					tv_start_date.setVisibility(View.VISIBLE);
					
					sb = new StringBuilder();
					sb.append("<font color=\"#999999\">结清日期：</font>");
					sb.append("<font color=\"#808080\"><big>%s</big></font>");
					str = String.format(sb.toString(), item.getEndDate());
					tv_end_date.setText(Html.fromHtml(str));
					tv_end_date.setVisibility(View.VISIBLE);
					
					if("".equals(item.getExtra_rate()) || item.getExtra_rate().isEmpty()) {
						tv_add_rate.setVisibility(View.GONE);
					} else {
						tv_add_rate.setText("+" + item.getExtra_rate());
						tv_add_rate.setVisibility(View.VISIBLE);
					}
					
					tv_money.setVisibility(View.GONE);
					tv_repayTime.setVisibility(View.GONE);
					btn_transfer.setVisibility(View.GONE);
					capital.setVisibility(View.GONE);
					tv_return_price.setVisibility(View.GONE);
					tv_return_interest.setVisibility(View.GONE);
					break;
				}
			}

			@Override
			public void click(int id, List<Invest> list, int position, ViewHolder viewHolder) {
				switch (id) {
				case R.id.ll_item:
					if(isClick) {
						Intent intent = new Intent(InvestActivity.this, InvestDetailActivity.class);
						intent.putExtra("oid_tender_id", list.get(position).getOid_tender_id());
						intent.putExtra("tender_from", list.get(position).getTender_from());
						startActivity(intent);
					}
					break;
				case R.id.btn_transfer:
					if("1".equals(list.get(position).getIsTransfer())) {
						Intent intent = new Intent(InvestActivity.this, TransferRuleActivity.class);
						intent.putExtra("flag", "");
						intent.putExtra("oid_tender_id", list.get(position).getOid_tender_id());
						intent.putExtra("tender_from", list.get(position).getTender_from());
						startActivity(intent);
					}
					break;
				}
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

	private HttpCallBack httpCallback = new HttpCallBack(InvestActivity.this) {
		public void success(org.json.JSONObject ret) {
			try {
				JSONObject articles = ret.getJSONObject("orders");
				page = articles.getInt("currentPage");
				int maxPage = articles.getJSONObject("pager").getInt("maxPage");
				if (page >= maxPage) {
					listview.hideFooter();
					noMoreData = true;
				} else {
					listview.showFooter();
					noMoreData = false;
				}
				if (page < 2) {
					data = new InvestList(articles.getJSONArray("items")).getInvests();
				} else {
					data = new InvestList(data, articles.getJSONArray("items")).getInvests();
				}
				adapter.setList(data);
			} catch (Exception e) {
				if (page > 0) {
					page = page - 1;
				}
				e.printStackTrace();
				Toast.makeText(InvestActivity.this, R.string.app_data_error, Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
			listview.stopRefreshData();
			clickable = true;
		}
	};

	public void widgetClick(android.view.View v) {
		
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
			url = AppConstants.INVEST_ORDER;
			state = 1;
			getData(1);
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
			url = AppConstants.INVEST_PENDING;
			state = 2;
			getData(1);
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
			url = AppConstants.INVEST_CLOSED;
			state = 3;
			getData(1);
			break;
		case R.id.tv_title_left:
			finish();
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
	 * 处理浮点数显示的时候后面会出现.0的问题
	 * */
	public static String numFormat(double d) {
		String str = String.valueOf(d);
		String[] s = str.split("\\.");
		if (s.length > 1 && s[1].equals("0")) {
			return s[0];
		} else {
			return str;
		}
	}
}
