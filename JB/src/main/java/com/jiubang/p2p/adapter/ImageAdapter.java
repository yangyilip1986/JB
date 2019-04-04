package com.jiubang.p2p.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiubang.p2p.R;
import com.jiubang.p2p.JiubangApplication;
import com.jiubang.p2p.utils.ToastCommom;
import com.jiubang.p2p.volley.LruImageCache;

public class ImageAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;

	private List<String> link;

	private Context context;

	public ImageAdapter(Context context, List<String> link) {
		this.layoutInflater = LayoutInflater.from(context);
		this.link = link;
		this.context = context;
	}

	@Override
	public int getCount() {
		return link.size();
	}

	@Override
	public Object getItem(int position) {
		return link.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.item_project_information_image, null);
			holder = new ViewHolder();
			holder.iv_image = (ImageView) convertView
					.findViewById(R.id.iv_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (link.get(position).startsWith("http")) {
			ImageLoader imageLoader = new ImageLoader(
					JiubangApplication.getHttpQueues(),
					LruImageCache.instance());
			ImageListener listener = ImageLoader.getImageListener(holder.iv_image, R.drawable.image_default_02, R.drawable.image_error);
			imageLoader.get(link.get(position), listener);
		} else {
			ToastCommom toastCommom = ToastCommom.createToastConfig();
			toastCommom.ToastShow(context, (ViewGroup) convertView.findViewById(R.id.toast_layout_root), "无效图片地址");
		}

		return convertView;

	}

	public static class ViewHolder {
		public ImageView iv_image;
	}

}