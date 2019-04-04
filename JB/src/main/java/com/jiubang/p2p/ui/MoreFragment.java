package com.jiubang.p2p.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;
import com.jiubang.p2p.R;
import com.jiubang.p2p.bean.Update;
import com.jiubang.p2p.dialog.CustomDialogUtil;
import com.jiubang.p2p.utils.ApplicationUtil;
import com.jiubang.p2p.utils.UpdateManager;
import com.jiubang.p2p.utils.UpdateManager.CheckVersionInterface;
import com.jiubang.p2p.utils.UpdateManager.OnCheckDoneListener;

public class MoreFragment extends Fragment implements CheckVersionInterface {

	private TextView version;
	private RelativeLayout rl_anno;
	private RelativeLayout share;
	private RelativeLayout update;
	private RelativeLayout aboutus;
	private RelativeLayout help;
	private RelativeLayout comment;
	private View v;

	private KJHttp http;
	private UpdateManager updateManager;
	private Update u;
	private JSONObject versionInfo;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_more, null);
		updateManager = UpdateManager.getUpdateManager();
		http = new KJHttp();
		initView();
		initData();
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();

		isSignin();
	}

	private void initView() {
		version = (TextView) v.findViewById(R.id.version);
		rl_anno = (RelativeLayout) v.findViewById(R.id.rl_anno);
		rl_anno.setOnClickListener(listener);
		share = (RelativeLayout) v.findViewById(R.id.share);
		share.setOnClickListener(listener);
		update = (RelativeLayout) v.findViewById(R.id.update);
		update.setOnClickListener(listener);
		aboutus = (RelativeLayout) v.findViewById(R.id.aboutus);
		aboutus.setOnClickListener(listener);
		help = (RelativeLayout) v.findViewById(R.id.help);
		help.setOnClickListener(listener);
		comment = (RelativeLayout) v.findViewById(R.id.comment);
		comment.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rl_anno:
				startActivity(new Intent(getActivity(), AnnoActivity.class));
				break;
			case R.id.share:
				if (!AppVariables.isSignin) {
					startActivity(new Intent(getActivity(),
							SigninActivity.class));
				} else {
					getLevel();
				}
				break;
			case R.id.update:
				http = new KJHttp();
				HttpParams params = new HttpParams();
				params.put("sid", AppVariables.sid);
				http.post(AppConstants.UPDATE, params, new HttpCallBack(
						getActivity()) {
					public void onPreStart() {
					};

					public void onFinish() {
					};

					public void onSuccess(String t) {
						try {
							versionInfo = new JSONObject(t);
							checkUpdate();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					};
				});
				break;
			case R.id.aboutus:
				startActivity(new Intent(getActivity(), AboutActivity.class));
				break;
			case R.id.help:
				startActivity(new Intent(getActivity(), HelpCenterActivity.class));
				break;
			case R.id.comment:
				final CustomDialogUtil dialog = new CustomDialogUtil(getActivity());
				dialog.setTitle("温馨提示");
				dialog.setMessage("确定拨打电话：400-185-0818吗？");
				dialog.positiveClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4001850818"));
						startActivity(intent);
					}
				});
				dialog.cancelClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				break;
			}
		}
	};

	private void checkUpdate() {
		updateManager.checkAppUpdate(getActivity(), true, this);
		updateManager.setOnCheckDoneListener(new OnCheckDoneListener() {
			@Override
			public void onCheckDone() {
			}
		});
	}

	private void initData() {
		version.setText(ApplicationUtil.getApkInfo(getActivity()).versionName);
	}

	private void getLevel() {
		HttpParams params = new HttpParams();
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.INVITE, params, new HttpCallBack(getActivity()) {
			@Override
			public void onSuccess(String t) {
				try {
					JSONObject ret = new JSONObject(t);
					int uid_level = ret.getInt("uid_level");
					if (uid_level == 2) {
						Intent intent = new Intent(getActivity(), YanInviteActivity.class);
						intent.putExtra("activity", "more");
						startActivity(intent);
					} else {
						Intent intent = new Intent(getActivity(), NormalInviteActivity.class);
						intent.putExtra("activity", "more");
						startActivity(intent);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public Update checkVersion() throws Exception {
		try {
			u = new Update(versionInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return u;
	}

	private void isSignin() {
		KJHttp kjh = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("sid", AppVariables.sid);
		kjh.post(AppConstants.ISSIGNIN, params,
				new HttpCallBack(getActivity()) {
					@Override
					public void onSuccess(String t) {
						try {
							JSONObject ret = new JSONObject(t);
							JSONObject o = ret.getJSONObject("body");
							AppVariables.isSignin = o.getBoolean("isLogin");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

}
