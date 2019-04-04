package com.jiubang.p2p.ui;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jiubang.p2p.AppConfig;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.CustomDialogUtil;
import com.jiubang.p2p.utils.ApplicationUtil;
import com.jiubang.p2p.widget.ContentView;
import com.jiubang.p2p.widget.Drawl.GestureCallBack;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.utils.StringUtils;

/**
 * 绘制九宫格锁屏
 * */
public class GestureActivity extends Activity {

	private FrameLayout body_layout;
	private ContentView content;
	private TextView hint;
	private TextView forget;
	private TextView transfer;
	private TextView tv_name;
	private TextView tv_title_left;

	private KJHttp kjh;
	private boolean isSet;
	private int step;
	private String name = null;

	private String pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		Intent intent = getIntent();
		isSet = intent.getBooleanExtra("isSet", false);
		initview(isSet);
		body_layout = (FrameLayout) findViewById(R.id.body_layout);
		pwd = AppConfig.getAppConfig(this).get("gesture");
		if (StringUtils.isEmpty(pwd)) {
			pwd = "";
			step = 2;
			hint.setText("绘制解锁图案");
		} else {
			step = 1;
			hint.setText("请输入手势密码");
		}
		// 初始化一个显示各个点的viewGroup
		content = new ContentView(this, pwd, new GestureCallBack() {

			@Override
			public void checkedSuccess() {
				if (!isSet) {
					finish();
				}
			}

			@Override
			public void checkedFail() {
				if (!isSet) {
					hint.setText("手势密码错误");
				}
			}

			@Override
			public void gestureCode(String code) {
				if (isSet) {
					switch (step) {
					case 1:
						if (pwd.equals(code)) {
							step = 2;
							hint.setText("绘制解锁图案");
						} else {
							hint.setText("手势错误");
						}
						break;
					case 2:
						if (code.length() < 4) {
							hint.setText("至少连接四个点，请重新绘制");
						} else {
							pwd = code;
							hint.setText("再次绘制解锁图案");
							step = 3;
						}
						break;
					case 3:
						if (pwd.equals(code)) {
							hint.setText("设置成功。");
							AppVariables.needGesture = false;
							AppConfig.getAppConfig(GestureActivity.this).set("gesture", pwd);
							AppConfig.getAppConfig(GestureActivity.this).set("gesturetel", AppConfig.getAppConfig(GestureActivity.this).get("tel"));
							new Handler().postDelayed(new Runnable() {
								public void run() {
									GestureActivity.this.finish();
								}
							}, 1000);
						} else {
							hint.setText("与上一次绘制不一致，请重新绘制");
							step = 2;
						}
						break;
					}
				}
			}
		});
		// 设置手势解锁显示到哪个布局里面
		content.setParentView(body_layout);
	}
	
	private void initview(boolean isSet) {
		hint = (TextView) this.findViewById(R.id.hint);
		forget = (TextView) this.findViewById(R.id.forget);
		transfer = (TextView) this.findViewById(R.id.transfer);
		tv_name = (TextView) this.findViewById(R.id.tv_name);
		tv_title_left = (TextView) this.findViewById(R.id.tv_title_left);
		tv_title_left.setOnClickListener(listerner);
		forget.setOnClickListener(listerner);
		transfer.setOnClickListener(listerner);
		getName();
		if (isSet) {
			forget.setVisibility(View.GONE);
			transfer.setVisibility(View.GONE);
			tv_title_left.setVisibility(View.VISIBLE);
		} else {
			forget.setVisibility(View.VISIBLE);
			transfer.setVisibility(View.VISIBLE);
			tv_title_left.setVisibility(View.GONE);
		}
	}

	private OnClickListener listerner = new OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.transfer:
				final CustomDialogUtil dialog = new CustomDialogUtil(GestureActivity.this);
				dialog.setTitle("温馨提示");
				dialog.setMessage("确定更改账号？");
				dialog.positiveClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						clearinfo();
						dialog.dismiss();
						startActivity(new Intent(GestureActivity.this, SigninActivity.class));
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
			case R.id.forget:
				startActivity(new Intent(GestureActivity.this, VerifyPwd.class));
				finish();
				break;
			case R.id.tv_title_left:
				finish();
				break;
			}
		}
	};

	@Override
	public void onBackPressed() {
		if (isSet) {
			finish();
		} else {
			final CustomDialogUtil dialog = new CustomDialogUtil(GestureActivity.this);
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
	}

	private void clearinfo() {
		AppConfig.getAppConfig(GestureActivity.this).set("sid", "");
		AppConfig.getAppConfig(GestureActivity.this).set("tel", "");
		AppConfig.getAppConfig(GestureActivity.this).set("uid", "");
		AppConfig.getAppConfig(GestureActivity.this).set("gesturetel", "");
		AppConfig.getAppConfig(GestureActivity.this).set("gesture", "");
		AppVariables.clear();
	}

	private void getName() {
		kjh = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("sid", AppVariables.sid);
		kjh.post(AppConstants.BASICINFO, params, new HttpCallBack(
				GestureActivity.this) {
			@Override
			public void onSuccess(String t) {
				try {
					JSONObject ret = new JSONObject(t);
					JSONObject o = ret.getJSONObject("user");
					JSONObject a = ret.getJSONObject("account");
					if (o.getInt("nameValidated") == 1) {
						name = a.getString("realName");
					} else {
						name = o.getString("phone");
					}
					tv_name.setText("用户名：" + name);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (ApplicationUtil.isNeedGesture(this)) {
			startActivity(new Intent(this, GestureActivity.class));
		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		AppVariables.lastTime = new Date().getTime();
		
	}
	
}
