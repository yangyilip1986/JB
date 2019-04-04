package com.jiubang.p2p.ui;

import org.json.JSONException;
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
 * 换绑手机号
 * */
@SuppressLint("NewApi")
public class UpdatePhoneActivity extends Activity implements OnClickListener {

	private KJHttp kjh;
	private LinearLayout ll;

	private TextView tv_next;
	private TextView tv_code;
	private EditText et_code;
	private EditText et_tel;

	private String req;
	private String sign;
	private String uri;
	private String status;
	private String msg;
	
    private ImageView drop_down_menu;//右上角下拉菜单按钮
	
	private TitlePopup titlePopup;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_update_phone);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		UIHelper.setTitleView(this, "", "换绑手机号");
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

	public void init() {
		tv_next = (TextView) findViewById(R.id.tv_next);
		tv_code = (TextView) findViewById(R.id.tv_code);
		et_tel = (EditText) findViewById(R.id.et_tel);
		et_code = (EditText) findViewById(R.id.et_code);
		drop_down_menu = (ImageView) findViewById(R.id.drop_down_menu);
		ll = (LinearLayout) findViewById(R.id.ll);
		
		drop_down_menu.setOnClickListener(this);
		tv_next.setOnClickListener(this);
		tv_code.setOnClickListener(this);
		ll.setOnClickListener(this);

		kjh = new KJHttp();
	}

	@Override
	public void onClick(View v) {
		ToastCommom toastCommom = ToastCommom.createToastConfig();
		switch (v.getId()) {
		case R.id.ll:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;
		case R.id.tv_next:
			if("".equals(et_tel.getText().toString())) {
				toastCommom.ToastShow(UpdatePhoneActivity.this, (ViewGroup)findViewById(R.id.toast_layout_root), "请输入手机号");
			} else if("".equals(et_code.getText().toString())) {
				toastCommom.ToastShow(UpdatePhoneActivity.this, (ViewGroup)findViewById(R.id.toast_layout_root), "请输入短信验证码");
			} else {
				next();
			}
			break;
		case R.id.tv_code:
			if("".equals(et_tel.getText().toString())) {
				toastCommom.ToastShow(UpdatePhoneActivity.this, (ViewGroup)findViewById(R.id.toast_layout_root), "请输入手机号");
			} else {
				getCode();
			}
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
		params.put("sid", AppVariables.sid);
		params.put("phoneCode", et_code.getText().toString());
		params.put("newtel", et_tel.getText().toString().trim());
		kjh.post(AppConstants.UPDATE_PHONE, params, new HttpCallBack(UpdatePhoneActivity.this) {
			@Override
			public void success(JSONObject ret) {
				try {
					
					String mode = ret.getString("mode");
					
					if("gateway".equals(mode)){
						String call_back = ret.getString("call_back");
						Intent intent = new Intent(UpdatePhoneActivity.this, YibaoActivity.class);
						intent.putExtra("call_back", call_back);
						intent.putExtra("mode", mode);
						startActivity(intent);
						finish();
					} else {
						status = ret.getString("status");
						if ("0".equals(status)) {
							req = ret.getString("req");
							sign = ret.getString("sign");
							uri = ret.getString("uri");
							Intent intent = new Intent(UpdatePhoneActivity.this, YibaoActivity.class);
							intent.putExtra("req", req);
							intent.putExtra("sign", sign);
							intent.putExtra("uri", uri);
							startActivity(intent);
							finish();
						} else {
							msg = ret.getString("msg");
							ToastCommom toastCommom = ToastCommom.createToastConfig();
							toastCommom.ToastShow(UpdatePhoneActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), msg);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 获得验证码
	 * */
	private void getCode() {
		HttpParams params = new HttpParams();
		params.put("phone", et_tel.getText().toString());
		params.put("phoneUsering", "1");
		kjh.post(AppConstants.UPDATE_PHONE_SENDCODE, params, new HttpCallBack(UpdatePhoneActivity.this) {
			
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
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
			tv_code.setText("剩余"+msg.arg1 + "秒");
			if (msg.arg1 == 0) {
				buttonHandle.removeCallbacks(buttonControl);
				tv_code.setBackground(getResources().getDrawable(R.drawable.bg_blue));
				tv_code.setText("获取验证码");
				tv_code.setClickable(true);
			} else {
				tv_code.setClickable(false);
				tv_code.setBackground(getResources().getDrawable(R.drawable.bg_gray));
				buttonHandle.postDelayed(buttonControl, 1000);
			}
		};
	};
}
