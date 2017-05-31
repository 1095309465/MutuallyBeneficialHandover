package com.jhzy.nursinghandover.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.utils.ImageLoaderUtils;

/**
 * Created by bigyu on 2017/2/23.
 * 点击增加蒙版
 */
public class ImageMaskView extends RelativeLayout {

    private Context mContext;
    private View view;
    private SimpleDraweeView bg; // dix底下的图片
    private ImageView front; //蒙版的图片
    private boolean checkState = false;
    private Drawable markDrawable;


    public ImageMaskView(Context context) {
        this(context, null);
        init(context);
    }


    public ImageMaskView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }


    public ImageMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageMaskView);
        markDrawable = typedArray.getDrawable(R.styleable.ImageMaskView_markIcon);
        if (markDrawable == null) {
            //markDrawable = context.getResources().getDrawable(R.mipmap.xuanz);
        }

        typedArray.recycle();
    }


    private void init(Context context) {
        mContext = context;
        view = LayoutInflater.from(mContext).inflate(R.layout.widget_image_mask, null);

        bg = ((SimpleDraweeView) view.findViewById(R.id.bg));
        front = ((ImageView) view.findViewById(R.id.front));
        front.setImageDrawable(markDrawable);
        addView(view);
    }


    public void setbg(String url) {
        ImageLoaderUtils.load(bg, url);
    }


    /**
     * 设置蒙版是否显示
     */
    public void isCheck(boolean isCheck) {
        checkState = isCheck;
        if (isCheck) {
            front.setVisibility(VISIBLE);
        } else {
            front.setVisibility(GONE);
        }
    }


    /**
     * 获得蒙版显示状态
     */
    public boolean getCheckState() {
        return checkState;
    }


    public void setFrontBg(int bg){
        front.setBackgroundResource(bg);
    }
}
