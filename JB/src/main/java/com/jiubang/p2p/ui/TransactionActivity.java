package com.jiubang.p2p.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.adapter.TransactionRecordAdapter;
import com.jiubang.p2p.bean.Transaction;
import com.jiubang.p2p.bean.TransactionList;
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
 * 交易记录
 * */
public class TransactionActivity extends KJActivity{

	@BindView(id = R.id.listview)
	private KJListView listview;
	
	@BindView(id = R.id.tv_0, click = true)
	private TextView tv_0;// 全部
	@BindView(id = R.id.tv_1, click = true)
	private TextView tv_1;// 充值
	@BindView(id = R.id.tv_2, click = true)
	private TextView tv_2;// 提现
	@BindView(id = R.id.tv_3, click = true)
	private TextView tv_3;// 投资
	@BindView(id = R.id.tv_4, click = true)
	private TextView tv_4;// 回款
	@BindView(id = R.id.tv_5, click = true)
	private TextView tv_5;// 返现
	
	@BindView(id = R.id.empty)
	private TextView empty;
	
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;
	
	private TitlePopup titlePopup;
	
	private KJHttp http;
	private HttpParams params;

	private List<Transaction> data;
	
	private int page = 1;
	private boolean noMoreData;
	
	private String typeFilter = "0";
	
	private boolean refresh = true;
	
	private TransactionRecordAdapter recordAdapter;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_transaction);
            
        //透明状态栏  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  
        
        UIHelper.setTitleView(this, "", "交易记录");
        ActivityUtil.getActivityUtil().addActivity(this);
	}
	
	public void widgetClick(android.view.View v) {
		
		switch (v.getId()) {
		case R.id.tv_0:
			typeFilter = "0";
			setTextColer();
			tv_0.setTextColor(getResources().getColor(R.color.app_font_selected));
			page = 1;
			getData(page);
			break;
		case R.id.tv_1:
			typeFilter = "1";
			setTextColer();
			tv_1.setTextColor(getResources().getColor(R.color.app_font_selected));
			page = 1;
			getData(page);
			break;
		case R.id.tv_2:
			typeFilter = "2";
			setTextColer();
			tv_2.setTextColor(getResources().getColor(R.color.app_font_selected));
			page = 1;
			getData(page);
			break;
		case R.id.tv_3:
			typeFilter = "3";
			setTextColer();
			tv_3.setTextColor(getResources().getColor(R.color.app_font_selected));
			page = 1;
			getData(page);
			break;
		case R.id.tv_4:
			typeFilter = "4";
			setTextColer();
			tv_4.setTextColor(getResources().getColor(R.color.app_font_selected));
			page = 1;
			getData(page);
			break;
		case R.id.tv_5:
			typeFilter = "5";
			setTextColer();
			tv_5.setTextColor(getResources().getColor(R.color.app_font_selected));
			page = 1;
			getData(page);
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
	
	private void setTextColer() {
		tv_0.setTextColor(getResources().getColor(R.color.app_font));
		tv_1.setTextColor(getResources().getColor(R.color.app_font));
		tv_2.setTextColor(getResources().getColor(R.color.app_font));
		tv_3.setTextColor(getResources().getColor(R.color.app_font));
		tv_4.setTextColor(getResources().getColor(R.color.app_font));
		tv_5.setTextColor(getResources().getColor(R.color.app_font));
	}
	
	@Override
	public void initData() {
		data = new ArrayList<Transaction>();
		http = new KJHttp();
		params = new HttpParams();
		getData(page);
	}
	
	private void getData(int page) {
		params.put("page", page);
		params.put("sid", AppVariables.sid);
		params.put("typeFilter", typeFilter);
		params.put("findStatus", 0);
		http.post(AppConstants.TRANSACTION, params, httpCallback);
	}
	
	private HttpCallBack httpCallback = new HttpCallBack(TransactionActivity.this) {
		@Override
		public void success(org.json.JSONObject ret) {
			try {
				JSONObject articles = ret.getJSONObject("balanceLogList");
				int maxPage = articles.getJSONObject("pager").getInt("maxPage");
				page = articles.getJSONObject("pager").getInt("page");
				if (page >= maxPage) {
					listview.hideFooter();
					noMoreData = true;
				} else {
					listview.showFooter();
					noMoreData = false;
				}
				if (page == 1) {
					data = new TransactionList(articles.getJSONArray("items"))
							.getList();
				} else {
					data = new TransactionList(data,
							articles.getJSONArray("items")).getList();
				}

				List<Transaction> data2 = new ArrayList<Transaction>();
				String datetime = "";
				String createTime = "";
				SimpleDateFormat sdfold;
				SimpleDateFormat sdfnew;
				for (int i = 0; i < data.size(); i++) {
					sdfold = new SimpleDateFormat("yyyy-MM-dd");
					createTime = data.get(i).getCreateTime();
					Date date = sdfold.parse(createTime);
					sdfnew = new SimpleDateFormat("yyyy年MM月");
					if (!datetime.equals(sdfnew.format(date))) {
						datetime = sdfnew.format(date);
						Transaction transaction = new Transaction();
						transaction.setDateflag(sdfnew.format(date));
						data2.add(transaction);
						data2.add(data.get(i));
					} else {
						data2.add(data.get(i));
					}
				}

				if (refresh) {
					recordAdapter = new TransactionRecordAdapter(TransactionActivity.this, data2);
					listview.setAdapter(recordAdapter);
					listview.setOnRefreshListener(refreshListener);
					listview.setEmptyView(findViewById(R.id.empty));
					refresh = false;
				} else {
					recordAdapter.setTransactions(data2);
					recordAdapter.notifyDataSetChanged();
				}

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(TransactionActivity.this, R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
			listview.stopRefreshData();
		}
	};
	
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
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		NetWorkUtils.show_wifi_empty(this, empty);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}
