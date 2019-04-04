package com.jiubang.p2p.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InvestDetailList {

	private List<InvestDetail> list;

	public InvestDetailList(JSONArray array) throws JSONException {
		super();
		list = new ArrayList<InvestDetail>();
		int len = array.length();
		for (int i = 0; i < len; i++) {
			InvestDetail a = new InvestDetail();
			JSONObject o = (JSONObject) array.get(i);
			a.setRecover_date(o.getString("recover_date"));//日期
			a.setRecover_amount_capital(o.getString("recover_amount_capital"));//本金
			a.setRecover_amount_interest(o.getString("amount_interest"));//本金利息
			a.setRecover_amount_total(o.getString("recover_amount_total"));//本金总额
			a.setRecover_flg(o.getString("recover_flg"));//状态
			if (o.has("exp_amount_interest")){
			a.setExp_amount_interest(o.getString("exp_amount_interest"));//体验金利息
			a.setExp_tender_amount(o.getString("exp_amount_interest"));//体验金总额==体验金利息
			}
			list.add(a);
		}
	}

	public InvestDetailList(List<InvestDetail> li, JSONArray array) throws JSONException {
		super();
		list = li;
		int len = array.length();
		for (int i = 0; i < len; i++) {
			InvestDetail a = new InvestDetail();
			JSONObject o = (JSONObject) array.get(i);
			a.setRecover_date(o.getString("recover_date"));
			a.setRecover_amount_capital(o.getString("recover_amount_capital"));
			a.setRecover_amount_interest(o.getString("amount_interest"));
			a.setRecover_amount_total(o.getString("recover_amount_total"));
			a.setRecover_flg(o.getString("recover_flg"));
			if (o.has("exp_amount_interest")){
				a.setExp_amount_interest(o.getString("exp_amount_interest"));//体验金利息
				a.setExp_tender_amount(o.getString("exp_amount_interest"));//体验金总额==体验金利息
				}
			list.add(a);
		}
	}

	public List<InvestDetail> getList() {
		return list;
	}

	public void setList(List<InvestDetail> list) {
		this.list = list;
	}

}
