package com.jiubang.p2p.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
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
import com.jiubang.p2p.utils.UIHelper;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;

/**
 * 体验金券
 * */
public class ExperienceUseRedActivity extends KJActivity {

	@BindView(id = R.id.listview)
	private ListView listview;
	@BindView(id = R.id.drop_down_menu, click = true)
	private ImageView drop_down_menu;//右上角下拉菜单按钮

	@BindView(id = R.id.tv_confirm, click = true)
	private TextView tv_confirm;//体验金的确定使用
	
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
	private PopupWindow popupWindow2;
	private View view;
	private ArrayList<Integer> list_id ;//存放id的list
	private ArrayList<Integer> list_price ;//存放金额的list
	private ArrayList<Integer> list_id2 = new ArrayList<Integer>();
	
	private Double experience_limit_price;//体验金个人投资上限
	private Double expAmountSurplus;//体验金可投金额
	private int sum;

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
		experience_limit_price = intent.getDoubleExtra("experience_limit_price", 0);
		expAmountSurplus = intent.getDoubleExtra("expAmountSurplus", 0);
		list_id2 = intent.getIntegerArrayListExtra("list_id");
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
		if("experience_cash_use".equals(user_type))
			http.post(AppConstants.ENABLE_EXPERIENCE, params, httpCallback);
	}
	
	private HttpCallBack httpCallback = new HttpCallBack(ExperienceUseRedActivity.this) {
		public void success(org.json.JSONObject ret) {
			try {
				data = new RedList(ret.getJSONArray("val")).getList();
				adapter.setList(data);
				if (data.size() != 0)
					tv_confirm.setVisibility(View.VISIBLE);
				else
					tv_confirm.setVisibility(View.GONE);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(ExperienceUseRedActivity.this, R.string.app_data_error, Toast.LENGTH_SHORT).show();
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
		case R.id.tv_confirm:
			list_id.clear();
			list_price.clear();
			for(Red red : data){
				if(red.isChecked()){
					list_id.add(red.getId());
					list_price.add(Integer.parseInt(red.getCash_price()));
				}
			}
			if(list_id!=null && list_price!=null){
				Intent intent = new Intent(ExperienceUseRedActivity.this, TransferRuleActivity.class);
				intent.putIntegerArrayListExtra("list_id", (ArrayList<Integer>) list_id);
				intent.putIntegerArrayListExtra("list_price", (ArrayList<Integer>) list_price);
				setResult(3, intent);
				finish();
			}
			break;
		}
	
	}

	@Override
	public void initWidget() {
		list_id = new ArrayList<Integer>();
		list_price = new ArrayList<Integer>();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.task_detail_popupwindow2, null);
		super.initWidget(); 
		adapter = new CommonAdapter<Red>(ExperienceUseRedActivity.this, R.layout.item_use_red) {
			@Override
			public void canvas(ViewHolder holder, Red item) {
				if(list_id2!=null && !list_id2.isEmpty()){
				for(int j : list_id2){
					if(j==item.getId()){
						item.setChecked(true);
						list_price.add(Integer.parseInt(item.getCash_price()));
						sum += Integer.parseInt(item.getCash_price());
					}
				  }
				}
				ImageView i = holder.getView(R.id.cash_check);
				final ImageView notice = holder.getView(R.id.iv_notice);
				i.setVisibility(View.VISIBLE);
				notice.setVisibility(View.GONE);
				holder.addClick(R.id.item);
				if (item.isChecked()) {
					i.setImageResource(R.drawable.checked);
				} else {
					i.setImageResource(R.drawable.no_checked);
				}
				TextView tv_cash_type = holder.getView(R.id.tv_cash_type);
				if("3".equals(item.getType_flag())){
					tv_cash_type.setText("体验金券");
					TextView tv_unit2 = holder.getView(R.id.tv_unit2);
					tv_unit2.setText("元");
				}
				holder.setText(R.id.tv_cash_price, item.getCash_price(), false);
				holder.setText(R.id.tv_text1, item.getCash_desc(), false);
//				if("长期有效".equals(item.getEnd_time())){
				holder.setText(R.id.tv_text2,  "有效期至\n" + item.getEnd_time(), false);
//				} else {
//					holder.setText(R.id.tv_text2, item.getStart_time() + "至\n" + item.getEnd_time(), false);
//				}
			
			   notice.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (popupWindow2 == null) {
						popupWindow2 = new PopupWindow(view);
					}
					iniPopupWindow(popupWindow2, view);

					if (popupWindow2.isShowing()) {
						popupWindow2.dismiss();// 关闭
					} else {
						popupWindow2.showAtLocation(notice, Gravity.CENTER, 0, 0);
					}
				}
			  });
			}

			@Override
			public void click(int id, List<Red> list, int position, ViewHolder viewHolder) {
				if(list_id2!=null && !list_id2.isEmpty())
				   list_id2.clear();
				Red red = list.get(position);
				if(!red.isChecked()){
					red.setChecked(true);
					list_price.add(Integer.parseInt(red.getCash_price()));
					sum += Integer.parseInt(red.getCash_price());
					list.set(position, red);
				}else{
					red.setChecked(false);
					list_price.remove(list.get(position));
					sum -= Integer.parseInt(red.getCash_price());
					list.set(position, red);
				}
				
				if(sum > expAmountSurplus){
					Toast.makeText(ExperienceUseRedActivity.this, "可投金额已经少于您的投资金额", Toast.LENGTH_SHORT).show();
					red.setChecked(false);
					list_price.remove(list.get(position));
					list.set(position, red);
					sum -= Integer.parseInt(red.getCash_price());
				} else if(sum > experience_limit_price){
					Toast.makeText(ExperienceUseRedActivity.this, "抱歉，您的体验金使用金额已经达到设定的最大使用上限", Toast.LENGTH_SHORT).show();
					red.setChecked(false);
					list_price.remove(list.get(position));
					list.set(position, red);
					sum -= Integer.parseInt(red.getCash_price());
				}
				
				adapter.setList(data);
				
			}
		};
		adapter.setList(data);
		listview.setAdapter(adapter);
		listview.setEmptyView(findViewById(R.id.empty));
	}
	
	private void iniPopupWindow(PopupWindow popupWindow, View v) {

		LinearLayout ll_notice = (LinearLayout) v.findViewById(R.id.ll_notice);
		TextView tv_tiyanjin_title = (TextView) ll_notice.findViewById(R.id.tv_tiyanjin_title);
		TextView tv_popup = (TextView) ll_notice.findViewById(R.id.tv_popup);
		tv_tiyanjin_title.setVisibility(View.GONE);
		tv_popup.setText(R.string.tiyanjinchoice);
		ll_notice.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

		// 控制popupwindow的宽度和高度自适应
		popupWindow.setWidth(ll_notice.getMeasuredWidth());
		popupWindow.setHeight(ll_notice.getMeasuredHeight());

		// 控制popupwindow点击屏幕其他地方消失
		popupWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
		popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
	}
}
