package com.jiubang.p2p.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.R;
import com.jiubang.p2p.adapter.CommonAdapter;
import com.jiubang.p2p.adapter.ViewHolder;
import com.jiubang.p2p.bean.Announce;
import com.jiubang.p2p.bean.AnnounceList;
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
 * 平台公告 列表
 * */
@SuppressWarnings("deprecation")
public class AnnoActivity extends KJActivity {
	private final String TAG = "AnnoActivity";
	@BindView(id = R.id.listview)
	private KJListView listview;
	
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;//右上角下拉菜单
	
	@BindView(id = R.id.empty)
	private TextView empty;
	
	private TitlePopup titlePopup;

	private KJHttp http;
	private HttpParams params;

	private CommonAdapter<Announce> adapter;
	private List<Announce> data;

	private int page = 1;
	private boolean noMoreData;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_listview);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		UIHelper.setTitleView(this, "", "平台公告");
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
		data = new ArrayList<Announce>();
		http = new KJHttp();
		params = new HttpParams();
		getData(page);
	}

	private void getData(int page) {
		params.put("page", page);
		params.put("sid", "");
		http.post(AppConstants.ANNOUNCE, params, httpCallback);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		adapter = new CommonAdapter<Announce>(AnnoActivity.this,
				R.layout.item_anno) {
			@Override
			public void click(int id, List<Announce> list, int position,
					ViewHolder viewHolder) {
				switch (id) {
				case R.id.item_anno:
					Intent intent = new Intent(AnnoActivity.this, AnnoContentActivity.class);
					intent.putExtra("title", list.get(position).getTitle());
					intent.putExtra("data", list.get(position).getDateline());
					intent.putExtra("content", list.get(position).getContent());
					startActivity(intent);
					break;
				}
			}

			@Override
			public void canvas(ViewHolder holder, Announce item) {
				holder.addClick(R.id.item_anno);
				holder.setText(R.id.data, item.getDateline(), false);
				holder.setText(R.id.title, item.getTitle(), false);
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

	private HttpCallBack httpCallback = new HttpCallBack(AnnoActivity.this) {
		public void success(org.json.JSONObject ret) {
			String msg;
			try {
				msg = String.valueOf(ret.getJSONObject("articles"));
				Log.e(TAG, "success: "+msg );
				JSONObject articles = ret.getJSONObject("articles");
				Log.e(TAG, "JSONObject: "+articles );
				page = articles.getInt("currentPage");
				int maxPage = articles.getJSONObject("pager").getInt("maxPage");
				if (page >= maxPage) {
					listview.hideFooter();
					noMoreData = true;
				} else {
					listview.showFooter();
					noMoreData = false;
				}
				if (page == 1) {
					data = new AnnounceList(articles.getJSONArray("items"))
							.getAnnounces();
				} else {
					data = new AnnounceList(data,
							articles.getJSONArray("items")).getAnnounces();
				}
				adapter.setList(data);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(AnnoActivity.this, R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
			listview.stopRefreshData();
		}
	};

}
