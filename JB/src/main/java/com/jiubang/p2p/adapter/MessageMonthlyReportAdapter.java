package com.jiubang.p2p.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiubang.p2p.R;
import com.jiubang.p2p.bean.Project;

public class MessageMonthlyReportAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;

	private List<Project> projects;

	public MessageMonthlyReportAdapter(Context context, List<Project> projects) {
		this.layoutInflater = LayoutInflater.from(context);
		this.projects = projects;
	}

	@Override
	public int getCount() {
		return projects.size();
	}

	@Override
	public Object getItem(int position) {
		return projects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_message_center_detail, null);
            holder = new ViewHolder();
            holder.tv_products_title = (TextView)convertView.findViewById(R.id.tv_products_title);
            holder.tv_sum_recover_amount = (TextView)convertView.findViewById(R.id.tv_sum_recover_amount);
            holder.tv_recover_amount_capital_yes = (TextView)convertView.findViewById(R.id.tv_recover_amount_capital_yes);
            holder.tv_recover_amount_interest_yes = (TextView)convertView.findViewById(R.id.tv_recover_amount_interest_yes);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
		
		holder.tv_products_title.setText(projects.get(position).getProducts_title());
		holder.tv_sum_recover_amount.setText(projects.get(position).getSum_recover_amount());
		holder.tv_recover_amount_capital_yes.setText(projects.get(position).getRecover_amount_capital_yes());
		
		if(Double.parseDouble(projects.get(position).getRecover_amount_interest_yes()) == 0){
			holder.tv_recover_amount_interest_yes.setText(projects.get(position).getRecover_amount_interest_yes());
			holder.tv_recover_amount_interest_yes.setTextColor(Color.rgb(130, 130, 130));
		}else if(Double.parseDouble(projects.get(position).getRecover_amount_interest_yes()) > 0){
			holder.tv_recover_amount_interest_yes.setText("+" + projects.get(position).getRecover_amount_interest_yes());
			holder.tv_recover_amount_interest_yes.setTextColor(Color.rgb(105, 188, 116));
		}else{
			holder.tv_recover_amount_interest_yes.setText(projects.get(position).getRecover_amount_interest_yes());
			holder.tv_recover_amount_interest_yes.setTextColor(Color.rgb(252, 104, 92));
		}

		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_products_title;
		public TextView tv_sum_recover_amount;
		public TextView tv_recover_amount_capital_yes;
		public TextView tv_recover_amount_interest_yes;
	}

}