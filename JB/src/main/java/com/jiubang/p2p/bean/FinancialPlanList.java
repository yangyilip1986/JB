package com.jiubang.p2p.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FinancialPlanList {

	private List<FinancialPlan> financialPlans;

    public FinancialPlanList(JSONArray array) throws JSONException {
        super();
        financialPlans = new ArrayList<FinancialPlan>();
        int len = array.length();
        for (int i = 0; i < len; i++) {
        	FinancialPlan a = new FinancialPlan();
            JSONObject o = (JSONObject) array.get(i);
            a.setFinancial_plan_title(o.getString("financial_plan_title"));// 理财名称
            a.setJoin_members(o.getString("join_members"));// 加入人数
            a.setAmount(o.getString("amount"));// 发行额度
            financialPlans.add(a);
        }
    }

    public FinancialPlanList(List<FinancialPlan> la, JSONArray array) throws JSONException {
        super();
        financialPlans = la;
        int len = array.length();
        for (int i = 0; i < len; i++) {
        	FinancialPlan a = new FinancialPlan();
            JSONObject o = (JSONObject) array.get(i);
            a.setFinancial_plan_title(o.getString("financial_plan_title"));// 理财名称
            a.setJoin_members(o.getString("join_members"));// 加入人数
            a.setAmount(o.getString("amount"));// 发行额度
            financialPlans.add(a);
        }
    }

	public List<FinancialPlan> getFinancialPlans() {
		return financialPlans;
	}

	public void setFinancialPlans(List<FinancialPlan> financialPlans) {
		this.financialPlans = financialPlans;
	}


}
