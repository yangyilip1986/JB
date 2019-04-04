package com.jiubang.p2p.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.jiubang.p2p.R;
import com.jiubang.p2p.ui.AdActivity;
import com.jiubang.p2p.utils.HttpHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

@SuppressLint("HandlerLeak")
public class SlideShowView extends FrameLayout {
	private final String TAG = "SlideShowView";
	// 使用universal-image-loader插件读取网络图片，需要工程导入universal-image-loader.jar
	private ImageLoader imageLoader = ImageLoader.getInstance();

	// 轮播图图片数量
//	private final static int IMAGE_COUNT = 5;
	// 自动轮播的时间间隔
//	private final static int TIME_INTERVAL = 5;
	// 自动轮播启用开关
	private final static boolean isAutoPlay = true;

	// 自定义轮播图的资源
	private ArrayList<String> imageUrls = new ArrayList<String>();
	private ArrayList<String> imageToUrls = new ArrayList<String>();
	// 放轮播图片的ImageView 的list
	private List<ImageView> imageViewsList;
	// 放圆点的View的list
	private List<View> dotViewsList;

	private ViewPager viewPager;
	// 当前轮播页
	private int currentItem = 0;
	// 定时任务
	private ScheduledExecutorService scheduledExecutorService;

	private Context context;
	
	private DisplayImageOptions options;

	// Handler
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			viewPager.setCurrentItem(currentItem);
		}
	};

	public SlideShowView(Context context) {
		this(context, null);
	}

	public SlideShowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initData();
		if (isAutoPlay) {
			startPlay();
		}
		if (imageUrls.size() == 0) {
			this.setVisibility(View.GONE);
		} else {
			this.setVisibility(View.VISIBLE);
		}
		
		options = new DisplayImageOptions.Builder()  
//		.showImageOnLoading(R.drawable.ic_launcher) //设置图片在下载期间显示的图片  
		.showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片  
		.showImageOnFail(R.drawable.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中  
		.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中  
//		.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示  
//		.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//  
//		.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置  
		//.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
		//设置图片加入缓存前，对bitmap进行设置  
		//.preProcessor(BitmapProcessor preProcessor)  
		.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
//		.displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少  
//		.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间  
		.build();//构建完成
	}

	/**
	 * 开始轮播图切换
	 */
	private void startPlay() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4,
				TimeUnit.SECONDS);
	}

//	/**
//	 * 停止轮播图切换
//	 */
//	private void stopPlay() {
//		scheduledExecutorService.shutdown();
//	}

	/**
	 * 初始化相关Data
	 */
	private void initData() {
		imageViewsList = new ArrayList<ImageView>();
		dotViewsList = new ArrayList<View>();

		// 异步任务获取图片
		new GetListTask().execute("");
	}

	/**
	 * 初始化Views等UI
	 */
	private void initUI(Context context) {
		if (imageUrls == null || imageUrls.size() == 0)
			return;
		LayoutInflater.from(context).inflate(R.layout.slide_show_view, this,
				true);

		LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
		dotLayout.removeAllViews();

		// 热点个数与图片特殊相等
		for (int i = 0; i < imageUrls.size(); i++) {
			ImageView view = new ImageView(context);
			view.setTag(imageUrls.get(i));
			view.setScaleType(ScaleType.FIT_XY);
			imageViewsList.add(view);

			ImageView dotView = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 4;
			params.rightMargin = 4;
			dotLayout.addView(dotView, params);
			dotViewsList.add(dotView);
		}

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setFocusable(true);

		viewPager.setAdapter(new MyPagerAdapter());
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		SlideShowView.this.setVisibility(View.VISIBLE);
	}

	/**
	 * 填充ViewPager的页面适配器
	 */
	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(imageViewsList.get(position));
		}

		@Override
		public Object instantiateItem(View container, final int position) {
			ImageView imageView = imageViewsList.get(position);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(context, AdActivity.class);
					intent.putExtra("url", imageToUrls.get(position));
					context.startActivity(intent);
				}
			});
			
			imageLoader.init(ImageLoaderConfiguration.createDefault(context));
			imageLoader.displayImage(imageView.getTag() + "", imageView , options);
//			imageLoader.displayImage(imageView.getTag() + "", imageView);

			((ViewPager) container).addView(imageViewsList.get(position));
			return imageViewsList.get(position);
		}

		@Override
		public int getCount() {
			return imageViewsList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}
	}

	/**
	 * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
	 *
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		boolean isAutoPlay = false;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
			case 1:// 手势滑动，空闲中
				isAutoPlay = false;
				break;
			case 2:// 界面切换中
				isAutoPlay = true;
				break;
			case 0:// 滑动结束，即切换完毕或者加载完毕
					// 当前为最后一张，此时从右向左滑，则切换到第一张
				if (viewPager.getCurrentItem() == viewPager.getAdapter()
						.getCount() - 1 && !isAutoPlay) {
					viewPager.setCurrentItem(0);
				}
				// 当前为第一张，此时从左向右滑，则切换到最后一张
				else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
					viewPager
							.setCurrentItem(viewPager.getAdapter().getCount() - 1);
				}
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int pos) {
			currentItem = pos;
			for (int i = 0; i < dotViewsList.size(); i++) {
				if (i == pos) {
					((View) dotViewsList.get(pos))
							.setBackgroundResource(R.drawable.dot_blue);
				} else {
					((View) dotViewsList.get(i))
							.setBackgroundResource(R.drawable.dot_white);
				}
			}
		}
	}

	/**
	 * 执行轮播图切换任务
	 */
	private class SlideShowTask implements Runnable {

		@Override
		public void run() {
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % imageViewsList.size();
				handler.obtainMessage().sendToTarget();
			}
		}
	}

//	/**
//	 * 销毁ImageView资源，回收内存
//	 */
//	private void destoryBitmaps() {
//
//		for (int i = 0; i < IMAGE_COUNT; i++) {
//			ImageView imageView = imageViewsList.get(i);
//			Drawable drawable = imageView.getDrawable();
//			if (drawable != null) {
//				// 解除drawable对view的引用
//				drawable.setCallback(null);
//			}
//		}
//	}

	/**
	 * 异步任务,获取数据
	 */
	class GetListTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				JSONObject jo = new HttpHelper().getSlideView();
				JSONArray ja = jo.getJSONArray("data");
				Log.e(TAG, "jo: "+jo );
				Log.e(TAG, "ja: "+ja );
				for (int i = 0; i < ja.length(); i++) {
					imageUrls.add(ja.getJSONObject(i).getJSONObject("extra")
							.getString("img"));
					Log.e(TAG, "doInBackground: "+ ja.getJSONObject(i).getJSONObject("extra")
							.getString("img"));
					imageToUrls.add(ja.getJSONObject(i).getJSONObject("extra")
							.getString("url"));
				}
				for (int i = 0; i < imageUrls.size(); i++) {
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				initUI(context);
			}
		}
	}
}
