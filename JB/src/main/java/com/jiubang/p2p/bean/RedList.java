package com.jiubang.p2p.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RedList {

	private List<Red> list;

	public RedList(JSONArray array) throws JSONException {
		super();
		list = new ArrayList<Red>();
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Red a = new Red();
			JSONObject o = (JSONObject) array.get(i);
			if (o.has("id"))
				a.setId(o.getInt("id"));
			if (o.has("type_flag"))
				a.setType_flag(o.getString("type_flag"));// 卡券类型
			if (o.has("cash_price"))
				a.setCash_price(o.getString("cash_price"));// 卡券金额（加息）
			if (o.has("cash_desc"))
				a.setCash_desc(o.getString("cash_desc"));// 现金券描述
			if (o.has("start_time"))
				a.setStart_time(o.getString("start_time"));// 有效开始时间
			if (o.has("end_time"))
				a.setEnd_time(o.getString("end_time"));// 有效结束时间
			if (o.has("used_time"))
				a.setUsed_time(o.getString("used_time"));// 使用时间
			if (o.has("rate_coupon_send_id"))
				a.setRate_coupon_send_id(o.getString("rate_coupon_send_id"));// 加息券ID可能重复 配合TYPE_FLAG使用
			a.setChecked(false);
			list.add(a);
		}
	}

	public RedList(List<Red> li, JSONArray array) throws JSONException {
		super();
		list = li;
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Red a = new Red();
			JSONObject o = (JSONObject) array.get(i);
			if (o.has("id"))
				a.setId(o.getInt("id"));
			if (o.has("type_flag"))
				a.setType_flag(o.getString("type_flag"));// 卡券类型
			if (o.has("cash_price"))
				a.setCash_price(o.getString("cash_price"));// 卡券金额（加息）
			if (o.has("cash_desc"))
				a.setCash_desc(o.getString("cash_desc"));// 现金券描述
			if (o.has("start_time"))
				a.setStart_time(o.getString("start_time"));// 有效开始时间
			if (o.has("end_time"))
				a.setEnd_time(o.getString("end_time"));// 有效结束时间
			if (o.has("used_time"))
				a.setUsed_time(o.getString("used_time"));// 使用时间
			if (o.has("rate_coupon_send_id"))
				a.setRate_coupon_send_id(o.getString("rate_coupon_send_id"));// 加息券ID可能重复 配合TYPE_FLAG使用
			a.setChecked(false);
			list.add(a);
		}
	}

	public List<Red> getList() {
		return list;
	}

	public void setList(List<Red> list) {
		this.list = list;
	}

}
