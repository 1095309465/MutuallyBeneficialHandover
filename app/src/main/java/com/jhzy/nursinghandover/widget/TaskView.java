package com.jhzy.nursinghandover.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.utils.ImageLoaderUtils;

/**
 * Created by Administrator on 2017/3/27.
 */

public class TaskView extends LinearLayout  {
    private Context mContext;
    private View view;
    private TextView title;
    private View bg;
    private SimpleDraweeView icon;

    private boolean isSelected = false;

    public TaskView(Context context) {
        super(context);
        init(context);
    }

    public TaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        view = LayoutInflater.from(mContext).inflate(R.layout.widget_task, null);
        title = ((TextView) view.findViewById(R.id.title));
        bg = view.findViewById(R.id.bg);
        icon = ((SimpleDraweeView) view.findViewById(R.id.icon));

        addView(view);
    }


    private void changeSelected() {
        if (isSelected) {
            bg.setVisibility(VISIBLE);
        } else {
            bg.setVisibility(GONE);
        }
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        changeSelected();
    }

    public boolean getSelected() {
        return isSelected;
    }


    /**
     * 设置背景
     *
     * @param url
     */
    public void setImg(String url) {
        ImageLoaderUtils.load(icon, url);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

}
