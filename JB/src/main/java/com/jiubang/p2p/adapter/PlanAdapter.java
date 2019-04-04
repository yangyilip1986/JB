package com.jiubang.p2p.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiubang.p2p.R;
import com.jiubang.p2p.bean.DetailPlan;

public class PlanAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;

	private List<DetailPlan> detailPlan;

	private Context context;

	public PlanAdapter(Context context, List<DetailPlan> detailPlan) {
		this.layoutInflater = LayoutInflater.from(context);
		this.detailPlan = detailPlan;
		this.context = context;
	}

	@Override
	public int getCount() {
		return detailPlan.size();
	}

	@Override
	public Object getItem(int position) {
		return detailPlan.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_detail_plan, null);
			
			holder = new ViewHolder();
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.principal = (TextView) convertView.findViewById(R.id.principal);
			holder.interest = (TextView) convertView.findViewById(R.id.interest);
			holder.amount = (TextView) convertView.findViewById(R.id.amount);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.date.setText(detailPlan.get(position).getDate());
		holder.principal.setText("¥" + detailPlan.get(position).getPrincipal());
		holder.interest.setText("¥" + detailPlan.get(position).getInterest());
		holder.amount.setText("¥" + detailPlan.get(position).getAmount());

		return convertView;

	}
	
	public static class ViewHolder {
		public TextView date;
		public TextView principal;
		public TextView interest;
		public TextView amount;
	}

}