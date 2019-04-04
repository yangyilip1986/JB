package com.jiubang.p2p.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiubang.p2p.R;

@SuppressLint("InflateParams")
public class ContractAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;

	private List<String> contractNames;

	public ContractAdapter(Context context, List<String> contractNames) {
		this.layoutInflater = LayoutInflater.from(context);
		this.contractNames = contractNames;
	}

	@Override
	public int getCount() {
		return contractNames.size();
	}

	@Override
	public Object getItem(int position) {
		return contractNames.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_contract_name, null);
            
            holder = new ViewHolder();
            holder.tv_contract_name = (TextView)convertView.findViewById(R.id.tv_contract_name);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
		
		holder.tv_contract_name.setText(contractNames.get(position));

		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_contract_name;
	}

}