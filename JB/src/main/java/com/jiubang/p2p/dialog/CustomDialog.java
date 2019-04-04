package com.jiubang.p2p.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jiubang.p2p.R;

public class CustomDialog {

	private android.app.AlertDialog dialog;
	private TextView tv_message1;
	private TextView tv_message2;
	private TextView tv_message3;
	private TextView tv_message4;
	private TextView tv_cancel;
	private TextView tv_positive;

	public CustomDialog(Context context) {

		dialog = new android.app.AlertDialog.Builder(context).create();
		dialog.show();
		// 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_normal_layout);

		tv_message1 = (TextView) window.findViewById(R.id.tv_message1);
		tv_message2 = (TextView) window.findViewById(R.id.tv_message2);
		tv_message3 = (TextView) window.findViewById(R.id.tv_message3);
		tv_message4 = (TextView) window.findViewById(R.id.tv_message4);
		tv_cancel = (TextView) window.findViewById(R.id.tv_cancel);
		tv_positive = (TextView) window.findViewById(R.id.tv_positive);

	}

	public void positiveClickListener(final View.OnClickListener listener) {
		tv_positive.setOnClickListener(listener);
	}

	public void cancelClickListener(final View.OnClickListener listener) {
		tv_cancel.setOnClickListener(listener);
	}

	public void setMessage(int messageId1, int messageId2, int messageId3,
			int messageId4) {
		tv_message1.setText(messageId1);
		tv_message2.setText(messageId2);
		tv_message3.setText(messageId3);
		tv_message4.setText(messageId4);
	}

	public void setMessage(String message1, String message2, String message3,
			String message4) {
		tv_message1.setText(message1);
		tv_message2.setText(message2);
		tv_message3.setText(message3);
		tv_message4.setText(message4);
	}

	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		dialog.dismiss();
	}

}
