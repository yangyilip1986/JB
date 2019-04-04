package com.jiubang.p2p.bean;

public class InvestDetail {

	private String recover_date;//预计还款时间
	private String recover_amount_capital;//应还本金
	private String recover_amount_interest;//应还利息
	private String recover_amount_total;//应还总额
	private String recover_flg; // 还款状态
	private String exp_tender_amount; // 体验金总额
	private String exp_amount_interest; // 体验金利息
	
	public String getExp_tender_amount() {
		return exp_tender_amount;
	}

	public void setExp_tender_amount(String exp_tender_amount) {
		this.exp_tender_amount = exp_tender_amount;
	}

	public String getExp_amount_interest() {
		return exp_amount_interest;
	}

	public void setExp_amount_interest(String exp_amount_interest) {
		this.exp_amount_interest = exp_amount_interest;
	}

	public String getRecover_date() {
		return recover_date;
	}

	public void setRecover_date(String recover_date) {
		this.recover_date = recover_date;
	}

	public String getRecover_amount_capital() {
		return recover_amount_capital;
	}

	public void setRecover_amount_capital(String recover_amount_capital) {
		this.recover_amount_capital = recover_amount_capital;
	}

	public String getRecover_amount_interest() {
		return recover_amount_interest;
	}

	public void setRecover_amount_interest(String recover_amount_interest) {
		this.recover_amount_interest = recover_amount_interest;
	}

	public String getRecover_amount_total() {
		return recover_amount_total;
	}

	public void setRecover_amount_total(String recover_amount_total) {
		this.recover_amount_total = recover_amount_total;
	}

	public String getRecover_flg() {
		return recover_flg;
	}

	public void setRecover_flg(String recover_flg) {
		this.recover_flg = recover_flg;
	}

}
