package com.jiubang.p2p.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiubang.p2p.R;
import com.jiubang.p2p.ui.YanInviteActivity.Words;

public class YanInviteAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;

	private List<Words> words;

	public YanInviteAdapter(Context context, List<Words> words) {
		this.layoutInflater = LayoutInflater.from(context);
		this.words = words;
	}

	@Override
	public int getCount() {
		return words.size();
	}

	@Override
	public Object getItem(int position) {
		return words.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_yan_invite, null);
			holder = new ViewHolder();
			holder.tv_str1 = (TextView) convertView.findViewById(R.id.tv_str1);
			holder.tv_str2 = (TextView) convertView.findViewById(R.id.tv_str2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_str1.setText(words.get(position).getStr1());
		holder.tv_str2.setText(words.get(position).getStr2());

		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_str1;
		public TextView tv_str2;
	}

}