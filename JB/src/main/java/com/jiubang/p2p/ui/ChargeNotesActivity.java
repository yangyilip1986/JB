package com.jiubang.p2p.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
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
import com.jiubang.p2p.bean.Transaction;
import com.jiubang.p2p.bean.TransactionList;
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
import com.louding.frame.widget.KJListView;
import com.louding.frame.widget.KJRefreshListener;

/**
 * 充值记录
 * */
public class ChargeNotesActivity extends KJActivity {

	@BindView(id = R.id.listview)
	private KJListView listview;
	
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;
	
	private TitlePopup titlePopup;

	private KJHttp http;
	private HttpParams params;

	private CommonAdapter<Transaction> adapter;
	private List<Transaction> data;

	private int page = 1;
	private boolean noMoreData;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_charge_notes);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		UIHelper.setTitleView(this, "", "充值记录");
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
		data = new ArrayList<Transaction>();
		http = new KJHttp();
		params = new HttpParams();
		getData(page);
	}

	private void getData(int page) {
		params.put("page", page);
		params.put("sid", AppVariables.sid);
		params.put("typeFilter", 1);
		params.put("findStatus", "1");
		http.post(AppConstants.TRANSACTION, params, httpCallback);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		adapter = new CommonAdapter<Transaction>(ChargeNotesActivity.this, R.layout.item_transaction_record) {
			@Override
			public void click(int id, List<Transaction> list, int position, ViewHolder viewHolder) {
			}

			@Override
			public void canvas(ViewHolder holder, Transaction item) {
				try {
					
					TextView amount = holder.getView(R.id.amount);
					amount.setTextColor(Color.rgb(61, 145, 64));
					amount.setText(item.getOperationAmount() + "元");
					
					holder.setText(R.id.transactionId, "交易号：" + item.getTransactionId(), false);
					SimpleDateFormat sdfold = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = sdfold.parse(item.getCreateTime());
					holder.setText(R.id.createTime, sdfold.format(date), false);
					holder.setText(R.id.transactionType, item.getTransactionType(), false);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
		};
		adapter.setList(data);
		listview.setAdapter(adapter);
		listview.setOnRefreshListener(refreshListener);
		listview.setEmptyView(findViewById(R.id.empty));
	}

	
	
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

	private HttpCallBack httpCallback = new HttpCallBack(ChargeNotesActivity.this) {
		public void success(org.json.JSONObject ret) {
			try {
				JSONObject articles = ret.getJSONObject("balanceLogList");
				int maxPage = articles.getJSONObject("pager").getInt("maxPage");
				page = articles.getJSONObject("pager").getInt("page");
				if (page >= maxPage) {
					listview.hideFooter();
					noMoreData = true;
//					listview.showFooter("仅显示最近七天的充值记录");
				} else {
					listview.showFooter();
					noMoreData = false;
				}
				if (page == 1) {
					data = new TransactionList(articles.getJSONArray("items")).getList();
				} else {
					data = new TransactionList(data, articles.getJSONArray("items")).getList();
				}
				adapter.setList(data);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(ChargeNotesActivity.this, R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
			listview.stopRefreshData();
		}
	};

}
