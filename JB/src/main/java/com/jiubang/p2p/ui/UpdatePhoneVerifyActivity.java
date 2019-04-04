package com.jiubang.p2p.ui;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.ToastCommom;
import com.jiubang.p2p.utils.UIHelper;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;

/**
 * 换绑手机号验证码
 * */
@SuppressLint("NewApi")
public class UpdatePhoneVerifyActivity extends Activity implements OnClickListener {

	private KJHttp kjh;
	
	private LinearLayout ll;

	private TextView tv_next;
	private TextView tv_code;
	private EditText et_code;
	private View v;
	
	private String tel;
	
	private TextView mPhone;
	
    private ImageView drop_down_menu;//右上角下拉菜单按钮
	
	private TitlePopup titlePopup;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_update_phone_verify);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		UIHelper.setTitleView(this, "", "修改绑定手机号");
		
		ActivityUtil.getActivityUtil().addActivity(this);
		
		Intent intent = getIntent();
		tel = intent.getStringExtra("tel");
		tel = tel.substring(0, 3) + "****" + tel.substring(7, tel.length());
		mPhone = (TextView) findViewById(R.id.phone);
        mPhone.setText(tel);
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

	public void init() {
		tv_next = (TextView) findViewById(R.id.tv_next);
		tv_code = (TextView) findViewById(R.id.tv_code);
		et_code = (EditText) findViewById(R.id.et_code);
		ll = (LinearLayout) findViewById(R.id.ll);
		v = (View) findViewById(R.id.v);
		
		drop_down_menu = (ImageView) findViewById(R.id.drop_down_menu);
		drop_down_menu.setOnClickListener(this);
		
		tv_next.setOnClickListener(this);
		tv_code.setOnClickListener(this);
		ll.setOnClickListener(this);

		kjh = new KJHttp();
		
		getCode();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ll:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;
		case R.id.tv_next:
			if("".equals(et_code.getText().toString())){
				ToastCommom toastCommom = ToastCommom.createToastConfig();
				toastCommom.ToastShow(UpdatePhoneVerifyActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请填写验证码");
			} else {
				next();
			}
			break;
		case R.id.tv_code:
			getCode();
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
	
	private void next() {
		HttpParams params = new HttpParams();
		params.put("phone", AppVariables.tel);
		params.put("phoneCode", et_code.getText().toString());
		kjh.post(AppConstants.UPDATE_PHONE_CHECKEDCODE, params, new HttpCallBack(UpdatePhoneVerifyActivity.this) {
			@Override
			public void success(JSONObject ret) {
				Intent intent = new Intent(UpdatePhoneVerifyActivity.this, UpdatePhoneActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	private void getCode() {
		HttpParams params = new HttpParams();
		params.put("phone", AppVariables.tel);
		params.put("phoneUsering", "0");
		kjh.post(AppConstants.UPDATE_PHONE_SENDCODE, params, new HttpCallBack(UpdatePhoneVerifyActivity.this) {
			
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				ToastCommom toastCommom = ToastCommom.createToastConfig();
				toastCommom.ToastShow(UpdatePhoneVerifyActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "验证码发送成功");
				buttonHandle.post(buttonControl);
			}
		});
	}
	
	Runnable buttonControl = new Runnable() {
		int sec = 60;
		@Override
		public void run() {
			Message msg = buttonHandle.obtainMessage();
			sec -= 1;
			msg.arg1 = sec;
			buttonHandle.sendMessage(msg);
			if (sec == 0) {
				sec = 60;// 读完秒 按下重新获取之后把sec重新设定为60
			}
		}
	};
	
	@SuppressLint("HandlerLeak")
	Handler buttonHandle = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tv_code.setText("剩余" + msg.arg1 + "秒");
			if (msg.arg1 == 0) {
				buttonHandle.removeCallbacks(buttonControl);
				v.setVisibility(View.GONE);
				tv_code.setText("获取验证码");
				tv_code.setClickable(true);
			} else {
				tv_code.setClickable(false);
				v.setVisibility(View.VISIBLE);
				buttonHandle.postDelayed(buttonControl, 1000);
			}
		};
	};
}
