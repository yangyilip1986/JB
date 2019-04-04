package com.jiubang.p2p.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.adapter.MessageMonthlyReportAdapter;
import com.jiubang.p2p.bean.Project;
import com.jiubang.p2p.bean.ProjectList;
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
 * 消息中心详细内容
 * */
public class MessageCenterDetailActivity extends KJActivity {

	private HttpParams params;
	private KJHttp http;

	@BindView(id = R.id.lv_monthly_report)
	private ListView lv_monthly_report;

	@BindView(id = R.id.tv_message_title)
	private TextView tv_message_title;
	@BindView(id = R.id.tv_message_time)
	private TextView tv_message_time;
	@BindView(id = R.id.tv_message_content)
	private TextView tv_message_content;

	@BindView(id = R.id.sv_monthly_report)
	private ScrollView sv_monthly_report;

	// 金融资产
	@BindView(id = R.id.tv_FirstDay)
	private TextView tv_FirstDay;
	@BindView(id = R.id.tv_TotalFirstDay)
	private TextView tv_TotalFirstDay;
	@BindView(id = R.id.tv_TenderFirstDay)
	private TextView tv_TenderFirstDay;
	@BindView(id = R.id.tv_AccountFirstDay)
	private TextView tv_AccountFirstDay;
	@BindView(id = R.id.tv_AccumulateFirstDay)
	private TextView tv_AccumulateFirstDay;
	@BindView(id = R.id.tv_LastDay)
	private TextView tv_LastDay;
	@BindView(id = R.id.tv_TotalLastDay)
	private TextView tv_TotalLastDay;
	@BindView(id = R.id.tv_TenderLastDay)
	private TextView tv_TenderLastDay;
	@BindView(id = R.id.tv_AccountLastDay)
	private TextView tv_AccountLastDay;
	@BindView(id = R.id.tv_AccumulateLastDay)
	private TextView tv_AccumulateLastDay;
	@BindView(id = R.id.tv_TotalChange)
	private TextView tv_TotalChange;
	@BindView(id = R.id.tv_TenderChange)
	private TextView tv_TenderChange;
	@BindView(id = R.id.tv_AccountChange)
	private TextView tv_AccountChange;
	@BindView(id = R.id.tv_AccumulateChange)
	private TextView tv_AccumulateChange;
	@BindView(id = R.id.tv_cash_volume)
	private TextView tv_cash_volume;

	// 金融资产变化
	@BindView(id = R.id.tv_TotalFirstDay2)
	private TextView tv_TotalFirstDay2;
	@BindView(id = R.id.tv_Recharge)
	private TextView tv_Recharge;
	@BindView(id = R.id.tv_Withdraw)
	private TextView tv_Withdraw;
	@BindView(id = R.id.tv_IncomeTender)
	private TextView tv_IncomeTender;
	@BindView(id = R.id.tv_RedPacketUsed)
	private TextView tv_RedPacketUsed;
	@BindView(id = R.id.tv_IncomeBack)
	private TextView tv_IncomeBack;
	@BindView(id = R.id.tv_TotalLastDay2)
	private TextView tv_TotalLastDay2;
	@BindView(id = R.id.tv_TotalChange2)
	private TextView tv_TotalChange2;
	@BindView(id = R.id.tv_FirstDay2)
	private TextView tv_FirstDay2;
	@BindView(id = R.id.tv_LastDay2)
	private TextView tv_LastDay2;

	// 积分变化
	@BindView(id = R.id.tv_AccumulateFirstDay2)
	private TextView tv_AccumulateFirstDay2;
	@BindView(id = R.id.tv_AccumulateReceived)
	private TextView tv_AccumulateReceived;
	@BindView(id = R.id.tv_AccumulateUsed)
	private TextView tv_AccumulateUsed;
	@BindView(id = R.id.tv_AccumulateLastDay2)
	private TextView tv_AccumulateLastDay2;
	@BindView(id = R.id.tv_AccumulateChange2)
	private TextView tv_AccumulateChange2;
	@BindView(id = R.id.tv_FirstDay3)
	private TextView tv_FirstDay3;
	@BindView(id = R.id.tv_LastDay3)
	private TextView tv_LastDay3;

	// 未来三月预估
	@BindView(id = R.id.tv_mouth1)
	private TextView tv_mouth1;
	@BindView(id = R.id.tv_mouth2)
	private TextView tv_mouth2;
	@BindView(id = R.id.tv_mouth3)
	private TextView tv_mouth3;
	@BindView(id = R.id.tv_PrognosisNextMonthTotal1)
	private TextView tv_PrognosisNextMonthTotal1;
	@BindView(id = R.id.tv_PrognosisNextMonthTotal2)
	private TextView tv_PrognosisNextMonthTotal2;
	@BindView(id = R.id.tv_PrognosisNextMonthTotal3)
	private TextView tv_PrognosisNextMonthTotal3;
	@BindView(id = R.id.tv_PrognosisNextMonthCapital1)
	private TextView tv_PrognosisNextMonthCapital1;
	@BindView(id = R.id.tv_PrognosisNextMonthCapital2)
	private TextView tv_PrognosisNextMonthCapital2;
	@BindView(id = R.id.tv_PrognosisNextMonthCapital3)
	private TextView tv_PrognosisNextMonthCapital3;
	@BindView(id = R.id.tv_PrognosisNextMonthInterest1)
	private TextView tv_PrognosisNextMonthInterest1;
	@BindView(id = R.id.tv_PrognosisNextMonthInterest2)
	private TextView tv_PrognosisNextMonthInterest2;
	@BindView(id = R.id.tv_PrognosisNextMonthInterest3)
	private TextView tv_PrognosisNextMonthInterest3;

	// 收款项目(本金+收益)
	@BindView(id = R.id.tv_monthly_report)
	private TextView tv_monthly_report;
	@BindView(id = R.id.tv_ProjectSumTotal)
	private TextView tv_ProjectSumTotal;
	@BindView(id = R.id.tv_ProjectSumCapital)
	private TextView tv_ProjectSumCapital;
	@BindView(id = R.id.tv_ProjectSumInterest)
	private TextView tv_ProjectSumInterest;
	@BindView(id = R.id.tv_info)
	private TextView tv_info;
	
	@BindView(id = R.id.drop_down_menu,click = true)
	private ImageView drop_down_menu;
	
	private TitlePopup titlePopup;

	private String message_title;
	private String message_time;
	private String message_content;
	private String id;
	private String msg_type;

	private MessageMonthlyReportAdapter adapter;
	private List<Project> data;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_message_center_detail);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		UIHelper.setTitleView(this, "", "");

		Intent intent = getIntent();
		message_title = intent.getStringExtra("message_title");
		message_time = intent.getStringExtra("message_time");
		message_content = intent.getStringExtra("message_content");
		msg_type = intent.getStringExtra("msg_type");
		id = intent.getStringExtra("id");
		ActivityUtil.getActivityUtil().addActivity(this);
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

		params = new HttpParams();
		http = new KJHttp();

		data = new ArrayList<Project>();

		getMessageAlread();// 将消息变为已读

		if ("5".equals(msg_type)) {
			sv_monthly_report.setVisibility(View.VISIBLE);
			getMonthReport(message_title);// 获得月报数据
		} else {
			tv_message_title.setText(message_title);
			tv_message_time.setText(message_time);
			tv_message_content.setText(message_content);
			sv_monthly_report.setVisibility(View.GONE);
		}
	}

	private void getMessageAlread() {
		params.put("id", id);
		http.post(AppConstants.MESSAGE_CENTER_ALREAD, params, new HttpCallBack(
				MessageCenterDetailActivity.this) {

			@Override
			public void failure(JSONObject ret) {
				super.failure(ret);
			}

			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					ret.getBoolean("boo");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void onFinish() {
				super.onFinish();
			}
		});
	}

	private void getMonthReport(String date) {
		params.put("sid", AppVariables.sid);
		params.put("month", date);
		http.post(AppConstants.MESSAGE_MONTHLY_REPORT, params,
				new HttpCallBack(MessageCenterDetailActivity.this) {

					@Override
					public void success(JSONObject ret) {
						super.success(ret);
						try {

							tv_message_title.setText(ret.getString("MonthCn")
									+ "账单");// 月份
							tv_message_time.setText(message_time);// 时间
							tv_message_content.setText("本月收益"
									+ ret.getString("Income") + "元（"
									+ ret.getString("FirstDay") + "~"
									+ ret.getString("LastDay") + "）");

							// 金融资产
							tv_FirstDay.setText(ret.getString("FirstDay"));
							tv_LastDay.setText(ret.getString("LastDay"));
							tv_TotalFirstDay.setText(ret
									.getString("TotalFirstDay"));// 月初总资产
							tv_TenderFirstDay.setText(ret
									.getString("TenderFirstDay"));// 月初项目资产
							tv_AccountFirstDay.setText(ret
									.getString("AccountFirstDay"));// 月初账户余额
							tv_AccumulateFirstDay.setText(ret
									.getString("AccumulateFirstDay"));// 月初积分
							tv_TotalLastDay.setText(ret
									.getString("TotalLastDay"));// 月末总资产
							tv_TenderLastDay.setText(ret
									.getString("TenderLastDay"));// 月末项目资产
							tv_AccountLastDay.setText(ret
									.getString("AccountLastDay"));// 月末账户余额
							tv_AccumulateLastDay.setText(ret
									.getString("AccumulateLastDay"));// 月末积分
							if (Double.parseDouble(ret.getString("TotalChange")) == 0) {
								tv_TotalChange.setText(ret
										.getString("TotalChange"));// 总资产变化值
								tv_TotalChange.setTextColor(Color.rgb(130, 130,
										130));
							} else if (Double.parseDouble(ret
									.getString("TotalChange")) > 0) {
								tv_TotalChange.setText("+"
										+ ret.getString("TotalChange"));// 总资产变化值
								tv_TotalChange.setTextColor(Color.rgb(105, 188,
										116));
							} else {
								tv_TotalChange.setText(ret
										.getString("TotalChange"));// 总资产变化值
								tv_TotalChange.setTextColor(Color.rgb(252, 104,
										92));
							}
							if (Double.parseDouble(ret
									.getString("TenderChange")) == 0) {
								tv_TenderChange.setText(ret
										.getString("TenderChange"));// 项目资产变化值
								tv_TenderChange.setTextColor(Color.rgb(130,
										130, 130));
							} else if (Double.parseDouble(ret
									.getString("TenderChange")) > 0) {
								tv_TenderChange.setText("+"
										+ ret.getString("TenderChange"));// 项目资产变化值
								tv_TenderChange.setTextColor(Color.rgb(105,
										188, 116));
							} else {
								tv_TenderChange.setText(ret
										.getString("TenderChange"));// 项目资产变化值
								tv_TenderChange.setTextColor(Color.rgb(252,
										104, 92));
							}
							if (Double.parseDouble(ret
									.getString("AccountChange")) == 0) {
								tv_AccountChange.setText(ret
										.getString("AccountChange"));// 账户余额变化值
								tv_AccountChange.setTextColor(Color.rgb(130,
										130, 130));
							} else if (Double.parseDouble(ret
									.getString("AccountChange")) > 0) {
								tv_AccountChange.setText("+"
										+ ret.getString("AccountChange"));// 账户余额变化值
								tv_AccountChange.setTextColor(Color.rgb(105,
										188, 116));
							} else {
								tv_AccountChange.setText(ret
										.getString("AccountChange"));// 账户余额变化值
								tv_AccountChange.setTextColor(Color.rgb(252,
										104, 92));
							}
							if (Double.parseDouble(ret
									.getString("AccumulateChange")) == 0) {
								tv_AccumulateChange.setText(ret
										.getString("AccumulateChange"));// 积分变化值
								tv_AccumulateChange.setTextColor(Color.rgb(130,
										130, 130));
							} else if (Double.parseDouble(ret
									.getString("AccumulateChange")) > 0) {
								tv_AccumulateChange.setText("+"
										+ ret.getString("AccumulateChange"));// 积分变化值
								tv_AccumulateChange.setTextColor(Color.rgb(105,
										188, 116));
							} else {
								tv_AccumulateChange.setText(ret
										.getString("AccumulateChange"));// 积分变化值
								tv_AccumulateChange.setTextColor(Color.rgb(252,
										104, 92));
							}
							tv_cash_volume.setText("(本月获得"
									+ ret.getString("RedPacketReceived")
									+ "元现金券，使用"
									+ ret.getString("RedPacketUsed")
									+ "元现金券，过期"
									+ ret.getString("RedPacketExpire")
									+ "元现金券)");

							// 金融资产变化
							tv_FirstDay2.setText(ret.getString("FirstDay"));
							tv_LastDay2.setText(ret.getString("LastDay"));
							tv_TotalFirstDay2.setText(ret
									.getString("TotalFirstDay"));// 月初总资产
							tv_Recharge.setText(ret.getString("Recharge"));// 充值
							tv_Withdraw.setText(ret.getString("Withdraw"));// 提现
							tv_IncomeTender.setText(ret
									.getString("IncomeTender"));// 投资项目
							tv_RedPacketUsed.setText(ret
									.getString("RedPacketUsed"));// 现金券抵扣
							tv_IncomeBack.setText(ret.getString("IncomeBack"));// 返现
							tv_TotalLastDay2.setText(ret
									.getString("TotalLastDay"));// 月末总资产
							if (Double.parseDouble(ret.getString("TotalChange")) == 0) {
								tv_TotalChange2.setText(ret
										.getString("TotalChange"));// 总资产变化值
								tv_TotalChange2.setTextColor(Color.rgb(130,
										130, 130));
							} else if (Double.parseDouble(ret
									.getString("TotalChange")) > 0) {
								tv_TotalChange2.setText("+"
										+ ret.getString("TotalChange"));// 总资产变化值
								tv_TotalChange2.setTextColor(Color.rgb(105,
										188, 116));
							} else {
								tv_TotalChange2.setText(ret
										.getString("TotalChange"));// 总资产变化值
								tv_TotalChange2.setTextColor(Color.rgb(252,
										104, 92));
							}

							// 积分变化
							tv_FirstDay3.setText(ret.getString("FirstDay"));
							tv_LastDay3.setText(ret.getString("LastDay"));
							tv_AccumulateFirstDay2.setText(ret
									.getString("AccumulateFirstDay"));// 月初积分
							if (Double.parseDouble(ret
									.getString("AccumulateReceived")) == 0) {
								tv_AccumulateReceived.setText(ret
										.getString("AccumulateReceived"));// 获得积分
								tv_AccumulateReceived.setTextColor(Color.rgb(
										130, 130, 130));
							} else if (Double.parseDouble(ret
									.getString("AccumulateReceived")) > 0) {
								tv_AccumulateReceived.setText("+"
										+ ret.getString("AccumulateReceived"));// 获得积分
								tv_AccumulateReceived.setTextColor(Color.rgb(
										105, 188, 116));
							} else {
								tv_AccumulateReceived.setText(ret
										.getString("AccumulateReceived"));// 获得积分
								tv_AccumulateReceived.setTextColor(Color.rgb(
										252, 104, 92));
							}
							if (Double.parseDouble(ret
									.getString("AccumulateUsed")) == 0) {
								tv_AccumulateUsed.setText(ret
										.getString("AccumulateUsed"));// 花费积分
								tv_AccumulateUsed.setTextColor(Color.rgb(130,
										130, 130));
							} else if (Double.parseDouble(ret
									.getString("AccumulateUsed")) > 0) {
								tv_AccumulateUsed.setText("+"
										+ ret.getString("AccumulateUsed"));// 花费积分
								tv_AccumulateUsed.setTextColor(Color.rgb(105,
										188, 116));
							} else {
								tv_AccumulateUsed.setText(ret
										.getString("AccumulateUsed"));// 花费积分
								tv_AccumulateUsed.setTextColor(Color.rgb(252,
										104, 92));
							}
							tv_AccumulateLastDay2.setText(ret
									.getString("AccumulateLastDay"));// 月末积分
							if (Double.parseDouble(ret
									.getString("AccumulateChange")) == 0) {
								tv_AccumulateChange2.setText(ret
										.getString("AccumulateChange"));// 积分变化值
								tv_AccumulateChange2.setTextColor(Color.rgb(
										130, 130, 130));
							} else if (Double.parseDouble(ret
									.getString("AccumulateChange")) > 0) {
								tv_AccumulateChange2.setText("+"
										+ ret.getString("AccumulateChange"));// 积分变化值
								tv_AccumulateChange2.setTextColor(Color.rgb(
										105, 188, 116));
							} else {
								tv_AccumulateChange2.setText(ret
										.getString("AccumulateChange"));// 积分变化值
								tv_AccumulateChange2.setTextColor(Color.rgb(
										252, 104, 92));
							}

							// 未来三月预估
							tv_mouth1.setText(ret
									.getString("PrognosisNextMonthTitle1"));// 第一个月日期
							tv_mouth2.setText(ret
									.getString("PrognosisNextMonthTitle2"));// 第二个月日期
							tv_mouth3.setText(ret
									.getString("PrognosisNextMonthTitle3"));// 第三个月日期
							tv_PrognosisNextMonthTotal1.setText(ret
									.getString("PrognosisNextMonthTotal1"));// 第一个月预测收款额
							tv_PrognosisNextMonthTotal2.setText(ret
									.getString("PrognosisNextMonthTotal2"));// 第二个月预测收款额
							tv_PrognosisNextMonthTotal3.setText(ret
									.getString("PrognosisNextMonthTotal3"));// 第三个月预测收款额
							tv_PrognosisNextMonthCapital1.setText(ret
									.getString("PrognosisNextMonthCapital1"));// 第一个月预测本金
							tv_PrognosisNextMonthCapital2.setText(ret
									.getString("PrognosisNextMonthCapital2"));// 第二个月预测本金
							tv_PrognosisNextMonthCapital3.setText(ret
									.getString("PrognosisNextMonthCapital3"));// 第三个月预测本金
							if (Double.parseDouble(ret
									.getString("PrognosisNextMonthInterest1")) == 0) {
								tv_PrognosisNextMonthInterest1.setText(ret
										.getString("PrognosisNextMonthInterest1"));// 第一个月预测收益
								tv_PrognosisNextMonthInterest1
										.setTextColor(Color.rgb(130, 130, 130));
							} else if (Double.parseDouble(ret
									.getString("PrognosisNextMonthInterest1")) > 0) {
								tv_PrognosisNextMonthInterest1.setText("+"
										+ ret.getString("PrognosisNextMonthInterest1"));// 第一个月预测收益
								tv_PrognosisNextMonthInterest1
										.setTextColor(Color.rgb(105, 188, 116));
							} else {
								tv_PrognosisNextMonthInterest1.setText(ret
										.getString("PrognosisNextMonthInterest1"));// 第一个月预测收益
								tv_PrognosisNextMonthInterest1
										.setTextColor(Color.rgb(252, 104, 92));
							}
							if (Double.parseDouble(ret
									.getString("PrognosisNextMonthInterest2")) == 0) {
								tv_PrognosisNextMonthInterest2.setText(ret
										.getString("PrognosisNextMonthInterest2"));// 第二个月预测收益
								tv_PrognosisNextMonthInterest2
										.setTextColor(Color.rgb(130, 130, 130));
							} else if (Double.parseDouble(ret
									.getString("PrognosisNextMonthInterest2")) > 0) {
								tv_PrognosisNextMonthInterest2.setText("+"
										+ ret.getString("PrognosisNextMonthInterest2"));// 第二个月预测收益
								tv_PrognosisNextMonthInterest2
										.setTextColor(Color.rgb(105, 188, 116));
							} else {
								tv_PrognosisNextMonthInterest2.setText(ret
										.getString("PrognosisNextMonthInterest2"));// 第二个月预测收益
								tv_PrognosisNextMonthInterest2
										.setTextColor(Color.rgb(252, 104, 92));
							}
							if (Double.parseDouble(ret
									.getString("PrognosisNextMonthInterest3")) == 0) {
								tv_PrognosisNextMonthInterest3.setText(ret
										.getString("PrognosisNextMonthInterest3"));// 第三个月预测收益
								tv_PrognosisNextMonthInterest3
										.setTextColor(Color.rgb(130, 130, 130));
							} else if (Double.parseDouble(ret
									.getString("PrognosisNextMonthInterest3")) > 0) {
								tv_PrognosisNextMonthInterest3.setText("+"
										+ ret.getString("PrognosisNextMonthInterest3"));// 第三个月预测收益
								tv_PrognosisNextMonthInterest3
										.setTextColor(Color.rgb(105, 188, 116));
							} else {
								tv_PrognosisNextMonthInterest3.setText(ret
										.getString("PrognosisNextMonthInterest3"));// 第三个月预测收益
								tv_PrognosisNextMonthInterest3
										.setTextColor(Color.rgb(252, 104, 92));
							}

							// 收款项目(本金+收益)
							tv_monthly_report.setText(ret.getString("MonthCn")
									+ " 项目收款（本金+收益）");
							tv_ProjectSumTotal.setText(ret
									.getString("ProjectSumTotal"));// 项目总计 收款额
							tv_ProjectSumCapital.setText(ret
									.getString("ProjectSumCapital"));// 项目总计 本金
							if (Double.parseDouble(ret
									.getString("ProjectSumInterest")) == 0) {
								tv_ProjectSumInterest.setText(ret
										.getString("ProjectSumInterest"));// 项目总计
																			// 收益
								tv_ProjectSumInterest.setTextColor(Color.rgb(
										130, 130, 130));
							} else if (Double.parseDouble(ret
									.getString("ProjectSumInterest")) > 0) {
								tv_ProjectSumInterest.setText("+"
										+ ret.getString("ProjectSumInterest"));// 项目总计
																				// 收益
								tv_ProjectSumInterest.setTextColor(Color.rgb(
										105, 188, 116));
							} else {
								tv_ProjectSumInterest.setText(ret
										.getString("ProjectSumInterest"));// 项目总计
																			// 收益
								tv_ProjectSumInterest.setTextColor(Color.rgb(
										252, 104, 92));
							}
							JSONObject articles = ret.getJSONObject("orders");
							data = new ProjectList(articles.getJSONArray("items")).getList();
							adapter = new MessageMonthlyReportAdapter(MessageCenterDetailActivity.this, data);
							lv_monthly_report.setAdapter(adapter);
							ListViewHight.setListViewHeightBasedOnChildren(lv_monthly_report);

							tv_info.setText("以上数据截止到"
									+ ret.getString("LastDay")
									+ "，您对账单有任何疑问，请联系客服咨询（400-059-6636）");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void failure(JSONObject ret) {
						super.failure(ret);
					}

					@Override
					public void onFinish() {
						super.onFinish();
					}
				});
	}

	
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
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
       	default:
			break;
		}
	}
}
