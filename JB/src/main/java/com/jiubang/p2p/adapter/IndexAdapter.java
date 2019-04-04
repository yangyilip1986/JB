package com.jiubang.p2p.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiubang.p2p.R;
import com.jiubang.p2p.bean.Product;
import com.jiubang.p2p.map.TypeArray;
import com.jiubang.p2p.ui.TenderActivity;
import com.jiubang.p2p.view.TasksCompletedView;

/**
 * 直投直贷专区listview列表adapter
 * */
public class IndexAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;

	private List<Product> products;

	private Context context;

	public IndexAdapter(Context context,
			List<Product> products) {
		this.layoutInflater = LayoutInflater.from(context);
		this.products = products;
		this.context = context;
	}

	@Override
	public int getCount() {
		return products.size();
	}

	@Override
	public Object getItem(int position) {
		return products.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_product, null);
			
			holder = new ViewHolder();
			//标题
			holder.name = (TextView) convertView.findViewById(R.id.name);
			//预期年化利率百分比
			holder.gain = (TextView) convertView.findViewById(R.id.gain);
			//理财活动名称
			holder.tv_product_type_name = (TextView) convertView.findViewById(R.id.tv_product_type_name);
			//理财产品名称
			holder.nameInfo = (TextView) convertView.findViewById(R.id.nameInfo);
			//投资时限
			holder.deadline = (TextView) convertView.findViewById(R.id.deadline);
			holder.deadlinedesc = (TextView) convertView.findViewById(R.id.deadlinedesc);
			//最小起投数
			holder.singlePurchaseLowerLimit = (TextView) convertView.findViewById(R.id.singlePurchaseLowerLimit);
			//圆形进度条+背景变色
			holder.percentagepb = (TasksCompletedView) convertView.findViewById(R.id.percentagepb);
			//item点击事件
			holder.ll_selected_financial = (LinearLayout) convertView.findViewById(R.id.ll_selected_financial);
			// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)
			holder.iv_ti = (ImageView) convertView.findViewById(R.id.iv_ti);
			holder.iv_xian = (ImageView) convertView.findViewById(R.id.iv_xian);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		//预期年化利率百分比
		if("".equals(products.get(position).getExtraRate())) {
			String str = products.get(position).getGain() + "%";
			SpannableStringBuilder builder = new SpannableStringBuilder(str);
			CharacterStyle cs = new AbsoluteSizeSpan(32);//字体大小
			builder.setSpan(cs, str.length() - 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.gain.setText(builder);
		} else {
			String str = products.get(position).getGain() + "%"+ "+" + products.get(position).getExtraRate() + "%";
			SpannableStringBuilder builder = new SpannableStringBuilder(str);
			CharacterStyle cs1 = new AbsoluteSizeSpan(32);//字体大小
			CharacterStyle cs2 = new AbsoluteSizeSpan(32);//字体大小
			builder.setSpan(cs1, products.get(position).getGain().length(), products.get(position).getGain().length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			builder.setSpan(cs2, str.length() - 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.gain.setText(builder);
		}

		//理财产品名称
		if (products.get(position).getActivity() == 0) { // 没有活动
			holder.nameInfo.setVisibility(View.GONE);
		} else {
			holder.nameInfo.setVisibility(View.VISIBLE);
			holder.nameInfo.setText(products.get(position).getNameInfo());
		}
		//投资时限
		holder.deadline.setText(products.get(position).getDeadline());
		holder.deadlinedesc.setText("期限(" + products.get(position).getDeadlinedesc() + ")");
		holder.singlePurchaseLowerLimit.setText(products.get(position).getSinglePurchaseLowerLimit_show());
		//圆形进度条+背景变色
		//百分比
		holder.percentagepb.setProgress(products.get(position).getPercentage());
		holder.percentagepb.setText(TypeArray.status[products.get(position).getNewstatus()]);

		//item点击事件
		holder.ll_selected_financial.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//向投标页面跳转并发送内容
				Intent intent = new Intent(context, TenderActivity.class);
				intent.putExtra("id", products.get(position).getId());// 产品ID
				intent.putExtra("name", products.get(position).getName());// 产品名称
				intent.putExtra("products_exp_type", products.get(position).getProducts_exp_type());// 产品类型
				context.startActivity(intent);
			}
		});

		//理财活动名称
		if (products.get(position).getConfine() == 0) {	// 没有活动
			holder.tv_product_type_name.setVisibility(View.GONE);
		} else {
			holder.tv_product_type_name.setVisibility(View.VISIBLE);
			holder.tv_product_type_name.setText(products.get(position).getProduct_type_name());
		}
		//标题
		holder.name.setText(products.get(position).getName());

		// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)
		if(products.get(position).getProducts_exp_type() == 2) {
			holder.iv_ti.setVisibility(View.VISIBLE);
			holder.iv_xian.setVisibility(View.VISIBLE);
		} else if(products.get(position).getProducts_exp_type() == 1) {
			holder.iv_ti.setVisibility(View.VISIBLE);
			holder.iv_xian.setVisibility(View.GONE);
		} else {
			holder.iv_ti.setVisibility(View.GONE);
			holder.iv_xian.setVisibility(View.GONE);
		}
		
		
		return convertView;

	}

	public static class ViewHolder {
		//预期年化利率百分比
		public TextView gain;
		//理财产品名称
		public TextView nameInfo;
		//投资时限
		public TextView deadlinedesc;
		public TextView deadline;
		//最小起投数
		public TextView singlePurchaseLowerLimit;
		//圆形进度条+背景变色
		public TasksCompletedView percentagepb;
		//理财活动名称
		public TextView tv_product_type_name;
		//标题
		public TextView name;
		//item点击事件
		public LinearLayout ll_selected_financial;
		// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)
		public ImageView iv_ti;
		public ImageView iv_xian;
	}

}