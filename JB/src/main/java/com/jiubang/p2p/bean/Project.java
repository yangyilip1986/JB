package com.jiubang.p2p.bean;

public class Project {

	private String products_title;// 项目标题
	private String sum_recover_amount;// 收款额
	private String recover_amount_capital_yes;// 本金
	private String recover_amount_interest_yes;// 收益

	public String getProducts_title() {
		return products_title;
	}

	public void setProducts_title(String products_title) {
		this.products_title = products_title;
	}

	public String getSum_recover_amount() {
		return sum_recover_amount;
	}

	public void setSum_recover_amount(String sum_recover_amount) {
		this.sum_recover_amount = sum_recover_amount;
	}

	public String getRecover_amount_capital_yes() {
		return recover_amount_capital_yes;
	}

	public void setRecover_amount_capital_yes(String recover_amount_capital_yes) {
		this.recover_amount_capital_yes = recover_amount_capital_yes;
	}

	public String getRecover_amount_interest_yes() {
		return recover_amount_interest_yes;
	}

	public void setRecover_amount_interest_yes(
			String recover_amount_interest_yes) {
		this.recover_amount_interest_yes = recover_amount_interest_yes;
	}

}
