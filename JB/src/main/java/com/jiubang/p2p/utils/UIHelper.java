package com.jiubang.p2p.utils;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiubang.p2p.R;

public class UIHelper {

	/**
	 * 设置titleView的通用方法
	 *
	 * @param atv
	 *            activity对象
	 * @param textContent
	 *            中间TextView的文字
	 */
	public static void setTitleView(final Activity atv, String back, String textContent) {
		TextView btnLeft = null;
		TextView titleTv = null;
		ImageView titleImage = null;
		btnLeft = (TextView) atv.findViewById(R.id.title_left);
		titleTv = (TextView) atv.findViewById(R.id.title_center);
		titleImage = (ImageView) atv.findViewById(R.id.title_image);
		btnLeft.setText(back);
		titleImage.setVisibility(View.GONE);
		btnLeft.setVisibility(View.VISIBLE);
		titleTv.setVisibility(View.VISIBLE);
		titleTv.setText(textContent);
		btnLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				atv.finish();
			}
		});
	}

}
