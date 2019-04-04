package com.jiubang.p2p.ui;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabWidget;
import android.widget.TextView;

import com.jiubang.p2p.AppConfig;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.receiver.AppReceiver;
import com.jiubang.p2p.utils.ApplicationUtil;
import com.jiubang.p2p.utils.ToastCommom;
import com.jiubang.p2p.view.CustomViewPager;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.utils.StringUtils;

public class MainActivity extends FragmentActivity {

	private Fragment IndexFrag;
	private Fragment ProductFrag;
	private Fragment AccountFrag;
	private Fragment MoreFrag;
	
	private TextView IndexTab;
	private TextView ProductTab;
	private TextView AccountTab;
	private TextView MoreTab;
	
	public static CustomViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	private TabWidget mTabWidget;
	private LinearLayout headLinear;
	private LinearLayout center;
	private TextView tel_center;

	private KJHttp kjh;
	private AppReceiver appReceiver;
	
	private MyBroadcastReceiver broadcastReceiver;
	
	
	// 再按一次退出程序
	private long firstTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		int tab = intent.getIntExtra("tab", 0);
		AppVariables.sid = AppConfig.getAppConfig(this).get("sid") == null ? "" : AppConfig.getAppConfig(this).get("sid");
		if (!StringUtils.isEmpty(AppVariables.sid)) {
			isSignin();
		} else {
			AppVariables.isSignin = false;
		}
		headLinear = (LinearLayout) findViewById(R.id.headLinear);
		headLinear.setVisibility(View.GONE);// 初始化先隐藏 切换其他TAB时显示
		center = (LinearLayout) findViewById(R.id.center);
		tel_center = (TextView) findViewById(R.id.tel_center);
		mTabWidget = (TabWidget) findViewById(R.id.tabwidget);
		
		// 首页
		IndexTab = getTvTab(R.string.index, R.drawable.tab_index_selecter);
		IndexTab.setTextSize(11);
		mTabWidget.addView(IndexTab);
		IndexTab.setOnClickListener(mTabClickListener);// Listener必须在mTabWidget.addView()之后再加入，用于覆盖默认的Listener，mTabWidget.addView()中默认的Listener没有NullPointer检测。
		
		// 投资
		ProductTab = getTvTab(R.string.product, R.drawable.tab_product_selecter);
		ProductTab.setTextSize(11);
		mTabWidget.addView(ProductTab);
		ProductTab.setOnClickListener(mTabClickListener);

		// 更多
		MoreTab = getTvTab(R.string.more, R.drawable.tab_more_selecter);
		MoreTab.setTextSize(11);
		mTabWidget.addView(MoreTab);
		MoreTab.setOnClickListener(mTabClickListener);
		
		// 我的
		AccountTab = getTvTab(R.string.account, R.drawable.tab_account_selecter);
		AccountTab.setTextSize(11);
		mTabWidget.addView(AccountTab);
		AccountTab.setOnClickListener(mTabClickListener);
		
		mViewPager = (CustomViewPager) findViewById(R.id.viewpager);
		mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(mPageChangeListener);
		mViewPager.setCanScroll(true);// 控制能否左右滑动
		
		mTabWidget.setCurrentTab(tab);
		mViewPager.setCurrentItem(tab);
		mViewPager.setOffscreenPageLimit(3);

		registerBoradcastReceiver();
		registerAppReceiver();
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
	}


	//签到
	private void isSignin() {
		kjh = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("sid", AppVariables.sid);
		kjh.post(AppConstants.ISSIGNIN, params, new HttpCallBack(MainActivity.this) {
			@Override
			public void onSuccess(String t) {
				try {
					JSONObject ret = new JSONObject(t);
					JSONObject o = ret.getJSONObject("body");
					AppVariables.isSignin = o.getBoolean("isLogin");
					if (AppVariables.isSignin) {
						AppVariables.tel = AppConfig.getAppConfig(MainActivity.this).get("tel");
						AppVariables.sid = AppConfig.getAppConfig(MainActivity.this).get("sid");
						String uid = AppConfig.getAppConfig(MainActivity.this).get("uid");

						if (!StringUtils.isEmpty(uid)) {
							AppVariables.uid = uid;
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}


	//切换页面点击事件
	private OnClickListener mTabClickListener = new OnClickListener() {
		Intent intent;
		@Override
		public void onClick(View v) {
			
			if (v == IndexTab) {
				intent = new Intent();
	            intent.setAction("tab");
	            intent.putExtra("tab", "index");
	            sendBroadcast(intent);
//				mViewPager.setCurrentItem(0);
			} else if(v == ProductTab) {
				intent = new Intent();
				intent.setAction("tab");
	            intent.putExtra("tab", "product");
	            sendBroadcast(intent);
//				mViewPager.setCurrentItem(1);
			} else if(v == MoreTab) {
				intent = new Intent();
				intent.setAction("tab");
	            intent.putExtra("tab", "more");
	            sendBroadcast(intent);
//				mViewPager.setCurrentItem(2);
			} else if(v == AccountTab) {
				
				if (ApplicationUtil.isNeedGesture(getApplicationContext())) {
					startActivity(new Intent(getApplicationContext(), GestureActivity.class));
				}
				
				if (!AppVariables.isSignin) {
					Intent intent = new Intent(MainActivity.this, SigninActivity.class);
					startActivityForResult(intent, 1);
				} else {
					intent = new Intent();
					intent.setAction("tab");
		            intent.putExtra("tab", "account");
		            sendBroadcast(intent);
//					mViewPager.setCurrentItem(3);
				}
			}
		}
	};
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 1 && resultCode == 1) {
			String state = data.getStringExtra("state");
			if("s".equals(state)) {
				mViewPager.setCurrentItem(0);
			}
		}
    }

    //接口回调切换页面点击事件
	private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0://首页
				center.setVisibility(View.GONE);
				tel_center.setText("");
				headLinear.setVisibility(View.GONE);
				mTabWidget.setCurrentTab(arg0);
				break;
			case 1://投资
				center.setVisibility(View.VISIBLE);
				tel_center.setText("投资");
				headLinear.setVisibility(View.VISIBLE);
				mTabWidget.setCurrentTab(arg0);
				break;
			case 2://更多
				center.setVisibility(View.VISIBLE);
				tel_center.setText("更多");
				headLinear.setVisibility(View.VISIBLE);
				mTabWidget.setCurrentTab(arg0);
				break;
			case 3://我的
				
				if (ApplicationUtil.isNeedGesture(getApplicationContext())) {
					startActivity(new Intent(getApplicationContext(), GestureActivity.class));
				}
				
				if (!AppVariables.isSignin) {
					mViewPager.setCurrentItem(2);
					Intent intent = new Intent(MainActivity.this, SigninActivity.class);
					startActivityForResult(intent, 1);
				} else {
					center.setVisibility(View.GONE);
					tel_center.setText("");
					headLinear.setVisibility(View.GONE);
					mTabWidget.setCurrentTab(arg0);
					break;
				}
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	private TextView getTvTab(int txtId, int resId) {
		TextView tv = new TextView(this);
		tv.setFocusable(true);
		tv.setText(getString(txtId));
		tv.setTextColor(getResources().getColorStateList(R.drawable.tab_font_selecter));
		tv.setTextSize(14);
		Drawable icon = getResources().getDrawable(resId);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight());
		tv.setCompoundDrawables(null, icon, null, null); // 设置上图标
		tv.setCompoundDrawablePadding(5);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}

	private class MyPagerAdapter extends FragmentStatePagerAdapter {
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (IndexFrag == null) {
					return new IndexFragment();
				} else {
					return IndexFrag;
				}
			case 1:
				if (ProductFrag == null) {
					return new ProductFragment();
				} else {
					return ProductFrag;
				}
			case 2:
				if (MoreFrag == null) {
					return new MoreFragment();
				} else {
					return MoreFrag;
				}
			case 3:
				if (AccountFrag == null) {
					return new AccountFragment();
				} else {
					return AccountFrag;
				}
			}
			return null;
		}

		@Override
		public int getCount() {
			return 4;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		AppVariables.lastTime = new Date().getTime();

	}

	// 再按一次退出程序
	@Override
	public void onBackPressed() {
		long secondTime = System.currentTimeMillis();
		if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
			ToastCommom toastCommom = ToastCommom.createToastConfig();
			toastCommom.ToastShow(this, (ViewGroup) findViewById(R.id.toast_layout_root), "再按一次退出程序");
			firstTime = secondTime;// 更新firstTime
		} else { // 两次按键小于2秒时，退出应用
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(appReceiver);
		unregisterReceiver(broadcastReceiver);// 注销广播
	}

	@Override
	public void finish() {
		super.finish();
	}
	
	/**
	 * 注册监听屏幕打开关闭广播
	 * */
	private void registerAppReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.SCREEN_OFF");
		intentFilter.addAction("android.intent.action.SCREEN_ON");
		appReceiver = new AppReceiver();
		registerReceiver(appReceiver, intentFilter);
		
	}
	
	/**
	 * 注册切换页面广播
	 * */
	private void registerBoradcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("tab");
		broadcastReceiver = new MyBroadcastReceiver();
		registerReceiver(broadcastReceiver, intentFilter);
	}

	/**
	 * 广播接收器
	 * */
	public class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String tab = intent.getStringExtra("tab");
			switch (tab) {
			case "index":
				mViewPager.setCurrentItem(0);
				break;
			case "product":
				mViewPager.setCurrentItem(1);
				break;
			case "more":
				mViewPager.setCurrentItem(2);
				break;
			case "account":
				
				if(!AppVariables.isSignin) {
					startActivity(new Intent(MainActivity.this, SigninActivity.class));
				} else {
					
					if (ApplicationUtil.isNeedGesture(getApplicationContext())) {
						startActivity(new Intent(getApplicationContext(), GestureActivity.class));
					}
					
					mViewPager.setCurrentItem(3);
				}
				break;
			}
		}
	}

}
