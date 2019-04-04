package com.jiubang.p2p.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
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
 * 实名认证
 * */
public class IdcardActivity extends KJActivity {
	
	@BindView(id = R.id.drop_down_menu,click  = true)
	private ImageView drop_down_menu;
	@BindView(id = R.id.post, click = true)
	private TextView mPost;
	@BindView(id = R.id.name)
	private EditText mName;
	@BindView(id = R.id.id)
	private EditText mId;
	@BindView(id = R.id.rl,click = true)
	private LinearLayout rl;

	private HttpParams params;
	private KJHttp kjh;

	private String idcard;
	private String realName;
	private String req;
	private String sign;
	private String uri;
	
	private TitlePopup titlePopup;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_idcard);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		UIHelper.setTitleView(this, "", "实名认证");
		
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
		case R.id.post:
			realName = mName.getText().toString();
			idcard = mId.getText().toString();
			if (StringUtils.isEmpty(realName) || StringUtils.isEmpty(idcard)) {
				ToastCommom toastCommom = ToastCommom.createToastConfig();
				toastCommom.ToastShow(IdcardActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请填写完整信息");
			} else {
				post();
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
		}
	}

	private void post() {
		kjh = new KJHttp();
		params = new HttpParams();
		params.put("sid", AppVariables.sid);
		params.put("idCard", idcard);
		params.put("realName", realName);
		kjh.post(AppConstants.IDCARD, params, new HttpCallBack(
				IdcardActivity.this) {
			@Override
			public void success(JSONObject ret) {
				try {
					String mode = ret.getString("mode");
					if("gateway".equals(mode)){
						String call_back = ret.getString("call_back");
						Intent intent = new Intent(IdcardActivity.this, YibaoActivity.class);
						intent.putExtra("call_back", call_back);
						intent.putExtra("mode", mode);
						startActivity(intent);
						finish();
					} else {
						req = ret.getString("req");
						sign = ret.getString("sign");
						uri = ret.getString("uri");
						Intent intent = new Intent(IdcardActivity.this, YibaoActivity.class);
						intent.putExtra("req", req);
						intent.putExtra("sign", sign);
						intent.putExtra("uri", uri);
						startActivity(intent);
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
