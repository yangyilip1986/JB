package com.jiubang.p2p.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.JiubangApplication;
import com.jiubang.p2p.R;
import com.jiubang.p2p.adapter.CommonAdapter;
import com.jiubang.p2p.adapter.ViewHolder;
import com.jiubang.p2p.bean.Media;
import com.jiubang.p2p.bean.MediaList;
import com.jiubang.p2p.utils.UIHelper;
import com.jiubang.p2p.volley.LruImageCache;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.widget.KJListView;
import com.louding.frame.widget.KJRefreshListener;

/**
 * 媒体报道 列表
 * */
@SuppressWarnings("deprecation")
public class MediaActivity extends KJActivity {

	@BindView(id = R.id.listview)
	private KJListView listview;

	private KJHttp http;
	private HttpParams params;

	private CommonAdapter<Media> adapter;
	private List<Media> data;

	private int page = 1;
	private boolean noMoreData;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_listview);
		UIHelper.setTitleView(this, "", "媒体报道");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void initData() {
		super.initData();
		data = new ArrayList<Media>();
		http = new KJHttp();
		params = new HttpParams();
		getData(page);
	}

	private void getData(int page) {
		params.put("page", page);
		params.put("sid", "");
		http.post(AppConstants.MEDIA, params, httpCallback);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		adapter = new CommonAdapter<Media>(MediaActivity.this, R.layout.item_media) {
			@Override
			public void click(int id, List<Media> list, int position, ViewHolder viewHolder) {
				switch (id) {
				case R.id.item_media:
					Intent intent = new Intent(MediaActivity.this, MediaContentActivity.class);
					intent.putExtra("title", list.get(position).getTitle());
					intent.putExtra("content", list.get(position).getSummary());
					intent.putExtra("data", list.get(position).getPublish_date());
					intent.putExtra("source", list.get(position).getSource());
					startActivity(intent);
					break;
				}
			}

			@Override
			public void canvas(ViewHolder holder, Media item) {
				holder.addClick(R.id.item_media);
				holder.setText(R.id.tv_title, item.getTitle(), false);
				holder.setText(R.id.tv_data, item.getPublish_date(), false);
				
				ImageView iv_media_image = holder.getView(R.id.iv_media_image);
				ImageLoader imageLoader = new ImageLoader(JiubangApplication.getHttpQueues(), LruImageCache.instance());
				ImageListener listener = ImageLoader.getImageListener(iv_media_image, R.drawable.image_default, R.drawable.image_default);
				imageLoader.get(item.getImg_path(), listener);
			}
		};
		adapter.setList(data);
		listview.setAdapter(adapter);
		listview.setOnRefreshListener(refreshListener);
		listview.setEmptyView(findViewById(R.id.empty));
	}

	private KJRefreshListener refreshListener = new KJRefreshListener() {
		@Override
		public void onRefresh() {
			getData(1);
		}

		@Override
		public void onLoadMore() {
			if (!noMoreData) {
				getData(page + 1);
			}
		}
	};

	private HttpCallBack httpCallback = new HttpCallBack(MediaActivity.this) {
		public void success(org.json.JSONObject ret) {
			try {
				JSONObject articles = ret.getJSONObject("articles");
				page = articles.getInt("currentPage");
				int maxPage = articles.getJSONObject("pager").getInt("maxPage");
				if (page >= maxPage) {
					listview.hideFooter();
					noMoreData = true;
				} else {
					listview.showFooter();
					noMoreData = false;
				}
				if (page == 1) {
					data = new MediaList(articles.getJSONArray("items"))
							.getMedias();
				} else {
					data = new MediaList(data, articles.getJSONArray("items"))
							.getMedias();
				}
				adapter.setList(data);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(MediaActivity.this, R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
			listview.stopRefreshData();
		}
	};

}
