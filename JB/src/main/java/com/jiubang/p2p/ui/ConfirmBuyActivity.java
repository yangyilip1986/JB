package com.jiubang.p2p.ui;

import java.math.BigDecimal;
import java.util.ArrayList;

import m.framework.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.bean.DetailProduct;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.CustomDialogUtil;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.ConvUtils;
import com.jiubang.p2p.utils.FormatUtils;
import com.jiubang.p2p.utils.MathUtils;
import com.jiubang.p2p.utils.ToastCommom;
import com.jiubang.p2p.utils.UIHelper;

@SuppressLint("NewApi")
public class ConfirmBuyActivity extends KJActivity {

	@BindView(id = R.id.et_price)
	private EditText et_price;
	@BindView(id = R.id.confirm_buy, click = true)
	private TextView confirm_buy;
	@BindView(id = R.id.tv_agreement, click = true)
	private TextView tv_agreement;
	@BindView(id = R.id.actual_price)
	private TextView actual_price;
	@BindView(id = R.id.cash_use, click = true)
	private TextView cash_use;       	  //现金券使用
	@BindView(id = R.id.add_interest_use,click = true)
	private TextView add_interest_use;    //加息券使用
	@BindView(id = R.id.experience_cash_use ,click = true)
	private TextView experience_cash_use; //体验金使用
	@BindView(id = R.id.drop_down_menu, click = true)
	private ImageView drop_down_menu;
	@BindView(id = R.id.ll, click = true)
	private LinearLayout ll;

	@BindView(id = R.id.tv_available)
	private TextView tv_available;				  // 可用余额

	@BindView(id = R.id.tv_remainingInvestmentAmount)
	private TextView tv_remainingInvestmentAmount;// 可投金额

	@BindView(id = R.id.tv_investmentPeriodDesc)
	private TextView tv_investmentPeriodDesc;	  // 投资期限(包含单位)

	@BindView(id = R.id.tv_annualizedGain)
	private TextView tv_annualizedGain;			  // 收益利率

	@BindView(id = R.id.tv_add)
	private TextView tv_add;					  // 收益利率加息部分

	@BindView(id = R.id.add_v)
	private LinearLayout add_v;					  // 收益利率加息部分的layout
	
	@BindView(id = R.id.tv_tenserinvest_price)
	private TextView tv_tenserinvest_price;		  // 预期总收益
	
	@BindView(id = R.id.tv_experience_tenserinvest)
	private TextView tv_experience_tenserinvest;  // 预计体验金总收益
	
	private ViewStub expericenceViewStub;
	
	private ViewStub normalViewStub;
	
	private ViewStub expericence_normal_ViewStub;

	private KJHttp http;
	private HttpParams params;

	private String type_flag = ""; 			// 加息券类型
	private String rate_coupon_send_id = "";// 加息券ID;
	private int id;							// 产品ID
	private int cashid;						// 现金券id
	private double add_interest_cash_price; // 加息卡券金额
	private double cash_price_cash;			// 现金券金额
	private double experience_limit_price;  // 体验金个人投资上限
	private double expAmountSurplus;        // 体验金可投金额
	private int sum = 0;
	private ArrayList<Integer> list_id;     // 存放体验金id的list
	private ArrayList<Integer> list_price;  // 存放体验金金额的list

	private double available; 				// 可用余额
	private double max;  					// 可投金额
	private int min;
	private int mul;
	private String title;
	private String investmentPeriodDesc; 	// 投资期限
	private String investmentPeriodDescunit;// 投资期限单位
	private String annualizedGain;
	private String add;

	private TitlePopup titlePopup;
	
	private int flag = 3;
	
	private DetailProduct product2;
	private String  experience_id ;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_confirm_buy);	

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		UIHelper.setTitleView(this, "", "确认购买");

		ActivityUtil.getActivityUtil().addActivity(this);

		Intent intent = getIntent();
		id = intent.getIntExtra("id", 0);
		title = intent.getStringExtra("title");			 // 产品标题
		flag = intent.getIntExtra("products_exp_type",0);// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)
		switch (flag) {
		case 0:
			normalViewStub = (ViewStub) findViewById(R.id.vs_normal_confirmbuy);
			normalViewStub.inflate();
			break;
		case 1:
			expericenceViewStub = (ViewStub) findViewById(R.id.vs_expericence_confirmbuy);
			expericenceViewStub.inflate();
			break;
		case 2:
			expericence_normal_ViewStub = (ViewStub) findViewById(R.id.vs_expericence_normal_confirmbuy);
			expericence_normal_ViewStub.inflate();
			break;
		default:
			break;
		}
	}

	@Override
	public void initWidget() {
		super.initWidget();

	}

	@Override
	protected void onResume() {
		super.onResume();
		http = new KJHttp();
		params = new HttpParams();
		getData(1);
	}
	
	private void getData(int page){
		params.put("sid", AppVariables.sid);
		params.put("id", id);
		params.put("page", page);
		http.post(AppConstants.DETAIL_PRODUCT + id, params, httpCallback2);
	}
	
	private HttpCallBack httpCallback2 = new HttpCallBack(ConfirmBuyActivity.this) {
		public void success(org.json.JSONObject ret){
			try {
				product2 = new DetailProduct(ret);
				mul = (Integer.valueOf(product2.getBaseLimitAmount())) / 100;
				min = Integer.parseInt(product2.getSinglePurchaseLowerLimit());
				max = Double.parseDouble(product2.getRemainingInvestmentAmount());
				experience_limit_price = Double.parseDouble(product2.getExpAmountBayLimit());//获取个人体验金购买上限
				expAmountSurplus = Double.parseDouble(product2.getExpAmountSurplus());//获取体验金剩余可投
				investmentPeriodDesc = product2.getInvestmentPeriodDesc();
				investmentPeriodDescunit = product2.getInvestmentPeriodDescunit();
				annualizedGain = product2.getAnnualizedGain();
				add = product2.getTenderAward();
				available = ret.getDouble(("available"));
				initView();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			initView();
		}	
	};
	
	private void initView(){
		tv_remainingInvestmentAmount.setText("可投金额：" + ConvUtils.convToMoney(product2.getRemainingInvestmentAmount())+ "元");
		tv_available.setText("可用余额：" + ConvUtils.convToMoney(available) + "元");
		tv_investmentPeriodDesc.setText("(投资期限：" + product2.getInvestmentPeriodDesc()+ product2.getInvestmentPeriodDescunit() + ")");
		tv_annualizedGain.setText(annualizedGain + "%");
		if (add == null || "".equals(add)) {
			add_v.setVisibility(View.GONE);
		} else {
			tv_add.setText(add);
		}

		et_price.setHint("起投" + Integer.parseInt(product2.getSinglePurchaseLowerLimit()) + "元");
		et_price.addTextChangedListener(textWatcher);
		
		String str = "投资视为同意" + product2.getAgreement();
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
		ForegroundColorSpan whiteSpan = new ForegroundColorSpan(Color.parseColor("#3385ff"));
		SpannableStringBuilder builder = new SpannableStringBuilder(str);
		builder.setSpan(redSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(whiteSpan, 6, str.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		tv_agreement.setText(builder);
	}

	TextWatcher textWatcher = new TextWatcher() {
		public void onTextChanged(CharSequence s, int start, int before,int count) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			cashid = 0;
			add_interest_cash_price = 0;
			cash_price_cash = 0;
			type_flag = "";
			rate_coupon_send_id = "";
			list_id = null;
			list_price = null;
			cash_use.setText("使用");
			add_interest_use.setText("使用");
			if(flag != 0){
				experience_cash_use.setText("使用");
				tv_experience_tenserinvest.setText("0.00");
			}
		}

		public void afterTextChanged(Editable s) {
			String sp = et_price.getText().toString();
			if (!Utils.isNullOrEmpty(sp)) {
				BigDecimal add1;
				if(add == null || "".equals(add)){
					add1 =ConvUtils.convToDecimal("0");
				}else {
					add1 = ConvUtils.convToDecimal(add);
				}
				BigDecimal date = null;
				if("月".equals(investmentPeriodDescunit)){
					date = ConvUtils.convToDecimal("1200");
				} else if("天".equals(investmentPeriodDescunit)){
					date = ConvUtils.convToDecimal("36500");
				}
				String str = MathUtils.divide(ConvUtils.convToDecimal(sp)
						.multiply(ConvUtils.convToDecimal(annualizedGain).add(add1))
						.multiply(ConvUtils.convToDecimal(investmentPeriodDesc)), date)
						.setScale(2, BigDecimal.ROUND_HALF_UP) + "";
				tv_tenserinvest_price.setText(str);
				actual_price.setText(ConvUtils.convToMoney(Double.parseDouble(sp) - cash_price_cash));
			} else {
				tv_tenserinvest_price.setText("0.00");
				actual_price.setText(ConvUtils.convToMoney(0 - cash_price_cash));
			}
		}
	};

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.ll:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;
		case R.id.confirm_buy:
			if(list_id != null){
				 experience_id = listToString(list_id, ',');
			  }else{
				 experience_id ="";
			  }
			 if(flag == 1){
				 ToastCommom toastCommom = ToastCommom.createToastConfig();
				if("使用".equals(experience_cash_use.getText().toString())){
					toastCommom.ToastShow(ConfirmBuyActivity.this,(ViewGroup) findViewById(R.id.toast_layout_root), "请先选择体验金");
				    break;
				}
			 }
			  if (checkPrice()) {
				final CustomDialogUtil dialog = new CustomDialogUtil(ConfirmBuyActivity.this,R.layout.dialog_confirmbuy_util_layout);
				dialog.setTitle("请确定本次投资");
				StringBuilder sb = new StringBuilder();
				
				String str = "";
				switch (flag) {// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)
				case 0:
					sb.append("<font color=\"#000000\">将用余额投资</font>");
					sb.append("<font color=\"#ff772d\">%s</font>");
					sb.append("<font color=\"#000000\">元</font><br>");
					str = String.format(sb.toString(), ConvUtils.convToMoney(et_price.getText().toString()));
					break;
				case 1:
					sb.append("<font color=\"#000000\">将用体验金投资</font>");
					sb.append("<font color=\"#ff772d\">%s</font>");
					sb.append("<font color=\"#000000\">元</font><br>");
					str = String.format(sb.toString(), ConvUtils.convToMoney(sum + ""));
					break;
				case 2:
					sb.append("<font color=\"#000000\">将用余额投资</font>");
					sb.append("<font color=\"#ff772d\">%s</font>");
					sb.append("<font color=\"#000000\">元</font><br>");
					sb.append("<font color=\"#000000\">将用体验金投资</font>");
					sb.append("<font color=\"#ff772d\">%s</font>");
					sb.append("<font color=\"#000000\">元</font><br>");
					str = String.format(sb.toString(), ConvUtils.convToMoney(et_price.getText().toString()), ConvUtils.convToMoney(sum + ""));
					break;
				}
				
				dialog.setMessage(Html.fromHtml(str));
				dialog.positiveClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						confirm_buy.setClickable(false);
						confirm_buy.setBackground(getResources().getDrawable(R.drawable.bg_gray));
						post();
					}
				});
				dialog.cancelClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			  }
			break;
		case R.id.cash_use:
			String amount1 = et_price.getText().toString();
//			if (amount1 == null || "".equals(amount1)) {
//				ToastCommom toastCommom = ToastCommom.createToastConfig();
//				toastCommom.ToastShow(ConfirmBuyActivity.this,(ViewGroup) findViewById(R.id.toast_layout_root),"请输入金额");
//			} else 
			if(checkPrice()){
				Intent intent = new Intent(ConfirmBuyActivity.this,UseRedActivity.class);
				intent.putExtra("user_type", "cash_use");
				intent.putExtra("amount", amount1);
				intent.putExtra("productid", id);
				startActivityForResult(intent, 1);
			}
			break;
		case R.id.add_interest_use:
			String amount2 = et_price.getText().toString();
			if(checkPrice()){
				Intent intent = new Intent(ConfirmBuyActivity.this,UseRedActivity.class);
				intent.putExtra("user_type", "add_interest_use");
				intent.putExtra("amount", amount2);
				intent.putExtra("productid", id);
				startActivityForResult(intent, 1);
			}
			break;
		case R.id.experience_cash_use:
			    String amount3 = et_price.getText().toString();
			    if(checkPrice()){
				Intent intent = new Intent(ConfirmBuyActivity.this,ExperienceUseRedActivity.class);
				intent.putExtra("user_type", "experience_cash_use");
				intent.putExtra("experience_limit_price", experience_limit_price);
				intent.putExtra("expAmountSurplus", expAmountSurplus);
				intent.putExtra("amount", amount3);
				intent.putExtra("productid", id);
				intent.putIntegerArrayListExtra("list_id", list_id);
				startActivityForResult(intent, 1);
			    }
			break;
		case R.id.tv_agreement:
			String amt = et_price.getText().toString();
			Intent protocol = new Intent(ConfirmBuyActivity.this,TenderProtocolActivity.class);
			protocol.putExtra("url", AppConstants.SERVICE_PROTOCOL + "?pid="+ id + "&uid=" + AppVariables.uid + "&amt=" + amt);
			startActivity(protocol);
			break;
		case R.id.drop_down_menu:
			titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			// 给标题栏弹窗添加子类
			titlePopup.addAction(new ActionItem(this, "首页",R.drawable.index_menu));
			titlePopup.addAction(new ActionItem(this, "投资",R.drawable.product_menu));
			titlePopup.addAction(new ActionItem(this, "更多",R.drawable.more_menu));
			titlePopup.addAction(new ActionItem(this, "我的",R.drawable.account_menu));
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

	/**
	 * 检验金额
	 * */
	private boolean checkPrice() {
		
		ToastCommom toastCommom = ToastCommom.createToastConfig();
        if(flag != 1){
		String amount = et_price.getText().toString();
		if (StringUtils.isEmpty(amount)) {
			toastCommom.ToastShow(ConfirmBuyActivity.this,(ViewGroup) findViewById(R.id.toast_layout_root), "请输入投资金额");
			return false;
		}
		final double price = Double.parseDouble(amount);
		if (price > max) {
			toastCommom.ToastShow(ConfirmBuyActivity.this,(ViewGroup) findViewById(R.id.toast_layout_root),"输入的金额超过可投金额，\n请重新输入！");
			return false;
		}else if (price < min) {
			toastCommom.ToastShow(ConfirmBuyActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "请大于最小投资金额");
			return false;
		}else if ((price % mul) > 0) {
			if (price != max) {
				toastCommom.ToastShow(ConfirmBuyActivity.this,(ViewGroup) findViewById(R.id.toast_layout_root), "请输入"+ mul + "的整数倍\n或最大可投金额");
				return false;
			}
		}else if (price > available) {
			final CustomDialogUtil dailog = new CustomDialogUtil(ConfirmBuyActivity.this,R.layout.dialog_confirmbuy_util_layout);
			dailog.setPositive("充值");
			dailog.setTitle("很抱歉，您的账户余额 (" + ConvUtils.convToMoney(available) + ") 不足");
			StringBuilder sb = new StringBuilder();
			sb.append("<font color=\"#000000\">将用银行卡充值</font>");
			sb.append("<font color=\"#ff772d\">%s</font>");
			sb.append("<font color=\"#000000\">元</font><br>");
			String str = String.format(sb.toString(),ConvUtils.convToMoney(Double.toString(Double.parseDouble(amount) - available)));
			dailog.setMessage(Html.fromHtml(str));
			dailog.positiveClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dailog.dismiss();
					charge();
				}
			});
			dailog.cancelClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dailog.dismiss();
				}
			});
			return false;
		 } 
        }
		return true;
	}

	private void post() {
		http = new KJHttp();
		params = new HttpParams();
		params.put("sid", AppVariables.sid);
		params.put("id", id);
		params.put("amount", et_price.getText().toString());
		params.put("cash", cashid);
		params.put("type_flag", type_flag);
		params.put("expAmountByLogId", experience_id);
		params.put("rate_coupon_send_id",rate_coupon_send_id);
		http.post(AppConstants.BUY + id + "/order/pay5", params, tenderCallback);
	}

	private HttpCallBack tenderCallback = new HttpCallBack(
			ConfirmBuyActivity.this) {
		
		@Override
		public void success(JSONObject ret) {
			try {
				String mode = ret.getString("mode");
				String trade_date = ret.getString("trade_date");
				if ("gateway".equals(mode)) {
					String call_back = ret.getString("call_back");
					if (call_back.startsWith("TransferCallback.html")) {
						Intent intent = new Intent(ConfirmBuyActivity.this,TenderInvestSuccessActivity.class);
						intent.putExtra("title", title);
						intent.putExtra("validityFlg",ret.getBoolean("validityFlg"));
						intent.putExtra("share_url", ret.getString("shareUrl"));
						intent.putExtra("amount",ConvUtils.convToMoney(et_price.getText().toString()) + "元");
						intent.putExtra("experience_amount",ConvUtils.convToMoney(sum + "") + "元");
						intent.putExtra("trade_date", trade_date);//投资日期
						intent.putExtra("product_type", flag);
						if(flag ==0 || flag == 2){
							// 预期总收益
							intent.putExtra("tv_tenserinvest_price", tv_tenserinvest_price.getText().toString());
						}
						if(flag == 1 || flag == 2){
							// 预期体验金总收益
							intent.putExtra("tv_experience_tenserinvest", tv_experience_tenserinvest.getText().toString());
						}
						startActivity(intent);
						finish();
					} else {
						Intent intent = new Intent(ConfirmBuyActivity.this,TenderInvestFailActivity.class);
						startActivity(intent);
						finish();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		public void failure(JSONObject ret) {
			ToastCommom toastCommom = ToastCommom.createToastConfig();
			try {
				String msg = ret.getString("msg");
				toastCommom.ToastShow(ConfirmBuyActivity.this,(ViewGroup) findViewById(R.id.toast_layout_root),msg);
				confirm_buy.setClickable(true);
				confirm_buy.setBackground(getResources().getDrawable(R.drawable.bg_blue));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		};
		
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == 1) {
			if (data != null) {
				if(data.getIntExtra("cash", 0)!=0 ){
				     cashid = data.getIntExtra("cash", 0);
				     type_flag = "";
				}
				if(data.getStringExtra("rate_coupon_send_id")!=null && !"".equals(data.getStringExtra("rate_coupon_send_id"))){
				     rate_coupon_send_id = data.getStringExtra("rate_coupon_send_id");
				     type_flag = data.getStringExtra("type_flag");
				}
			} else {
				cashid = 0;
				cash_price_cash = 0;
				add_interest_cash_price = 0;
				type_flag = "";
				rate_coupon_send_id = "";
				cash_use.setText("使用");
				add_interest_use.setText("使用");
				String sp = et_price.getText().toString();
				if (!Utils.isNullOrEmpty(sp)) {
					actual_price.setText(ConvUtils.convToMoney(sp));
				} else {
					actual_price.setText(ConvUtils.convToMoney(0));
				}
			}
			if ("".equals(type_flag)) {
				cash_price_cash = data.getDoubleExtra("price", 0);
				cash_use.setText(FormatUtils.numFormat(cash_price_cash)+ "元现金券");
				String sp = et_price.getText().toString();
				if (!Utils.isNullOrEmpty(sp)) {
					actual_price.setText(ConvUtils.convToMoney(Double.parseDouble(sp) - cash_price_cash));
				} else {
					actual_price.setText(ConvUtils.convToMoney(0 - cash_price_cash));
				}
			} else if ("1".equals(type_flag)) {
				add_interest_cash_price = data.getDoubleExtra("price", 0);
				add_interest_use.setText(FormatUtils.numFormat(add_interest_cash_price) + "%加息券");
				BigDecimal cash;
				cash = ConvUtils.convToDecimal(add_interest_cash_price);
				String sp = et_price.getText().toString();
				String str = calculate("1", cash, sp, 0);
				tv_tenserinvest_price.setText(str);
				actual_price.setText(ConvUtils.convToMoney(Double.parseDouble(sp) - cash_price_cash));
				if(flag!=0){
					String str2 = calculate("3", cash, sp, sum);
					tv_experience_tenserinvest.setText(str2);
				}
			}
		} else if (requestCode == 1 && resultCode == 2) {
			actual_price.setBackgroundResource(R.drawable.bg_blue);
			actual_price.setClickable(true);
			cashid = 0;
			cash_price_cash = 0;
			add_interest_cash_price = 0;
			type_flag = "";
			rate_coupon_send_id = "";
			cash_use.setText("使用");
			String sp = et_price.getText().toString();
			if (!Utils.isNullOrEmpty(sp)) {
				actual_price.setText(ConvUtils.convToMoney(sp));
			} else {
				actual_price.setText(ConvUtils.convToMoney(0));
			}
		} else if (requestCode == 1 && resultCode == 3) {
			if (data != null) {
				list_id = data.getIntegerArrayListExtra("list_id");
				list_price = data.getIntegerArrayListExtra("list_price");
				type_flag = "";
			} else {
				list_id = null;
				list_price = null;
				type_flag = "";
				experience_cash_use.setText("使用");
				String sp = et_price.getText().toString();
				if (!Utils.isNullOrEmpty(sp)) {
					actual_price.setText(ConvUtils.convToMoney(sp));
				} else {
					actual_price.setText(ConvUtils.convToMoney(0));
				}
			}
			sum = 0;
			for (int i : list_price) {
				sum += i;
			}
			experience_cash_use.setText(sum + "元体验金");
			String sp = et_price.getText().toString();
			BigDecimal cash;
			cash = ConvUtils.convToDecimal(add_interest_cash_price);
			String str = calculate("3", cash, sp, sum);
			tv_experience_tenserinvest.setText(str);
		} 
	}

	/**
	 * 收益计算方法
	 * @param type_flag
	 * @param cash
	 * @param sp
	 * @param experience_price
	 * @return
	 */
	private String calculate(String type_flag,BigDecimal cash,String sp,int experience_price) {
			BigDecimal add1;
			if (add == null || "".equals(add)) {
				add1 = ConvUtils.convToDecimal("0");
			} else {
				add1 = ConvUtils.convToDecimal(add);
			}
			BigDecimal date = null;
			if ("月".equals(investmentPeriodDescunit)) {
				date = ConvUtils.convToDecimal("1200");
			} else if ("天".equals(investmentPeriodDescunit)) {
				date = ConvUtils.convToDecimal("36500");
			}
			if("1".equals(type_flag)){
			    String str = MathUtils.divide(ConvUtils.convToDecimal(sp)
							.multiply(ConvUtils.convToDecimal(annualizedGain).add(add1).add(cash))
							.multiply(ConvUtils.convToDecimal(investmentPeriodDesc)),date)
							.setScale(2, BigDecimal.ROUND_HALF_UP)+ "";
			    return str;
			}else{
				String str = MathUtils.divide(ConvUtils.convToDecimal(experience_price)
						.multiply(ConvUtils.convToDecimal(annualizedGain).add(add1).add(cash))
						.multiply(ConvUtils.convToDecimal(investmentPeriodDesc)), date)
						.setScale(2, BigDecimal.ROUND_HALF_UP) + "";
				return str;
			}
	}
	
	/**
	 * ArrayList转换为String（带分隔符）
	 * @param list ArrayList
	 * @param separator 分隔符
	 * @return
	 */
	public static String listToString(ArrayList<Integer> list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() - 1) {
				sb.append(list.get(i));
			} else {
				sb.append(list.get(i));
				sb.append(separator);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 充值
	 * */
	public void charge() {
		http = new KJHttp();
		params = new HttpParams();
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.BASICINFO, params, new HttpCallBack(ConfirmBuyActivity.this) {
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					JSONObject account = ret.getJSONObject("account");
					if (account.getInt("accountStatus") == 2) {
						// 充值
						Intent charge = new Intent(ConfirmBuyActivity.this, ChargeCashBFActivity.class);
						charge.putExtra("type", "charge");
						charge.putExtra("cardStatus", account.getInt("cardStatus"));// 绑卡标识 2：有效
						charge.putExtra("bf_bank_id", account.getString("bf_bank_id"));
						charge.putExtra("bankAccount", account.getString("bankAccount"));
						startActivity(charge);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
