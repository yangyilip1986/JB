package com.jiubang.p2p.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
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
 * 现金券管理
 * */
@SuppressWarnings("deprecation")
public class RedActivity extends KJActivity {

	@BindView(id = R.id.listview)
	private KJListView listview;
	
	@BindView(id = R.id.ll_one, click = true)
	private LinearLayout ll_one;
	@BindView(id = R.id.ll_two, click = true)
	private LinearLayout ll_two;
	@BindView(id = R.id.ll_three, click = true)
	private LinearLayout ll_three;
	
	@BindView(id = R.id.one)
	private TextView one;
	@BindView(id = R.id.two)
	private TextView two;
	@BindView(id = R.id.three)
	private TextView three;
	
	@BindView(id = R.id.empty)
	private TextView empty;
	
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;//右上角下拉菜单
	
	private TitlePopup titlePopup;
	
	private KJHttp http;
	private HttpParams params;

	private CommonAdapter<Red> adapter;
	private List<Red> data;

	private int page = 1;
	private String status = "1";
	private boolean noMoreData;
	
	private PopupWindow popupWindow;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_red);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		UIHelper.setTitleView(this, "", "我的卡券");
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
		data = new ArrayList<Red>();
		http = new KJHttp();
		params = new HttpParams();
		getData(page);
	}

	private void getData(int page) {
		params.put("page", page);
		params.put("status", status);
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.RED, params, httpCallback);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.task_detail_popupwindow_red, null);
		
		adapter = new CommonAdapter<Red>(RedActivity.this, R.layout.item_red) {
			@Override
			public void canvas(ViewHolder holder, Red item) {
				holder.addClick(R.id.tv_text3);
				holder.addClick(R.id.iv_prompt);
				if ("1".equals(status)) {
					
					holder.setText(R.id.tv_text, "有效时间：", false);
					ImageView iv_bgimg = holder.getView(R.id.iv_bgimg);
					iv_bgimg.setImageResource(R.drawable.red_bg);
					if("2".equals(item.getType_flag())) {// 现金券
						TextView tv_unit2 = holder.getView(R.id.tv_unit2);
						tv_unit2.setText("元");
						ImageView iv_prompt = holder.getView(R.id.iv_prompt);
						iv_prompt.setVisibility(View.GONE);
					} else if("3".equals(item.getType_flag())) {// 体验金
						TextView tv_unit2 = holder.getView(R.id.tv_unit2);
						tv_unit2.setText("元体验金");
						ImageView iv_prompt = holder.getView(R.id.iv_prompt);
						iv_prompt.setVisibility(View.VISIBLE);
					} else {// 加息券
						TextView tv_unit2 = holder.getView(R.id.tv_unit2);
						tv_unit2.setText("%加息券");
						ImageView iv_prompt = holder.getView(R.id.iv_prompt);
						iv_prompt.setVisibility(View.GONE);
					}
					holder.setText(R.id.tv_cash_price, item.getCash_price(), false);
					holder.setText(R.id.tv_text1, item.getCash_desc(), false);
					if("长期有效".equals(item.getEnd_time())) {
						holder.setText(R.id.tv_text2, item.getEnd_time(), false);
					} else {
						holder.setText(R.id.tv_text2, item.getStart_time() + "至\n" + item.getEnd_time(), false);
					}
					TextView tv_text3 = holder.getView(R.id.tv_text3);
					tv_text3.setText("立即使用 >");
					tv_text3.setTextColor(Color.rgb(252, 98, 98));
				} else if ("2".equals(status)) {
					holder.setText(R.id.tv_text, "使用时间：", false);
					ImageView iv_bgimg = holder.getView(R.id.iv_bgimg);
					iv_bgimg.setImageResource(R.drawable.red_bg_grey);
					if("2".equals(item.getType_flag())){
						TextView tv_unit2 = holder.getView(R.id.tv_unit2);
						tv_unit2.setText("元");
						ImageView iv_prompt = holder.getView(R.id.iv_prompt);
						iv_prompt.setVisibility(View.GONE);
					} else if("3".equals(item.getType_flag())){
						TextView tv_unit2 = holder.getView(R.id.tv_unit2);
						tv_unit2.setText("元体验金");
						ImageView iv_prompt = holder.getView(R.id.iv_prompt);
						iv_prompt.setVisibility(View.VISIBLE);
					} else {
						ImageView iv_prompt = holder.getView(R.id.iv_prompt);
						iv_prompt.setVisibility(View.GONE);
						TextView tv_unit2 = holder.getView(R.id.tv_unit2);
						tv_unit2.setText("%加息券");
					}
					holder.setText(R.id.tv_cash_price, item.getCash_price(), false);
					holder.setText(R.id.tv_text1, item.getCash_desc(), false);
					holder.setText(R.id.tv_text2, item.getUsed_time(), false);
//					if("长期有效".equals(item.getEnd_time())) {
//						holder.setText(R.id.tv_text2, item.getEnd_time(), false);
//					} else {
//						holder.setText(R.id.tv_text2, item.getStart_time() + "至\n" + item.getEnd_time(), false);
//					}
					TextView tv_text3 = holder.getView(R.id.tv_text3);
					tv_text3.setText("已使用");
					tv_text3.setTextColor(Color.rgb(136, 136, 136));
				} else {
					holder.setText(R.id.tv_text, "有效时间：", false);
					ImageView iv_bgimg = holder.getView(R.id.iv_bgimg);
					iv_bgimg.setImageResource(R.drawable.red_bg_grey);
					if("2".equals(item.getType_flag())){
						TextView tv_unit2 = holder.getView(R.id.tv_unit2);
						tv_unit2.setText("元");
						ImageView iv_prompt = holder.getView(R.id.iv_prompt);
						iv_prompt.setVisibility(View.GONE);
					} else if("3".equals(item.getType_flag())){
						TextView tv_unit2 = holder.getView(R.id.tv_unit2);
						tv_unit2.setText("元体验金");
						ImageView iv_prompt = holder.getView(R.id.iv_prompt);
						iv_prompt.setVisibility(View.VISIBLE);
					} else {
						TextView tv_unit2 = holder.getView(R.id.tv_unit2);
						tv_unit2.setText("%加息券");
						ImageView iv_prompt = holder.getView(R.id.iv_prompt);
						iv_prompt.setVisibility(View.GONE);
					}
					holder.setText(R.id.tv_cash_price, item.getCash_price(), false);
					holder.setText(R.id.tv_text1, item.getCash_desc(), false);
					holder.setText(R.id.tv_text2, item.getStart_time() + "至\n" + item.getEnd_time(), false);
					TextView tv_text3 = holder.getView(R.id.tv_text3);
					tv_text3.setText("已过期");
					tv_text3.setTextColor(Color.rgb(136, 136, 136));
				}
			}

			@Override
			public void click(int id, List<Red> list, int position, ViewHolder viewHolder) {
				switch (id) {
				case R.id.tv_text3:
					if ("1".equals(status)) {
						// 发送广播 跳转更多
						Intent intent = new Intent();
			            intent.setAction("tab");
			            intent.putExtra("tab", "product");
			            RedActivity.this.sendBroadcast(intent);
		            	ActivityUtil.getActivityUtil().finishAllActivity();
					}
					break;
				case R.id.iv_prompt:
					
					if (popupWindow == null) {
						popupWindow = new PopupWindow(view);
					}

					if (popupWindow.isShowing()) {
						popupWindow.dismiss();// 关闭
					} else {
						iniPopupWindow(popupWindow, view);
						popupWindow.showAtLocation(findViewById(R.id.listview), Gravity.CENTER, 0, 0);
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
	
	private void iniPopupWindow(final PopupWindow popupWindow, View v) {

		LinearLayout ll_notice = (LinearLayout) v.findViewById(R.id.ll_notice);
		ll_notice.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

		// 控制popupwindow的宽度和高度自适应
		popupWindow.setWidth(ll_notice.getMeasuredWidth());
		popupWindow.setHeight(ll_notice.getMeasuredHeight());

		// 控制popupwindow点击屏幕其他地方消失
		popupWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
		popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
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

	private HttpCallBack httpCallback = new HttpCallBack(RedActivity.this) {
		public void success(org.json.JSONObject ret) {
			try {
				JSONObject articles = ret.getJSONObject("cash");
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
					data = new RedList(articles.getJSONArray("items"))
							.getList();
				} else {
					data = new RedList(data, articles.getJSONArray("items"))
							.getList();
				}
				adapter.setList(data);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(RedActivity.this, R.string.app_data_error, Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
			listview.stopRefreshData();
		}
	};

	public void widgetClick(android.view.View v) {
		switch (v.getId()) {
		case R.id.ll_one:
			
			one.setTextColor(getResources().getColor(R.color.white));
			one.setBackgroundResource(R.drawable.bg_title_left_solid);
			two.setTextColor(getResources().getColor(R.color.app_blue));
			two.setBackgroundResource(R.drawable.bg_title_center);
			three.setTextColor(getResources().getColor(R.color.app_blue));
			three.setBackgroundResource(R.drawable.bg_title_right);
			
			status = "1";
			getData(1);
			break;
		case R.id.ll_two:
			
			one.setTextColor(getResources().getColor(R.color.app_blue));
			one.setBackgroundResource(R.drawable.bg_title_left);
			two.setTextColor(getResources().getColor(R.color.white));
			two.setBackgroundResource(R.drawable.bg_title_center_solid);
			three.setTextColor(getResources().getColor(R.color.app_blue));
			three.setBackgroundResource(R.drawable.bg_title_right);
			
			status = "2";
			getData(1);
			break;
		case R.id.ll_three:
			
			one.setTextColor(getResources().getColor(R.color.app_blue));
			one.setBackgroundResource(R.drawable.bg_title_left);
			two.setTextColor(getResources().getColor(R.color.app_blue));
			two.setBackgroundResource(R.drawable.bg_title_center);
			three.setTextColor(getResources().getColor(R.color.white));
			three.setBackgroundResource(R.drawable.bg_title_right_solid);
			
			status = "99";
			getData(1);
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

}
