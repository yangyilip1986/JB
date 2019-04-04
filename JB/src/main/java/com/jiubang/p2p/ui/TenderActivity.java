package com.jiubang.p2p.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.adapter.ImageAdapter;
import com.jiubang.p2p.adapter.PlanAdapter;
import com.jiubang.p2p.adapter.ProductDetailAdapter;
import com.jiubang.p2p.adapter.RecordAdapter;
import com.jiubang.p2p.adapter.TenderAdapter;
import com.jiubang.p2p.bean.DetailPlan;
import com.jiubang.p2p.bean.DetailPlanList;
import com.jiubang.p2p.bean.DetailProduct;
import com.jiubang.p2p.bean.DetailRecord;
import com.jiubang.p2p.bean.DetailRecordList;
import com.jiubang.p2p.bean.TenderBean;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.CustomDialogUtil;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.map.TypeArray;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.ConvUtils;
import com.jiubang.p2p.utils.ListViewHight;
import com.jiubang.p2p.utils.UIHelper;
import com.jiubang.p2p.view.ListViewForScrollView;
import com.jiubang.p2p.widget.CircleProgressBar;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;

/**
 * 直投直贷专区item详情--投标
 * */
public class TenderActivity extends KJActivity {
	private final String TAG = "TenderActivity";
	//屏幕适配
	private DisplayMetrics displayMetrics;
	private int window_width;
	private int window_height;
	private LayoutParams para;

	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;
	
	// 产品详情
	//总金额
	@BindView(id = R.id.totalInvestment)
	private TextView totalInvestment;
	//投资时限
	@BindView(id = R.id.investmentPeriodDesc)
	private TextView investmentPeriodDesc;
	@BindView(id = R.id.investmentPeriodDescunit)
	private TextView investmentPeriodDescunit;
	//预计年化收益率
	@BindView(id = R.id.annualizedGain)
	private TextView annualizedGain;
	//第三方机构担保
	@BindView(id = R.id.guaranteeModeName)
	private TextView guaranteeModeName;
	//还款方式
	@BindView(id = R.id.repaymentMethodName)
	private TextView repaymentMethodName;
	//还款中or投标截止
	@BindView(id = R.id.interestBeginDate)
	private TextView interestBeginDate;
	//可投金额
	@BindView(id = R.id.remainingInvestmentAmount)
	private TextView remainingInvestmentAmount;
	//起投金额
	@BindView(id = R.id.singlePurchaseLowerLimit)
	private TextView singlePurchaseLowerLimit;
	//圆形百分比控件
	@BindView(id = R.id.percentagepb)
	private CircleProgressBar percentagepb;
	//充值按钮
	@BindView(id = R.id.tender_cash, click = true)
	private TextView tender_cash;
	//收益计算
	@BindView(id = R.id.et_price)
	private EditText et_price;
	//最下边按钮
	@BindView(id = R.id.tv_buy,click = true)
	private TextView tv_buy;
	//账户余额
	@BindView(id = R.id.available)
	private TextView mAvaliable;
	//持有N天以上可转让
	@BindView(id = R.id.tv_transfer_froze_time)
	private TextView tv_transfer_froze_time;
	//// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)
	@BindView(id = R.id.tv_xian_or_ti)
	private TextView tv_xian_or_ti;
	//加息
	@BindView(id = R.id.add_v)
	private LinearLayout add_v;
	@BindView(id = R.id.add)
	private TextView add;
	//计算具体收益
	@BindView(id =R.id.tenserinvest_price)
	private TextView tenserinvest_price;
	
	@BindView(id = R.id.ll_plan)
	private LinearLayout ll_plan;//回款计划
	@BindView(id = R.id.listview1)
	private ListViewForScrollView listview1;// 回款计划
	private PlanAdapter planAdapter;
	private List<DetailPlan> plan;
	
	@BindView(id = R.id.record)
	private LinearLayout mRecord;// 投标记录
	@BindView(id = R.id.listview2)
	private ListViewForScrollView listview2;// 投标记录
	private RecordAdapter recordAdapter;
	private List<DetailRecord> record;
	
	@BindView(id = R.id.empty)
	private TextView empty;
	

	@BindView(id = R.id.ll_project_profile)
	private LinearLayout ll_project_profile;//项目简介
	
	// 个人信息 
	@BindView(id = R.id.ll_person)
	private LinearLayout ll_person;//原始借款人Linear
	@BindView(id = R.id.tv_person_info)//原始借款人信息
	private TextView tv_person_info;
	@BindView(id = R.id.tv_username)
	private TextView tv_username;
	@BindView(id = R.id.tv_sex)
	private TextView tv_sex;
	@BindView(id = R.id.tv_age)
	private TextView tv_age;
	@BindView(id = R.id.tv_purpose)
	private TextView tv_purpose;
	
	// 贷款描述
	@BindView(id = R.id.description)
	private TextView description;
	
	// 企业信息
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
	@BindView(id = R.id.tv_purpose1)
	private TextView tv_purpose1;
	@BindView(id = R.id.tv_company_introduce)
	private TextView tv_company_introduce;
	
	// 个人审核状况
	@BindView(id = R.id.ll_personal)
	private LinearLayout ll_personal;
	// 企业审核状况
	@BindView(id = R.id.ll_company)
	private LinearLayout ll_company;
	
	// 信息纰漏
	@BindView(id = R.id.ll_project_information)
	private LinearLayout ll_project_information;//项目资料
	
	//担保方介绍
	@BindView(id = R.id.tv_description)
	private TextView tv_description;
	@BindView(id = R.id.ll_description)
	private LinearLayout ll_description;
	
	//安全保障
	@BindView(id = R.id.tv_description_riskDescri)
	private TextView tv_description_riskDescri;
	@BindView(id = R.id.ll_description_riskDescri)
	private LinearLayout ll_description_riskDescri;
	
	@BindView(id = R.id.gv_gridview)
	private GridView gv_gridview;
	private ProductDetailAdapter productDetailAdapter;
	private List<Map<String, Object>> data_list;
	@BindView(id = R.id.rl_project_information)
	private RelativeLayout rl_project_information;
	@BindView(id = R.id.iv_cancel, click = true)
	private ImageView iv_cancel;
	@BindView(id = R.id.lv_image)
	private ListView lv_image;
	private ImageAdapter imageAdapter;
	
	@BindView(id = R.id.tv_profit, click = true)
	private TextView tv_profit;
	@BindView(id = R.id.tv_stauts)
	private TextView tv_stauts;
	
//	@BindView(id = R.id.ll, click = true)
//	private LinearLayout ll;
	
	@BindView(id = R.id.gv_audit_record)
	private GridView gv_audit_record;//个人审核记录
	
	@BindView(id = R.id.gv_audit_company)
	private GridView gv_audit_company;//企业审核状况
	
//	@BindView(id = R.id.ll_tender_product, click = true)
//	private LinearLayout ll_tender_product;
	
	private KJHttp http;
	private HttpParams params;
	private DetailProduct product;
	private int id;
	private int products_exp_type;// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)
	private String products_name;
	private double available; //账户余额
	private TitlePopup titlePopup;
	
	private Double annualizedGain2;
	private Double add2;
	private Double num;
	//加息
	private String tenderAward;
	
	private boolean next = false;// 下一个页面可进行标识
	
	private List<TenderBean> mListTenderBean;
	
	@BindView(id = R.id.iv_pro, click = true)
	private ImageView iv_pro;
	
	@BindView(id = R.id.ll_singleLimit)
	private LinearLayout ll_singleLimit;
	
	private PopupWindow popupWindow;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_tender);
		// 透明状态栏  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // EditText上弹
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //屏幕适配
        displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		window_height = displayMetrics.heightPixels;
		window_width = displayMetrics.widthPixels;
        
		ActivityUtil.getActivityUtil().addActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		next = false;

		Intent intent = getIntent();
		id = intent.getIntExtra("id", 0);
		products_name = intent.getStringExtra("name");
		products_exp_type = intent.getIntExtra("products_exp_type", 0);// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)
		UIHelper.setTitleView(this, "", products_name);
		
		// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)
		if(products_exp_type == 1) {
			ll_singleLimit.setVisibility(View.GONE);
			iv_pro.setVisibility(View.VISIBLE);
		} else if(products_exp_type == 2) {
			iv_pro.setVisibility(View.VISIBLE);
		}
		
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
		params.put("id", id);
		params.put("page", page);
		http.post(AppConstants.DETAIL_PRODUCT + id, params, httpCallback);
	}
	
	private HttpCallBack httpCallback = new HttpCallBack(TenderActivity.this) {
		public void success(org.json.JSONObject ret) {
			try {
				product = new DetailProduct(ret);
				JSONObject p = ret.getJSONObject("product");
				plan = new DetailPlanList(ret.getJSONArray("productRepayPlan"), p.getString("firstPaybackDate")).getList();
				JSONObject articles = ret.getJSONObject("productOrders");
				int itemCount = articles.getInt("itemCount");
				if(itemCount != 0){
					empty.setVisibility(View.GONE);
				}
				record = new DetailRecordList(articles.getJSONArray("items")).getList();
				initView();
				
				planAdapter = new PlanAdapter(TenderActivity.this, plan);
				listview1.setAdapter(planAdapter);
				listview1.setOnItemClickListener(listener);
				recordAdapter = new RecordAdapter(TenderActivity.this, record);
				listview2.setAdapter(recordAdapter);
				listview2.setOnItemClickListener(listener);
				available = ret.getDouble("available");
				//账户余额
				mAvaliable.setText(ConvUtils.convToMoney(available) + "元");
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(TenderActivity.this, R.string.app_data_error, Toast.LENGTH_SHORT).show();
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

		// 产品详情
		//总金额
		totalInvestment.setText(product.getTotalInvestment());
		//投资时限
		investmentPeriodDesc.setText(product.getInvestmentPeriodDesc());
		investmentPeriodDescunit.setText(product.getInvestmentPeriodDescunit());
		//预计年化收益率
		annualizedGain.setText(product.getAnnualizedGain());
		//加息
		tenderAward = product.getTenderAward();
		if (!"".equals(tenderAward)) {
			add_v.setVisibility(View.VISIBLE);
			add.setText(tenderAward);
			add2 = Double.parseDouble(tenderAward);
		}else {
			add2=0.00;
		}
		annualizedGain2 = Double.parseDouble(annualizedGain.getText().toString());
        num = annualizedGain2 + add2;
        //第三方机构担保
		guaranteeModeName.setText(product.getGuaranteeModeName());
		//还款方式
		repaymentMethodName.setText(product.getRepaymentMethodName());
		//还款中or投标截止
		interestBeginDate.setText("剩余投资时间："+product.getExpirationDate());
		//可投金额
		remainingInvestmentAmount.setText(product.getRemainingInvestmentAmount_show());
		//起投金额
		singlePurchaseLowerLimit.setText(product.getSinglePurchaseLowerLimit_show());
//		if (product.getInvestmentProgress() == 100) {
//			tv_buy.setBackgroundColor(getResources().getColor(R.color.app_font_light));
//			tv_buy.setText("已满标");
//			tv_buy.setClickable(false);
//		}

		//最下边按钮显示文字状态设置
		String str = TypeArray.status[product.getNewstatus()];
		switch (product.getNewstatus()) {
		case 1:
			tv_stauts.setText("还款中");
			ll_plan.setVisibility(View.VISIBLE);
			//还款中or投标截止
			interestBeginDate.setText("还款中");
			tv_buy.setBackgroundColor(getResources().getColor(R.color.app_font_light));
			tv_buy.setText("还款中");
			tv_buy.setClickable(false);
			break;
		case 2:
			tv_stauts.setText("已满标");
			tv_buy.setBackgroundColor(getResources().getColor(R.color.app_font_light));
			tv_buy.setText("已满标");
			tv_buy.setClickable(false);
			break;
		case 3:
			tv_stauts.setText("预约");
			tv_buy.setBackgroundColor(getResources().getColor(R.color.app_font_light));
			tv_buy.setText("预约("+ product.getFinance_start_date() + "开始)");
			tv_buy.setClickable(false);
			break;
		case 4:
			tv_stauts.setText("已结束");
			ll_plan.setVisibility(View.VISIBLE);
			tv_buy.setBackgroundColor(getResources().getColor(R.color.app_font_light));
			tv_buy.setClickable(false);
			break;
		case 5:
			tv_stauts.setText("正在售卖");
			tv_stauts.setTextColor(getResources().getColor(R.color.app_blue));
			break;
		case 6:
			tv_stauts.setText("已还款");
			ll_plan.setVisibility(View.VISIBLE);
			tv_buy.setBackgroundColor(getResources().getColor(R.color.app_font_light));
			tv_buy.setText("已还款");
			tv_buy.setClickable(false);
			break;
		case 7:
			tv_stauts.setText("审核中");
			ll_plan.setVisibility(View.VISIBLE);
			tv_buy.setBackgroundColor(getResources().getColor(R.color.app_font_light));
			tv_buy.setText("审核中");
			tv_buy.setClickable(false);
			break;
		case 8:
			tv_stauts.setText("转让成功");
			ll_plan.setVisibility(View.VISIBLE);
			tv_buy.setBackgroundColor(getResources().getColor(R.color.app_font_light));
			tv_buy.setClickable(false);
			break;
		}
		
		// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)
		if(products_exp_type == 1) {
			tv_xian_or_ti.setText("仅限体验金投资");
			tv_xian_or_ti.setVisibility(View.VISIBLE);
		} else if(products_exp_type == 2) {
			tv_xian_or_ti.setText("支持现金和体验金投资");
			tv_xian_or_ti.setVisibility(View.VISIBLE);
		} else {
			tv_xian_or_ti.setVisibility(View.GONE);

			//持有N天以上可转让
			if(!"".equals(product.getTransfer_froze_time())) {
				tv_transfer_froze_time.setVisibility(View.VISIBLE);
				tv_transfer_froze_time.setText(product.getTransfer_froze_time());
			}
		}
		
		if ("".equals(product.getDetailDescription())) {
			ll_project_profile.setVisibility(View.GONE);
		} else {
			ll_project_profile.setVisibility(View.VISIBLE);
			description.setText(product.getDetailDescription());
		}
		
		// 项目简介
		if ("".equals(product.getDetailDescription())) {
			ll_project_profile.setVisibility(View.GONE);
		} else {
			ll_project_profile.setVisibility(View.VISIBLE);
			description.setText(product.getDetailDescription());
		}
		// 担保方介绍
		if ("".equals(product.getDescription())) {
			ll_description.setVisibility(View.GONE);
		} else {
			ll_description.setVisibility(View.VISIBLE);
			tv_description.setText(product.getDescription());
		}
		
		// 安全保障
		if ("".equals(product.getDescriptionRiskDescri())) {
			ll_description_riskDescri.setVisibility(View.GONE);
		} else {
			ll_description_riskDescri.setVisibility(View.VISIBLE);
			tv_description_riskDescri.setText(product
					.getDescriptionRiskDescri());
		}
		//信息纰漏
		if (product.getInformationList().size() == 0) {
			ll_project_information.setVisibility(View.GONE);
		} else {
			ll_project_information.setVisibility(View.VISIBLE);
			List<Map<String, Object>> informationList = product.getInformationList();

			data_list = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < informationList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("imageList", informationList.get(i).get("informationImageList"));
				map.put("image", informationList.get(i).get("typeImageUrl"));
				map.put("typeName", informationList.get(i).get("typeName"));
				data_list.add(map);
			}

			LayoutParams params = new LayoutParams(data_list.size() * ((window_width / 3)), LayoutParams.WRAP_CONTENT);
			gv_gridview.setLayoutParams(params);
			gv_gridview.setColumnWidth((window_width / 3));
			gv_gridview.setStretchMode(GridView.NO_STRETCH);
			gv_gridview.setNumColumns(data_list.size());
			String[] from = { "img_path", "cost_point" };
			int[] to = { R.id.iv_image, R.id.tv_text };
			productDetailAdapter = new ProductDetailAdapter(TenderActivity.this, data_list, R.layout.item_project_information, from, to);
			gv_gridview.setAdapter(productDetailAdapter);

			gv_gridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					rl_project_information.setVisibility(View.VISIBLE);

					@SuppressWarnings("unchecked")
					List<String> list = (List<String>) data_list.get(position).get("imageList");
					imageAdapter = new ImageAdapter(TenderActivity.this, list);

					//屏幕适配
					para = (LayoutParams) lv_image.getLayoutParams();
//					para.height = (int) (window_height * (750.0 / 1334));
//					para.width = (int) (window_width * (430.0 / 750));
					para.height = (int) (window_height * 0.8);
					para.width = (int) (window_width * 0.8);
					lv_image.setLayoutParams(para);
					lv_image.setAdapter(imageAdapter);
				}
			});
		}
		
		// 借款方区分 0:无 1:借款人信息 2:原始借款人信息 3:原始借款企业借款信息
		if ("0".equals(product.getPersonTypeKbn())) {
			ll_person.setVisibility(View.GONE);
			ll_business.setVisibility(View.GONE);

		} else if ("1".equals(product.getPersonTypeKbn())
				|| "2".equals(product.getPersonTypeKbn())) {
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
			tv_purpose1.setText("资金用途：" + product.getgPurpose1());
			tv_company_introduce.setText("企业介绍："+product.getgCompanyIntroduce());
		}
		
		// 审核信息区分 0:无 1:个人或者原始借款人审核信息 2:原始企业审核信息
		if ("0".equals(product.getCheckInfoFlg())) {
			ll_personal.setVisibility(View.GONE);
			ll_company.setVisibility(View.GONE);
		} else if ("1".equals(product.getCheckInfoFlg())) {
			mListTenderBean = new ArrayList<TenderBean>();
			//身份证
			if (product.isIdCardCheckFlg()) {
				TenderBean tenderbean = new TenderBean();
				tenderbean.setAuditing("身份证");
				mListTenderBean.add(tenderbean);
			}
			if (product.isEstateCheckFlg()) {
				TenderBean tenderbean = new TenderBean();
				tenderbean.setAuditing("房产证");
				mListTenderBean.add(tenderbean);
			}
			//婚姻证明
			if (product.isMarriageCheckFlg()) {
				TenderBean tenderbean = new TenderBean();
				tenderbean.setAuditing("婚姻证明");
				mListTenderBean.add(tenderbean);
			}
			//户口本
			if (product.isHouseholdCheckFlg()) {
				TenderBean tenderbean = new TenderBean();
				tenderbean.setAuditing("户口本");
				mListTenderBean.add(tenderbean);
			}
			//信用报告
			if (product.isCredibilityCheckFlg()) {
				TenderBean tenderbean = new TenderBean();
				tenderbean.setAuditing("信用报告");
				mListTenderBean.add(tenderbean);
			}
			//机构担保审核
			if (product.isGuaranteeCheckFlg()) {
				TenderBean tenderbean = new TenderBean();
				tenderbean.setAuditing("机构担保审核");
				mListTenderBean.add(tenderbean);
			}
				
			if(mListTenderBean.size() != 0){
				ll_personal.setVisibility(View.VISIBLE);
				ll_company.setVisibility(View.GONE);
				gv_audit_record.setAdapter(new TenderAdapter(TenderActivity.this, mListTenderBean));
				gv_audit_record.setSelector(new ColorDrawable(Color.TRANSPARENT));
				ListViewHight.setGridViewHeightBasedOnChildren(gv_audit_record);
			} else {
				ll_personal.setVisibility(View.GONE);
				ll_company.setVisibility(View.GONE);
			}
		} else if ("2".equals(product.getCheckInfoFlg())) {
			mListTenderBean = new ArrayList<TenderBean>();
			//营业执照
			if (product.isBusinessCheckFlg()) {
				TenderBean tenderbean = new TenderBean();
				tenderbean.setAuditing("营业执照");
				mListTenderBean.add(tenderbean);
			}
			//法人身份证
			if (product.isLegalCardCheckFlg()) {
				TenderBean tenderbean = new TenderBean();
				tenderbean.setAuditing("法人身份证");
				mListTenderBean.add(tenderbean);
			}
			//机构担保
			if (product.isBondingCheckFlg()) {
				TenderBean tenderbean = new TenderBean();
				tenderbean.setAuditing("机构担保");
				mListTenderBean.add(tenderbean);
			}
			//平台审核
			if (product.isPlatformCheckFlg()) {
				TenderBean tenderbean = new TenderBean();
				tenderbean.setAuditing("平台审核");
				mListTenderBean.add(tenderbean);
			}
			//营业场所审核
			if (product.isAddressCheckFlg()) {
				TenderBean tenderbean = new TenderBean();
				tenderbean.setAuditing("营业场所审核");
				mListTenderBean.add(tenderbean);
			}

			if(mListTenderBean.size() != 0) {
				ll_personal.setVisibility(View.GONE);
				ll_company.setVisibility(View.VISIBLE);
				gv_audit_company.setAdapter(new TenderAdapter(TenderActivity.this, mListTenderBean));
				gv_audit_company.setSelector(new ColorDrawable(Color.TRANSPARENT));
				ListViewHight.setGridViewHeightBasedOnChildren(gv_audit_company);
			} else {
				ll_personal.setVisibility(View.GONE);
				ll_company.setVisibility(View.GONE);
			}
		}

		//圆形百分比控件
		percentagepb.setProgress(product.getInvestmentProgress());

		http = new KJHttp();
		params = new HttpParams();
		params.put("day_month", product.getInvestmentPeriodDescunit());
		params.put("interest", num.toString());
		//收益计算
		params.put("amount", et_price.getText().toString());
		params.put("invest_day", product.getInvestmentPeriodDesc());
		http.post(AppConstants.PROFIT_CALCULATOR, params, profitcallback);
		
		next = true;
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		InputMethodManager imm;
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.task_detail_popupwindow2, null);
		if(next){
			switch (v.getId()) {
				//充值
			case R.id.tender_cash:
				if(!AppVariables.isSignin) {
					startActivity(new Intent(TenderActivity.this, SigninActivity.class));
				} else {
					charge();
				}
				break;
			case R.id.iv_pro:
				if (popupWindow == null) {
					popupWindow = new PopupWindow(view);
				}

				if (popupWindow.isShowing()) {
					popupWindow.dismiss();// 关闭
				} else {
					iniPopupWindow(popupWindow, view);
					
					popupWindow.showAtLocation(findViewById(R.id.ll), Gravity.CENTER, 0, 160);
					
				}
				
				break;
			case R.id.tv_buy:
				if (!AppVariables.isSignin) {
					startActivity(new Intent(TenderActivity.this, SigninActivity.class));
					break;
				} else {
					buy();
				}
				break;
			case R.id.tv_profit:
				imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
		        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				
				if(add.getText().toString()==null || "".equals(add.getText().toString())){
					add2 = 0.00;
				} else{
					add2 = Double.parseDouble(add.getText().toString());
				}
				annualizedGain2 = Double.parseDouble(annualizedGain.getText().toString());
			    num = annualizedGain2 + add2;
			    
			    http = new KJHttp();
				params = new HttpParams();
				params.put("day_month", product.getInvestmentPeriodDescunit());
				params.put("invest_day", product.getInvestmentPeriodDesc());
				params.put("interest",num.toString());
				//收益计算
				params.put("amount", et_price.getText().toString());
				http.post(AppConstants.PROFIT_CALCULATOR, params, profitcallback);
				break;
			case R.id.iv_cancel:
				rl_project_information.setVisibility(View.GONE);
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
		http.post(AppConstants.BASICINFO, params, new HttpCallBack(TenderActivity.this) {
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					JSONObject account = ret.getJSONObject("account");
					if (account.getInt("accountStatus") == 2) {
						Intent intent = new Intent(TenderActivity.this, ConfirmBuyActivity.class);
						intent.putExtra("id", id);
						intent.putExtra("products_exp_type", products_exp_type);
	                    intent.putExtra("title", products_name);// 产品标题
						startActivity(intent);
					} else {
						final CustomDialogUtil dialog = new CustomDialogUtil(TenderActivity.this);
						dialog.setTitle("温馨提示");
						dialog.setMessage("请先实名认证");
						dialog.setPositive("前往");
						dialog.positiveClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								startActivity(new Intent(TenderActivity.this, IdcardActivity.class));
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
	private HttpCallBack profitcallback = new HttpCallBack(TenderActivity.this) {

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
	public void charge() {
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.BASICINFO, params, new HttpCallBack(TenderActivity.this) {
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					JSONObject account = ret.getJSONObject("account");
					if (account.getInt("accountStatus") == 2) {
						// 充值
						Intent charge = new Intent(TenderActivity.this, ChargeCashBFActivity.class);
						charge.putExtra("type", "charge");
						charge.putExtra("cardStatus", account.getInt("cardStatus"));// 绑卡标识 2：有效 0无效
						charge.putExtra("bf_bank_id", account.getString("bf_bank_id"));
						charge.putExtra("bankAccount", account.getString("bankAccount"));
						startActivity(charge);
					} else {
						final CustomDialogUtil dialog = new CustomDialogUtil(TenderActivity.this);
						dialog.setTitle("温馨提示");
						dialog.setMessage("请先实名认证");
						dialog.positiveClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								startActivity(new Intent(TenderActivity.this, AccountActivity.class));
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
	
	private void iniPopupWindow(final PopupWindow popupWindow, View v) {

		LinearLayout ll_notice = (LinearLayout) v.findViewById(R.id.ll_notice);
		TextView tv_popup = (TextView) ll_notice.findViewById(R.id.tv_popup);
		if(products_exp_type == 2)
		tv_popup.setText(R.string.hunhebiaoshuoming);
		ll_notice.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
		// 控制popupwindow的宽度和高度自适应
		popupWindow.setWidth(ll_notice.getMeasuredWidth());
		popupWindow.setHeight(ll_notice.getMeasuredHeight());

		// 控制popupwindow点击屏幕其他地方消失
		popupWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
		popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
	}
}
