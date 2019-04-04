package com.jiubang.p2p.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailPlanBProduct {
	    


	// 产品信息部分
	private String status;
	private String usable_amount;//可用余额
	
	private String amount; // 计划金额
	private String amount_show; // 计划金额
	
	private String surplus_amount; // 剩余可投
	private String surplus_amount_show; // 剩余可投
	
	private String investBPeriod; // 投资期限
	private String investBPeriodUnit; // 投资期限
	
	private String interest_rate; // 预计年化收益率
	
    private String add_time;//加入时间
    
	private String addConditions;//加入条件
	
	private String recover_type; // 回款方式
	private String financial_plan_status; // 计划状态 1：立即投资 3：已售罄 5：停止售卖
	private int progress; // 百分比
	private String minAddAmount; // 最小投资金额，起投
	private String minAddAmount_show; // 最小投资金额，起投

	private String brief; // 项目简介
	
	private String vestment_universe;//投资范围
	
	private String amountTotal;//收益计算器
	
	private String protocol;
	
	private String protocol_url;
	private String financial_plan_title;
	
	public DetailPlanBProduct(JSONObject o) throws JSONException{
		super();
		amount = o.getString("amount");//计划可投
		amount_show = o.getString("amount_show");//计划可投
		interest_rate = o.getString("interest_rate");//预计年化收益率
		financial_plan_title = o.getString("financial_plan_title");//理财计划标名
		investBPeriod = o.getString("investBPeriod");// 投资时限
		investBPeriodUnit = o.getString("investBPeriodUnit");// 投资时限单位
		financial_plan_status = o.getString("financial_plan_status");// 计划状态 1：立即投资 3：已售罄 5：停止售卖
		progress = o.getInt("progress");//百分比
		surplus_amount = o.getString("surplus_amount");// 剩余可投
		surplus_amount_show = o.getString("surplus_amount_show");// 剩余可投
		minAddAmount = o.getString("minAddAmount");// 起投金额
		minAddAmount_show = o.getString("minAddAmount_show");// 起投金额
		
		add_time = o.getString("add_time");//加入时间
		addConditions = o.getString("addConditions");//加入条件
		
		usable_amount = o.getString("usable_amount");//可用余额
		amountTotal = o.getString("amountTotal");//收益计算器
		
		brief = o.getString("brief");// 项目简介
		vestment_universe = o.getString("vestment_universe");// 投资范围
		recover_type = o.getString("recover_type");//回款方式
		
		status = o.getString("status");
		
		protocol = o.getString("agreement");//协议名
		
		protocol_url = o.getString("agreement_url");//协议链接
	}

	public String getFinancial_plan_title() {
		return financial_plan_title;
	}

	public void setFinancial_plan_title(String financial_plan_title) {
		this.financial_plan_title = financial_plan_title;
	}

	public String getAmount_show() {
		return amount_show;
	}

	public void setAmount_show(String amount_show) {
		this.amount_show = amount_show;
	}

	public String getSurplus_amount_show() {
		return surplus_amount_show;
	}

	public void setSurplus_amount_show(String surplus_amount_show) {
		this.surplus_amount_show = surplus_amount_show;
	}

	public String getMinAddAmount_show() {
		return minAddAmount_show;
	}

	public void setMinAddAmount_show(String minAddAmount_show) {
		this.minAddAmount_show = minAddAmount_show;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsable_amount() {
		return usable_amount;
	}

	public void setUsable_amount(String usable_amount) {
		this.usable_amount = usable_amount;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSurplus_amount() {
		return surplus_amount;
	}

	public void setSurplus_amount(String surplus_amount) {
		this.surplus_amount = surplus_amount;
	}

	public String getInvestBPeriod() {
		return investBPeriod;
	}

	public void setInvestBPeriod(String investBPeriod) {
		this.investBPeriod = investBPeriod;
	}

	public String getInvestBPeriodUnit() {
		return investBPeriodUnit;
	}

	public void setInvestBPeriodUnit(String investBPeriodUnit) {
		this.investBPeriodUnit = investBPeriodUnit;
	}

	public String getInterest_rate() {
		return interest_rate;
	}

	public void setInterest_rate(String interest_rate) {
		this.interest_rate = interest_rate;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public String getAddConditions() {
		return addConditions;
	}

	public void setAddConditions(String addConditions) {
		this.addConditions = addConditions;
	}

	public String getRecover_type() {
		return recover_type;
	}

	public void setRecover_type(String recover_type) {
		this.recover_type = recover_type;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getMinAddAmount() {
		return minAddAmount;
	}

	public void setMinAddAmount(String minAddAmount) {
		this.minAddAmount = minAddAmount;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getVestment_universe() {
		return vestment_universe;
	}

	public void setVestment_universe(String vestment_universe) {
		this.vestment_universe = vestment_universe;
	}

	public String getAmountTotal() {
		return amountTotal;
	}

	public void setAmountTotal(String amountTotal) {
		this.amountTotal = amountTotal;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getProtocol_url() {
		return protocol_url;
	}

	public void setProtocol_url(String protocol_url) {
		this.protocol_url = protocol_url;
	}

	public String getFinancial_plan_status() {
		return financial_plan_status;
	}

	public void setFinancial_plan_status(String financial_plan_status) {
		this.financial_plan_status = financial_plan_status;
	}
}
