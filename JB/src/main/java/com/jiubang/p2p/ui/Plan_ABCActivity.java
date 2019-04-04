package com.jiubang.p2p.ui;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.adapter.RecordAdapter;
import com.jiubang.p2p.bean.DetailPlanBProduct;
import com.jiubang.p2p.bean.DetailPlanBRecordList;
import com.jiubang.p2p.bean.DetailRecord;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.CustomDialogUtil;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.ConvUtils;
import com.jiubang.p2p.utils.UIHelper;
import com.jiubang.p2p.view.ListViewForScrollView;
import com.jiubang.p2p.widget.CircleProgressBar;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;

/**
 * 理财计划ABC
 * */
public class Plan_ABCActivity extends KJActivity {

	// 产品详情
	@BindView(id = R.id.totalInvestment)
	private TextView totalInvestment;// 计划金额
	@BindView(id = R.id.investmentPeriodDesc)
	private TextView investmentPeriodDesc;// 项目期限
	@BindView(id = R.id.investmentPeriodDescunit)
	private TextView investmentPeriodDescunit;// 项目期限单位
	@BindView(id = R.id.annualizedGain)
	private TextView annualizedGain;// 年化收益率
	@BindView(id = R.id.guaranteeModeName)
	private TextView guaranteeModeName;// 第三方担保机构
	@BindView(id = R.id.remainingInvestmentAmount)
	private TextView remainingInvestmentAmount;// 可投数字
	@BindView(id = R.id.singlePurchaseLowerLimit)
	private TextView singlePurchaseLowerLimit;// 起投数字
	@BindView(id = R.id.percentagepb)
	private CircleProgressBar percentagepb;// 圆环
	@BindView(id = R.id.tv_join_date_content)
	private TextView tv_join_date_content;// 加入时间
	@BindView(id = R.id.tv_join_condition)
	private TextView tv_join_condition;// 加入条件
	@BindView(id = R.id.tender_cash, click = true)
	private TextView tender_cash;// 充值
	@BindView(id = R.id.et_price)
	private EditText et_price;// 输入的金额
	@BindView(id = R.id.tv_buy, click = true)
	private TextView tv_buy;
	@BindView(id = R.id.available)
	private TextView mAvaliable;// 账户余额
	@BindView(id = R.id.tenserinvest_price)
	private TextView tenserinvest_price;// 投资...钱后
	@BindView(id = R.id.listview2)
	private ListViewForScrollView listview2;// 投标记录
	@BindView(id = R.id.ll_project_profile)
	private LinearLayout ll_project_profile;// 项目简介
	@BindView(id = R.id.tv_payment_methods)
	private TextView tv_payment_methods;// 回款方式
	@BindView(id = R.id.tv_payment_methods_content)
	private TextView tv_payment_methods_content;// 回款方式内容
	@BindView(id = R.id.tv_scope_of_investment_content)
	private TextView tv_scope_of_investment_content;// 投资范围
//	@BindView(id = R.id.record,click = true)
//	private LinearLayout mRecord;// 投标记录
	@BindView(id = R.id.description)
	private TextView description;// 项目简介
	@BindView(id = R.id.tv_profit, click = true)
	private TextView tv_profit;// 收益计算
	@BindView(id = R.id.empty)
	private TextView empty;
	@BindView(id = R.id.tv_stauts)
	private TextView tv_stauts;
	@BindView(id = R.id.drop_down_menu, click = true)
	private ImageView drop_down_menu;
//	@BindView(id = R.id.ll_planb_product,click = true)
//	private LinearLayout ll_planb_product;
//	@BindView(id = R.id.ll,click = true)
//	private LinearLayout ll;
	
	private TitlePopup titlePopup;
	
	private RecordAdapter adapter;
	private List<DetailRecord> record;
	
	private String type;

	private KJHttp http;
	private HttpParams params;
	private DetailPlanBProduct plan_b;
	private String id;
	private String agreement;// 我同意协议内容
	private String agreement_url;// 协议链接

	private boolean next = false;// 下一个页面可进行标识

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_plan_b);
		// 透明状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// EditText上弹
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		UIHelper.setTitleView(this, "", "理财计划" + type);
		
		ActivityUtil.getActivityUtil().addActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		next = false;
		http = new KJHttp();
		params = new HttpParams();
		getData(1);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void initData() {
		super.initData();
	}

	private void getData(int page) {
		params.put("sid", AppVariables.sid);
		params.put("type", type);
		params.put("amount", "10000");
		http.post(AppConstants.PLAN_B_DETAIL_PRODUCT, params, httpCallback);// 产品详情
	}

	private HttpCallBack httpCallback = new HttpCallBack(Plan_ABCActivity.this) {
		public void success(org.json.JSONObject ret) {
			try {
				plan_b = new DetailPlanBProduct(ret);
				UIHelper.setTitleView(Plan_ABCActivity.this, "", plan_b.getFinancial_plan_title());
				id = ret.getString("id");
				record = new DetailPlanBRecordList(ret.getJSONArray("records")).getList();
				if (record.size() != 0) {
					empty.setVisibility(View.GONE);
				}
				initView();
				adapter = new RecordAdapter(Plan_ABCActivity.this, record);
				listview2.setAdapter(adapter);
				
//				ViewGroup.LayoutParams params = listview2.getLayoutParams();
////				params.height = 200 + (listview2.getDividerHeight() * (adapter.getCount() - 1));
//				params.height = 200;
//				listview2.setLayoutParams(params);
				
				
				listview2.setOnItemClickListener(listener);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(Plan_ABCActivity.this, R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	public OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			InputMethodManager imm;
			imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	};
	
	private void initView() {
		// 投资总额
		totalInvestment.setText(plan_b.getAmount_show());
		// 项目期限
		investmentPeriodDesc.setText(plan_b.getInvestBPeriod());
		// 项目期限单位
		investmentPeriodDescunit.setText(plan_b.getInvestBPeriodUnit());
		// 预计年化收益
		annualizedGain.setText(plan_b.getInterest_rate());
		// 加入时间
		tv_join_date_content.setText("加入时间: " + plan_b.getAdd_time());
		// 加入条件
		tv_join_condition.setText("加入条件: " + plan_b.getAddConditions());
		// 剩余金额
		mAvaliable.setText(ConvUtils.convToMoney(plan_b.getUsable_amount()) + "元");
		// 剩余可投
		remainingInvestmentAmount.setText(plan_b.getSurplus_amount_show());
		// 起投金额
		singlePurchaseLowerLimit.setText(plan_b.getMinAddAmount_show());
		// 项目简介
		ll_project_profile.setVisibility(View.VISIBLE);
		// 项目简介文字
		description.setText(plan_b.getBrief());
		// 进度条
		percentagepb.setProgress(plan_b.getProgress());
		
		if (plan_b.getProgress() != 100 && "1".equals(plan_b.getFinancial_plan_status())) {
			tv_stauts.setTextColor(getResources().getColor(R.color.app_blue));
			tv_buy.setText("立即投资");
			tv_stauts.setText("正在售卖");
		}
		if (plan_b.getProgress() == 100 || "3".equals(plan_b.getFinancial_plan_status())) {
			tv_buy.setBackgroundColor(getResources().getColor(R.color.app_font_light));
			tv_buy.setText("已售罄");
			tv_buy.setClickable(false);
			tv_stauts.setText("已售罄");
		}
		if ("5".equals(plan_b.getFinancial_plan_status())) {
			tv_buy.setBackgroundColor(getResources().getColor(R.color.app_font_light));
			tv_buy.setText("投资结束");
			tv_buy.setClickable(false);
			tv_stauts.setText("投资结束");
		}
		
		// 回款方式
		tv_payment_methods_content.setText(plan_b.getRecover_type());
		// 投资范围
		tv_scope_of_investment_content.setText(plan_b.getVestment_universe());
		// 协议
		agreement = plan_b.getProtocol();
		// 协议链接
		agreement_url = plan_b.getProtocol_url();

		next = true;
		
		params.put("interest", annualizedGain.getText().toString());
		params.put("amount", et_price.getText().toString());
		params.put("invest_day", plan_b.getInvestBPeriod());
		http.post(AppConstants.PROFIT_CALCULATOR, params, profitcallback);
	}

	@Override
	public void widgetClick(View v) {
		InputMethodManager imm;
		super.widgetClick(v);
		if (next) {
			switch (v.getId()) {
//			case R.id.ll:
//				imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
//		        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//				break;
//			case R.id.record:
//				imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
//		        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//				break;
//			case R.id.ll_planb_product:
//				imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
//		        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//				break;
			case R.id.tender_cash:
				if(!AppVariables.isSignin) {
					startActivity(new Intent(Plan_ABCActivity.this, SigninActivity.class));
				} else {
					charge();
				}
				break;
			case R.id.tv_buy:
				if (!AppVariables.isSignin) {
					startActivity(new Intent(Plan_ABCActivity.this, SigninActivity.class));
				} else {
					buy();
				}
				break;
			case R.id.tv_profit:
				imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
		        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				params.put("interest", annualizedGain.getText().toString());
				params.put("amount", et_price.getText().toString());
				params.put("invest_day", plan_b.getInvestBPeriod());
				http.post(AppConstants.PROFIT_CALCULATOR, params, profitcallback);
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
	}
	
	/**
	 * 购买
	 * */
	private void buy() {
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.BASICINFO, params, new HttpCallBack(Plan_ABCActivity.this) {
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					JSONObject account = ret.getJSONObject("account");
					if (account.getInt("accountStatus") == 2) {
						Intent intent = new Intent(Plan_ABCActivity.this, PlanABCConfirmBuyActivity.class);
						intent.putExtra("available", plan_b.getUsable_amount());// 可用余额
						intent.putExtra("agreement", agreement);
						intent.putExtra("agreement_url", agreement_url);
						intent.putExtra("id", id);
						intent.putExtra("min", plan_b.getMinAddAmount());//起投金额
						intent.putExtra("title", plan_b.getFinancial_plan_title());
						intent.putExtra("remainingInvestmentAmount", plan_b.getSurplus_amount());//可投金额
						intent.putExtra("remainingInvestmentAmount_show", plan_b.getSurplus_amount_show());//可投金额
						intent.putExtra("investmentPeriodDesc", investmentPeriodDesc.getText().toString());//投资期限
	                    intent.putExtra("investmentPeriodDescunit", investmentPeriodDescunit.getText().toString());//投资期限单位
	                    intent.putExtra("annualizedGain", annualizedGain.getText().toString());//收益利率
						startActivity(intent);
						
					} else {
						final CustomDialogUtil dialog = new CustomDialogUtil(Plan_ABCActivity.this);
						dialog.setTitle("温馨提示");
						dialog.setMessage("请先实名认证");
						dialog.setPositive("前往");
						dialog.positiveClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								startActivity(new Intent(Plan_ABCActivity.this, AccountActivity.class));
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
	 * 收益计算
	 * */
	private HttpCallBack profitcallback = new HttpCallBack(
			Plan_ABCActivity.this) {

		@Override
		public void success(JSONObject ret) {
			try {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color=\"#333333\">投资%s元，到期后可得</font>");
				sb.append("<font color=\"#ff772d\"><big>%s</big></font>");
				sb.append("<font color=\"#333333\">元</font><br>");
				sb.append("<font color=\"#999999\"><small>具体收益以实际到账为准</small></font>");
				String str = String.format(sb.toString(), ret.getString("amount"), ret.getString("amountTotalA"));
				tenserinvest_price.setText(Html.fromHtml(str));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.success(ret);
		}

	};
	
	/**
	 * 充值
	 * */
	private void charge() {
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.BASICINFO, params, new HttpCallBack(
				Plan_ABCActivity.this) {
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					JSONObject account = ret.getJSONObject("account");
					if (account.getInt("accountStatus") == 2) {
						// 充值
						Intent charge = new Intent(Plan_ABCActivity.this,
								ChargeCashBFActivity.class);
						charge.putExtra("type", "charge");
						charge.putExtra("cardStatus",
								account.getInt("cardStatus"));// 绑卡标识 2：有效 0无效
						charge.putExtra("bf_bank_id",
								account.getString("bf_bank_id"));
						charge.putExtra("bankAccount",
								account.getString("bankAccount"));
						startActivity(charge);
					} else {
						final CustomDialogUtil dialog = new CustomDialogUtil(Plan_ABCActivity.this);
						dialog.setTitle("温馨提示");
						dialog.setMessage("请先实名认证");
						dialog.positiveClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								startActivity(new Intent(Plan_ABCActivity.this, AccountActivity.class));
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
}
