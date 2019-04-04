package com.jiubang.p2p.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiubang.p2p.R;
import com.jiubang.p2p.JiubangApplication;
import com.jiubang.p2p.volley.LruImageCache;

public class ProductDetailAdapter extends SimpleAdapter {

	private Context context;
	private List<? extends Map<String, ?>> data;

	public ProductDetailAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.context = context;
		this.data = data;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View v = super.getView(position, convertView, parent);

		ImageView iv_image = (ImageView) v.findViewById(R.id.iv_image);
		TextView tv_text = (TextView) v.findViewById(R.id.tv_text);

		ImageLoader imageLoader = new ImageLoader(
				JiubangApplication.getHttpQueues(), LruImageCache.instance());
		ImageListener listener = ImageLoader.getImageListener(iv_image,
				R.drawable.image_default, R.drawable.image_error);
		imageLoader.get(data.get(position).get("image").toString(), listener);

		tv_text.setText("" + data.get(position).get("typeName"));

		return v;
	}

}
