package com.jhzy.nursinghandover.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jhzy.nursinghandover.R;

/**
 * Created by nakisaRen
 * on 17/3/28.
 */

public class DotTextView extends LinearLayout {
    private Context context;
    private View view;
    private ImageView dot;
    private TextView tv;


    public DotTextView(Context context) {
        this(context,null);
    }


    public DotTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.dotview,null);
        dot = ((ImageView) view.findViewById(R.id.dot_imageview));
        tv = ((TextView) view.findViewById(R.id.dot_textview));
        addView(view);
    }

    public void setContent(String text){
        if(TextUtils.isEmpty(text)){
            this.setVisibility(GONE);
        }else{
            tv.setText(text);
            this.setVisibility(VISIBLE);
        }
    }
}
