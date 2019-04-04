package com.jiubang.p2p.ui;

import java.util.List;

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
import com.jiubang.p2p.bean.MessageCenter;
import com.jiubang.p2p.bean.MessageCenterList;
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
 * 消息中心
 * */
@SuppressWarnings("deprecation")
public class MessageCenterActivity extends KJActivity {

	@BindView(id = R.id.lv_message_center)
	private KJListView lv_message_center;
	
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;
	
	@BindView(id = R.id.empty)
	private TextView empty;
	
	private TitlePopup titlePopup;
	
	private CommonAdapter<MessageCenter> adapter;

	private List<MessageCenter> messageCenters;

	private HttpParams params;
	private KJHttp http;

	private boolean noMoreData;

	private int page = 1;
	private int maxPage = 1;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_message_center);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		UIHelper.setTitleView(this, "", "全部消息");
		ActivityUtil.getActivityUtil().addActivity(this);
	}

	@Override
	public void initData() {
		super.initData();

		params = new HttpParams();
		http = new KJHttp();

	}

	@Override
	public void onResume() {
		super.onResume();
		getData(page);

		NetWorkUtils.show_wifi_empty(this, empty);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void getData(int page) {
		params.put("sid", AppVariables.sid);
		params.put("page", page);
		http.post(AppConstants.MESSAGE_CENTER, params, httpCallback);
	}

	private HttpCallBack httpCallback = new HttpCallBack(
			MessageCenterActivity.this) {

		@Override
		public void failure(JSONObject ret) {
			super.failure(ret);
		}

		@Override
		public void success(JSONObject ret) {
			super.success(ret);
			try {
				page = ret.getInt("page");
				maxPage = ret.getInt("maxPage");
				if (page >= maxPage) {
					lv_message_center.hideFooter();
					noMoreData = true;
				} else {
					lv_message_center.showFooter();
					noMoreData = false;
				}

				if (page < 2) {
					messageCenters = new MessageCenterList(
							ret.getJSONArray("messageList")).getList();
				} else {
					messageCenters = new MessageCenterList(messageCenters,
							ret.getJSONArray("messageList")).getList();
				}
				adapter.setList(messageCenters);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void onFinish() {
			super.onFinish();
			lv_message_center.stopRefreshData();
		}
	};

	@Override
	public void initWidget() {
		super.initWidget();
		adapter = new CommonAdapter<MessageCenter>(MessageCenterActivity.this,
				R.layout.item_message_center) {

			@Override
			public void canvas(ViewHolder holder, MessageCenter item) {
				holder.addClick(R.id.tv_message_content);

				TextView tv_message_title = holder.getView(R.id.tv_message_title);
				TextView tv_message_time = holder.getView(R.id.tv_message_time);
				if ("0".equals(item.getOpen_flg())) {
					tv_message_title.setTextColor(getResources().getColor(R.color.app_blue));
					tv_message_time.setTextColor(getResources().getColor(R.color.app_blue));
				} else {
					tv_message_title.setTextColor(getResources().getColor(R.color.app_font_dark));
					tv_message_time.setTextColor(getResources().getColor(R.color.app_font_light));
				}
				holder.setText(R.id.tv_message_title, item.getSubject(), false);
				holder.setText(R.id.tv_message_time, item.getIns_date(), false);
				holder.setText(R.id.tv_message_content, item.getContents(), false);

			}

			@Override
			public void click(int id, List<MessageCenter> list, int position,
					ViewHolder viewHolder) {

				Intent intent = new Intent(MessageCenterActivity.this, MessageCenterDetailActivity.class);
				intent.putExtra("message_title", list.get(position).getSubject());
				intent.putExtra("message_time", list.get(position).getIns_date());
				intent.putExtra("message_content", list.get(position).getContents());
				intent.putExtra("id", list.get(position).getId());
				intent.putExtra("msg_type", list.get(position).getMsg_type());
				startActivity(intent);
			}
		};
		lv_message_center.setAdapter(adapter);
		lv_message_center.setOnRefreshListener(refreshListener);
		lv_message_center.setEmptyView(findViewById(R.id.empty));

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

	
	
}
