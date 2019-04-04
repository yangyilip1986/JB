package com.jiubang.p2p.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubang.p2p.R;

public class ToastCommom {

	private static ToastCommom toastCommom;

	private Toast toast;

	private ToastCommom() {
	}

	public static ToastCommom createToastConfig() {
		if (toastCommom == null) {
			toastCommom = new ToastCommom();
		}
		return toastCommom;
	}

	/**
	 * 显示Toast
	 */
	public void ToastShow(Context context, ViewGroup root, String tvString) {
		if (toast != null) {
			toast.cancel();
		}
		View layout = LayoutInflater.from(context).inflate(R.layout.toast, root);
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(tvString);
		toast = new Toast(context);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}
}
