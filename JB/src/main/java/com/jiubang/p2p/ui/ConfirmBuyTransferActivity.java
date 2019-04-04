package com.jiubang.p2p.ui;

import java.math.BigDecimal;

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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.CustomDialogUtil;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.ConvUtils;
import com.jiubang.p2p.utils.MathUtils;
import com.jiubang.p2p.utils.StringUtils;
import com.jiubang.p2p.utils.ToastCommom;
import com.jiubang.p2p.utils.UIHelper;

@SuppressLint("NewApi")
public class ConfirmBuyTransferActivity extends KJActivity {

	@BindView(id = R.id.et_price)
	private EditText et_price;
	@BindView(id = R.id.confirm_buy, click = true)
	private TextView confirm_buy;
	@BindView(id = R.id.tv_agreement, click = true)
	private TextView tv_agreement;
	@BindView(id = R.id.drop_down_menu, click = true)
	private ImageView drop_down_menu;
	@BindView(id = R.id.ll, click = true)
	private LinearLayout ll;
	@BindView(id = R.id.actual_price)
	private TextView actual_price;

	@BindView(id = R.id.tv_available)
	private TextView tv_available;// 可用余额

	@BindView(id = R.id.tv_remainingInvestmentAmount)
	private TextView tv_remainingInvestmentAmount;// 可投金额

	@BindView(id = R.id.tv_investmentPeriodDesc)
	private TextView tv_investmentPeriodDesc;// 投资期限(包含单位)

	@BindView(id = R.id.tv_annualizedGain)
	private TextView tv_annualizedGain;// 收益利率

	@BindView(id = R.id.tv_tenserinvest_price)
	private TextView tv_tenserinvest_price;// 预期总收益

	private KJHttp http;
	private HttpParams params;

	private String agreement_url;
	private String agreement;
	private int oid_transfer_id;
	private double available;
	private int mul;
	private int min;
	private double max;
	private String title;
	private String investmentPeriodDesc;// 投资期限
	private String investmentPeriodDescunit;// 投资期限单位
	private String remainingInvestmentAmount;// 可投金额
	private String annualizedGain;

	private TitlePopup titlePopup;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_confirm_buy_transfer);

		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		UIHelper.setTitleView(this, "", "确认购买");

		ActivityUtil.getActivityUtil().addActivity(this);

		Intent intent = getIntent();
		agreement = intent.getStringExtra("agreement");// 获取协议
		agreement_url = intent.getStringExtra("agreement_url");// 获取协议
		oid_transfer_id = intent.getIntExtra("oid_transfer_id", 0);// 债权标ID

		available = intent.getDoubleExtra("available", 0);// 可用余额
		remainingInvestmentAmount = intent
				.getStringExtra("remainingInvestmentAmount2");// 最大可投金额
		max = intent.getDoubleExtra("max", 0);// 最大可投金额
		min = intent.getIntExtra("min", 0);// 最小投资金额
		mul = intent.getIntExtra("mul", 0);// 递增金额
		investmentPeriodDesc = intent.getStringExtra("investmentPeriodDesc");// 获取投资期限
		investmentPeriodDescunit = intent
				.getStringExtra("investmentPeriodDescunit");// 获取投资期限单位
		annualizedGain = intent.getStringExtra("annualizedGain");// 收益利率
		title = intent.getStringExtra("title");// 产品标题

	}

	@Override
	public void initWidget() {
		super.initWidget();

		// 剩余金额
		BigDecimal amount = new BigDecimal(max);
		// 最小倍数
		BigDecimal minb = new BigDecimal(Math.ceil((double) min / (double) mul));
		// 判断是不是最后一笔投资 (剩余金额 - 最小投资金额（并且是最小递增金额的整数倍） >= 最小投资金额（并且是最小递增金额的整数倍）)
		if (amount.subtract(minb.multiply(new BigDecimal(mul))).compareTo(
				minb.multiply(new BigDecimal(mul))) < 0) {
			et_price.setText(max + "");
			et_price.setEnabled(false);
			CalculatorMoney();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		http = new KJHttp();

		String str = "投资视为同意" + agreement;
		SpannableStringBuilder builder = new SpannableStringBuilder(str);

		tv_remainingInvestmentAmount.setText("可投金额："
				+ remainingInvestmentAmount + "元");
		tv_available.setText("可用余额：" + ConvUtils.convToMoney(available) + "元");
		tv_investmentPeriodDesc.setText("(投资期限：" + investmentPeriodDesc
				+ investmentPeriodDescunit + ")");
		tv_annualizedGain.setText(annualizedGain + "%");

		ForegroundColorSpan redSpan = new ForegroundColorSpan(
				Color.parseColor("#333333"));
		ForegroundColorSpan whiteSpan = new ForegroundColorSpan(
				Color.parseColor("#3385ff"));

		builder.setSpan(redSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(whiteSpan, 6, str.length(),
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		tv_agreement.setText(builder);

		et_price.setHint("起投" + min + "元");
		et_price.addTextChangedListener(textWatcher);

		http = new KJHttp();
		params = new HttpParams();

		getData(1);
	}

	TextWatcher textWatcher = new TextWatcher() {
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void afterTextChanged(Editable s) {
			String sp = et_price.getText().toString();
			if (!Utils.isNullOrEmpty(sp)) {
				BigDecimal date = null;
				if ("月".equals(investmentPeriodDescunit)) {
					date = ConvUtils.convToDecimal("1200");
				} else if ("天".equals(investmentPeriodDescunit)) {
					date = ConvUtils.convToDecimal("36500");
				}
				String str = MathUtils
						.divide(ConvUtils
								.convToDecimal(sp)
								.multiply(
										ConvUtils.convToDecimal(annualizedGain))
								.multiply(
										ConvUtils
												.convToDecimal(investmentPeriodDesc)),
								date).setScale(2, BigDecimal.ROUND_HALF_UP)
						+ "";
				tv_tenserinvest_price.setText(str);
				actual_price.setText(ConvUtils.convToMoney(Double
						.parseDouble(sp)));
			} else {
				tv_tenserinvest_price.setText("0.00");
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
			if (checkPrice()) {
				final CustomDialogUtil dialog = new CustomDialogUtil(
						ConfirmBuyTransferActivity.this,
						R.layout.dialog_confirmbuy_util_layout);
				dialog.setTitle("请确定本次投资");
				StringBuilder sb = new StringBuilder();
				sb.append("<font color=\"#000000\">将用余额投资</font>");
				sb.append("<font color=\"#ff772d\">%s</font>");
				sb.append("<font color=\"#000000\">元</font><br>");
				String str = String.format(sb.toString(),
						ConvUtils.convToMoney(et_price.getText().toString()));
				dialog.setMessage(Html.fromHtml(str));
				dialog.positiveClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						confirm_buy.setClickable(false);
						confirm_buy.setBackground(getResources().getDrawable(
								R.drawable.bg_gray));
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
		case R.id.tv_agreement:
			String amt = et_price.getText().toString();
			Intent protocol = new Intent(ConfirmBuyTransferActivity.this,
					TenderProtocolActivity.class);
			protocol.putExtra("url", agreement_url + "&amt=" + amt + "&phone="
					+ "android");
			startActivity(protocol);
			break;
		case R.id.drop_down_menu:
			titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			// 给标题栏弹窗添加子类
			titlePopup.addAction(new ActionItem(this, "首页",
					R.drawable.index_menu));
			titlePopup.addAction(new ActionItem(this, "投资",
					R.drawable.product_menu));
			titlePopup.addAction(new ActionItem(this, "更多",
					R.drawable.more_menu));
			titlePopup.addAction(new ActionItem(this, "我的",
					R.drawable.account_menu));
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
		String amount = et_price.getText().toString();
		if (StringUtils.isEmpty(amount)) {
			toastCommom
					.ToastShow(ConfirmBuyTransferActivity.this,
							(ViewGroup) findViewById(R.id.toast_layout_root),
							"请输入投资金额");
			return false;
		}
		final double price = Double.parseDouble(amount);
		if (price > max) {
			toastCommom.ToastShow(ConfirmBuyTransferActivity.this,
					(ViewGroup) findViewById(R.id.toast_layout_root),
					"输入的金额超过可投金额，\n请重新输入！");
			return false;
		} else if (price < min) {
			toastCommom.ToastShow(ConfirmBuyTransferActivity.this,
					(ViewGroup) findViewById(R.id.toast_layout_root),
					"请大于最小投资金额");
			return false;
		} else if ((price % mul) > 0) {
			if (price != max) {
				toastCommom.ToastShow(ConfirmBuyTransferActivity.this,
						(ViewGroup) findViewById(R.id.toast_layout_root), "请输入"
								+ mul + "的整数倍\n或最大可投金额");
				return false;
			}
		} else if (price > available) {
			final CustomDialogUtil dailog = new CustomDialogUtil(
					ConfirmBuyTransferActivity.this,
					R.layout.dialog_confirmbuy_util_layout);
			dailog.setPositive("充值");
			dailog.setTitle("很抱歉，您的账户余额 (" + ConvUtils.convToMoney(available)
					+ ") 不足");
			StringBuilder sb = new StringBuilder();
			sb.append("<font color=\"#000000\">将用银行卡充值</font>");
			sb.append("<font color=\"#ff772d\">%s</font>");
			sb.append("<font color=\"#000000\">元</font><br>");
			String str = String.format(
					sb.toString(),
					ConvUtils.convToMoney(Double.toString(Double
							.parseDouble(amount) - available)));
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
		return true;
	}

	/**
	 * 计算预期总收益和实际支付金额
	 */
	private void CalculatorMoney() {
		String sp = et_price.getText().toString();
		if (!Utils.isNullOrEmpty(sp)) {
			BigDecimal date = null;
			if ("月".equals(investmentPeriodDescunit)) {
				date = ConvUtils.convToDecimal("1200");
			} else if ("天".equals(investmentPeriodDescunit)) {
				date = ConvUtils.convToDecimal("36500");
			}
			String str = MathUtils
					.divide(ConvUtils
							.convToDecimal(sp)
							.multiply(ConvUtils.convToDecimal(annualizedGain))
							.multiply(
									ConvUtils
											.convToDecimal(investmentPeriodDesc)),
							date).setScale(2, BigDecimal.ROUND_HALF_UP)
					+ "";
			tv_tenserinvest_price.setText(str);
			actual_price.setText(ConvUtils.convToMoney(Double.parseDouble(sp)));
		}
	}

	private void post() {
		http = new KJHttp();
		params = new HttpParams();

		params.put("sid", AppVariables.sid);
		params.put("id", oid_transfer_id);
		params.put("amount", et_price.getText().toString());
		http.post(AppConstants.BUY + "transfer/" + oid_transfer_id
				+ "/order/pay3", params, tenderCallback);
	}

	private HttpCallBack tenderCallback = new HttpCallBack(
			ConfirmBuyTransferActivity.this) {
		@Override
		public void success(JSONObject ret) {
			try {
				String mode = ret.getString("mode");
				String trade_date = ret.getString("trade_date");
				if ("gateway".equals(mode)) {
					String call_back = ret.getString("call_back");
					if (call_back.startsWith("TenderTransferCallback.html")) {
						Intent intent = new Intent(
								ConfirmBuyTransferActivity.this,
								TenderInvestSuccessActivity.class);
						intent.putExtra("title", title);
						intent.putExtra(
								"amount",
								ConvUtils.convToMoney(et_price.getText()
										.toString()) + "元");
						intent.putExtra("trade_date", trade_date);// 投资日期
						intent.putExtra("tv_tenserinvest_price",
								tv_tenserinvest_price.getText().toString());// 预期总收益
						startActivity(intent);
						finish();
					} else {
						Intent intent = new Intent(
								ConfirmBuyTransferActivity.this,
								TenderInvestFailActivity.class);
						startActivity(intent);
						finish();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void failNext(JSONObject ret) {

		}
	};

	private void getData(int page) {
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.GAIN, params, httpCallback);
	}

	private HttpCallBack httpCallback = new HttpCallBack(
			ConfirmBuyTransferActivity.this) {
		public void success(org.json.JSONObject ret) {
			try {

				available = ret.getDouble("available");
				tv_available.setText("可用余额：" + ConvUtils.convToMoney(available)
						+ "元");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 充值
	 * */
	public void charge() {
		http = new KJHttp();
		params = new HttpParams();
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.BASICINFO, params, new HttpCallBack(
				ConfirmBuyTransferActivity.this) {
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					JSONObject account = ret.getJSONObject("account");
					if (account.getInt("accountStatus") == 2) {
						// 充值
						Intent charge = new Intent(
								ConfirmBuyTransferActivity.this,
								ChargeCashBFActivity.class);
						charge.putExtra("type", "charge");
						charge.putExtra("cardStatus",
								account.getInt("cardStatus"));// 绑卡标识 2：有效
						charge.putExtra("bf_bank_id",
								account.getString("bf_bank_id"));
						charge.putExtra("bankAccount",
								account.getString("bankAccount"));
						startActivity(charge);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
