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
import com.jiubang.p2p.utils.UIHelper;

/**
 * 债权转让发布成功
 * */
public class TransferSuccessActivity extends Activity implements
		OnClickListener {
	
	private TextView tv_lave_date;
	private TextView tv_tender_amount;
	private TextView tv_finish;
	
    private ImageView drop_down_menu;//右上角下拉菜单
	private TitlePopup titlePopup;

	private String transfer_price = "";
	private String lave_date = "";
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_transfer_success);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		UIHelper.setTitleView(this, "", "转让详情");

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
		
		tv_lave_date = (TextView) findViewById(R.id.tv_lave_date);
		tv_tender_amount = (TextView) findViewById(R.id.tv_tender_amount);
		tv_finish = (TextView) findViewById(R.id.tv_finish);
		tv_finish.setOnClickListener(this);
		
		drop_down_menu = (ImageView) findViewById(R.id.drop_down_menu);
		drop_down_menu.setOnClickListener(this);
		
		Bundle extras = getIntent().getExtras();
		transfer_price = extras.getString("transfer_price");
		lave_date = extras.getString("lave_date");
		
		tv_tender_amount.setText(transfer_price);
		tv_lave_date.setText(lave_date + "天");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_finish:
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
