package com.louding.frame.http;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.louding.frame.utils.KJLoger;
import com.jiubang.p2p.R;
import com.jiubang.p2p.utils.ApplicationUtil;
import com.jiubang.p2p.utils.ToastCommom;
import com.jiubang.p2p.widget.LoudingDialog;

public abstract class HttpCallBack {

	private Context context;
	private LoudingDialog ld;
	private LoudingDialog ldc;

	public HttpCallBack(Context context) {
		super();
		this.context = context;
	}

	/**
	 * Http请求开始前回调
	 */
	public void onPreStart() {
		if (context != null) {
			ld = new LoudingDialog(context);
			ld.showLouding(R.string.load_ing);
		}
	}

	;

	/**
	 * 进度回调，仅支持Download时使用
	 *
	 * @param count
	 *            总数
	 * @param current
	 *            当前进度
	 */
	public void onLoading(long count, long current) {
	}

	/**
	 * Http请求成功时回调
	 *
	 * @param t
	 */
	public void onSuccess(String t) {
		try {
			JSONObject ret = new JSONObject(t);
			int state = ret.getInt("status");
			if (state != 0) {
				failure(ret);
			} else {
				success(ret);
			}
		} catch (JSONException e) {
			Toast.makeText(context, R.string.app_data_error, Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void failure(JSONObject ret) {
		if (!ret.isNull("msg")) {
			try {
				String msg = ret.getString("msg");
				if (context != null) {
					if (msg.equals("not login")) {
						ApplicationUtil.restartApplication(context);
					} else {
						View view = new View(context);
						ToastCommom toastCommom = ToastCommom.createToastConfig();
						toastCommom.ToastShow(context, (ViewGroup) view.findViewById(R.id.toast_layout_root), msg);
					}
				}
			} catch (JSONException e) {
				Toast.makeText(context, R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(context, R.string.app_exception, Toast.LENGTH_SHORT)
					.show();
		}
		failNext(ret);
	}

	public void failNext(JSONObject ret) {

	}

	public void success(JSONObject ret) {

	}

	/**
	 * Http下载成功时回调
	 */
	public void onSuccess(File f) {
	}

	/**
	 * Http请求失败时回调
	 *
	 * @param t
	 * @param errorNo
	 *            错误码
	 * @param strMsg
	 *            错误原因
	 */
	public void onFailure(Throwable t, int errorNo, String strMsg) {
		if (context != null) {
			Toast.makeText(context, "网络错误，请重试。。。", Toast.LENGTH_SHORT).show();
		}
		KJLoger.debug("Http请求失败。。。" + strMsg);
	}

	/**
	 * Http请求结束后回调
	 */
	public void onFinish() {
		KJLoger.debug("Http请求结束。。。");
		if (ld != null) {
			ld.dismiss();
		}
	}
}
