package com.jiubang.p2p.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.jiubang.p2p.R;
  
@SuppressLint("InflateParams")
public class SelectPicPopupWindow extends PopupWindow {  
  
    private LinearLayout ll_wechat, ll_wechat_moments;  
    private View menuView;
  
    @SuppressWarnings("deprecation")
	public SelectPicPopupWindow(Activity context,OnClickListener itemsOnClick) {
        super(context);  
        
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        menuView = inflater.inflate(R.layout.share_alert_dialog, null);  
        ll_wechat = (LinearLayout) menuView.findViewById(R.id.ll_wechat);  
        ll_wechat_moments = (LinearLayout) menuView.findViewById(R.id.ll_wechat_moments);  
        //设置按钮监听  
        ll_wechat.setOnClickListener(itemsOnClick);
        ll_wechat_moments.setOnClickListener(itemsOnClick);  
        //设置SelectPicPopupWindow的View
        this.setContentView(menuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //menuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        menuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = menuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP) {
                    if(y<height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
