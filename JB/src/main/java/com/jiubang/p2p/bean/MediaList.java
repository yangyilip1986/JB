package com.jiubang.p2p.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MediaList {
	private List<Media> medias;

	public MediaList(JSONArray array) throws JSONException {
		super();
		medias = new ArrayList<Media>();
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Media media = new Media();
			JSONObject o = (JSONObject) array.get(i);
			media.setAuthor(o.getString("author"));
			media.setSummary(o.getString("summary"));
			media.setName(o.getString("name"));
			media.setNews_type_id(o.getString("news_type_id"));
			media.setImg_path(o.getString("img_path"));
			media.setStatus(o.getString("status"));
			media.setPublish_date(o.getString("publish_date"));
			media.setSource(o.getString("source"));
			media.setLink_url(o.getString("link_url"));
			media.setId(o.getString("id"));
			media.setShow_type_id(o.getString("show_type_id"));
			media.setTitle(o.getString("title"));
			medias.add(media);
		}
	}

	public MediaList(List<Media> la, JSONArray array) throws JSONException {
		super();
		medias = la;
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Media media = new Media();
			JSONObject o = (JSONObject) array.get(i);
			media.setAuthor(o.getString("author"));
			media.setSummary(o.getString("summary"));
			media.setName(o.getString("name"));
			media.setNews_type_id(o.getString("news_type_id"));
			media.setImg_path(o.getString("img_path"));
			media.setStatus(o.getString("status"));
			media.setPublish_date(o.getString("publish_date"));
			media.setSource(o.getString("source"));
			media.setLink_url(o.getString("link_url"));
			media.setId(o.getString("id"));
			media.setShow_type_id(o.getString("show_type_id"));
			media.setTitle(o.getString("title"));
			medias.add(media);
		}
	}

	public List<Media> getMedias() {
		return medias;
	}

	public void setMedias(List<Media> medias) {
		this.medias = medias;
	}

}
