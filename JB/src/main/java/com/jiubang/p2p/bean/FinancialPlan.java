package com.jiubang.p2p.bean;

public class FinancialPlan {

	private String financial_plan_title;// 理财名称
	private String join_members;// 加入人数
	private String amount;// 发行额度

	public String getFinancial_plan_title() {
		return financial_plan_title;
	}

	public void setFinancial_plan_title(String financial_plan_title) {
		this.financial_plan_title = financial_plan_title;
	}

	public String getJoin_members() {
		return join_members;
	}

	public void setJoin_members(String join_members) {
		this.join_members = join_members;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
