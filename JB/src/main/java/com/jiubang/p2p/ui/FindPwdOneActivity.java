package com.jiubang.p2p.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.HttpHelper;
import com.jiubang.p2p.utils.ToastCommom;
import com.jiubang.p2p.utils.UIHelper;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;

/**
 * 找回密码
 * */
public class FindPwdOneActivity extends KJActivity {

	@BindView(id = R.id.tel)
	private EditText mTel;
	@BindView(id = R.id.verification)
	private EditText mVrify;
	@BindView(id = R.id.pwd)
	private EditText mPwd;
	@BindView(id = R.id.pwdconfirm)
	private EditText mPwdConfirm;
	@BindView(id = R.id.tel_verify)
	private EditText mTelcode;
	@BindView(id = R.id.verifyimage, click = true)
	private ImageView mVrifyImage;
	@BindView(id = R.id.code, click = true)
	private TextView mCode;
	@BindView(id = R.id.next, click = true)
	private TextView mNext;
	@BindView(id = R.id.drop_down_menu, click = true)
	private ImageView drop_down_menu;
	@BindView(id = R.id.rl,click = true)
	private LinearLayout rl;
	
	private TitlePopup titlePopup;

	private String tel;
	private String sid;
	private String telcode;
	private String pwd;
	private String pwdc;
	private String captcha;
	private KJHttp http;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_find_pwd);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
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
	public void initWidget() {
		super.initWidget();
		UIHelper.setTitleView(this, "", "忘记密码");
	}

	@Override
	public void initData() {
		super.initData();
		post();
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		ToastCommom toastCommom = ToastCommom.createToastConfig();
		switch (v.getId()) {
		case R.id.rl:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;
		case R.id.next:// 确定
			tel = mTel.getText().toString();
			captcha = mVrify.getText().toString();
			telcode = mTelcode.getText().toString();
			pwd = mPwd.getText().toString();
			pwdc = mPwdConfirm.getText().toString();
			if (StringUtils.isEmpty(captcha)) {
				toastCommom.ToastShow(FindPwdOneActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请先输入图片验证码");
			} else if (StringUtils.isEmpty(tel) || (tel.length() < 11)) {
				toastCommom.ToastShow(FindPwdOneActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请先输入11位电话号码");
			} else if (!pwd.equals(pwdc)) {
				toastCommom.ToastShow(FindPwdOneActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "两次输入密码不一致");
			} else {
				next();
			}
			break;
		case R.id.verifyimage:
			getCapture();
			break;
		case R.id.code:// 获取短信验证码
			tel = mTel.getText().toString();
			captcha = mVrify.getText().toString();
			if (StringUtils.isEmpty(captcha)) {
				toastCommom.ToastShow(FindPwdOneActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请先输入图片验证码");
			} else if (StringUtils.isEmpty(tel) || (tel.length() < 11)) {
				toastCommom.ToastShow(FindPwdOneActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请先输入11位电话号码");
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

	/**
	 * 确定
	 * */
	private void next() {
		http = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("sid", sid);
		params.put("account", tel);
		params.put("phoneCode", telcode);
		params.put("captcha", captcha);
		http.post(AppConstants.VERIFY_CODE, params, new HttpCallBack(
				FindPwdOneActivity.this) {

			@Override
			public void failure(JSONObject ret) {
				super.failure(ret);
				String msg = null;
				try {
					msg = ret.getString("msg");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				ToastCommom toastCommom = ToastCommom.createToastConfig();
				toastCommom.ToastShow(FindPwdOneActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), msg);
			}

			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				confirm();
			}
		});
	}

	private void confirm() {
		http = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("sid", sid);
		params.put("account", tel);
		params.put("password", pwd);
		http.post(AppConstants.GET_LOSE, params, new HttpCallBack(
				FindPwdOneActivity.this) {

			@Override
			public void failure(JSONObject ret) {
				super.failure(ret);
				String msg = null;
				try {
					msg = ret.getString("code");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				ToastCommom toastCommom = ToastCommom.createToastConfig();
				toastCommom.ToastShow(FindPwdOneActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), msg);
			}

			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				ToastCommom toastCommom = ToastCommom.createToastConfig();
				toastCommom.ToastShow(FindPwdOneActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "恭喜您!修改成功!");
				finish();
			}
		});
	}

	/**
	 * 获取短信验证码
	 * */
	private void getCode() {
		http = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("sid", sid);
		params.put("phone", tel);
		params.put("captcha", captcha);
		http.post(AppConstants.SENDCODE, params, new HttpCallBack(
				FindPwdOneActivity.this) {

			@Override
			public void failure(JSONObject ret) {
				super.failure(ret);
				String msg = null;
				try {
					msg = ret.getString("msg");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				ToastCommom toastCommom = ToastCommom.createToastConfig();
				toastCommom.ToastShow(FindPwdOneActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), msg);
			}

			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				ToastCommom toastCommom = ToastCommom.createToastConfig();
				toastCommom.ToastShow(FindPwdOneActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "发送成功");
				buttonHandle.post(buttonControl);
			}
		});
	}

	/**
	 * 验证是否已登陆
	 * */
	private void post() {
		http = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("sid", "");
		http.post(AppConstants.ISSIGNIN, params, new HttpCallBack(
				FindPwdOneActivity.this) {

			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					sid = ret.getString("sid");
					getCapture();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 获取图片验证码
	 * */
	private void getCapture() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final Bitmap b = new HttpHelper().getCapture(sid);
				runOnUiThread(new Runnable() {
					public void run() {
						mVrifyImage.setImageBitmap(b);
					}
				});
			}
		}).start();
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
			mCode.setText(msg.arg1 + "秒后重新获取");
			if (msg.arg1 == 0) {
				buttonHandle.removeCallbacks(buttonControl);
				mCode.setBackgroundResource(R.drawable.bg_blue);
				mCode.setText("点击获取");
				mCode.setClickable(true);
			} else {
				mCode.setClickable(false);
				mCode.setBackgroundResource(R.drawable.bg_gray);
				buttonHandle.postDelayed(buttonControl, 1000);
			}
		};
	};
}
