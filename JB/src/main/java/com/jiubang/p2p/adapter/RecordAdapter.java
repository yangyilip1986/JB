package com.jiubang.p2p.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiubang.p2p.R;
import com.jiubang.p2p.bean.DetailRecord;

public class RecordAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;

	private List<DetailRecord> financialPlan;

	private Context context;

	public RecordAdapter(Context context,
			List<DetailRecord> financialPlan) {
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
			convertView = layoutInflater.inflate(R.layout.item_detail_record, null);
			
			holder = new ViewHolder();
			holder.realName = (TextView) convertView.findViewById(R.id.realName);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.createDate = (TextView) convertView.findViewById(R.id.createDate);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.realName.setText(financialPlan.get(position).getRealName());
		holder.price.setText("¥" + financialPlan.get(position).getPrice());
		holder.createDate.setText(financialPlan.get(position).getCreateDate());

		return convertView;

	}

	public static class ViewHolder {
		public TextView realName;
		public TextView price;
		public TextView createDate;
	}

}