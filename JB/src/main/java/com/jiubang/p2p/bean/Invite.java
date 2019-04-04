package com.jiubang.p2p.bean;

public class Invite {
	private int inv_type;
	private int ret_status;
	private String amount;
	private String create_time;
	private String name;
	private String red_packet_amount;

	public String getRed_packet_amount() {
		return red_packet_amount;
	}

	public void setRed_packet_amount(String red_packet_amount) {
		this.red_packet_amount = red_packet_amount;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public int getInv_type() {
		return inv_type;
	}

	public void setInv_type(int inv_type) {
		this.inv_type = inv_type;
	}

	public int getRet_status() {
		return ret_status;
	}

	public void setRet_status(int ret_status) {
		this.ret_status = ret_status;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCreate_time() {
		return create_time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
