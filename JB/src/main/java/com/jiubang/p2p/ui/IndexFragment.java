package com.jiubang.p2p.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.adapter.IndexAdapter;
import com.jiubang.p2p.bean.Product;
import com.jiubang.p2p.bean.ProductList_new;
import com.jiubang.p2p.map.TypeArray;
import com.jiubang.p2p.utils.ToastCommom;
import com.jiubang.p2p.view.ListViewForScrollView;
import com.jiubang.p2p.view.TasksCompletedView;
/**
* 首页fragment
* */
public class IndexFragment extends Fragment implements OnClickListener{
	private final String TAG = "首页";

	//通知小喇叭
	private TextView tv_message;
	//红点提醒
	private ImageView iv_red_point;
	//新手计划
	private TextView tv_products_title;
	//预期年化利率内容
	private TextView tv_finance_interest_rate;
	//期限时间内容
	private TextView tv_products_term;
	//期限时间
	private TextView tv_products_unit;
	//起投钱数
	private TextView tv_min_tender_amount;
	//抢购还款中已还款自定义圆圈
	private TasksCompletedView tcv_progress;
	//理财计划A
	private TextView tv_financialPlanA;
	//理财计划B
	private TextView tv_financialPlanB;
	//理财计划C
	private TextView tv_financialPlanC;
	//轻松投查看更多
	private TextView tv_financial_plan;
	private LinearLayout ll_newhand;
	// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)
	private ImageView iv_ti;
	// 产品类型 现金
	private ImageView iv_xian;
	//理财计划A
	private LinearLayout ll_index_plan_a;
	//理财计划B
	private LinearLayout ll_index_plan_b;
	//理财计划C
	private LinearLayout ll_index_plan_c;
	//每日签到
	private TextView tv_sign;
	private TextView tv_text;
	private TextView tv_qiandao;
	//优惠券
	private FrameLayout fl_coupon;
	//安全保障
	private TextView tv_safe;
	//邀请有礼
	private TextView tv_invitation;
	//签到成功popwindow
	private LinearLayout ll_pop_sign;
	//签到背景图片
	private ImageView iv_background;
	//确定签到
	private Button btn_ok;
	//直投直贷专区 查看更多
	private TextView tv_more;
	
	private View v;
	//获取数据
	private HttpParams params;
	private KJHttp http;
	//理财产品实体类
	private List<Product> data;
	//直投直贷专区listview列表
	private ListViewForScrollView listview;
	//直投直贷专区listview列表adapter
	private IndexAdapter indexAdapter;
	//id
	private int new_products_id;
	//标题
	private String new_products_title;
	private int new_products_exp_type;// 新手标类型
	//是否为新手
	private int new_hand = 0;
	// 用户等级
	private int uid_level = 0;
	//从MainAcitvity发送过来的广播
	private MyBroadcastReceiver broadcastReceiver;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_index, null);
		
		getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		initView();
		data = new ArrayList<Product>();
		http = new KJHttp();
		params = new HttpParams();

		//注册广播
		registerBoradcastReceiver();
		return v;
	}

	//获取数据
	private void getData() {
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.INDEX, params, httpCallback);
	}

	@Override
	public void onResume() {
		super.onResume();
		getData();
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		getData();
	}
	
	private void initView() {
		//通知小喇叭
		tv_message = (TextView) v.findViewById(R.id.tv_message);
		//红点提醒
		iv_red_point = (ImageView) v.findViewById(R.id.iv_red_point);
		//新手计划
		tv_products_title = (TextView) v.findViewById(R.id.tv_products_title);
		//新手专享预期年化利率内容
		tv_finance_interest_rate = (TextView) v.findViewById(R.id.tv_finance_interest_rate);
		tv_products_term = (TextView) v.findViewById(R.id.tv_products_term);
		//期限时间内容
		tv_products_unit = (TextView) v.findViewById(R.id.tv_products_unit);
		//起投钱数
		tv_min_tender_amount = (TextView) v.findViewById(R.id.tv_min_tender_amount);
		//理财计划A
		tv_financialPlanA = (TextView) v.findViewById(R.id.tv_financialPlanA);
		//理财计划B
		tv_financialPlanB = (TextView) v.findViewById(R.id.tv_financialPlanB);
		//理财计划C
		tv_financialPlanC = (TextView) v.findViewById(R.id.tv_financialPlanC);
		ll_index_plan_a = (LinearLayout) v.findViewById(R.id.ll_index_plan_a);
		ll_index_plan_b = (LinearLayout) v.findViewById(R.id.ll_index_plan_b);
		ll_index_plan_c = (LinearLayout) v.findViewById(R.id.ll_index_plan_c);
		ll_newhand = (LinearLayout) v.findViewById(R.id.ll_newhand);
		// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)
		iv_ti = (ImageView) v.findViewById(R.id.iv_ti);
		// 产品类型 现金
		iv_xian = (ImageView) v.findViewById(R.id.iv_xian);
		//每日签到
		tv_sign = (TextView) v.findViewById(R.id.tv_sign);
		tv_text = (TextView) v.findViewById(R.id.tv_text);
		fl_coupon = (FrameLayout) v.findViewById(R.id.fl_coupon);
		//安全保障
		tv_safe = (TextView) v.findViewById(R.id.tv_safe);
		//邀请有礼
		tv_invitation = (TextView) v.findViewById(R.id.tv_invitation);
		//每日签到
		tv_sign.setOnClickListener(this);
		//优惠券
		fl_coupon.setOnClickListener(this);
		//安全保障
		tv_safe.setOnClickListener(this);
		//邀请有礼
		tv_invitation.setOnClickListener(this);
		//理财计划abc
		ll_index_plan_a.setOnClickListener(this);
		ll_index_plan_b.setOnClickListener(this);
		ll_index_plan_c.setOnClickListener(this);

		//轻松投查看更多
		tv_financial_plan = (TextView) v.findViewById(R.id.tv_financial_plan);
		tv_financial_plan.setOnClickListener(this);
		
		// 签到
		tv_qiandao = (TextView) v.findViewById(R.id.tv_qiandao);
		//签到背景图片
		iv_background = (ImageView) v.findViewById(R.id.iv_background);
		//签到成功popwindow
		ll_pop_sign = (LinearLayout) v.findViewById(R.id.ll_pop_sign);
		//确定签到
		btn_ok = (Button) v.findViewById(R.id.btn_ok);
		//签到成功popwindow
		ll_pop_sign.setOnClickListener(this);
		//确定签到
		btn_ok.setOnClickListener(this);
		//抢购还款中已还款圆圈
		tcv_progress = (TasksCompletedView) v.findViewById(R.id.tcv_progress);
		tcv_progress.setOnClickListener(this);
		//直投直贷专区listview列表
		listview = (ListViewForScrollView) v.findViewById(R.id.listview);
		//直投直贷专区 查看更多
		tv_more = (TextView) v.findViewById(R.id.tv_more);
		tv_more.setOnClickListener(this);
		ll_newhand.setOnClickListener(this);
	}

	//数据解析
	private HttpCallBack httpCallback = new HttpCallBack(getActivity()) {
		public void onSuccess(String t) {
			try {
				JSONObject ret = new JSONObject(t);

				//一个TextView分不同颜色字段
				StringBuilder message = new StringBuilder();
				message.append("<font color=\"#333333\">累计会员：</font>");
				message.append("<font color=\"#ff772d\">%s人</font>");
				message.append("<font color=\"#333333\">&nbsp;累计成交：</font>");
				message.append("<font color=\"#ff772d\">%s元</font>");
				//通知小喇叭
				tv_message.setText(Html.fromHtml(String.format(message.toString(), ret.getString("member_cnt"), ret.getString("tender_amount_all"))));
				//是否为新手
				new_hand = ret.getInt("new_hand");
				//等级
				uid_level = ret.getInt("uid_level");

				//红点提醒操作
				if("0".equals(ret.getString("xianjin_jiaxi_coupon"))){
					iv_red_point.setVisibility(View.GONE);
				} else {
					iv_red_point.setVisibility(View.VISIBLE);
				}

				//每日签到
				if (ret.getBoolean("is_sign")) {
					tv_sign.setText("今日已签到");
				} else {
					tv_sign.setText("每日签到");
				}
				
				// 新手专享
				JSONObject new_products = ret.getJSONObject("new_financeProduct");
				new_products_id = new_products.getInt("oid_finance_products_id");
				new_products_title = new_products.getString("new_products_title");
				//新手计划
				tv_products_title.setText(new_products_title);
				new_products_exp_type = new_products.getInt("new_products_exp_type");
				
				// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)
				if(new_products_exp_type == 2) {
					iv_ti.setVisibility(View.VISIBLE);// 产品类型 体验金
					iv_xian.setVisibility(View.VISIBLE);// 产品类型 现金
				} else if(new_products_exp_type == 1) {
					iv_ti.setVisibility(View.VISIBLE);
					iv_xian.setVisibility(View.GONE);
				} else {
					iv_ti.setVisibility(View.GONE);
					iv_xian.setVisibility(View.GONE);
				}

				//新手专享预期年化利率内容
				String str = new_products.getString("new_finance_interest_rate") + "%";
				SpannableStringBuilder builder = new SpannableStringBuilder(str);
				CharacterStyle cs = new AbsoluteSizeSpan(32);//字体大小
				builder.setSpan(cs, str.length() - 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				//预期年化利率内容
				tv_finance_interest_rate.setText(builder);
				//期限时间内容
				tv_products_term.setText(new_products.getString("new_products_term"));
				//期限时间内容
				tv_products_unit.setText("期限(" + new_products.getString("new_products_unit") + ")");
				//起投钱数
				tv_min_tender_amount.setText(new_products.getString("new_min_tender_amount_show"));
				//抢购还款中已还款圆圈
				tcv_progress.setProgress(new_products.getInt("new_finance_amount_scale"));//进度条
				tcv_progress.setText(TypeArray.status[new_products.getInt("newstatus")]);//圆圈内容
				
				//理财计划A
				tv_financialPlanA.setText(ret.getString("financialPlanA"));
				//理财计划B
				tv_financialPlanB.setText(ret.getString("financialPlanB"));
				//理财计划C
				tv_financialPlanC.setText(ret.getString("financialPlanC"));
				// 精选理财
				data = new ProductList_new(ret.getJSONArray("select_product_list")).getProducts();
				//直投直贷专区listview列表adapter
				indexAdapter = new IndexAdapter(getActivity(), data);
				listview.setAdapter(indexAdapter);

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), R.string.app_data_error, Toast.LENGTH_SHORT).show();
			}
		};
	};

	//点击事件
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
			//每日签到
		case R.id.tv_sign:
			if (!AppVariables.isSignin) {
				intent = new Intent(getActivity(), SigninActivity.class);
				startActivity(intent);
			} else {
				sign();
			}
			break;

			//签到成功popwindow
		case R.id.btn_ok:
			ll_pop_sign.setVisibility(View.GONE);
			tv_sign.setText("今日已签到");
			break;

			//优惠券
		case R.id.fl_coupon:
			if (!AppVariables.isSignin) {
				intent = new Intent(getActivity(), SigninActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(getActivity(), RedActivity.class);
				startActivity(intent);
			}
			break;

			//安全保障
		case R.id.tv_safe:
			intent = new Intent(getActivity(), SecurityActivity.class);
			startActivity(intent);
			break;

			//邀请有礼
		case R.id.tv_invitation:
			if (!AppVariables.isSignin) {
				intent = new Intent(getActivity(), SigninActivity.class);
				startActivity(intent);
			} else {
				
				if (uid_level == 2) {
					intent = new Intent(getActivity(), YanInviteActivity.class);
					intent.putExtra("activity", "account");
					startActivity(intent);
				} else {
					intent = new Intent(getActivity(), NormalInviteActivity.class);
					intent.putExtra("activity", "account");
					startActivity(intent);
				}
				
			}
			break;

			//轻松投查看更多
		case R.id.tv_financial_plan:
		    intent = new Intent(getActivity(), FinancialPlanActivity.class);
		    startActivity(intent);
			break;

			//直投直贷专区查看更多
		case R.id.tv_more:
			// 发送广播 跳转更多
			intent = new Intent();  
            intent.setAction("tab");
            intent.putExtra("tab", "product");
            getActivity().sendBroadcast(intent);
			break;

			//抢购
		case R.id.tcv_progress:
			if (!AppVariables.isSignin) {
				intent = new Intent(getActivity(), SigninActivity.class);
				startActivity(intent);
			} else {
				if (new_hand != 0) {
					ToastCommom toastCommom = ToastCommom.createToastConfig();
					toastCommom.ToastShow(getActivity(), (ViewGroup) v.findViewById(R.id.toast_layout_root), "您已不是新手");
				} else {
					intent = new Intent(getActivity(), TenderActivity.class);
					intent.putExtra("id", new_products_id);// 产品ID
					intent.putExtra("name", new_products_title);// 产品名称
					intent.putExtra("products_exp_type", new_products_exp_type);// 产品类型
					startActivity(intent);
				}
			}
			break;

			//理财计划A
		case R.id.ll_index_plan_a:
			if (!AppVariables.isSignin) {
				intent = new Intent(getActivity(), SigninActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(getActivity(), Plan_ABCActivity.class);
				intent.putExtra("type", "A");
				startActivity(intent);
			}
			break;

			//理财计划B
		case R.id.ll_index_plan_b:
			if (!AppVariables.isSignin) {
				intent = new Intent(getActivity(), SigninActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(getActivity(), Plan_ABCActivity.class);
				intent.putExtra("type", "B");
				startActivity(intent);
			}
			break;

			//理财计划C
		case R.id.ll_index_plan_c:
			if (!AppVariables.isSignin) {
				intent = new Intent(getActivity(), SigninActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(getActivity(), Plan_ABCActivity.class);
				intent.putExtra("type", "C");
				startActivity(intent);
			}
			break;

			//轻松投查看更多
		case R.id.ll_newhand:
			if (!AppVariables.isSignin) {
				intent = new Intent(getActivity(), SigninActivity.class);
				startActivity(intent);
			} else {
				if (new_hand != 0) {
					ToastCommom toastCommom = ToastCommom.createToastConfig();
					toastCommom.ToastShow(getActivity(), (ViewGroup) v.findViewById(R.id.toast_layout_root), "您已不是新手");
				} else {
					intent = new Intent(getActivity(), TenderActivity.class);
					intent.putExtra("id", new_products_id);// 产品ID
					intent.putExtra("name", new_products_title);// 产品名称
					startActivity(intent);
				}
			}
			break;
		}
		
		
	}
	
	/**
	 * 签到
	 * */
	private void sign() {
		http = new KJHttp();
		params = new HttpParams();
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.MY_USER_SIGN, params, new HttpCallBack(getActivity()) {
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
							//签到背景图片
							iv_background.setImageDrawable(getResources().getDrawable(R.drawable.click_60));
							tv_text.setVisibility(View.GONE);
							tv_qiandao.setText("连续签到7天");
						} else {
							//签到背景图片
							iv_background.setImageDrawable(getResources().getDrawable(R.drawable.click_10));
							tv_qiandao.setText("连续签到" + ret.getInt("signCnt") + "天");
						}
						ll_pop_sign.setVisibility(View.VISIBLE);
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
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getActivity().unregisterReceiver(broadcastReceiver);// 注销广播
	}
	
	/**
	 * 注册广播
	 * */
	private void registerBoradcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
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
			case "index":
				getData();
				break;
			}
		}
	}
	
}
