package com.jiubang.p2p.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiubang.p2p.AppConfig;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.CustomDialogUtil;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.ToastCommom;
import com.jiubang.p2p.utils.UIHelper;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;

/**
 * 账户中心
 * */
public class AccountActivity extends KJActivity {

	@BindView(id = R.id.rl_username, click = true)
	private RelativeLayout rl_username;
	@BindView(id = R.id.username)
	private TextView mUsername;
	@BindView(id = R.id.rl_tel, click = true)
	private RelativeLayout rl_tel;
	@BindView(id = R.id.tel)
	private TextView mTel;
	@BindView(id = R.id.rl_idcard, click = true)
	private RelativeLayout rl_idcard;
	@BindView(id = R.id.idcard)
	private TextView mIdcard;
	@BindView(id = R.id.rl_bankcard, click = true)
	private RelativeLayout rl_bankcard;
	@BindView(id = R.id.bankcard)
	private TextView mBankcard;
	@BindView(id = R.id.handimg, click = true)
	private ImageView mHandimg;
	@BindView(id = R.id.hand)
	private TextView mHand;
	@BindView(id = R.id.rl_pwd, click = true)
	private RelativeLayout rl_pwd;
	@BindView(id = R.id.pwd)
	private TextView mPwd;
	@BindView(id = R.id.signout, click = true)
	private TextView signout;
	@BindView(id = R.id.drop_down_menu,click  = true)
	private ImageView drop_down_menu;

	@BindView(id = R.id.usernameimg)
	private ImageView mUsernameImg;
	@BindView(id = R.id.idcardimg)
	private ImageView mIdcardImg;
	@BindView(id = R.id.bankcardimg)
	private ImageView mBankcardImg;

	private String name;
	private String phone;
	private String idCard;
	private String bankCard;
	private int nameValidated;// 昵称标识 1：有效 0无效
	private int bankValidated;// 绑卡标识 2：有效 0无效
	private int accountStatus;// 身份证有效标识 2:有效 0无效
	
	private TitlePopup titlePopup;

	private String pwd;
	private boolean opened;

	private KJHttp kjh;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_account);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		UIHelper.setTitleView(this, "", "账户中心");
		
		ActivityUtil.getActivityUtil().addActivity(this);
	}

	@Override
	public void initData() {
		super.initData();
		kjh = new KJHttp();
		pwd = AppConfig.getAppConfig(this).get("gesture");
		if (StringUtils.isEmpty(pwd)) {
			mHand.setText("启用手势密码");
			mHandimg.setImageResource(R.drawable.gesture_close);
			opened = false;
		} else {
			mHand.setText("关闭手势密码");
			mHandimg.setImageResource(R.drawable.gesture_open);
			opened = true;
		}
		getInfo();
	}

	private void getInfo() {
		HttpParams params = new HttpParams();
		params.put("sid", AppVariables.sid);
		kjh.post(AppConstants.BASICINFO, params, new HttpCallBack(
				AccountActivity.this) {
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					JSONObject user = ret.getJSONObject("user");
					nameValidated = user.getInt("nameValidated");
					name = user.getString("name");
					phone = user.getString("phone");
					JSONObject account = ret.getJSONObject("account");
					idCard = account.getString("idCard");
					bankValidated = account.getInt("cardStatus");
					accountStatus = account.getInt("accountStatus");
					if (bankValidated == 2) {
						bankCard = account.getString("bankAccount");
					}
					initView();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initView() {
		if (nameValidated == 1) { // 昵称
			mUsername.setText(name);
			mUsernameImg.setVisibility(View.GONE);
		}

		if (accountStatus == 0) { // 身份证
			mIdcardImg.setVisibility(View.VISIBLE);
		} else if((accountStatus == 2)) {
			mIdcard.setText(idCard);
			mIdcardImg.setVisibility(View.GONE);
		}
		
		if (bankValidated == 1) { // 银行卡
			mBankcard.setText("审核中");
		} else if (bankValidated == 2) {
			mBankcard.setText(bankCard);
			mBankcardImg.setVisibility(View.GONE);
		}
		
		AppVariables.tel = phone;
		// 手机号脱敏
		mTel.setText(phone.substring(0, phone.length() - (phone.substring(3)).length()) + "****" + phone.substring(7));
	}

	@Override
	public void widgetClick(View v) {
		Intent intent;
		
		ToastCommom toastCommom = ToastCommom.createToastConfig();
		
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.rl_username:
			if (nameValidated != 1) {
				showActivity(AccountActivity.this, UsernameActivity.class);
			}
			break;
		case R.id.rl_tel:
			if ("".equals(AppConfig.getAppConfig(AccountActivity.this).get("tel"))) {
				toastCommom.ToastShow(AccountActivity.this, (ViewGroup)findViewById(R.id.toast_layout_root), "您还没有绑定手机号");
			} else {
				intent = new Intent(AccountActivity.this, UpdatePhoneVerifyActivity.class);
				intent.putExtra("tel",mTel.getText().toString());
				startActivity(intent);
			}
			break;
		case R.id.rl_idcard:
			if (accountStatus != 2) {
			
				if (nameValidated != 1) {
					toastCommom.ToastShow(AccountActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请先设置用户名");
				} else {
					showActivity(AccountActivity.this, IdcardActivity.class);
				}
			} else {
				toastCommom.ToastShow(AccountActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "实名已认证");
			}
			break;
		case R.id.rl_bankcard:
			if (accountStatus != 2) {
				toastCommom.ToastShow(AccountActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请先实名认证");
			} else {
				if(bankValidated == 0) {
					intent = new Intent(AccountActivity.this, BankCardBFActivity.class);
					startActivity(intent);
				} else {
					toastCommom.ToastShow(AccountActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "银行卡已绑定");
				}
			}
			break;
		case R.id.handimg:
			if (opened) {
				intent = new Intent(AccountActivity.this, GestureCloseActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(AccountActivity.this, GestureActivity.class);
				intent.putExtra("isSet", true);
				startActivity(intent);
			}
			break;
		case R.id.rl_pwd:
			showActivity(AccountActivity.this, PasswordActivity.class);
			break;
		case R.id.signout:
			
			final CustomDialogUtil dialog = new CustomDialogUtil(AccountActivity.this);
			dialog.setTitle("温馨提示");
			dialog.setMessage("确定退出登录？");
			dialog.positiveClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					ToastCommom toastCommom = ToastCommom.createToastConfig();
					toastCommom.ToastShow(AccountActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "退出成功");
					
					AppConfig.getAppConfig(AccountActivity.this).set("sid", "");
					AppConfig.getAppConfig(AccountActivity.this).set("uid", "");
					AppConfig.getAppConfig(AccountActivity.this).set("tel", "");
					AppConfig.getAppConfig(AccountActivity.this).set("gesturetel", "");
					AppConfig.getAppConfig(AccountActivity.this).set("gesture", "");
					AppVariables.clear();
					signout.setVisibility(View.GONE);
					AppVariables.isSignin = false;

					Intent intent = new Intent();  
		            intent.setAction("tab");
		            intent.putExtra("tab", "index");
		            sendBroadcast(intent);
		            
		            finish();
				}
			});
			dialog.cancelClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
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
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		pwd = AppConfig.getAppConfig(this).get("gesture");
		if (StringUtils.isEmpty(pwd)) {
			mHand.setText("启用手势密码");
			mHandimg.setImageResource(R.drawable.gesture_close);
			opened = false;
		} else {
			mHand.setText("关闭手势密码");
			mHandimg.setImageResource(R.drawable.gesture_open);
			opened = true;
		}
		getInfo();

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

}
