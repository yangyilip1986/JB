package com.jiubang.p2p.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;

/**
 * 登录
 * */
public class SigninActivity extends KJActivity {

	@BindView(id = R.id.et_tel)
	private EditText et_tel;
	@BindView(id = R.id.et_pwd)
	private EditText et_pwd;
	@BindView(id = R.id.et_verification)
	private EditText et_verification;
	@BindView(id = R.id.verifyimage, click = true)
	private ImageView mVrifyImage;
	@BindView(id = R.id.signin, click = true)
	private TextView mSignin;
	@BindView(id = R.id.go_siginup, click = true)
	private TextView mSignup;
	@BindView(id = R.id.verify1)
	private LinearLayout mVrify1;
	@BindView(id = R.id.verify2)
	private LinearLayout mVrify2;
	@BindView(id = R.id.losepwd, click = true)
	private TextView mLose;
	@BindView(id = R.id.iv_sigin_show, click = true)
	private ImageView iv_sigin_show;
	@BindView(id = R.id.iv_back, click = true)
	private ImageView iv_back;
	@BindView(id = R.id.ll, click = true)
	private LinearLayout ll;
	
	private String tel;
	private String pwd;
	private String code;
	private String sid;
	private String uid;

	private KJHttp kjh;
	
	private boolean flag = true;
	private boolean needCaptcha = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_signin);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		ActivityUtil.getActivityUtil().addActivity(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		mPwd.setOnEditorActionListener(new OnEditorActionListener() {
//			
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				switch (actionId) {
//	            case EditorInfo.IME_ACTION_GO:
//	                break;
//	            case EditorInfo.IME_ACTION_SEARCH:
//	                break;
//	            case EditorInfo.IME_ACTION_SEND:
//	                break;
//	            case EditorInfo.IME_ACTION_NEXT:
//	                break;
//				}
//				return false;
//			}
//		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		
	}

	@Override
	public void initWidget() {
		super.initWidget();
	}

	@Override
	public void initData() {
		super.initData();
		sid = "";
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.ll:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;
		case R.id.signin:
			tel = et_tel.getText().toString();
			pwd = et_pwd.getText().toString();
			code = et_verification.getText().toString();
			if (StringUtils.isEmpty(tel) || StringUtils.isEmpty(pwd)) {
				ToastCommom toastCommom = ToastCommom.createToastConfig();
				toastCommom.ToastShow(SigninActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请先填写完整信息！");
			} else {
				if(needCaptcha && StringUtils.isEmpty(code)){
					ToastCommom toastCommom = ToastCommom.createToastConfig();
					toastCommom.ToastShow(SigninActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请输入验证码！");
				} else {
					post();
				}
			}
			break;
		case R.id.verifyimage:
			getCapture();
			break;
		case R.id.losepwd:
			showActivity(SigninActivity.this, FindPwdOneActivity.class);
			break;
		case R.id.go_siginup:
			showActivity(SigninActivity.this, SiginupActivity.class);
			break;
		case R.id.iv_sigin_show:
			if(flag) {
				iv_sigin_show.setImageResource(R.drawable.show);
				et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());		
				flag = false;
			} else {
				iv_sigin_show.setImageResource(R.drawable.hide);
				et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				flag = true;
			}
			break;
		case R.id.iv_back:
			finish();
			break;
		}
	}

	private void post() {
		kjh = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("sid", sid);
		params.put("account", tel);
		params.put("passwd", pwd);
		params.put("captcha", code);
		params.put("loginVersionName", "Android" + getAppVersionName(this));
		kjh.post(AppConstants.SIGNIN, params, new HttpCallBack(
				SigninActivity.this) {
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					JSONObject o = ret.getJSONObject("body");
					sid = o.getString("sid");
					uid = o.getString("uid");
					AppConfig.getAppConfig(SigninActivity.this).set("sid", sid);
					AppConfig.getAppConfig(SigninActivity.this).set("tel", tel);
					AppConfig.getAppConfig(SigninActivity.this).set("uid", uid);
					AppVariables.uid = uid;
					AppVariables.sid = sid;
					AppVariables.tel = tel;
					AppVariables.isSignin = true;
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
                Intent intent = new Intent();
                intent.putExtra("state", "s");
                SigninActivity.this.setResult(1, intent);
                SigninActivity.this.finish();
			}

			@Override
			public void failure(JSONObject ret) {
				super.failure(ret);
				try {
					JSONObject o = ret.getJSONObject("body");
					sid = ret.getString("sid");
					if (o.getBoolean("needCaptcha")) {
						mVrify1.setVisibility(View.VISIBLE);
						mVrify2.setVisibility(View.VISIBLE);
						needCaptcha = true;
						getCapture();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

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
