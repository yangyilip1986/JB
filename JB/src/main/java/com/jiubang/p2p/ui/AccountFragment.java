package com.jiubang.p2p.ui;

import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiubang.p2p.AppConfig;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.CustomDialogUtil;
import com.jiubang.p2p.receiver.ScreenStatusReceiver;
import com.jiubang.p2p.utils.ConvUtils;
import com.jiubang.p2p.utils.ToastCommom;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.utils.StringUtils;

@SuppressLint("InflateParams")
public class AccountFragment extends Fragment {

	protected static final int MODE_PRIVATE = 0;
	private LinearLayout ll_red;
	private LinearLayout ll_sign;
	private TextView tv_sign;
	private LinearLayout ll_member;
	private LinearLayout ll_trans;
	private LinearLayout ll_transfer;
	private LinearLayout ll_invest;
	private LinearLayout ll_integral;
	private LinearLayout ll_invite;
	private LinearLayout ll_account;
	private TextView tv_text;
	private ImageView tv_account_mailbox;
	private ImageView iv_red_point;
	
	private View v;
	private TextView mTotal;
	private TextView mGain;
	private TextView mUnrepaid;
	private TextView mAmount;
	private TextView mBalance;
	private TextView mfrozeAmount;
	private LinearLayout ll_pop_sign;
	private TextView mCharge;
	private TextView mCash;
	private TextView tv_totalAmtXianjinCoupon;
	private TextView tv_countJiaxiCoupon;
	
	private String mUnrepaid_string;
	private String mTotal_string;
	private String mGain_string;
	private String mAmount_string;
	private String mBalance_string;
	private String mfrozeAmount_string;

	private TextView tv_qiandao;
	private ImageView iv_background;
	private Button btn_ok;

	private double available;

	private KJHttp http;
	private HttpParams params;
	
	private ImageView iv_level;
	private String name = null;
	private TextView tel_center;
	private int member_level;// 会员等级
	
	private LinearLayout ll_see;//可见与不可见
	private String see;
	
	private TextView tv_totalmoney;
	
	private MyBroadcastReceiver broadcastReceiver;
	private ScreenStatusReceiver mScreenStatusReceiver;
	
	// 签到次数
	private int signday;

	private int attemp = 0; // 个人中心数据拉取尝试次数
	
	static boolean isActive = true;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_account, null);
		http = new KJHttp();
		params = new HttpParams();
		initView();
		
		registerBoradcastReceiver();
		registSreenStatusReceiver();
		
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		getData();
		
		// 是否打开了手势密码
		String pwd = AppConfig.getAppConfig(getActivity()).get("gesture");
		
        if((!isActive) && !StringUtils.isEmpty(pwd)) {
            isActive = true;
            Intent n = new Intent(getActivity(), GestureActivity.class);
            startActivity(n);
        }
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		AppVariables.lastTime = new Date().getTime();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		if(!isAppOnFreground()){
            isActive = false;
        } 
	}

	private void initView() {
		
		ll_red = (LinearLayout) v.findViewById(R.id.ll_red);
		ll_sign = (LinearLayout) v.findViewById(R.id.ll_sign);
		tv_sign = (TextView) v.findViewById(R.id.tv_sign);
		ll_member = (LinearLayout) v.findViewById(R.id.ll_member);
		ll_trans = (LinearLayout) v.findViewById(R.id.ll_trans);
		ll_transfer = (LinearLayout) v.findViewById(R.id.ll_transfer);
		ll_invest = (LinearLayout) v.findViewById(R.id.ll_invest);
		ll_integral = (LinearLayout) v.findViewById(R.id.ll_integral);
		ll_invite = (LinearLayout) v.findViewById(R.id.ll_invite);
		ll_account = (LinearLayout) v.findViewById(R.id.ll_account);
		tv_text = (TextView) v.findViewById(R.id.tv_text);
		tv_account_mailbox = (ImageView) v.findViewById(R.id.tv_account_mailbox);
		iv_red_point = (ImageView) v.findViewById(R.id.iv_red_point);
		tv_totalAmtXianjinCoupon = (TextView) v.findViewById(R.id.tv_totalAmtXianjinCoupon);
		tv_countJiaxiCoupon = (TextView) v.findViewById(R.id.tv_countJiaxiCoupon);
		ll_see = (LinearLayout) v.findViewById(R.id.ll_see);
		tv_totalmoney = (TextView) v.findViewById(R.id.tv_totalmoney);
		
		ll_red.setOnClickListener(listener);
		ll_sign.setOnClickListener(listener);
		ll_member.setOnClickListener(listener);
		ll_trans.setOnClickListener(listener);
		ll_transfer.setOnClickListener(listener);
		ll_invest.setOnClickListener(listener);
		ll_integral.setOnClickListener(listener);
		ll_invite.setOnClickListener(listener);
		ll_account.setOnClickListener(listener);
		tv_account_mailbox.setOnClickListener(listener);
		ll_see.setOnClickListener(listener);
		
		mTotal = (TextView) v.findViewById(R.id.total);
		mGain = (TextView) v.findViewById(R.id.gain);
		mUnrepaid = (TextView) v.findViewById(R.id.unrepaid);
		mAmount = (TextView) v.findViewById(R.id.amount);
		mBalance = (TextView) v.findViewById(R.id.balance);
		mfrozeAmount = (TextView) v.findViewById(R.id.frozeAmount);
		mCharge = (TextView) v.findViewById(R.id.charge);
		mCharge.setOnClickListener(listener);
		mCash = (TextView) v.findViewById(R.id.cash);
		mCash.setOnClickListener(listener);

		// 签到
		ll_pop_sign = (LinearLayout) v.findViewById(R.id.ll_pop_sign);
		ll_pop_sign.setOnClickListener(listener);
		tv_qiandao = (TextView) v.findViewById(R.id.tv_qiandao);
		iv_background = (ImageView) v.findViewById(R.id.iv_background);
		btn_ok = (Button) v.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(listener);
		
		tel_center = (TextView) v.findViewById(R.id.tel_center);
		iv_level = (ImageView) v.findViewById(R.id.iv_level);
		
	}

	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent;
			
			switch (v.getId()) {
			
			case R.id.ll_red://现金券
				intent = new Intent(getActivity(), RedActivity.class);
				startActivity(intent);
				break;
			case R.id.ll_sign://签到
				sign();
				break;
			case R.id.ll_member://会员中心
				startActivity(new Intent(getActivity(), MemberActivity.class));
				break;
			case R.id.ll_trans://交易记录
				startActivity(new Intent(getActivity(), TransactionActivity.class));
				break;
			case R.id.ll_transfer:// 转让项目
				startActivity(new Intent(getActivity(), ClaimsTransferActivity.class));
				break;
			case R.id.ll_invest://回款计划
				intent = new Intent(getActivity(), InvestActivity.class);
				intent.putExtra("state", 1);
				startActivity(intent);
				break;
			case R.id.ll_integral://我的积分
				startActivity(new Intent(getActivity(), IntegralMallActivity.class));
				break;
			case R.id.ll_invite:
				getLevel();// 邀请
				break;
			case R.id.ll_account://账户中心
				startActivity(new Intent(getActivity(), AccountActivity.class));
				break;     
			case R.id.charge:
				charge();// 充值
				break;
			case R.id.cash:
				cash();// 提现
				break;
			case R.id.btn_ok:
				ll_pop_sign.setVisibility(View.GONE);
//				tv_sign.setText("已连续签到" + (signday + 1) + "天");
				tv_sign.setText("今日已签到");
				break;
			case R.id.tv_account_mailbox:
				startActivity(new Intent(getActivity(), MessageCenterActivity.class));
				break;
			case R.id.ll_see:
				if("1".equals(see)){
					setHide("0");
					params.put("sid", AppVariables.sid);
					http.post(AppConstants.ACCOUNT_SEE, params, null);
					getData();
				}else {
					setHide("1");
					params.put("sid", AppVariables.sid);
					http.post(AppConstants.ACCOUNT_SEE, params, null);
					getData();
				}
				break;
			}
		}

	};

	/**
	 * 获得账户页面信息
	 * */
	private void getData() {
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.GAIN, params, new HttpCallBack(getActivity()) {
			// 下面这两个方法必须重写覆盖掉父方法中的对话框显示
			@Override
			public void onPreStart() {
			}

			@Override
			public void onFinish() {
			}

			@Override
			public void onSuccess(String t) {
				try {
					JSONObject ret = new JSONObject(t);
					available = ret.getDouble("available");
					see = ret.getString("auto_show_or_hide_flg");
					mUnrepaid_string = ConvUtils.convToMoney(ret.getDouble("unrepaidInterest")) + "元";
					mTotal_string = ConvUtils.convToMoney(available+ ret.getDouble("investAmount")+ ret.getDouble("frozeAmount"));
					mBalance_string = ConvUtils.convToMoney(ret.getDouble("available"));
					mAmount_string = ConvUtils.convToMoney(ret.getDouble("investAmount"));
					mfrozeAmount_string = ConvUtils.convToMoney(ret.getDouble("frozeAmount"));
					mGain_string = ConvUtils.convToMoney(ret.getDouble("totalInterest"));
					tv_totalAmtXianjinCoupon.setText(ret.getString("totalAmtXianjinCoupon"));
					tv_countJiaxiCoupon.setText(ret.getString("countJiaxiCoupon"));
					
					setHide(AppConfig.getAppConfig(getActivity()).get("account_see"));
					signday = ret.getInt("signday");
					if (ret.getBoolean("is_sign")) {
						// if (signday == 0) {
						// tv_sign.setText("已连续签到7天");
						// } else {
						// tv_sign.setText("已连续签到" + signday + "天");
						// }
						tv_sign.setText("今日已签到");
					} else {
						tv_sign.setText("签到获取积分");
					}

					attemp = 0;

					int[] member_levels = { R.drawable.vip_01,
							R.drawable.vip_02, R.drawable.vip_03,
							R.drawable.vip_04, R.drawable.vip_05,
							R.drawable.vip_06, R.drawable.vip_07,
							R.drawable.vip_08 };

					if (ret.getInt("nameValidated") == 1) {
						name = ret.getString("nickname");
						tel_center.setText(name);
					} else {
						name = ret.getString("mobile");
						tel_center.setText(name.substring(0, name.length()- (name.substring(3)).length())+ "****" + name.substring(7));
					}
					member_level = ret.getInt("member");// 会员等级
					iv_level.setImageResource(member_levels[member_level - 1]);

					if(ret.getInt("noreadmessage") > 0) {
						iv_red_point.setVisibility(View.VISIBLE);
					} else {
						iv_red_point.setVisibility(View.GONE);
					}
					setHide(see);

				} catch (JSONException e) {
					e.printStackTrace();
					if (attemp < 3) {
						getData();
						attemp += 1;
					}
				}
			}
		});
	}
	
	/**
	 * 账户信息隐藏显示设置
	 */
	private void setHide(String flag_see) {
		Drawable drawable_can_see = getResources().getDrawable(R.drawable.account_can_see);
		drawable_can_see.setBounds(0, 0, drawable_can_see.getMinimumWidth(),drawable_can_see.getMinimumHeight());
		Drawable drawable_not_see = getResources().getDrawable(R.drawable.account_not_see);
		drawable_not_see.setBounds(0, 0, drawable_not_see.getMinimumWidth(),drawable_not_see.getMinimumHeight());

		if ("0".equals(flag_see)) {
			tv_totalmoney.setCompoundDrawables(null, null, drawable_can_see,null);
			mUnrepaid.setText(mUnrepaid_string);
			mTotal.setText(mTotal_string);
			mBalance.setText(mBalance_string);
			mAmount.setText(mAmount_string);
			mfrozeAmount.setText(mfrozeAmount_string);
			mGain.setText(mGain_string);
		} else {
			tv_totalmoney.setCompoundDrawables(null, null, drawable_not_see,null);
			mUnrepaid.setText("* * * *元");
			mTotal.setText("＊ ＊ ＊ ＊");
			mBalance.setText("＊ ＊ ＊ ＊");
			mAmount.setText("＊ ＊ ＊ ＊");
			mfrozeAmount.setText("＊ ＊ ＊ ＊ ");
			mGain.setText("＊ ＊ ＊ ＊");
		}
	}

	/**
	 * 充值
	 * */
	private void charge() {
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.BASICINFO, params, new HttpCallBack(getActivity()) {
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					JSONObject account = ret.getJSONObject("account");
					if (account.getInt("accountStatus") == 2) {
						// 充值
						Intent charge = new Intent(getActivity(), ChargeCashBFActivity.class);
						charge.putExtra("type", "charge");
						charge.putExtra("cardStatus", account.getInt("cardStatus"));// 绑卡标识 2：有效 0无效
						charge.putExtra("bf_bank_id", account.getString("bf_bank_id"));
						charge.putExtra("bankAccount", account.getString("bankAccount"));
						startActivity(charge);
					} else {
						final CustomDialogUtil dialog = new CustomDialogUtil(getActivity());
						dialog.setTitle("温馨提示");
						dialog.setMessage("请先实名认证");
						dialog.setPositive("前往");
						dialog.positiveClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								startActivity(new Intent(getActivity(), AccountActivity.class));
							}
						});
						dialog.cancelClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 提现
	 * */
	private void cash() {
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.BASICINFO, params, new HttpCallBack(getActivity()) {
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					JSONObject account = ret.getJSONObject("account");
					if (account.getInt("accountStatus") == 2) {
						Intent cash = new Intent(getActivity(), ChargeCashBFActivity.class);
						cash.putExtra("type", "cash");
						cash.putExtra("userPayPwd", account.getString("userPayPwd"));// 提现密码
						cash.putExtra("cardStatus", account.getInt("cardStatus"));// 绑卡标识 2：有效 0无效
						cash.putExtra("bf_bank_id", account.getString("bf_bank_id"));
						cash.putExtra("bankAccount", account.getString("bankAccount"));
						startActivity(cash);
					} else {
						final CustomDialogUtil dialog = new CustomDialogUtil(getActivity());
						dialog.setTitle("温馨提示");
						dialog.setMessage("请先实名认证");
						dialog.setPositive("前往");
						dialog.positiveClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								startActivity(new Intent(getActivity(), AccountActivity.class));
								dialog.dismiss();
							}
						});
						dialog.cancelClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 签到
	 * */
	private void sign() {
		http = new KJHttp();
		params = new HttpParams();
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.MY_USER_SIGN, params, new HttpCallBack(getActivity()) {
			// 下面这两个方法必须重写覆盖掉父方法中的对话框显示
			@Override
			public void onPreStart() {
			}

			@Override
			public void onFinish() {
			}

			@Override
			public void onSuccess(String t) {
				try {
					JSONObject ret = new JSONObject(t);
					if (ret.getBoolean("usersign")) {
						if (ret.getInt("signCnt") == 0) {
							iv_background.setImageDrawable(getResources().getDrawable(R.drawable.click_60));
							tv_text.setVisibility(View.GONE);
							tv_qiandao.setText("连续签到7天");
						} else {
							iv_background.setImageDrawable(getResources().getDrawable(R.drawable.click_10));
							tv_qiandao.setText("连续签到" + ret.getInt("signCnt") + "天");
						}
						ll_pop_sign.setVisibility(View.VISIBLE);
						
//						// 发送广播 通知已签到
//						Intent intent = new Intent();  
//			            intent.setAction("sign");
//			            getActivity().sendBroadcast(intent);
					} else {
						ToastCommom toastCommom = ToastCommom.createToastConfig();
						toastCommom.ToastShow(getActivity(), (ViewGroup) v.findViewById(R.id.toast_layout_root), "已签到");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 获得用户级别 邀请
	 * */
	private void getLevel() {
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.INVITE, params, new HttpCallBack(getActivity()) {
			@Override
			public void onSuccess(String t) {
				try {
					JSONObject ret = new JSONObject(t);
					int uid_level = ret.getInt("uid_level");
					if (uid_level == 2) {
						Intent intent = new Intent(getActivity(), YanInviteActivity.class);
						intent.putExtra("activity", "account");
						startActivity(intent);
					} else {
						Intent intent = new Intent(getActivity(), NormalInviteActivity.class);
						intent.putExtra("activity", "account");
						startActivity(intent);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getActivity().unregisterReceiver(broadcastReceiver);// 注销广播
		getActivity().unregisterReceiver(mScreenStatusReceiver);// 注销广播
	}
	
	/**
	 * 注册广播
	 * */
	private void registerBoradcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction("sign");
		intentFilter.addAction("tab");
		broadcastReceiver = new MyBroadcastReceiver();
		getActivity().registerReceiver(broadcastReceiver, intentFilter);
	}

	/**
	 * 广播接收器
	 * */
	public class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String tab = intent.getStringExtra("tab");
			switch (tab) {
			case "account":
				getData();
				break;
			}
		}
	}
	
    /** 
     * 是否在后台 
     */  
    public boolean isAppOnFreground(){  
        ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);  
        String curPackageName = getActivity().getApplicationContext().getPackageName();  
        List<RunningAppProcessInfo> app = am.getRunningAppProcesses();  
        if(app==null){  
            return false;  
        }  
        for(RunningAppProcessInfo a:app){  
            if(a.processName.equals(curPackageName) && a.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND){  
                return true;  
            }  
        }  
        return false;  
    }

    private void registSreenStatusReceiver() {
        mScreenStatusReceiver = new ScreenStatusReceiver();
        IntentFilter screenStatusIF = new IntentFilter();
        screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
        screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
        getActivity().registerReceiver(mScreenStatusReceiver, screenStatusIF);
    }
    
}
