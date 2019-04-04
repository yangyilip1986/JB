package com.jiubang.p2p.ui;

import java.text.DecimalFormat;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.CustomDialogUtil;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.UIHelper;
import com.jiubang.p2p.view.KeyboardLayout;
import com.jiubang.p2p.view.KeyboardLayout.onKybdsChangeListener;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;

/**
 * 债权转让申请
 * */
@SuppressLint("NewApi")
public class TransferActivity extends Activity implements OnClickListener{

	private HttpParams params;
	private KJHttp http;
	
	private String url;

	private TextView tv_products_title;
	private TextView tv_lave_date;
	private TextView tv_date;
	private TextView tv_finance_interest_rate;
	private TextView tv_off_shelf_time;
	private TextView tv_yield;
	private TextView tv_interest;
	private Button btn_transfer_publish;
	private TextView tv_counter_fee;
	private TextView tv_transfer_price;
	private TextView tv_agreement;
	private TextView tv_fundrais_term;
	
	private PopupWindow popupWindow2;
	private PopupWindow popupWindow3;
	private PopupWindow popupWindow4;
	private PopupWindow popupWindow5;
	private PopupWindow popupWindow6;
	private PopupWindow popupWindow7;
	
	private ImageView iv_problem1;
	private ImageView iv_problem2;
	private ImageView iv_problem3;
	private ImageView iv_problem4;
	private ImageView iv_problem5;
	private ImageView iv_problem6;
	private ImageView iv_problem7;

	private String discount_amount = "";
	private EditText et_discount_amount;
	private KeyboardLayout keyboardLayout;
	
    private ImageView drop_down_menu;//右上角下拉菜单
	private TitlePopup titlePopup;

	private String products_title;// 产品标题
	private String oid_tender_id;
	private String tender_from;// 转让来源（1：借款标；2：债权标）
	private String lave_date;// 剩余期限
	private String date;// 期限
	private String finance_interest_rate;// 预期收益率
	private String extra_rate;// 活动加息
	private String tender_amount;// 项目本金
	private String interest = "0";// 利息金额
	private String procedure_cost;// 转让手续费
	private String off_shelf_time;// 预计下架时间
	private String fundrais_term;// 筹款期限
	private String listed_income;// 挂牌收益率
	private String agreement_name;// 合同名
	private String contractUrl;// 合同URL
	
	private LinearLayout ll;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_transfer);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		UIHelper.setTitleView(this, "", "申请转让");
		
		ActivityUtil.getActivityUtil().addActivity(this);
		
		Bundle extras = getIntent().getExtras();
		oid_tender_id = extras.getString("oid_tender_id");
		tender_from = extras.getString("tender_from");
		
		init();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		getData("0");
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	private void getData(String discount) {
		params = new HttpParams();
		params.put("tenderFrom", tender_from);
		params.put("oid_tender_id", oid_tender_id);
		params.put("discount", discount);
		params.put("sid", AppVariables.sid);
		
		http = new KJHttp();
		url = AppConstants.TRANSFER_INFO;
		http.post(url, params, new HttpCallBack(TransferActivity.this) {
			@Override
			public void failure(JSONObject ret) {
				super.failure(ret);
			}

			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				
				try {
					products_title = ret.getString("products_title");
					lave_date = ret.getString("lave_date");
					date = ret.getString("date");
					finance_interest_rate = ret.getString("finance_interest_rate");
					extra_rate = ret.getString("extra_rate");
					tender_amount = ret.getString("tender_amount");
					interest = ret.getString("interest");
					procedure_cost = ret.getString("procedure_cost");
					off_shelf_time = ret.getString("off_shelf_time");
					fundrais_term = ret.getString("fundrais_term");
					listed_income = ret.getString("listed_income");
					agreement_name = ret.getString("agreement_name");
					contractUrl = ret.getString("url");
					
					tv_products_title.setText(products_title);
					tv_lave_date.setText(lave_date + "天");
					tv_date.setText(date);
					if("".equals(extra_rate)) {
						tv_finance_interest_rate.setText(finance_interest_rate + "%");// 预期年化收益率
					} else {
						tv_finance_interest_rate.setText(finance_interest_rate + "%" + "+" + extra_rate + "%");// 预期年化收益率 + 加息
					}
					tv_yield.setText(listed_income + "%");// 挂牌收益率
					tv_interest.setText(interest + "元");
					
					et_discount_amount.addTextChangedListener(textWatcher);
					String discount_amount = et_discount_amount.getText().toString();// 输入的折让利息金额
					if ("".equals(discount_amount)) {
						discount_amount = "0";
					}
					DecimalFormat df = new DecimalFormat("#.00");
					tv_transfer_price.setText(df.format(Double.parseDouble(tender_amount.replace(",", "")) + (Double.parseDouble(interest) - Double.parseDouble(discount_amount))) + "元");
					
					tv_counter_fee.setText(procedure_cost + "元");// 转让手续费
					tv_off_shelf_time.setText(off_shelf_time);
					tv_fundrais_term.setText(fundrais_term + "小时");
					tv_agreement.setText(agreement_name);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(TransferActivity.this, R.string.app_data_error, Toast.LENGTH_SHORT).show();
				}
			}

			public void onFinish() {
				super.onFinish();
			}
		});
	}
	
	TextWatcher textWatcher = new TextWatcher() {
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void afterTextChanged(Editable s) {
			et_discount_amount.addTextChangedListener(textWatcher);
			String discount_amount = et_discount_amount.getText().toString();// 输入的折让利息金额
			if ("".equals(discount_amount)) {
				discount_amount = "0";
			}
			DecimalFormat df = new DecimalFormat("#.00");
			tv_transfer_price.setText(df.format(Double.parseDouble(tender_amount.replace(",", "")) + (Double.parseDouble(interest) - Double.parseDouble(discount_amount))) + "元");
		}
	};
	
	/**
	 * 获得double小数位数
	 * */
	private int getDoubleDigits(double v){ 
		String value= v + "";
		int index = value.indexOf(".");
		if(index != -1) { 
			value = value.substring(index + 1, value.length()); 
			return value.length();
		} else {
			return 0; 
		}
	}

	private void init() {
		tv_products_title = (TextView) findViewById(R.id.tv_products_title);
		tv_lave_date = (TextView) findViewById(R.id.tv_lave_date);
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_finance_interest_rate = (TextView) findViewById(R.id.tv_finance_interest_rate);
		tv_off_shelf_time = (TextView) findViewById(R.id.tv_off_shelf_time);
		tv_yield = (TextView) findViewById(R.id.tv_yield);
		tv_interest = (TextView) findViewById(R.id.tv_interest);
		btn_transfer_publish = (Button) findViewById(R.id.btn_transfer_publish);
		tv_counter_fee = (TextView) findViewById(R.id.tv_counter_fee);
		tv_transfer_price = (TextView) findViewById(R.id.tv_transfer_price);
		tv_agreement = (TextView) findViewById(R.id.tv_agreement);
		tv_fundrais_term = (TextView) findViewById(R.id.tv_fundrais_term);
		
		et_discount_amount = (EditText) findViewById(R.id.et_discount_amount);
		et_discount_amount.setImeOptions(EditorInfo.IME_ACTION_DONE);// 监听键盘done
		
		et_discount_amount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {//回车键
                	if(!"".equals(et_discount_amount.getText().toString())){
                		getData(et_discount_amount.getText().toString());
                	}
                }
                return true;
			}
        });
		
		keyboardLayout = (KeyboardLayout) findViewById(R.id.keyboardLayout);
		
		ll = (LinearLayout) findViewById(R.id.ll);
		
		iv_problem1 = (ImageView) findViewById(R.id.iv_problem1);
		iv_problem2 = (ImageView) findViewById(R.id.iv_problem2);
		iv_problem3 = (ImageView) findViewById(R.id.iv_problem3);
		iv_problem4 = (ImageView) findViewById(R.id.iv_problem4);
		iv_problem5 = (ImageView) findViewById(R.id.iv_problem5);
		iv_problem6 = (ImageView) findViewById(R.id.iv_problem6);
		iv_problem7 = (ImageView) findViewById(R.id.iv_problem7);
		
		drop_down_menu = (ImageView) findViewById(R.id.drop_down_menu);

		btn_transfer_publish.setOnClickListener(this);
		iv_problem1.setOnClickListener(this);
		iv_problem2.setOnClickListener(this);
		iv_problem3.setOnClickListener(this);
		iv_problem4.setOnClickListener(this);
		iv_problem5.setOnClickListener(this);
		iv_problem6.setOnClickListener(this);
		iv_problem7.setOnClickListener(this);
		tv_agreement.setOnClickListener(this);
		drop_down_menu.setOnClickListener(this);
		ll.setOnClickListener(this);
		keyboardLayout.setOnClickListener(this);
		keyboardLayout.setOnkbdStateListener(listener);
		
	}
	
	onKybdsChangeListener listener = new onKybdsChangeListener() {
		@Override
		public void onKeyBoardStateChange(int state) {
			switch (state) {
			case KeyboardLayout.KEYBOARD_STATE_HIDE:
				LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.task_detail_popupwindow, null);
				
				if("".equals(et_discount_amount.getText().toString())){
					getData("0");
				} else {
					if(getDoubleDigits(Double.parseDouble(et_discount_amount.getText().toString())) > 2){
						
						popupWindow3 = new PopupWindow(view);
						iniPopupWindow("折让利息金额不能超过两位小数", popupWindow3, view);
						
						if (popupWindow3.isShowing()) {
							popupWindow3.dismiss();// 关闭
						} else {
							popupWindow3.showAsDropDown(iv_problem3);// 显示
						}
						
						break;
					}
					
					if(Double.parseDouble(listed_income) > 24){
						
						popupWindow6 = new PopupWindow(view);
						iniPopupWindow("挂牌收益率不得超过24%", popupWindow6, view);
						
						if (popupWindow6.isShowing()) {
							popupWindow6.dismiss();// 关闭
						} else {
							popupWindow6.showAsDropDown(iv_problem6);// 显示
						}
						
						break;
					}
					
					if(Double.parseDouble(et_discount_amount.getText().toString()) > Double.parseDouble(interest)){
						popupWindow3 = new PopupWindow(view);
						iniPopupWindow("折让利息金额不可超过预期所得利息", popupWindow3, view);
						
						if (popupWindow3.isShowing()) {
							popupWindow3.dismiss();// 关闭
						} else {
							popupWindow3.showAsDropDown(iv_problem3);// 显示
						}
						break;
					}
					getData(et_discount_amount.getText().toString());
					
					btn_transfer_publish.setBackground(getResources().getDrawable(R.color.app_blue));
					btn_transfer_publish.setEnabled(true);
				}
	            break;
	        case KeyboardLayout.KEYBOARD_STATE_SHOW:
	        	btn_transfer_publish.setBackground(getResources().getDrawable(R.color.gray));
				btn_transfer_publish.setEnabled(false);
	            break;
	        }
		}
	};
	
	@Override
	public void onClick(View v) {
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.task_detail_popupwindow, null);
		
		switch (v.getId()) {
		case R.id.ll:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	        
	        if(!"".equals(et_discount_amount.getText().toString()) && !discount_amount.equals(et_discount_amount.getText().toString())) {
	        	discount_amount = et_discount_amount.getText().toString();
        		getData(et_discount_amount.getText().toString());
        	}
			break;
		case R.id.btn_transfer_publish:
			
			if("".equals(et_discount_amount.getText().toString())){
				final CustomDialogUtil dialog = new CustomDialogUtil(TransferActivity.this);
				dialog.setTitle("温馨提示");
				dialog.setMessage("请输入折让利息金额");
				dialog.positiveClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				break;
			} else {
				if(getDoubleDigits(Double.parseDouble(et_discount_amount.getText().toString())) > 2){
					final CustomDialogUtil dialog = new CustomDialogUtil(TransferActivity.this);
					dialog.setTitle("温馨提示");
					dialog.setMessage("折让利息金额不能超过两位小数");
					dialog.positiveClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					break;
				}
				
				if(Double.parseDouble(listed_income) > 24){
					final CustomDialogUtil dialog = new CustomDialogUtil(TransferActivity.this);
					dialog.setTitle("温馨提示");
					dialog.setMessage("挂牌收益率不得超过24%");
					dialog.positiveClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					break;
				}
				
				if(Double.parseDouble(et_discount_amount.getText().toString()) > Double.parseDouble(interest)){
					final CustomDialogUtil dialog = new CustomDialogUtil(TransferActivity.this);
					dialog.setTitle("温馨提示");
					dialog.setMessage("折让利息金额不可超过预期所得利息");
					dialog.positiveClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					break;
				}
			}
			
			final CustomDialogUtil dialog = new CustomDialogUtil(TransferActivity.this);
			dialog.setTitle("温馨提示");
			dialog.setMessage("您确定转让该产品吗？");
			dialog.positiveClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					sendData();
				}
			});
			dialog.cancelClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			break;
		case R.id.iv_problem2:
			if (popupWindow2 == null) {
				popupWindow2 = new PopupWindow(view);
			}
			iniPopupWindow("从起息日至转让发布日当天，为计息天数，此处为默认全部转出所得利息，仅供参考", popupWindow2, view);
			
			if (popupWindow2.isShowing()) {
				popupWindow2.dismiss();// 关闭
			} else {
				popupWindow2.showAsDropDown(iv_problem2);// 显示
			}
			break;
		case R.id.iv_problem3:
			if (popupWindow3 == null) {
				popupWindow3 = new PopupWindow(view);
			}
			iniPopupWindow("折让利息金额不可超过预期所得利息", popupWindow3, view);
			
			if (popupWindow3.isShowing()) {
				popupWindow3.dismiss();// 关闭
			} else {
				popupWindow3.showAsDropDown(iv_problem3);// 显示
			}
			break;
		case R.id.iv_problem4:
			if (popupWindow4 == null) {
				popupWindow4 = new PopupWindow(view);
			}
			iniPopupWindow("根据平台运营标准，预计将收取转让本金0.1%的平台服务费，具体以实际产生费用为主", popupWindow4, view);
			
			if (popupWindow4.isShowing()) {
				popupWindow4.dismiss();// 关闭
			} else {
				popupWindow4.showAsDropDown(iv_problem4);// 显示
			}
			break;
		case R.id.iv_problem5:
			if (popupWindow5 == null) {
				popupWindow5 = new PopupWindow(view);
			}
			iniPopupWindow("转让本金+预期所得利息-折让利息金额", popupWindow5, view);
			
			if (popupWindow5.isShowing()) {
				popupWindow5.dismiss();// 关闭
			} else {
				popupWindow5.showAsDropDown(iv_problem5);// 显示
			}
			break;
		case R.id.iv_problem6:
			if (popupWindow6 == null) {
				popupWindow6 = new PopupWindow(view);
			}
			iniPopupWindow("收益率不能超过24%", popupWindow6, view);
			
			if (popupWindow6.isShowing()) {
				popupWindow6.dismiss();// 关闭
			} else {
				popupWindow6.showAsDropDown(iv_problem6);// 显示
			}
			break;
		case R.id.iv_problem7:
			if (popupWindow7 == null) {
				popupWindow7 = new PopupWindow(view);
			}
			iniPopupWindow("转让筹款期限为24小时", popupWindow7, view);
			
			if (popupWindow7.isShowing()) {
				popupWindow7.dismiss();// 关闭
			} else {
				popupWindow7.showAsDropDown(iv_problem7);// 显示
			}
			break;
		case R.id.tv_agreement:
			Intent protocol = new Intent(this, TenderProtocolActivity.class);
			protocol.putExtra("url", contractUrl);
			startActivity(protocol);
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
	
	private void sendData() {
		params = new HttpParams();
		params.put("sid", AppVariables.sid);
		params.put("oid_tender_id", oid_tender_id);
		params.put("tender_from", tender_from);
		params.put("transfer_capital", tender_amount);
		params.put("discount_amount", et_discount_amount.getText().toString());
		http = new KJHttp();
		url = AppConstants.TRANSFER_PUBLISH;
		http.post(url, params, httpCallback);
	}

	private HttpCallBack httpCallback = new HttpCallBack(TransferActivity.this) {

		@Override
		public void failure(JSONObject ret) {
			super.failure(ret);
		}

		@Override
		public void success(JSONObject ret) {
			super.success(ret);
			try {
				if ("1".equals(ret.getString("transfer_tag"))) {
					Intent intent = new Intent(TransferActivity.this, TransferSuccessActivity.class);
					intent.putExtra("transfer_price", tv_transfer_price.getText().toString());
					intent.putExtra("lave_date", lave_date);
					startActivity(intent);
					TransferActivity.this.finish();
				} else if ("0".equals(ret.getString("transfer_tag"))) {
					Intent intent = new Intent(TransferActivity.this, TransferFailActivity.class);
					startActivity(intent);
					TransferActivity.this.finish();
				}
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(TransferActivity.this, R.string.app_data_error, Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
		}
	};

	private void iniPopupWindow(String msg, PopupWindow popupWindow , View v) {
		
		TextView tv_popup = (TextView) v.findViewById(R.id.tv_popup);
		tv_popup.setText(msg);
		tv_popup.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

		// 控制popupwindow的宽度和高度自适应
		popupWindow.setWidth(tv_popup.getMeasuredWidth());
		popupWindow.setHeight(tv_popup.getMeasuredHeight());

		// 控制popupwindow点击屏幕其他地方消失
		popupWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
		popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
	}
}
