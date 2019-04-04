package com.jiubang.p2p.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.adapter.CommonAdapter;
import com.jiubang.p2p.adapter.ViewHolder;
import com.jiubang.p2p.bean.Invite;
import com.jiubang.p2p.bean.InviteList;
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
 * 邀请记录
 * */
@SuppressWarnings("deprecation")
public class InviteListActivity extends KJActivity {

	@BindView(id = R.id.listview)
	private KJListView listview;
	
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;//右上角下拉菜单
	
	private TitlePopup titlePopup;

	private KJHttp http;
	private HttpParams params;

	private CommonAdapter<Invite> adapter;
	private List<Invite> data;

	private int page = 1;
	private boolean noMoreData;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_listview);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		UIHelper.setTitleView(this, "", "邀请记录");
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
		data = new ArrayList<Invite>();
		http = new KJHttp();
		params = new HttpParams();
		getData(page);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		adapter = new CommonAdapter<Invite>(InviteListActivity.this,
				R.layout.item_invite_record) {
			@Override
			public void canvas(ViewHolder holder, Invite item) {
				holder.setText(R.id.name, item.getName(), false);
				TextView type = holder.getView(R.id.type);
				TextView desc = holder.getView(R.id.desc);
				TextView date = holder.getView(R.id.date);
				if (item.getRet_status() == 1) {
					holder.setText(R.id.status, "已返", false);
					type.setTextColor(getResources().getColor(R.color.font_green));
					desc.setTextColor(getResources().getColor(R.color.font_green));
				} else {
					holder.setText(R.id.status, "待返", false);
					type.setTextColor(getResources().getColor(R.color.red));
					desc.setTextColor(getResources().getColor(R.color.red));
				}
				date.setText(item.getCreate_time());
				switch (item.getInv_type()) {
				case 1:
					type.setText("邀请注册");
					desc.setText(item.getRed_packet_amount());
					break;
				case 2:
					type.setText("邀请投资");
					desc.setText(item.getRed_packet_amount());
					break;
				case 3:
					type.setText("邀请投资");
					desc.setText(item.getAmount());
					break;
				}
			}

			@Override
			public void click(int id, List<Invite> list, int position,
					ViewHolder viewHolder) {
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

	private void getData(int page) {
		params.put("sid", AppVariables.sid);
		params.put("page", page);
		http.post(AppConstants.INVITE, params, httpCallback);
	}

	private HttpCallBack httpCallback = new HttpCallBack(
			InviteListActivity.this) {
		@Override
		public void onSuccess(String t) {
			try {
				JSONObject ret = new JSONObject(t);
				JSONObject invitations = ret.getJSONObject("invitations");
				page = invitations.getInt("currentPage");
				int maxPage = invitations.getJSONObject("pager").getInt(
						"maxPage");
				if (page >= maxPage) {
					listview.hideFooter();
					noMoreData = true;
				} else {
					listview.showFooter();
					noMoreData = false;
				}
				if (page < 2) {
					data = new InviteList(invitations.getJSONArray("items"))
							.getList();
				} else {
					data = new InviteList(data,
							invitations.getJSONArray("items")).getList();
				}
				adapter.setList(data);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		public void onFinish() {
			super.onFinish();
			listview.stopRefreshData();
		}
	};

}
