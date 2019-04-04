package com.jiubang.p2p.ui;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.adapter.FinancialPlanAdapter;
import com.jiubang.p2p.bean.FinancialPlan;
import com.jiubang.p2p.bean.FinancialPlanList;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.UIHelper;
import com.jiubang.p2p.view.ListViewForScrollView;
import com.jiubang.p2p.view.TasksCompletedView;
import com.jiubang.p2p.widget.CircleProgressBar;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;

/**
 * 理财计划
 * */
public class FinancialPlanActivity extends Activity implements OnClickListener {

	private LinearLayout ll_1;
	private LinearLayout ll_2;
	private LinearLayout ll_3;
	
	private TextView tv_interest_rate_a;
	private TextView tv_interest_rate_b;
	private TextView tv_interest_rate_c;
	private TextView tv_invest_period_a;
	private TextView tv_invest_period_b;
	private TextView tv_invest_period_c;
	private TextView tv_minAddAmount_a;
	private TextView tv_minAddAmount_b;
	private TextView tv_minAddAmount_c;
	private TasksCompletedView tcv_progress_a;
	private TasksCompletedView tcv_progress_b;
	private TasksCompletedView tcv_progress_c;
	private CircleProgressBar cpb_percentage_a;
	private CircleProgressBar cpb_percentage_b;
	private CircleProgressBar cpb_percentage_c;
	private TextView tv_amount_total_a;
	private TextView tv_amount_total_b;
	private TextView tv_amount_total_c;
	private EditText et_amount;
	private TextView tv_profit;
	private TextView tv_financialPlanListA;
	private TextView tv_financialPlanListB;
	private TextView tv_financialPlanListC;
	private ImageView drop_down_menu;

	private HttpParams params;
	private KJHttp http;
	
	private ListViewForScrollView lv_financialPlanA;
	private ListViewForScrollView lv_financialPlanB;
	private ListViewForScrollView lv_financialPlanC;
	private FinancialPlanAdapter financialPlanAdapter;
	
	private List<FinancialPlan> financialPlanAs ;
	private List<FinancialPlan> financialPlanBs ;
	private List<FinancialPlan> financialPlanCs ;
	
	private TitlePopup titlePopup;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_financial_plan);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		UIHelper.setTitleView(this, "", "理财计划");
		
		ActivityUtil.getActivityUtil().addActivity(this);
		
		init();

	}

	@Override
	protected void onResume() {
		super.onResume();

		params = new HttpParams();
		http = new KJHttp();
		getData("10000");

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void init() {
//		ll = (LinearLayout) findViewById(R.id.ll);
//		ll.setOnClickListener(this);
//		ll_plan2 = (LinearLayout) findViewById(R.id.ll_plan2);
//		ll_plan2.setOnClickListener(this);
		
		ll_1 = (LinearLayout) findViewById(R.id.ll_1);
		ll_1.setOnClickListener(this);
		ll_2 = (LinearLayout) findViewById(R.id.ll_2);
		ll_2.setOnClickListener(this);
		ll_3 = (LinearLayout) findViewById(R.id.ll_3);
		ll_3.setOnClickListener(this);
		
		tv_interest_rate_a = (TextView) findViewById(R.id.tv_interest_rate_a);
		tv_interest_rate_b = (TextView) findViewById(R.id.tv_interest_rate_b);
		tv_interest_rate_c = (TextView) findViewById(R.id.tv_interest_rate_c);

		tv_invest_period_a = (TextView) findViewById(R.id.tv_invest_period_a);
		tv_invest_period_b = (TextView) findViewById(R.id.tv_invest_period_b);
		tv_invest_period_c = (TextView) findViewById(R.id.tv_invest_period_c);

		tv_minAddAmount_a = (TextView) findViewById(R.id.tv_minAddAmount_a);
		tv_minAddAmount_b = (TextView) findViewById(R.id.tv_minAddAmount_b);
		tv_minAddAmount_c = (TextView) findViewById(R.id.tv_minAddAmount_c);

		tcv_progress_a = (TasksCompletedView) findViewById(R.id.tcv_progress_a);
		tcv_progress_b = (TasksCompletedView) findViewById(R.id.tcv_progress_b);
		tcv_progress_c = (TasksCompletedView) findViewById(R.id.tcv_progress_c);
		
		tv_amount_total_a = (TextView) findViewById(R.id.tv_amount_total_a);
		tv_amount_total_b = (TextView) findViewById(R.id.tv_amount_total_b);
		tv_amount_total_c = (TextView) findViewById(R.id.tv_amount_total_c);

		cpb_percentage_a = (CircleProgressBar) findViewById(R.id.cpb_percentage_a);
		cpb_percentage_b = (CircleProgressBar) findViewById(R.id.cpb_percentage_b);
		cpb_percentage_c = (CircleProgressBar) findViewById(R.id.cpb_percentage_c);

		et_amount = (EditText) findViewById(R.id.et_amount);
		
		tv_profit = (TextView) findViewById(R.id.tv_profit);
		tv_profit.setOnClickListener(this);
		
		lv_financialPlanA = (ListViewForScrollView) findViewById(R.id.lv_financialPlanA);
		lv_financialPlanB = (ListViewForScrollView) findViewById(R.id.lv_financialPlanB);
		lv_financialPlanC = (ListViewForScrollView) findViewById(R.id.lv_financialPlanC);
		
		tv_financialPlanListA = (TextView) findViewById(R.id.tv_financialPlanListA);
		tv_financialPlanListB = (TextView) findViewById(R.id.tv_financialPlanListB);
		tv_financialPlanListC = (TextView) findViewById(R.id.tv_financialPlanListC);
		
		drop_down_menu = (ImageView) findViewById(R.id.drop_down_menu);
		drop_down_menu.setOnClickListener(this);
		
		tv_financialPlanListA.setOnClickListener(this);
		tv_financialPlanListB.setOnClickListener(this);
		tv_financialPlanListC.setOnClickListener(this);

	}

	private void getData(String amount) {
		params = new HttpParams();
		http = new KJHttp();
		
		params.put("amount", amount);
		http.post(AppConstants.FINANCIAL_PLAN, params, httpCallback);
	}

	private HttpCallBack httpCallback = new HttpCallBack(
			FinancialPlanActivity.this) {

		@Override
		public void failure(JSONObject ret) {
			super.failure(ret);
		}

		public void onSuccess(String t) {
			try {

				JSONObject ret = new JSONObject(t);

				JSONObject financialPlanA = ret.getJSONObject("financialPlanA");
				tv_interest_rate_a.setText(financialPlanA.getString("interest_rate"));
				tv_invest_period_a.setText(financialPlanA.getString("investBPeriod") + financialPlanA.getString("investBPeriodUnit"));
				tv_minAddAmount_a.setText(financialPlanA.getString("minAddAmount") + "元");
				tcv_progress_a.setProgress(financialPlanA.getInt("progress"));
				financialPlanA.getInt("financial_plan_status");
				if(financialPlanA.getInt("progress") != 100 && "1".equals(financialPlanA.getString("financial_plan_status"))){
					tcv_progress_a.setText("加入");
				} else {
					tcv_progress_a.setText("查看详情");
					tcv_progress_a.setStatus(1);// 进度变灰
				}

				JSONObject financialPlanB = ret.getJSONObject("financialPlanB");
				tv_interest_rate_b.setText(financialPlanB.getString("interest_rate"));
				tv_invest_period_b.setText(financialPlanB.getString("investBPeriod") + financialPlanB.getString("investBPeriodUnit"));
				tv_minAddAmount_b.setText(financialPlanB.getString("minAddAmount") + "元");
				tcv_progress_b.setProgress(financialPlanB.getInt("progress"));
				if(financialPlanB.getInt("progress") != 100 && "1".equals(financialPlanB.getString("financial_plan_status"))){
					tcv_progress_b.setText("加入");
				} else {
					tcv_progress_b.setText("查看详情");
					tcv_progress_b.setStatus(1);// 进度变灰
				}

				JSONObject financialPlanC = ret.getJSONObject("financialPlanC");
				tv_interest_rate_c.setText(financialPlanC.getString("interest_rate"));
				tv_invest_period_c.setText(financialPlanC.getString("investBPeriod") + financialPlanC.getString("investBPeriodUnit"));
				tv_minAddAmount_c.setText(financialPlanC.getString("minAddAmount") + "元");
				tcv_progress_c.setProgress(financialPlanC.getInt("progress"));
				if(financialPlanC.getInt("progress") != 100 && "1".equals(financialPlanC.getString("financial_plan_status"))){
					tcv_progress_c.setText("加入");
				} else {
					tcv_progress_c.setText("查看详情");
					tcv_progress_c.setStatus(1);// 进度变灰
				}

				JSONObject profit = ret.getJSONObject("profit");
				cpb_percentage_a.setProgress(profit.getInt("percentageA"));
				cpb_percentage_b.setProgress(profit.getInt("percentageB"));
				cpb_percentage_c.setProgress(profit.getInt("percentageC"));
				tv_amount_total_a.setText(profit.getString("amountTotalA")
						+ "元");
				tv_amount_total_b.setText(profit.getString("amountTotalB")
						+ "元");
				tv_amount_total_c.setText(profit.getString("amountTotalC")
						+ "元");
				
				financialPlanAs = new FinancialPlanList(ret.getJSONArray("financialPlanListA")).getFinancialPlans();
				financialPlanBs = new FinancialPlanList(ret.getJSONArray("financialPlanListB")).getFinancialPlans();
				financialPlanCs = new FinancialPlanList(ret.getJSONArray("financialPlanListC")).getFinancialPlans();
				
				financialPlanAdapter = new FinancialPlanAdapter(FinancialPlanActivity.this, financialPlanAs);
				lv_financialPlanA.setAdapter(financialPlanAdapter);
				lv_financialPlanA.setOnItemClickListener(listener);
				financialPlanAdapter = new FinancialPlanAdapter(FinancialPlanActivity.this, financialPlanBs);
				lv_financialPlanB.setAdapter(financialPlanAdapter);
				lv_financialPlanB.setOnItemClickListener(listener);
				financialPlanAdapter = new FinancialPlanAdapter(FinancialPlanActivity.this, financialPlanCs);
				lv_financialPlanC.setAdapter(financialPlanAdapter);
				lv_financialPlanC.setOnItemClickListener(listener);
				
//				ListViewHight.setListViewHeightBasedOnChildren(lv_financialPlanA);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void onFinish() {
			super.onFinish();
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

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ll_1:
			if (!AppVariables.isSignin) {
				intent = new Intent(FinancialPlanActivity.this, SigninActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(FinancialPlanActivity.this, Plan_ABCActivity.class);
				intent.putExtra("type", "A");
				startActivity(intent);
			}
			break;
		case R.id.ll_2:
			if (!AppVariables.isSignin) {
				intent = new Intent(FinancialPlanActivity.this, SigninActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(FinancialPlanActivity.this, Plan_ABCActivity.class);
				intent.putExtra("type", "B");
				startActivity(intent);
			}
			break;
		case R.id.ll_3:
			if (!AppVariables.isSignin) {
				intent = new Intent(FinancialPlanActivity.this, SigninActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(FinancialPlanActivity.this, Plan_ABCActivity.class);
				intent.putExtra("type", "C");
				startActivity(intent);
			}
			break;
		case R.id.tv_profit:
			String amount = et_amount.getText().toString();
			if ("".equals(amount)) {
				amount = "0";
			}
			getData(amount);
			break;
		case R.id.tv_financialPlanListA:
			tv_financialPlanListA.setBackgroundResource(R.drawable.bg_financial_plan);
			tv_financialPlanListB.setBackgroundResource(R.drawable.bg_financial_planed);
			tv_financialPlanListC.setBackgroundResource(R.drawable.bg_financial_planed);
			
			lv_financialPlanA.setVisibility(View.VISIBLE);
			lv_financialPlanB.setVisibility(View.GONE);
			lv_financialPlanC.setVisibility(View.GONE);
			
			break;
		case R.id.tv_financialPlanListB:
			tv_financialPlanListA.setBackgroundResource(R.drawable.bg_financial_planed);
			tv_financialPlanListB.setBackgroundResource(R.drawable.bg_financial_plan);
			tv_financialPlanListC.setBackgroundResource(R.drawable.bg_financial_planed);
			
			lv_financialPlanA.setVisibility(View.GONE);
			lv_financialPlanB.setVisibility(View.VISIBLE);
			lv_financialPlanC.setVisibility(View.GONE);
			
			break;
		case R.id.tv_financialPlanListC:
			tv_financialPlanListA.setBackgroundResource(R.drawable.bg_financial_planed);
			tv_financialPlanListB.setBackgroundResource(R.drawable.bg_financial_planed);
			tv_financialPlanListC.setBackgroundResource(R.drawable.bg_financial_plan);
			
			lv_financialPlanA.setVisibility(View.GONE);
			lv_financialPlanB.setVisibility(View.GONE);
			lv_financialPlanC.setVisibility(View.VISIBLE);
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
