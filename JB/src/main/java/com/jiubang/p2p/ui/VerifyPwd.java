package com.jiubang.p2p.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiubang.p2p.AppConfig;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.CustomDialogUtil;
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
 * 校验用户名密码
 * */
public class VerifyPwd extends KJActivity {

	@BindView(id = R.id.tel)
	private EditText mTel;
	@BindView(id = R.id.pwd)
	private EditText mPwd;
	@BindView(id = R.id.verification)
	private EditText mVrify;
	@BindView(id = R.id.verifyimage, click = true)
	private ImageView mVrifyImage;
	@BindView(id = R.id.signin, click = true)
	private TextView mSignin;
	@BindView(id = R.id.verify1)
	private LinearLayout mVrify1;
	
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;//右上角下拉菜单
	
	private String tel;
	private String pwd;
	private String code;
	private String sid;
	private String uid;

	private KJHttp kjh;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_verify_pwd);
		
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
		mTel.setText(AppVariables.tel);
		TextView btnLeft = null;
		TextView titleTv = null;
		ImageView titleImage = null;
		
		drop_down_menu.setVisibility(View.GONE);
		
		btnLeft = (TextView) findViewById(R.id.title_left);
		titleTv = (TextView) findViewById(R.id.title_center);
		titleImage = (ImageView) findViewById(R.id.title_image);
		btnLeft.setText(" ");
		titleImage.setVisibility(View.GONE);
		btnLeft.setVisibility(View.VISIBLE);
		titleTv.setVisibility(View.VISIBLE);
		titleTv.setText("验证登录");
		btnLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final CustomDialogUtil dialog = new CustomDialogUtil(VerifyPwd.this);
				dialog.setTitle("温馨提示");
				dialog.setMessage("是否退出登录？");
				dialog.positiveClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						clearinfo();
						dialog.dismiss();
						finish();
					}
				});
				dialog.cancelClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			}
		});
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
		case R.id.signin:
			tel = mTel.getText().toString();
			pwd = mPwd.getText().toString();
			code = mVrify.getText().toString();
			if (StringUtils.isEmpty(tel) || StringUtils.isEmpty(pwd)) {
				ToastCommom toastCommom = ToastCommom.createToastConfig();
				toastCommom.ToastShow(VerifyPwd.this, (ViewGroup) v.findViewById(R.id.toast_layout_root), "请先填写完整信息！");
			} else {
				post();
			}
			break;
		case R.id.verifyimage:
			getCapture();
			break;
		case R.id.losepwd:
			showActivity(VerifyPwd.this, FindPwdOneActivity.class);
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
		kjh.post(AppConstants.SIGNIN, params, new HttpCallBack(VerifyPwd.this) {
			@Override
			public void failure(JSONObject ret) {
				super.failure(ret);
				try {
					JSONObject o = ret.getJSONObject("body");
					sid = ret.getString("sid");
					if (o.getBoolean("needCaptcha")) {
						mVrify1.setVisibility(View.VISIBLE);
						getCapture();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					JSONObject o = ret.getJSONObject("body");
					sid = o.getString("sid");
					uid = o.getString("uid");
					AppConfig.getAppConfig(VerifyPwd.this).set("sid", sid);
					AppConfig.getAppConfig(VerifyPwd.this).set("tel", tel);
					AppConfig.getAppConfig(VerifyPwd.this).set("uid", uid);
					AppConfig.getAppConfig(VerifyPwd.this).set("gesturetel", "");
					AppConfig.getAppConfig(VerifyPwd.this).set("gesture", "");
					AppVariables.uid = uid;
					AppVariables.sid = sid;
					AppVariables.tel = tel;
					AppVariables.isSignin = true;
				} catch (JSONException e) {
					e.printStackTrace();
				}
				VerifyPwd.this.finish();
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

	@Override
	public void onBackPressed() {
		final CustomDialogUtil dialog = new CustomDialogUtil(VerifyPwd.this);
		dialog.setTitle("温馨提示");
		dialog.setMessage("是否退出登录？");
		dialog.positiveClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearinfo();
				dialog.dismiss();
				finish();
			}
		});
		dialog.cancelClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	private void clearinfo() {
		AppConfig.getAppConfig(VerifyPwd.this).set("sid", "");
		AppConfig.getAppConfig(VerifyPwd.this).set("tel", "");
		AppConfig.getAppConfig(VerifyPwd.this).set("uid", "");
		AppConfig.getAppConfig(VerifyPwd.this).set("gesturetel", "");
		AppConfig.getAppConfig(VerifyPwd.this).set("gesture", "");
		AppVariables.clear();
		AppVariables.isSignin = false;
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
