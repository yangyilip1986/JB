package com.jiubang.p2p.volley;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public abstract class VolleyInterface {

	public static Listener<String> listener;
	public static ErrorListener errorListener;

	public abstract void onMySuccess(String result);

	public abstract void onMyError(VolleyError error);

	/**
	 * 监听成功
	 * */
	public Listener<String> loadingListener() {
		listener = new Listener<String>() {

			@Override
			public void onResponse(String response) {
				onMySuccess(response);
			}
		};
		return listener;
	}

	/**
	 * 监听失败
	 * */
	public ErrorListener errorListener() {
		errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				onMyError(error);
			}
		};
		return errorListener;
	}
	
	
}
