package com.jiubang.p2p.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProjectList {

	private List<Project> list;

	public ProjectList(JSONArray array) throws JSONException {
		super();
		list = new ArrayList<Project>();
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Project a = new Project();
			JSONObject o = (JSONObject) array.get(i);
			a.setProducts_title(o.getString("products_title"));
			a.setSum_recover_amount(o.getString("sum_recover_amount"));
			a.setRecover_amount_capital_yes(o
					.getString("recover_amount_capital_yes"));
			a.setRecover_amount_interest_yes(o
					.getString("recover_amount_interest_yes"));
			list.add(a);
		}
	}

	public ProjectList(List<Project> li, JSONArray array) throws JSONException {
		super();
		list = li;
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Project a = new Project();
			JSONObject o = (JSONObject) array.get(i);
			a.setProducts_title(o.getString("products_title"));
			a.setSum_recover_amount(o.getString("sum_recover_amount"));
			a.setRecover_amount_capital_yes(o
					.getString("recover_amount_capital_yes"));
			a.setRecover_amount_interest_yes(o
					.getString("recover_amount_interest_yes"));
			list.add(a);
		}
	}

	public List<Project> getList() {
		return list;
	}

	public void setList(List<Project> list) {
		this.list = list;
	}

}
