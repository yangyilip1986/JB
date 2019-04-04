package com.jiubang.p2p.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.ConvUtils;
import com.jiubang.p2p.utils.UIHelper;

/**
 * 理财计划投资成功
 * */
public class PlanABCBuySuccessActivity extends Activity implements
		OnClickListener {
	
	private TextView tv_title;
	private TextView tv_amount;
	private TextView tv_contract_date;
	private TextView tv_expected_return;
	
	private TextView tv_home;
	private TextView tv_buy;
	
	private ImageView drop_down_menu;
	
	private TitlePopup titlePopup;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_planabc_buy_success);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		UIHelper.setTitleView(this, "", "投资结果");

		ActivityUtil.getActivityUtil().addActivity(this);
		
		init();

	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void init() {
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_amount = (TextView) findViewById(R.id.tv_amount);
		tv_contract_date = (TextView) findViewById(R.id.tv_contract_date);
		tv_expected_return = (TextView) findViewById(R.id.tv_expected_return);
		
		tv_home = (TextView) findViewById(R.id.tv_home);
		tv_home.setOnClickListener(this);
		tv_buy = (TextView) findViewById(R.id.tv_buy);
		tv_buy.setOnClickListener(this);
		
		drop_down_menu = (ImageView) findViewById(R.id.drop_down_menu);
		drop_down_menu.setOnClickListener(this);
		
		Intent intent = getIntent();
		tv_title.setText(intent.getStringExtra("title"));
		tv_amount.setText(intent.getStringExtra("amount"));
		tv_contract_date.setText(intent.getStringExtra("trade_date"));
		tv_expected_return.setText(ConvUtils.convToMoney(intent.getStringExtra("tv_tenserinvest_price")) + "元");
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.tv_home:
			ActivityUtil.getActivityUtil().finishAllActivity();
			intent = new Intent();  
            intent.setAction("tab");
            intent.putExtra("tab", "index");
            sendBroadcast(intent);
			break;
		case R.id.tv_buy:
			intent = new Intent(PlanABCBuySuccessActivity.this, FinancialPlanActivity.class);
			startActivity(intent);
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

}
