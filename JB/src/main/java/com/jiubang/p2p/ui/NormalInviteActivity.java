package com.jiubang.p2p.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
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
 * 普通用户邀请好友
 * */
public class NormalInviteActivity extends KJActivity {

	@BindView(id = R.id.tv_count)
	private TextView tv_count;
	@BindView(id = R.id.tv_invite, click = true)
	private TextView tv_invite;
	@BindView(id = R.id.tv_scan, click = true)
	private TextView tv_scan;
	@BindView(id = R.id.tv_refCode)
	private TextView tv_refCode;
	@BindView(id = R.id.tv_pointTotal)
	private TextView tv_pointTotal;
	@BindView(id = R.id.tv_word)
	private TextView tv_word;
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;

	private KJHttp http;
	private HttpParams params;
	private TitlePopup titlePopup;

	private String shareUrl;
	private String invitationCount;
	private String refCode;
	private int pointTotal;
	private String point_type_friend_regist = "";

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_normal_invite);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		Intent intent = getIntent();
		if ("account".equals(intent.getStringExtra("activity"))) {
			UIHelper.setTitleView(this, "", "邀请好友");
		} else {
			UIHelper.setTitleView(this, "", "邀请好友");
		}
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
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.tv_invite:
			showShare();
			break;
		case R.id.tv_scan:
			showActivity(NormalInviteActivity.this, InviteListActivity.class);
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

	@Override
	public void initData() {
		super.initData();
		http = new KJHttp();
		params = new HttpParams();
		getInfo();
		getPoint();
	}

	private void getInfo() {
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.INVITE, params, new HttpCallBack(NormalInviteActivity.this) {
			@Override
			public void onSuccess(String t) {
				try {
					JSONObject ret = new JSONObject(t);
					shareUrl = ret.getString("recommendationUrl");
					invitationCount = ret.getString("invitationCount");
					refCode = ret.getString("refCode");
					pointTotal = ret.getInt("pointTotal");
					tv_count.setText("已成功邀请" + invitationCount + "位好友");
					tv_pointTotal.setText("赚取积分" + pointTotal + "积分");
					tv_refCode.setText("您的邀请码：" + refCode);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void getPoint() {
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.POINT_ALL, params, new HttpCallBack(NormalInviteActivity.this) {
			@Override
			public void onSuccess(String t) {
				try {
					JSONObject ret = new JSONObject(t);
					point_type_friend_regist = ret.getString("POINT_TYPE_FRIEND_REGIST");
					
					tv_word.setText("1.即日起成功邀请好友注册，您将获得" + point_type_friend_regist + "个积分;");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://www.9banker.com");
		// text是分享文本，所有平台都需要这个字段
		oks.setText(shareUrl);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://www.9banker.com");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://www.9banker.com");

		// 启动分享GUI
		oks.show(this);
	}
}
