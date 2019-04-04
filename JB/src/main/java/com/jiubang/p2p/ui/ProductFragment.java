package com.jiubang.p2p.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.widget.KJListView;
import com.louding.frame.widget.KJRefreshListener;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.adapter.SlideAdapter;
import com.jiubang.p2p.adapter.ViewHolder;
import com.jiubang.p2p.bean.Product;
import com.jiubang.p2p.bean.ProductList;
import com.jiubang.p2p.map.TypeArray;
import com.jiubang.p2p.utils.NetWorkUtils;
import com.jiubang.p2p.utils.ToastCommom;
import com.jiubang.p2p.view.TasksCompletedView;

@SuppressLint("InflateParams")
@SuppressWarnings("deprecation")
public class ProductFragment extends Fragment {
	private final String TAG = "ProductFragment";
	private View v;
	
	// 工具栏
	private LinearLayout llTopBar;

	// 顶部的四个筛选
	private TextView tv_type;
	private TextView tv_date;
	private TextView tv_huibao;
	private TextView tv_mode;
	private TextView tv_startmoney;

	// 四个弹出窗口
	private PopupWindow poptype;
	private PopupWindow popdate;
	private PopupWindow pophuibao;
	private PopupWindow popmode;
	private PopupWindow popstartmoney;

	// 四个layout
	private View layouttype;
	private View layoutdate;
	private View layouthuibao;
	private View layoutmode;
	private View layoutstartmoney;

	// 四个ListView控件（弹出窗口里）
	private ListView menulisttype;
	private ListView menulistdate;
	private ListView menulisthuibao;
	private ListView menulistmode;
	private ListView menuliststartmoney;

	// 菜单数据项
	private List<Map<String, String>> listtype;
	private List<Map<String, String>> listdate;
	private List<Map<String, String>> listhuibao;
	private List<Map<String, String>> listmode;
	private List<Map<String, String>> liststartmoney;
	
	private TextView empty;
	private View view_alpha;

	private int type_choice;
	private int date_choice;
	private int huibao_choice;
	private int huankuanstyle_choice;
	private int startmoney_choice;

	private KJListView listview;
	private KJHttp http;
	private HttpParams params;

	private SlideAdapter<Product> adapter;
	private List<Product> data;

	private int page = 1;
	private int new_hand = 0;
	private boolean noMoreData;
	private LayoutInflater minflater;
	private Drawable down_active;
	private Drawable down;
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		data = new ArrayList<Product>();
		http = new KJHttp();
		params = new HttpParams();
		minflater = inflater;
		getData(1);
		v = inflater.inflate(R.layout.fragment_product, null);
		tv_type = (TextView) v.findViewById(R.id.tv_type);
		tv_date = (TextView) v.findViewById(R.id.tv_date);
		tv_huibao = (TextView) v.findViewById(R.id.tv_huibao);
		tv_mode = (TextView) v.findViewById(R.id.tv_mode);
		tv_startmoney = (TextView) v.findViewById(R.id.tv_startmoney);
		
		tv_type.setOnClickListener(onClickListener);
		tv_date.setOnClickListener(onClickListener);
		tv_huibao.setOnClickListener(onClickListener);
		tv_mode.setOnClickListener(onClickListener);
		tv_startmoney.setOnClickListener(onClickListener);
		
		llTopBar = (LinearLayout) v.findViewById(R.id.llTopBar);
		empty = (TextView) v.findViewById(R.id.empty);
		view_alpha = v.findViewById(R.id.view_alpha);
		initParam();
		listview = (KJListView) v.findViewById(R.id.listview);
		
		NetWorkUtils.show_wifi_empty(getActivity(), empty);
		
		adapter = new SlideAdapter<Product>(getActivity(),R.layout.item_product) {
			@Override
			public void canvas(ViewHolder holder, Product item) {
				holder.addClick(R.id.percentagepb);
				holder.addClick(R.id.ll_selected_financial);
				TextView nameInfo = holder.getView(R.id.nameInfo);
				
				TextView tv_gain = holder.getView(R.id.gain);
				if("".equals(item.getExtraRate())) {
					String str = item.getGain() + "%";
					SpannableStringBuilder builder = new SpannableStringBuilder(str);
					CharacterStyle cs = new AbsoluteSizeSpan(32);//字体大小
					builder.setSpan(cs, str.length() - 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					tv_gain.setText(builder);
				} else {
					String str = item.getGain() + "%"+ "+" + item.getExtraRate() + "%";
					SpannableStringBuilder builder = new SpannableStringBuilder(str);
					CharacterStyle cs1 = new AbsoluteSizeSpan(32);//字体大小
					CharacterStyle cs2 = new AbsoluteSizeSpan(32);//字体大小
					builder.setSpan(cs1, item.getGain().length(), item.getGain().length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					builder.setSpan(cs2, str.length() - 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					tv_gain.setText(builder);
				}
				
				if (item.getActivity() == 0) {
					nameInfo.setVisibility(View.GONE);
				} else {
					nameInfo.setVisibility(View.VISIBLE);
					nameInfo.setText(" " + item.getNameInfo());
				}
				holder.setText(R.id.deadline, item.getDeadline(), false);
				holder.setText(R.id.deadlinedesc,"期限(" + item.getDeadlinedesc() + ")", false);
				holder.setText(R.id.singlePurchaseLowerLimit, item.getSinglePurchaseLowerLimit_show(), false);
				TasksCompletedView pb = holder.getView(R.id.percentagepb);
				pb.setProgress(item.getPercentage());
				int i = item.getNewstatus();
				pb.setText(TypeArray.status[i]);
				switch (i) {
				case 1:// 还款中
					pb.setCircleColor(getResources().getColor(R.color.app_grey));
					break;
				case 2:// 满标
					pb.setCircleColor(getResources().getColor(R.color.app_grey));
					break;
				case 3:// 预约
					pb.setCircleColor(getResources().getColor(R.color.app_grey));
					break;
				case 4:// 已结束
					pb.setCircleColor(getResources().getColor(R.color.app_grey));
					break;
				case 5:// 投资
					pb.setCircleColor(getResources().getColor(R.color.app_blue));
					break;
				case 6:// 已还款
					pb.setCircleColor(getResources().getColor(R.color.app_grey));
					break;
				case 7:// 审核中
					pb.setCircleColor(getResources().getColor(R.color.app_grey));
					break;
				case 8:// 转让成功
					pb.setCircleColor(getResources().getColor(R.color.app_grey));
					break;
				}

				TextView tv_product_type_name = holder.getView(R.id.tv_product_type_name);
				// 没有活动
				if (item.getConfine() == 0) {
					tv_product_type_name.setVisibility(View.GONE);
				} else {
					tv_product_type_name.setVisibility(View.VISIBLE);
					tv_product_type_name.setText(item.getProduct_type_name());
				}
				TextView name = holder.getView(R.id.name);
				name.setText(item.getName());
				
				ImageView iv_ti = holder.getView(R.id.iv_ti);
				ImageView iv_xian = holder.getView(R.id.iv_xian);
				// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)
				if(item.getProducts_exp_type() == 2) {
					iv_ti.setVisibility(View.VISIBLE);
					iv_xian.setVisibility(View.VISIBLE);
				} else if(item.getProducts_exp_type() == 1) {
					iv_ti.setVisibility(View.VISIBLE);
					iv_xian.setVisibility(View.GONE);
				} else {
					iv_ti.setVisibility(View.GONE);
					iv_xian.setVisibility(View.GONE);
				}
				
			}

			@Override
			public void click(int id, List<Product> list, int position,
					ViewHolder viewHolder) {
				Intent intent;
				if (list.get(position).getConfine() == 1) {
					if (!AppVariables.isSignin) {
						intent = new Intent(getActivity(), SigninActivity.class);
						startActivity(intent);
					} else {
						if (new_hand != 0) {
							ToastCommom toastCommom = ToastCommom.createToastConfig();
							toastCommom.ToastShow(getActivity(), (ViewGroup) v.findViewById(R.id.toast_layout_root), "您已不是新手");
						} else {
							if (list.get(position).getIs_transfer() == 1) {// 债权转让产品
								intent = new Intent(getActivity(), TenderTransferActivity.class);
								intent.putExtra("id", list.get(position).getId());// 产品ID
								intent.putExtra("name", list.get(position).getName());// 产品名称
								intent.putExtra("oid_transfer_id", Integer.parseInt(list.get(position).getOid_transfer_id()));// 债权产品ID
							} else {// 不是债权转让产品
								intent = new Intent(getActivity(), TenderActivity.class);
								intent.putExtra("id", list.get(position).getId());// 产品ID
								intent.putExtra("name", list.get(position).getName());// 产品名称
								intent.putExtra("products_exp_type", list.get(position).getProducts_exp_type());// 产品类型
							}
							startActivity(intent);
						}
					}
				} else {
					if (list.get(position).getIs_transfer() == 1) {// 债权转让产品
						intent = new Intent(getActivity(), TenderTransferActivity.class);
						intent.putExtra("id", list.get(position).getId());// 产品ID
						intent.putExtra("name", list.get(position).getName());// 产品名称
						intent.putExtra("oid_transfer_id", Integer.parseInt(list.get(position).getOid_transfer_id()));// 债权产品ID
					} else {// 不是债权转让产品
						intent = new Intent(getActivity(), TenderActivity.class);
						intent.putExtra("id", list.get(position).getId());// 产品ID
						intent.putExtra("name", list.get(position).getName());// 产品名称
						intent.putExtra("products_exp_type", list.get(position).getProducts_exp_type());// 产品类型
					}
					startActivity(intent);
				}
			}
		};
		adapter.setList(data);
		listview.setAdapter(adapter);
		listview.setOnRefreshListener(refreshListener);
//		listview.setEmptyView(v.findViewById(R.id.empty));
		return v;
	}

	private void getData(int page) {
		params.put("page", page);
		params.put("sid", AppVariables.sid);
		params.put("loginVersionName", "Android"+ getAppVersionName(getActivity()));
		http.post(AppConstants.PRODUCTS, params, httpCallback);
		Log.e(TAG, "getChoice: "+AppConstants.PRODUCTS+"加"+params+"加"+choiceCallback );
	}

	/**
	 * 返回当前程序版本名
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}

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

	private HttpCallBack httpCallback = new HttpCallBack(getActivity()) {
		public void onSuccess(String t) {
			try {
				JSONObject ret = new JSONObject(t);
				int state = ret.getInt("status");
				new_hand = ret.getInt("new_hand");
				if (state != 0) {
					listview.hideFooter();
					noMoreData = true;
				} else {
					listview.showFooter();
					noMoreData = false;
					page = ret.getInt("current_page");
					if (page == 1) {
						// 因为后来要求改变，所以需要数据第一位为空，显示轮播图。在构造方法里面做处理。
						data = new ProductList(ret.getJSONArray("product_list")).getProducts();
						if(data.size() <= 1){
							empty.setVisibility(View.VISIBLE);
						} else {
							empty.setVisibility(View.GONE);
						}
					} else {
						data = new ProductList(data, ret.getJSONArray("product_list")).getProducts();
						if(data.size() <= 1){
							empty.setVisibility(View.VISIBLE);
						} else {
							empty.setVisibility(View.GONE);
						}
					}
					adapter.setList(data);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), R.string.app_data_error,Toast.LENGTH_SHORT).show();
			}
		};

		public void onFinish() {
			listview.stopRefreshData();
		};
	};

	private void getChoice(int page) {
		params.put("page", page);
		params.put("sid", AppVariables.sid);
		params.put("sType", type_choice);
		params.put("sTerm", date_choice);
		params.put("sRate", huibao_choice);
		params.put("sRecover", huankuanstyle_choice);
		params.put("sStart", startmoney_choice);
		http.post(AppConstants.PRODUCTS, params, choiceCallback);

	}

	private HttpCallBack choiceCallback = new HttpCallBack(getActivity()) {
		public void onSuccess(String t) {
			try {
				JSONObject ret = new JSONObject(t);
				int state = ret.getInt("status");
				new_hand = ret.getInt("new_hand");
				if (state != 0) {
					listview.hideFooter();
					noMoreData = true;
				} else {
					listview.showFooter();
					noMoreData = false;
					page = ret.getInt("current_page");
					if (page == 1) {
						// 因为后来要求改变，所以需要数据第一位为空，显示轮播图。在构造方法里面做处理。
						data = new ProductList(ret.getJSONArray("product_list")).getProducts();
						if(data.size() <= 1){// ProductList里面默认加了一个对象 所以data不可能等于0
							empty.setVisibility(View.VISIBLE);
						} else {
							empty.setVisibility(View.GONE);
						}
					} else {
						data = new ProductList(data,ret.getJSONArray("product_list")).getProducts();
						if(data.size() <= 1){
							empty.setVisibility(View.VISIBLE);
						} else {
							empty.setVisibility(View.GONE);
						}
					}
					adapter.setList(data);
					listview.smoothScrollToPosition(0);  
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), R.string.app_data_error,Toast.LENGTH_SHORT).show();
			}
		};

		public void onFinish() {
			listview.stopRefreshData();
		};
	};

	private void initParam() {
		down_active = getResources().getDrawable(R.drawable.down_active);
		down = getResources().getDrawable(R.drawable.down);
		down_active.setBounds(0, 0, down_active.getMinimumWidth(),down_active.getMinimumHeight());
		down.setBounds(0, 0, down.getMinimumWidth(), down.getMinimumHeight());

		listtype = TypeArray.getFilterList("type");
		listdate = TypeArray.getFilterList("date");
		listhuibao = TypeArray.getFilterList("huibao");
		listmode = TypeArray.getFilterList("mode");
		liststartmoney = TypeArray.getFilterList("startmoney");
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_type:
				view_alpha.setVisibility(View.VISIBLE);
				
				setColor(R.id.tv_type);
				
				if (poptype != null && poptype.isShowing()) {
					poptype.dismiss();// 如果这个窗口正在显示，就关闭
				} else {
					layouttype = minflater.inflate(R.layout.product_choice_listview, null);
					menulisttype = (ListView) layouttype.findViewById(R.id.choicelist);
					SimpleAdapter listAdapter = new SimpleAdapter(getActivity(), listtype, R.layout.product_choice_item, new String[] { "item" }, new int[] { R.id.choice_item });
					menulisttype.setAdapter(listAdapter);
					// 点击listView 中的item处理
					menulisttype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// 改变顶部对应TextView 的值
							String strItem = listtype.get(position).get("item");
							tv_type.setText(strItem);
							if (poptype != null && poptype.isShowing()) {
								poptype.dismiss();
							}
							if(position == 0){
								tv_type.setText("项目类型");
							}
							type_choice = position;
							getChoice(1);
						}
					});
					poptype = newPopupWindow(layouttype, tv_type);
				}
				break;
			case R.id.tv_date:
				view_alpha.setVisibility(View.VISIBLE);
				
				setColor(R.id.tv_date);

				if (popdate != null && popdate.isShowing()) {
					popdate.dismiss();// 如果这个窗口正在显示，就关闭
				} else {
					layoutdate = minflater.inflate(R.layout.product_choice_listview, null);
					menulistdate = (ListView) layoutdate.findViewById(R.id.choicelist);
					SimpleAdapter listAdapter = new SimpleAdapter(getActivity(), listdate, R.layout.product_choice_item, new String[] { "item" }, new int[] { R.id.choice_item });
					menulistdate.setAdapter(listAdapter);
					// 点击listView 中的item处理
					menulistdate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// 改变顶部对应TextView 的值
							String strItem = listdate.get(position).get("item");
							tv_date.setText(strItem);
							if (popdate != null && popdate.isShowing()) {
								popdate.dismiss();
							}
							if(position == 0){
								tv_date.setText("项目期限");
							}
							date_choice = position;
							getChoice(1);
						}
					});
					popdate = newPopupWindow(layoutdate, tv_date);
				}
				break;
			case R.id.tv_huibao:
				view_alpha.setVisibility(View.VISIBLE);
				
				setColor(R.id.tv_huibao);
				
				if (pophuibao != null && pophuibao.isShowing()) {
					pophuibao.dismiss();// 如果这个窗口正在显示，就关闭
				} else {
					layouthuibao = minflater.inflate(R.layout.product_choice_listview, null);
					menulisthuibao = (ListView) layouthuibao.findViewById(R.id.choicelist);
					SimpleAdapter listAdapter = new SimpleAdapter(getActivity(), listhuibao, R.layout.product_choice_item, new String[] { "item" }, new int[] { R.id.choice_item });
					menulisthuibao.setAdapter(listAdapter);
					// 点击listView 中的item处理
					menulisthuibao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// 改变顶部对应TextView 的值
							String strItem = listhuibao.get(position).get("item");
							tv_huibao.setText(strItem);
							if (pophuibao != null&& pophuibao.isShowing()) {
								pophuibao.dismiss();
							}
							if(position == 0){
								tv_huibao.setText("收益回报");
							}
							huibao_choice = position;
							getChoice(1);
						}
					});
					pophuibao = newPopupWindow(layouthuibao, tv_huibao);
				}
				break;
			case R.id.tv_mode:
				view_alpha.setVisibility(View.VISIBLE);
				
				setColor(R.id.tv_mode);
				
				if (popmode != null && popmode.isShowing()) {
					popmode.dismiss();// 如果这个窗口正在显示，就关闭
				} else {
					layoutmode = minflater.inflate(R.layout.product_choice_listview, null);
					menulistmode = (ListView) layoutmode.findViewById(R.id.choicelist);
					SimpleAdapter listAdapter = new SimpleAdapter(getActivity(), listmode, R.layout.product_choice_item, new String[] { "item" }, new int[] { R.id.choice_item });
					menulistmode.setAdapter(listAdapter);
					// 点击listView 中的item处理
					menulistmode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,View view, int position, long id) {
							// 改变顶部对应TextView 的值
							String strItem = listmode.get(position).get("item");
							tv_mode.setText(strItem);
							if (popmode != null&& popmode.isShowing()) {
								popmode.dismiss();
							}
							if(position == 0){
								tv_mode.setText("还款方式");
							}
							huankuanstyle_choice = position;
							getChoice(1);
						}
					});
					popmode = newPopupWindow(layoutmode, tv_mode);
				}
				break;
			case R.id.tv_startmoney:
				view_alpha.setVisibility(View.VISIBLE);
				
				setColor(R.id.tv_startmoney);
				
				if (popstartmoney != null && popstartmoney.isShowing()) {
					popstartmoney.dismiss();// 如果这个窗口正在显示，就关闭
				} else {
					layoutstartmoney = minflater.inflate(R.layout.product_choice_listview, null);
					menuliststartmoney = (ListView) layoutstartmoney.findViewById(R.id.choicelist);
					SimpleAdapter listAdapter = new SimpleAdapter(getActivity(), liststartmoney, R.layout.product_choice_item, new String[] { "item" }, new int[] { R.id.choice_item });
					menuliststartmoney.setAdapter(listAdapter);
					// 点击listView 中的item处理
					menuliststartmoney.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// 改变顶部对应TextView 的值
							String strItem = liststartmoney.get(position).get("item");
							tv_startmoney.setText(strItem);
							if (popstartmoney != null && popstartmoney.isShowing()) {
								popstartmoney.dismiss();
							}
							if(position == 0){
								tv_startmoney.setText("起投金额");
							}
							startmoney_choice = position;
							getChoice(1);
						}
					});
					popstartmoney = newPopupWindow(layoutstartmoney, tv_startmoney);
				}
				break;
			}
		}
		
		/**
		 * new PopupWindow
		 * */
		private PopupWindow newPopupWindow(View view, TextView tv) {
			// 创建弹出窗口
			// 窗口内容为layoutLeft，里面包含一个ListView
			// 窗口宽度跟tvLeft一样
			final PopupWindow pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			ColorDrawable cd = new ColorDrawable(-0000);
			pop.setBackgroundDrawable(cd);
			pop.setAnimationStyle(R.style.PopupAnimation2);
			pop.update();
			pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			pop.setTouchable(true);// 设置popupwindow可点击
			pop.setOutsideTouchable(true);// 设置popupwindow外部可点击
			pop.setFocusable(true);// 获取焦点
			pop.showAsDropDown(tv, 0, (llTopBar.getBottom() - tv.getHeight()) / 2);
			pop.setTouchInterceptor(new View.OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					// 如果点击了popupwindow的外部，popupwindow也会消失
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						pop.dismiss();
						return true;
					}
					view_alpha.setVisibility(View.GONE);
					return false;
				}
			});
			return pop;
		}

		private void setColor(int id) {
			tv_type.setTextColor(getResources().getColor(R.color.ylc_product_choice_gray));
			tv_type.setCompoundDrawables(null, null, down, null);
			tv_date.setTextColor(getResources().getColor(R.color.ylc_product_choice_gray));
			tv_date.setCompoundDrawables(null, null, down, null);
			tv_huibao.setTextColor(getResources().getColor(R.color.ylc_product_choice_gray));
			tv_huibao.setCompoundDrawables(null, null, down, null);
			tv_mode.setTextColor(getResources().getColor(R.color.ylc_product_choice_gray));
			tv_mode.setCompoundDrawables(null, null, down, null);
			tv_startmoney.setTextColor(getResources().getColor(R.color.ylc_product_choice_gray));
			tv_startmoney.setCompoundDrawables(null, null, down, null);
			switch(id){
			case R.id.tv_type:
				tv_type.setTextColor(getResources().getColor(R.color.ylc_product_choice_orange));
				tv_type.setCompoundDrawables(null, null, down_active, null);
				break;
			case R.id.tv_date:
				tv_date.setTextColor(getResources().getColor(R.color.ylc_product_choice_orange));
				tv_date.setCompoundDrawables(null, null, down_active, null);
				break;
			case R.id.tv_huibao:
				tv_huibao.setTextColor(getResources().getColor(R.color.ylc_product_choice_orange));
				tv_huibao.setCompoundDrawables(null, null, down_active, null);
				break;
			case R.id.tv_mode:
				tv_mode.setTextColor(getResources().getColor(R.color.ylc_product_choice_orange));
				tv_mode.setCompoundDrawables(null, null, down_active, null);
				break;
			case R.id.tv_startmoney:
				tv_startmoney.setTextColor(getResources().getColor(R.color.ylc_product_choice_orange));
				tv_startmoney.setCompoundDrawables(null, null, down_active, null);
				break;
			}
		}
	};
}
