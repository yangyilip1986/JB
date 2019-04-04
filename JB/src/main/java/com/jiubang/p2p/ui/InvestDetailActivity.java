package com.jiubang.p2p.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.adapter.ContractAdapter;
import com.jiubang.p2p.adapter.PaymentDetailsAdapter;
import com.jiubang.p2p.bean.InvestDetail;
import com.jiubang.p2p.bean.InvestDetailList;
import com.jiubang.p2p.dialog.ActionItem;
import com.jiubang.p2p.dialog.TitlePopup;
import com.jiubang.p2p.dialog.TitlePopup.OnItemOnClickListener;
import com.jiubang.p2p.utils.ActivityUtil;
import com.jiubang.p2p.utils.ListViewHight;
import com.jiubang.p2p.utils.UIHelper;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;

/**
 * 投资详情
 * */
public class InvestDetailActivity extends KJActivity {

	@BindView(id = R.id.tv_products_title)
	private TextView tv_products_title;
	@BindView(id = R.id.tv_products_status)
	private TextView tv_products_status;
	@BindView(id = R.id.tv_finance_repay_type)
	private TextView tv_finance_repay_type;
	@BindView(id = R.id.tv_finance_amount)
	private TextView tv_finance_amount;
	@BindView(id = R.id.tv_finance_interest_rate)
	private TextView tv_finance_interest_rate;
	@BindView(id = R.id.tv_extra_rate)
	private TextView tv_extra_rate;
	@BindView(id = R.id.tv_finance_period)
	private TextView tv_finance_period;
	@BindView(id = R.id.tv_finance_start_interest_date)
	private TextView tv_finance_start_interest_date;
	@BindView(id = R.id.tv_finance_end_interest_date)
	private TextView tv_finance_end_interest_date;
	@BindView(id = R.id.tv_total_and_extra)
	private TextView tv_total_and_extra;
	@BindView(id = R.id.tv_total)
	private TextView tv_total;
	@BindView(id = R.id.tv_extra)
	private TextView tv_extra;
	@BindView(id = R.id.tv_penalbond)
	private TextView tv_penalbond;
	
	@BindView(id = R.id.iv_experience_detail_small)
	private ImageView iv_experience_detail_small;   //投资金额的"体"字
	@BindView(id = R.id.iv_experience_ben_xi)
	private ImageView iv_experience_ben_xi;         //应收本息的"体"字
	@BindView(id = R.id.iv_experience_interest)
	private ImageView iv_experience_interest;       //应收利息的"体"字
	
	@BindView(id = R.id.lv_contract)
	private ListView lv_contract;
	@BindView(id = R.id.lv_payment_details)
	private ListView lv_payment_details;
	
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;//右上角下拉菜单
	
	private TitlePopup titlePopup;
	
	private KJHttp http;
	private HttpParams params;
	
	private String oid_tender_id;
	private String tender_from;
	// 合同地址
	private String url;
	
	private ContractAdapter contractAdapter;
	
	private PaymentDetailsAdapter adapter;
	private List<InvestDetail> data;
	
	private String products_exp_type;//产品类型

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_invest_detail);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		UIHelper.setTitleView(this, "", "回款详情");

		ActivityUtil.getActivityUtil().addActivity(this);
		
		Intent intent = getIntent();
		oid_tender_id = intent.getStringExtra("oid_tender_id");
		tender_from = intent.getStringExtra("tender_from");
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
	public void initData() {
		super.initData();
		http = new KJHttp();
		params = new HttpParams();
		getData();
	}
	
	private void getData() {
		params.put("sid", AppVariables.sid);
		params.put("oid_tender_id", oid_tender_id);
		params.put("tender_from", tender_from);
		http.post(AppConstants.INVEST_DETAIL, params, httpCallback);
	}
	
	private HttpCallBack httpCallback = new HttpCallBack(InvestDetailActivity.this) {
		
		@Override
		public void failure(JSONObject ret) {
			super.failure(ret);
		}
		
		@Override
		public void success(JSONObject ret) {
			super.success(ret);
				try {
					products_exp_type = ret.getString("products_exp_type");//产品类型
					if("1".equals(products_exp_type) || "2".equals(products_exp_type)){
						iv_experience_detail_small.setVisibility(View.VISIBLE);
						iv_experience_ben_xi.setVisibility(View.VISIBLE);
						iv_experience_interest.setVisibility(View.VISIBLE);
					}
					if("1".equals(products_exp_type)){
					//纯体验金
						tv_finance_amount.setText(ret.getString("exp_tender_amount") + "元");//投资金额
						tv_total_and_extra.setText(ret.getString("tiyan_interest") + "元");//应收本息
						tv_extra.setText(ret.getString("tiyan_interest") + "元");//应收利息
					}else if("2".equals(products_exp_type)){
					//混合标
						tv_finance_amount.setText(ret.getString("finance_amount") + "元" + "+"+ ret.getString("exp_tender_amount") + "元");//投资金额
						tv_total_and_extra.setText(ret.getString("total_and_extra") + "元" + "+" + ret.getString("tiyan_interest") + "元");//应收本息
						tv_extra.setText(ret.getString("extra") + "元" + "+" + ret.getString("tiyan_interest") + "元");//应收利息
					}else if("0".equals(products_exp_type)){
						tv_finance_amount.setText(ret.getString("finance_amount") + "元");//投资金额
						tv_total_and_extra.setText(ret.getString("total_and_extra") + "元");//应收本息
						tv_extra.setText(ret.getString("extra") + "元");//应收利息
					}
					tv_products_title.setText(ret.getString("products_title"));
					tv_products_status.setText(ret.getString("products_status"));
					tv_finance_repay_type.setText("还款方式：" + ret.getString("finance_repay_type"));
					
					tv_finance_interest_rate.setText("年化收益率：" + ret.getString("finance_interest_rate") + "%");
					if("".equals(ret.getString("extra_rate"))){
						tv_extra_rate.setVisibility(View.GONE);
					} else {
						tv_extra_rate.setVisibility(View.VISIBLE);
						tv_extra_rate.setText("+" + ret.getString("extra_rate"));
					}
					tv_finance_period.setText("项目期限：" + ret.getString("finance_period"));
					tv_finance_start_interest_date.setText("起息日期：" + ret.getString("finance_start_interest_date"));
					tv_finance_end_interest_date.setText("到期日期：" + ret.getString("finance_end_interest_date"));
					
					tv_total.setText(ret.getString("total") + "元");
					
					tv_penalbond.setText(ret.getString("penalbond") + "元");
					url = ret.getString("url");
					
					JSONArray array = ret.getJSONArray("agreement_items");
					List<String> contractNames = new ArrayList<String>();
					for (int i = 0; i < array.length(); i++) {
						contractNames.add(array.get(i) + "");
					}
					contractAdapter = new ContractAdapter(InvestDetailActivity.this, contractNames);
					lv_contract.setAdapter(contractAdapter);
					ListViewHight.setListViewHeightBasedOnChildren(lv_contract);
					lv_contract.setOnItemClickListener(listener);
					
					data = new InvestDetailList(ret.getJSONArray("items")).getList();
					adapter = new PaymentDetailsAdapter(InvestDetailActivity.this, data,products_exp_type);
					lv_payment_details.setAdapter(adapter);
					ListViewHight.setListViewHeightBasedOnChildren(lv_payment_details);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(InvestDetailActivity.this, R.string.app_data_error, Toast.LENGTH_SHORT).show();
				}
			}
		
		@Override
		public void onFinish() {
			super.onFinish();
		}
	};
	
	public void widgetClick(android.view.View v) {
		
		switch (v.getId()) {
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
	

	private OnItemClickListener listener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(InvestDetailActivity.this, TenderProtocolActivity.class);
			intent.putExtra("url", url);
			startActivity(intent);
		}
	};
}
