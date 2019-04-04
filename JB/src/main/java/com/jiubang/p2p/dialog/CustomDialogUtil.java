package com.jiubang.p2p.dialog;

import android.content.Context;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jiubang.p2p.R;

public class CustomDialogUtil {

	private android.app.AlertDialog dialog;
	private TextView tv_title;
	private TextView tv_message;
	private TextView tv_cancel;
	private TextView tv_positive;

	public CustomDialogUtil(Context context) {
		dialog = new android.app.AlertDialog.Builder(context).create();
		dialog.show();
		
		// 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_normal_util_layout);

		tv_title = (TextView) window.findViewById(R.id.tv_title);
		tv_message = (TextView) window.findViewById(R.id.tv_message);
		tv_cancel = (TextView) window.findViewById(R.id.tv_cancel);
		tv_positive = (TextView) window.findViewById(R.id.tv_positive);

	}

	public CustomDialogUtil(Context context,int layout){
		dialog = new android.app.AlertDialog.Builder(context).create();
		dialog.show();
		
		Window window = dialog.getWindow();
		window.setContentView(layout);

		tv_title = (TextView) window.findViewById(R.id.tv_title);
		tv_message = (TextView) window.findViewById(R.id.tv_message);
		tv_cancel = (TextView) window.findViewById(R.id.tv_cancel);
		tv_positive = (TextView) window.findViewById(R.id.tv_positive);
	}
	
	public void positiveClickListener(final View.OnClickListener listener) {
		tv_positive.setOnClickListener(listener);
	}

	public void cancelClickListener(final View.OnClickListener listener) {
		tv_cancel.setOnClickListener(listener);
		tv_cancel.setVisibility(View.VISIBLE);
	}

	public void setMessage(int messageId) {
		tv_message.setText(messageId);
	}

	public void setMessage(String message) {
		tv_message.setText(message);
	}
	
	public void setMessage(Spanned spanned) {
		tv_message.setText(spanned);
	}
	
	public void setTitle(int titleId) {
		tv_title.setText(titleId);
	}
	
	public void setTitle(String title) {
		tv_title.setText(title);
	}
	
	public void setPositive(int positiveId) {
		tv_positive.setText(positiveId);
	}
	
	public void setPositive(String positive) {
		tv_positive.setText(positive);
	}

	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		dialog.dismiss();
	}

}
