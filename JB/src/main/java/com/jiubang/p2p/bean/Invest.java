package com.jiubang.p2p.bean;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Invest {

	private String id;
	private String oid_tender_id;
	private String tender_from;
	private String name;
	private String rate; // 预计年化收益
	private String extra_rate; // 加息
	private String price; // 投资金额
	private String lastReturn; // 回款总额
	private String principalAndInterest; // 回款本息
	private String repayTime; // 下个还款日
	private String statusText; // 状态
	private String createDate;
	private String interestBeginDate;
	private String endDate;
	private String expiryDate;// 到期日期
	private String total;// 总额
	private String isTransfer;// 是否可转让 1:可转让 0:不可转让
	private String capital_accrual;    //本金利息
	private String experience_accrual; //体验金利息
	private String capital_return;      //应还本金
	private String experience_amount;   //体验金金额
	private String capital_all;            //本金
	private String products_exp_type;  //产品类型

	private boolean hasCoupon; // 是否使用优惠券
	private String couponName;
	private String couponPrice;
	private String couponLastReturn;
	
	public String getExperience_amount() {
		return experience_amount;
	}

	public void setExperience_amount(String experience_amount) {
		this.experience_amount = experience_amount;
	}

	public String getProducts_exp_type() {
		return products_exp_type;
	}

	public void setProducts_exp_type(String products_exp_type) {
		this.products_exp_type = products_exp_type;
	}

	public String getIsTransfer() {
		return isTransfer;
	}

	public void setIsTransfer(String isTransfer) {
		this.isTransfer = isTransfer;
	}

	public String getTender_from() {
		return tender_from;
	}

	public void setTender_from(String tender_from) {
		this.tender_from = tender_from;
	}

	public String getOid_tender_id() {
		return oid_tender_id;
	}

	public void setOid_tender_id(String oid_tender_id) {
		this.oid_tender_id = oid_tender_id;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setRepayTime(String repayTime) {
		this.repayTime = repayTime;
	}

	public String getExtra_rate() {
		return extra_rate;
	}

	public void setExtra_rate(String extra_rate) {
		this.extra_rate = extra_rate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public boolean isHasCoupon() {
		return hasCoupon;
	}

	public void setHasCoupon(boolean hasCoupon) {
		this.hasCoupon = hasCoupon;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getCouponPrice() {
		return couponPrice;
	}

	public void setCouponPrice(String couponPrice) {
		this.couponPrice = couponPrice;
	}

	public String getCouponLastReturn() {
		return couponLastReturn;
	}

	public void setCouponLastReturn(String couponLastReturn) {
		this.couponLastReturn = couponLastReturn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInterestBeginDate() {
		return interestBeginDate;
	}

	public void setInterestBeginDate(String interestBeginDate) {
		this.interestBeginDate = interestBeginDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getLastReturn() {
		return lastReturn;
	}

	public void setLastReturn(String lastReturn) {
		this.lastReturn = lastReturn;
	}

	public String getPrincipalAndInterest() {
		return principalAndInterest;
	}

	public void setPrincipalAndInterest(String principalAndInterest) {
		this.principalAndInterest = principalAndInterest;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public String getRepayTime() {
		return repayTime;
	}

	public void setRepayTime(long repayTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sd = sdf.format(new Date(repayTime * 1000));
		this.repayTime = sd;
	}
	
	public String getCapital_accrual() {
		return capital_accrual;
	}

	public void setCapital_accrual(String capital_accrual) {
		this.capital_accrual = capital_accrual;
	}

	public String getExperience_accrual() {
		return experience_accrual;
	}

	public void setExperience_accrual(String experience_accrual) {
		this.experience_accrual = experience_accrual;
	}

	public String getCapital_return() {
		return capital_return;
	}

	public void setCapital_return(String capital_return) {
		this.capital_return = capital_return;
	}

	public String getCapital_all() {
		return capital_all;
	}

	public void setCapital_all(String capital) {
		this.capital_all = capital;
	}

	@Override
	public String toString() {
		return "Invest [name=" + name + ", rate=" + rate + ", price=" + price
				+ ", lastReturn=" + lastReturn + ", repayTime=" + repayTime
				+ ", statusText=" + statusText + "]";
	}

}
