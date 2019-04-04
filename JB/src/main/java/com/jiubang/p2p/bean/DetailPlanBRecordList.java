package com.jiubang.p2p.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailPlanBRecordList {

    private List<DetailRecord> list;

    public DetailPlanBRecordList(JSONArray array) throws JSONException {
        super();
        list = new ArrayList<DetailRecord>();
        int len = array.length();
        for (int i = 0; i < len; i++) {
            DetailRecord a = new DetailRecord();
            JSONObject o = (JSONObject) array.get(i);
            a.setRealName(o.getString("user_name"));
            a.setPrice(o.getString("join_amount"));
            a.setCreateDate(o.getString("join_date"));
            list.add(a);
        }
    }

    public DetailPlanBRecordList(List<DetailRecord> li, JSONArray array) throws JSONException {
        super();
        list = li;
        int len = array.length();
        for (int i = 0; i < len; i++) {
            DetailRecord a = new DetailRecord();
            JSONObject o = (JSONObject) array.get(i);
            a.setRealName(o.getString("user_name"));
            a.setPrice(o.getString("join_amount"));
            a.setCreateDate(o.getString("join_date"));
            list.add(a);
        }
    }

    public List<DetailRecord> getList() {
        return list;
    }

    public void setList(List<DetailRecord> list) {
        this.list = list;
    }

}
