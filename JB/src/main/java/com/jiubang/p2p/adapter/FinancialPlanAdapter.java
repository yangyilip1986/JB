package com.jiubang.p2p.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiubang.p2p.R;
import com.jiubang.p2p.bean.FinancialPlan;

public class FinancialPlanAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;

	private List<FinancialPlan> financialPlan;

	private Context context;

	public FinancialPlanAdapter(Context context,
			List<FinancialPlan> financialPlan) {
		this.layoutInflater = LayoutInflater.from(context);
		this.financialPlan = financialPlan;
		this.context = context;
	}

	@Override
	public int getCount() {
		return financialPlan.size();
	}

	@Override
	public Object getItem(int position) {
		return financialPlan.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_financial_plan, null);
			
			holder = new ViewHolder();
			holder.tv_financial_plan_title = (TextView) convertView.findViewById(R.id.tv_financial_plan_title);
			holder.tv_join_members = (TextView) convertView.findViewById(R.id.tv_join_members);
			holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_financial_plan_title.setText(financialPlan.get(position).getFinancial_plan_title());
		holder.tv_join_members.setText(financialPlan.get(position).getJoin_members() + "人");
		holder.tv_amount.setText(financialPlan.get(position).getAmount() + "元");

		return convertView;

	}

	public static class ViewHolder {
		public TextView tv_financial_plan_title;
		public TextView tv_join_members;
		public TextView tv_amount;
	}

}