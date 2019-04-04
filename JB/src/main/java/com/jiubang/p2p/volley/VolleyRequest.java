package com.jiubang.p2p.volley;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.JiubangApplication;

public class VolleyRequest {

	private static StringRequest stringrequest;

	/**
	 * Get
	 * */
	public static void RequestGet(String method, String tag,
			VolleyInterface volleyInterface) {
		JiubangApplication.getHttpQueues().cancelAll(tag);

		stringrequest = new StringRequest(Method.GET, AppConstants.HOST
				+ method, volleyInterface.loadingListener(),
				volleyInterface.errorListener());

		stringrequest.setTag(tag);
		JiubangApplication.getHttpQueues().add(stringrequest);
		JiubangApplication.getHttpQueues().start();
	}

	/**
	 * Post
	 * */
	public static void RequestPost(String method, String tag,
			final Map<String, String> params, VolleyInterface volleyInterface) {
		JiubangApplication.getHttpQueues().cancelAll(tag);

		stringrequest = new StringRequest(Method.POST, AppConstants.HOST
				+ method, volleyInterface.loadingListener(),
				volleyInterface.errorListener()) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return params;
			}
		};

		stringrequest.setTag(tag);
		JiubangApplication.getHttpQueues().add(stringrequest);
		JiubangApplication.getHttpQueues().start();
	}

}
