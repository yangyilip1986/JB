package com.jiubang.p2p.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.TextView;

import com.jiubang.p2p.AppConfig;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.HttpHelper;
import com.jiubang.p2p.utils.ToastCommom;
import com.jiubang.p2p.utils.Umeng_channel;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;

/**
 * 注册获取短信验证码
 * */
@SuppressLint("NewApi")
public class SiginupMessageActivity extends KJActivity {
 
	@BindView(id = R.id.tel_verify)
	private EditText mTelVerify;//短信验证码的输入
	@BindView(id = R.id.invite_code)
	private EditText invite_code;//邀请人手机号的输入
	@BindView(id = R.id.tv_code, click = true)
	private TextView tv_code;//获取短信验证的按钮
	@BindView(id = R.id.next, click = true)
	private TextView next;//下一步
	@BindView(id = R.id.phone)
	private TextView mPhone;
	@BindView(id = R.id.iv_back, click = true)
	private ImageView iv_back;
	@BindView(id = R.id.v)
	private View v;
	@BindView(id = R.id.ll,click = true)
	private LinearLayout ll;
	
	private String tel;
	private String code;
	private String pwd;
	private String sid;
	private String captcha;
	private String uid;

	private boolean hascode;

	private KJHttp kjh;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_siginupmessage);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		Intent intent = getIntent();
		tel = intent.getStringExtra("tel");
		pwd = intent.getStringExtra("pwd");
		
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
		hascode = false;
		sid = "";
		captcha = "";
		kjh = new KJHttp();
		mPhone.setText(tel.substring(0, 3) + "****" + tel.substring(7, tel.length()));
		
		getCode();
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.ll:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;
		case R.id.tv_code:
			getCode();
			break;
		case R.id.next:
		    code = mTelVerify.getText().toString();
			if (!hascode) {
				ToastCommom toastCommom = ToastCommom.createToastConfig();
				toastCommom.ToastShow(SiginupMessageActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请先获取短信验证码！");
			} else {
				next.setClickable(false);
				next.setBackground(getResources().getDrawable(R.drawable.bg_gray));
				signup();
			}
			break;
		case R.id.iv_back:
			finish();
			break;
		}
	}

	private void signup() {
		HttpParams params = new HttpParams();
		params.put("sid", sid);
		params.put("phone", tel);
		params.put("password", pwd);
		params.put("phoneCode", code);
		params.put("loginType", "mobile");
		params.put("introducerPhone", invite_code.getText().toString());
		params.put("captcha", captcha);
		params.put("umeng_channel", "android");
		kjh.post(AppConstants.SIGNUP, params, new HttpCallBack(SiginupMessageActivity.this) {
			@Override
			public void failure(JSONObject ret) {
				super.failure(ret);
				
				next.setClickable(true);
				next.setBackground(getResources().getDrawable(R.drawable.bg_blue));
				
				try {
					JSONObject o = ret.getJSONObject("body");
					sid = ret.getString("sid");
					if (o.getBoolean("needCaptcha")) {
						getCapture();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				ToastCommom toastCommom = ToastCommom.createToastConfig();
				toastCommom.ToastShow(SiginupMessageActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "注册成功");
				post();
			}
		});
	}

	private void getCode() {
		HttpParams params = new HttpParams();
		params.put("phone", tel);
		params.put("sid", sid);
		params.put("captcha", captcha);
		kjh.post(AppConstants.GETCODE, params, new HttpCallBack(
				SiginupMessageActivity.this) {
			@Override
			public void failure(JSONObject ret) {
				super.failure(ret);
				try {
					JSONObject o = ret.getJSONObject("body");
					sid = ret.getString("sid");
					if (o.getBoolean("needCaptcha")) {
						getCapture();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				ToastCommom toastCommom = ToastCommom.createToastConfig();
				toastCommom.ToastShow(SiginupMessageActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "验证码发送成功");
				hascode = true;
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

	private void getCapture() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final Bitmap b = new HttpHelper().getCapture(sid);
				runOnUiThread(new Runnable() {
					public void run() {
//						mVrifyImage.setImageBitmap(b);
					}
				});
			}
		}).start();
	}

	// 注册成功后登录
	private void post() {
		kjh = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("sid", sid);
		params.put("account", tel);
		params.put("passwd", pwd);
		params.put("captcha", "");
		params.put("loginVersionName", "Android" + getAppVersionName(this));
		kjh.post(AppConstants.SIGNIN, params, new HttpCallBack(
				SiginupMessageActivity.this) {
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					JSONObject o = ret.getJSONObject("body");
					sid = o.getString("sid");
					uid = o.getString("uid");
					AppConfig.getAppConfig(SiginupMessageActivity.this).set("sid", sid);
					AppConfig.getAppConfig(SiginupMessageActivity.this).set("tel", tel);
					AppConfig.getAppConfig(SiginupMessageActivity.this).set("uid", uid);
					AppVariables.uid = uid;
					AppVariables.sid = sid;
					AppVariables.tel = tel;
					AppVariables.isSignin = true;

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				
				ActivityUtil.getActivityUtil().finishAllActivity();
				
				showActivity(SiginupMessageActivity.this, AccountActivity.class);
			}
		});
	}

	/**
	 * 返回当前程序版本名
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}

}
