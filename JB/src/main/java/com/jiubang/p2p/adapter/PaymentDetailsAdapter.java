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
import com.jiubang.p2p.bean.InvestDetail;

@SuppressLint("InflateParams")
public class PaymentDetailsAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;

	private List<InvestDetail> investDetails;
	
	private String mProducts_exp_type;

	public PaymentDetailsAdapter(Context context, List<InvestDetail> investDetails,String products_exp_type) {
		this.layoutInflater = LayoutInflater.from(context);
		this.investDetails = investDetails;
		this.mProducts_exp_type = products_exp_type;
	}

	@Override
	public int getCount() {
		return investDetails.size();
	}

	@Override
	public Object getItem(int position) {
		return investDetails.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_payment_detail, null);
            holder = new ViewHolder();
            holder.tv_date = (TextView)convertView.findViewById(R.id.tv_date);
            holder.tv_total = (TextView)convertView.findViewById(R.id.tv_total);
            holder.tv_extra = (TextView)convertView.findViewById(R.id.tv_extra);
            holder.tv_total_and_extra = (TextView)convertView.findViewById(R.id.tv_total_and_extra);
            holder.tv_state = (TextView)convertView.findViewById(R.id.tv_state);
            holder.tv_exp_amount_interest = (TextView)convertView.findViewById(R.id.tv_exp_amount_interest);
            holder.tv_exp_tender_amount = (TextView)convertView.findViewById(R.id.tv_exp_tender_amount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
		if(mProducts_exp_type.equals("0")){
			holder.tv_exp_amount_interest.setVisibility(View.GONE);
			holder.tv_exp_tender_amount.setVisibility(View.GONE);
		}
		if(mProducts_exp_type.equals("1")){
			holder.tv_extra.setVisibility(View.GONE);
			holder.tv_total_and_extra.setVisibility(View.GONE);
		}
		holder.tv_date.setText(investDetails.get(position).getRecover_date());
		holder.tv_total.setText(investDetails.get(position).getRecover_amount_capital());
		holder.tv_extra.setText(investDetails.get(position).getRecover_amount_interest());
		holder.tv_total_and_extra.setText(investDetails.get(position).getRecover_amount_total());
		holder.tv_state.setText(investDetails.get(position).getRecover_flg());
		holder.tv_exp_amount_interest.setText(investDetails.get(position).getExp_amount_interest());
		holder.tv_exp_tender_amount.setText(investDetails.get(position).getExp_tender_amount());

		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_date;
		public TextView tv_total;
		public TextView tv_extra;
		public TextView tv_total_and_extra;
		public TextView tv_state;
		public TextView tv_exp_amount_interest;
		public TextView tv_exp_tender_amount;
	}

}