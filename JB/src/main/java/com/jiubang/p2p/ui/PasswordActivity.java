package com.jiubang.p2p.ui;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jiubang.p2p.AppConfig;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
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
 * 修改密码
 * */
public class PasswordActivity extends KJActivity {

	@BindView(id = R.id.pwd_old, click = true)
	private TextView mPwd_old;
	@BindView(id = R.id.pwd_new, click = true)
	private TextView mPwd_new;
	@BindView(id = R.id.pwd_confirm, click = true)
	private TextView mPwd_confirm;
	@BindView(id = R.id.confirm, click = true)
	private TextView mConfirm;
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;
	@BindView(id = R.id.rl,click = true)
	private LinearLayout rl;

	private String pwd_old;
	private String pwd_new;
	private String pwd_confirm;
	private TitlePopup titlePopup;

	private KJHttp kjh;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_password);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		UIHelper.setTitleView(this, "", "修改密码");
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
		case R.id.rl:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;
		case R.id.confirm:
			comfirm();
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

	private void comfirm() {
		ToastCommom toastCommom = ToastCommom.createToastConfig();
		
		pwd_old = mPwd_old.getText().toString();
		pwd_new = mPwd_new.getText().toString();
		pwd_confirm = mPwd_confirm.getText().toString();
		if (StringUtils.isEmpty(pwd_old) || StringUtils.isEmpty(pwd_new)) {
			toastCommom.ToastShow(PasswordActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请输入完整信息");
			return;
		}
		if (pwd_new.length() < 8 || pwd_new.length() > 20) {
			toastCommom.ToastShow(PasswordActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "密码长度必须大于等于8位小于等于20位");
			return;
		}
		if (!pwd_new.equals(pwd_confirm)) {
			toastCommom.ToastShow(PasswordActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "两次输入密码不一致");
			return;
		}
		kjh = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("sid", AppVariables.sid);
		params.put("originPassword", pwd_old);
		params.put("newPassword", pwd_new);
		params.put("confirmNewPassword", pwd_confirm);
		kjh.post(AppConstants.CHANGEPWD, params, new HttpCallBack(PasswordActivity.this) {
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				ToastCommom toastCommom = ToastCommom.createToastConfig();
				toastCommom.ToastShow(PasswordActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "修改密码成功\n请重新登录");
				
				AppConfig.getAppConfig(PasswordActivity.this).set("sid", "");
				AppConfig.getAppConfig(PasswordActivity.this).set("uid", "");
				AppConfig.getAppConfig(PasswordActivity.this).set("tel", "");
				AppConfig.getAppConfig(PasswordActivity.this).set("gesturetel", "");
				AppConfig.getAppConfig(PasswordActivity.this).set("gesture", "");
				AppVariables.clear();
				AppVariables.isSignin = false;

				Intent intent;
				intent = new Intent(PasswordActivity.this, SigninActivity.class);
				startActivity(intent);
				
				intent = new Intent();  
	            intent.setAction("tab");
	            intent.putExtra("tab", "index");
	            sendBroadcast(intent);
	            
				ActivityUtil.getActivityUtil().finishAllActivity();
			}
		});
	}

}
