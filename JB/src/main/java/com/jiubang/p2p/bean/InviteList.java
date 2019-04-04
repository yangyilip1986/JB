package com.jiubang.p2p.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InviteList {

	private List<Invite> list;

	public InviteList(JSONArray array) throws JSONException {
		super();
		list = new ArrayList<Invite>();
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Invite a = new Invite();
			JSONObject o = (JSONObject) array.get(i);
			a.setCreate_time(o.getString("create_time"));
			a.setInv_type(o.getInt("inv_type"));
			a.setRet_status(o.getInt("ret_status"));
			a.setAmount(o.getString("amount"));
			a.setName(o.getString("name"));
			if (o.has("red_packet_amount")) {
				a.setRed_packet_amount(o.getString("red_packet_amount"));
			}
			list.add(a);
		}
	}

	public InviteList(List<Invite> li, JSONArray array) throws JSONException {
		super();
		list = li;
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Invite a = new Invite();
			JSONObject o = (JSONObject) array.get(i);
			a.setCreate_time(o.getString("create_time"));
			a.setInv_type(o.getInt("inv_type"));
			a.setRet_status(o.getInt("ret_status"));
			a.setAmount(o.getString("amount"));
			a.setName(o.getString("name"));
			if (o.has("red_packet_amount")) {
				a.setRed_packet_amount(o.getString("red_packet_amount"));
			}
			list.add(a);
		}
	}

	public List<Invite> getList() {
		return list;
	}

	public void setList(List<Invite> list) {
		this.list = list;
	}

}
