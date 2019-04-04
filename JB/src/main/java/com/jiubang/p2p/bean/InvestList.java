package com.jiubang.p2p.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InvestList {

	private List<Invest> invests;

	public InvestList(JSONArray array) throws JSONException {
		super();
		invests = new ArrayList<Invest>();
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Invest a = new Invest();
			JSONObject o = (JSONObject) array.get(i);
			JSONObject order = o.getJSONObject("order");
			if (order.has("isTransfer"))
				a.setIsTransfer(order.getString("isTransfer"));
			if (order.has("lastReturn"))
				a.setLastReturn(order.getString("lastReturn"));
			if (order.has("principalAndInterest"))
				a.setPrincipalAndInterest(order.getString("principalAndInterest"));
			if (order.has("name"))
				a.setName(order.getString("name"));
			if (order.has("price"))
				a.setPrice(order.getString("price"));
			if (order.has("rate"))
				a.setRate(order.getString("rate"));
			if (order.has("extra_rate"))
				a.setExtra_rate(order.getString("extra_rate"));
			if (order.has("repayTime"))
				a.setRepayTime(order.getLong("repayTime"));
			if (order.has("createDate"))
				a.setCreateDate(order.getString("createDate"));
			if (order.has("interestBeginDate"))
				a.setInterestBeginDate(order.getString("interestBeginDate"));
			if (order.has("statusText"))
				a.setStatusText(order.getString("statusText"));
			if (order.has("id"))
				a.setId(order.getString("id"));
			if (order.has("endDate"))
				a.setEndDate(order.getString("endDate"));
			if (order.has("expiryDate"))
				a.setExpiryDate(order.getString("expiryDate"));
			if (order.has("total"))
				a.setTotal(order.getString("total"));
			if (order.has("oid_tender_id"))
				a.setOid_tender_id(order.getString("oid_tender_id"));
			if (order.has("tender_from"))
				a.setTender_from(order.getString("tender_from"));
			//体验金券相关内容
			//产品类型
			if (order.has("products_exp_type"))
				a.setProducts_exp_type(order.getString("products_exp_type"));
			//投资体验金金额
			if (order.has("exp_tender_amount"))
				a.setExperience_amount(order.getString("exp_tender_amount"));
			//体验金利息
			if (order.has("exp_amount_interest"))
				a.setExperience_accrual(order.getString("exp_amount_interest"));
			//本金利息
			if (order.has("amount_interest"))
				a.setCapital_accrual(order.getString("amount_interest"));
			//应还本金
			if (order.has("recover_amount_capital"))
				a.setCapital_return(order.getString("recover_amount_capital"));
			//应还真钱总额
			if (order.has("recover_amount_total_real"))
				a.setCapital_all(order.getString("recover_amount_total_real"));
			invests.add(a);
		}
	}

	public InvestList(List<Invest> li, JSONArray array) throws JSONException {
		super();
		invests = li;
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Invest a = new Invest();
			JSONObject o = (JSONObject) array.get(i);
			JSONObject order = o.getJSONObject("order");
			if (order.has("isTransfer"))
				a.setIsTransfer(order.getString("isTransfer"));
			if (order.has("lastReturn"))
				a.setLastReturn(order.getString("lastReturn"));
			if (order.has("principalAndInterest"))
				a.setPrincipalAndInterest(order.getString("principalAndInterest"));
			if (order.has("name"))
				a.setName(order.getString("name"));
			if (order.has("price"))
				a.setPrice(order.getString("price"));
			if (order.has("rate"))
				a.setRate(order.getString("rate"));
			if (order.has("extra_rate"))
				a.setExtra_rate(order.getString("extra_rate"));
			if (order.has("repayTime"))
				a.setRepayTime(order.getLong("repayTime"));
			if (order.has("createDate"))
				a.setCreateDate(order.getString("createDate"));
			if (order.has("interestBeginDate"))
				a.setInterestBeginDate(order.getString("interestBeginDate"));
			if (order.has("statusText"))
				a.setStatusText(order.getString("statusText"));
			if (order.has("id"))
				a.setId(order.getString("id"));
			if (order.has("endDate"))
				a.setEndDate(order.getString("endDate"));
			if (order.has("expiryDate"))
				a.setExpiryDate(order.getString("expiryDate"));
			if (order.has("total"))
				a.setTotal(order.getString("total"));
			if (order.has("oid_tender_id"))
				a.setOid_tender_id(order.getString("oid_tender_id"));
			if (order.has("tender_from"))
				a.setTender_from(order.getString("tender_from"));
			//体验金券相关内容
			//产品类型
			if (order.has("products_exp_type"))
				a.setProducts_exp_type(order.getString("products_exp_type"));
			//投资体验金金额
			if (order.has("exp_tender_amount"))
				a.setExperience_amount(order.getString("exp_tender_amount"));
			//体验金利息
			if (order.has("exp_amount_interest"))
				a.setExperience_accrual(order.getString("exp_amount_interest"));
			//本金利息
			if (order.has("amount_interest"))
				a.setCapital_accrual(order.getString("amount_interest"));
			//应还本金
			if (order.has("recover_amount_capital"))
				a.setCapital_return(order.getString("recover_amount_capital"));
			//应还真钱总额
			if (order.has("recover_amount_total_real"))
				a.setCapital_all(order.getString("recover_amount_total_real"));
			invests.add(a);
		}
	}

	public List<Invest> getInvests() {
		return invests;
	}

	public void setInvests(List<Invest> invests) {
		this.invests = invests;
	}

	@Override
	public String toString() {
		int len = invests.size();
		String st = "";
		for (int i = 0; i < len; i++) {
			Invest a = invests.get(i);
			st += " /**" + i + a.toString();
		}
		return st;
	}

}
