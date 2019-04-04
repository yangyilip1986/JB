package com.jiubang.p2p.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductList_new {

	private List<Product> products;

	public ProductList_new(JSONArray array) throws JSONException {
		super();
		products = new ArrayList<Product>();
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Product p = new Product();
			JSONObject o = (JSONObject) array.get(i);
			
			p.setId(o.getInt("id"));
			p.setProducts_exp_type(o.getInt("products_exp_type"));// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)
			p.setName(o.getString("products_title"));// 项目标题
			p.setGain(o.getString("finance_interest_rate"));// 年化收益
			p.setExtraRate(o.getString("extra_rate"));// 加息
			p.setDeadline(o.getString("products_term"));// 项目期限
			p.setDeadlinedesc(o.getString("products_unit"));// 项目期限单位
			p.setPercentage(o.getInt("finance_amount_scale"));// 融资进度
			p.setSinglePurchaseLowerLimit(o.getString("min_tender_amount"));// 起投金额
			p.setSinglePurchaseLowerLimit_show(o.getString("min_tender_amount_show"));// 起投金额
			p.setConfine(o.getInt("confine"));
			p.setProduct_type_name(o.getString("product_type_name"));// 活动名称
			p.setActivity(o.getInt("activity"));// 是否加息
			p.setNameInfo(o.getString("activityTab"));// 加息内容 相当于6个汉字
			p.setNewstatus(o.getInt("newstatus"));// 产品状态
			products.add(p);
		}

	}

	public ProductList_new(List<Product> ps, JSONArray array) throws JSONException {
		super();
		products = ps;
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Product p = new Product();
			JSONObject o = (JSONObject) array.get(i);
			
			p.setId(o.getInt("id"));
			p.setProducts_exp_type(o.getInt("products_exp_type"));// 产品类型 0:普通标(一般情况) 1:体验标(仅限体验金) 2:混合标(可用现金和体验金)
			p.setName(o.getString("products_title"));// 项目标题
			p.setGain(o.getString("finance_interest_rate"));// 年化收益
			p.setExtraRate(o.getString("extra_rate"));// 加息
			p.setDeadline(o.getString("products_term"));// 项目期限
			p.setDeadlinedesc(o.getString("products_unit"));// 项目期限单位
			p.setPercentage(o.getInt("finance_amount_scale"));// 融资进度
			p.setSinglePurchaseLowerLimit(o.getString("min_tender_amount"));// 起投金额
			p.setSinglePurchaseLowerLimit_show(o.getString("min_tender_amount_show"));// 起投金额
			p.setConfine(o.getInt("confine"));
			p.setProduct_type_name(o.getString("product_type_name"));// 活动名称
			p.setActivity(o.getInt("activity"));// 是否加息
			p.setNameInfo(o.getString("activityTab"));// 加息内容 相当于6个汉字
			p.setNewstatus(o.getInt("newstatus"));// 产品状态
			products.add(p);
		}
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}
