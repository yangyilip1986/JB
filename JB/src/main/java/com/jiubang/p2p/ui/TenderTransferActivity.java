package com.jiubang.p2p.ui;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.jiubang.p2p.adapter.CommonAdapter;
import com.jiubang.p2p.adapter.ViewHolder;
import com.jiubang.p2p.bean.DetailProduct;
import com.jiubang.p2p.bean.DetailRecord;
import com.jiubang.p2p.bean.DetailRecordList;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.CustomDialogUtil;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.ConvUtils;
import com.jiubang.p2p.utils.ToastCommom;
import com.jiubang.p2p.utils.UIHelper;
import com.jiubang.p2p.widget.CircleProgressBar;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.widget.KJListView;
import com.louding.frame.widget.KJRefreshListener;

/**
 * 债权转让产品投标
 * */
@SuppressWarnings("deprecation")
public class TenderTransferActivity extends KJActivity {

	// 产品详情
	@BindView(id = R.id.totalInvestment)
	private TextView totalInvestment;
	@BindView(id = R.id.investmentPeriodDesc)
	private TextView investmentPeriodDesc; //项目期限的数字
	@BindView(id = R.id.investmentPeriodDescunit)
	private TextView investmentPeriodDescunit; //项目期限的数字的单位
	@BindView(id = R.id.annualizedGain)
	private TextView annualizedGain; //预计年化收益的数字
	@BindView(id = R.id.guaranteeModeName)
	private TextView guaranteeModeName;
	@BindView(id = R.id.repaymentMethodName)
	private TextView repaymentMethodName;
	@BindView(id = R.id.interestBeginDate)
	private TextView interestBeginDate;
	@BindView(id = R.id.remainingInvestmentAmount)
	private TextView remainingInvestmentAmount;// 可投金额
	@BindView(id = R.id.singlePurchaseLowerLimit)
	private TextView singlePurchaseLowerLimit;//起投金额
	@BindView(id = R.id.percentagetxt)
	private TextView percentagetxt;
	@BindView(id = R.id.percentage)
	private TextView percentage;
	@BindView(id = R.id.percentagepb)
	private CircleProgressBar percentagepb;

	@BindView(id = R.id.tender_cash, click = true)
	private TextView tender_cash;
	@BindView(id = R.id.et_price)
	private EditText et_price;
	@BindView(id = R.id.cash, click = true)
	private TextView cash;
	@BindView(id = R.id.tv_buy, click = true)
	private TextView tv_buy;
	@BindView(id = R.id.available)
	private TextView mAvaliable;
	@BindView(id = R.id.tv_transfer_froze_time)
	private TextView tv_transfer_froze_time;
	@BindView(id = R.id.tv_profit,click = true)
	private TextView mtv_profit;
	@BindView(id = R.id.tenserinvest_price)
	private TextView tenserinvest_price;
	@BindView(id = R.id.tv_stauts)
	private TextView tv_stauts;
	
	// 转让人信息
	@BindView(id = R.id.ll_transfer_person)
	private LinearLayout ll_transfer_person;
	@BindView(id = R.id.tv_person_transfer_info)
	private TextView tv_person_transfer_info;
	@BindView(id = R.id.tv_transfer_nickname)
	private TextView tv_transfer_nickname;
	@BindView(id = R.id.tv_transfer_sex)
	private TextView tv_transfer_sex;
	@BindView(id = R.id.tv_transfer_phone)
	private TextView tv_transfer_phone;
	@BindView(id = R.id.tv_transfer_register)
	private TextView tv_transfer_register;

	// 原始借款人信息
	@BindView(id = R.id.ll_person)
	private LinearLayout ll_person;
	@BindView(id = R.id.tv_person_info)
	private TextView tv_person_info;
	@BindView(id = R.id.tv_username)
	private TextView tv_username;
	@BindView(id = R.id.tv_sex)
	private TextView tv_sex;
	@BindView(id = R.id.tv_age)
	private TextView tv_age;
	@BindView(id = R.id.tv_purpose)
	private TextView tv_purpose;

	// 原始借款企业信息
	@BindView(id = R.id.ll_business)
	private LinearLayout ll_business;
	@BindView(id = R.id.tv_business_info)
	private TextView tv_business_info;
	@BindView(id = R.id.tv_company_name)
	private TextView tv_company_name;
	@BindView(id = R.id.tv_legalPerson)
	private TextView tv_legalPerson;
	@BindView(id = R.id.tv_industry)
	private TextView tv_industry;
	@BindView(id = R.id.tv_registered_capital)
	private TextView tv_registered_capital;
	@BindView(id = R.id.tv_old_info, click = true)
	private TextView tv_old_info;
	
	@BindView(id = R.id.listview2)
	private KJListView listview2;
	private CommonAdapter<DetailRecord> adapter2;
	private List<DetailRecord> record;
	
//	@BindView(id = R.id.record,click = true)
//	private LinearLayout ll_transferrecord;
	
	@BindView(id = R.id.empty)
	private TextView empty;
	
	@BindView(id = R.id.drop_down_menu, click = true)
	private ImageView drop_down_menu;
	
//	@BindView(id = R.id.ll,click = true)
//	private LinearLayout ll;
	
//	@BindView(id = R.id.ll_transfer_product,click = true)
//	private LinearLayout ll_transfer_product;

	private KJHttp http;
	private HttpParams params;
	private DetailProduct product;
	private int id;
	private String products_name;
	private String products_title;
	private int oid_transfer_id;// 债权产品ID
	private int mul;
	private double max;
	private int min;
	private double available;//账户余额
	private String agreement;// 合同
	
	private boolean next = false;// 下一个页面可进行标识
	private int page = 1;
	private boolean noMoreData;
	
	private TitlePopup titlePopup;
	
	private int cardStatus;
	private String bf_bank_id;
	private String bankAccount;
	private String remainingInvestmentAmount2;//可投金额

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_tender_transfer);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// EditText上弹
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		UIHelper.setTitleView(this, "", "投标");
		
		ActivityUtil.getActivityUtil().addActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Intent intent = getIntent();
		id = intent.getIntExtra("id", 0);
		products_name = intent.getStringExtra("name");
		UIHelper.setTitleView(this, "", products_name);
		oid_transfer_id = intent.getIntExtra("oid_transfer_id", 0);
		http = new KJHttp();
		
		getData(1);
		next = false;
		
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
		params = new HttpParams();
		params.put("sid", AppVariables.sid);
		params.put("id", id);
		params.put("page", page);
		params.put("oid_transfer_id", oid_transfer_id);
		http.post(AppConstants.DETAIL_PRODUCT_TRANSFER + id, params, httpCallback);
	}

	
	@Override
	public void initWidget() {
		super.initWidget();
		adapter2 = new CommonAdapter<DetailRecord>(TenderTransferActivity.this, R.layout.item_detail_record) {
			@Override
			public void canvas(ViewHolder holder, DetailRecord item) {
				holder.setText(R.id.realName, item.getRealName(), false);
				holder.setText(R.id.price, "¥" + item.getPrice(), false);
				holder.setText(R.id.createDate, item.getCreateDate(), false);
			}

			@Override
			public void click(int id, List<DetailRecord> list, int position, ViewHolder viewHolder) {
			}
		};
		adapter2.setList(record);
		listview2.setAdapter(adapter2);
		listview2.setOnRefreshListener(refreshListener);
		listview2.setEmptyView(findViewById(R.id.empty));
		listview2.setOnItemClickListener(listener);
	}

	public OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			InputMethodManager imm;
			imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	};
	private KJRefreshListener refreshListener = new KJRefreshListener() {
		@Override
		public void onRefresh() {
			getData(1);
		}

		@Override
		public void onLoadMore() {
			if (!noMoreData) {
				getData(page + 1);
			}
		}
	};

	private HttpCallBack httpCallback = new HttpCallBack(TenderTransferActivity.this) {
		public void success(org.json.JSONObject ret) {
			try {
				product = new DetailProduct(ret);
				products_title = ret.getJSONObject("product").getString("product_name");
				JSONObject p = ret.getJSONObject("product");
				agreement = p.getString("agreement");
				mul = (p.getInt("baseLimitAmount")) / 100;
				max = Double.parseDouble(p.getString("remainingInvestmentAmount"));
				min = Integer.parseInt(p.getString("singlePurchaseLowerLimit"));
				initView();
				
				JSONObject articles = ret.getJSONObject("productOrders");
				int itemCount = articles.getInt("itemCount");
				if(itemCount != 0){
					empty.setVisibility(View.GONE);
				}
				page = articles.getInt("currentPage");
				int maxPage = articles.getJSONObject("pager").getInt("maxPage");
				if (page >= maxPage) {
					listview2.hideFooter();
					noMoreData = true;
				} else {
					listview2.showFooter();
					noMoreData = false;
				}
				record = new DetailRecordList(articles.getJSONArray("items")).getList();
				
				adapter2.setList(record);
				available = ret.getDouble("available");
				mAvaliable.setText(ConvUtils.convToMoney(available) + "元");
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(TenderTransferActivity.this, R.string.app_data_error, Toast.LENGTH_SHORT).show();
			}
		}
	};

	private void initView() {
		
		totalInvestment.setText(product.getTotalInvestment());
		investmentPeriodDesc.setText(product.getInvestmentPeriodDesc());
		investmentPeriodDescunit.setText(product.getInvestmentPeriodDescunit());
		annualizedGain.setText(product.getAnnualizedGain());
		guaranteeModeName.setText(product.getGuaranteeModeName());
		repaymentMethodName.setText(product.getRepaymentMethodName());
		interestBeginDate.setText("剩余投资时间：" + product.getExpirationDate());
		remainingInvestmentAmount.setText(product.getRemainingInvestmentAmount_show());
		remainingInvestmentAmount2 = product.getRemainingInvestmentAmount_show();
		singlePurchaseLowerLimit.setText(product.getSinglePurchaseLowerLimit_show());
		tv_transfer_froze_time.setText(product.getTransfer_froze_time());
		
		if (product.getInvestmentProgress() == 100) {
			tv_buy.setBackgroundColor(getResources().getColor(R.color.app_font_light));
			tv_buy.setText("已满标");
			tv_buy.setClickable(false);
		} else {
			percentage.setText("");
			percentagetxt.setText(product.getInvestmentProgress() + "%");
		}
		percentagepb.setProgress(product.getInvestmentProgress());
		
//		String str = TypeArray.status[product.getNewstatus()];
		switch (product.getNewstatus()) {
		case 0:
			tv_stauts.setText("正在售卖");
			tv_stauts.setTextColor(getResources().getColor(R.color.app_blue));
			tv_buy.setText("立即投资");
			break;
		case 1:
			tv_stauts.setText("还款中");
			tv_buy.setText("还款中");
			break;
		case 2:
			tv_stauts.setText("已满标");
			break;
		case 3:
			tv_stauts.setText("预约");
			tv_buy.setText("预约("+ product.getFinance_start_date() + "开始)");
			tv_buy.setBackgroundColor(getResources().getColor(R.color.app_font_light));
			tv_buy.setClickable(false);
			break;
		case 4:
			tv_stauts.setText("已结束");
			break;
		case 5:
			tv_stauts.setText("正在售卖");
			tv_stauts.setTextColor(getResources().getColor(R.color.app_blue));
			break;
		case 6:
			tv_stauts.setText("已还款");
			break;
		case 7:
			tv_stauts.setText("审核中");
			break;
		case 8:
			tv_stauts.setText("转让成功");
			break;
		case 9:
			tv_stauts.setText("正在售卖");
			tv_stauts.setTextColor(getResources().getColor(R.color.app_blue));
			break;
		}
		
		// 转让人信息
		ll_transfer_person.setVisibility(View.VISIBLE);
		
		tv_person_transfer_info.setText("转让人信息");
		tv_transfer_nickname.setText(product.getTransferNickname());
		tv_transfer_sex.setText(product.getTransferSex());
		tv_transfer_phone.setText(product.getTransferPhone());
		tv_transfer_register.setText(product.getTransferCreate());
		
		// 借款方区分 0:无 1:借款人信息 2:原始借款人信息 3:原始借款企业借款信息
		if ("0".equals(product.getPersonTypeKbn())) {
			ll_person.setVisibility(View.GONE);
			ll_business.setVisibility(View.GONE);

		} else if ("1".equals(product.getPersonTypeKbn()) || "2".equals(product.getPersonTypeKbn())) {
			ll_person.setVisibility(View.VISIBLE);
			ll_business.setVisibility(View.GONE);

			tv_person_info.setText(product.getPersonTypeTitle());
			tv_username.setText(product.getUsername());
			tv_sex.setText(product.getGender());
			tv_age.setText(product.getAge());
			tv_purpose.setText("资金用途：" + product.getPurpose());
		} else if ("3".equals(product.getPersonTypeKbn())) {
			ll_person.setVisibility(View.GONE);
			ll_business.setVisibility(View.VISIBLE);

			tv_business_info.setText("原始借款企业信息");
			tv_company_name.setText(product.getgCompanyName());
			tv_legalPerson.setText(product.getgLegalPerson());
			tv_registered_capital.setText(product.getgRegisteredCapital());
			tv_industry.setText(product.getgIndustry());
		}
		
		// 收益计算
		http = new KJHttp();
		params = new HttpParams();
		params.put("day_month", product.getInvestmentPeriodDescunit());
		params.put("interest",annualizedGain.getText().toString());
		params.put("amount", et_price.getText().toString());
		params.put("invest_day", product.getInvestmentPeriodDesc());
		http.post(AppConstants.PROFIT_CALCULATOR, params, profitcallback);
		
		next = true;
	}
	
	@Override
	public void widgetClick(View v) {
		Intent intent;
		InputMethodManager imm;
		super.widgetClick(v);
		if(next){
			switch (v.getId()) {
//			case R.id.ll:
//				imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
//		        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);//强制隐藏键盘
//				break;
//			case R.id.ll_transfer_product:
//				imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
//		        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);//强制隐藏键盘
//				break;
//			case R.id.record:
//				imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
//		        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);//强制隐藏键盘
//				break;
			case R.id.tender_cash:
				if(!AppVariables.isSignin){
					startActivity(new Intent(TenderTransferActivity.this, SigninActivity.class));
				} else {
					charge();
				}
				break;
			case R.id.tv_buy:
				if (!AppVariables.isSignin) {
					startActivity(new Intent(TenderTransferActivity.this, SigninActivity.class));
					break;
				} else {
					if(product.getNewstatus() == 9){
						ToastCommom toastCommom = ToastCommom.createToastConfig();
						toastCommom.ToastShow(TenderTransferActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "您不能投资自己发布的借款产品或转让产品");
					} else {
						buy();
					}
				}
				break;
			case R.id.tv_old_info:
				intent = new Intent(TenderTransferActivity.this, TenderActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("name", products_title);// 产品名称
				startActivity(intent);
				break;
			case R.id.tv_profit:
				imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
		        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				http = new KJHttp();
				params = new HttpParams();
				params.put("day_month", product.getInvestmentPeriodDescunit());
				params.put("interest",annualizedGain.getText().toString());
				params.put("amount", et_price.getText().toString());
				params.put("invest_day", product.getInvestmentPeriodDesc());
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
		http.post(AppConstants.BASICINFO, params, new HttpCallBack(TenderTransferActivity.this) {
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					JSONObject account = ret.getJSONObject("account");
					if (account.getInt("accountStatus") == 2) {
						Intent intent = new Intent(TenderTransferActivity.this, ConfirmBuyTransferActivity.class);
						intent.putExtra("agreement", agreement);
						intent.putExtra("agreement_url", product.getUrl());
						intent.putExtra("oid_transfer_id", oid_transfer_id);
						intent.putExtra("available", available);// 可用余额
						intent.putExtra("max", max);// 最大可投金额
						intent.putExtra("remainingInvestmentAmount2", remainingInvestmentAmount2);//可投金额
						intent.putExtra("min", min);// 最小投资金额
						intent.putExtra("mul", mul);// 递增金额
						intent.putExtra("title", products_name);// 产品标题
						intent.putExtra("investmentPeriodDesc", investmentPeriodDesc.getText().toString());//投资期限
	                    intent.putExtra("investmentPeriodDescunit", investmentPeriodDescunit.getText().toString());//投资期限单位
	                    intent.putExtra("annualizedGain", annualizedGain.getText().toString());//收益利率
	                    intent.putExtra("cardStatus", cardStatus);
	                    intent.putExtra("bf_bank_id", bf_bank_id);
	                    intent.putExtra("bankAccount", bankAccount);
						startActivity(intent);
						
					} else {
						final CustomDialogUtil dialog = new CustomDialogUtil(TenderTransferActivity.this);
						dialog.setTitle("温馨提示");
						dialog.setMessage("请先实名认证");
						dialog.setPositive("前往");
						dialog.positiveClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								startActivity(new Intent(TenderTransferActivity.this, AccountActivity.class));
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
	private HttpCallBack profitcallback = new HttpCallBack(TenderTransferActivity.this) {

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
		http.post(AppConstants.BASICINFO, params, new HttpCallBack(TenderTransferActivity.this) {
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					JSONObject account = ret.getJSONObject("account");
					if (account.getInt("accountStatus") == 2) {
						// 充值
						cardStatus = account.getInt("cardStatus");
						bf_bank_id = account.getString("bf_bank_id");
						bankAccount = account.getString("bankAccount");
						Intent charge = new Intent(TenderTransferActivity.this, ChargeCashBFActivity.class);
						charge.putExtra("type", "charge");
						charge.putExtra("cardStatus", cardStatus);// 绑卡标识 2：有效 0无效
						charge.putExtra("bf_bank_id", bf_bank_id);
						charge.putExtra("bankAccount", bankAccount);
						startActivity(charge);
					} else {
						final CustomDialogUtil dialog = new CustomDialogUtil(TenderTransferActivity.this);
						dialog.setTitle("温馨提示");
						dialog.setMessage("请先实名认证");
						dialog.positiveClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								startActivity(new Intent(TenderTransferActivity.this, AccountActivity.class));
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 1 && resultCode == 2) {
			tv_buy.setBackgroundColor(getResources().getColor(R.color.app_blue));
			tv_buy.setClickable(true);
		}
	}
	
}
