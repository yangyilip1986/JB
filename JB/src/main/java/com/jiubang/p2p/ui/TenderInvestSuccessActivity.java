package com.jiubang.p2p.ui;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.friends.Wechat.ShareParams;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.SelectPicPopupWindow;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.ConvUtils;
import com.jiubang.p2p.utils.UIHelper;

/**
 * 正常标投资成功
 * */
public class TenderInvestSuccessActivity extends Activity implements
		OnClickListener {
	
	private LinearLayout ll_experience_amount;
	private LinearLayout ll_tiyanjin_return;
	private LinearLayout ll_amount;
	private LinearLayout ll_expected_return;
	private TextView tv_title;
	private TextView tv_amount;
	private TextView tv_experience_amount;
	private TextView tv_contract_date;
	private TextView tv_expected_return;
	private TextView tv_tiyanjin_return;
	
	private TextView tv_home;
	private TextView tv_buy;
	
	private TextView tv_share;
	
	private ImageView drop_down_menu;
	
	private TitlePopup titlePopup;
	
	private SelectPicPopupWindow menuWindow;
	
	private String shareUrl = "";
	private int flag;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_tender_invest_success);
		
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
		ll_amount = (LinearLayout) findViewById(R.id.ll_amount);
		tv_amount = (TextView) findViewById(R.id.tv_amount);
		ll_experience_amount = (LinearLayout) findViewById(R.id.ll_experience_amount);
		tv_experience_amount = (TextView) findViewById(R.id.tv_experience_amount);
		tv_contract_date = (TextView) findViewById(R.id.tv_contract_date);
		ll_expected_return = (LinearLayout) findViewById(R.id.ll_expected_return);
		tv_expected_return = (TextView) findViewById(R.id.tv_expected_return);
		ll_tiyanjin_return = (LinearLayout) findViewById(R.id.ll_tiyanjin_return);
		tv_tiyanjin_return = (TextView) findViewById(R.id.tv_tiyanjin_return);
		
		tv_share = (TextView) findViewById(R.id.tv_share);
		tv_share.setOnClickListener(this);
		
		tv_home = (TextView) findViewById(R.id.tv_home);
		tv_home.setOnClickListener(this);
		tv_buy = (TextView) findViewById(R.id.tv_buy);
		tv_buy.setOnClickListener(this);
		
		drop_down_menu = (ImageView) findViewById(R.id.drop_down_menu);
		drop_down_menu.setOnClickListener(this);
		
		Intent intent = getIntent();
		flag = intent.getIntExtra("product_type", 0);
		tv_title.setText(intent.getStringExtra("title"));
		tv_amount.setText(intent.getStringExtra("amount"));
		tv_experience_amount.setText(intent.getStringExtra("experience_amount"));
		tv_contract_date.setText(intent.getStringExtra("trade_date"));
		tv_expected_return.setText(ConvUtils.convToMoney(intent.getStringExtra("tv_tenserinvest_price")) + "元");//预期总收益
		tv_tiyanjin_return.setText(ConvUtils.convToMoney(intent.getStringExtra("tv_experience_tenserinvest")) + "元");//预期体验金总收益
		
		if(flag==0){
			//普通标
			ll_experience_amount.setVisibility(View.GONE);
			ll_tiyanjin_return.setVisibility(View.GONE);
		}else if(flag==1){
			//体验标
			ll_amount.setVisibility(View.GONE);
			ll_expected_return.setVisibility(View.GONE);
		}else if(flag==2){
			
		}
		if(intent.getBooleanExtra("validityFlg", false)){
			tv_share.setVisibility(View.VISIBLE);
			shareUrl = intent.getStringExtra("share_url");
		} else {
			tv_share.setVisibility(View.GONE);
		}
		
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
			ActivityUtil.getActivityUtil().finishAllActivity();
			intent = new Intent();  
            intent.setAction("tab");
            intent.putExtra("tab", "product");
            sendBroadcast(intent);
			break;
		case R.id.tv_share:
			menuWindow = new SelectPicPopupWindow(TenderInvestSuccessActivity.this, itemsOnClick);
			menuWindow.showAtLocation(findViewById(R.id.ll), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
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
	
	private OnClickListener  itemsOnClick = new OnClickListener(){  
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
            case R.id.ll_wechat:
            	shareWechat(shareUrl, Wechat.NAME);
                break;  
            case R.id.ll_wechat_moments:
            	shareWechat(shareUrl, WechatMoments.NAME);
                break;
            }
        }  
    };

	/**
	 * 微信好友分享
	 * */
	private void shareWechat(String shareUrl, String name) {
		ShareSDK.initSDK(this);
		ShareParams paramsToShare = new ShareParams();
		paramsToShare.setTitle("快来领九邦信投网加息券吧，我只想让你任性的赚钱！");
		paramsToShare.setText("最近我为什么这么有钱，全靠九邦信投网呢！");
		paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
		paramsToShare.setUrl(shareUrl);
		paramsToShare.setImageUrl("http://ylc-site-dev.oss-cn-hangzhou.aliyuncs.com/test-commodity-avatar/549640bbfaee3cd77479714bedbb4e8f-155d851c1e2.png");
		
		Platform platform;
		if(Wechat.NAME.equals(name)){
			platform = ShareSDK.getPlatform(Wechat.NAME);
			// 设置分享事件回调
			platform.setPlatformActionListener(listener); 
			// 执行图文分享
			platform.share(paramsToShare);
		} else if(WechatMoments.NAME.equals(name)) {
			platform = ShareSDK.getPlatform(WechatMoments.NAME);
			// 设置分享事件回调
			platform.setPlatformActionListener(listener); 
			// 执行图文分享
			platform.share(paramsToShare);
		}
		
	}
	
	PlatformActionListener listener = new PlatformActionListener(){
		public void onCancel(Platform arg0, int arg1) {
		}
		public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		}
		public void onError(Platform arg0, int arg1, Throwable arg2) {
		}
	};
}
