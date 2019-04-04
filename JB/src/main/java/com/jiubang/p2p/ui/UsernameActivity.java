package com.jiubang.p2p.ui;

import java.io.UnsupportedEncodingException;

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
 * 昵称设置
 * */
public class UsernameActivity extends KJActivity {

	@BindView(id = R.id.username, click = true)
	private TextView mUsername;
	@BindView(id = R.id.confirm, click = true)
	private TextView mConfirm;
	@BindView(id = R.id.rl,click = true)
	private LinearLayout rl;
	
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;//右上角下拉菜单
	
	private TitlePopup titlePopup;

	private String name;

	private KJHttp kjh;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_username);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		UIHelper.setTitleView(this, "", "昵称设置");
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
		name = mUsername.getText().toString();
		if (StringUtils.isEmpty(name)) {
			ToastCommom toastCommom = ToastCommom.createToastConfig();
			toastCommom.ToastShow(UsernameActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请输入用户名");
			return;
		}
		kjh = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("sid", AppVariables.sid);
		try {
			params.put("name", new String(name.getBytes(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		kjh.post(AppConstants.ADDNAME, params, new HttpCallBack(
				UsernameActivity.this) {
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				ToastCommom toastCommom = ToastCommom.createToastConfig();
				toastCommom.ToastShow(UsernameActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "设置成功");
				finish();
			}
		});
	}
}
