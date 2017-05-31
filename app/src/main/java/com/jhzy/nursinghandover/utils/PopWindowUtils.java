package com.jhzy.nursinghandover.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.beans.nextItem.SubItem;
import com.jhzy.nursinghandover.ui.adapters.NurseItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxmd on 2017/2/23.
 */

public class PopWindowUtils {

    private NurseItemCallback nurseItemCallback;

    public interface NurseItemCallback {
        void subItem(List<SubItem> subItems);
    }

    /**
     * 服务子项弹出框
     */
    public static View showNurseItemPop(final Context mContext, View view, final Window window, String title, final List<SubItem> subItemList, final NurseItemCallback nurseItemCallback) {
        final List<SubItem> list = new ArrayList<>();
        final View contentView = LayoutInflater.from(mContext).inflate(R.layout.service_item, null);
        TextView titleView = (TextView) contentView.findViewById(R.id.tv_item_name);
        titleView.setText(title);
        final RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recycle);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        final NurseItemAdapter nurseItemAdapter = new NurseItemAdapter(subItemList, mContext);
        nurseItemAdapter.setOnItemClickListener(new NurseItemAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                subItemList.get(position).setSelected(!subItemList.get(position).isSelected());
                nurseItemAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(nurseItemAdapter);
        Button btn_ok = (Button) contentView.findViewById(R.id.btn_ok);
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        int screenHeight = ScreenUtils.getScreenHeight(mContext);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                screenWidth / 2,
                WindowManager.LayoutParams.WRAP_CONTENT, true);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    for (int i = 0; i < subItemList.size(); i++) {
                        SubItem subItem = subItemList.get(i);
                        if (subItem.isSelected()) {
                            list.add(subItem);
                        }
                    }
                    nurseItemCallback.subItem(list);
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.setAnimationStyle(R.style.from_bottom_anim);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams wl = window.getAttributes();
                wl.alpha = 1f;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
                window.setAttributes(wl);
            }
        });
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.alpha = 0.6f;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
        window.setAttributes(wl);
        return contentView;
    }


    /**
     * 显示绑定对话框
     */
    public static void showBindDialog(String content, Context mContext, View view, final Window window, final OnBindListener onBindListener) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_dialog_bind, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        TextView btn_ok = (TextView) contentView.findViewById(R.id.btn_ok);
        TextView btn_cancle = (TextView) contentView.findViewById(R.id.btn_cancle);
        TextView tv_info = (TextView) contentView.findViewById(R.id.tv_info);
        tv_info.setText(content);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (onBindListener != null) {
                    onBindListener.onOkClick();
                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (onBindListener != null) {
                    onBindListener.onNoClick();
                }
            }
        });

        popupWindow.setAnimationStyle(R.style.from_bottom_anim);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.alpha = 0.6f;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
        window.setAttributes(wl);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams wl = window.getAttributes();
                wl.alpha = 1f;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
                window.setAttributes(wl);
            }
        });

    }

    public interface OnBindListener {
        void onOkClick();

        void onNoClick();
    }


}
