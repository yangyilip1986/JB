package com.jiubang.p2p.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
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

import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.R;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.ToastCommom;

/**
 * 注册
 * */
public class SiginupActivity extends KJActivity {

	@BindView(id = R.id.tel)
	private EditText mTel;

	@BindView(id = R.id.pwd)
	private EditText mPwd;

	@BindView(id = R.id.pwdconfirm)
	private EditText mPwdConfirm;

	@BindView(id = R.id.verifyimage, click = true)
	private ImageView mVrifyImage;

	@BindView(id = R.id.code, click = true)
	private TextView mCode;

	@BindView(id = R.id.tv_signup, click = true)
	private TextView tv_signup;

	@BindView(id = R.id.go_sigin, click = true)
	private TextView go_Sign;

	@BindView(id = R.id.verify1)
	private LinearLayout mVrify1;

	@BindView(id = R.id.protocol, click = true)
	private TextView mProtocol;
	
	@BindView(id = R.id.iv_siginup_show, click = true)
	private ImageView iv_siginup_show;
	
	@BindView(id = R.id.iv_back, click = true)
	private ImageView iv_back;
	
	@BindView(id = R.id.ll,click = true)
	private LinearLayout ll;

	private String tel;
	private String pwd;
	private String pwdc;
	
	private boolean flag = true;
	
	private KJHttp kjHttp;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_signup);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		ActivityUtil.getActivityUtil().addActivity(this);
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.ll:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;
		case R.id.tv_signup:
			tel = mTel.getText().toString();
			pwd = mPwd.getText().toString();
			pwdc = mPwdConfirm.getText().toString();
			
			String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(pwd);
			ToastCommom toastCommom = ToastCommom.createToastConfig();
			if(StringUtils.isEmpty(tel)){
				toastCommom.ToastShow(SiginupActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请填写完整信息");
				break;
			} else if(tel.length() != 11 || !isNumeric(tel)){
				toastCommom.ToastShow(SiginupActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请填写正确的手机号");
				break;
			} else if (StringUtils.isEmpty(pwd)) {
				toastCommom.ToastShow(SiginupActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请填写完整信息");
				break;
			} else if (matcher.find()) {
				toastCommom.ToastShow(SiginupActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "8-20位字母、数字和符号的组合");
				break;
			} else if (pwd.length() < 8 || pwd.length() > 20) {
				toastCommom.ToastShow(SiginupActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "8-20位字母、数字和符号的组合");
				break;
			} else if (isNumeric(pwd)) {
				toastCommom.ToastShow(SiginupActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "8-20位字母、数字和符号的组合");
				break;
			} else if (!pwd.equals(pwdc)) {
				toastCommom.ToastShow(SiginupActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "两次输入的密码不一致");
				break;
			} else {
				post();
			}
			break;
		case R.id.protocol:
			startActivity(new Intent(this, SignupProtocolActivity.class));
			break;
		case R.id.go_sigin:
			startActivity(new Intent(this, SigninActivity.class));
			finish();
			break;
		case R.id.iv_siginup_show:
			if (flag) {
				// 显示密码
				iv_siginup_show.setImageResource(R.drawable.show);
				mPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				mPwdConfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				flag = false;
			} else {
				// 隐藏密码
				iv_siginup_show.setImageResource(R.drawable.hide);
				mPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				mPwdConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
				flag = true;
			}
			break;
		case R.id.iv_back:
			finish();
			break;
		}
	}
	
	/**
	 * 校验手机号
	 * */
	private void post() {
		kjHttp = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("phone", tel);
		kjHttp.post(AppConstants.CHECK_PHONE, params, new HttpCallBack(SiginupActivity.this) {
			@Override
			public void onSuccess(String t) {
				try {
					
					JSONObject ret = new JSONObject(t);
					
					ToastCommom toastCommom = ToastCommom.createToastConfig();
					if(!"".equals(ret.getString("msg"))) {
						toastCommom.ToastShow(SiginupActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), ret.getString("msg"));
					}else {
						Intent intent = new Intent(SiginupActivity.this, SiginupMessageActivity.class);
						intent.putExtra("tel", tel);
						intent.putExtra("pwd", pwd);
						startActivity(intent);
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onPreStart() {
			}

			@Override
			public void onFinish() {
			}
		});
	}
	
	/**
	 * 纯数字
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

}
