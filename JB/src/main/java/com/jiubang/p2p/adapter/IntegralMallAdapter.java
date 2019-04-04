package com.jiubang.p2p.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.JiubangApplication;
import com.jiubang.p2p.dialog.CustomDialog;
import com.jiubang.p2p.utils.ToastCommom;
import com.jiubang.p2p.volley.LruImageCache;

@SuppressLint("NewApi")
public class IntegralMallAdapter extends SimpleAdapter {

	private Context context;
	private List<Map<String, Object>> data;
	private String type;

	private HttpParams params;
	private KJHttp http;

	private String title = "";
	
	private TextView tv_usable_point_m;

	private View v;
	
	public IntegralMallAdapter(Context context, List<Map<String, Object>> data, int resource, String[] from, int[] to, String type, TextView tv_usable_point_m) {
		super(context, data, resource, from, to);
		this.context = context;
		this.data = data;
		this.type = type;
		this.tv_usable_point_m = tv_usable_point_m;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		v = super.getView(position, convertView, parent);

		ImageView iv_image = (ImageView) v.findViewById(R.id.iv_image);
		TextView tv_integral = (TextView) v.findViewById(R.id.tv_integral);
		TextView tv_describe = (TextView) v.findViewById(R.id.tv_describe);
		TextView tv_stock = (TextView) v.findViewById(R.id.tv_stock);

		if (data.get(position).get("img_path").toString().startsWith("http")) {
			ImageLoader imageLoader = new ImageLoader(JiubangApplication.getHttpQueues(), LruImageCache.instance());
			ImageListener listener = ImageLoader.getImageListener(iv_image, R.drawable.image_default, R.drawable.image_error);
			imageLoader.get(data.get(position).get("img_path").toString(), listener);
		}
		
		tv_integral.setText(data.get(position).get("cost_point").toString() + "积分");
		tv_describe.setText("("
				+ data.get(position).get("description").toString() + ")");
		tv_stock.setText("库存" + data.get(position).get("stock").toString()
				+ "个");

		// 库存不为0时
		Button btn = (Button) v.findViewById(R.id.btn_exchange);
		if (!"0".equals(data.get(position).get("stock").toString())) {
			final int p = position;
			btn.setText("我要兑换");
			btn.setBackground(v.getResources().getDrawable(R.drawable.bg_alibuybutton));
			btn.setTextColor(Color.rgb(56, 131, 215));
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					title = data.get(p).get("title").toString();
					
					final CustomDialog dialog = new CustomDialog(context);
					dialog.setMessage("使用", data.get(p).get("cost_point").toString(), "积分兑换", title);
					dialog.positiveClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							params = new HttpParams();
							http = new KJHttp();
							dialog.dismiss();
							getData(data.get(p).get("commodity_id").toString(), data.get(p).get("red_packet_id").toString(), data.get(p).get("cost_point").toString());
						}
					});
					dialog.cancelClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					
				}
			});
		} else {
			btn.setText("已兑完");
			btn.setBackground(v.getResources().getDrawable(R.drawable.bg_alibuybutton_grey));
			btn.setTextColor(Color.rgb(192, 192, 192));
		}
		return v;
	}

	private void getData(String commodity_id, String red_packet_id,
			String cost_point) {
		params.put("sid", AppVariables.sid);
		params.put("commodity_id", commodity_id);
		params.put("red_packet_id", red_packet_id);
		params.put("cost_point", cost_point);
		http.post(AppConstants.MY_INTEGRAL_MALL_EXCHANGE, params, httpCallback);
	}

	private HttpCallBack httpCallback = new HttpCallBack(context) {

		@Override
		public void success(JSONObject ret) {
			super.success(ret);
			try {
				String is_success = ret.getString("is_success");
				String msg = ret.getString("msg");
				
				if ("success".equals(is_success)) {// 兑换成功
					notifyData();
					ToastCommom toastCommom = ToastCommom.createToastConfig();
					toastCommom.ToastShow(context, (ViewGroup) v.findViewById(R.id.toast_layout_root), "您已经获得一张" + title + "\n请到我的卡券中查看");
				} else {
					ToastCommom toastCommom = ToastCommom.createToastConfig();
					toastCommom.ToastShow(context, (ViewGroup) v.findViewById(R.id.toast_layout_root), msg);
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void failure(JSONObject ret) {
			super.failure(ret);
		}
	};

	private void notifyData() {
		params.put("sid", AppVariables.sid);
		params.put("type", type);
		http.post(AppConstants.MY_INTEGRAL_MALL, params, notifyhttpCallback);
	}

	private HttpCallBack notifyhttpCallback = new HttpCallBack(context) {

		@Override
		public void onSuccess(String t) {
			try {
				JSONObject ret = new JSONObject(t);
				JSONArray mallArray = ret.getJSONArray("mallMapList");
				tv_usable_point_m.setText(ret.getString("usable_point_m"));
				data = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < mallArray.length(); i++) {
					JSONObject o = (JSONObject) mallArray.get(i);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("img_path", o.getString("img_path"));
					map.put("cost_point", o.getString("cost_point"));
					map.put("description", o.getString("description"));
					map.put("title", o.getString("title"));
					map.put("cost_money", o.getString("cost_money"));
					map.put("red_packet_id", o.getString("red_packet_id"));
					map.put("commodity_id", o.getString("commodity_id"));
					map.put("stock", o.getInt("stock"));
					data.add(map);
				}

				IntegralMallAdapter.this.notifyDataSetChanged();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void onFinish() {
			super.onFinish();
		}
	};

}
