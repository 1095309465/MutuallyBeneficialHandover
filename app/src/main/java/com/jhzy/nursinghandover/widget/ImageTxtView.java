package com.jhzy.nursinghandover.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.utils.ImageLoaderUtils;

/**
 * Created by bigyu on 2017/2/23.
 * 快捷方式的图标
 */
public class ImageTxtView extends LinearLayout {
    private View view;
    private SimpleDraweeView img;
    private TextView txt;

    public ImageTxtView(Context context) {
        super(context);
    }

    public ImageTxtView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ImageTxtView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = LayoutInflater.from(context).inflate(R.layout.widget_image_txt, null);
        img = ((SimpleDraweeView) view.findViewById(R.id.img));
        txt = ((TextView) view.findViewById(R.id.txt));

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageMaskView);
        Drawable drawable = typedArray.getDrawable(R.styleable.ImageMaskView_markIcon);
        String content = typedArray.getString(R.styleable.ImageMaskView_text);

        typedArray.recycle();

        if (drawable != null) {
            img.setBackground(drawable);
        }
        if (!TextUtils.isEmpty(content)){
            txt.setText(content);
        }
        setGravity(Gravity.CENTER_HORIZONTAL);
        addView(view);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        txt.setText(title);
    }


    /**
     * 获取标题
     * @return
     */
    public String getTitle(){
        return txt.getText().toString();
    }

    /**
     * 设置背景
     *
     * @param pic
     */
    public void setImage(int pic) {
        img.setBackgroundResource(pic);
    }


    /**
     * 设置网络加载图片
     * @param url
     */
    public void setUrlImage(String url){
        ImageLoaderUtils.load(img,url);
    }
}
