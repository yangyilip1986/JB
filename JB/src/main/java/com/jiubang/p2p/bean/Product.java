package com.jiubang.p2p.bean;

/*
* 理财产品实体类
* */
public class Product {

	private int id; // 产品ID

	private int is_transfer; // 是否债权产品 1：债权产品

	private String oid_transfer_id; // 债权产品ID

	private int type; // 产品类型 productType -1原始 0壹财贷 1壹财宝
	
	private int products_exp_type;// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)

	private String url; // 产品链接 productUrl

	private String name; // 产品名称 name

	private int confine; // 产品类型confine 0所有用户，1，新手专享，2，app专享，3，周末专享

	private String product_type_name; // 活动名称

	private String product_type_image_url; // 活动图标

	private String nameInfo; // 产品名称 activityTab

	private int activity; // activity 0 nameInfo没有，1，nameInfo有

	private int activityType; // 为3 下面有值

	private String extraRate;

	private String singlePurchaseLowerLimit; // 最小起投数
	

	private String singlePurchaseLowerLimit_show; // 最小起投数

	private String guaranteeModeName; // 还款类型 本息担保

	private String remainingInvestmentAmount; // 剩余投资量
	
	private String totalInvestment; // 投资总量

	private String gain; // 预期年化收益

	private String deadline; // 投资时限 investmentPeriodDesc[0]

	private String deadlinedesc; // 投资时限 investmentPeriodDesc[1]

	private int percentage; // 百分比

	private String repayMethod; // 到期还本付息(还款方式)

	private int status; // 产品状态

	private int newstatus;

	public int getProducts_exp_type() {
		return products_exp_type;
	}

	public void setProducts_exp_type(int products_exp_type) {
		this.products_exp_type = products_exp_type;
	}

	public String getProduct_type_name() {
		return product_type_name;
	}

	public void setProduct_type_name(String product_type_name) {
		this.product_type_name = product_type_name;
	}

	public void setSinglePurchaseLowerLimit(String singlePurchaseLowerLimit) {
		this.singlePurchaseLowerLimit = singlePurchaseLowerLimit;
	}

	public int getIs_transfer() {
		return is_transfer;
	}

	public void setIs_transfer(int is_transfer) {
		this.is_transfer = is_transfer;
	}

	public String getOid_transfer_id() {
		return oid_transfer_id;
	}

	public void setOid_transfer_id(String oid_transfer_id) {
		this.oid_transfer_id = oid_transfer_id;
	}

	public String getSinglePurchaseLowerLimit_show() {
		return singlePurchaseLowerLimit_show;
	}

	public void setSinglePurchaseLowerLimit_show(
			String singlePurchaseLowerLimit_show) {
		this.singlePurchaseLowerLimit_show = singlePurchaseLowerLimit_show;
	}

	public String getProduct_type_image_url() {
		return product_type_image_url;
	}

	public void setProduct_type_image_url(String product_type_image_url) {
		this.product_type_image_url = product_type_image_url;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getConfine() {
		return confine;
	}

	public void setConfine(int confine) {
		this.confine = confine;
	}

	public String getNameInfo() {
		return nameInfo;
	}

	public void setNameInfo(String nameInfo) {
		this.nameInfo = nameInfo;
	}

	public String getSinglePurchaseLowerLimit() {
		return singlePurchaseLowerLimit;
	}

	public void setSinglePurchaseLowerLimit(int singlePurchaseLowerLimit) {
		this.singlePurchaseLowerLimit = singlePurchaseLowerLimit / 100 + "";
	}

	public String getGuaranteeModeName() {
		return guaranteeModeName;
	}

	public void setGuaranteeModeName(String guaranteeModeName) {
		this.guaranteeModeName = guaranteeModeName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getNewstatus() {
		return newstatus;
	}

	public void setNewstatus(int newstatus) {
		this.newstatus = newstatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGain() {
		return gain;
	}

	public void setGain(String gain) {
		this.gain = gain;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public String getRepayMethod() {
		return repayMethod;
	}

	public void setRepayMethod(String repayMethod) {
		this.repayMethod = repayMethod;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getDeadlinedesc() {
		return deadlinedesc;
	}

	public void setDeadlinedesc(String deadlinedesc) {
		this.deadlinedesc = deadlinedesc;
	}

	public String getRemainingInvestmentAmount() {
		return remainingInvestmentAmount;
	}

	public void setRemainingInvestmentAmount(String remainingInvestmentAmount) {
		this.remainingInvestmentAmount = remainingInvestmentAmount;
	}

	public String getTotalInvestment() {
		return totalInvestment;
	}

	public void setTotalInvestment(String totalInvestment) {
		this.totalInvestment = totalInvestment;
	}

	public int getActivity() {
		return activity;
	}

	public void setActivity(int activity) {
		this.activity = activity;
	}

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public String getExtraRate() {
		return extraRate;
	}

	public void setExtraRate(String extraRate) {
		this.extraRate = extraRate;
	}

}
