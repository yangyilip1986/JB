package com.jiubang.p2p;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jiubang.p2p.utils.CrashHandler;

public class JiubangApplication extends Application {

	// 请求队列
	private static RequestQueue requestQueue;

	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());

		requestQueue = Volley.newRequestQueue(getApplicationContext());

	}

	public static RequestQueue getHttpQueues() {
		return requestQueue;
	}

}
