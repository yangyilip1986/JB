package com.jiubang.p2p.bean;

public class Red {

	private int id;// 现金券ID
	private String rate_coupon_send_id;// 加息券ID可能重复 配合TYPE_FLAG使用
	private String type_flag;// 卡券类型
	private String cash_price; // 卡券金额（加息）
	private String cash_desc; // 卡券描述
	private String start_time; // 有效开始时间
	private String end_time; // 有效结束时间
	private String used_time; // 使用时间
	private boolean checked; // 是否被选中

	public int getId() {
		return id;
	}

	public String getRate_coupon_send_id() {
		return rate_coupon_send_id;
	}

	public void setRate_coupon_send_id(String rate_coupon_send_id) {
		this.rate_coupon_send_id = rate_coupon_send_id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType_flag() {
		return type_flag;
	}

	public void setType_flag(String type_flag) {
		this.type_flag = type_flag;
	}

	public String getCash_price() {
		return cash_price;
	}

	public void setCash_price(String cash_price) {
		this.cash_price = cash_price;
	}

	public String getCash_desc() {
		return cash_desc;
	}

	public void setCash_desc(String cash_desc) {
		this.cash_desc = cash_desc;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getUsed_time() {
		return used_time;
	}

	public void setUsed_time(String used_time) {
		this.used_time = used_time;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
