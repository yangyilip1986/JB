package com.jiubang.p2p.receiver;

import com.jiubang.p2p.ui.GestureActivity;
import com.jiubang.p2p.utils.ApplicationUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenStatusReceiver extends BroadcastReceiver{
	
	String SCREEN_ON = "android.intent.action.SCREEN_ON";
	String SCREEN_OFF = "android.intent.action.SCREEN_OFF";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (SCREEN_ON.equals(intent.getAction())) {

		} else if (SCREEN_OFF.equals(intent.getAction())) {
			if (ApplicationUtil.isNeedGesture(context)) {
				context.startActivity(new Intent(context, GestureActivity.class));
			}
		}
	}
}
