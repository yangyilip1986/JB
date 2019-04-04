package com.jiubang.p2p.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiubang.p2p.R;
import com.jiubang.p2p.bean.TenderBean;

public class TenderAdapter extends BaseAdapter {
	
	private List<TenderBean> mList;
	private LayoutInflater mInflater;
	
	public TenderAdapter(Context context ,List<TenderBean> datas) {
		this.mList = datas;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_tender_gridview, null);
			viewholder = new ViewHolder();
			viewholder.text = (TextView) convertView.findViewById(R.id.tv_auditing);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		TenderBean TenderBean = mList.get(position);
		viewholder.text.setText(TenderBean.getAuditing());
		return convertView;
	}
	
	public class ViewHolder{
		private TextView text;
	}

}
