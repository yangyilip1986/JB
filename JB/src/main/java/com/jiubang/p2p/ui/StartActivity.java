package com.jiubang.p2p.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.WindowManager;

import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.bean.Update;
import com.jiubang.p2p.ui.GuideActivity;
import com.jiubang.p2p.ui.MainActivity;
import com.jiubang.p2p.utils.UpdateManager;
import com.jiubang.p2p.utils.UpdateManager.CheckVersionInterface;
import com.jiubang.p2p.utils.UpdateManager.OnCheckDoneListener;

public class StartActivity extends KJActivity implements
		CheckVersionInterface {

	private KJHttp http;
	private JSONObject versionInfo;
	private UpdateManager updateManager;
	private Update u;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_start);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	}

	@Override
	public void initData() {
		super.initData();
		// 初始化ImageLoader
		ImageLoaderConfiguration config = ImageLoaderConfiguration
				.createDefault(this);
		ImageLoader.getInstance().init(config);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				http = new KJHttp();
				updateManager = UpdateManager.getUpdateManager();
				HttpParams params = new HttpParams();

				params.put("sid", AppVariables.sid);
				http.post(AppConstants.UPDATE, params, new HttpCallBack(
						StartActivity.this) {
					public void onPreStart() {
					};

					public void onFinish() {

					};

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						AppVariables.needGesture = true;
						isStartGuide(false);// 是否启动引导页 true:启动 false不启动
					};

					public void onSuccess(String t) {
						try {
							versionInfo = new JSONObject(t);
							checkUpdate();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}, 2900);
	}

	private void checkUpdate() {
		updateManager.setOnCheckDoneListener(new OnCheckDoneListener() {
			@Override
			public void onCheckDone() {
				AppVariables.needGesture = true;
				isStartGuide(false);// 是否启动引导页 true:启动 false不启动
			}
		});
		updateManager.checkAppUpdate(StartActivity.this, false, this);
	}

	@Override
	public Update checkVersion() throws Exception {
		try {
			u = new Update(versionInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return u;
	}

	/**
	 * 判断是否第一次安装
	 * */
	private boolean isFirstOpen() {
		boolean boo = true;
		PackageInfo info;
		try {
			info = getPackageManager()
					.getPackageInfo("com.jiubang.p2p", 0);
			int currentVersion = info.versionCode;
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			int lastVersion = prefs.getInt("VERSION_KEY", 0);
			if (currentVersion > lastVersion) {
				// 如果当前版本大于上次版本，该版本属于第一次启动
				// 将当前版本写入preference中，则下次启动的时候，据此判断，不再为首次启动
				prefs.edit().putInt("VERSION_KEY", currentVersion).commit();
				boo = true;
				
//				AppConfig.getAppConfig(StartApplication.this).set("account_see", "see");
				
			} else {
				boo = false;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return boo;
	}

	/**
	 * 是否启动引导页 true:启动 false不启动
	 * */
	private void isStartGuide(boolean boo) {
		Intent mainIntent = null;
		if (isFirstOpen() && boo) {
			mainIntent = new Intent(StartActivity.this, GuideActivity.class);// 引导页
		} else {
			mainIntent = new Intent(StartActivity.this, MainActivity.class);
		}
		mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		StartActivity.this.startActivity(mainIntent);
		StartActivity.this.finish();
	}

}
