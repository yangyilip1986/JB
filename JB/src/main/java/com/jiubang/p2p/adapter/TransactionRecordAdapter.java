package com.jiubang.p2p.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiubang.p2p.R;
import com.jiubang.p2p.bean.Transaction;

public class TransactionRecordAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;

	private List<Transaction> transactions;
	
	private Context context;

	public TransactionRecordAdapter(Context context,
			List<Transaction> transactions) {
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.transactions = transactions;
	}

	@Override
	public int getCount() {
		return transactions.size();
	}

	@Override
	public Object getItem(int position) {
		return transactions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		holder = new ViewHolder();
		if (transactions.get(position).getDateflag() != null) {
			convertView = layoutInflater.inflate(R.layout.item_transaction_record_date, null);

			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);

			holder.tv_date.setText(transactions.get(position).getDateflag());
		} else {
			convertView = layoutInflater.inflate(R.layout.item_transaction_record, null);
			holder.transactionId = (TextView) convertView.findViewById(R.id.transactionId);
			holder.amount = (TextView) convertView.findViewById(R.id.amount);
			holder.createTime = (TextView) convertView.findViewById(R.id.createTime);
			holder.transactionType = (TextView) convertView.findViewById(R.id.transactionType);

			holder.transactionId.setText("交易号：" + transactions.get(position).getTransactionId());
			if (transactions.get(position).getOperationAmount().startsWith("+")) {
				holder.amount.setTextColor(context.getResources().getColor(R.color.app_green));
			} else if (transactions.get(position).getOperationAmount().startsWith("-")) {
				holder.amount.setTextColor(context.getResources().getColor(R.color.app_red));
			} else {
				holder.amount.setTextColor(context.getResources().getColor(R.color.app_green));
			}
			holder.amount.setText(transactions.get(position).getOperationAmount()+"元");
			try {
				SimpleDateFormat sdfold = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date= sdfold.parse(transactions.get(position).getCreateTime());
				holder.createTime.setText(sdfold.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			holder.transactionType.setText(transactions.get(position).getTransactionType());

		}

		return convertView;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public static class ViewHolder {
		public TextView transactionId;
		public TextView amount;
		public TextView createTime;
		public TextView transactionType;
		public TextView tv_date;
	}

}